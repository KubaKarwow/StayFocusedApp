package App.GuiControllers;

import App.dtos.PomodoroSession;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

import java.awt.*;
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
        motivationalTextLabel.setText("Ill figure smth out I promise");

        int amountOfWorkingMinutesBegin = sessionList.get(0).getAmountOfWorkingSeconds()/60;
        int amountOfWorkingSecondsBegin = sessionList.get(0).getAmountOfWorkingSeconds()%60;
        String timerT = String.format("%02d:%02d", amountOfWorkingMinutesBegin, amountOfWorkingSecondsBegin);
        timerLabel.setText(timerT);
       new Thread(() -> {
            for (PomodoroSession pomodoroSession : sessionList) {
                //work session
                Platform.runLater( () -> {
                    stateLabel.setText("WORKING");
                    motivationalTextLabel.setText("insert motivation XD");
                    stateLabel.setStyle("-fx-background-color: #6439FF;");
                    timerLabel.setStyle("-fx-background-color: #4F75FF;");
                    motivationalTextLabel.setStyle("-fx-background-color:  #00CCDD;");
                    sessionLabel.setStyle("-fx-background-color: #7CF5FF;");
                });
                int amountOfWorkingMinutes = pomodoroSession.getAmountOfWorkingSeconds();
                amountOfWorkingMinutes=20;
                while (amountOfWorkingMinutes > 0) {
                    try {
                        Thread.sleep(1000); // usypiamy wątek na 1 sekundę
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    amountOfWorkingMinutes--;

                    // obliczamy czas, który pozostał
                    int amountOfMinutesLeft = amountOfWorkingMinutes / 60;
                    int amountOfSecondsLeft = amountOfWorkingMinutes % 60;

                    // budujemy tekst wyświetlający timer
                    String timerText = String.format("%02d:%02d", amountOfMinutesLeft, amountOfSecondsLeft);

                    // aktualizujemy Label z poziomu JavaFX Application Thread
                    Platform.runLater(() -> {
                        timerLabel.setText(timerText); // ustawiamy tekst na labelce
                    });
                }
                Platform.runLater( () -> {
                    stateLabel.setText("BREAK TIME");
                    motivationalTextLabel.setText("You the goat homie, get some water and come back :D");
                    stateLabel.setStyle("-fx-background-color: #CCD5AE;");
                    timerLabel.setStyle("-fx-background-color: #E0E5B6;");
                    motivationalTextLabel.setStyle("-fx-background-color: #FAEDCE;");
                    sessionLabel.setStyle("-fx-background-color: #FEFAE0;");
                });
                int amountOfBreakSeconds = pomodoroSession.getAmountOfBreakSeconds();
                amountOfBreakSeconds=61;
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
                }
            }
        }).start();

    }
}

