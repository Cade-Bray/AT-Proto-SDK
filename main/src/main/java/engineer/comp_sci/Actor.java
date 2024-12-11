package engineer.comp_sci;
import java.net.http.HttpResponse;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Actor {
    static private String did;
    static private String id;
    static private String handle;
    static private String email;
    static private boolean emailConfirmed;
    static private boolean emailAuthFactor;
    static private String accessJwt;
    static private String refreshJwt;
    static private boolean active;
    static private String app_uri_base = "app.bsky";

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
            parseCreateSession200(session);

        } else if (session.statusCode() == 200) {
            // 2FA is not required. Parse the response.
            parseCreateSession200(session);

        }

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
        Actor.handle = handle;
        Actor.accessJwt = accessJwt;
        Actor.refreshJwt = refreshJwt;
    }

    /**
     * Parses the response from the createSession method. This method will set the class variables to the values
     * returned from the server. Only provide a response from the createSession method that has a status code of 200.
     *
     * @param session HttpResponse The response from the createSession method.
     */
    private static void parseCreateSession200(HttpResponse<String> session) {
        String session_body = session.body();

        //Parse did
        Pattern regex = Pattern.compile("did:plc:\\w*");
        Matcher matcher = regex.matcher(session_body);
        if (matcher.find()) {
            did = matcher.group(0);
        }

        //Parse id
        if (matcher.find()) {
            id = matcher.group(0);
        }

        //Parse didDoc TODO

        //Parse alsoKnownAs TODO

        //Parse verificationMethod TODO

        //Parse service TODO

        //Parse handle
        regex = Pattern.compile("\"handle\":\"\\S*?\"");
        matcher = regex.matcher(session_body);
        if (matcher.find()) {
            String phandle = matcher.group(0);
            phandle = phandle.substring(10, phandle.length() - 1);
            Actor.handle = phandle;
        }


        //Parse email
        regex = Pattern.compile("\"email\":\"\\S*?\"");
        matcher = regex.matcher(session_body);
        if (matcher.find()) {
            String pemail = matcher.group(0);
            pemail = pemail.substring(9, pemail.length() - 1);
            Actor.email = pemail;
        }

        //Parse emailConfirmed
        regex = Pattern.compile("\"emailConfirmed\":\\S*?,");
        matcher = regex.matcher(session_body);
        if (matcher.find()) {
            String emailConfirmed = matcher.group(0);
            emailConfirmed = emailConfirmed.substring(17, emailConfirmed.length() - 1);
            Actor.emailConfirmed = Boolean.parseBoolean(emailConfirmed);
        }

        //Parse emailAuthFactor
        regex = Pattern.compile("\"emailAuthFactor\":\\S*?,");
        matcher = regex.matcher(session_body);
        if (matcher.find()) {
            String emailAuthFactor = matcher.group(0);
            emailAuthFactor = emailAuthFactor.substring(18, emailAuthFactor.length() - 1);
            Actor.emailAuthFactor = Boolean.parseBoolean(emailAuthFactor);
        }

        //Parse accessJwt
        regex = Pattern.compile("\"accessJwt\":\"\\S*?\"");
        matcher = regex.matcher(session_body);
        if (matcher.find()) {
            String accessJwt = matcher.group(0);
            accessJwt = accessJwt.substring(13, accessJwt.length() - 1);
            Actor.accessJwt = accessJwt;
        }

        //Parse refreshJwt
        regex = Pattern.compile("\"refreshJwt\":\"\\S*?\"");
        matcher = regex.matcher(session_body);
        if (matcher.find()) {
            String refreshJwt = matcher.group(0);
            refreshJwt = refreshJwt.substring(14, refreshJwt.length() - 1);
            Actor.refreshJwt = refreshJwt;
        }

        //Parse active
        regex = Pattern.compile("\"active\":\\S*?}");
        matcher = regex.matcher(session_body);
        if (matcher.find()) {
            String active = matcher.group(0);
            active = active.substring(9, active.length() - 1);
            Actor.active = Boolean.parseBoolean(active);
        }
    }

    /**
     * Getter for the app_uri_base class variable.
     *
     * @return String The did of the user.
     */
    public static String getApp_uri_base() {
        return app_uri_base;
    }

    /**
     * Setter for the app_uri_base class variable. You may need to set this if you are using a different app_uri_base if
     * you are using a different app_uri_base than the default such as your own ATProto app.
     *
     * @param app_uri_base String The app_uri_base of the user.
     */
    public static void setApp_uri_base(String app_uri_base) {
        Actor.app_uri_base = app_uri_base;
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
   public static HttpResponse<String> getProfiles(String[] actors){
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
    public static HttpResponse<String> searchActorsTypeahead(String query, int limit){

        if (limit > 100 || limit < 1) {
            throw new IndexOutOfBoundsException("Limit needs to be within range >= 1 and <= 100");
        }

        String uri_sb = app_uri_base + ".actor.searchActorsTypeahead?" + "q=" + query + "&limit=" + limit;

        return HTTP.GET(false, uri_sb);
    }

    /**
     * Find actors (profiles) matching search criteria. Does not require auth.
     * <a href="https://docs.bsky.app/docs/api/app-bsky-actor-search-actors">API Documentation Link</a>
     *
     * @param query is a string of what to search on blue sky.
     * @param limit is the integer of how many returns you want from the search. Must be 1 => and <= 100. Suggested 10.
     * @param cursor TODO define cursor in parameter context
     * @return An HTTP Response. The information is in the body as a string JSON. Refer to API doc link for specifics.
     */
    public static HttpResponse<String> searchActors(String query, int limit, String cursor){
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
    public static HttpResponse<String> createSession(String handle, String password) {
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
    public static HttpResponse<String> createSession(String handle, String password, String authFactorToken) {
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
    public static HttpResponse<String> getSuggestions(int limit, String cursor){

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
    public static HttpResponse<String> createRecord(String text){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        String formattedDateTime = ZonedDateTime.now().format(formatter);
        String uri_string = "com.atproto.repo.createRecord";
        String body = "{\n" +
                "\"repo\": \"" + did + "\"," +
                "\"collection\": \"" + app_uri_base + ".feed.post\"," +
                "\"record\": {" +
                "\"$type\": \"" + app_uri_base + ".feed.post\"," +
                "\"createdAt\": \"" + formattedDateTime + "\"," +
                "\"text\": \"" + text + "\"," +
                // TODO: Currently only supports English. Allow for enumeration.
                "\"langs\": [\"en\"]}}";

        return HTTP.POST(true ,uri_string, body, accessJwt);
    }

   // **** END OF POST REQUESTS

   // **** Main Testing ****
    public static void main(String[] args) {
        Actor actor = new Actor(args[0], args[1]);

    }
}