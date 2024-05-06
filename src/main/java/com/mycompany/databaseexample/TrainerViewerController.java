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
import java.sql.CallableStatement;
import javax.swing.JOptionPane;



public class TrainerViewerController extends Application implements Initializable {

    private Trainer selectedTrainer = null;
    
    private ObservableList<Trainer> data;
    
    private Parent root;
    private static Scene scene;
    private static Stage stage;

    @FXML
    private VBox vBox;
    
    @FXML
    private TextField nameTextField, usernameTextField;

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

    public TrainerViewerController() throws SQLException {
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
            selectedTrainer = null;
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
    
    
    
    @FXML
    public void uploadProfPic(ActionEvent event) throws SQLException {
        // Load properties from file
        Properties properties = new Properties();
        try (FileInputStream inputStream = new FileInputStream("config.properties")) {
            properties.load(inputStream);
        } catch (IOException e) {
            e.printStackTrace(); // Handle the exception appropriately
            return;
        }

        // Retrieve the username from properties
        String username = properties.getProperty("key");

        // Open a file chooser dialog to let the user select an image
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif"));
        File selectedFile = fileChooser.showOpenDialog(null);

        if (selectedFile != null) {
            try {
                // Convert the file to a byte array
                byte[] profPic = Files.readAllBytes(selectedFile.toPath());

                // Update the database with the byte array for the user
                updateProfilePicture(username, profPic);
                
            } catch (IOException e) {
                e.printStackTrace(); // Handle the exception appropriately
            }
        }
        
    }

    private void updateProfilePicture(String username, byte[] profPic) throws SQLException {
        // Database URL
        

        // SQL query to update the profile picture for the given username
        String sql = "UPDATE trainer SET profPic = ? WHERE username = ?";

        // Use try-with-resources to ensure that resources are closed
        try (Connection conn = ConnectionHelp.getConn();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // Set the profPic as a binary stream
            pstmt.setBinaryStream(1, new ByteArrayInputStream(profPic));
            pstmt.setString(2, username);

            // Execute the update
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("Profile picture updated successfully.");
                
            } else {
                System.out.println("Could not update profile picture. User not found?");
            }

        } catch (Exception e) {
            e.printStackTrace(); // Handle the exception appropriately
        }
        loadData();
        insertTrainers(data);
    }

