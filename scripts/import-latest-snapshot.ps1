param(
    [string]$ReleaseTag = 'snapshot-latest',
    [string]$OutputDir = (Join-Path $PSScriptRoot '..\data\snapshots'),
    [string]$ApiBaseUrl = 'http://localhost:8080',
    [string]$RepositorySlug = 'SamuelGRA/Watts2Crypto-back'
)

$ErrorActionPreference = 'Stop'

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

    return & curl.exe -sS -X POST $ImportUrl -H 'Accept: application/json' -F "file=@$SnapshotDestination"
}

function Start-LocalDockerCompose {
    $backendRoot = Join-Path $PSScriptRoot '..'
    Push-Location $backendRoot
    try {
        & docker compose up -d --build
        if ($LASTEXITCODE -ne 0) {
            throw 'No se pudo ejecutar docker compose up -d --build. Comprueba que docker desktop está ejecutándose si estás en Windows'
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
$response = Invoke-SnapshotImport -ImportUrl $importUrl -SnapshotDestination $destination

if ($LASTEXITCODE -ne 0) {
    if ($LASTEXITCODE -eq 7) {
        Write-Warning 'La snapshot se ha descargado en data/snapshots, pero la app no estaba ejecutándose.'
        $decision = Read-Host '¿Quieres arrancar la app (esto ejecutará docker compose up -d --build) y reintentar la importación? (S/n)'
        if ($decision -match '^(n|no)$') {
            Write-Warning 'No se ha levantado la app. La snapshot queda descargada en data/snapshots y puedes importarla más tarde ejecutando este script.'
            exit 0
        }

        Start-LocalDockerCompose
        Write-Host 'Docker Compose se ha levantado. Reintentando la importación de la snapshot...'

        $retries = 1
        $maxRetries = 5
        while ($retries -le $maxRetries) {
            Start-Sleep -Seconds 2
            $response = Invoke-SnapshotImport -ImportUrl $importUrl -SnapshotDestination $destination
            if ($LASTEXITCODE -eq 0) {
                Write-Host $response
                exit 0
            }

            if ($LASTEXITCODE -ne 7) {
                break
            }

            $retries++
        }

        throw 'No se pudo importar la snapshot.'
    }

    throw 'No se pudo importar la snapshot en el backend local.'
}

Write-Host $response