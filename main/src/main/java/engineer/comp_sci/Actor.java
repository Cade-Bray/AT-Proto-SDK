package engineer.comp_sci;
import java.net.http.HttpResponse;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Scanner;

public class Actor {
    private HashMap<String, Object> session = new HashMap<>();
    private String app_uri_base = "app.bsky";
    public Server server;

    /**
     * Constructor for the Actor class. This constructor will create a session for the user. The user will need to
     * provide their handle and password. The server response could require 2FA in which case manual input is required.
     *
     * @param handle String The handle of the user.
     * @param password String The password of the user.
     */
    public Actor(String handle, String password) {
        HttpResponse<String> session = createSession(handle, password);

        if (session.statusCode() == 401) {
            // 2FA is required. Prompt user for 2FA token.
            // I'd like to find an alternative to using System.in for user input. I'm not sure what that looks like at
            // this point.
            System.out.println("2FA is required. Please enter your 2FA token: ");
            Scanner scanner = new Scanner(System.in);
            session = createSession(handle, password, scanner.nextLine());
            this.session = Parser.createSession200(session);
            scanner.close();

        } else if (session.statusCode() == 200) {
            // 2FA is not required. Parse the response.
            this.session = Parser.createSession200(session);
        }

        server = new Server((String) this.session.get("refreshJwt"), (String) this.session.get("accessJwt"));
    }

    /**
     * UNDER CONSTRUCTION - DO NOT USE
     * <p>
     * Constructor for the Actor class. This constructor will create a session for the user. The user will need to
     * provide their handle and password. The server response could require 2FA in which case manual input is required.
     *
     * @param handle String The handle of the user.
     * @param accessJwt String The accessJwt of the user.
     * @param refreshJwt String The refreshJwt of the user.
     */
    public Actor(String handle, String accessJwt, String refreshJwt) {
        //TODO: Allow for the creation of an Actor object with only the accessJwt and refreshJwt.
        // This will allow for the Actor object to be created without the need for a password.
        // Need to make a createSession method that will allow for the creation of an Actor object with only the
        // accessJwt and refreshJwt.
        this.session.put("handle", handle);
        this.session.put("accessJwt", accessJwt);
        this.session.put("refreshJwt", refreshJwt);
    }

    /**
     * Getter for the app_uri_base class variable.
     *
     * @return String The did of the user.
     */
    public String getApp_uri_base() {
        return app_uri_base;
    }

    /**
     * Setter for the app_uri_base class variable. You may need to set this if you are using a different app_uri_base if
     * you are using a different app_uri_base than the default such as your own ATProto app.
     *
     * @param app_uri_base String The app_uri_base of the user.
     */
    public void setApp_uri_base(String app_uri_base) {
        this.app_uri_base = app_uri_base;
    }

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
    public HttpResponse<String> getPreferences() {
        //TODO requires auth token. See issue #4
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
   public HttpResponse<String> getProfile(String actor) {
       String uri_string = app_uri_base + ".actor.getProfile?actor=" + actor;
       return HTTP.GET(false, uri_string);
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
   public HttpResponse<String> getProfiles(String[] actors){
       StringBuilder uri_string = new StringBuilder(app_uri_base + ".actor.getProfiles?");

       for (String actor : actors){
           uri_string.append("actors[]=").append(actor);
           if (!actors[actors.length - 1].equals(actor)){
               uri_string.append("&");
           }
       }

       return HTTP.GET(false, String.valueOf(uri_string));
   }

    /**
     * Find actor suggestions for a prefix search term. Expected use is for auto-completion during text field entry.
     * Does not require auth.
     * <a href="https://docs.bsky.app/docs/api/app-bsky-actor-search-actors-typeahead">API Documentation Link</a>
     *
     * @param query is a string of what to search on blue sky.
     * @param limit is the integer of how many returns you want from the search. Must be 1 => and <= 100. Suggested 10.
     * @return An HTTP Response. The information is in the body as a string JSON. Refer to API doc link for specifics.
     */
    public HttpResponse<String> searchActorsTypeahead(String query, int limit){

        if (limit > 100 || limit < 1) {
            throw new IndexOutOfBoundsException("Limit needs to be within range >= 1 and <= 100");
        }

        String uri_sb = app_uri_base + ".actor.searchActorsTypeahead?" + "q=" + query + "&limit=" + limit;

        return HTTP.GET(false, uri_sb);
    }

    /**
     * Find actors (profiles) matching search criteria. Does not require auth.
     * <a href="https://docs.bsky.app/docs/api/app-bsky-actor-search-actors">API Documentation Link</a>
     * TODO this is in-progress please see issue #6
     *
     * @param query is a string of what to search on blue sky.
     * @param limit is the integer of how many returns you want from the search. Must be 1 => and <= 100. Suggested 10.
     * @param cursor TODO define cursor in parameter context
     * @return An HTTP Response. The information is in the body as a string JSON. Refer to API doc link for specifics.
     */
    public HttpResponse<String> searchActors(String query, int limit, String cursor){
        String uri_sb = app_uri_base + ".actor.searchActors?" + "q=" + query + "&limit=" + limit + "&cursor=" + cursor;

        // Checking limit range.
        if (limit > 100 || limit < 1) {
            throw new IndexOutOfBoundsException("Limit needs to be within range >= 1 and <= 100");
        }

        return HTTP.GET(false, uri_sb);
    }

    // **** END OF GET REQUESTS ****

    // **** POST REQUESTS BELOW ****

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
     * Get a list of suggested actors. Expected use is discovery of accounts to follow during new account onboarding.
     * <a href="https://docs.bsky.app/docs/api/app-bsky-actor-get-suggestions">API Documentation Link</a>
     *
     * @param limit This is the limit of suggested actors. Default value is 50 but can be Possible values:
     *              >= 1 and <= 100
     * @param cursor TODO Create definition for cursor.
     * @return An HttpResponse object containing the array of suggested actors.
     */
    public HttpResponse<String> getSuggestions(int limit, String cursor){

        return null;
    }

    /**
     * This will create a post to the given actor instance. The post will be created in the actor's feed.
     *
     * @param text The text of the post.
     *             The text of the post. This is the main content of the post. It can be up to 280 characters long.
     *             The text can contain hashtags, mentions, and links. The text can also contain emojis.
     *
     * @see <a href="https://docs.bsky.app/docs/api/com-atproto-repo-create-record">API Documentation Link</a>
     * @see <a href="https://docs.bsky.app/docs/advanced-guides/posts#post-record-structure">Post Record Structure</a>
     *
     * @return An HttpResponse object containing the response from the server.
     */
    public HttpResponse<String> createRecord(String text){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        String formattedDateTime = ZonedDateTime.now().format(formatter);
        String uri_string = "com.atproto.repo.createRecord";
        String body = "{\n" +
                "\"repo\": \"" + session.get("did") + "\"," +
                "\"collection\": \"" + app_uri_base + ".feed.post\"," +
                "\"record\": {" +
                "\"$type\": \"" + app_uri_base + ".feed.post\"," +
                "\"createdAt\": \"" + formattedDateTime + "\"," +
                "\"text\": \"" + text + "\"," +
                // TODO: Currently only supports English. Allow for enumeration.
                "\"langs\": [\"en\"]}}";

        return HTTP.POST(true ,uri_string, body, server.getAccessJwt());
    }

   // **** END OF POST REQUESTS

   // **** Main Testing ****
    public static void main(String[] args) {
        Actor actor = new Actor(args[0], args[1]);
        actor.server.refreshSession();
    }
}