    private void insertTrainers(ObservableList<Trainer> data) {
        
       ObservableList<Trainer> data1 = data != null ? data : this.data;

       
        trainerPane.getChildren().clear(); // Clear existing content before adding new children

for (Trainer trainer : data1) {
            ImageView imageView = null;
            if(trainer.getProfPic()!= null){
                 imageView = new ImageView(new Image(new ByteArrayInputStream(trainer.getProfPic())));

            }
            else{
                 imageView = new ImageView(new Image(getClass().getResourceAsStream("images/pokeball.png")));

            }
            imageView.setFitHeight(100);
            imageView.setFitWidth(100);
            imageView.setPreserveRatio(true);
            
            String faveGame = "";
            
            
            String sql = "SELECT GameName FROM Games WHERE GameID = ?";
            if(trainer.getFavoriteGameID() != 0){
        try (Connection conn = ConnectionHelp.getConn();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, trainer.getFavoriteGameID()); // Set the GameID parameter

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    faveGame = rs.getString("GameName"); // Retrieve the game name
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
            }
            else{
                faveGame = "None";
            }
            
            
            

            Label label = new Label("Name: " + trainer.getName() + " Username: " 
                    + trainer.getUsername() + " \nRank: " + trainer.getRank() +
                    " \nPokemon Owned: " + trainer.getOwnedPokemon()
            + " \nFavorite Game: " + faveGame);
            VBox container = new VBox(imageView, label);
            container.setAlignment(Pos.CENTER);

            container.setOnMouseClicked(event -> {
                nameTextField.setText(trainer.getName());  
                usernameTextField.setText(trainer.getName());
                
                selectedTrainer = trainer;
               
                highlightSelectedVBox(container);
                
                
                event.consume();
                
            });

            trainerPane.getChildren().add(container);
        }
        //tableView.setOpacity(0.3);
        /* Allow for the values in each cell to be changable */
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
            String sql = "SELECT * FROM trainer;";
            // Ensure we can query the actors table
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                Trainer trainer;
                trainer = new Trainer(rs.getInt("id"), rs.getString("name"), rs.getString("username"),rs.getString("rank"),rs.getString("ownedPokemon"), rs.getBytes("profPic"), rs.getInt("FavoriteGameID"));
           
                data.add(trainer);
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
        usernameTextField.setText("");
        
    }
    
    
    @SuppressWarnings("empty-statement")
    public ObservableList<Trainer> search(String _name, String _username) throws SQLException, UnsupportedAudioFileException, URISyntaxException, IOException, LineUnavailableException {
        ObservableList<Trainer> searchResult = FXCollections.observableArrayList();
        // read data from SQLite database
       
        String sql = "Select * from trainer where 1=1";

        if (!_name.isEmpty()) {
            sql += " and name like '%" + _name + "%'";
        }
        
        if (!_username.isEmpty()) {
            sql += " and username like '%" + _username + "%'";
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

                    int recordId = rs.getInt("id");
                    String name = rs.getString("name");
                    String username = rs.getString("username");
                    String rank = rs.getString("rank");
                    String type = rs.getString("ownedPokemon");
                    byte[] profPic = rs.getBytes("profPic");
                    int favGame = rs.getInt("FavoriteGameID");

                    Trainer movie = new Trainer(recordId, name, username,rank, type,profPic,favGame);
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
        String _username = usernameTextField.getText().trim();
        
        ObservableList<Trainer> tableItems = search(_name,_username);
        insertTrainers(tableItems);
        
    }
    
    
    
    
    
    
    
    @FXML
    public void tradeWith(ActionEvent event) throws SQLException, IOException {
    Properties properties = new Properties();
    FileInputStream inputStream = null;
    Connection connection = null;
    try {
        inputStream = new FileInputStream("config.properties");
        properties.load(inputStream);

       
        String username = properties.getProperty("key");
        String secondName = selectedTrainer.getUsername();

       
        connection = ConnectionHelp.getConn();
        connection.setAutoCommit(false); 
        
        String trainer1PokemonName = getTrainersPokemonName(connection,username); 
        String trainer2PokemonName = getTrainersPokemonName(connection,secondName); 

       
        performTrade(connection, username, trainer1PokemonName, secondName, trainer2PokemonName);

        connection.commit(); 
        JOptionPane.showMessageDialog(null, "Trade successful!");

    } catch (FileNotFoundException f) {
        
    } catch (IOException | SQLException o) {
    
        if (connection != null) {
            try {
                connection.rollback();
            } catch (SQLException ex) {
               
            }
        }
    } finally {
 
        try {
            if (inputStream != null) {
                inputStream.close();
            }
            if (connection != null) {
                connection.setAutoCommit(true);
                connection.close();
            }
        } catch (IOException | SQLException e) {
           
        }
    }

    loadData();
    insertTrainers(null);

        FXMLLoader loader = new FXMLLoader(getClass().getResource("fxml/Trade.fxml"));
        Parent root = loader.load();

        TradeController tradeController = loader.getController();
        Scene currentScene = ((Node)event.getSource()).getScene().getWindow().getScene();
        tradeController.setPreviousScene(currentScene); // Set the previous scene

        stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
}

private void performTrade(Connection connection, String trainer1Name, String trainer1PokemonName, String trainer2Name, String trainer2PokemonName) throws SQLException {
    
    String sql = "{call TradePokemon(?, ?)}";
try (CallableStatement cstmt = connection.prepareCall(sql)) {
    cstmt.setString(1, trainer1Name);
    cstmt.setString(2, trainer2Name);
    cstmt.execute();
   
}

}


private String getTrainersPokemonName(Connection connection, String trainerName) throws SQLException {
    String query = "SELECT ownedPokemon FROM trainer WHERE username = ?";
    try (PreparedStatement pstmt = connection.prepareStatement(query)) {
        pstmt.setString(1, trainerName);
        try (ResultSet rs = pstmt.executeQuery()) {
            if (rs.next()) {
                return rs.getString("ownedPokemon");
            } else {
                throw new SQLException("No Pokémon found for trainer ID: " + trainerName);
            }
        }
    }
}


@FXML
public void executeTransaction(ActionEvent event) {
    Connection conn = null;
    PreparedStatement pstmt = null;

    try {
        conn = ConnectionHelp.getConn();
        conn.setAutoCommit(false); // Start transaction

        String sql = "INSERT INTO trainer (username, password, name, ownedPokemon, rank) VALUES (?, ?, ?, ?, ?)";
        pstmt = conn.prepareStatement(sql);

        pstmt.setString(1, "newUser");
        pstmt.setString(2, "newPass");
        pstmt.setString(3, "New User");
        pstmt.setString(4, "Pikachu");
        pstmt.setString(5, "Beginner");

        pstmt.executeUpdate();
        conn.commit(); // Commit transaction

        JOptionPane.showMessageDialog(null, "Transaction executed successfully.");
    } catch (SQLException se) {
        if (conn != null) {
            try {
                conn.rollback(); // Rollback on error
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        se.printStackTrace();
        JOptionPane.showMessageDialog(null, "Transaction failed: " + se.getMessage());
    } finally {
        try {
            if (pstmt != null) pstmt.close();
            if (conn != null) conn.close();
        } catch (SQLException se) {
            se.printStackTrace();
        }
    }
}


@FXML
public void performDirtyRead() {
    String readUncommittedQuery = "SET TRANSACTION ISOLATION LEVEL READ UNCOMMITTED;";
    String selectQuery = "SELECT * FROM trainer;"; // Your SQL SELECT query
    String readCommittedQuery = "SET TRANSACTION ISOLATION LEVEL READ COMMITTED;";

    try (Connection con = ConnectionHelp.getConn();
         Statement stmt = con.createStatement()) {


        stmt.execute(readUncommittedQuery);


        try (ResultSet rs = stmt.executeQuery(selectQuery)) {
            while (rs.next()) {

                int id = rs.getInt("id");
                String name = rs.getString("name");

                System.out.println("Trainer ID: " + id + ", Name: " + name);
            }
        }


        stmt.execute(readCommittedQuery);

    } catch (SQLException e) {
        e.printStackTrace();
    }
}

   
}
