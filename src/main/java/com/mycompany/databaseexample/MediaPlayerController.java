package com.mycompany.databaseexample;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.util.Duration;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class MediaPlayerController implements Initializable {
    static Clip clip;
    private Parent root;
    private static Scene scene;
    private static Stage stage;
    @FXML
    private MediaView mediaView;

    @FXML
    private Button skipButton; // Skip button

    private Media media;
    private MediaPlayer mediaPlayer;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        String filePath = "/com/mycompany/databaseexample/movies/Pokémon Movie.mp4";
        media = new Media(getClass().getResource(filePath).toString());
        mediaPlayer = new MediaPlayer(media);
        mediaView.setMediaPlayer(mediaPlayer);

        mediaPlayer.setAutoPlay(true); // Set the video to autoplay

        // Event listener for when the video ends
        mediaPlayer.setOnEndOfMedia(() -> {
            try {
                Parent root = FXMLLoader.load(getClass().getResource("fxml/userLogin.fxml"));
                stage = (Stage) mediaView.getScene().getWindow();
                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                File music = new File(getClass().getResource("music/main_theme.wav").toURI());
                AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(music);
                clip = AudioSystem.getClip();
                clip.open(audioInputStream);
                clip.start();
            }
            catch(Exception e){

            }


        });
    }

    @FXML
    public void skipMedia() {
        mediaPlayer.stop(); // Stop the current media
        try {
            Parent root = FXMLLoader.load(getClass().getResource("fxml/userLogin.fxml"));
            stage = (Stage) mediaView.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            File music = new File(getClass().getResource("music/main_theme.wav").toURI());
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(music);
            clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            clip.start();
        }
        catch(Exception e){

        }
    }
}
