package world.sc2.shadowcraftrelics;

import org.bukkit.plugin.java.JavaPlugin;
import world.sc2.shadowcraftrelics.commands.GiveRelicTagCommand;
import world.sc2.shadowcraftrelics.commands.SetPurgerStatesCommand;
import world.sc2.shadowcraftrelics.config.ConfigManager;
import world.sc2.shadowcraftrelics.config.ConfigUpdater;
import world.sc2.shadowcraftrelics.listeners.EntityDamageListener;
import world.sc2.shadowcraftrelics.listeners.PlayerHitGroundListener;
import world.sc2.shadowcraftrelics.listeners.PlayerInteractListener;
import world.sc2.shadowcraftrelics.listeners.PlayerMoveListener;
import world.sc2.shadowcraftrelics.managers.RelicManager;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

public final class ShadowcraftRelics extends JavaPlugin {

    private static ShadowcraftRelics plugin = null;

    // Managers
    private ConfigManager configManager;
    private RelicManager relicManager;

    @Override
    public void onEnable() {
        // Plugin startup logic
        plugin = this;

        // Create managers
        configManager = new ConfigManager(plugin);
        relicManager = new RelicManager(plugin, configManager);

        // Setup and update configs
        saveAndUpdateConfig("relicProperties/simonObliterator.yml");
        saveAndUpdateConfig("relicProperties/purger.yml");
        saveAndUpdateConfig("relicProperties/voidwalkers.yml");

        // Register listeners
        getServer().getPluginManager().registerEvents(new EntityDamageListener(relicManager), this);
        getServer().getPluginManager().registerEvents(new PlayerInteractListener(relicManager), this);
        getServer().getPluginManager().registerEvents(new PlayerMoveListener(relicManager), this);
        getServer().getPluginManager().registerEvents(new PlayerHitGroundListener(relicManager), this);

        // Register commands
        Objects.requireNonNull(getCommand("giverelictags")).setExecutor(new GiveRelicTagCommand(relicManager));
        Objects.requireNonNull(getCommand("setpurgerstates")).setExecutor(new SetPurgerStatesCommand(this));
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
            configManager.saveConfig(name);
        }
    }

    /**
     * Updates a config by comparing in the plugin's assigned data folder by copying over data from the jar file
     * @param name The path to the config from the plugin's assigned data file
     */
    private void updateConfig(String name) {
        File configFile = new File(getDataFolder(), name);
        try {
            ConfigUpdater.update(plugin, name, configFile, new ArrayList<>());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
