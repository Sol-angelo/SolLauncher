// Decompiled by Procyon v0.5.36
// 

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class BufferedImageLoader
{
    BufferedImage image;
    
    public BufferedImage loadImage(final String path) {
        try {
            this.image = ImageIO.read(this.getClass().getResource(path));
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return this.image;
    }
}
