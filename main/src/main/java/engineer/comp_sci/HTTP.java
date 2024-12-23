package engineer.comp_sci;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class HTTP {
    static private String public_uri = "https://public.api.bsky.app/xrpc/";
    static private String private_uri = "https://bsky.social/xrpc/";

    public static String getPublic_uri() {
        return public_uri;
    }

    public static void setPublicUri(String public_uri) {
        HTTP.public_uri = public_uri;
    }

    public static String getPrivateUri() {
        return private_uri;
    }

    public static void setPrivate_uri(String private_uri) {
        HTTP.private_uri = private_uri;
    }

    /**
     * Sends a GET request to the specified URI. The response body is a JSON string.
     *
     * @param uri_string The URI to send the GET request to.
     * @return The response from the server. Formated as a JSON string.
     */
    public static HttpResponse<String> GET(boolean isPrivate, String uri_string){
        try{
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(uriConstructor(isPrivate, uri_string)))
                    .GET()
                    .build();

            //TODO add a check for a valid response and throw if a bad response is received.

            return client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (Exception e) {
            // TODO: Create robust logging.
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Sends an authenticated GET request to the specified URI. The response body is a JSON string.
     *
     * @param uri_string The URI to send the GET request to.
     * @return The response from the server. Formated as a JSON string.
     */
    public static HttpResponse<String> GET(boolean isPrivate, String uri_string, String jwt){
        try{
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(uriConstructor(isPrivate, uri_string)))
                    .header("Authorization", "Bearer " + jwt)
                    .GET()
                    .build();

            //TODO add a check for a valid response and throw if a bad response is received.

            return client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (Exception e) {
            // TODO: Create robust logging.
            e.printStackTrace();
            return null;
        }
    }

    public static HttpResponse<String> POST(boolean isPrivate, String uri_string, String body) {
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(uriConstructor(isPrivate, uri_string)))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(body))
                    .build();

            //TODO add a check for a valid response and throw if a bad response is received.

            return client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (Exception e) {
            // TODO: Create robust logging.
            e.printStackTrace();
            return null;
        }
    }

    public static HttpResponse<String> POST(boolean isPrivate, String uri_string, String body, String jwt) {
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request;

            if (body.isEmpty()){
                request = HttpRequest.newBuilder()
                        .uri(new URI(uriConstructor(isPrivate, uri_string)))
                        .header("Authorization", "Bearer " + jwt)
                        .POST(HttpRequest.BodyPublishers.noBody())
                        .build();
            } else {
                request = HttpRequest.newBuilder()
                        .uri(new URI(uriConstructor(isPrivate, uri_string)))
                        .header("Content-Type", "application/json")
                        .header("Authorization", "Bearer " + jwt)
                        .POST(HttpRequest.BodyPublishers.ofString(body))
                        .build();
            }

            //TODO add a check for a valid response and throw if a bad response is received.

            return client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (Exception e) {
            // TODO: Create robust logging.
            e.printStackTrace();
            return null;
        }
    }

    public static HttpResponse<String> POST(boolean isPrivate, String uri_string, byte[] blob, String jwt) {
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request;

            request = HttpRequest.newBuilder()
                    .uri(new URI(uriConstructor(isPrivate, uri_string)))
                    .header("Content-Type", "image/jpeg")
                    .header("Authorization", "Bearer " + jwt)
                    .POST(HttpRequest.BodyPublishers.ofByteArray(blob))
                    .build();

            //TODO add a check for a valid response and throw if a bad response is received.

            return client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (Exception e) {
            // TODO: Create robust logging.
            e.printStackTrace();
            return null;
        }
    }

    public static String uriConstructor(boolean isPrivate, String endpoint) {
        if (isPrivate) {
            return private_uri + endpoint;
        } else {
            return public_uri + endpoint;
        }
    }
}
