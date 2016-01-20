package com.amayd.uploadservice.tools;

import com.amayd.uploadservice.modes.UploadedFile;
import com.amayd.uploadservice.repository.FileRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

@Service
public class ThreadExecutor {

    private static final Logger logger = LoggerFactory.getLogger(ThreadExecutor.class);

    @Autowired
    private FileRepository fileRepository;


    public static List<String> resizeImagesConcurrently(final BufferedImage inputBufferedImage, int newHeight, int newWidth, boolean isTransparent, String newName) {
        final ExecutorService executorService = Executors.newWorkStealingPool();

        List<String> images = null;
//        InputStream concurrentInputStream1 = inputStream;
//        BufferedImage inputBufferedImage = ImageIO.read(inputStream);

        final List<Callable<String>> callables = Arrays.asList(
                () -> {
                    BufferedImage bufferedImage = ProcessImage.createResizedCopy(inputBufferedImage, newHeight, newWidth, isTransparent);
                    Thread.sleep(60000);
                    return ProcessImage.saveImage(bufferedImage, newName);
                },
                () -> {
                    BufferedImage bufferedImage = ProcessImage.createResizedCopy(inputBufferedImage, newHeight * 2, newWidth * 2, isTransparent);
                    Thread.sleep(70000);
                    return ProcessImage.saveImage(bufferedImage, newName);
                },
//                () -> {
//                    BufferedImage bufferedImage = ProcessImage.createResizedCopy(inputBufferedImage, newHeight * 3, newWidth * 3, isTransparent);
//                    Thread.sleep(15000);
//                    return ProcessImage.saveImage(bufferedImage);
//                },
//                () -> {
//                    BufferedImage bufferedImage = ProcessImage.createResizedCopy(inputBufferedImage, newHeight * 4, newWidth * 4, isTransparent);
//                    Thread.sleep(5000);
//                    return ProcessImage.saveImage(bufferedImage);
//                },
//                () -> {
//                    BufferedImage bufferedImage = ProcessImage.createResizedCopy(inputBufferedImage, newHeight * 5, newWidth * 5, isTransparent);
//                    Thread.sleep(5000);
//                    return ProcessImage.saveImage(bufferedImage);
//                },
                () -> {
                    BufferedImage bufferedImage = ProcessImage.createResizedCopy(inputBufferedImage, newHeight * 6, newWidth * 6, isTransparent);
                    Thread.sleep(60000);
                    return ProcessImage.saveImage(bufferedImage, newName);
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

    @Async
    public Future<String> changeSize(final InputStream inputStream, int newHeight, int newWidth, boolean isTransparent, String newName) {

        logger.debug("Start changing an image");

        final String result;

        BufferedImage inputBufferedImage;
        List<String> images = null;
        try {
            inputBufferedImage = ImageIO.read(inputStream);
            images = ThreadExecutor.resizeImagesConcurrently(inputBufferedImage, newHeight, newWidth, isTransparent, newName);
        } catch (IOException e) {
            e.printStackTrace();
        }

        result = saveToDB(images);

        logger.debug("Image changed");

        return new AsyncResult<>(result);
    }

    private String saveToDB(List<String> locations){

        logger.debug("Save to DB");

        locations.stream().forEach( newImageLocation -> fileRepository.save(new UploadedFile(newImageLocation)));
        return "done";
    }
}
