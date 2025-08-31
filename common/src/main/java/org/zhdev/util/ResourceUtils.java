package org.zhdev.util;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Path;

public class ResourceUtils {
    public static InputStream getResource(String path, ClassLoader loader) {
        try {
            URL url = loader.getResource(path);
            if (url == null) {
                return null;
            }

            URLConnection connection = url.openConnection();
            connection.setUseCaches(false);
            return connection.getInputStream();
        } catch (IOException e) {
            return null;
        }
    }

    public static InputStream getResource(String path) {
        return getResource(path, ResourceUtils.class.getClassLoader());
    }

    public static boolean saveResource(InputStream inputStream, Path outPath) throws IOException {
        Path parent = outPath.getParent();
        if (parent != null && Files.notExists(parent)) {
            Files.createDirectories(parent);
        }

        if (Files.notExists(outPath) || Files.size(outPath) == 0) {
            OutputStream outputStream = Files.newOutputStream(outPath);
            byte[] buf = new byte[1024];
            int len;
            while ((len = inputStream.read(buf)) > 0) {
                outputStream.write(buf, 0, len);
            }
            outputStream.close();
            inputStream.close();
            return true;
        }

        return false;
    }

    public static boolean saveResource(String resourcePath, Path outPath, ClassLoader loader) throws IllegalStateException {
        if (resourcePath == null || resourcePath.isEmpty()) {
            return false;
        }

        resourcePath = resourcePath.replace('\\', '/');
        InputStream inputStream = getResource(resourcePath, loader);
        if (inputStream == null) {
            return false;
        }

        try {
            return saveResource(inputStream, outPath);
        } catch (IOException e) {
            throw new IllegalStateException("Could not save resource " + resourcePath + " to " + outPath, e);
        }
    }

    public static boolean saveResource(String resourcePath, Path outPath) throws IllegalStateException {
        return saveResource(resourcePath, outPath, ResourceUtils.class.getClassLoader());
    }
}
