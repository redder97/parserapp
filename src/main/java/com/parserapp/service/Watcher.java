package com.parserapp.service;


import com.parserapp.parsing.ParserFactory;
import com.parserapp.parsing.service.TextParser;
import org.apache.commons.lang3.NotImplementedException;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.parserapp.Parser.PARSE_MAP;

public class Watcher {

    private final String observablePath;
    private final List<String> dirNames = List.of("processed", "unprocessed");

    public Watcher (String observablePath) {
        this.observablePath = observablePath;
    }

    public void doWatch() {
        ParserFactory parserFactory = ParserFactory.getInstance();
        try (WatchService watchService = FileSystems.getDefault().newWatchService()) {

            Map<WatchKey, Path> keyMap = new HashMap<>();
            Path path = Paths.get(observablePath);

            keyMap.put(path.register(
                watchService,
                StandardWatchEventKinds.ENTRY_CREATE
            ), path);

            WatchKey watchKey;

            do {
                watchKey = watchService.take();
                Path eventDir = keyMap.get(watchKey);

                for (WatchEvent<?> event : watchKey.pollEvents()) {
                    WatchEvent.Kind<?> kind = event.kind();
                    Path eventPath = (Path) event.context();

                    if (kind.equals(StandardWatchEventKinds.ENTRY_CREATE)
                        && !dirNames.contains(eventPath.toString())) {

                        System.out.println(eventDir + " ACTION: " + kind + " FILENAME: " + eventPath);
                        String fileExt = eventPath.getFileName().toString().split("\\.(?=[^\\.]+$)")[1];

                        System.out.println("found file with extension: " + fileExt + " || file is: " + eventPath);

                        try {
                            parserFactory.getAppropriateParser(fileExt)
                                .parseWithActions(eventDir.toString(), eventPath.toString());
                            handleAfterProcessed(eventDir.toString(), eventPath.toString());
                            doResults();
                        } catch (NotImplementedException e) {
                            System.out.println(e.getMessage());
                            handleUnprocessed(eventDir.toString(), eventPath.toString());
                        }

                    }
                }


            } while (watchKey.reset());

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void handleAfterProcessed(String dir, String file) throws IOException {
        final String path = dir + "\\" + file;

        if (!new File(dir + "\\processed").exists()) {
            Files.createDirectories(Paths.get(dir + "\\processed"));
        }

        Files.move(Paths.get(path), Paths.get(dir + "\\processed\\" + file));
    }

    private void handleUnprocessed(String dir, String file) throws IOException {
        final String path = dir + "\\" + file;

        if (!new File(dir + "\\unprocessed").exists()) {
            Files.createDirectories(Paths.get(dir + "\\unprocessed"));
        }

        Files.move(Paths.get(path), Paths.get(dir + "\\unprocessed\\" + file));
    }

    private void doResults() {
        System.out.println("PROCESSED");

        for (Map.Entry<String, Integer> entry : PARSE_MAP.entrySet()) {
            System.out.println(entry.getKey() + " : " + entry.getValue());
        }

        if (!PARSE_MAP.entrySet().isEmpty()) {
            Map.Entry<String, Integer> entry = PARSE_MAP
                .entrySet()
                .stream().filter(e -> StringUtils.isAlpha(e.getKey()))
                .sorted((x, y) -> y.getValue() - x.getValue())
                .findFirst()
                .get();

            int totalWords = PARSE_MAP
                .entrySet()
                .stream().filter(e -> StringUtils.isAlpha(e.getKey()))
                    .map(Map.Entry::getValue)
                        .reduce(0, Integer::sum);

            System.out.println("Total (alpha-only) words: " + totalWords);
            System.out.println("Most occuring word: "  + entry.getKey());
        }


    }
}
