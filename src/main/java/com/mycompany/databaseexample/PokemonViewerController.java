package com.mycompany.databaseexample;





import com.mycompany.databaseexample.Pokemon;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import java.util.Properties;
//import javafx.scene.media.MediaPlayer;
//import javafx.scene.media.MediaView;
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








public class PokemonViewerController extends Application implements Initializable {


    private Pokemon selectedPokemon = null;
//    @FXML
//    private TableView tableView;
    @FXML
    ScrollPane scroll;
    
    @FXML
    FlowPane pokemonFlowPane;

    @FXML
    private VBox vBox;
    
    @FXML
    Label ownThis;

    @FXML
    private TextField nameTextField, typeTextField, levelTextField;

    @FXML
    Label footerLabel, textToChange, typeLabel, descriptionLabel;
    
    @FXML
    TableColumn id = new TableColumn("ID");

    @Override
    public void initialize(URL location, ResourceBundle rb){
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
//        textToChange.setText("Trainer " + username);
        try {
            
          loadData();
        } catch (SQLException ex) {
            
            System.out.println(ex.toString());
        }
        insertPokemon(null);
        CreateSQLiteTable();
    }

    String databaseURL = "jdbc:sqlite:src/main/resources/com/mycompany/databaseexample/pokemon.db";

    /* Connect to a sample database
     */
    private ObservableList<Pokemon> data;

    /*
       ArrayList: Resizable-array implementation of the List interface. 
       Implements all optional list operations, and permits all elements, including null.
       ObservableList: A list that allows listeners to track changes when they occur
    */
    
    
    public PokemonViewerController() throws SQLException {
        this.data = FXCollections.observableArrayList();
    }
    
@Override
public void start(Stage primaryStage) {
    insertPokemon(null);

    Scene scene = new Scene(pokemonFlowPane, 800, 600);
    primaryStage.setScene(scene);
    primaryStage.setTitle("Pokemon Selector");

    // Set the click event handler on the scene's root
    scene.getRoot().setOnMouseClicked(event -> {
        if (!(event.getTarget() instanceof VBox)) {
            selectedPokemon = null;
            resetVBoxStyles();
        }
    });

    primaryStage.show();
}


     private void resetVBoxStyles() {
    for (Node child : pokemonFlowPane.getChildren()) {
        if (child instanceof VBox) {
            child.setStyle(""); // Reset style
        }
    }
}


    private void highlightSelectedVBox(VBox container) {
        resetVBoxStyles();
        container.setStyle("-fx-border-color: blue; -fx-border-width: 2;");
    }
    
   public String cleanDescription(String description) {
    // Replace the caret and "M" which represents a Windows carriage return (^M) with a space
    description = description.replace("^M", " ");
    
    // Replace escaped newlines and carriage returns with a space
    description = description.replace("\\n", " ").replace("\\r", " ");
    
    // Replace actual newlines and carriage returns with a space, if they exist
    description = description.replace("\n", " ").replace("\r", " ");
    
    // Optional: remove any remaining control characters if they are not needed
    description = description.replaceAll("[\\p{Cntrl}&&[^\r\n\t]]", " ");
    
    // Normalize space characters, replacing multiple spaces with a single space
    description = description.replaceAll("\\s+", " ");
    
    return description;
}


    
    private void insertPokemon(ObservableList<Pokemon> data) {

       ObservableList<Pokemon> data1 = data != null ? data : this.data;

        pokemonFlowPane.getChildren().clear(); // Clear existing content before adding new children

for (Pokemon pokemon : data1) {
            ImageView imageView = null;
            if(pokemon.getImageUrl() != null && getClass().getResourceAsStream(pokemon.getImageUrl()) != null){
                 imageView = new ImageView(new Image(getClass().getResourceAsStream(pokemon.getImageUrl())));

            }
            else{
                 imageView = new ImageView(new Image(getClass().getResourceAsStream("images/pokeball.png")));

            }
            imageView.setFitHeight(100);
            imageView.setFitWidth(100);
            imageView.setPreserveRatio(true);

            Label label = new Label(pokemon.getName() + " (#" + pokemon.getPokedexNo() + ")");
            VBox container = new VBox(imageView, label);
            container.setAlignment(Pos.CENTER);

            container.setOnMouseClicked(event -> {
                nameTextField.setText(pokemon.getName());  
                typeTextField.setText(pokemon.getType());
                levelTextField.setText(String.valueOf(pokemon.getPokedexNo()));
                selectedPokemon = pokemon;
               
                highlightSelectedVBox(container);
                typeLabel.setText("Type: " + pokemon.getType());
                Platform.runLater(() -> {
            descriptionLabel.setText("Loading...");
            String description = PokemonDescriptionFetcher.getPokemonDescription(pokemon.getName());
            
            description = cleanDescription(description);

            descriptionLabel.setText("Description: " + description);
            System.out.println(description);
        });
                event.consume();
                
            });

            pokemonFlowPane.getChildren().add(container);
        }




    }

