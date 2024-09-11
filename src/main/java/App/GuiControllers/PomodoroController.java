package App.GuiControllers;

import App.dtos.PomodoroSession;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

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
        for (PomodoroSession pomodoroSession : sessionList) {
            //work session
            int amountOfWorkingMinutes = pomodoroSession.getAmountOfWorkingMinutes();
            while (amountOfWorkingMinutes > 0) {
               Thread.sleep(1000);
                amountOfWorkingMinutes--;
                int amountOfMinutesLeft = amountOfWorkingMinutes / 60;
                int amountOfSecondsLeft = amountOfMinutesLeft % 60;
                String timerText = "";
                if (amountOfMinutesLeft < 10) {
                    timerText += "0" + amountOfMinutesLeft;
                } else {
                    timerText += amountOfMinutesLeft;
                }
                timerText += ":";
                if (amountOfSecondsLeft < 10) {
                    timerText += "0" + amountOfSecondsLeft;
                } else {
                    timerText += amountOfSecondsLeft;
                }
                timerLabel.setText(timerText);
            }
        }
    }
}

