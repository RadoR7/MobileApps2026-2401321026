$ErrorActionPreference = 'Stop'
$Root = Split-Path -Parent $MyInvocation.MyCommand.Path
$Jar = Join-Path $Root 'gradle\wrapper\gradle-wrapper.jar'
$Temp = "$Jar.official.download"
$Expected = '497c8c2a7e5031f6aa847f88104aa80a93532ec32ee17bdb8d1d2f67a194a9c7'
$Url = 'https://raw.githubusercontent.com/gradle/gradle/v9.6.0/gradle/wrapper/gradle-wrapper.jar'
Write-Host 'Downloading official Gradle 9.6 wrapper from the Gradle GitHub repository...'
try {
    Invoke-WebRequest -Uri $Url -OutFile $Temp -UseBasicParsing
    $Actual = (Get-FileHash -Path $Temp -Algorithm SHA256).Hash.ToLowerInvariant()
    if ($Actual -ne $Expected) {
        throw "Downloaded JAR failed official SHA-256 verification. Expected $Expected; actual $Actual"
    }
    Move-Item -LiteralPath $Temp -Destination $Jar -Force
    Write-Host 'SUCCESS: Official, checksum-verified Gradle wrapper installed.' -ForegroundColor Green
    Write-Host 'You can now open this FitTrack folder in Android Studio.'
} catch {
    Remove-Item -LiteralPath $Temp -Force -ErrorAction SilentlyContinue
    Write-Host ('SETUP FAILED: ' + $_.Exception.Message) -ForegroundColor Red
    Write-Host 'Check internet access or follow the manual instructions in FIRST_RUN_WINDOWS.md.'
    exit 1
}
