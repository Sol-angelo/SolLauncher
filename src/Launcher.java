import java.awt.*;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Random;
import java.util.Scanner;

public class Launcher extends Canvas implements Runnable
{
    public static final float WIDTH = 640.0f;
    public static final float HEIGHT = 480.0f;
    public String blobVersion;
    public String miraculousVersion;
    public String tetrisVersion;
    public boolean closeOnOpen = false;
    private Thread thread;
    private boolean running;
    private Random r;
    private Handler handler;
    private Menu menu;
    private LauncherLoadSave loadSave;
    public static BufferedImage sprite_sheet;
    public static STATE menuState;
    public static int menuStateAsInt;

    public Launcher() {
        this.running = false;
        menuState = STATE.Menu;
        this.handler = new Handler(this);
        this.loadSave = new LauncherLoadSave(this);
        LauncherLoadSave.ReadFromSettingsFile();
        this.menu = new Menu(this, this.handler, this.loadSave);
        this.addKeyListener(new KeyInput(this.handler, this));
        this.addMouseListener(this.menu);
        new Window(1000.0f, 700.0f, "Launcher", this);
        final BufferedImageLoader loader = new BufferedImageLoader();
        //Launcher.sprite_sheet = loader.loadImage("/sprites.png");
        LauncherLoadSave.ReadFromVersionFile();
        this.r = new Random();
        //download("Blob", "0.1.1");
    }
    
    public synchronized void start() {
        (this.thread = new Thread(this)).start();
        this.running = true;
    }
    
    public synchronized void stop() {
        try {
            this.thread.join();
            this.running = false;
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void download(String name, String version) {
        try {
            URL url = new URL("https://github.com/Sol-angelo/" + name + "/releases/download/v" + version + "/" + name.toLowerCase() + ".jar");
            url.openConnection();
            System.out.println("connected");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void checkVersion() {
        try {
            URL url = new URL("http://www.puzzlers.org/pub/wordlists/pocket.txt");
            Scanner s = new Scanner(url.openStream());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    
    @Override
    public void run() {
        this.requestFocus();
        long lastTime = System.nanoTime();
        final double amountOfTicks = 60.0;
        final double ns = 1.0E9 / amountOfTicks;
        double delta = 0.0;
        long timer = System.currentTimeMillis();
        float frames = 0.0f;
        while (this.running) {
            final long now = System.nanoTime();
            delta += (now - lastTime) / ns;
            lastTime = now;
            while (delta >= 1.0) {
                this.tick();
                --delta;
            }
            if (this.running) {
                this.render();
            }
            ++frames;
            if (System.currentTimeMillis() - timer > 1000L) {
                timer += 1000L;
                System.out.println(frames);
                frames = 0.0f;
            }
        }
        this.stop();
    }
    
    private void tick() {
        this.handler.tick();
        if (this.menuState == STATE.Menu || this.menuState == STATE.Help) {
            this.menu.tick();
        }
    }
    
    private void render() {
        final BufferStrategy bs = this.getBufferStrategy();
        if (bs == null) {
            this.createBufferStrategy(3);
            return;
        }
        final Graphics g = bs.getDrawGraphics();
        g.setColor(new Color(59, 59, 59));
        g.fillRect(0, 0, 1000, 700);
        this.handler.render(g);
        if (this.menuState == STATE.Menu || this.menuState == STATE.Help || this.menuState == STATE.Saveload) {
            this.menu.render(g);
        }
        g.dispose();
        bs.show();
    }

    public static Process exec(File file) throws IOException {
        String javaHome = System.getProperty("java.home");
        String javaBin = javaHome +
                File.separator + "bin" +
                File.separator + "java";
        ProcessBuilder pb = new ProcessBuilder(javaBin, "-jar", file.toString());
        return pb.start();
    }

    public static void drawThickRect(Graphics g, int x, int y, int width, int height, int thickness) {
        for (int i = 0; i < thickness; i++)
            g.drawRect(x + i, y + i, width - 2 * i, height - 2 * i);
    }
    
    public static float clamp(float var, final float min, final float max) {
        if (var >= max) {
            var = max;
            return max;
        }
        if (var <= min) {
            var = min;
            return min;
        }
        return var;
    }
    
    public static void main(final String[] args) {
        new Launcher();
    }
    public enum STATE
    {
        Menu,
        Help,
        Saveload
    }
}
