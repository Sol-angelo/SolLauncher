import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;

public class Menu extends MouseAdapter
{
    private Launcher launcher;
    private Handler handler;
    private Random r;

    private LauncherLoadSave loadSave;
    public boolean[] clicked;

    
    public Menu(final Launcher launcher, final Handler handler, LauncherLoadSave loadSave) {
        this.r = new Random();
        this.launcher = launcher;
        this.handler = handler;
        this.loadSave = loadSave;
        this.clicked = new boolean[4];
    }
    
    @Override
    public void mousePressed(final MouseEvent e) {
        final int mx = e.getX();
        final int my = e.getY();
        if (Launcher.menuState == Launcher.STATE.Menu) {
            if (this.mouseOver(mx, my, 400, 300, 200, 64)) {
                if (clicked[0]) {
                    System.out.println("pressed");
                    try {
                        System.out.println("tried");
                        File file = LauncherLoadSave.getFileByOS("jars", "blob", "jar");
                        if (file.exists()) {
                            Util.exec(file);
                        } else {
                            Util.download("blob", Util.getLatestVersion("blob"));
                            Launcher.blobVersion = Util.getLatestVersion("blob");
                            LauncherLoadSave.writeToVersionFile();
                            Util.exec(file);
                        }
                        if (launcher.closeOnOpen) {
                            System.exit(2);
                        }
                    } catch (IOException ex) {
                        System.out.println("failed");
                        throw new RuntimeException(ex);
                    }
                }
                if (clicked[1]) {
                    System.out.println("pressed m");
                    try {
                        System.out.println("tried");
                        File file = LauncherLoadSave.getFileByOS("jars", "miraculous", "jar");
                        if (file.exists()) {
                            Util.exec(file);
                        } else {
                            Util.download("miraculous", Util.getLatestVersion("miraculous"));
                            Launcher.miraculousVersion = Util.getLatestVersion("miraculous");
                            LauncherLoadSave.writeToVersionFile();
                            Util.exec(file);
                        }
                        if (launcher.closeOnOpen) {
                            System.exit(2);
                        }
                    } catch (IOException ex) {
                        System.out.println("failed");
                        throw new RuntimeException(ex);
                    }
                }
                if (clicked[2]) {
                    System.out.println("pressed t");
                    try {
                        System.out.println("tried t");
                        File file = LauncherLoadSave.getFileByOS("jars", "tetris", "jar");
                        if (file.exists()) {
                            Util.exec(file);
                        } else {
                            Util.download("tetris", Util.getLatestVersion("tetris"));
                            Launcher.tetrisVersion = Util.getLatestVersion("tetris");
                            LauncherLoadSave.writeToVersionFile();
                            Util.exec(file);
                        }
                        if (launcher.closeOnOpen) {
                            System.exit(2);
                        }
                    } catch (IOException ex) {
                        System.out.println("failed");
                        throw new RuntimeException(ex);
                    }
                }
            }
            if (clicked[3]) {
                if (this.mouseOver(mx, my, 450, 174, 32, 16)) {
                    System.out.println("close on open pressed");
                    launcher.closeOnOpen = !launcher.closeOnOpen;
                    LauncherLoadSave.writeToSettingsFile();
                }
            }
            if (this.mouseOver(mx, my, 0, 100, 198, 100)) {
                this.clicked[0] = true;
                this.clicked[1] = false;
                this.clicked[2] = false;
                this.clicked[3] = false;
            }
            if (this.mouseOver(mx, my, 0, 200, 198, 100)) {
                this.clicked[1] = true;
                this.clicked[0] = false;
                this.clicked[2] = false;
                this.clicked[3] = false;
            }
            if (this.mouseOver(mx, my, 0, 300, 198, 100)) {
                this.clicked[2] = true;
                this.clicked[1] = false;
                this.clicked[0] = false;
                this.clicked[3] = false;
            }
            if (this.mouseOver(mx, my, 0, 0, 198, 100)) {
                this.clicked[0] = false;
                this.clicked[1] = false;
                this.clicked[2] = false;
                this.clicked[3] = true;
            }
        }
    }
    
    @Override
    public void mouseReleased(final MouseEvent e) {
    }
    
    private boolean mouseOver(final int mx, final int my, final int x, final int y, final int width, final int height) {
        return mx > x && mx < x + width && (my > y && my < y + height);
    }
    
