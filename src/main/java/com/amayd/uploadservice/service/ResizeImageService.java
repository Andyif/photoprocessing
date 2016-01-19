package com.amayd.uploadservice.service;

import com.amayd.uploadservice.modes.UploadedFile;
import com.amayd.uploadservice.repository.FileRepository;
import com.amayd.uploadservice.rest.model.ImageEntity;
import com.amayd.uploadservice.rest.model.ProcessingResult;
import com.amayd.uploadservice.rest.repository.ImageEntityRepository;
import com.amayd.uploadservice.rest.repository.ProcessingResultRepository;
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
import java.net.MalformedURLException;
import java.net.URL;
import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

@Service
public class ResizeImageService {

    @Autowired
    ImageEntityRepository imageEntityRepository;

    @Autowired
    ProcessingResultRepository processingResultRepository;

    @Autowired
    private FileRepository fileRepository;

    private static final Logger logger = LoggerFactory.getLogger(ResizeImageService.class);

    private Map <Long, Future<String>> requestMap = new HashMap<>();

    public InputStream getFileInputStream(final ImageEntity imageEntity) {

        if (imageEntity.getLocalFile() != null){
            try {
                return new FileInputStream(new File(imageEntity.getLocalFile()));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }else {
            try {
                return new URL(imageEntity.getUrl()).openStream();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return null;
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

    public ProcessingResult createProcessingResult(Future<String> resizeResult) {
        Long uid = Instant.now().getEpochSecond();
        requestMap.put(uid, resizeResult);
        ProcessingResult processingResult = new ProcessingResult();
        processingResult.setUid(uid);

        processingResultRepository.save(processingResult);

        return processingResult;
    }

    public ProcessingResult getProcessingStatus(Long uid) {
        ProcessingResult processingResult = processingResultRepository.findOne(uid);
        Future<String> imageProcessingResult = requestMap.get(uid);

        try {
            if(imageProcessingResult.isDone() || imageProcessingResult.get().equals("Done")){
                 processingResult.setFinished(true);
            }
        } catch (InterruptedException|ExecutionException e) {
            e.printStackTrace();
        }

        processingResultRepository.save(processingResult);
        return processingResult;
    }
}
