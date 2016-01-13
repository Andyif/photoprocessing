package com.amayd.uploadservice.tools;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.time.Instant;

public class ProcessImage {


    private static final Logger logger = LoggerFactory.getLogger(ProcessImage.class);

    public static BufferedImage createResizedCopy(final Image originalImage,
                                                  int scaledWidth, int scaledHeight,
                                                  boolean preserveAlpha)
    {
        logger.debug("resizing started");
        logger.debug("resizing thread" + Thread.currentThread().getName());

        final int imageType = preserveAlpha ? BufferedImage.TYPE_INT_RGB : BufferedImage.TYPE_INT_ARGB;
        final BufferedImage scaledBI = new BufferedImage(scaledWidth, scaledHeight, imageType);
        final Graphics2D g = scaledBI.createGraphics();
        if (preserveAlpha) {
            g.setComposite(AlphaComposite.Src);
        }
        g.drawImage(originalImage, 0, 0, scaledWidth, scaledHeight, null);
        g.dispose();

        logger.debug("resizing finished");

        return scaledBI;
    }

    public static String saveImage(BufferedImage outputBufferedImage){
        logger.debug("saving started");
        logger.debug("saving thread" + Thread.currentThread().getName());

        final File desktop = new File(System.getProperty("user.home"), "Desktop");

        final File outPutFile = new File(desktop.getAbsolutePath() + "/" + Instant.now().getEpochSecond() + "_" + outputBufferedImage.getHeight() + ".png");
        final String path = outPutFile.getAbsolutePath().replace("\\", "/");

        try {
            ImageIO.write(outputBufferedImage, "png", outPutFile);
        } catch (IOException e) {
            e.printStackTrace();
        }

        logger.debug("saving finished");

        return path;
    }
}
