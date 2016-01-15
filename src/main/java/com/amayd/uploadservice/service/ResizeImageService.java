package com.amayd.uploadservice.service;

import com.amayd.uploadservice.modes.UploadedFile;
import com.amayd.uploadservice.repository.FileRepository;
import com.amayd.uploadservice.rest.model.ImageEntity;
import com.amayd.uploadservice.tools.ThreadExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;

@Service
public class ResizeImageService {

    private static final Logger logger = LoggerFactory.getLogger(ResizeImageService.class);

    private Map <Long, Future<String>> requestMap = new HashMap<>();
    private Future<String> processResult;
    private Long uid;

    public Long resizeFromRest(final ImageEntity imageEntity) {
        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream(new File(imageEntity.getUrl()));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        if (inputStream != null){
            processResult = changeSize(inputStream, imageEntity.getImageNewSize().getHeight(), imageEntity.getImageNewSize().getWidth(), false);
            uid = Instant.now().getEpochSecond();
        }

        requestMap.put(uid, processResult);

        return uid;

    }

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
