param (
    [string]$jarFileName
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
$jarPath = Join-Path -Path $libsDirectory -ChildPath "UnblockWebsites.jar"

Write-Host "Current script directory: $scriptDirectory"
Write-Host "JAR file path: $jarPath"
Write-Host "Libraries directory: $libsDirectory"

# Sprawdzenie, czy skrypt ma uprawnienia administratora
if (-not ([Security.Principal.WindowsPrincipal] [Security.Principal.WindowsIdentity]::GetCurrent()).IsInRole([Security.Principal.WindowsBuiltInRole] "Administrator")) {
     $arguments = "-jarFileName `"$jarFileName`""
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
    exit 1
}

# Utworzenie komendy do uruchomienia JAR
$command = "java -jar `"$jarPath`""

Write-Host "Running command: $command"

# Wywołanie JAR
Invoke-Expression $command
