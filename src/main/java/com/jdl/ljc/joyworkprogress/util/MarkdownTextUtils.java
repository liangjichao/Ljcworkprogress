package com.jdl.ljc.joyworkprogress.util;

import com.intellij.util.ui.ImageUtil;
import org.apache.commons.codec.binary.Base64;

import java.awt.*;
import java.awt.image.BufferedImage;

public class MarkdownTextUtils {
    public static String createLink(String title,String url){
        return String.format("[%s](%s)",title,url);
    }
    public static String createImg(String url){
        return String.format("![%s](%s)","",url);
    }

    public static String imageToBase64(Image image) {
        BufferedImage bufferedImage = ImageUtil.toBufferedImage(image);
        Graphics2D graphics = bufferedImage.createGraphics();
        return null;
    }
}
