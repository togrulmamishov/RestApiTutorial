import com.google.gson.Gson;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Arrays;
import java.util.Properties;

import static java.net.http.HttpRequest.*;


public class RestApiTutorial {

    public static void main(String[] args) throws URISyntaxException, IOException, InterruptedException {

        Transcript transcript = new Transcript();
        transcript.setAudio_url(value("audio-url"));
        transcript.setLanguage_detection(Boolean.valueOf(value("language-detection")));
        Gson gson = new Gson();

        String jsonRequest = gson.toJson(transcript);

        HttpRequest post = newBuilder()
                .uri(new URI("https://api.assemblyai.com/v2/transcript"))
                .header("Authorization", value("authorization.api-key"))
                .POST(BodyPublishers.ofString(jsonRequest))
                .build();

        HttpClient httpClient = HttpClient.newHttpClient();
        var postResponse = httpClient.send(post, HttpResponse.BodyHandlers.ofString());

        transcript = gson.fromJson(postResponse.body(), Transcript.class);


        HttpRequest getRequest = HttpRequest.newBuilder()
                .uri(new URI("https://api.assemblyai.com/v2/transcript/" + transcript.getId()))
                .header("Authorization", value("authorization.api-key"))
                .build();

        while (true) {

            var getResponse = httpClient.send(getRequest, HttpResponse.BodyHandlers.ofString());

            transcript = gson.fromJson(getResponse.body(), Transcript.class);
            String status = transcript.getStatus();

            if("completed".equals(status) || "error".equals(status)) {
                break;
            }

        }

        System.out.println("Transcription completed. Result: \n");
        System.out.println(transcript.getText());
    }

    private static String value(String key) {
        final Properties properties = new Properties();
        try {
            properties.load(
                    new BufferedInputStream(
                            new FileInputStream("src/main/resources/application.properties"))
            );

            return properties.getProperty(key);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
