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



public class RegionsViewerController extends Application implements Initializable {

    private Regions selectedRegion = null;
    
    @FXML
    private TableView tableView;

    @FXML
    private VBox vBox;
    
    @FXML
    private TextField nameTextField, usernameTextField;
    

//    @FXML
//    private TextField nameTextField, typeTextField, levelTextField;

    @FXML
    Label footerLabel, textToChange2;
    
    @FXML
    TableColumn id = new TableColumn("ID");
    
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
        insertRegions(data);
        CreateSQLiteTable();
    }
    
    private Parent root;
    private static Scene scene;
    private static Stage stage;
    
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

    String databaseURL = "jdbc:sqlite:src/main/resources/com/mycompany/databaseexample/pokemon.db";

    /* Connect to a sample database
     */
    private ObservableList<Regions> data;

    /*
       ArrayList: Resizable-array implementation of the List interface. 
       Implements all optional list operations, and permits all elements, including null.
       ObservableList: A list that allows listeners to track changes when they occur
    */
    
    
    public RegionsViewerController() throws SQLException {
        this.data = FXCollections.observableArrayList();
    }
    
    
    
    
    @Override
public void start(Stage primaryStage) {
    insertRegions(data);

    Scene scene = new Scene(trainerPane, 800, 600);
    primaryStage.setScene(scene);
    primaryStage.setTitle("Pokémon Selector");

    // Set the click event handler on the scene's root
    scene.getRoot().setOnMouseClicked(event -> {
        if (!(event.getTarget() instanceof VBox)) {
            selectedRegion = null;
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
    
    

    

    private void insertRegions(ObservableList<Regions> data) {
        
       ObservableList<Regions> data1 = data != null ? data : this.data;

       int gen = 1;
        trainerPane.getChildren().clear(); // Clear existing content before adding new children

for (Regions region : data1) {
            ImageView imageView = null;
            if(region.getRegionImage()!= null){
                 imageView = new ImageView(new Image(new ByteArrayInputStream(region.getRegionImage())));

            }
            else{
                 imageView = new ImageView(new Image(getClass().getResourceAsStream("images/pokeball.jpg")));

            }
            imageView.setFitHeight(100);
            imageView.setFitWidth(100);
            imageView.setPreserveRatio(true);

            Label label = new Label("Gen " + gen++ + ": " + region.getRegionName());
            VBox container = new VBox(imageView, label);
            container.setAlignment(Pos.CENTER);

            container.setOnMouseClicked(event -> {
                nameTextField.setText(region.getRegionName());  
              
                
                selectedRegion = region;
               
                highlightSelectedVBox(container);
                
                
                event.consume();
                
            });

            trainerPane.getChildren().add(container);
        }
        //tableView.setOpacity(0.3);
        /* Allow for the values in each cell to be changable */
    }
     private void CreateSQLiteTable() {
    // SQL statement for creating a new table
    String sql = "CREATE TABLE IF NOT EXISTS trainer (\n"
            + "	id INTEGER PRIMARY KEY AUTOINCREMENT,\n"
            + "	name TEXT NOT NULL,\n"
            + "	username TEXT NOT NULL UNIQUE,\n"
            + "	rank TEXT NOT NULL,\n"
            + "	password TEXT NOT NULL UNIQUE,\n"
            + "	ownedPokemon TEXT UNIQUE\n"
            + ");";

    try (Connection conn = ConnectionHelp.getConn();
            Statement stmt = conn.createStatement()) {
        // create a new table
        stmt.execute(sql);
        System.out.println("Table Created Successfully");
        conn.close();
    } catch (SQLException e) {
        System.out.println(e.getMessage());
    }
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
            String sql = "SELECT * FROM Regions;";
            // Ensure we can query the actors table
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                Regions region;
                region = new Regions(rs.getInt("RegionID"),rs.getString("RegionName"),rs.getBytes("RegionImage"));
           
                data.add(region);
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

    public void drawText() {
        //Drawing a text 
        Text text = new Text("The Trainer Database");

        //Setting the font of the text 
        text.setFont(Font.font("Edwardian Script ITC", 55));

        //Setting the position of the text 
//        text.setX(600);
//        text.setY(100);
        //Setting the linear gradient 
        Stop[] stops = new Stop[]{
            new Stop(0, Color.DARKSLATEBLUE),
            new Stop(1, Color.RED)
        };
        LinearGradient linearGradient = new LinearGradient(0, 0, 1, 0, true, CycleMethod.NO_CYCLE, stops);

        //Setting the linear gradient to the text 
        text.setFill(linearGradient);
        // Add the child to the grid
        vBox.getChildren().add(text);

    }

    /**
     * Insert a new row into the movies table
     *
     * @param title
     * @param year
     * @param rating
     * @throws java.sql.SQLException
     */
 
  
//    private void CreateSQLiteTable() {
//        // SQL statement for creating a new table
//        String sql = "CREATE TABLE IF NOT EXISTS pokemon (\n"
//                + "	id integer PRIMARY KEY (\"id\" AUTOINCREMENT),\n"
//                + "	name text NOT NULL,\n"
//                + "	type text NOT NULL,\n"
//                + "	level int NOT NULL\n"
//                + ");";
//
//        try (Connection conn = DriverManager.getConnection(databaseURL);
//                Statement stmt = conn.createStatement()) {
//            // create a new table
//            stmt.execute(sql);
//            System.out.println("Table Created Successfully");
//        } catch (SQLException e) {
//            System.out.println(e.getMessage());
//        }
//    }

       Integer index = -1;

    @FXML
    private void showRowData() {

        index = tableView.getSelectionModel().getSelectedIndex();
        if (index <= -1) {
            return;
        }

        System.out.println("showRowData");
        System.out.println(index);
        Trainer movie = (Trainer) tableView.getSelectionModel().getSelectedItem();
        System.out.println("ID: " + movie.getId());
        System.out.println("Title: " + movie.getName());
        System.out.println("Username: " + movie.getUsername());
        System.out.println("Year: " + movie.getRank());
        System.out.println("Rating: " + movie.getOwnedPokemon());

        

        String content = "Id= " + movie.getId() + "\nName= " + movie.getName() + "\nUsername= " + movie.getUsername() + "\nType= " + movie.getRank()  + "\nRating= " + movie.getOwnedPokemon();

    }
    
    @FXML
    private void handleShowAllRecords(ActionEvent event) throws IOException, SQLException {
        insertRegions(data);
        nameTextField.setText("");
        usernameTextField.setText("");
        
    }
    
    
    @SuppressWarnings("empty-statement")
    public ObservableList<Regions> search(String _name) throws SQLException, UnsupportedAudioFileException, URISyntaxException, IOException, LineUnavailableException {
        ObservableList<Regions> searchResult = FXCollections.observableArrayList();
        // read data from SQLite database
        CreateSQLiteTable();
        String sql = "Select * from Regions where 1=1";

        if (!_name.isEmpty()) {
            sql += " and RegionName like '%" + _name + "%'";
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

                    
                    
                    int regionID = rs.getInt("RegionID");
                    String regionName = rs.getString("RegionName");
                    byte[] regionImage = rs.getBytes("RegionImage");

                    Regions movie = new Regions(regionID,regionName,regionImage);
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
        
        ObservableList<Regions> tableItems = search(_name);
        insertRegions(tableItems);
        
        
        
    }
    
    
    
    
   
    
}
