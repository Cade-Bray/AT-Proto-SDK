package engineer.comp_sci;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class Actor {
   public static HttpResponse<String> getProfile(String actor) {
       try{
           HttpClient client = HttpClient.newHttpClient();
           HttpRequest request = HttpRequest.newBuilder()
                   .uri(new URI("https://public.api.bsky.app/xrpc/app.bsky.actor.getProfile?actor=" + actor))
                   .GET()
                   .build();

           return client.send(request, HttpResponse.BodyHandlers.ofString());
       } catch (Exception e) {
           // TODO: Create robust logging.
           e.printStackTrace();
           return null;
       }
   }

    public static void main(String[] args) {

    }
}