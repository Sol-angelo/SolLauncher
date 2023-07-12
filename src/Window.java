// Decompiled by Procyon v0.5.36
// 

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class Window extends Canvas
{
    private BufferedImage img;
    
    public Window(final float width, final float height, final String title, final Launcher launcher) {
        final JFrame frame = new JFrame(title);
        frame.setPreferredSize(new Dimension((int)width, (int)height));
        frame.setMaximumSize(new Dimension((int)width + 100, (int)height + 50));
        frame.setMinimumSize(new Dimension((int)width - 10, (int)height));
        /*final ImageIcon logo = new ImageIcon(this.getClass().getClassLoader().getResource("icon.png"));
        final InputStream is = this.getClass().getResourceAsStream("/icon.png");
        try {
            this.img = ImageIO.read(is);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        final Taskbar taskbar = Taskbar.getTaskbar();
        try {
            taskbar.setIconImage(this.img);
        }
        catch (UnsupportedOperationException e2) {
            System.out.println("the os does not support taskbar.setIconImage");
        }
        catch (SecurityException e3) {
            System.out.println("Security Exception");
        }
        frame.setIconImage(logo.getImage());*/
        frame.setDefaultCloseOperation(3);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.add(launcher);
        frame.setVisible(true);
        launcher.start();
    }
}
