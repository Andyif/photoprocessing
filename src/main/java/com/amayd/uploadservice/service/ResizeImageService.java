package com.amayd.uploadservice.service;

import com.amayd.uploadservice.tools.ThreadExecutor;
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

    @Async
    public Future<List<String>> changeSize(final InputStream inputStream, int newHeight, int newWidth, boolean isTransparent) {

        BufferedImage inputBufferedImage;
        List<String> images = null;
        try {
            inputBufferedImage = ImageIO.read(inputStream);
            images = ThreadExecutor.resizeImagesConcurrently(inputBufferedImage, newHeight, newWidth, isTransparent);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new AsyncResult<>(images);
    }

}
