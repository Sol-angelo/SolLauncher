import java.io.*;
import java.nio.file.Path;

public class LauncherLoadSave {
    public static int state;
    public static int fileAmount;

    private static Launcher launcher;

    public LauncherLoadSave(Launcher launcher) {
        this.launcher = launcher;
    }

    public static File getFileByOS(String addpath, String name, String fileType) {
        String osname = System.getProperty("os.name");
        if (osname.contains("Mac")) {
            Path path = Path.of(System.getProperty("user.home"), "Library", "Application Support", "Solangelo", "Launcher");
            File path2 = new File(path + "/" + addpath);
            path2.mkdirs();
            File txt = new File(path2 + "/"+name+"."+fileType);
            return txt;
        } else if (osname.contains("Window")) {
            Path path = Path.of(System.getProperty("user.home"), "AppData", "Solangelo", "Launcher");
            File path2 = new File(path + "/" + addpath);
            path2.mkdirs();
            File txt = new File(path2 + "/"+name+"."+fileType);
            return txt;
        }
        return null;
    }

    public static File getFolderByOS(String addpath, String name) {
        String osname = System.getProperty("os.name");
        if (osname.contains("Mac")) {
            Path path = Path.of(System.getProperty("user.home"), "Library", "Application Support", "Solangelo", "Launcher");
            File path2 = new File(path + "/" + addpath);
            path2.mkdirs();
            File txt = new File(path2 + "/"+name);
            return txt;
        } else if (osname.contains("Window")) {
            Path path = Path.of(System.getProperty("user.home"), "AppData", "Solangelo", "Launcher");
            File path2 = new File(path + "/" + addpath);
            path2.mkdirs();
            File txt = new File(path2 + "/"+name);
            return txt;
        }
        return null;
    }

    public static void ReadFromVersionFile() {
        File txtFile = getFileByOS("data", "version", "txt");
        if (txtFile.exists()) {
            try {
                BufferedReader br = new BufferedReader(new FileReader(txtFile));

                launcher.blobVersion = br.readLine();
                launcher.miraculousVersion = br.readLine();
                launcher.tetrisVersion = br.readLine();

                br.close();

            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static void ReadFromSettingsFile() {
        File txtFile = getFileByOS("data", "settings", "txt");
        if (txtFile.exists()) {
            try {
                BufferedReader br = new BufferedReader(new FileReader(txtFile));

                launcher.closeOnOpen = Boolean.parseBoolean(br.readLine());

                br.close();

            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } else {
            WriteToSettingsFile();
        }
    }

    public static void WriteToVersionFile() {
        File txtFile = getFileByOS("data", "version", "txt");
        try {
            txtFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            PrintWriter pw = new PrintWriter(txtFile);
            pw.println(Launcher.blobVersion);
            pw.println(Launcher.miraculousVersion);
            pw.println(Launcher.tetrisVersion);
            pw.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void WriteToSettingsFile() {
        File txtFile = getFileByOS("data", "settings", "txt");
        try {
            txtFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            PrintWriter pw = new PrintWriter(txtFile);
            pw.write(String.valueOf(launcher.closeOnOpen));
            pw.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void deleteJarFile(String name) {
        File txtFile = getFileByOS("jars", name, "jar");
        if (txtFile.exists()) {
            txtFile.delete();
        }
    }
}
