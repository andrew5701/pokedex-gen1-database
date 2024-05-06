package com.mycompany.databaseexample;




import com.mycompany.databaseexample.Pokemon;
import com.mycompany.databaseexample.Trainer;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URISyntaxException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ScrollPane;
import javafx.stage.Stage;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javafx.geometry.Pos;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.application.Application;
import javafx.application.Platform;
import java.io.ByteArrayInputStream;
import javafx.fxml.FXML;
import javafx.event.ActionEvent;
import javafx.stage.FileChooser;
import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.Properties;
import java.nio.file.Files;
import java.nio.file.Path;



public class GymLeadersViewerController extends Application implements Initializable {

    private Parent root;
    private static Scene scene;
    private static Stage stage;
    
    private GymLeaders selectedLeader = null;
    
    private ObservableList<GymLeaders> data;

    @FXML
    private VBox vBox;
    
    @FXML
    private TextField nameTextField;

    @FXML
    Label footerLabel, textToChange2;
 
    
    @FXML
    FlowPane trainerPane;

    @Override
    public void initialize(URL location, ResourceBundle rb) {
        Properties properties = new Properties();
        FileInputStream inputStream = null;
        try{
        inputStream = new FileInputStream("config.properties");
        } catch(FileNotFoundException f){}
        try{
            properties.load(inputStream);
        }catch(IOException o){}

            // Retrieve the value associated with the key "key"
        String username = properties.getProperty("key");
        textToChange2.setText("Trainer " + username);
        
        
        
        try {
            loadData();
        } catch (SQLException ex) {
            
            System.out.println(ex.toString());
        }
        insertTrainers(data);
       
    }
    
    
    
    @FXML
    public void signOut(ActionEvent event) throws IOException{
        LoginController.clip.close();
        LoginController.playSound("music/main_theme.wav");
        Parent root = FXMLLoader.load(getClass().getResource("fxml/userLogin.fxml"));
        stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
        
        
    }
  





    
    public GymLeadersViewerController() throws SQLException {
        this.data = FXCollections.observableArrayList();
    }
    
    
    
    
    @Override
public void start(Stage primaryStage) {
    insertTrainers(data);

    Scene scene = new Scene(trainerPane, 800, 600);
    primaryStage.setScene(scene);
    primaryStage.setTitle("Pokémon Selector");

    // Set the click event handler on the scene's root
    scene.getRoot().setOnMouseClicked(event -> {
        if (!(event.getTarget() instanceof VBox)) {
            selectedLeader = null;
            resetVBoxStyles();
        }
    });

    primaryStage.show();
}


     private void resetVBoxStyles() {
    for (Node child : trainerPane.getChildren()) {
        if (child instanceof VBox) {
            child.setStyle(""); // Reset style
        }
    }
}


    private void highlightSelectedVBox(VBox container) {
        resetVBoxStyles();
        container.setStyle("-fx-border-color: blue; -fx-border-width: 2;");
    }
    
    private void insertTrainers(ObservableList<GymLeaders> data) {
        
//       ObservableList<GymLeaders> data1 = data != null ? data : this.data;

       
        trainerPane.getChildren().clear(); // Clear existing content before adding new children

for (GymLeaders leader : data) {
            ImageView imageView = null;
            if(leader.getLeaderImage()!= null){
                 imageView = new ImageView(new Image(new ByteArrayInputStream(leader.getLeaderImage())));

            }
            else{
                 imageView = new ImageView(new Image(getClass().getResourceAsStream("images/pokeball.jpg")));

            }
            imageView.setFitHeight(100);
            imageView.setFitWidth(100);
            imageView.setPreserveRatio(true);

            Label label = new Label("Name: " + leader.getLeaderName() + " Gen: " + leader.getRegionID() + " \nBadge: " + leader.getBadgeName());
            VBox container = new VBox(imageView, label);
            container.setAlignment(Pos.CENTER);

            container.setOnMouseClicked(event -> {
                nameTextField.setText(leader.getLeaderName());  
                
                
                selectedLeader = leader;
               
                highlightSelectedVBox(container);
                
                
                event.consume();
                
            });

            trainerPane.getChildren().add(container);
        }
      
    }


    public void loadData() throws SQLException {

        Connection conn = null;
        Statement stmt = null;
        
        if(!data.isEmpty()){
            data.clear();
        }
 
        try {

            // create a connection to the database
            conn = ConnectionHelp.getConn();

            System.out.println("Connection to SQLite has been established.");
            String sql = "SELECT * FROM GymLeaders;";
            // Ensure we can query the actors table
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                GymLeaders leader;
                leader = new GymLeaders(rs.getInt("LeaderID"),rs.getString("LeaderName"),rs.getInt("RegionID"),rs.getString("BadgeName"),rs.getBytes("LeaderImage"));
           
                data.add(leader);
            }

            rs.close();
            conn.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
        }
    }

    @FXML
    private void handleShowAllRecords(ActionEvent event) throws IOException, SQLException {
        insertTrainers(data);
        nameTextField.setText("");
    }
    
    
    @SuppressWarnings("empty-statement")
    public ObservableList<GymLeaders> search(String _name) throws SQLException, UnsupportedAudioFileException, URISyntaxException, IOException, LineUnavailableException {
        ObservableList<GymLeaders> searchResult = FXCollections.observableArrayList();
     
        String sql = "Select * from GymLeaders where 1=1";

        if (!_name.isEmpty()) {
            sql += " and LeaderName like '%" + _name + "%'";
        }
        
        System.out.println(sql);
        try (Connection conn = ConnectionHelp.getConn();
                Statement stmt = conn.createStatement()) {
            // create a ResultSet

            ResultSet rs = stmt.executeQuery(sql);
            // checking if ResultSet is empty
            if (rs.next() == false) {
                System.out.println("ResultSet in empty");
            } else {
                // loop through the result set
                do {

                    int leaderID = rs.getInt("LeaderID");
                    String leaderName = rs.getString("LeaderName");
                    int regionID = rs.getInt("RegionID");
                    String badgeName = rs.getString("BadgeName");
                    byte[] leaderImage = rs.getBytes("LeaderImage");

                    GymLeaders movie = new GymLeaders(leaderID,leaderName,regionID,badgeName,leaderImage);
                    searchResult.add(movie);
                } while (rs.next());
            }
            conn.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        
        
        return searchResult;
    }

    
    @FXML
    public void searchTrainer(ActionEvent event) throws IOException, SQLException, UnsupportedAudioFileException, URISyntaxException, LineUnavailableException{
        
        if(nameTextField.getText() != ""){
        File music = new File(getClass().getResource("music/pokemoncenter.wav").toURI());
        AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(music); 
        Clip clip = AudioSystem.getClip();
        clip.open(audioInputStream);
        clip.start();
        }
        
        String _name = nameTextField.getText().trim();
       
        
        ObservableList<GymLeaders> tableItems = search(_name);
        insertTrainers(tableItems);
        
        
        
    }
    
    
    
    
   
    
}
