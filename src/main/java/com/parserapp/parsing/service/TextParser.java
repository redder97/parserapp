package com.parserapp.parsing.service;


import com.parserapp.parsing.action.ParsingAction;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

import static com.parserapp.Parser.PARSE_MAP;

public class TextParser implements FileParser {

    private final Collection<ParsingAction> parseActions = new ArrayList<>();

    public TextParser(List<ParsingAction> parsingActions) {
        this.parseActions.addAll(parsingActions);
    }

    public void parseWithActions(String dir, String file) throws IOException {
        final String path = dir + "\\" + file;
        try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
            String line = reader.readLine();

            while (line != null) {

                for (ParsingAction parsingAction : parseActions) {
                    processResults(parsingAction.doParse(line));
                }
                line = reader.readLine();
            }

        }

    }

    private void processResults(Map<String, Integer> result) {
        for (Map.Entry<String, Integer> resultEntry : result.entrySet()) {
            if (PARSE_MAP.containsKey(resultEntry.getKey())) {
                int updatedValue = PARSE_MAP.get(resultEntry.getKey()) + resultEntry.getValue();
                PARSE_MAP.put(
                    resultEntry.getKey(),
                    updatedValue
                );
            } else {
                PARSE_MAP.put(
                    resultEntry.getKey(),
                    resultEntry.getValue()
                );
            }
        }


    }

}
