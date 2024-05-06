package com.mycompany.databaseexample;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.Stage;
import javax.sound.sampled.FloatControl;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class TradeController implements Initializable {
    static Clip clip;
    @FXML
    VBox contentPane;
    private Parent root;
    private static Scene scene;
    private static Stage stage;
    @FXML
    private MediaView mediaView;

    @FXML
    private Button skipButton; // Skip button

    private Media media;
    private MediaPlayer mediaPlayer;
    private static Scene previousScene; // To store the previous scene
    public static void setPreviousScene(Scene scene) {
        previousScene = scene;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        String filePath = "/com/mycompany/databaseexample/movies/trade.mp4";
        media = new Media(getClass().getResource(filePath).toString());
        mediaPlayer = new MediaPlayer(media);
        mediaView.setMediaPlayer(mediaPlayer);

        LoginController.clip.isControlSupported(FloatControl.Type.MASTER_GAIN);
        FloatControl gainControl = (FloatControl) LoginController.clip.getControl(FloatControl.Type.MASTER_GAIN);

        float reduceVolume = -20.0f;
        gainControl.setValue(reduceVolume);

        mediaPlayer.setAutoPlay(true); // Set the video to autoplay

        // Event listener for when the video ends
        mediaPlayer.setOnEndOfMedia(() -> {
            gainControl.setValue(0.0f);



            if (previousScene != null) {
                stage = (Stage) mediaView.getScene().getWindow();
                stage.setScene(previousScene);
                stage.show();
            }

            previousScene = null;


        });
    }


}
