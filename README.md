![AT-Proto](.github/AT-Proto.png)

# AT-Protocol Java SDK
This is a Java SDK for the AT-Protocol, a decentralized protocol for social networking. It provides a simple interface for interacting with AT-Protocol servers, allowing you to build decentralized social applications.

## Roadmap
Currently our roadmap is being defined. That being said the first stage is to build out the API GET/POST requests and the methods will currently return a HTTPResponse. In later versions I intend to parse the responses so you will not need to to yourself. Before moving to this stage we need to build out all responses and junit test them. This is a beginning product that is still very experimental.  


# Quick-start basics

## Creating a session and making a post
The Actor constructor has a built-in system for generating a session but if two factor authentication is enabled for the account it will require an input through the CLI currently. I recommend storing your handle and password in a seperate json file or passing it as enviroment arguments. The specific code below will post to the given account a "Hello World!" message. You may replace args[0] with your handle and args[1] with the password. 
```
public static void main(String[] args) {
        Actor actor = new Actor(args[0], args[1]);
        actor.createRecord("Hello World!");
    }
```

## Refreshing a session.
### !Under construction! This does not parse and assign the responses yet but will be implemented soon.
Eventually I'll abstract the need for a user of this SDK to refresh the accessJwt themselves by making check mechanisms. For now if you need to refresh your access token you can do so as depicted below. The accessJwt and refreshJwt are stored in the server object which is publically accessable through your actor object as depicted. 
```
public static void main(String[] args) {
        Actor actor = new Actor(args[0], args[1]);
        actor.server.refreshSession();
    }
```
