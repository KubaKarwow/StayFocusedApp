param (
    [string]$jarPath,
    [string]$websites
)

# Ustal katalog roboczy skryptu
$scriptDirectory = Split-Path -Parent $MyInvocation.MyCommand.Path

# Ustal katalogi
$libsDirectory = Join-Path -Path $scriptDirectory -ChildPath "..\libs"
$logDirectory = Join-Path -Path $scriptDirectory -ChildPath "..\logs"

# Rozwiązywanie ścieżek
$libsDirectory = Resolve-Path -Path $libsDirectory
$logDirectory = Resolve-Path -Path $logDirectory

# Ścieżka do pliku JAR
$jarPath = Join-Path -Path $libsDirectory -ChildPath "blockWebsites.jar"


Write-Host "Current script directory: $scriptDirectory"
Write-Host "JAR file path: $jarPath"
Write-Host "libs file path: $libsDirectory"


# Sprawdzenie, czy skrypt ma uprawnienia administratora
if (-not ([Security.Principal.WindowsPrincipal] [Security.Principal.WindowsIdentity]::GetCurrent()).IsInRole([Security.Principal.WindowsBuiltInRole] "Administrator")) {
    $arguments = "-jarPath `"$jarPath`" -websites `"$websites`""
    Start-Process powershell -ArgumentList "-NoProfile -ExecutionPolicy RemoteSigned -File `"$($MyInvocation.MyCommand.Path)`" $arguments" -Verb RunAs
    exit
}

# Sprawdzenie istnienia pliku JAR
if (-not [System.IO.File]::Exists($jarPath)) {
    Write-Error "Plik JAR nie został znaleziony: $jarPath"
    Read-Host "Press Enter to exit"
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

# Opcjonalnie: usunięcie plików logów po użyciu
Remove-Item $outputLog -ErrorAction SilentlyContinue
Remove-Item $errorLog -ErrorAction SilentlyContinue
