package engineer.comp_sci;

import java.net.http.HttpResponse;

public class Feed {
    static private String app_uri_base = "app.bsky";

    public static String getApp_uri_base() {
        return app_uri_base;
    }

    public static void setApp_uri_base(String app_uri_base) {
        Feed.app_uri_base = app_uri_base;
    }

    public static String uriConstructor(boolean isPrivate, String uri_string){
        if(isPrivate){
            return app_uri_base + uri_string;
        } else {
            return app_uri_base + uri_string;
        }
    }

    public static HttpResponse<String> describeFeedGenerator(){
        //TODO: Implement this method. No matter what I try I get a 404 html response.
        return null;
    }

    public static HttpResponse<String> getActorFeeds(){
        return null;
    }

}
