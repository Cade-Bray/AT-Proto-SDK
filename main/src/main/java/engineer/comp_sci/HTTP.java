package engineer.comp_sci;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class HTTP {
    /**
     * Sends a GET request to the specified URI. The response body is a JSON string.
     *
     * @param uri_string The URI to send the GET request to.
     * @return The response from the server. Formated as a JSON string.
     */
    public static HttpResponse<String> GET(String uri_string){
        try{
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(uri_string))
                    .GET()
                    .build();

            return client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (Exception e) {
            // TODO: Create robust logging.
            e.printStackTrace();
            return null;
        }
    }
}
