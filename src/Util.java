import net.htmlparser.jericho.Source;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class Util {

    private static void unzip(String zipFilePath, String destDir) {
        File dir = new File(destDir);
        // create output directory if it doesn't exist
        if(!dir.exists()) dir.mkdirs();
        FileInputStream fis;
        //buffer for read and write data to file
        byte[] buffer = new byte[1024];
        try {
            fis = new FileInputStream(zipFilePath);
            ZipInputStream zis = new ZipInputStream(fis);
            ZipEntry ze = zis.getNextEntry();
            while(ze != null){
                String fileName = ze.getName();
                File newFile = new File(destDir + File.separator + fileName);
                System.out.println("Unzipping to "+newFile.getAbsolutePath());
                //create directories for sub directories in zip
                new File(newFile.getParent()).mkdirs();
                FileOutputStream fos = new FileOutputStream(newFile);
                int len;
                while ((len = zis.read(buffer)) > 0) {
                    fos.write(buffer, 0, len);
                }
                fos.close();
                //close this ZipEntry
                zis.closeEntry();
                ze = zis.getNextEntry();
            }
            //close last ZipEntry
            zis.closeEntry();
            zis.close();
            fis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void download(String name, String version) {
        try {
            URL url = new URL("https://github.com/Sol-angelo/" + name + "/releases/download/v" + version + "/" + name.toLowerCase() + ".jar");
            File file = LauncherLoadSave.getFileByOS("jars", name.toLowerCase(), "jar");
            copyURLToFile(url, file);
            /*File metaINF = LauncherLoadSave.getFolderByOS("jars", "META-INF");
            if (metaINF.exists() &&  metaINF.isDirectory()) {
                for (File file1 : metaINF.listFiles()) {
                    file1.delete();
                }
                metaINF.delete();
            }
            unzip(file.getPath(), file.getParentFile().toString());
            file.delete();*/
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    public static void checkVersion() {
        String version = new Source(getTextFromGithub("https://raw.githubusercontent.com/Sol-angelo/SolLauncher/master/version.txt")).getRenderer().toString();
        String[] versionsep = version.split(" ");
        System.out.println(version);
        System.out.println(Arrays.toString(versionsep));
        if (!Objects.equals(versionsep[0], Launcher.blobVersion)) {
            LauncherLoadSave.deleteJarFile("blob");
            Launcher.blobVersion = versionsep[0];
            LauncherLoadSave.WriteToVersionFile();
            download("Blob", versionsep[0]);
        }
        if (!Objects.equals(versionsep[1], Launcher.miraculousVersion)) {
            LauncherLoadSave.deleteJarFile("miraculous");
            Launcher.miraculousVersion = versionsep[1];
            LauncherLoadSave.WriteToVersionFile();
            download("Miraculous", versionsep[1]);
        }
        if (!Objects.equals(versionsep[2], Launcher.tetrisVersion)) {
            LauncherLoadSave.deleteJarFile("tetris");
            Launcher.tetrisVersion = versionsep[2];
            LauncherLoadSave.WriteToVersionFile();
            download("Tetris", versionsep[2]);
        }
    }

    public static String getLatestVersion(String name) {
        String version = new Source(getTextFromGithub("https://raw.githubusercontent.com/Sol-angelo/SolLauncher/master/version.txt")).getRenderer().toString();
        String[] versionsep = version.split(" ");
        if (name.equalsIgnoreCase("blob")) {
            return versionsep[0];
        } else if (name.equalsIgnoreCase("miraculous")) {
            return versionsep[1];
        } else if (name.equalsIgnoreCase("tetris")) {
            return versionsep[2];
        }
        return null;
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

    public static void exec(File file) throws IOException {
        String javaHome = System.getProperty("java.home");
        String javaBin = javaHome +
                File.separator + "bin" +
                File.separator + "java";
        ProcessBuilder pb = new ProcessBuilder(javaBin, "-jar", file.toString());
        pb.start();
    }
}
