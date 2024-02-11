package world.sc2.shadowcraftrelics.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
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
        // Relics that implement TriggerOnInteractRelic
        ItemStack itemInMainHand = event.getPlayer().getInventory().getItemInMainHand();

        Relic relic = relicManager.getRelicType(itemInMainHand);

        if (relic instanceof TriggerOnInteractRelic triggerOnInteractRelic) {
            triggerOnInteractRelic.onInteract(event);
        }
    }

}
