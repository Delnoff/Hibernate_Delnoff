package jm.task.core.jdbc.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public final class HelperUtil {
    private static final Properties PROPERTIES = new Properties();

    private HelperUtil() {
    }

    static {
        loadConnection();
    }

    private static void loadConnection() {
        try (FileInputStream files = new FileInputStream("src/resources/application.properties")) {
            PROPERTIES.load(files);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static String get(String key) {
        return PROPERTIES.getProperty(key);
    }
}
