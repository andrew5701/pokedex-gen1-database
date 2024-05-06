package com.mycompany.databaseexample;

import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import org.json.JSONObject;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpClient.Version;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

public class ChatGPTController {
    @FXML
    private TextArea chatArea;
    @FXML
    private TextField inputField;
    @FXML
    private Button sendButton;

    private final HttpClient httpClient = HttpClient.newBuilder()
            .version(Version.HTTP_2)
            .build();

    // OpenAI API Key
    private final String apiKey = ""; // Replace with your API key

    // Initialize method if needed
    public void initialize() {
        // Initialization code
    }

    // Handle send button action
    @FXML
    private void handleSendAction() {
        String inputText = inputField.getText().trim();
        if (!inputText.isEmpty()) {
            // Clear the input field
            inputField.clear();

            // Append the user's message to the chat area
            chatArea.appendText("\nYou: " + inputText + "\n");

            // Call the ChatGPT API with the user's message
            callChatGPT(inputText);
        }
    }





    private void callChatGPT(String message) {
        Task<String> task = new Task<>() {
            @Override
            protected String call() throws Exception {

                String pokemonPrompt = "As a Pokémon expert, " + message;


                String requestBody = "{\"prompt\": \"" + pokemonPrompt + "\", \"temperature\": 0.7, \"max_tokens\": 150}";

                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create("https://api.openai.com/v1/engines/text-davinci-003/completions"))
                        .header("Content-Type", "application/json")
                        .header("Authorization", "Bearer " + apiKey)
                        .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                        .timeout(Duration.ofSeconds(30))
                        .build();

                // Send the request
                HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

                // Parse the JSON response
                JSONObject jsonResponse = new JSONObject(response.body());
                String chatResponse = jsonResponse.getJSONArray("choices").getJSONObject(0).getString("text");

                return chatResponse;
            }
        };

        task.setOnSucceeded(event -> chatArea.appendText("\nPokemon Assistant: " + task.getValue() + "\n"));
        task.setOnFailed(event -> chatArea.appendText("Pokemon Assistant: Failed to get response\n"));

        // Run the task in a new thread
        new Thread(task).start();
    }



}
