package engineer.comp_sci;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.http.HttpResponse;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Scanner;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@SuppressWarnings("unused")
public class Actor {
    private static final Logger logger = LogManager.getLogger(Actor.class);
    private ObjectNode session_details = new ObjectMapper().createObjectNode();
    private String app_uri_base = "app.bsky";
    private String[] languages = {"en"};
    public Server server;

    /**
     * Constructor for the Actor class. This constructor will create a session for the user. The user will need to
     * provide their handle and password. The server response could require 2FA in which case manual input is required.
     *
     * @param handle String The handle of the user.
     * @param password String The password of the user.
     */
    public Actor(String handle, String password) {
        server = new Server();
        ObjectMapper mapper = new ObjectMapper();
        HttpResponse<String> session = server.createSession(handle, password);

        if (session.statusCode() == 401) {
            // 2FA is required. Prompt user for 2FA token.
            // I'd like to find an alternative to using System.in for user input. I'm not sure what that looks like at
            // this point.
            logger.info("2FA is required. Acquiring 2FA token from user.");
            System.out.println("2FA is required. Please enter your 2FA token: ");
            Scanner scanner = new Scanner(System.in);
            // TODO there is no error handling for wrong 2FA token.
            session = server.createSession(handle, password, scanner.nextLine());

            // Parse the response.
            try {
                this.session_details = (ObjectNode) mapper.readTree(session.body());
            } catch (JsonProcessingException e) {
                logger.error("2FA required: Error parsing the response from the server.");
            }
            scanner.close();

        } else if (session.statusCode() == 200) {
            // 2FA is not required. Parse the response.
            try {
                this.session_details = (ObjectNode) mapper.readTree(session.body());
            } catch (JsonProcessingException e) {
                logger.error("No 2FA: Error parsing the response from the server.");
            }
        }

        server = new Server(this.session_details.get("refreshJwt").asText(), this.session_details.get("accessJwt").asText());
    }

    /**
     * Getter for the app_uri_base class variable.
     *
     * @return String The app uri base.
     */
    @SuppressWarnings("unused")
    public String getApp_uri_base() {
        return app_uri_base;
    }

    /**
     * Setter for the app_uri_base class variable. You may need to set this if you are using a different app_uri_base if
     * you are using a different app_uri_base than the default such as your own ATProto app.
     *
     * @param app_uri_base String The app_uri_base of the user.
     */
    @SuppressWarnings("unused")
    public void setApp_uri_base(String app_uri_base) {
        this.app_uri_base = app_uri_base;
    }

    /**
     * Getter for the languages.
     *
     * @return ArrayList<String> The languages set for posts.
     */
    @SuppressWarnings("unused")
    public String[] getLanguages() {
        return languages;
    }

