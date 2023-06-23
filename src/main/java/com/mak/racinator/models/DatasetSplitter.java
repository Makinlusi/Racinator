package com.mak.racinator.models;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;


public class DatasetSplitter {

    public void splitDataset(String inputDirectory, String trainingDirectory, String validationDirectory, double splitRatio) {
        File[] preprocessedImageFiles = new File(inputDirectory).listFiles();

        if (preprocessedImageFiles != null) {
            int totalImages = preprocessedImageFiles.length;
            int trainingCount = (int) (totalImages * splitRatio);
            int validationCount = totalImages - trainingCount;

            // Shuffle the preprocessed image files
            List<File> shuffledFiles = Arrays.asList(preprocessedImageFiles);
            Collections.shuffle(shuffledFiles);

            int count = 0;
            for (File imageFile : shuffledFiles) {
                if (count < trainingCount) {
                    moveFile(imageFile, trainingDirectory);
                } else {
                    moveFile(imageFile, validationDirectory);
                }
                count++;
            }
        }
    }

    private void moveFile(File file, String destinationDirectory) {
        Path sourcePath = file.toPath();
        Path destinationPath = Paths.get(destinationDirectory, file.getName());

        try {
            Files.move(sourcePath, destinationPath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        String inputDirectory = "src/main/resources/static/UTKFaces/data/preprocessed";
        String trainingDirectory = "src/main/resources/static/UTKFaces/data/training";
        String validationDirectory = "src/main/resources/static/UTKFaces/data/validation";
        double splitRatio = 0.8;

        DatasetSplitter splitter = new DatasetSplitter();
        splitter.splitDataset(inputDirectory, trainingDirectory, validationDirectory, splitRatio);
    }
}
