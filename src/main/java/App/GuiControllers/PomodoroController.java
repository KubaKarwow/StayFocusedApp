package App.GuiControllers;

import App.dtos.PomodoroSession;
import App.repositories.JokeRepo;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

import javax.sound.sampled.*;

public class PomodoroController {

    @FXML
    private Label stateLabel;

    @FXML
    private Label timerLabel;

    @FXML
    private Label motivationalTextLabel;

    @FXML
    private Label sessionLabel;

    //toDO could make the pomodoroWindow make some noise when the work/break ends
    private final String SOUND_FILE_PATH = "src/main/resources/notificationSounds/notificationSound1.wav";

    public void startPomodoroSessions(List<PomodoroSession> sessionList) throws InterruptedException {
        sessionLabel.setText("1/"+sessionList.size());
        motivationalTextLabel.setText("You got this!");

        int amountOfWorkingMinutesBegin = sessionList.get(0).getAmountOfWorkingSeconds()/60;
        int amountOfWorkingSecondsBegin = sessionList.get(0).getAmountOfWorkingSeconds()%60;
        String timerT = String.format("%02d:%02d", amountOfWorkingMinutesBegin, amountOfWorkingSecondsBegin);
        timerLabel.setText(timerT);
       new Thread(() -> {
           int currentSession=1;
            for (PomodoroSession pomodoroSession : sessionList) {
                int currentSessionNumberInsideLambda = currentSession;
                Platform.runLater( () -> {
                    stateLabel.setText("WORKING");
                    motivationalTextLabel.setText("You got this!");
                    sessionLabel.setText("session "+currentSessionNumberInsideLambda+"/"+sessionList.size());
                    stateLabel.setStyle("-fx-background-color: #6439FF;");
                    timerLabel.setStyle("-fx-background-color: #4F75FF;");
                    motivationalTextLabel.setStyle("-fx-background-color:  #00CCDD;");
                    sessionLabel.setStyle("-fx-background-color: #7CF5FF;");
                    File file = new File("src/main/resources/notificationSounds/notificationSound1.wav");
                    AudioInputStream audioStream = null;
                    try {
                        audioStream = AudioSystem.getAudioInputStream(file);
                    } catch (UnsupportedAudioFileException e) {
                        throw new RuntimeException(e);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    Clip clip = null;
                    try {
                        clip = AudioSystem.getClip();
                    } catch (LineUnavailableException e) {
                        throw new RuntimeException(e);
                    }
                    try {
                        clip.open(audioStream);
                    } catch (LineUnavailableException e) {
                        throw new RuntimeException(e);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    clip.start();
                });
                int amountOfWorkingSeconds = pomodoroSession.getAmountOfWorkingSeconds();
                amountOfWorkingSeconds=20;
                while (amountOfWorkingSeconds > 0) {
                    try {
                        Thread.sleep(1000); // usypiamy wątek na 1 sekundę
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    amountOfWorkingSeconds--;

                    // obliczamy czas, który pozostał
                    int amountOfMinutesLeft = amountOfWorkingSeconds / 60;
                    int amountOfSecondsLeft = amountOfWorkingSeconds % 60;

                    // budujemy tekst wyświetlający timer
                    String timerText = String.format("%02d:%02d", amountOfMinutesLeft, amountOfSecondsLeft);

                    // aktualizujemy Label z poziomu JavaFX Application Thread
                    Platform.runLater(() -> {
                        timerLabel.setText(timerText); // ustawiamy tekst na labelce
                    });
                }
                Platform.runLater( () -> {
                    stateLabel.setText("BREAK TIME");
                    motivationalTextLabel.setText("Enjoy the break and the joke <3!");
                    stateLabel.setStyle("-fx-background-color: #CCD5AE;");
                    timerLabel.setStyle("-fx-background-color: #E0E5B6;");
                    motivationalTextLabel.setStyle("-fx-background-color: #FAEDCE;");
                    sessionLabel.setStyle("-fx-background-color: #FEFAE0;");
                    File file = new File("src/main/resources/notificationSounds/notificationSound1.wav");
                    AudioInputStream audioStream = null;
                    try {
                        audioStream = AudioSystem.getAudioInputStream(file);
                    } catch (UnsupportedAudioFileException e) {
                        throw new RuntimeException(e);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    Clip clip = null;
                    try {
                        clip = AudioSystem.getClip();
                    } catch (LineUnavailableException e) {
                        throw new RuntimeException(e);
                    }
                    try {
                        clip.open(audioStream);
                    } catch (LineUnavailableException e) {
                        throw new RuntimeException(e);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    clip.start();
                    String joke = null;
                    try {
                        joke = new JokeRepo().getJoke().toString();
                    } catch (URISyntaxException e) {
                        throw new RuntimeException(e);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    showJokePopup(joke);
                });
                int amountOfBreakSeconds = pomodoroSession.getAmountOfBreakSeconds();
                amountOfBreakSeconds=10;
                while(amountOfBreakSeconds > 0){
                    try{
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    amountOfBreakSeconds--;

                    // obliczamy czas, który pozostał
                    int amountOfMinutesLeft = amountOfBreakSeconds / 60;
                    int amountOfSecondsLeft = amountOfBreakSeconds % 60;

                    // budujemy tekst wyświetlający timer
                    String timerText = String.format("%02d:%02d", amountOfMinutesLeft, amountOfSecondsLeft);

                    // aktualizujemy Label z poziomu JavaFX Application Thread
                    Platform.runLater(() -> {
                        timerLabel.setText(timerText); // ustawiamy tekst na labelce
                    });
                }currentSession++;
            }
        }).start();

    }
    private void showJokePopup(String joke) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Joke time");
        alert.setHeaderText(null);
        alert.setContentText(joke);
        alert.showAndWait();
    }
}

