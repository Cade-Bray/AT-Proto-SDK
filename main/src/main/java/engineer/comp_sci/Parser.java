package engineer.comp_sci;

import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Parser {
    /**
     * Parses the response from the createSession method. This method will set the class variables to the values
     * returned from the server. Only provide a response from the createSession method that has a status code of 200.
     *
     * @param session HttpResponse The response from the createSession method.
     */
    public static HashMap<String, Object> createSession200(HttpResponse<String> session) {
        HashMap<String, Object> parsed = new HashMap<>();
        String session_body = session.body();

        //Parse did
        Pattern regex = Pattern.compile("did:plc:\\w*");
        Matcher matcher = regex.matcher(session_body);
        if (matcher.find()) {
            parsed.put("did", matcher.group(0));
        }

        //Parse didDoc TODO See issue #3

        //Parse handle
        regex = Pattern.compile("\"handle\":\"\\S*?\"");
        matcher = regex.matcher(session_body);
        if (matcher.find()) {
            String phandle = matcher.group(0);
            phandle = phandle.substring(10, phandle.length() - 1);
            parsed.put("handle", phandle);
        }


        //Parse email
        regex = Pattern.compile("\"email\":\"\\S*?\"");
        matcher = regex.matcher(session_body);
        if (matcher.find()) {
            String pemail = matcher.group(0);
            pemail = pemail.substring(9, pemail.length() - 1);
            parsed.put("email", pemail);
        }

        //Parse emailConfirmed
        regex = Pattern.compile("\"emailConfirmed\":\\S*?,");
        matcher = regex.matcher(session_body);
        if (matcher.find()) {
            String emailConfirmed = matcher.group(0);
            emailConfirmed = emailConfirmed.substring(17, emailConfirmed.length() - 1);
            parsed.put("emailConfirmed", Boolean.parseBoolean(emailConfirmed));
        }

        //Parse emailAuthFactor
        regex = Pattern.compile("\"emailAuthFactor\":\\S*?,");
        matcher = regex.matcher(session_body);
        if (matcher.find()) {
            String emailAuthFactor = matcher.group(0);
            emailAuthFactor = emailAuthFactor.substring(18, emailAuthFactor.length() - 1);
            parsed.put("emailAuthFactor", Boolean.parseBoolean(emailAuthFactor));
        }

        //Parse accessJwt
        String accessJwt;
        regex = Pattern.compile("\"accessJwt\":\"\\S*?\"");
        matcher = regex.matcher(session_body);
        if (matcher.find()) {
            accessJwt = matcher.group(0);
            accessJwt = accessJwt.substring(13, accessJwt.length() - 1);
            parsed.put("accessJwt", accessJwt);
        }

        //Parse refreshJwt
        String refreshJwt;
        regex = Pattern.compile("\"refreshJwt\":\"\\S*?\"");
        matcher = regex.matcher(session_body);
        if (matcher.find()) {
            refreshJwt = matcher.group(0);
            refreshJwt = refreshJwt.substring(14, refreshJwt.length() - 1);
            parsed.put("refreshJwt", refreshJwt);
        }


        //Parse active
        regex = Pattern.compile("\"active\":\\S*?\\s}");
        matcher = regex.matcher(session_body);
        if (matcher.find()) {
            String active = matcher.group(0);
            active = active.substring(9, active.length() - 1);
            parsed.put("active", Boolean.parseBoolean(active.trim()));
        }

        return parsed;
    }

    /**
     * Parses the response from the deleteRecord method with a status code of 200.
     *
     * @param session HttpResponse The response from the createSession method.\
     * @return HashMap<String, Object> The parsed response.
     * @see Actor#deleteRecord(String)
     */
    public static HashMap<String, Object> deleteRecord200(HttpResponse<String> session){
        HashMap<String, Object> response = new HashMap<>();
        HashMap<String, Object> commit = new HashMap<>();
        String session_body = session.body();

        //Parse cid
        Pattern regex = Pattern.compile("\"cid\":\"\\S*?\"");
        Matcher matcher = regex.matcher(session_body);
        if (matcher.find()) {
            String cid = matcher.group(0);
            cid = cid.substring(7, cid.length() - 1);
            commit.put("cid", cid);
        }

        //Parse rev
        regex = Pattern.compile("\"rev\":\"\\S*?\"");
        matcher = regex.matcher(session_body);
        if (matcher.find()) {
            String rev = matcher.group(0);
            rev = rev.substring(7, rev.length() - 1);
            commit.put("rev", rev);
        }
        response.put("commit", commit);
        return response;
    }
}
