
param (
    [string]$jarPath,
    [string]$websites
)
Write-Host "Current script directory: $PSScriptRoot"

# Sprawdzenie, czy skrypt ma uprawnienia administratora
if (-not ([Security.Principal.WindowsPrincipal] [Security.Principal.WindowsIdentity]::GetCurrent()).IsInRole([Security.Principal.WindowsBuiltInRole] "Administrator")) {
    $arguments = "-jarPath `"$jarPath`" -websites `"$websites`""
    Start-Process powershell -ArgumentList "-NoProfile -ExecutionPolicy RemoteSigned -File `"$($myInvocation.MyCommand.Definition)`" $arguments" -Verb RunAs
    exit
}

# Uruchomienie pliku JAR
Write-Host "Uruchamianie pliku JAR: $jarPath z witrynami: $websites"

if (-not [System.IO.File]::Exists($jarPath)) {
    Write-Error "Plik JAR nie został znaleziony: $jarPath"
    exit 1
}

# Rozdzielenie stron do zablokowania
$websitesArray = $websites -split " "

# Tworzenie komendy do uruchomienia JAR
$arguments = $websitesArray -join " "
$command = "java -jar `"$jarPath`" $arguments"

# Wywołanie JAR
Invoke-Expression $command