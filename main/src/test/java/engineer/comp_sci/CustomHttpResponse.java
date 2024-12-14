package engineer.comp_sci;

import javax.net.ssl.SSLSession;
import java.net.http.HttpClient;
import java.net.http.HttpResponse;
import java.net.http.HttpRequest;
import java.net.http.HttpHeaders;
import java.net.URI;
import java.util.Optional;


/**
 * A custom implementation of the HttpResponse interface. This is used for testing purposes. You
 * can use this class to create a custom HttpResponse object with a custom body and status code. We can add
 * more functionality to this class as needed.
 */
public class CustomHttpResponse implements HttpResponse<String> {
    private final String body;
    private final int statusCode;

    public CustomHttpResponse(String body, int statusCode) {
        this.body = body;
        this.statusCode = statusCode;
    }

    @Override
    public int statusCode() {
        return statusCode;
    }

    @Override
    public HttpRequest request() {
        return null;
    }

    @Override
    public Optional<HttpResponse<String>> previousResponse() {
        return Optional.empty();
    }

    @Override
    public HttpHeaders headers() {
        return null;
    }

    @Override
    public String body() {
        return body;
    }

    @Override
    public Optional<SSLSession> sslSession() {
        return Optional.empty();
    }

    @Override
    public URI uri() {
        return null;
    }

    @Override
    public HttpClient.Version version() {
        return HttpClient.Version.HTTP_1_1;
    }

}
