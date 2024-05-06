/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.databaseexample;

import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.RadioButton;
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
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleGroup;

/**
 *
 * @author ak7272
 */
public class SignUpController implements Initializable{
    private Parent root;
    private static Scene scene;
    private static Stage stage;
    
    private String rank = "";
    
    @FXML
    private TableView tableView;

    @FXML
    private RadioButton beginnerButton, masterButton, legendButton;

    @FXML
    private TextField suUsernameText, suPasswordText, trainerNameText;
    
    @FXML
    private RadioButton beginnerRadio, masterRadio, legendRadio;
    
    
    @FXML
    public void goBack(ActionEvent event) throws IOException{
        Parent root = FXMLLoader.load(getClass().getResource("fxml/userLogin.fxml"));
        stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    
    
    @FXML
    public void beginnerRadioPress(ActionEvent event){
        rank = beginnerRadio.getText();
    }
    
    @FXML
    public void masterRadioPress(ActionEvent event){
        rank = masterRadio.getText();
    }
    
    @FXML
    public void legendRadioPress(ActionEvent event){
        rank = legendRadio.getText();
    }
    @FXML
public void createUser(ActionEvent event) {
    Connection connection = null;
    PreparedStatement preparedStatement = null;
    ResultSet resultSet = null;
    Alert a = new Alert(Alert.AlertType.NONE);

    if (rank.equals("")) {
        rank = "Beginner";
    }

    try {
        connection = ConnectionHelp.getConn();
        String checkUserQuery = "SELECT * FROM trainer WHERE username = ?";
        preparedStatement = connection.prepareStatement(checkUserQuery);
        preparedStatement.setString(1, suUsernameText.getText());
        resultSet = preparedStatement.executeQuery();

        if (!resultSet.next()) {
            String insertQuery = "INSERT INTO trainer (name, rank, username, password) VALUES (?, ?, ?, ?)";
            preparedStatement.close(); // Close the previous prepared statement
            preparedStatement = connection.prepareStatement(insertQuery);

            preparedStatement.setString(1, trainerNameText.getText());
            preparedStatement.setString(2, rank);
            preparedStatement.setString(3, suUsernameText.getText());
            preparedStatement.setString(4, suPasswordText.getText());

            preparedStatement.executeUpdate();

            Parent root = FXMLLoader.load(getClass().getResource("fxml/userLogin.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } else {
            a.setAlertType(Alert.AlertType.ERROR);
            a.setContentText("Someone else has this username");
            a.getButtonTypes().setAll(ButtonType.OK);
            a.setGraphic(null);
            a.show();
        }
    } catch (SQLException e) {
        System.out.println("Sql Sign Up Error Caught");
        e.printStackTrace();
    } catch (IOException e) {
        System.out.println("IOException caught");
        e.printStackTrace();
    } finally {
        try {
            if (resultSet != null) resultSet.close();
            if (preparedStatement != null) preparedStatement.close();
            if (connection != null) connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}


    @Override
    public void initialize(URL url, ResourceBundle rb) {
        ToggleGroup toggleGroup = new ToggleGroup();
        
        beginnerRadio.setToggleGroup(toggleGroup);
        masterRadio.setToggleGroup(toggleGroup);
        legendRadio.setToggleGroup(toggleGroup);
        
        beginnerRadio.setSelected(true);
        
    }
}
