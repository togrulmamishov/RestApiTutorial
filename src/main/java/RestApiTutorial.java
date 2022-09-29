import com.google.gson.Gson;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Properties;

import static java.net.http.HttpRequest.*;


public class RestApiTutorial {

    private static final Properties properties = new Properties();


    public static void main(String[] args) throws URISyntaxException, IOException, InterruptedException {

        Transcript transcript = new Transcript();
        transcript.setAudio_url(value("audio-url"));
        Gson gson = new Gson();

        String jsonRequest = gson.toJson(transcript);
        System.out.println(jsonRequest);

        HttpRequest post = newBuilder()
                .uri(new URI("https://api.assemblyai.com/v2/transcript"))
                .header("Authorization", value("authorization.api-key"))
                .POST(BodyPublishers.ofString(jsonRequest))
                .build();

        HttpClient httpClient = HttpClient.newHttpClient();
        var postResponse = httpClient.send(post, HttpResponse.BodyHandlers.ofString());

        transcript = gson.fromJson(postResponse.body(), Transcript.class);

        System.out.println(transcript.getId());

        HttpRequest getRequest = HttpRequest.newBuilder()
                .uri(new URI("https://api.assemblyai.com/v2/transcript/" + transcript.getId()))
                .header("Authorization", value("authorization.api-key"))
                .build();

        while (true) {

            var getResponse = httpClient.send(getRequest, HttpResponse.BodyHandlers.ofString());

            transcript = gson.fromJson(getResponse.body(), Transcript.class);
            String status = transcript.getStatus();

            if(status.equals("completed") || status.equals("error")) {
                break;
            }

        }

        System.out.println("Transcription completed");
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
