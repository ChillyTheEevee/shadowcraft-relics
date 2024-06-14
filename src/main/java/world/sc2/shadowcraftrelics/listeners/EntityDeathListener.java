package world.sc2.shadowcraftrelics.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.EntityEquipment;
import world.sc2.shadowcraftrelics.managers.RelicManager;
import world.sc2.shadowcraftrelics.relics.on_death.TriggerOnDeathRelic;

/**
 * A {@link Listener} for listening to the {@link EntityDeathEvent}
 */
public class EntityDeathListener implements Listener {

    private final RelicManager relicManager;

    public EntityDeathListener(RelicManager relicManager) {
        this.relicManager = relicManager;
    }
    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        EntityEquipment playerEquipment = event.getEntity().getEquipment();
        if (playerEquipment == null) {
            return;
        }

        if (relicManager.getRelicType(playerEquipment.getHelmet()) instanceof TriggerOnDeathRelic relicHelmet) {
            relicHelmet.onDeath(event);
        }
        if (relicManager.getRelicType(playerEquipment.getChestplate()) instanceof TriggerOnDeathRelic relicChestplate) {
            relicChestplate.onDeath(event);
        }
        if (relicManager.getRelicType(playerEquipment.getLeggings()) instanceof TriggerOnDeathRelic relicLeggings) {
            relicLeggings.onDeath(event);
        }
        if (relicManager.getRelicType(playerEquipment.getBoots()) instanceof TriggerOnDeathRelic relicBoots) {
            relicBoots.onDeath(event);
        }
    }

}