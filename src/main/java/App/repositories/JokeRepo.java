package App.repositories;

import App.dtos.JokeResponseDTO;
import com.google.gson.JsonParser;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

public class JokeRepo {

    
    public JokeResponseDTO getJoke() throws URISyntaxException, IOException {
        OkHttpClient client = new OkHttpClient();

        String urlString = "https://v2.jokeapi.dev/joke/Programming,Miscellaneous,Dark,Pun,Spooky,Christmas?blacklistFlags=nsfw,religious,political,racist,sexist,explicit";
        Request request = new Request.Builder()
                .url(urlString)
                .build();

        JSONObject jsonResponse = null;
        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new Exception("Unexpected code " + response);
            }

            String responseBody = response.body().string();
            System.out.println("Response:");
            System.out.println(responseBody);

            // Parsowanie JSON
            jsonResponse = new JSONObject(responseBody);
            String setup = (String) jsonResponse.get("setup");
            String delivery = (String) jsonResponse.get("delivery");
            return new JokeResponseDTO(setup, delivery);

        } catch (Exception e) {
            return new JokeResponseDTO((String) jsonResponse.get("joke"));
        }
    }
}
