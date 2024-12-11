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
