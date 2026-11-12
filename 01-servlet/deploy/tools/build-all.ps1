$ErrorActionPreference = 'Stop'

$scripts = 'build-wallet.ps1', 'build-games.ps1', 'build-ui.ps1'

foreach ($script in $scripts) {
    Write-Host "=== $script ==="
    & (Join-Path $PSScriptRoot $script)
}

Write-Host 'All WARs built and copied.'