    /**
     * Setter for the languages.
     *
     * @param languages ArrayList<String> The languages set for posts.
     */
    @SuppressWarnings("unused")
    public void setLanguages(String[] languages) {
        this.languages = languages;
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
    @SuppressWarnings("unused")
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
    @SuppressWarnings("unused")
    public ObjectNode getProfile(String actor) throws JsonProcessingException {
       String uri_string = app_uri_base + ".actor.getProfile?actor=" + actor;

       HttpResponse<String> response = HTTP.GET(false, uri_string);
       ObjectMapper mapper = new ObjectMapper();
       assert response != null;
       ObjectNode profile = (ObjectNode) mapper.readTree(response.body());
       profile.put("status", response.statusCode());
       return profile;
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
   @SuppressWarnings("unused")
    public ObjectNode getProfiles(String[] actors) throws JsonProcessingException {
       StringBuilder uri_string = new StringBuilder(app_uri_base + ".actor.getProfiles?");

       for (String actor : actors){
           uri_string.append("actors[]=").append(actor);
           if (!actors[actors.length - 1].equals(actor)){
               uri_string.append("&");
           }
       }

       HttpResponse<String> response = HTTP.GET(false, String.valueOf(uri_string));
       ObjectMapper mapper = new ObjectMapper();
       assert response != null;
       ObjectNode profiles = (ObjectNode) mapper.readTree(response.body());
       profiles.put("status", response.statusCode());

       return profiles;
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
    @SuppressWarnings("unused")
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
    @SuppressWarnings("unused")
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
     * Get a list of suggested actors. Expected use is discovery of accounts to follow during new account onboarding.
     * <a href="https://docs.bsky.app/docs/api/app-bsky-actor-get-suggestions">API Documentation Link</a>
     *
     * @param limit This is the limit of suggested actors. Default value is 50 but can be Possible values:
     *              >= 1 and <= 100
     * @param cursor TODO Create definition for cursor.
     * @return An HttpResponse object containing the array of suggested actors.
     */
    @SuppressWarnings("unused")
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
    @SuppressWarnings("unused")
    public ObjectNode createRecord(String text) throws JsonProcessingException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        String formattedDateTime = ZonedDateTime.now().format(formatter);
        String uri_string = "com.atproto.repo.createRecord";

        // Create the JSON object to be sent to the server.
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode record = mapper.createObjectNode();

        // Set the values for the JSON object.
        record.put("repo", session_details.get("did").asText());
        record.put("collection", app_uri_base + ".feed.post");
        record.set("record", mapper.createObjectNode()
                .put("$type", app_uri_base + ".feed.post")
                .put("createdAt", formattedDateTime)
                .put("text", text)
        );

        // Add the languages to the JSON object.
        ArrayNode langs = record.putArray("langs");
        Arrays.stream(languages).forEach(langs::add);

        // Send the POST request to the server.
        HttpResponse<String> response = HTTP.POST(true ,uri_string, mapper.writeValueAsString(record),
                server.getAccessJwt());

        // Parse the response from the server.
        assert response != null;
        ObjectNode post = (ObjectNode) mapper.readTree(response.body());
        post.put("status", response.statusCode());

        return post;
    }

    /**
     * This will create a post to the given actor instance. The post will be created in the actor's feed.
     *
     * @param text The text of the post. The text of the post. This is the main content of the post. It can be up to
     *              280 characters long.
     * @param imageFP The file path of the image to be uploaded.
     * @param alt The alt text of the image.
     *
     * @see <a href="https://docs.bsky.app/docs/api/com-atproto-repo-apply-writes">API Documentation Link</a>
     * @see <a href="https://github.com/Cade-Bray/AT-Proto-SDK/wiki/Payload:-com.atproto.repo.applyWrites">Payload Structure</a>
     *
     * @return An ObjectNode containing the response from the server.
     */
    @SuppressWarnings("unused")
    public ObjectNode applyWrites(String text, String imageFP, String alt) throws IOException {
        // Create the formatted date time string.
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        String formattedDateTime = ZonedDateTime.now().format(formatter);

        String uri_string = "com.atproto.repo.applyWrites";

        // Upload the image to the server.
        ObjectNode blob = uploadBlob(imageFP);
        File image = new File(imageFP);
        BufferedImage img = ImageIO.read(image);
        int width = img.getWidth();
        int height = img.getHeight();

        // Create the JSON object to be sent to the server.
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode record = mapper.createObjectNode();

        // Set the values for the JSON object.
        record.put("repo", session_details.get("did").asText());
        record.put("validate", true);

        //Create writes object
        ObjectNode writes = mapper.createObjectNode();

        //Create write object
        ObjectNode write = mapper.createObjectNode();
        write.put("$type", uri_string + "#create");
        write.put("collection", app_uri_base + ".feed.post");
        //TODO placeholder for rkey
        write.put("rkey", "TODO placeholder");

        //Create value object
        ObjectNode value = mapper.createObjectNode();
        value.put("$type", app_uri_base + ".feed.post");
        value.put("createdAt", formattedDateTime);
        value.put("text", text);

        //Create embed object
        ObjectNode embed = mapper.createObjectNode();
        embed.put("$type", "com.atproto.embed.images");

        // Create images object
        ObjectNode images = mapper.createObjectNode();
        ObjectNode imageNode = mapper.createObjectNode();
        imageNode.put("alt", alt);

        // Create aspectRatio object
        ObjectNode aspectRatio = mapper.createObjectNode();
        aspectRatio.put("width", width);
        aspectRatio.put("height", height);
        imageNode.set("aspectRatio", aspectRatio);

        // Create imageDetails object
        ObjectNode imageDetails = mapper.createObjectNode();
        imageDetails.put("$type", blob.get("blob").get("$type").asText());
        imageDetails.put("mimeType", blob.get("blob").get("mimeType").asText());
        imageDetails.put("size", blob.get("blob").get("size").asInt());

        // Create ref object
        ObjectNode ref = mapper.createObjectNode();
        ref.put("$link", blob.get("blob").get("ref").get("$link").asText());

        // Start nesting for better visualization of this JSON object see the java doc link above for payload structure.
        imageDetails.set("ref", ref);
        imageNode.set("image", imageDetails);
        images.set("0", imageNode);
        embed.set("images", images);
        value.set("embed", embed);

        // Add the languages to the JSON object.
        ArrayNode langs = value.putArray("langs");
        Arrays.stream(languages).forEach(langs::add);

        write.set("value", value);
        writes.set("0", write);
        record.set("writes", writes);

        // Send the POST request to the server.
        HttpResponse<String> response = HTTP.POST(true ,uri_string, mapper.writeValueAsString(record),
                server.getAccessJwt());

        // Parse the response from the server.
        assert response != null;
        ObjectNode post = (ObjectNode) mapper.readTree(response.body());
        post.put("status", response.statusCode());

        return post;
    }

    /**
     * This will delete a post from the user's feed. It just requires the rkey of the post to be deleted.
     *
     * @see <a href="https://docs.bsky.app/docs/api/com-atproto-repo-delete-record">API Documentation Link</a>
     * @param rkey The rkey of the post to be deleted.
     * @return An HttpResponse object containing the response from the server.
     */
    @SuppressWarnings("unused")
    public HttpResponse<String> deleteRecord(String rkey) throws JsonProcessingException {
        String uri_string = "com.atproto.repo.deleteRecord";

        ObjectMapper mapper = new ObjectMapper();
        ObjectNode record = mapper.createObjectNode();

        record.put("collection", app_uri_base + ".feed.post");
        record.put("repo", session_details.get("did").asText());
        record.put("rkey", rkey);

        return HTTP.POST(true ,uri_string, mapper.writeValueAsString(record), server.getAccessJwt());
    }

    /**
     * Upload blob to the server. This is used to upload images to the server. This is not posting the image to the
     * feed. This is just uploading the image to the server. The link it produces is what is used to post the image to
     * the feed.
     * @param imageFP The file path of the image to be uploaded.
     * @return An ObjectNode containing the response from the server. This contains the link needed.
     *
     * @throws JsonProcessingException If there is an error parsing the JSON response from the server. Please report this
     * error to the developers on the GitHub repository issues page.
     */
    public ObjectNode uploadBlob(String imageFP) throws JsonProcessingException {
        String uri_string = "com.atproto.repo.uploadBlob";
        byte[] imageBytes = null;

        // Read the image file into a byte array.
        logger.info("Uploading image: {}", imageFP);
        try {
            File image = new File(imageFP);
            FileInputStream imageStream = new FileInputStream(image);
            imageBytes = new byte[(int) image.length()];

            if (imageStream.read(imageBytes) > 1000000) {
                throw new IndexOutOfBoundsException("File size is too large. Must be less than 1MB.");
            }

            imageStream.close();
        } catch (Exception e) {
            logger.error("Error uploading image: {}", e.getMessage());
        }

        // Send the POST request to the server.
        HttpResponse<String> response = HTTP.POST(true ,uri_string, imageBytes, server.getAccessJwt());
        assert response != null;

        // Parse the response from the server.
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode blob = (ObjectNode) mapper.readTree(response.body());
        blob.put("status", response.statusCode());

        return blob;
    }

   // **** END OF POST REQUESTS

   // **** Main Testing ****
    public static void main(String[] args) {
        Actor actor = new Actor(args[0], args[1]);
    }
}