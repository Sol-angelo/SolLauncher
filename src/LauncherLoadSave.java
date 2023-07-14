import java.io.*;
import java.nio.file.Path;

public class LauncherLoadSave {
    public static int state;
    public static int fileAmount;

    private static Launcher launcher;

    public LauncherLoadSave(Launcher launcher) {
        this.launcher = launcher;
    }

    public static void readFromVersionFile() {
        File txtFile = Util.getFileByOS("data", "version", "txt");
        File eFile = Util.getEncryptedByOS("data", "version");
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
        File txtFile = Util.getFileByOS("data", "settings", "txt");
        File eFile = Util.getEncryptedByOS("data", "settings");
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
        File txtFile = Util.getFileByOS("data", "version", "txt");
        File eFile = Util.getEncryptedByOS("data", "version");
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
        File txtFile = Util.getFileByOS("data", "settings", "txt");
        File eFile = Util.getEncryptedByOS("data", "settings");
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
        File txtFile = Util.getFileByOS("jars", name, "jar");
        if (txtFile.exists()) {
            txtFile.delete();
        }
    }
}
