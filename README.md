README

### Requirements
1) [mvn 3.8.5+](https://maven.apache.org/docs/3.8.5/release-notes.html)
2) [java 1.9+](https://www.oracle.com/ph/java/technologies/javase/javase9-archive-downloads.html)

### Checklist 
2) open a terminal and run `java -version` and you should see something
along the lines of `java version "..."`
3) open a terminal and run `mvn -v` and you should see your maven version
and your java version.

### Building the application
1) Clone this repository
2) cd into project dir root
3) run `mvn clean install dependency:copy-dependencies`
   1) This will ensure dependencies are installed and copied to `/target`.

### Running the application
1) cd to the project dir root
2) run `mvn compile package`
3) run `java -jar target/java-project-1.0-SNAPSHOT.jar $DIR_TO_WATCH`
   1) replace ``$DIR_TO_WATCH`` with the actual directory to watch.
   2) if there are spaces, surround `$DIR_TO_WATCH` with "" (double quotes).
4) Start adding supported files to the directory (currently only .txt files).
   1) A `/processed` is automatically created within the `$DIR_TO_WATCH` if none exists, to house processed
   files.
   2) A `/unprocessed` is automatically created within the `$DIR_TO_WATCH` if none exists, to house
   unprocessed files.

### Running tests
1) cd into project dir root
2) run `mvn test`

## Application Overview

### Packages & Classes
1) `com.parserapp`
   1) Top level package that contains `Parser.java` which houses the main method
2) `com.parserapp.service`
   1) service package that houses the main service class `Watcher.java`
      1) `Watcher.java` is responsible for watching the directory 
3) `com.parserapp.parsing`
   1) service package that the various interfaces/components that go hand in hand to ensure files 
   are properly handled and parsed
      1) `ParserFactory.java` is a singleton that returns the appropriate file parser
         (which is a class that implements `FileParser`) is returned, depending on the
         file type passed. This is where valid file extensions are mapped to their appropriate 
         parsers/handlers.
      2) `com.parserapp.parsing.service` is a service package that houses the interface `FileParser` and its 
      concrete implementations
         1) `FileParser.java` is the interface which defines the contract of a class that is able
        to parse a file.
         2) `TextParser.java` is the parser for files that have `.txt, .text` extensions.
      3) `com.parserapp.parsing.action` is a service package that contains the interface and concrete
      classes of actions (classes that dictate what to do with a parsed file)
         1) `ParsingAction.java` is the interface that defines what an action is.
         2) `StringOccurrenceParsingAction.java` is the concrete class that implements 
         `ParsingAction.java`, which maps words/dots found.

### How they go hand-in-hand
1) A `Watcher` basically needs a `directory (String)` to watch. 
2) The `ParserFactory` houses all `Parsers` mapped against their valid file types.
3) If `Watcher` sees a file being added, it attempts to parse it using a valid parser,
   1) if no appropriate parser is found, it just moves the file to `/unprocessed`
4) A `FileParser` basically needs `actions (ParsingAction)` to do something with the parsed file.
5) The `FileParser` loops through all available actions (defined at time of creation
in the `ParserFactory`) to do on the parsed file.
6) All outputs are resolved as `Map<String, Integer>` and printed out.

### Motivation and thought process behind design
I wanted something modular and extendable without having to perform changes on classes 
that dont house the main responsibility of the desired change. (ie if you want to change
the how an action works, just change the concrete action class, or if you want to 
add a new file type, just implement the FileParser and include it in the Factory)

### I want to..
1) I want to support new files (PDF, csv, etc).
   1) You should implement the `FileParser` and define how your new file type is parsed
      (implement the method body of `parseWithActions(..)`),
   2) Then, add it in the `ParserFactory.java` by mapping it accordingly against
    its valid file extensions.
2) I want to do new actions (start tracking new things/properties)
   1) You could add to the implementation of `StringOccurrenceParsingAction.java` 
   if you think the action you are adding is related to the class, else you should
   implement `ParsingAction.java` and define your new action and how it resolves to a 
   `Map<String, Integer>`. (Since this is how results are displayed).


### What could be better
Probably should modularize and abstract how results are created/processed. Right now
everything has to resolved to a `Map<String, Integer>` and is only printed in one defined way 
in the `Watcher.java`. But for now this should suffice.