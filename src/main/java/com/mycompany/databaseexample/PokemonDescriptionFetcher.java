package com.mycompany.databaseexample;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.JSONObject;

public class PokemonDescriptionFetcher {

    public static String getPokemonDescription(String pokemonName) {
        try {
            String pkmName = pokemonName.toLowerCase();

            if (pkmName.equals("nidoranm")){
                pkmName = "nidoran-m";
            } else if (pkmName.equals("nidoranf")) {
                pkmName = "nidoran-f";
            }


            URL url = new URL("https://pokeapi.co/api/v2/pokemon-species/" + pkmName);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");

            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer content = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }
            in.close();
            con.disconnect();

            // Parse the response to extract the description
            JSONObject jsonObject = new JSONObject(content.toString());
            String description = jsonObject.getJSONArray("flavor_text_entries")
                                           .getJSONObject(0)
                                           .getString("flavor_text")
                                           .replace("\n", " "); // Replacing new lines for better formatting
            return description;

        } catch (Exception e) {
            e.printStackTrace();
            return "Description not available";
        }
    }
}

