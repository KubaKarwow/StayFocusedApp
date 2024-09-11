package App.GuiControllers;

import App.utils.BlockedWebsitesHandler;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class WebsitesEditWindow implements Initializable {
    @FXML
    private Button DeleteWebsiteButton;

    @FXML
    private Button AddNewWebsiteButton;

    @FXML
    private ListView<String> websitesToBlockListView;

    @FXML
    private TextField websiteToBlockField;

    private String currentlySelected="";

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        websitesToBlockListView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                currentlySelected = websitesToBlockListView.getSelectionModel().getSelectedItem();
                System.out.println(currentlySelected);
            }
        });
        BlockedWebsitesHandler blockedWebsitesHandler = new BlockedWebsitesHandler();
        List<String> websitesToBlock = null;
        try {
            websitesToBlock = blockedWebsitesHandler.getWebsitesToBlock();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        websitesToBlockListView.getItems().removeAll(websitesToBlockListView.getItems());
        websitesToBlockListView.getItems().addAll(websitesToBlock);
    }

    @FXML
    protected void addNewWebsite() throws IOException {
        websitesToBlockListView.getItems().add(websiteToBlockField.getText());
        BlockedWebsitesHandler blockedWebsitesHandler = new BlockedWebsitesHandler();
        blockedWebsitesHandler.addWebsite(websiteToBlockField.getText());
        websiteToBlockField.setText("");
    }

    @FXML
    protected void deleteWebsite() throws IOException {
        BlockedWebsitesHandler blockedWebsitesHandler = new BlockedWebsitesHandler();
        blockedWebsitesHandler.deleteWebsite(currentlySelected);
        websitesToBlockListView.getItems().remove(currentlySelected);

    }
}
