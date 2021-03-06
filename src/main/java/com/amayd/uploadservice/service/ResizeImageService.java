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
import java.util.Optional;
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

        return processingResult;
    }

    public ProcessingResult getResizeImageStatus(final Long uid) {
        logger.debug("get processing results");
        ProcessingResult processingResult = processingResultRepository.findOne(uid);
        Optional<Future<String>> imageProcessingResult = Optional.ofNullable(requestMap.get(uid));

        if (imageProcessingResult.isPresent()){
            if(imageProcessingResult.get().isDone() && imageProcessingResult.get().equals("done")){
                processingResult.setFinished(true);
            }
        }

//        processingResultRepository.save(processingResult);
        return processingResult;
    }

    public ProcessingResult resizeImage(final ImageEntity imageEntity) {
        logger.debug("=================Service in==================");
        InputStream inputStream = getFileInputStream(imageEntity);
        Future<String> resizeResult = threadExecutor.changeSize(inputStream, imageEntity.getHeight(), imageEntity.getWidth(), false, imageEntity.getName());
        logger.debug("=================Service out==================");
        return createProcessingResult(resizeResult);
    }
}
