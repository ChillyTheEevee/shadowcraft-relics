package world.sc2.shadowcraftrelics.listeners;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.EntityEquipment;
import world.sc2.shadowcraftrelics.events.PlayerHitGroundEvent;
import world.sc2.shadowcraftrelics.managers.RelicManager;
import world.sc2.shadowcraftrelics.relics.on_move.TriggerOnMoveRelic;

/**
 * A {@link Listener} for listening to the {@link PlayerMoveEvent}.
 */
public class PlayerMoveListener implements Listener {

    private final RelicManager relicManager;

    public PlayerMoveListener(RelicManager relicManager) {
        this.relicManager = relicManager;
    }

    @EventHandler
    public void onEntityMove(PlayerMoveEvent event) {

        Player player = event.getPlayer();

        if (playerHitGround(event)) {
            Bukkit.getPluginManager().callEvent(new PlayerHitGroundEvent(player));
        }

        EntityEquipment playerEquipment = player.getEquipment();

        if (relicManager.getRelicType(playerEquipment.getHelmet())
                instanceof TriggerOnMoveRelic relicHelmet) {
            relicHelmet.onMove(event);
        }

        if (relicManager.getRelicType(playerEquipment.getChestplate())
                instanceof TriggerOnMoveRelic relicChestplate) {
            relicChestplate.onMove(event);
        }

        if (relicManager.getRelicType(playerEquipment.getLeggings())
                instanceof TriggerOnMoveRelic relicLeggings) {
            relicLeggings.onMove(event);
        }

        if (relicManager.getRelicType(playerEquipment.getBoots())
                instanceof TriggerOnMoveRelic relicBoots) {
            relicBoots.onMove(event);
        }

    }

    /**
     * @param event the {@link PlayerMoveEvent} to check
     * @return true if the {@link Player} just hit the ground
     * @todo Make this method not rely on a deprecated method, make method not return true multiple ticks in a row
     */
    private boolean playerHitGround(PlayerMoveEvent event) {
        return event.getFrom().getY() > event.getTo().getY() && event.getPlayer().isOnGround();
    }

}
