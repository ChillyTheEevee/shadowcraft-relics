package world.sc2.shadowcraftrelics.relics.on_move;

import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitScheduler;
import world.sc2.shadowcraftrelics.config.Config;
import world.sc2.shadowcraftrelics.events.PlayerHitGroundEvent;
import world.sc2.shadowcraftrelics.nbt.NBTTag;
import world.sc2.shadowcraftrelics.relics.Relic;
import world.sc2.shadowcraftrelics.relics.onHitGround.TriggerOnEntityHitGroundRelic;

public class Voidwalkers extends Relic implements TriggerOnMoveRelic, TriggerOnEntityHitGroundRelic {

    // System
    private final Plugin plugin;
    private final NBTTag voidwalkersTriggeredTag;

    // Properties
    private int yLevelTrigger;
    private int levitationTime;
    private PotionEffect voidSavingLevitationEffect;
    private boolean slowFallingEnabled;
    private PotionEffect postLevitationSlowFallingEffect;

    // Constants
    private final int TICKS_PER_SECOND = 20;

    public Voidwalkers(String name, Config config, Plugin plugin) {
        super(name, config);
        this.plugin = plugin;

        // Create NBT Tags
        var voidwalkersTriggeredKey = new NamespacedKey(plugin, "voidwalkersTriggered");
        voidwalkersTriggeredTag = new NBTTag(voidwalkersTriggeredKey, PersistentDataType.INTEGER, 0);


        // Get data from Config
        YamlConfiguration configInstance = config.get();

        yLevelTrigger = configInstance.getInt("uniqueProperties.triggersAtYLevel");

        // Get the levitation effect from Config
        levitationTime = configInstance.getInt("uniqueProperties.levitationTime") * TICKS_PER_SECOND;
        int levitationLevel = configInstance.getInt("uniqueProperties.levitationLevel");
        voidSavingLevitationEffect = new PotionEffect(PotionEffectType.LEVITATION, levitationTime,
                levitationLevel-1);

        // Get slow falling form Config
        slowFallingEnabled = configInstance.getBoolean("uniqueProperties.enableSlowFallingAfterLevitation");

        if (slowFallingEnabled) {
            // TODO update this slow fall effect to use infinite duration once we update to 1.20
            int slowFallingTime = configInstance.getInt("uniqueProperties.slowFallingTime") * TICKS_PER_SECOND;
            int slowFallingLevel = configInstance.getInt("uniqueProperties.slowFallingLevel");
            postLevitationSlowFallingEffect = new PotionEffect(PotionEffectType.SLOW_FALLING, slowFallingTime,
                    slowFallingLevel-1);
        }

    }

    @Override
    public void onMove(PlayerMoveEvent event) {

        Player player = event.getPlayer();

        if(player.getWorld().getEnvironment() == World.Environment.THE_END && event.getTo().getY() <= yLevelTrigger) {

            PersistentDataContainer playerPersistentDataContainer = player.getPersistentDataContainer();

            NamespacedKey voidwalkersTriggeredKey = voidwalkersTriggeredTag.getNamespacedKey();
            PersistentDataType voidwalkersTriggeredTagDataType = voidwalkersTriggeredTag.getPersistentDataType();

            if (!playerPersistentDataContainer.has(voidwalkersTriggeredKey, voidwalkersTriggeredTagDataType)) {
                Bukkit.getLogger().info("Entity does not have voidwalkersTriggeredKey, giving them one");
                voidwalkersTriggeredTag.applyTag(player, 0);
            }

            // Get whether voidWalkers have already been triggered
            Integer voidWalkersTriggeredValue = playerPersistentDataContainer.get(voidwalkersTriggeredKey,
                    PersistentDataType.INTEGER);
            if (voidWalkersTriggeredValue == null) {
                return;
            }

            boolean alreadyTriggered = voidWalkersTriggeredValue == 1;

            // Adds levitation to Player and schedules slow falling if enabled
            if (!alreadyTriggered) {
                player.addPotionEffect(voidSavingLevitationEffect);

                if (slowFallingEnabled) {
                    BukkitScheduler scheduler = Bukkit.getScheduler();
                    scheduler.runTaskLater(plugin, () -> {
                        player.addPotionEffect(postLevitationSlowFallingEffect);
                    }, levitationTime);
                }

                voidwalkersTriggeredTag.applyTag(player, 1);
            }
        }

    }

    @Override
    public void onHitGround(PlayerHitGroundEvent event) {

        Player player = event.getPlayer();
        PersistentDataContainer playerPDC = player.getPersistentDataContainer();

        NamespacedKey voidwalkersTriggeredKey = voidwalkersTriggeredTag.getNamespacedKey();
        PersistentDataType voidwalkersTriggeredTagDataType = voidwalkersTriggeredTag.getPersistentDataType();

        if (playerPDC.has(voidwalkersTriggeredKey, voidwalkersTriggeredTagDataType)) {
            voidwalkersTriggeredTag.applyTag(player, 0);
        }

    }

}
