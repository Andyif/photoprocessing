package com.amayd.uploadservice.service;

import com.amayd.uploadservice.modes.UploadedFile;
import com.amayd.uploadservice.repository.FileRepository;
import com.amayd.uploadservice.tools.ThreadExecutor;
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
import java.util.List;
import java.util.concurrent.Future;

@Service
public class ResizeImageService {

    private static final Logger logger = LoggerFactory.getLogger(ResizeImageService.class);


    @Autowired
    private FileRepository fileRepository;

    @Async
    public Future<String> changeSize(final InputStream inputStream, int newHeight, int newWidth, boolean isTransparent) {

        logger.debug("Start changing an image");

        final String result;

        BufferedImage inputBufferedImage;
        List<String> images = null;
        try {
            inputBufferedImage = ImageIO.read(inputStream);
            images = ThreadExecutor.resizeImagesConcurrently(inputBufferedImage, newHeight, newWidth, isTransparent);
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
