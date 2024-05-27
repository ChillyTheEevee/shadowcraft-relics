package world.sc2.shadowcraftrelics.listeners;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import world.sc2.shadowcraftrelics.managers.RelicManager;
import world.sc2.shadowcraftrelics.relics.Relic;
import world.sc2.shadowcraftrelics.relics.on_interact.TriggerOnInteractRelic;

/**
 * A {@link Listener} for listening to the {@link PlayerInteractEvent}.
 */
public class PlayerInteractListener implements Listener {

    private final RelicManager relicManager;

    public PlayerInteractListener(RelicManager relicManager) {
        this.relicManager = relicManager;
    }
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.getHand() == EquipmentSlot.HAND) {
            // Relics that implement TriggerOnInteractRelic
            Bukkit.getLogger().info("PlayerInteractEvent called for main hand."); // todo remove debugging code
            Bukkit.getLogger().info("Action: " + event.getAction());
            Bukkit.getLogger().info("Player: " + event.getPlayer());
            Bukkit.getLogger().info("EventName: " + event.getEventName());
            Bukkit.getLogger().info("Material: " + event.getMaterial()); // todo remove debugging code
            ItemStack itemInMainHand = event.getPlayer().getInventory().getItemInMainHand();
            Relic relic = relicManager.getRelicType(itemInMainHand);
            if (relic instanceof TriggerOnInteractRelic triggerOnInteractRelic) {
                triggerOnInteractRelic.onInteract(event);
            }
        }
    }

}
