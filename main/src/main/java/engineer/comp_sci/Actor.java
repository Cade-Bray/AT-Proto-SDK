package engineer.comp_sci;
import java.net.http.HttpResponse;

public class Actor {
    // **** GET REQUESTS BELOW ****
    // Most GET requests are queried against the public.api.bsky.app which does not require user authentication.

    /**
     * Get private preferences attached to the current account. Expected use is synchronization between multiple
     * devices, and import/export during account migration. Requires auth.
     * <a href="https://docs.bsky.app/docs/api/app-bsky-actor-get-preferences">API Documentation Link</a>
     *
     * @return Returns an array of preferences for the authenticated user. Check API documentation for what the JSON
     *          will include.
     */
    public static HttpResponse<String> getPreferences() {
        //TODO requires auth token
        return null;
    }

    /**
     * Retrieves the profile information for a single actor.
     * <a href="https://docs.bsky.app/docs/api/app-bsky-actor-get-profile">API Documentation Link</a>
     *
     * @param actor String of an actor. This can be the user handle or DID of the account.
     * @return An HttpResponse object containing the profile information of the specified actor.
     *         The response body is a JSON string.
     */
   public static HttpResponse<String> getProfile(String actor) {
       String uri_string = "https://public.api.bsky.app/xrpc/app.bsky.actor.getProfile?actor=" + actor;
       return HTTP.GET(uri_string);
   }

    /**
     * Retrieves the profile information for multiple actors.
     * <a href="https://docs.bsky.app/docs/api/app-bsky-actor-get-profiles">API Documentation Link</a>
     *
     * @param actors An array of actor handles whose profile information is to be retrieved. This can be user handles or
     *         the user DID.
     * @return An HttpResponse object containing the profile information of the specified actors.
     *         The response body is a JSON string.
     */
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

    // **** END OF GET REQUESTS ****

    // **** POST REQUESTS BELOW ****
    // TODO Need to create OAuth Token request system to query against PDS Entryway.

    /**
     * Get a list of suggested actors. Expected use is discovery of accounts to follow during new account onboarding.
     * <a href="https://docs.bsky.app/docs/api/app-bsky-actor-get-suggestions">API Documentation Link</a>
     *
     * @param limit This is the limit of suggested actors. Default value is 50 but can be Possible values:
     *              >= 1 and <= 100
     * @param cursor TODO Create definition for cursor.
     * @return An HttpResponse object containing the array of suggested actors.
     */
    public static HttpResponse<String> getSuggestions(int limit, String cursor){

        return null;
    }

   // **** END OF POST REQUESTS

   // **** Main Testing ****
    public static void main(String[] args) {
        // Example of a getProfile Usage.
        //System.out.println(getProfile("comp-sci.engineer").body());

        // Example of a getProfiles Usage.
        System.out.println(getProfiles(new String[]{"cade.comp-sci.engineer", "comp-sci.engineer"}).body());
    }
}