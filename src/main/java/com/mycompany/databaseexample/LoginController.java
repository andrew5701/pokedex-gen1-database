/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.databaseexample;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.Parent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.Node;
import javafx.stage.*;
import javafx.stage.Stage;
import java.sql.*;
import javafx.scene.control.Label;
import java.io.*;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Properties;
import javax.print.attribute.standard.Media;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

/**
 *
 * @author ak7272
 */
public class LoginController implements Initializable{
    static Clip clip = null;
    static Clip themePokeClip = null;
    
    private Parent root;
    private static Scene scene;
    private static Stage stage;
    
    @FXML
    private Label errorLabel;

    
    @FXML
    private Button signIn;
    
    @FXML
    private TextField userTextBox, passwordTextBox;
    
    static void close(){
        if(clip != null){
            clip.close();
        }
    }
    
//    @FXML 
//    public void signInClick(ActionEvent event) throws IOException, LineUnavailableException, UnsupportedAudioFileException, URISyntaxException{
//        
//        
//       
//        
//        if(userTextBox.getText() != "" && passwordTextBox.getText() != ""){
//            Connection connection = null;
//            PreparedStatement preparedStatement = null;
//            ResultSet resultSet = null;
//            
//            try {
////                connection = DriverManager.getConnection("jdbc:sqlite:src/main/resources/com/mycompany/databaseexample/pokemon.db");
//                connection = getConn();
//                preparedStatement = connection.prepareStatement("SELECT id FROM trainer WHERE username = ? AND password = ?");
//                preparedStatement.setString(1, userTextBox.getText());
//                preparedStatement.setString(2, passwordTextBox.getText());
//                
//                resultSet = preparedStatement.executeQuery();
//                
////                connection.close();
////                preparedStatement.close();
//                
//            if(resultSet.next() == false){
//                     errorLabel.setText("Try again");
////                     resultSet.close();
//                     
//            }
//            else{
////                     resultSet.close();
//                    
//                     
//                String databaseURL = "jdbc:sqlite:src/main/resources/com/mycompany/databaseexample/pokemon.db";
//                Connection conn = null;
//
//                // create a connection to the database
////                conn = DriverManager.getConnection(databaseURL);
//                conn = getConn();
////
////                // Get Trainer ID
//                     System.out.println("Start");
//                String query = "SELECT id FROM trainer WHERE name = ?";
//                PreparedStatement pstmt = conn.prepareStatement(query);
//                pstmt.setString(1,"Andrew");
//                ResultSet rs = pstmt.executeQuery();
//                System.out.println(rs.getString("id"));
//                int id = rs.getInt("id");
//                
//                System.out.println("After");
//                
////                pstmt.close();
////                rs.close();
////                conn.close();
//
//                Properties properties = new Properties();
//                properties.setProperty("key", userTextBox.getText());
//                properties.setProperty("id", String.valueOf(id));
//                properties.store(new FileOutputStream("config.properties"), null);
//                
//
//
////                File music = new File(getClass().getResource("music/route1.wav").toURI());
////                AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(music); 
////                clip = AudioSystem.getClip();
////                clip.open(audioInputStream);
////                clip.start();
//
//                Parent root = FXMLLoader.load(getClass().getResource("fxml/signUp.fxml"));
//                stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
//                scene = new Scene(root);
//                stage.setScene(scene);
////                MainController mc = ;
////                mc.trainerText.setText("Welcome trainer " + userTextBox.getText() + "!");
//                stage.show();
//            }
//            } catch (Exception e){
//                
//            }            
//        }  
//    }
    @FXML 
public void signInClick(ActionEvent event) {
    if (!userTextBox.getText().isEmpty() && !passwordTextBox.getText().isEmpty()) {
        try (Connection connection = ConnectionHelp.getConn();
             PreparedStatement preparedStatement = connection.prepareStatement(
                 "SELECT id FROM trainer WHERE username = ? AND password = ?")) {

            preparedStatement.setString(1, userTextBox.getText());
            preparedStatement.setString(2, passwordTextBox.getText());

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (!resultSet.next()) {
                    errorLabel.setText("Try again");
                } else {
                    int trainerId = resultSet.getInt("id");
                    System.out.println(trainerId);
                    
                    // Store user information
                    Properties properties = new Properties();
                    properties.setProperty("key", userTextBox.getText());
                    properties.setProperty("trainerId", String.valueOf(trainerId));
                    properties.store(new FileOutputStream("config.properties"), null);

                    // Play audio (if applicable)
//                    playAudio("path/to/your/audio/file.wav"); // Replace with actual path

                    // Switch scene to main application view
                    Parent root = FXMLLoader.load(getClass().getResource("fxml/main.fxml")); // Replace with actual FXML
                    stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
                    scene = new Scene(root);
                    stage.setScene(scene);
                    stage.show();
                    MediaPlayerController.clip.close();

                    if(themePokeClip != null){
                        themePokeClip.close();
                    }

                    try {
                        File music = new File(getClass().getResource("music/pokepark.wav").toURI());
                        AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(music);
                        clip = AudioSystem.getClip();
                        clip.open(audioInputStream);
                        clip.start();
                    }
                    catch (Exception e){

                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Log or handle the exception
        } catch (IOException e) {
            e.printStackTrace(); // Handle IOException
        }
    }
}

private void playAudio(String audioFilePath) {
    try {
        AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(audioFilePath));
        clip = AudioSystem.getClip();
        clip.open(audioInputStream);
        clip.start();
    } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
        e.printStackTrace(); // Handle exception
    }
}

    
    @FXML
    public void signUpClick(ActionEvent event) throws IOException{
        Parent root = FXMLLoader.load(getClass().getResource("fxml/signUp.fxml"));
        stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
        TradeController.setPreviousScene(scene);
        
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
//        File file = new File("movies/gameboyintro.mp4");
//        Media media = new Media("") {};
    }

static void playSound(String audioFilePath){
        try {

            File music = new File(LoginController.class.getResource(audioFilePath).toURI());
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(music);
            themePokeClip = AudioSystem.getClip();
            themePokeClip.open(audioInputStream);
            themePokeClip.start();
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace(); // Handle exception
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
}
    
    
    
    
    
    
    
    
    

    
  
      
}
