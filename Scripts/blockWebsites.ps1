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
    try {
        # Próba uruchomienia skryptu jako administrator
        Start-Process powershell -ArgumentList "-NoProfile -ExecutionPolicy RemoteSigned -File `"$($MyInvocation.MyCommand.Path)`" $arguments" -Verb RunAs -ErrorAction Stop
        exit 0  # Wyjście z kodem sukcesu, ponieważ użytkownik zgodził się na podniesienie uprawnień
    } catch {
        Write-Error "Odmowa nadania uprawnień administratora. Skrypt nie może kontynuować."
        exit 1  # Kod błędu 1, gdy użytkownik odmówił podniesienia uprawnień
    }
}

# Sprawdzenie istnienia pliku JAR
if (-not [System.IO.File]::Exists($jarPath)) {
    Write-Error "Plik JAR nie został znaleziony: $jarPath"
    exit 2  # Kod błędu 2, gdy plik JAR nie istnieje
}

# Rozdzielenie stron do zablokowania
$websitesArray = $websites -split " "

# Tworzenie komendy do uruchomienia JAR
$arguments = $websitesArray -join " "
$command = "java -jar `"$jarPath`" $arguments"
Write-Host "Running command: $command"

# Wywołanie JAR z przekierowaniem wyjścia i błędów
Start-Process -FilePath "java" -ArgumentList "-jar `"$jarPath`" $arguments"