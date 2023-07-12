import net.htmlparser.jericho.Source;

import java.awt.*;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.util.*;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

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
        checkVersion();
        download("Blob", "0.1.1");
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
            File file = LauncherLoadSave.getFileByOS("jars", name.toLowerCase(), "jar");
            copyURLToFile(url, file);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    public void checkVersion() {
        String version = new Source(getTextFromGithub("https://raw.githubusercontent.com/Sol-angelo/SolLauncher/master/version.txt")).getRenderer().toString();
        String[] versionsep = version.split(" ");
        if (!Objects.equals(versionsep[0], blobVersion)) {
            download("Blob", versionsep[0]);
        }
        if (!Objects.equals(versionsep[1], blobVersion)) {
            download("Miraculous", versionsep[1]);
        }
        if (!Objects.equals(versionsep[2], blobVersion)) {
            download("Tetris", versionsep[2]);
        }
        System.out.println(version);
    }

    public static String getTextFromGithub(String link) {
        URL Url = null;
        try {
            Url = new URL(link);
        } catch (MalformedURLException e1) {
            e1.printStackTrace();
        }
        HttpURLConnection Http = null;
        try {
            Http = (HttpURLConnection) Url.openConnection();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        Map<String, List<String>> Header = Http.getHeaderFields();

        for (String header : Header.get(null)) {
            if (header.contains(" 302 ") || header.contains(" 301 ")) {
                link = Header.get("Location").get(0);
                try {
                    Url = new URL(link);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                try {
                    Http = (HttpURLConnection) Url.openConnection();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Header = Http.getHeaderFields();
            }
        }
        InputStream Stream = null;
        try {
            Stream = Http.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String Response = null;
        try {
            Response = GetStringFromStream(Stream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Response;
    }

    private static String GetStringFromStream(InputStream Stream) throws IOException {
        if (Stream != null) {
            Writer Writer = new StringWriter();
            PrintWriter pw = new PrintWriter(Writer);
            char[] Buffer = new char[2048];
            try {
                Reader Reader = new BufferedReader(new InputStreamReader(Stream, "UTF-8"));
                int counter;
                while ((counter = Reader.read(Buffer)) != -1) {
                    Writer.write(Buffer, 0, counter);
                }
            } finally {
                Stream.close();
            }
            return Writer.toString();
        } else {
            return "No Contents";
        }
    }

    public static void copyURLToFile(URL url, File file) {
        try {
            InputStream input = url.openStream();
            if (file.exists()) {
                if (file.isDirectory())
                    throw new IOException("File '" + file + "' is a directory");

                if (!file.canWrite())
                    throw new IOException("File '" + file + "' cannot be written");
            } else {
                File parent = file.getParentFile();
                if ((parent != null) && (!parent.exists()) && (!parent.mkdirs())) {
                    throw new IOException("File '" + file + "' could not be created");
                }
            }

            FileOutputStream output = new FileOutputStream(file);

            byte[] buffer = new byte[4096];
            int n = 0;
            while (-1 != (n = input.read(buffer))) {
                output.write(buffer, 0, n);
            }

            input.close();
            output.close();

            System.out.println("File '" + file + "' downloaded successfully!");
        }
        catch(IOException ioEx) {
            ioEx.printStackTrace();
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
