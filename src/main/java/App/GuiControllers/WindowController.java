package App.GuiControllers;

import App.GoogleCalendarConnection;
import App.Main;
import App.Services.EventService;
import App.dtos.PomodoroSession;
import App.dtos.ProcessEventsDTO;
import App.utils.BlockedWebsitesHandler;
import com.google.api.services.calendar.model.Event;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;

public class WindowController {
    @FXML
    private Button howToUseButton;





    @FXML
    private Button hideButton;

    @FXML
    private Label processingOutcomeLabel;

    @FXML
    protected void hideOutcome(){
        processingOutcomeLabel.setVisible(false);
        hideButton.setVisible(false);
    }
    @FXML
    protected void editWebsiteList(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("websitesEditWindow.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        Stage stage = new Stage();
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    protected void processEvents() throws IOException, InterruptedException, GeneralSecurityException {
        List<Event> events = GoogleCalendarConnection.getEvents();
        EventService eventService = new EventService(events);
        ProcessEventsDTO processEventsDTO = eventService.processEvents();

        processingOutcomeLabel.setText(processEventsDTO.getProcessingOutcome());
        processingOutcomeLabel.setVisible(true);
        hideButton.setVisible(true);
    }

    @FXML
    protected void showInstructions() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("instructions.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        Stage stage = new Stage();
        stage.setTitle("Instructions");
        stage.setScene(scene);
        stage.show();
    }
}
 /*  if (processEventsDTO.getProcessingOutcome().equals("applied blocks")) {
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    for (PomodoroSession pomodoroSession : processEventsDTO.getPomodoroSessions()) {
                        //work session
                        int amountOfWorkingMinutes = pomodoroSession.getAmountOfWorkingMinutes();
                        while (amountOfWorkingMinutes > 0) {
                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException e) {
                                throw new RuntimeException(e);
                            }
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
            });
            currentStateLabel.setText("Work time");
            sessionLabel.setText("1/" + processEventsDTO.getPomodoroSessions().size());
            timerLabel.setText("25:00");*/

