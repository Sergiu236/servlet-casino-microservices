$ErrorActionPreference = 'Stop'

$service = 'games-service'
$war = 'games.war'
$server = 'wildfly'

$deployDir = Split-Path $PSScriptRoot -Parent
$serviceDir = (Resolve-Path (Join-Path $deployDir "..\services\$service")).Path
$serverDir = Join-Path $deployDir $server

Push-Location $serviceDir
try {
    & mvn clean package
    if ($LASTEXITCODE -ne 0) { throw "Maven build failed for $service" }
}
finally {
    Pop-Location
}

Remove-Item (Join-Path $serverDir $war) -Force -ErrorAction SilentlyContinue
Copy-Item (Join-Path $serviceDir "target\$war") $serverDir

Write-Host "$war copied to $serverDir"
