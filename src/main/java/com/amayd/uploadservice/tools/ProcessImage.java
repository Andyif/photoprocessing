package com.amayd.uploadservice.tools;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.time.Instant;

public class ProcessImage {
    public static synchronized BufferedImage createResizedCopy(Image originalImage,
                                                  int scaledWidth, int scaledHeight,
                                                  boolean preserveAlpha)
    {
        System.out.println("resizing...");
        System.out.println("changing size " + Thread.currentThread().getName());
        int imageType = preserveAlpha ? BufferedImage.TYPE_INT_RGB : BufferedImage.TYPE_INT_ARGB;
        BufferedImage scaledBI = new BufferedImage(scaledWidth, scaledHeight, imageType);
        Graphics2D g = scaledBI.createGraphics();
        if (preserveAlpha) {
            g.setComposite(AlphaComposite.Src);
        }
        g.drawImage(originalImage, 0, 0, scaledWidth, scaledHeight, null);
        g.dispose();
        return scaledBI;
    }

    public static synchronized String saveImage(BufferedImage outputBufferedImage){
        System.out.println("saving an image" + Thread.currentThread().getName());

        File outputfile = new File("C:/Users/Andy_/Desktop/" + Instant.now().getEpochSecond() + "_" + outputBufferedImage.getHeight() + ".gif");
        String path = outputfile.getAbsolutePath().replace("\\", "/");

        try {
            ImageIO.write(outputBufferedImage, "gif", outputfile);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return path;
    }
}
