package src;

import java.io.*;

public class Configuration implements Serializable {

    private final static String FILE = "conf";
    public static void init() {
        try {
            File file = new File(FILE);
            if (file.exists()) {
                FileInputStream fis = new FileInputStream(new File(FILE));
                ObjectInputStream ois = new ObjectInputStream(fis);
                configuration = (Configuration) ois.readObject();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    public static void save() {
        try {
            FileOutputStream fos = new FileOutputStream(FILE);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(configuration);
            System.out.println("тип сохранили");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Settings settings;

    private static Configuration configuration;

    private Configuration() {
        settings = new Settings();
    }

    public static Settings getSettings() {
        if (configuration == null) {
            configuration = new Configuration();
        }

        return configuration.settings;
    }

    public static void setSettings(Settings settings) {
        if (configuration == null) {
            configuration = new Configuration();
        }
        configuration.settings = settings;
        save();
    }

    //private String lastFolder;

    /*public static String getLastFolder() {
        if (configuration == null) {
            return null;
        } else {
            return configuration.lastFolder;
        }
    }

    public static void setLastFolder(String lastFolder) {
        if (configuration == null) {
            configuration = new Configuration();
        }
        configuration.lastFolder = lastFolder;
        save();
    }*/
}
