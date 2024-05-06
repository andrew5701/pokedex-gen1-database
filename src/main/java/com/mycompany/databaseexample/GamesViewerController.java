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
import java.util.Comparator;

public class GamesViewerController extends Application implements Initializable {

    private Games selectedGame = null;
    
    private Parent root;
    private static Scene scene;
    private static Stage stage;

    @FXML
    private VBox vBox;
    
    @FXML
    private TextField nameTextField, usernameTextField;
    

//    @FXML
//    private TextField nameTextField, typeTextField, levelTextField;

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
        
        insertTrainers(null);
       
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


    /* Connect to a sample database
     */
    private ObservableList<Games> data;

    /*
       ArrayList: Resizable-array implementation of the List interface. 
       Implements all optional list operations, and permits all elements, including null.
       ObservableList: A list that allows listeners to track changes when they occur
    */
    
    
    public GamesViewerController() throws SQLException {
        this.data = FXCollections.observableArrayList();
    }
    
    
    
    
    @Override
public void start(Stage primaryStage) {
    insertTrainers(null);

    Scene scene = new Scene(trainerPane, 800, 600);
    primaryStage.setScene(scene);
    primaryStage.setTitle("Pokémon Selector");

    // Set the click event handler on the scene's root
    scene.getRoot().setOnMouseClicked(event -> {
        if (!(event.getTarget() instanceof VBox)) {
            selectedGame = null;
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
    
    
    
   

    

    private void insertTrainers(ObservableList<Games> data) {
        
       ObservableList<Games> data1 = data != null ? data : this.data;

       
        trainerPane.getChildren().clear(); // Clear existing content before adding new children
        
        ObservableList<Games> sortedData = data1
            .sorted(Comparator.comparingInt(g -> {
            String releaseDate = g.getReleaseDate();
            if (releaseDate != null && !releaseDate.isEmpty()) {
                try {
                    return Integer.parseInt(releaseDate);
                } catch (NumberFormatException e) {
                    // Handle parsing error or log it
                    System.out.println("Error parsing release date: " + releaseDate);
                }
            }
            return Integer.MAX_VALUE; // Or a default value indicating erroneous or missing date
        }));
        
for (Games game : sortedData) {
            ImageView imageView = null;
            if(game.getGameImage()!= null){
                 imageView = new ImageView(new Image(new ByteArrayInputStream(game.getGameImage())));

            }
            else{
                 imageView = new ImageView(new Image(getClass().getResourceAsStream("images/pokeball.jpg")));

            }
            imageView.setFitHeight(100);
            imageView.setFitWidth(100);
            imageView.setPreserveRatio(true);

            Label label = new Label("Name: " + game.getGameName() + " Gen: " + game.getRegionID() + " \nYear: " + game.getReleaseDate());
            VBox container = new VBox(imageView, label);
            container.setAlignment(Pos.CENTER);

            container.setOnMouseClicked(event -> {
                nameTextField.setText(game.getGameName());  
               
                
                selectedGame = game;
               
                highlightSelectedVBox(container);
                
                
                event.consume();
                
            });

            trainerPane.getChildren().add(container);
        }
        //tableView.setOpacity(0.3);
        /* Allow for the values in each cell to be changable */
    }
    
    
    
       public void insert(String name) throws SQLException {
    int last_inserted_id = 0;
    Connection conn = null;
    PreparedStatement pstmt = null;
    ResultSet rs = null;
    try {
        // create a connection to the database
        conn = ConnectionHelp.getConn();
        System.out.println("Connection to SQL Server has been established.");

        System.out.println("Inserting one record!");
        String sql = "INSERT INTO Games(name) VALUES(?)";

        // Prepare the statement with the option to retrieve auto-generated keys
        pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        pstmt.setString(1, name);
        
        pstmt.executeUpdate(); // Execute the update

        // Retrieve the generated keys
        rs = pstmt.getGeneratedKeys();
        if (rs.next()) {
            last_inserted_id = rs.getInt(1); // Get the last inserted id
        }
        System.out.println("last_inserted_id " + last_inserted_id);

        // Assuming data is a list where you store Pokémon objects
        // Add the new Pokémon with the generated ID to the list
        data.add(new Games(last_inserted_id, name,0,null,null));

    } catch (SQLException e) {
        System.out.println(e.getMessage());
    } finally {
        try {
            // It's important to close ResultSet, PreparedStatement, and Connection in this order to avoid any resource leaks
            if (rs != null) {
                rs.close();
            }
            if (pstmt != null) {
 pstmt.close();
            }
            if (conn != null) {
                conn.close();
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }
}

    @FXML
    public void handleAddMovie() throws UnsupportedAudioFileException, LineUnavailableException, IOException, URISyntaxException, SQLException {
        
        
        
        System.out.println("Title: " + nameTextField.getText());

        try {
            // insert a new rows
            insert(nameTextField.getText());

            System.out.println("Data was inserted Successfully");
            File music = new File(getClass().getResource("music/itempickup.wav").toURI());
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(music); 
            Clip clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            clip.start();
        } catch (SQLException ex) {
            System.out.println(ex.toString());
        }

        nameTextField.setText("");
    
        
        insertTrainers(null);

//        footerLabel.setText("Record inserted into table successfully!");
    }


     public void loadData() throws SQLException {

        Connection conn = null;
        Statement stmt = null;
        
//        if(!data.isEmpty()){
//            data.clear();
//        }
 
        try {

            // create a connection to the database
            conn = ConnectionHelp.getConn();

            System.out.println("Connection to SQLite has been established.");
            String sql = "SELECT * FROM Games;";
            // Ensure we can query the actors table
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                Games game;
                game = new Games(rs.getInt("GameID"),rs.getString("GameName"),rs.getInt("RegionID"),rs.getString("ReleaseDate"),rs.getBytes("GameImage"));
           
                data.add(game);
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
        insertTrainers(null);
        nameTextField.setText("");
        
        
    }
    
    
    @SuppressWarnings("empty-statement")
    public ObservableList<Games> search(String _name) throws SQLException, UnsupportedAudioFileException, URISyntaxException, IOException, LineUnavailableException {
        ObservableList<Games> searchResult = FXCollections.observableArrayList();
        // read data from SQLite database
   
        String sql = "Select * from Games where 1=1";

        if (!_name.isEmpty()) {
            sql += " and GameName like '%" + _name + "%'";
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

                 
                    
                    int gameID = rs.getInt("GameID");
                    String gameName = rs.getString("GameName");
                    int regionID = rs.getInt("RegionID");
                    String releaseDate = rs.getString("ReleaseDate");
                    byte[] gameImage = rs.getBytes("GameImage");

                    Games movie = new Games(gameID, gameName, regionID, releaseDate, gameImage);
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
        
        if(nameTextField.getText() != "" || usernameTextField.getText() != null){
        File music = new File(getClass().getResource("music/pokemoncenter.wav").toURI());
        AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(music); 
        Clip clip = AudioSystem.getClip();
        clip.open(audioInputStream);
        clip.start();
        }
        
        String _name = nameTextField.getText().trim();

        ObservableList<Games> tableItems = search(_name);
        insertTrainers(tableItems);    
    }
    
     @FXML
    public void favoriteGame(ActionEvent event) {
        Properties properties = new Properties();
        try (FileInputStream inputStream = new FileInputStream("config.properties")) {
            properties.load(inputStream);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            // Handle file not found exception
        } catch (IOException e) {
            e.printStackTrace();
            // Handle IO exception
        }

        String username = properties.getProperty("key");
        updateFavoriteGame(username, selectedGame.getGameID());
    }

    private void updateFavoriteGame(String username, int gameId) {
        String sql = "UPDATE Trainer SET FavoriteGameID = ? WHERE Username = ?";
        
        try (Connection conn = ConnectionHelp.getConn();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, gameId);
            pstmt.setString(2, username);

            int affectedRows = pstmt.executeUpdate();

            if (affectedRows > 0) {
                showAlert("Success", "Favorite game updated successfully!");
            } else {
                showAlert("Update Failed", "No records updated. Please check the username.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Database Error", "Error updating favorite game in the database.");
        }
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
    
    
    
    
   
    
}
