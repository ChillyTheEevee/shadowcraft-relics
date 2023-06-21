package world.sc2.shadowcraftrelics;

import org.bukkit.plugin.java.JavaPlugin;
import world.sc2.shadowcraftrelics.config.ConfigManager;
import world.sc2.shadowcraftrelics.config.ConfigUpdater;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public final class ShadowcraftRelics extends JavaPlugin {

    private static ShadowcraftRelics plugin = null;

    @Override
    public void onEnable() {
        // Plugin startup logic
        plugin = this;
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    // Config

    private void saveAndUpdateConfig(String config){
        saveConfig(config);
        updateConfig(config);
    }

    /**
     * If a config doesn't exist in the specified data folder, then the config is copied over from the jar file
     * @param name The path to the config from the plugin's assigned data file
     */
    public void saveConfig(String name){
        File config = new File(this.getDataFolder(), name);
        if (!config.exists()){
            this.saveResource(name, false);
            ConfigManager.getInstance().saveConfig(name);
        }
    }

    /**
     * Updates a config by comparing in the plugin's assigned data folder by copying over data from the jar file
     * @param name The path to the config from the plugin's assigned data file
     */
    private void updateConfig(String name){
        File configFile = new File(getDataFolder(), name);
        try {
            ConfigUpdater.update(plugin, name, configFile, new ArrayList<>());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Getters

    public static ShadowcraftRelics getPlugin() {
        return plugin;
    }
}
