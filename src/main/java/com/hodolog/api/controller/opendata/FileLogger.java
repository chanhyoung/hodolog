package com.hodolog.api.controller.opendata;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FileLogger {
    private static final String LOG_DIRECTORY = "./output";
    private final String filePath;

    public FileLogger(String fileName) {
        this.filePath = LOG_DIRECTORY + "/" + fileName;
    }

    public void log(String message) {
        try (FileWriter fw = new FileWriter(filePath, true);
                BufferedWriter bw = new BufferedWriter(fw);
                PrintWriter out = new PrintWriter(bw)) {
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            out.println(timestamp + " - " + message);
        } catch (IOException e) {
            log.error("Could not write to log file", e);
        }
    }
}

