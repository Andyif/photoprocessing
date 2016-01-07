package com.amayd.uploadservice.tools;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

public class ThreadExecutor {

    public static List<String> resizeImagesConcurrently(BufferedImage inputBufferedImage, int newHeight, int newWidth, boolean isTransparant) throws IOException {
        ExecutorService executorService = Executors.newWorkStealingPool();

        List<String> images = null;
//        InputStream concurrentInputStream1 = inputStream;
//        BufferedImage inputBufferedImage = ImageIO.read(inputStream);

        List<Callable<String>> callables = Arrays.asList(
                () -> {
                    BufferedImage bufferedImage = ProcessImage.createResizedCopy(inputBufferedImage, newHeight, newWidth, isTransparant);
                    return ProcessImage.saveImage(bufferedImage);
                },
                () -> {
                    BufferedImage bufferedImage = ProcessImage.createResizedCopy(inputBufferedImage, newHeight * 2, newWidth * 2, isTransparant);
                    return ProcessImage.saveImage(bufferedImage);
                },
                () -> {
                    BufferedImage bufferedImage = ProcessImage.createResizedCopy(inputBufferedImage, newHeight * 3, newWidth * 3, isTransparant);
                    return ProcessImage.saveImage(bufferedImage);
                }
        );

        try {
            images = executorService.invokeAll(callables).stream().map(future -> {
                try {
                    return future.get();
                }
                catch (Exception e) {
                    throw new IllegalStateException(e);
                }
            }).collect(Collectors.toList());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return images;
    }
}
