package engineer.comp_sci;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.net.http.HttpResponse;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

class createSession200Test {
    public static HttpResponse<String> session_body = new CustomHttpResponse(
            // This is a JSON string that represents a session object. This is currently (12/13/2024) the exact response
            // from the server but with fake data. This is a 200 response.
            """
                    {
                      "did":"did:plc:1234567890",
                      "didDoc":{
                        "@context":[
                          "https://www.w3.org/ns/did/v1",
                          "https://w3id.org/security/multikey/v1",
                          "https://w3id.org/security/suites/secp256k1-2019/v1"
                        ],
                        "id":"did:plc:1234567890",
                        "alsoKnownAs":[
                          "at://test.comp-sci.engineer"
                        ],
                        "verificationMethod":[
                          {
                            "id":"did:plc:1234567890#atproto",
                            "type":"Multikey",
                            "controller":"did:plc:1234567890",
                            "publicKeyMultibase":"1234567890"
                          }
                        ],
                        "service":[
                          {
                            "id":"#atproto_pds",
                            "type":"AtprotoPersonalDataServer",
                            "serviceEndpoint":"https://bracket.us-west.host.bsky.network"
                          }
                        ]
                      },
                      "handle":"test.comp-sci.engineer",
                      "email":"test@test.com",
                      "emailConfirmed":true,
                      "emailAuthFactor":true,
                      "accessJwt":"access1234567890Jwt",
                      "refreshJwt":"refresh1234567890Jwt",
                      "active":true
                    }""",
            200
    );

    @Test
    @DisplayName("createSession200: Testing did assignment")
    void createSession200Did() {
        // Act
        var result = Parser.createSession200(session_body);

        // Assert
        assertEquals("did:plc:1234567890", result.get("did"));
    }

    @Test
    @DisplayName("createSession200: Testing email assignment")
    void createSession200Email() {
        // Act
        var result = Parser.createSession200(session_body);

        // Assert
        assertEquals("test@test.com", result.get("email"));
    }

    @Test
    @DisplayName("createSession200: Testing handle assignment")
    void createSession200Handle() {
        // Act
        var result = Parser.createSession200(session_body);

        // Assert
        assertEquals("test.comp-sci.engineer", result.get("handle"));
    }

    @Test
    @DisplayName("createSession200: Testing emailConfirmed assignment")
    void createSession200emailConfirmed() {
        // Act
        var result = Parser.createSession200(session_body);

        // Assert
        assertEquals(true, result.get("emailConfirmed"));
    }

    @Test
    @DisplayName("createSession200: Testing emailAuthFactor assignment")
    void createSession200EmailAuthFactor() {
        // Act
        var result = Parser.createSession200(session_body);

        // Assert
        assertEquals(true, result.get("emailAuthFactor"));
    }

    @Test
    @DisplayName("createSession200: Testing accessJwt assignment")
    void createSession200AccessJwt() {
        // Act
        var result = Parser.createSession200(session_body);

        // Assert
        assertEquals("access1234567890Jwt", result.get("accessJwt"));
    }

    @Test
    @DisplayName("createSession200: Testing refreshJwt assignment")
    void createSession200RefreshJwt() {
        // Act
        var result = Parser.createSession200(session_body);

        // Assert
        assertEquals("refresh1234567890Jwt", result.get("refreshJwt"));
    }

    @Test
    @DisplayName("createSession200: Testing active assignment")
    void createSession200Active() {
        // Act
        var result = Parser.createSession200(session_body);

        // Assert
        assertEquals(true, result.get("active"));
    }

    @Test
    @DisplayName("createSession200: Testing didDoc assignment")
    void createSession200DidDoc() {
        // Act
        var result = Parser.createSession200(session_body);
        HashMap<String, Object> example_didDoc = new HashMap<>();
        example_didDoc.put("@context", new String[]{
                "https://www.w3.org/ns/did/v1",
                "https://w3id.org/security/multikey/v1",
                "https://w3id.org/security/suites/secp256k1-2019/v1"}
        );
        example_didDoc.put("id", "did:plc:1234567890");
        example_didDoc.put("alsoKnownAs", new String[]{"at://test.comp-sci.engineer"});
        example_didDoc.put("verificationMethod", new HashMap[]{
                new HashMap<String, Object>(){{
                    put("id", "did:plc:1234567890#atproto");
                    put("type", "Multikey");
                    put("controller", "did:plc:1234567890");
                    put("publicKeyMultibase", "1234567890");
                }}
        });
        example_didDoc.put("service", new HashMap[]{
                new HashMap<String, Object>(){{
                    put("id", "#atproto_pds");
                    put("type", "AtprotoPersonalDataServer");
                    put("serviceEndpoint", "https://bracket.us-west.host.bsky.network");
                }}
        });

        // Assert
        assertEquals(example_didDoc, result.get("didDoc"));
    }
}