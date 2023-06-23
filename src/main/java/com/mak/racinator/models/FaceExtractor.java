package com.mak.racinator.models;

import nu.pattern.OpenCV;
import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.objdetect.CascadeClassifier;

import java.io.File;

public class FaceExtractor {

    static {
        OpenCV.loadLocally();
    }

    public void extractFaces(String inputDirectory, String outputDirectory) {
        File directory = new File(outputDirectory);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        String classifierPath = "src/main/resources/static/UTKFaces/data/haarcascade_frontalface_default.xml";
        CascadeClassifier faceClassifier = new CascadeClassifier(classifierPath);

        File[] preprocessedImageFiles = new File(inputDirectory).listFiles();

        if (preprocessedImageFiles != null) {
            for (File imageFile : preprocessedImageFiles) {
                if (imageFile.isFile()) {
                    Mat preprocessedImage = Imgcodecs.imread(imageFile.getAbsolutePath());

                    MatOfRect faceDetections = new MatOfRect();
                    faceClassifier.detectMultiScale(preprocessedImage, faceDetections);

                    int faceIndex = 1;
                    for (Rect face : faceDetections.toArray()) {
                        Mat extractedFace = new Mat(preprocessedImage, face);

                        String outputFilePath = outputDirectory + "/" + imageFile.getName() + "_face" + faceIndex + ".jpg";
                        Imgcodecs.imwrite(outputFilePath, extractedFace);

                        faceIndex++;
                    }
                }
            }
        }
    }

    public static void main(String[] args) {
        String inputDirectory = "src/main/resources/static/UTKFaces/data/preprocessed";
        String outputDirectory = "src/main/resources/static/UTKFaces/data/extracted_faces";
        FaceExtractor faceExtractor = new FaceExtractor();
        faceExtractor.extractFaces(inputDirectory, outputDirectory);
    }
}
