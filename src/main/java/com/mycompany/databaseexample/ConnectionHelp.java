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
public class ConnectionHelp {
  
    
    public static Connection getConn(){
        String userName = "andrew";
        String password = "123";
        String url = "jdbc:sqlserver://localhost:1433;databaseName=pokemon;encrypt=true;trustServerCertificate=true";
        Connection conn = null;
        try{
            conn = DriverManager.getConnection(url, userName, password);
        }catch(SQLException ex){
            System.out.println(ex.getMessage());
        }
        return conn;
    }
    
}
