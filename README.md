# NickBook

Java 8-compatible written-book nickname flow for Spigot/Paper 1.8.8+ servers you control.

## Build

```bash
mvn clean package
```

The plugin jar is `target/nickbook-1.0.1.jar`.

The 1.8.8 Spigot Maven API references a vanished `bungeecord-chat:1.8-SNAPSHOT` artifact. This build intentionally uses standard book pages instead of that unavailable chat component dependency, so GitHub Actions can build reliably. The book displays the exact commands to type; command execution remains fully functional.

## Important

Your error means Java 17 was running a Java 21 jar: class file 65 versus 61. This version targets Java 8 and is compiled against the Spigot 1.8.8 API, so it is suitable for a 1.8.8 server running Java 17. It cannot control Hypixel's real nick system, and the skin choices are stored preferences rather than actual client skin replacement.
