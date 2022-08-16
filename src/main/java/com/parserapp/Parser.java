package com.parserapp;

import com.parserapp.parsing.action.StringOccurrenceParsingAction;
import com.parserapp.parsing.service.TextParser;
import com.parserapp.service.Watcher;

import java.util.HashMap;
import java.util.Map;

public class Parser {
    public static final Map<String, Integer> PARSE_MAP = new HashMap<>();

    public static void main(String[] args) throws Exception {

        String dirToWatch = args[0];

        if (dirToWatch == null) {
            throw new Exception("Directory to watch cannot be empty!");
        }

        System.out.println("Watch start at dir: " + dirToWatch);


        Watcher watcher = new Watcher(dirToWatch);

        watcher.doWatch();

    }

}
