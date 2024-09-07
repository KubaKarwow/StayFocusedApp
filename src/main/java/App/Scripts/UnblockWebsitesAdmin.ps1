param (
    [string]$jarPath
)

# Sprawdzenie, czy skrypt jest uruchamiany z uprawnieniami administratora
if (-not ([Security.Principal.WindowsPrincipal] [Security.Principal.WindowsIdentity]::GetCurrent()).IsInRole([Security.Principal.WindowsBuiltInRole] "Administrator"))
{
    # Jeśli nie, ponowne uruchomienie skryptu jako administrator z tymi samymi argumentami
    $arguments = "-jarPath `"$jarPath`""
    Start-Process powershell -ArgumentList "-NoProfile -ExecutionPolicy Bypass -File `"$($myInvocation.MyCommand.Definition)`" $arguments" -Verb RunAs
    exit
}

# Uruchomienie skryptu z uprawnieniami administratora
Write-Host "Running with administrator privileges"

if (-not [System.IO.File]::Exists($jarPath)) {
    Write-Error "Plik JAR nie został znaleziony: $jarPath"
    exit 1
}

# Utworzenie komendy do uruchomienia JAR
$command = "java -jar `"$jarPath`""

# Uruchomienie pliku JAR bez dodatkowych argumentów
Invoke-Expression $command