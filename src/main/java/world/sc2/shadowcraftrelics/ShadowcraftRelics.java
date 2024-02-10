package world.sc2.shadowcraftrelics;

import org.bukkit.plugin.java.JavaPlugin;
import world.sc2.command.CommandManager;
import world.sc2.config.ConfigManager;
import world.sc2.shadowcraftrelics.commands.GiveRelicTagCommand;
import world.sc2.shadowcraftrelics.commands.SetPurgerStatesCommand;
import world.sc2.shadowcraftrelics.listeners.EntityDamageListener;
import world.sc2.shadowcraftrelics.listeners.PlayerHitGroundListener;
import world.sc2.shadowcraftrelics.listeners.PlayerInteractListener;
import world.sc2.shadowcraftrelics.listeners.PlayerMoveListener;
import world.sc2.shadowcraftrelics.managers.RelicManager;

public final class ShadowcraftRelics extends JavaPlugin {

    // Managers
    private ConfigManager configManager;
    private CommandManager commandManager;
    private RelicManager relicManager;

    @Override
    public void onEnable() {
        // Plugin startup logic

        // Create managers
        configManager = new ConfigManager(this);
        commandManager = new CommandManager(this, configManager);
        relicManager = new RelicManager(this, configManager);


        // Register listeners
        getServer().getPluginManager().registerEvents(new EntityDamageListener(relicManager), this);
        getServer().getPluginManager().registerEvents(new PlayerInteractListener(relicManager), this);
        getServer().getPluginManager().registerEvents(new PlayerMoveListener(relicManager), this);
        getServer().getPluginManager().registerEvents(new PlayerHitGroundListener(relicManager), this);

        // Register commands
        commandManager.addCommand("giverelictag",
                new GiveRelicTagCommand(configManager.getConfig("commands/giverelictag.yml"), relicManager));
        commandManager.addCommand("setpurgerstates",
                new SetPurgerStatesCommand(configManager.getConfig("commands/setpurgerstates.yml"), this));

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
