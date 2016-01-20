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
    ProcessingResultRepository processingResultRepository;

    @Autowired
    private ThreadExecutor threadExecutor;

    private static final Logger logger = LoggerFactory.getLogger(ResizeImageService.class);

    private Map <Long, Future<String>> requestMap = new HashMap<>();

    private InputStream getFileInputStream(final ImageEntity imageEntity) {
        logger.debug("get Input Stream");
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

    private ProcessingResult createProcessingResult(final Future<String> resizeResult) {
        Long uid = Instant.now().getEpochSecond();
        requestMap.put(uid, resizeResult);
        ProcessingResult processingResult = new ProcessingResult();
        processingResult.setUid(uid);

        processingResultRepository.save(processingResult);

        logger.debug("======> result <====== " + resizeResult.isDone());

        return processingResult;
    }

    public ProcessingResult getProcessingStatus(final Long uid) {
        logger.debug("get processing results");
        ProcessingResult processingResult = processingResultRepository.findOne(uid);
        Future<String> imageProcessingResult = requestMap.get(uid);

        logger.debug("======> Is Done <====== " + imageProcessingResult.isDone());

        try {
            if(imageProcessingResult.isDone() && imageProcessingResult.get().equals("done")){
                logger.debug("======> Is Done <====== " + imageProcessingResult.isDone());
                logger.debug("======> Is Done <====== " + imageProcessingResult.get());

                logger.debug("processing finished");
                 processingResult.setFinished(true);
            }
        } catch (InterruptedException|ExecutionException e) {
            e.printStackTrace();
        }

//        processingResultRepository.save(processingResult);
        return processingResult;
    }

    public ProcessingResult resizeImage(final ImageEntity imageEntity) {
        logger.debug("Start resizing");
        InputStream inputStream = getFileInputStream(imageEntity);
        Future<String> resizeResult = threadExecutor.changeSize(inputStream, imageEntity.getHeight(), imageEntity.getWidth(), false, imageEntity.getName());
        logger.debug("return result");
        return createProcessingResult(resizeResult);
    }
}
