package com.parserapp.parsing.service;

import java.io.IOException;

public interface FileParser {

    void parseWithActions(String dir, String file) throws IOException;
}
