package engineer.comp_sci;
import java.net.http.HttpResponse;

public class Actor {
   public static HttpResponse<String> getProfile(String actor) {
       String uri_string = "https://public.api.bsky.app/xrpc/app.bsky.actor.getProfile?actor=" + actor;
       return HTTP.GET(uri_string);
   }

   public static HttpResponse<String> getProfiles(String[] actors){
       StringBuilder uri_string = new StringBuilder("https://public.api.bsky.app/xrpc/app.bsky.actor.getProfiles?");

       for (String actor : actors){
           uri_string.append("actors[]=").append(actor);
           if (!actors[actors.length - 1].equals(actor)){
               uri_string.append("&");
           }
       }

       return HTTP.GET(String.valueOf(uri_string));
   }

   public static void getSuggestions(){

   }



    public static void main(String[] args) {
        // Example of a getProfile Usage.
        //System.out.println(getProfile("comp-sci.engineer").body());

        // Example of a getProfiles Usage.
        System.out.println(getProfiles(new String[]{"cade.comp-sci.engineer", "comp-sci.engineer"}).body());
    }
}