    public void loadData() throws SQLException {

        Connection conn = null;
        Statement stmt = null;
 
        try {

            // create a connection to the database
            conn = ConnectionHelp.getConn();

            System.out.println("Connection to SQLite has been established.");
            String sql = "SELECT * FROM pokemon;";
            // Ensure we can query the actors table
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                Pokemon pokemon;
                pokemon = new Pokemon(rs.getInt("id"), rs.getString("name"),rs.getString("type"), rs.getInt("pokedexNo"), rs.getString("imageUrl"));
                System.out.println(pokemon.getId() + " - " + pokemon.getName() + " - " + pokemon.getType() + " - " + pokemon.getPokedexNo());
                data.add(pokemon);
            }

            rs.close();
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

   


    public void insert(String name, String type, int level) throws SQLException {
    int last_inserted_id = 0;
    Connection conn = null;
    PreparedStatement pstmt = null;
    ResultSet rs = null;
    try {
        // create a connection to the database
        conn = ConnectionHelp.getConn();
        System.out.println("Connection to SQL Server has been established.");

        System.out.println("Inserting one record!");
        String sql = "INSERT INTO pokemon(name,type,pokedexNo) VALUES(?,?,?)";

        // Prepare the statement with the option to retrieve auto-generated keys
        pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        pstmt.setString(1, name);
        pstmt.setString(2, type);
        pstmt.setInt(3, level);
        pstmt.executeUpdate(); // Execute the update

        // Retrieve the generated keys
        rs = pstmt.getGeneratedKeys();
        if (rs.next()) {
            last_inserted_id = rs.getInt(1); // Get the last inserted id
        }
        System.out.println("last_inserted_id " + last_inserted_id);

        // Assuming data is a list where you store Pokemon objects
        // Add the new Pokemon with the generated ID to the list
        data.add(new Pokemon(last_inserted_id, name, type, level,null));

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
        
        
        
        System.out.println("Title: " + nameTextField.getText() + "\nYear: " + levelTextField.getText() + "\nRating: " + typeTextField.getText());

        try {
            // insert a new rows
            insert(nameTextField.getText(),typeTextField.getText(), Integer.parseInt(levelTextField.getText()));

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
        typeTextField.setText("");
        levelTextField.setText("");
        
        insertPokemon(null);

//        footerLabel.setText("Record inserted into table successfully!");
    }

    private void CreateSQLiteTable() {
        // SQL statement for creating a new table
        String sql = "CREATE TABLE IF NOT EXISTS pokemon (\n"
                + "	id integer PRIMARY KEY (\"id\" AUTOINCREMENT),\n"
                + "	name text NOT NULL,\n"
                + "	type text NOT NULL,\n"
                + "	level int NOT NULL\n"
                + ");";

        try (Connection conn = ConnectionHelp.getConn();
                Statement stmt = conn.createStatement()) {
            // create a new table
            stmt.execute(sql);
            System.out.println("Table Created Successfully");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

   public void deleteRecord() throws SQLException, IOException, LineUnavailableException, URISyntaxException, UnsupportedAudioFileException {
    try (Connection conn = ConnectionHelp.getConn(); 
         PreparedStatement preparedStatement = conn.prepareStatement("DELETE FROM pokemon WHERE id = ?;")) {

        preparedStatement.setInt(1, selectedPokemon.getId());
        preparedStatement.executeUpdate();
        System.out.println("Record Deleted Successfully");

        // Remove the selected Pokemon from the data list
        data.remove(selectedPokemon);
        
        // Refresh the UI
        insertPokemon(data);

        // Play sound effect
        playSoundEffect("music/pokemonfaint.wav");
    } catch (SQLException e) {
        System.out.println(e.getMessage());
    }
}

private void playSoundEffect(String soundFilePath) throws UnsupportedAudioFileException, IOException, LineUnavailableException, URISyntaxException {
    File music = new File(getClass().getResource(soundFilePath).toURI());
    AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(music);
    Clip clip = AudioSystem.getClip();
    clip.open(audioInputStream);
    clip.start();
}



    @FXML
    private void handleDeleteAction() throws SQLException, UnsupportedAudioFileException, LineUnavailableException, IOException, URISyntaxException {
        deleteRecord();
        
    }

    Integer index = -1;


    public ObservableList<Pokemon> search(String _name, String _type, String _level) throws SQLException {
    ObservableList<Pokemon> searchResult = FXCollections.observableArrayList();

    String sql = "Select * from pokemon where 1=1"; // Changed for SQL Server compatibility

    if (!_name.isEmpty()) {
        sql += " and name like '%" + _name + "%'";
    }
    if (!_type.isEmpty()) {
        sql += " and type like '%" + _type + "%'";
    }
    if (!_level.isEmpty()) {
        sql += " and pokedexNo ='" + _level + "'";
    }

    System.out.println(sql);
    try (Connection conn = ConnectionHelp.getConn();
         Statement stmt = conn.createStatement()) {

        ResultSet rs = stmt.executeQuery(sql);
        
        if (!rs.next()) {
            System.out.println("ResultSet is empty");
        } else {
            do {
                int recordId = rs.getInt("id");
                String name = rs.getString("name");
                int level = rs.getInt("pokedexNo"); // Directly getting int
                String type = rs.getString("type");
                String url = rs.getString("imageUrl");
                
                System.out.println(name);

                Pokemon pokemon = new Pokemon(recordId, name, type, level, url);
                searchResult.add(pokemon);
            } while (rs.next());
        }
    } catch (SQLException e) {
        System.out.println(e.getMessage());
    }
    return searchResult;
}


    @FXML
    private void handleSearchAction() throws IOException, SQLException, UnsupportedAudioFileException, URISyntaxException, LineUnavailableException {
        if(nameTextField.getText() != "" || typeTextField.getText() != "" || levelTextField.getText() != ""){
        File music = new File(getClass().getResource("music/pokemoncenter.wav").toURI());
        AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(music); 
        Clip clip = AudioSystem.getClip();
        clip.open(audioInputStream);
        clip.start();
        }

        
        
        String _name = nameTextField.getText().trim();
        String _level = levelTextField.getText().trim();
        String _type = typeTextField.getText().trim();
        ObservableList<Pokemon> tableItems = search(_name, _type, _level);
        
        insertPokemon(tableItems);
        
//        tableView.setItems(tableItems);

    }

    @FXML
    private void handleShowAllRecords() throws IOException, SQLException {
//        tableView.setItems(data);
        insertPokemon(null);
        nameTextField.setText("");
        typeTextField.setText("");
        levelTextField.setText("");

    }


    public void update(String name, String type, int level, int id) throws SQLException, IOException, LineUnavailableException, URISyntaxException, UnsupportedAudioFileException {
    Connection conn = null;
    PreparedStatement pstmt = null;
    try {
        // create a connection to the database
        conn = ConnectionHelp.getConn();
        String sql = "UPDATE pokemon SET name = ?, type = ?, pokedexNo = ? WHERE id = ?;";
        pstmt = conn.prepareStatement(sql);
        pstmt.setString(1, name);
        pstmt.setString(2, type);
        pstmt.setInt(3, level);
        pstmt.setInt(4, id);

        pstmt.executeUpdate();

        playSoundEffect("music/itempickup.wav");

        // Refresh the data list and UI
        data.clear();
        loadData();
        insertPokemon(null);

    } catch (SQLException e) {
        System.out.println(e.getMessage());
    } finally {
        if (pstmt != null) {
            pstmt.close();
        }
        if (conn != null) {
            conn.close();
        }
    }
}
    
    

    @FXML
    private void handleUpdateRecord() throws IOException, SQLException, UnsupportedAudioFileException, URISyntaxException, LineUnavailableException {

        
                try {
                    // insert a new rows
                    update(nameTextField.getText(),typeTextField.getText(), Integer.parseInt(levelTextField.getText()), selectedPokemon.getId());

                    System.out.println("Record updated successfully!");
                } catch (SQLException ex) {
                    System.out.println(ex.toString());
                }

                
           

        
        nameTextField.setText("");
        typeTextField.setText("");
        levelTextField.setText("");

     
        

    }

   

    

  
@FXML
private void ownThisPokemon() {
    Alert a = new Alert(Alert.AlertType.NONE);
    Connection conn = null;
    PreparedStatement pstmt = null;
    ResultSet resultSet = null;
    String wantedPokemon = nameTextField.getText();

    if (wantedPokemon.isEmpty()) {
        a.setAlertType(Alert.AlertType.ERROR);
        a.setContentText("Select a Pokemon first.");
        a.getButtonTypes().setAll(ButtonType.OK);
        a.setGraphic(null);
        a.show();
    } else {
        try {
            Properties properties = new Properties();
            FileInputStream inputStream = new FileInputStream("config.properties");
            properties.load(inputStream);
            inputStream.close();

            String username = properties.getProperty("key"); // Assume this is the username
            // create a connection to the database
            conn = ConnectionHelp.getConn();

            String sql = "SELECT * FROM trainer WHERE userName = ? AND ownedPokemon = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, username);
            pstmt.setString(2, wantedPokemon);
            resultSet = pstmt.executeQuery();

            if (!resultSet.next()) {
                pstmt.close();
                sql = "UPDATE trainer SET ownedPokemon = ? WHERE username = ?";
                pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, wantedPokemon);
                pstmt.setString(2, username);

                pstmt.executeUpdate();

                // Play the sound
                // ... sound playing code ...

                a.setAlertType(Alert.AlertType.CONFIRMATION);
                a.setContentText("You now own " + wantedPokemon + "!");
            } else {
                String trainerName = resultSet.getString("name");
                String trainerUsername = resultSet.getString("username");

                a.setAlertType(Alert.AlertType.ERROR);
                
                    a.setContentText("You already own " + wantedPokemon + ".");
                
            }
            a.getButtonTypes().setAll(ButtonType.OK);
            a.setGraphic(null);
            a.show();
        } catch (SQLException | IOException e) {
            System.out.println(e.getMessage());
            // Handle other exceptions appropriately
        } finally {
            // Close resources in the reverse order of their opening
            try {
                if (resultSet != null) resultSet.close();
                if (pstmt != null) pstmt.close();
                if (conn != null) conn.close();
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
        }
    }
}

    
    private Parent root;
    private static Scene scene;
    private static Stage stage;
    
    @FXML
    public void signOut(ActionEvent event) throws IOException{
//        LoginController.clip.close();
        LoginController.clip.close();
        LoginController.playSound("music/main_theme.wav");
        Parent root = FXMLLoader.load(getClass().getResource("fxml/userLogin.fxml"));
        stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
        
        
    }

    
}
