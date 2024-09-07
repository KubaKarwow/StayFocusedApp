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

# Sprawdzenie istnienia pliku JAR
if (-not [System.IO.File]::Exists($jarPath)) {
    Write-Error "Plik JAR nie został znaleziony: $jarPath"
    Read-Host "poczekaj byku"
    Start-Sleep -Seconds 9999
    exit 1
}

# Rozdzielenie stron do zablokowania
$websitesArray = $websites -split " "

# Tworzenie komendy do uruchomienia JAR
$arguments = $websitesArray -join " "
$command = "java -jar `"$jarPath`" $arguments"
Write-Host "Running command: $command"

# Ścieżki do plików logów
$logDirectory = "C:\Users\Jakub\Desktop\myJavaProjects\StayFocusedApp\GoogleApiTest\logs"
$outputLog = Join-Path -Path $logDirectory -ChildPath "output.log"
$errorLog = Join-Path -Path $logDirectory -ChildPath "error.log"

# Tworzenie katalogu logów, jeśli nie istnieje
if (-not (Test-Path $logDirectory)) {
    Write-Host "Creating log directory: $logDirectory"
    New-Item -Path $logDirectory -ItemType Directory
}

# Wywołanie JAR z przekierowaniem wyjścia i błędów
Start-Process -FilePath "java" -ArgumentList "-jar `"$jarPath`" $arguments" -NoNewWindow -RedirectStandardOutput $outputLog -RedirectStandardError $errorLog -Wait

# Odczyt standardowego wyjścia i wyświetlanie na konsoli
if (Test-Path $outputLog) {
    Write-Host "Output log:"
    Get-Content $outputLog | ForEach-Object { Write-Output $_ }
} else {
    Write-Host "No output log file found."
}

# Odczyt błędów i wyświetlanie na konsoli
if (Test-Path $errorLog) {
    Write-Host "Error log:"
    Get-Content $errorLog | ForEach-Object { Write-Error $_ }
} else {
    Write-Host "No error log file found."
}


