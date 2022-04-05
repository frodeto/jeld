# Jeld

For testing out Java api to Elasticsearch 8

## Prerequisites
- Maven
- Java 17
- Elasticsearch 8

My Mac environment:
```
$ mvn --version
    Apache Maven 3.8.4 (9b656c72d54e5bacbed989b64718c159fe39b537)
    Maven home: /opt/homebrew/Cellar/maven/3.8.4/libexec
    Java version: 17.0.2, vendor: Homebrew, runtime: /opt/homebrew/Cellar/openjdk/17.0.2/libexec/openjdk.jdk/Contents/Home
    Default locale: en_NO, platform encoding: UTF-8
    OS name: "mac os x", version: "12.3", arch: "aarch64", family: "mac"
```

## Build it with Maven
```
> mvn clean install spring-boot:repackage
```

## Run it
```
> java -jar ./target/jeld.jar
```
