package com.amayd.uploadservice.service;

import com.amayd.uploadservice.tools.ProcessImage;
import com.amayd.uploadservice.tools.ThreadExecutor;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.time.Instant;
import java.util.List;

public class ResizeImageService {

    public static List<String> changeSize (InputStream inputStream, int newHeight, int newWidth, boolean isTransparant){

        BufferedImage inputBufferedImage = null;
        List<String> images = null;
        try {
            inputBufferedImage = ImageIO.read(inputStream);
            images = ThreadExecutor.resizeImagesConcurrently(inputBufferedImage, newHeight, newWidth, isTransparant);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return images;
    }

}
