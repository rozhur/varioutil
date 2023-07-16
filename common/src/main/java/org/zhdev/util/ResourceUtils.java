package org.zhdev.util;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;

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

    public static boolean saveResource(String resourcePath, File outFile, ClassLoader loader) throws IllegalStateException {
        if (resourcePath == null || resourcePath.equals("")) {
            return false;
        }

        resourcePath = resourcePath.replace('\\', '/');
        InputStream in = getResource(resourcePath, loader);
        if (in == null) {
            return false;
        }

        File parent = outFile.getParentFile();
        if (parent != null && !parent.exists()) {
            parent.mkdirs();
        }

        try {
            if (!outFile.exists() || outFile.length() == 0) {
                OutputStream stream = Files.newOutputStream(outFile.toPath());
                byte[] buf = new byte[1024];
                int len;
                while ((len = in.read(buf)) > 0) {
                    stream.write(buf, 0, len);
                }
                stream.close();
                in.close();
                return true;
            }
        } catch (IOException e) {
            throw new IllegalStateException("Could not save resource " + resourcePath + " to " + outFile, e);
        }

        return false;
    }


    public static boolean saveResource(String resourcePath, File outFile) throws IllegalStateException {
        return saveResource(resourcePath, outFile);
    }
}
