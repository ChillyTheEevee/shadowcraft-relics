package world.sc2.shadowcraftrelics;

import org.bukkit.plugin.java.JavaPlugin;
import world.sc2.command.subcommand.SubcommandManager;
import world.sc2.config.ConfigManager;
import world.sc2.shadowcraftrelics.commands.CreateMorphableRelicSubcommand;
import world.sc2.shadowcraftrelics.commands.GiveRelicTagSubcommand;
import world.sc2.shadowcraftrelics.listeners.*;
import world.sc2.shadowcraftrelics.listeners.custom.PlayerHitGroundListener;
import world.sc2.shadowcraftrelics.listeners.custom.RelicMorphListener;
import world.sc2.shadowcraftrelics.managers.RelicManager;

/**
 * The main class of the ShadowcraftRelics plugin.
 * @author ChillyTheEevee
 */
public final class ShadowcraftRelics extends JavaPlugin {

    // Managers
    private ConfigManager configManager;
    private SubcommandManager commandManager;
    private RelicManager relicManager;

    @Override
    public void onEnable() {
        // Plugin startup logic

        // Create managers
        configManager = new ConfigManager(this);
        commandManager = new SubcommandManager(this, configManager);
        relicManager = new RelicManager(this, configManager);


        // Register listeners
        getServer().getPluginManager().registerEvents(new EntityDamageListener(relicManager), this);
        getServer().getPluginManager().registerEvents(new PlayerInteractListener(relicManager), this);
        getServer().getPluginManager().registerEvents(new PlayerMoveListener(relicManager), this);
        getServer().getPluginManager().registerEvents(new PlayerItemConsumeListener(relicManager), this);
        getServer().getPluginManager().registerEvents(new EntityDeathListener(relicManager), this);

        // Register custom listeners
        getServer().getPluginManager().registerEvents(new PlayerHitGroundListener(relicManager), this);
        getServer().getPluginManager().registerEvents(new RelicMorphListener(relicManager), this);

        // Register commands
        commandManager.registerSubcommand("giverelictag",
                new GiveRelicTagSubcommand(configManager.getConfig("commands/giverelictag.yml"), relicManager));
        commandManager.registerSubcommand("createmorphablerelicchain",
                new CreateMorphableRelicSubcommand(
                        configManager.getConfig("commands/createmorphablerelicchain.yml"), this));

        // Save configs
        configManager.saveConfigs();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic

        // Save configs
        configManager.saveConfigs();
    }

}
