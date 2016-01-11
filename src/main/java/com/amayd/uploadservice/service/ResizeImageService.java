package com.amayd.uploadservice.service;

import com.amayd.uploadservice.tools.ProcessImage;
import com.amayd.uploadservice.tools.ThreadExecutor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.time.Instant;
import java.util.List;
import java.util.concurrent.Future;

@Service
public class ResizeImageService {

    @Async
    public Future<List<String>> changeSize (InputStream inputStream, int newHeight, int newWidth, boolean isTransparant){

        BufferedImage inputBufferedImage = null;
        List<String> images = null;
        try {
            inputBufferedImage = ImageIO.read(inputStream);
            images = ThreadExecutor.resizeImagesConcurrently(inputBufferedImage, newHeight, newWidth, isTransparant);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new AsyncResult<>(images);
    }

}
