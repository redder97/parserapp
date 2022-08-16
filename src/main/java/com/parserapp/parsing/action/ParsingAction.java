package com.parserapp.parsing.action;

import java.util.Map;

public interface ParsingAction {

    Map<String, Integer> doParse(String fileContent);

}
