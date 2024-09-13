package App.GuiControllers;

import App.dtos.PomodoroSession;
import App.repositories.JokeRepo;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

import java.awt.*;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

public class PomodoroController {

    @FXML
    private Label stateLabel;

    @FXML
    private Label timerLabel;

    @FXML
    private Label motivationalTextLabel;

    @FXML
    private Label sessionLabel;


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
                });
                int amountOfWorkingSeconds = pomodoroSession.getAmountOfWorkingSeconds();
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
                    try {
                        motivationalTextLabel.setText( new JokeRepo().getJoke().toString());
                    } catch (URISyntaxException e) {
                        throw new RuntimeException(e);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    stateLabel.setStyle("-fx-background-color: #CCD5AE;");
                    timerLabel.setStyle("-fx-background-color: #E0E5B6;");
                    motivationalTextLabel.setStyle("-fx-background-color: #FAEDCE;");
                    sessionLabel.setStyle("-fx-background-color: #FEFAE0;");
                });
                int amountOfBreakSeconds = pomodoroSession.getAmountOfBreakSeconds();
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
}

