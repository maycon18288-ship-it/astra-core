package astra.core.utils;

import net.md_5.bungee.api.plugin.Plugin;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class FileUtils {
    public static void saveResource(Plugin plugin, String resourcePath, File outFile) {
        try (InputStream in = plugin.getResourceAsStream(resourcePath)) {
            if (in == null) {
                throw new IllegalArgumentException("O recurso '" + resourcePath + "' não foi encontrado no plugin!");
            }

            if (!outFile.exists()) {
                outFile.createNewFile();
            }

            try (OutputStream out = new FileOutputStream(outFile)) {
                byte[] buf = new byte[1024];
                int len;
                while ((len = in.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
} 