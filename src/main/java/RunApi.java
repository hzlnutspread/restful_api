import com.google.gson.Gson;
import java.net.URI;
import java.net.http.HttpRequest.*;
import java.net.http.HttpResponse.*;
import java.net.http.*;


public class RunApi {
    public static void main(String[] args) throws Exception {



        // creating instance of Transcript class
        Transcript transcript = new Transcript();
        // set the audio_url portion to be equal to the URL of audio file
        transcript.setAudio_url("https://github.com/johnmarty3/JavaAPITutorial/blob/main/Thirsty.mp4?raw=true");
        // create new instance of Gson
        Gson gson = new Gson();
        // create our jsonRequest variable which turns the URL from transcript into a json
        String jsonRequest = gson.toJson(transcript);
        System.out.println(jsonRequest);



        // create an HttpRequest (a POST request) giving it the end point of the API, a header and body (jsonRequest from above)
        HttpRequest postRequest = HttpRequest.newBuilder()
                .uri(new URI("https://api.assemblyai.com/v2/transcript"))
                .header("Authorization", "d797ff219b0c4c1eb56665b35426bab0")
                // jsonRequest contains the body of what we are sending
                .POST(BodyPublishers.ofString(jsonRequest))
                .build();
        // HttpClient used to send HttpRequests and retrieve HttpResponses
        HttpClient httpClient = HttpClient.newHttpClient();
        // create the HttpResponse (postResponse) which will contain what we receive back from the website. Pass in our HttpRequest
        HttpResponse<String> postResponse = httpClient.send(postRequest, BodyHandlers.ofString());
        // retrieve the body of the response
        postResponse.body();
        System.out.println(postResponse.body());




        // take the json and create a Transcript class object and getId of the response
        transcript = gson.fromJson(postResponse.body(), Transcript.class);
        System.out.println(transcript.getId());
        // create another HttpRequest (a GET request this time) but now have to add in the ID to the end of the URL with the same header
        HttpRequest getRequest = HttpRequest.newBuilder()
                .uri(new URI("https://api.assemblyai.com/v2/transcript/" + transcript.getId()))
                .header("Authorization", "d797ff219b0c4c1eb56665b35426bab0")
                .build();



        // continue calling the API until the status of the response is completed at which point we can print out the text that has been transcribed
        while (true) {
            HttpResponse<String> getResponse = httpClient.send(getRequest, HttpResponse.BodyHandlers.ofString());
            transcript = gson.fromJson(getResponse.body(), Transcript.class);

            System.out.println(transcript.getStatus());

            if ("completed".equals(transcript.getStatus()) || "error".equals(transcript.getStatus())) {
                break;
            }
            Thread.sleep(1000);
        }
        System.out.println("Transcription completed!");
        System.out.println(transcript.getText());
    }

}
