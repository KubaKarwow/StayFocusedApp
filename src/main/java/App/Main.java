package App;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.File;
import java.nio.file.Paths;
import java.util.Scanner;

public class Main extends Application {
    public static void main(String[] args) {
       launch(args);

    }
    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("windowConfig.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Productivity app");
        stage.setScene(scene);
        stage.show();
    }

}
