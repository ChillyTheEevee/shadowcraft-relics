package world.sc2.shadowcraftrelics.config;

import world.sc2.shadowcraftrelics.ShadowcraftRelics;

import java.util.HashMap;

//All credit to spigotmc.org user Bimmr for this manager
public class ConfigManager {

    private final ShadowcraftRelics plugin;
    private final HashMap<String, Config> configs = new HashMap<>();

    public ConfigManager(ShadowcraftRelics plugin) {
        this.plugin = plugin;
    }

    public HashMap<String, Config> getConfigs() {
        return configs;
    }

    public Config getConfig(String name) {
        if (!configs.containsKey(name))
            configs.put(name, new Config(plugin, name));

        return configs.get(name);
    }

    public Config saveConfig(String name) {
        return getConfig(name).save();
    }

    public Config reloadConfig(String name) {
        return getConfig(name).reload();
    }

}