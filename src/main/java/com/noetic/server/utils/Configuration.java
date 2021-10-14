package com.noetic.server.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Properties;

public class Configuration {
    private static final String FILE_RESOURCE = "config.cfg";

    public static String worldServerPort;
    public static String authServerPort;
    public static String accountDataPath;

    public static void load() {
        Properties properties = new Properties();
        InputStream istream = null;

        try {
            istream = new FileInputStream(getResourceAsFile(FILE_RESOURCE));

            properties.load(istream);

            worldServerPort = String.valueOf(properties.getProperty("world_server_port"));
            authServerPort = String.valueOf(properties.getProperty("auth_server_port"));
            accountDataPath = String.valueOf(properties.getProperty("account_data_path"));


            istream.close();

        } catch (IOException ioe) {
            ioe.printStackTrace();
            System.exit(1);
        }
    }

    private static File getResourceAsFile(String resource) {
        File file = null;
        try {
            file = loadFile(resource);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        return file;
    }

    private static File loadFile(String filePath) throws URISyntaxException {
        ClassLoader classLoader = Configuration.class.getClassLoader();
        URL resource = classLoader.getResource(filePath);
        if (resource == null) {
            throw new IllegalArgumentException("File not found" + filePath);
        } else {
            return new File(resource.toURI());
        }
    }
}
