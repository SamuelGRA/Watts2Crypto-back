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
$response = & curl.exe -sS -X POST $importUrl -H 'Accept: application/json' -F "file=@$destination"

if ($LASTEXITCODE -ne 0) {
    throw 'No se pudo importar la snapshot en el backend local.'
}

Write-Host $response