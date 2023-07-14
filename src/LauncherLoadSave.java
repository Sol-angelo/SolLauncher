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

    public static File getEncryptedByOS(String addpath, String name) {
        String osname = System.getProperty("os.name");
        if (osname.contains("Mac")) {
            Path path = Path.of(System.getProperty("user.home"), "Library", "Application Support", "Solangelo", "Launcher");
            File path2 = new File(path + "/" + addpath);
            path2.mkdirs();
            File txt = new File(path2 + "/"+name+".sexinthecorridor");
            return txt;
        } else if (osname.contains("Window")) {
            Path path = Path.of(System.getProperty("user.home"), "AppData", "Solangelo", "Launcher");
            File path2 = new File(path + "/" + addpath);
            path2.mkdirs();
            File txt = new File(path2 + "/"+name+".sexinthecorridor");
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

    public static void readFromVersionFile() {
        File txtFile = getFileByOS("data", "version", "txt");
        File eFile = getEncryptedByOS("data", "version");
        if (eFile.exists()) {
            try {
                Util.decrypt(Util.key, eFile, txtFile);
                BufferedReader br = new BufferedReader(new FileReader(txtFile));
                launcher.blobVersion = br.readLine();
                launcher.miraculousVersion = br.readLine();
                launcher.tetrisVersion = br.readLine();
                br.close();
                Util.encrypt(Util.key, txtFile, eFile);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } else {
            writeToVersionFile();
        }
    }

    public static void readFromSettingsFile() {
        File txtFile = getFileByOS("data", "settings", "txt");
        File eFile = getEncryptedByOS("data", "settings");
        if (eFile.exists()) {
            try {
                Util.decrypt(Util.key, eFile, txtFile);
                BufferedReader br = new BufferedReader(new FileReader(txtFile));

                launcher.closeOnOpen = Boolean.parseBoolean(br.readLine());

                br.close();
                Util.encrypt(Util.key, txtFile, eFile);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } else {
            writeToSettingsFile();
        }
    }

    public static void writeToVersionFile() {
        File txtFile = getFileByOS("data", "version", "txt");
        File eFile = getEncryptedByOS("data", "version");
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
            Util.encrypt(Util.key, txtFile, eFile);
        } catch (FileNotFoundException | CryptoException e) {
            e.printStackTrace();
        }
    }

    public static void writeToSettingsFile() {
        File txtFile = getFileByOS("data", "settings", "txt");
        File eFile = getEncryptedByOS("data", "settings");
        try {
            txtFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            PrintWriter pw = new PrintWriter(txtFile);
            pw.write(String.valueOf(launcher.closeOnOpen));
            pw.close();
            Util.encrypt(Util.key, txtFile, eFile);
        } catch (FileNotFoundException | CryptoException e) {
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
