package App.Services;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

public class ScriptService {

    public static boolean runBlockWebsitesScript(List<String> args) throws IOException, InterruptedException {
        String scriptPath = "Scripts/blockWebsites.ps1"; // Użyj absolutnej ścieżki
        String jarPath = "blockWebsites.jar"; // Ścieżka do JARa

        // Komenda do uruchomienia skryptu PowerShell
        StringBuilder commandBuilder = new StringBuilder();
        commandBuilder.append("powershell.exe -NoProfile -ExecutionPolicy RemoteSigned -File \"")
                .append(scriptPath)
                .append("\" -jarPath \"").append(jarPath).append("\" -websites \"");

        for (String arg : args) {
            commandBuilder.append(arg).append(" ");
        }
        String command = commandBuilder.toString().trim();
        command += "\"";

        System.out.println(command);

        // Tworzenie procesu
        ProcessBuilder processBuilder = new ProcessBuilder("cmd.exe", "/c", command);
        processBuilder.redirectErrorStream(true);

        // Uruchamianie procesu
        Process process = processBuilder.start();

        // Odczytywanie wyjścia procesu
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String line;
        while ((line = reader.readLine()) != null) {
            System.out.println(line);
            //script didnt go through
            if(line.contains("Write-Error")){
                return false;
            }
        }

        // Oczekiwanie na zakończenie procesu
        int exitCode = process.waitFor();
        int i = process.exitValue();
        System.out.println("FAKTYCZNY EXIT CODE " + i);
        System.out.println("Exit Code: " + exitCode);
        return true;
    }

    public static boolean runUnblockWebsitesScript() throws IOException, InterruptedException {
        String scriptPath = "Scripts/UnblockWebsitesAdmin.ps1";
        String jarPath = "UnblockWebsites.jar"; // Ścieżka do JARa

        // Komenda do uruchomienia skryptu PowerShell
        StringBuilder commandBuilder = new StringBuilder();
        commandBuilder.append("powershell.exe -NoProfile -ExecutionPolicy RemoteSigned -File \"")
                .append(scriptPath).append("\" -jarPath \"").append(jarPath).append("\"");

        String command = commandBuilder.toString().trim();
        System.out.println(command);
        // Tworzenie procesu
        ProcessBuilder processBuilder = new ProcessBuilder("cmd.exe", "/c", command);
        processBuilder.redirectErrorStream(true);

        // Uruchamianie procesu
        Process process = processBuilder.start();

        // Odczytywanie wyjścia procesu
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String line;
        while ((line = reader.readLine()) != null) {
            System.out.println(line);
            if(line.contains("Write-Error")){
                return false;
            }
        }

        // Czekanie na zakończenie procesu
        int exitCode = process.waitFor();
        System.out.println("Exit Code: " + exitCode);
        return true;
    }
}
