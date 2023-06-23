package com.mak.racinator.models;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

public class RaceSorter {

    public static void main(String[] args) {
        String sourceDir = "src/main/resources/static/UTKFaces/data/testingSample";
        String destinationDir = "src/main/resources/static/UTKFaces/data/sorted_races";

        File sourceDirectory = new File(sourceDir);
        File[] files = sourceDirectory.listFiles();

        if (files != null) {
            for (File file : files) {
                if (file.isFile()) {
                    try {
                        String fileName = file.getName();
                        int race = extractRaceFromFileName(fileName);
                        if (race >= 0 && race <= 4) {  // Adjusted to match race values 0-4
                            String destinationPath = destinationDir + "/race_" + race + "/" + fileName;
                            createDirectoryIfNotExists(destinationDir + "/race_" + race);
                            Files.copy(file.toPath(), Path.of(destinationPath), StandardCopyOption.REPLACE_EXISTING);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    private static void createDirectoryIfNotExists(String directoryPath) {
        File directory = new File(directoryPath);
        if (!directory.exists()) {
            directory.mkdirs();
        }
    }

    private static int extractRaceFromFileName(String fileName) {
        String[] parts = fileName.split("_");
        if (parts.length >= 4) {
            try {
                return Integer.parseInt(parts[2]);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
        return -1;
    }
}
