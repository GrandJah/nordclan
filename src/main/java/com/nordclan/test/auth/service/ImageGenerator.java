package com.nordclan.test.auth.service;

import org.springframework.stereotype.Service;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import javax.imageio.ImageIO;

@Service
public class ImageGenerator {
  public byte[] generateFromText(String text) {
    try {
      byte[] digest = MessageDigest.getInstance("MD5")
        .digest(text.getBytes(StandardCharsets.UTF_8));
      boolean[][] rawimg = new boolean[16][16];
      int scale = 10;
      int w = 16 * scale;
      int h = 16 * scale;
      int white = Color.WHITE.getRGB();
      int colorAva = new Color(digest[13] + 128, digest[14] + 128, digest[15] + 128).getRGB();
      for (int j = 0; j < 16; j++) {
        for (int i = 0; i < 8; i++) {
          boolean pix = true;
          if (j != 0 && j != 15 && i != 0) {
            pix = (digest[(j + 1) % 12] & (1L << i)) > 0;
          }
          rawimg[j][i] = pix;
          rawimg[j][15 - i] = pix;
        }
      }

      BufferedImage bimg = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
      for (int j = 0; j < h; j++) {
        for (int i = 0; i < w; i++) {
          bimg.setRGB(i, j, rawimg[j / scale][i / scale] ? white : colorAva);
        }
      }

      ByteArrayOutputStream out = new ByteArrayOutputStream();
      ImageIO.write(bimg, "jpeg", out);
      return out.toByteArray();
    } catch (Exception ex) {
      ex.printStackTrace();
      return new byte[0];
    }
  }
}
