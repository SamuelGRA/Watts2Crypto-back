param(
    [string]$ReleaseTag = 'snapshot-latest',
    [string]$OutputDir = (Join-Path $PSScriptRoot '..\data\snapshots'),
    [string]$ApiBaseUrl = 'http://localhost:8080',
    [string]$RepositorySlug = 'SamuelGRA/Watts2Crypto-back'
)

$ErrorActionPreference = 'Stop'

chcp 65001 > $null
$utf8Encoding = New-Object System.Text.UTF8Encoding($false)
[Console]::OutputEncoding = $utf8Encoding
[Console]::InputEncoding = $utf8Encoding
$OutputEncoding = $utf8Encoding
$PSDefaultParameterValues['*:Encoding'] = 'utf8'

function Resolve-RepositorySlug {
    param([string]$ExplicitSlug)

    if ($ExplicitSlug) {
        return $ExplicitSlug
    }

    $remoteUrl = git config --get remote.origin.url
    if (-not $remoteUrl) {
        throw 'No se ha podido detectar el repositorio remoto. Pasa -RepositorySlug manualmente.'
    }

    if ($remoteUrl -match 'github\.com[:/](?<slug>[^/]+/[^/]+?)(?:\.git)?$') {
        return $Matches.slug
    }

    throw "No se ha podido resolver el repositorio desde el remoto: $remoteUrl"
}

function Get-LatestSnapshotDownloadUrl {
    param(
        [string]$Tag,
        [string]$Repository
    )

    $releaseApiUrl = "https://api.github.com/repos/$Repository/releases/tags/$Tag"
    $release = Invoke-RestMethod -Uri $releaseApiUrl -Headers @{
        'User-Agent' = 'Watts2Crypto-Snapshot-Importer'
        'Accept' = 'application/vnd.github+json'
    }

    $asset = $release.assets | Where-Object { $_.name -like 'watts2crypto-snapshot-*.sql' } | Select-Object -First 1
    if (-not $asset) {
        throw "No se ha encontrado una snapshot en la release $Tag."
    }

    [pscustomobject]@{
        Name = $asset.name
        Url  = $asset.browser_download_url
    }
}

function Invoke-SnapshotImport {
    param(
        [string]$ImportUrl,
        [string]$SnapshotDestination
    )

    try {
        Add-Type -AssemblyName System.Net.Http

        $fileName = [System.IO.Path]::GetFileName($SnapshotDestination)
        $fileBytes = [System.IO.File]::ReadAllBytes($SnapshotDestination)

        $content = New-Object System.Net.Http.MultipartFormDataContent
        $fileContent = New-Object System.Net.Http.ByteArrayContent(,$fileBytes)
        $fileContent.Headers.ContentType = [System.Net.Http.Headers.MediaTypeHeaderValue]::Parse('application/octet-stream')
        $content.Add($fileContent, 'file', $fileName)

        $client = New-Object System.Net.Http.HttpClient
        $client.DefaultRequestHeaders.Accept.Clear()
        $client.DefaultRequestHeaders.Accept.Add(
            [System.Net.Http.Headers.MediaTypeWithQualityHeaderValue]::new('application/json')
        )

        $result = $client.PostAsync($ImportUrl, $content).GetAwaiter().GetResult()
        $body = $result.Content.ReadAsStringAsync().GetAwaiter().GetResult()

        if (-not $result.IsSuccessStatusCode) {
            return [pscustomobject]@{
                Success          = $false
                ConnectionFailed = $false
                Response         = $body
                StatusCode       = [int]$result.StatusCode
            }
        }

        return [pscustomobject]@{
            Success          = $true
            ConnectionFailed = $false
            Response         = $body
            StatusCode       = [int]$result.StatusCode
        }
    }
    catch [System.Net.WebException] {
        return [pscustomobject]@{
            Success          = $false
            ConnectionFailed = $true
            Response         = $null
            StatusCode       = $null
        }
    }
    catch [System.Net.Http.HttpRequestException] {
        return [pscustomobject]@{
            Success          = $false
            ConnectionFailed = $true
            Response         = $null
            StatusCode       = $null
        }
    }
}

function Start-LocalDockerCompose {
    $backendRoot = Join-Path $PSScriptRoot '..'
    Push-Location $backendRoot
    try {
        & docker compose up -d --build
        if ($LASTEXITCODE -ne 0) {
            throw 'No se pudo ejecutar docker compose up -d --build. Comprueba que Docker Desktop está ejecutándose si estás en Windows.'
        }
    }
    finally {
        Pop-Location
    }
}

$RepositorySlug = Resolve-RepositorySlug -ExplicitSlug $RepositorySlug
New-Item -ItemType Directory -Force -Path $OutputDir | Out-Null

$snapshot = Get-LatestSnapshotDownloadUrl -Tag $ReleaseTag -Repository $RepositorySlug
$destination = Join-Path $OutputDir $snapshot.Name

Invoke-WebRequest -Uri $snapshot.Url -Headers @{
    'User-Agent' = 'Watts2Crypto-Snapshot-Importer'
    'Accept' = 'application/octet-stream'
} -OutFile $destination

Write-Host "Snapshot descargada en $destination"
Write-Host 'Importando snapshot en la base de datos local...'

$importUrl = "$ApiBaseUrl/api/snapshot/import"
$result = Invoke-SnapshotImport -ImportUrl $importUrl -SnapshotDestination $destination

if (-not $result.Success) {
    if ($result.ConnectionFailed) {
        Write-Warning 'La snapshot se ha descargado en data/snapshots, pero la app no estaba ejecutándose.'
        $decision = Read-Host '¿Quieres arrancar la app (esto ejecutará docker compose up -d --build) y reintentar la importación? (S/n)'
        if ($decision -match '^(n|no)$') {
            Write-Warning 'No se ha levantado la app. La snapshot queda descargada en data/snapshots y puedes importarla más tarde ejecutando este script.'
            exit 0
        }

        Start-LocalDockerCompose
        Write-Host 'Docker Compose se ha levantado. Reintentando la importación de la snapshot...'

        $retries = 1
        $maxRetries = 10
        while ($retries -le $maxRetries) {
            Start-Sleep -Seconds 3
            $result = Invoke-SnapshotImport -ImportUrl $importUrl -SnapshotDestination $destination

            if ($result.Success) {
                Write-Host $result.Response
                exit 0
            }

            if (-not $result.ConnectionFailed) {
                break
            }

            $retries++
        }

        throw 'No se pudo importar la snapshot.'
    }

    throw "No se pudo importar la snapshot en el backend local. Código HTTP: $($result.StatusCode). Respuesta: $($result.Response)"
}

Write-Host $result.Response