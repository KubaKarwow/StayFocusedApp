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
import javafx.scene.Parent;
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

        if(processEventsDTO.getTimeToWaitTillTheBlockBegins() == 0 && processEventsDTO.getPomodoroSessions()!=null){
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("pomodoroWindow.fxml"));
            Object load = fxmlLoader.load();

            PomodoroController pomodoroController = fxmlLoader.getController();
            Scene scene = new Scene((Parent) load);
            Stage stage = new Stage();
            stage.setTitle("Hello!");
            stage.setScene(scene);
            stage.show();
           // pomodoroController.startPomodoroSessions(processEventsDTO.getPomodoroSessions());

        }

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


