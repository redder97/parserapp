package com.parserapp.parsing.action;

import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;

public class StringOccurrenceParsingAction implements ParsingAction {

    @Override
    public Map<String, Integer> doParse(String fileContent) {
        Map<String, Integer> resultMap = new HashMap<>();

        String[] wordInstances = fileContent.split("\\W+");

        for (String word : wordInstances) {
            String currentWord = word.toLowerCase();
            if (resultMap.containsKey(currentWord)) {
                int updatedValue = resultMap.get(currentWord) + 1;
                resultMap.put(currentWord, updatedValue);
            } else {
                resultMap.put(currentWord, 1);
            }
        }

        int dotMatches = StringUtils.countMatches(fileContent, ".");

        resultMap.put(".", dotMatches);

        return resultMap;
    }
}
