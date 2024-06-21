package world.sc2.shadowcraftrelics.listeners.custom;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.inventory.EntityEquipment;
import world.sc2.shadowcraftrelics.events.PlayerHitGroundEvent;
import world.sc2.shadowcraftrelics.managers.RelicManager;
import world.sc2.shadowcraftrelics.relics.on_hit_ground.TriggerOnEntityHitGroundRelic;

/**
 * A {@link Listener} for listening to the custom {@link PlayerHitGroundEvent}.
 */
public class PlayerHitGroundListener implements Listener {

    private final RelicManager relicManager;

    public PlayerHitGroundListener(RelicManager relicManager) {
        this.relicManager = relicManager;
    }

    @EventHandler (priority = EventPriority.MONITOR)
    public void onEntityHitGroundEvent(PlayerHitGroundEvent event) {

        EntityEquipment playerEquipment = event.getPlayer().getEquipment();

        if (relicManager.getRelicType(playerEquipment.getHelmet())
                instanceof TriggerOnEntityHitGroundRelic relicHelmet) {
            relicHelmet.onHitGround(event);
        }

        if (relicManager.getRelicType(playerEquipment.getChestplate())
                instanceof TriggerOnEntityHitGroundRelic relicChestplate) {
            relicChestplate.onHitGround(event);
        }

        if (relicManager.getRelicType(playerEquipment.getLeggings())
                instanceof TriggerOnEntityHitGroundRelic relicLeggings) {
            relicLeggings.onHitGround(event);
        }

        if (relicManager.getRelicType(playerEquipment.getBoots())
                instanceof TriggerOnEntityHitGroundRelic relicBoots) {
            relicBoots.onHitGround(event);
        }
    }

}
