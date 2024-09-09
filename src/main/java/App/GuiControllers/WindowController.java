package App.GuiControllers;

import App.GoogleCalendarConnection;
import App.Services.EventService;
import App.utils.BlockedWebsitesHandler;
import com.google.api.services.calendar.model.Event;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

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
    private String currentlySelected="";
    @FXML
    protected void exitWebsitesEdit(){
        websitePane.setVisible(false);
    }


    @FXML
    protected void deleteWebsite() throws IOException {
        BlockedWebsitesHandler blockedWebsitesHandler = new BlockedWebsitesHandler();
        blockedWebsitesHandler.deleteWebsite(currentlySelected);
        websitesToBlockListView.getItems().remove(currentlySelected);

    }
    @FXML
    protected void addNewWebsite() throws IOException {
        websitesToBlockListView.getItems().add(websiteToBlockField.getText());
        BlockedWebsitesHandler blockedWebsitesHandler = new BlockedWebsitesHandler();
        blockedWebsitesHandler.addWebsite(websiteToBlockField.getText());
        websiteToBlockField.setText("");
    }

    @FXML
    protected void editWebsiteList(ActionEvent event) throws IOException {
        websitePane.setVisible(true);

        websitesToBlockListView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                currentlySelected = websitesToBlockListView.getSelectionModel().getSelectedItem();
                System.out.println(currentlySelected);
            }
        });
        BlockedWebsitesHandler blockedWebsitesHandler = new BlockedWebsitesHandler();
        List<String> websitesToBlock = blockedWebsitesHandler.getWebsitesToBlock();
        websitesToBlockListView.getItems().removeAll(websitesToBlockListView.getItems());
        websitesToBlockListView.getItems().addAll(websitesToBlock);

    }

    @FXML
    protected void processEvents() throws IOException, InterruptedException, GeneralSecurityException {
        List<Event> events = GoogleCalendarConnection.getEvents();
        EventService eventService = new EventService(events);
        eventService.processEvents();
    }

    @FXML
    protected void onHelloButtonClick(ActionEvent event) throws IOException {
        System.out.println("CONFIG JEST SUPPPPPERRRRRR");
    }
}