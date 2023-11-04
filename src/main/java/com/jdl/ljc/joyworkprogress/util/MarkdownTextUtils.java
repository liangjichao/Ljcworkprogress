package com.jdl.ljc.joyworkprogress.util;

import com.intellij.util.ui.ImageUtil;
import org.apache.commons.codec.binary.Base64;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class MarkdownTextUtils {
    public static String createLink(String title,String url){
        return String.format("[%s](%s)",title,url);
    }
    public static String createImg(String url){
        return String.format("![%s](%s)","",url);
    }

    public static String imageToBase64(Image image,String formatName) {
        BufferedImage bufferedImage = ImageUtil.toBufferedImage(image);
        Graphics2D graphics = bufferedImage.createGraphics();
        graphics.drawImage(image, 0, 0, null);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try {
            ImageIO.write(bufferedImage, formatName, outputStream);
        } catch (IOException e) {
            return null;
        }
        byte[] imageBytes = outputStream.toByteArray();
        return Base64.encodeBase64String(imageBytes);
    }
}
