package astra.core.plugin.config;

import astra.core.plugin.AuroraPlugin;

import java.io.*;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;

public class FileUtils {

  private AuroraPlugin plugin;

  public FileUtils(AuroraPlugin plugin) {
    this.plugin = plugin;
  }

  public void deleteFile(File file) {
    if (!file.exists()) {
      return;
    }

    if (file.isDirectory()) {
      Arrays.stream(file.listFiles()).forEach(this::deleteFile);
    }

    file.delete();
  }

  public void copyFiles(File in, File out, String... ignore) {
    List<String> list = Arrays.asList(ignore);
    if (in.isDirectory()) {
      if (!out.exists()) {
        out.mkdirs();
      }

      for (File file : in.listFiles()) {
        if (list.contains(file.getName())) {
          continue;
        }

        copyFiles(file, new File(out, file.getName()));
      }
    } else {
      try {
        copyFile(new FileInputStream(in), out);
      } catch (IOException ex) {
        this.plugin.getLogger().log(Level.WARNING, "Um erro inesperado ocorreu ao copiar o arquivo \"" + out.getName() + "\": ", ex);
      }
    }
  }

  public void copyFile(InputStream input, File out) {
    FileOutputStream ou = null;
    try {
      ou = new FileOutputStream(out);
      byte[] buff = new byte[1024];
      int len;
      while ((len = input.read(buff)) > 0) {
        ou.write(buff, 0, len);
      }
    } catch (IOException ex) {
      this.plugin.getLogger().log(Level.WARNING, "Um erro inesperado ocorreu ao copiar o arquivo \"" + out.getName() + "\": ", ex);
    } finally {
      try {
        if (ou != null) {
          ou.close();
        }
        if (input != null) {
          input.close();
        }
      } catch (IOException ignored) {}
    }
  }
}