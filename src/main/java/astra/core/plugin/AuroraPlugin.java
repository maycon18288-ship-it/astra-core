package astra.core.plugin;

import astra.core.plugin.config.FileUtils;
import astra.core.plugin.config.MWriter;
import astra.core.reflection.Accessors;
import astra.core.reflection.acessors.FieldAccessor;
import lombok.Getter;
import org.bukkit.plugin.PluginLogger;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

@Getter
public abstract class AuroraPlugin extends JavaPlugin {

    private static final FieldAccessor<PluginLogger> LOGGER_ACCESSOR = Accessors.getField(JavaPlugin.class, "logger", PluginLogger.class);
    private final FileUtils fileUtils;

    public AuroraPlugin() {
        this.fileUtils = new FileUtils(this);
        LOGGER_ACCESSOR.set(this, new AuroraLogger(this));

        this.start();
    }

    public abstract void start();

    public abstract void load();

    public abstract void enable();

    public abstract void disable();

    @Override
    public void onLoad() {
        this.load();
    }

    @Override
    public void onEnable() {
        this.enable();
    }

    @Override
    public void onDisable() {
        this.disable();
    }

    public AuroraConfig getConfig(String name) {
        return this.getConfig("", name);
    }

    public AuroraConfig getConfig(String path, String name) {
        return AuroraConfig.getConfig(this, "plugins/" + this.getName() + "/" + path, name);
    }

    public MWriter getWriter(File file) {
        return this.getWriter(file, "");
    }

    public MWriter getWriter(File file, String header) {
        return new MWriter((AuroraLogger) this.getLogger(), file, header);
    }

}