package com.mycompany.databaseexample;



import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import java.io.*;
import java.io.IOException;
import java.util.Properties;
//import javafx.scene.media.MediaPlayer;
//import javafx.scene.media.MediaView;
import java.io.File;
import java.net.URISyntaxException;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
//import javafx.scene.media.Media;
//import javafx.scene.media.MediaPlayer;





public class MainController implements Initializable {
    
//    private File file;
//    private Media media;
//    private MediaPlayer mediaPlayer;

    @FXML
    VBox root;
    
    @FXML
    VBox contentPane;
    
    @FXML
    Label trainerText;
    
    @FXML
    ImageView pikachuPic;
    
//    @FXML
//    MediaView mediaView;

   
  
    
    @FXML
    private void pokemon() throws IOException, UnsupportedAudioFileException, LineUnavailableException {
        

        try {
            Pane newLoadedPane = FXMLLoader.load(getClass().getResource("fxml/PokemonViewer.fxml"));
            contentPane.getChildren().clear();
            contentPane.getChildren().add(newLoadedPane);

        } catch (IOException ex) {
            ex.printStackTrace();
        }

    } 
    
    @FXML
    private void trainerTable() throws IOException {
        

        try {
            Pane newLoadedPane = FXMLLoader.load(getClass().getResource("fxml/TrainerViewer.fxml"));
            contentPane.getChildren().clear();
            contentPane.getChildren().add(newLoadedPane);

        } catch (IOException ex) {
            ex.printStackTrace();
        }

    } 
    
    @FXML
    private void regionsTable() throws IOException {
        

        try {
            Pane newLoadedPane = FXMLLoader.load(getClass().getResource("fxml/RegionViewer.fxml"));
            contentPane.getChildren().clear();
            contentPane.getChildren().add(newLoadedPane);

        } catch (IOException ex) {
            ex.printStackTrace();
        }

    } 
    
    @FXML
    private void gamesTable() throws IOException {
        

        try {
            Pane newLoadedPane = FXMLLoader.load(getClass().getResource("fxml/GamesViewer.fxml"));
            contentPane.getChildren().clear();
            contentPane.getChildren().add(newLoadedPane);

        } catch (IOException ex) {
            ex.printStackTrace();
        }

    } 
    
    @FXML
    private void leadersTable() throws IOException {
        

        try {
            Pane newLoadedPane = FXMLLoader.load(getClass().getResource("fxml/GymLeaderViewer.fxml"));
            contentPane.getChildren().clear();
            contentPane.getChildren().add(newLoadedPane);

        } catch (IOException ex) {
            ex.printStackTrace();
        }

    }

    @FXML
    private void openChatGPT() throws IOException {


        try {
            Pane newLoadedPane = FXMLLoader.load(getClass().getResource("fxml/ChatGPTView.fxml"));
            contentPane.getChildren().clear();
            contentPane.getChildren().add(newLoadedPane);

        } catch (IOException ex) {
            ex.printStackTrace();
        }

    }
    
    @FXML
    private void openMain() throws IOException {
        

        try {
            Pane newLoadedPane = FXMLLoader.load(getClass().getResource("fxml/main.fxml"));
            contentPane.getChildren().clear();
            contentPane.getChildren().add(newLoadedPane);

        } catch (IOException ex) {
            ex.printStackTrace();
        }

    } 
    
    
    @FXML
    private void pikachuSound() throws IOException, LineUnavailableException, UnsupportedAudioFileException, URISyntaxException {
        

        File music = new File(getClass().getResource("music/pikachusound.wav").toURI());
        AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(music); 
        Clip clip = AudioSystem.getClip();
        clip.open(audioInputStream);
        clip.start();

    } 
    
//    @FXML
//    private void loginFXML() throws IOException {
//
//        try {
//            Pane newLoadedPane = FXMLLoader.load(getClass().getResource("userLogin.fxml"));
//            contentPane.getChildren().clear();
//            contentPane.getChildren().add(newLoadedPane);
//
//        } catch (IOException ex) {
//            ex.printStackTrace();
//        }
//
//    }   
    
    
    @FXML
    private void close() throws IOException {

        System.out.println("Quitting!!");
        Platform.exit();

    }

//    @Override
//public void initialize(URL url, ResourceBundle rb) {
////    file = new File("C:\\Users\\ak7272\\Desktop\\gameboyintro.mp4");
////    media = new Media(file.toURI().toString());
////    mediaPlayer = new MediaPlayer(media);
////    // mediaView.setMediaPlayer(mediaPlayer); // You can uncomment this line if you have a MediaView component
////    mediaPlayer.play();
//}

    @Override
    public void initialize(URL url, ResourceBundle rb) {
//        pikachuPic.setImage(new Image(getClass().getResourceAsStream("C:\\Users\\ak7272\\Desktop\\Pokemon Photos\\Pikachu.png")));
        Properties properties = new Properties();

        try {
           
            FileInputStream fileInput = new FileInputStream("config.properties");
            properties.load(fileInput);
            fileInput.close();

            
            String value = properties.getProperty("key");
            System.out.println("Retrieved value: " + value);
            trainerText.setText("Welcome Trainer " + value + "!!!");
        } catch (IOException e) {
            e.printStackTrace();
        }
        
    }

}