    public void tick() {
    }
    
    public void render(final Graphics g) {
        final Font fnt = new Font("arial", 1, 50);
        final Font fnt2 = new Font("arial", 1, 30);
        final Font fnt3 = new Font("arial", 1, 20);
        if (Launcher.menuState == Launcher.STATE.Menu) {
            g.setColor(new Color(77, 77, 77));
            g.fillRect(0,0,198,700);
            g.setColor(new Color(54, 54, 54));
            g.drawLine(198,0, 198, 700);
            g.drawLine(197,0, 197, 700);
            g.drawLine(196,0, 196, 700);
            Launcher.drawThickRect(g,-2, 100, 200, 102, 3);
            Launcher.drawThickRect(g,-2, 200, 200, 102, 3);
            Launcher.drawThickRect(g,-2, 300, 200, 100, 3);
            if (clicked[0]) {
                g.setColor(new Color(119, 119, 119));
                g.fillRect(0,100,198,100);
                g.setColor(new Color(77, 77, 77));
                Launcher.drawThickRect(g,-2, 100, 200, 102, 3);
                g.fillRect(400, 300, 200, 64);
                g.setFont(fnt);
                g.setColor(new Color(33, 33, 33));
                g.drawString("Blob", 250, 90);
                g.setFont(fnt2);
                g.drawString("Play", 465, 350);
                g.drawString("Version "+launcher.blobVersion, 700, 90);
            }
            if (clicked[1]) {
                g.setColor(new Color(119, 119, 119));
                g.fillRect(0,200,198,100);
                g.setColor(new Color(77, 77, 77));
                Launcher.drawThickRect(g,-2, 200, 200, 102, 3);
                g.fillRect(400, 300, 200, 64);
                g.setFont(fnt);
                g.setColor(new Color(33, 33, 33));
                g.drawString("Miraculous", 250, 90);
                g.setFont(fnt2);
                g.drawString("Play", 465, 350);
                g.drawString("Version "+launcher.miraculousVersion, 700, 90);
            }
            if (clicked[2]) {
                g.setColor(new Color(119, 119, 119));
                g.fillRect(0,300,198,100);
                g.setColor(new Color(77, 77, 77));
                Launcher.drawThickRect(g,-2, 300, 200, 102, 3);
                g.fillRect(400, 300, 200, 64);
                g.setFont(fnt);
                g.setColor(new Color(33, 33, 33));
                g.drawString("Tetris", 250, 90);
                g.setFont(fnt2);
                g.drawString("Play", 465, 350);
                g.drawString("Version "+launcher.tetrisVersion, 700, 90);
            }
            if (clicked[3]) {
                g.setColor(new Color(119, 119, 119));
                g.fillRect(0,0,198,100);
                g.setColor(new Color(77, 77, 77));
                g.fillRect(250, 150, 240, 64);
                Launcher.drawThickRect(g,-2, 0, 200, 102, 3);
                g.setColor(new Color(33, 33, 33));
                g.setFont(fnt);
                g.drawString("Settings", 250, 90);
                g.setFont(fnt3);
                g.drawString("Close on Launch", 260, 190);
                g.drawRect(450, 174, 32, 16);
                g.drawRect(250, 150, 240, 64);
                if (launcher.closeOnOpen) {
                    g.setColor(new Color(45, 225, 50));
                    g.fillRect(451, 175, 15, 14);
                } else {
                    g.setColor(new Color(215, 65, 31));
                    g.fillRect(466, 175, 15, 14);
                }
            }
            g.drawImage(Launcher.blobLogo, 60, 118, null);
        } else if (Launcher.menuState == Launcher.STATE.Saveload) {
            g.setFont(fnt2);
            g.setColor(Color.white);
            g.drawString("Back", 130, 90);
            g.drawRect(50, 50, 200, 64);
            g.drawString("Save 1", 265, 190);
            g.drawString("Save 2", 265, 290);
            g.drawString("Save 3", 265, 390);
            g.drawRect(210, 150, 200, 64);
            g.drawRect(210, 250, 200, 64);
            g.drawRect(210, 350, 200, 64);
        } else if (Launcher.menuState == Launcher.STATE.Help) {
            g.setFont(fnt2);
            g.setColor(Color.white);
            g.drawString("Back", 130, 90);
            g.setColor(Color.WHITE);
            g.drawRect(50, 50, 200, 64);
        }
    }
}
