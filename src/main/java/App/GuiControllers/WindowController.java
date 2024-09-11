package App.GuiControllers;

import App.GoogleCalendarConnection;
import App.Main;
import App.Services.EventService;
import App.utils.BlockedWebsitesHandler;
import com.google.api.services.calendar.model.Event;
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
    private ListView<String> websitesToBlockListView;
    @FXML
    private Label editWebsitesLabel;
    @FXML
    private AnchorPane websitePane;
    @FXML
    private Button AddNewWebsiteButton;
    @FXML
    private Button DeleteWebsiteButton;
    @FXML
    private TextField websiteToBlockField;
    @FXML
    private Button exitButton;
    @FXML
    private Label processingOutcomeLabel;
    @FXML
    private Button outcomeHideButton;








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
        String processingOutcome = eventService.processEvents();
        processingOutcomeLabel.setText(processingOutcome);
        processingOutcomeLabel.setVisible(true);
        outcomeHideButton.setVisible(true);

    }
    @FXML
    protected void hideOutcome(){
        processingOutcomeLabel.setVisible(false);
        outcomeHideButton.setVisible(false);
    }

    @FXML
    protected void onHelloButtonClick(ActionEvent event) throws IOException {
        System.out.println("CONFIG JEST SUPPPPPERRRRRR");
    }
}