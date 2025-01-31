![AT-Proto](.github/AT-Proto.png)

# Current Version: 0.1-Alpha
[![Lexicon Updater](https://github.com/Cade-Bray/AT-Proto-SDK/actions/workflows/update_lexicons.yaml/badge.svg)](https://github.com/Cade-Bray/AT-Proto-SDK/actions/workflows/update_lexicons.yaml)

# AT-Protocol Java SDK
This is a Java SDK for the AT-Protocol, a decentralized protocol for social networking. It provides a simple interface for interacting with AT-Protocol servers, allowing you to build decentralized social applications.

### Roadmap
Refer to our open roadmap in the projects section. Items will be scheduled in iterations for official releases. Any item that doesn't get finished during an iteration may be put on the backlog. Anything put on the back log will get a 'help wanted' tag put onto it. Keep an eye out for items that have good first issue assigned to them.


# Quick-start basics
This section will get you up to speed on some basic functionality. For more information consider using the wiki or reaching out to a contributor while our docs are being constructed.

### Creating a session and making a post
The Actor constructor has a built-in system for generating a session but if two factor authentication is enabled for the account it will require an input through the CLI currently. I recommend storing your handle and password in a seperate json file or passing it as enviroment arguments. The specific code below will post to the given account a "Hello World!" message. You may replace args[0] with your handle and args[1] with the password. 
```
public static void main(String[] args) {
        Actor actor = new Actor(args[0], args[1]);
        actor.createRecord("Hello World!");
    }
```

### Refreshing a session.
#### !Under construction! This does not parse and assign the responses yet but will be implemented soon.
Eventually I'll abstract the need for a user of this SDK to refresh the accessJwt themselves by making check mechanisms. For now if you need to refresh your access token you can do so as depicted below. The accessJwt and refreshJwt are stored in the server object which is publically accessable through your actor object as depicted. 
```
public static void main(String[] args) {
        Actor actor = new Actor(args[0], args[1]);
        actor.server.refreshSession();
    }
```
