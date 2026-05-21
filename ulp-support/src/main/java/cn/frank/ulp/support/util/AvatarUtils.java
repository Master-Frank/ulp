package cn.frank.ulp.support.util;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Random;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.RandomStringUtils;

/**
 * 头像生成工具.
 */
public class AvatarUtils {

    public static final String  RANDOM_AVATAR    = "https://api.multiavatar.com/";

    private static final Pattern CHINESE_PATTERN = Pattern.compile("[\u4e00-\u9fa5]");

    private static final Color[] BG_COLORS       = new Color[] { new Color(244, 67, 54),
        new Color(233, 30, 99), new Color(156, 39, 176), new Color(103, 58, 183),
        new Color(63, 81, 181), new Color(33, 150, 243), new Color(0, 188, 212),
        new Color(0, 150, 136), new Color(76, 175, 80), new Color(255, 152, 0) };

    public AvatarUtils() {
    }

    public static BufferedImage generateAvatarImg(String text) {
        try {
            int size = 100;
            String draw;
            if (text.length() <= 2) {
                draw = text;
            } else if (isChinese(text.substring(0, 1))) {
                draw = text.substring(text.length() - 2);
            } else {
                draw = text.substring(0, 2).toUpperCase();
            }

            BufferedImage img = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g = (Graphics2D) img.getGraphics();
            g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

            Color bg = BG_COLORS[new Random().nextInt(BG_COLORS.length)];
            g.setBackground(bg);
            g.clearRect(0, 0, size, size);
            g.setPaint(Color.WHITE);

            Font font = new Font(Font.SANS_SERIF, Font.PLAIN, 30);
            if (draw.length() == 2) {
                if (isChinese(draw)) {
                    g.setFont(font.deriveFont(Font.BOLD, 50f));
                    g.drawString(draw, 25, 70);
                } else {
                    g.setFont(font.deriveFont(Font.PLAIN, 55f));
                    g.drawString(draw.toUpperCase(), 33, 67);
                }
            } else if (draw.length() == 1) {
                if (isChinese(draw)) {
                    g.setFont(font.deriveFont(Font.BOLD, 60f));
                    g.drawString(draw, 30, 70);
                } else {
                    g.setFont(font.deriveFont(Font.PLAIN, 65f));
                    g.drawString(draw.toUpperCase(), 35, 70);
                }
            } else {
                g.setFont(font.deriveFont(Font.BOLD, 30f));
                g.drawString(draw, 20, 60);
            }
            g.dispose();
            return makeRoundedCorner(img, 99);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String bufferedImageToBase64(BufferedImage image) {
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            ImageIO.write(image, "png", out);
            return "data:image/png;base64," + new Base64().encodeToString(out.toByteArray());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean isChinese(String input) {
        return CHINESE_PATTERN.matcher(input).find();
    }

    public static BufferedImage makeRoundedCorner(BufferedImage source, int radius) {
        int w = source.getWidth();
        int h = source.getHeight();
        BufferedImage out = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = out.createGraphics();
        g.setComposite(AlphaComposite.Src);
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setColor(Color.WHITE);
        g.fill(new RoundRectangle2D.Float(0f, 0f, w, h, radius, radius));
        g.setComposite(AlphaComposite.SrcAtop);
        g.drawImage(source, 0, 0, null);
        g.dispose();
        return out;
    }

    public static String getRandomAvatar() {
        return RANDOM_AVATAR + RandomStringUtils.randomAlphanumeric(6) + ".svg";
    }
}
