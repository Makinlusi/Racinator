package com.mak.racinator.models;

import nu.pattern.OpenCV;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.core.CvType;
import org.opencv.imgproc.Imgproc;
import org.opencv.imgcodecs.Imgcodecs;

import java.io.File;

public class ImagePreprocessor {

    static {
        OpenCV.loadLocally();
    }

    public void preprocessImages(String inputDirectory, String outputDirectory, int targetWidth, int targetHeight) {
        File directory = new File(outputDirectory);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        File[] imageFiles = new File(inputDirectory).listFiles();

        if (imageFiles != null) {
            for (File imageFile : imageFiles) {
                if (imageFile.isFile()) {
                    Mat inputImage = Imgcodecs.imread(imageFile.getAbsolutePath());
                    Mat preprocessedImage = preprocessImage(inputImage, targetWidth, targetHeight);
                    String outputFilePath = outputDirectory + "/" + imageFile.getName();
                    Imgcodecs.imwrite(outputFilePath, preprocessedImage);
                }
            }
        }
    }

    Mat preprocessImage(Mat inputImage, int targetWidth, int targetHeight) {
        Mat resizedImage = new Mat();
        Size targetSize = new Size(targetWidth, targetHeight);
        Imgproc.resize(inputImage, resizedImage, targetSize, 0, 0, Imgproc.INTER_LINEAR);

        Mat grayImage = new Mat();
        Imgproc.cvtColor(resizedImage, grayImage, Imgproc.COLOR_BGR2GRAY);

        Mat normalizedImage = new Mat();
        grayImage.convertTo(normalizedImage, CvType.CV_32F, 1.0 / 255.0);

        Mat preprocessedImage = new Mat();
        normalizedImage.convertTo(preprocessedImage, CvType.CV_8U, 255.0);

        Mat denoisedImage = new Mat();
        Imgproc.GaussianBlur(preprocessedImage, denoisedImage, new Size(3, 3), 0);

        return denoisedImage;
    }

    public static void main(String[] args) {
        String inputDirectory = "src/main/resources/static/UTKFaces/data/testingSample";
        String outputDirectory = "src/main/resources/static/UTKFaces/data/preprocessed";
        int targetWidth = 224;
        int targetHeight = 224;

        ImagePreprocessor preprocessor = new ImagePreprocessor();
        preprocessor.preprocessImages(inputDirectory, outputDirectory, targetWidth, targetHeight);
    }
}
