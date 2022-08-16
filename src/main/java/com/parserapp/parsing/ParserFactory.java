package com.parserapp.parsing;

import com.parserapp.parsing.action.StringOccurrenceParsingAction;
import com.parserapp.parsing.service.FileParser;
import com.parserapp.parsing.service.TextParser;
import org.apache.commons.lang3.NotImplementedException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ParserFactory {

    private Map<List<String>, FileParser> parsers = new HashMap<>();
    private static ParserFactory factory;

    private ParserFactory() {}

    public static ParserFactory getInstance() {
        if (factory == null) {
            factory = new ParserFactory();

            Map<List<String>, FileParser> factoryRef = factory.parsers;
            factoryRef.put(
                List.of("txt", "text"),
                new TextParser(
                    List.of(new StringOccurrenceParsingAction())
                )
            );
        }

        return factory;
    }

    public FileParser getAppropriateParser(String ext) {
        return this.parsers
            .entrySet()
            .stream()
            .filter((entry) -> entry.getKey().contains(ext))
            .findFirst()
            .orElseThrow(() -> new NotImplementedException("Support for " + ext + " is not implemented yet."))
            .getValue();
    }


}
