package engineer.comp_sci;

import java.net.http.HttpResponse;

public class Server {
    static private String app_uri_base = "app.bsky";
    static private String refreshJwt;
    static private String accessJwt;
    static public boolean active = false;

    public Server(String rJwt, String aJwt){
        setRefreshJwt(rJwt);
        setAccessJwt(aJwt);
        active = true;
    }

    /**
     * Creates a session for the user. This is the first step in the login process. The user will need to provide their
     * handle and password. The response will contain an access token and a refresh token. The access token is used to
     * authenticate the user for the current session. The refresh token is used to get a new access token when the current
     * one expires.
     * <p>
     * If the account has 2FA enabled, the user will need to provide the authFactorToken as well.
     * If the 2FA is provided after the first request, the user will need to provide the authFactorToken in the second
     * request.
     * <p>
     * <a href="https://docs.bsky.app/docs/api/com-atproto-server-create-session">API Documentation Link</a>
     *
     * @return HttpResponse<String> object containing the response from the server.
     */
    public HttpResponse<String> createSession(String handle, String password, String authFactorToken) {
        String uri_string = "com.atproto.server.createSession";
        String body = "{\n" +
                "  \"identifier\": \"" + handle + "\",\n" +
                "  \"password\": \"" + password + "\",\n" +
                "  \"authFactorToken\": \"" + authFactorToken + "\"\n" +
                "}";

        return HTTP.POST(true, uri_string, body);
    }

    /**
     * Creates a session for the user. This is the first step in the login process. The user will need to provide their
     * handle and password. The response will contain an access token and a refresh token. The access token is used to
     * authenticate the user for the current session. The refresh token is used to get a new access token when the current
     * one expires.
     * <p>
     * If the account has 2FA enabled, the user will need to provide the authFactorToken as well.
     * If the 2FA is provided after the first request, the user will need to provide the authFactorToken in the second
     * request.
     * <p>
     * <a href="https://docs.bsky.app/docs/api/com-atproto-server-create-session">API Documentation Link</a>
     *
     * @return HttpResponse<String> object containing the response from the server.
     */
    public HttpResponse<String> createSession(String handle, String password) {
        String uri_string = "com.atproto.server.createSession";
        String body = "{\n" +
                "  \"identifier\": \"" + handle + "\",\n" +
                "  \"password\": \"" + password + "\"\n" +
                "}";

        return HTTP.POST(true, uri_string, body);
    }

    public String getApp_uri_base() {
        return app_uri_base;
    }

    public void setApp_uri_base(String app_uri_base) {
        Server.app_uri_base = app_uri_base;
    }

    public void refreshSession(){
        active = false;
        HttpResponse<String> response = HTTP.POST(true, "com.atproto.server.refreshSession", "",
                getRefreshJwt());
        //TODO parse the response.
        active = true;
    }

    private String getRefreshJwt() {
        return Server.refreshJwt;
    }

    public void setRefreshJwt(String refreshJwt) {
        Server.refreshJwt = refreshJwt;
    }

    public String getAccessJwt() {
        return accessJwt;
    }

    public void setAccessJwt(String accessJwt) {
        Server.accessJwt = accessJwt;
    }
}
