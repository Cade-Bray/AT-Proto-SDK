package engineer.comp_sci;

import org.junit.jupiter.api.Test;

import java.net.http.HttpResponse;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;

class uploadBlob200Test {
    public static HttpResponse<String> session_body = new CustomHttpResponse(
            // This is a JSON string that represents a session object. This is currently (12/13/2024) the exact response
            // from the server but with fake data. This is a 200 response.
            """
                    {
                      "blob":{
                        "$type":"blob",
                        "ref":{
                          "$link":"bafydzyxeqa4gx7dhw5ms4iadgvdyfcejrd7mmu"
                        },
                        "mimeType":"image/jpeg",
                        "size":183149
                      }
                    }""",
            200
    );

    @Test
    void uploadBlob200$link() {
        // Act
        var result = Parser.uploadBlob200(session_body);
        HashMap<String, Object> blob = (HashMap<String, Object>) result.get("blob");
        HashMap<String, Object> ref = (HashMap<String, Object>) blob.get("ref");

        // Assert
        assertEquals("bafydzyxeqa4gx7dhw5ms4iadgvdyfcejrd7mmu", ref.get("$link"));
    }

    @Test
    void uploadBlob200$type() {
        // Act
        var result = Parser.uploadBlob200(session_body);
        HashMap<String, Object> blob = (HashMap<String, Object>) result.get("blob");

        // Assert
        assertEquals("blob", blob.get("$type"));
    }

    @Test
    void uploadBlob200$mimeType() {
        // Act
        var result = Parser.uploadBlob200(session_body);

        // Assert
        assertEquals("image/jpeg", result.get("mimeType"));
    }

    @Test
    void uploadBlob200$size() {
        // Act
        var result = Parser.uploadBlob200(session_body);

        // Assert
        assertEquals(183149, result.get("size"));
    }
}