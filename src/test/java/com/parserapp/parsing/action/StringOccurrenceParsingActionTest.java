package com.parserapp.parsing.action;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Map;

public class StringOccurrenceParsingActionTest {

    @DisplayName("Test to add words in map")
    @Test
    public void testAddWords() {
        ParsingAction parsingAction = new StringOccurrenceParsingAction();
        Map<String, Integer> parseResult = parsingAction.doParse("series of wOrds woRdS");
        Assertions.assertEquals(parseResult.get("words"), 2);
    }
}
