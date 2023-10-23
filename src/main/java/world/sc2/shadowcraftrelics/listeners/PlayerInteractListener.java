package world.sc2.shadowcraftrelics.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import world.sc2.shadowcraftrelics.managers.RelicManager;
import world.sc2.shadowcraftrelics.relics.Relic;
import world.sc2.shadowcraftrelics.relics.on_interact.TriggerOnInteractRelic;

public class PlayerInteractListener implements Listener {

    private final RelicManager relicManager;

    public PlayerInteractListener(RelicManager relicManager) {
        this.relicManager = relicManager;
    }
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {

        // Relics that implement TriggerOnInteractRelic
        ItemStack interactedRelic = event.getPlayer().getInventory().getItemInMainHand();
        for (Relic relic : relicManager.getRelicsMatchingFilter(r -> r instanceof TriggerOnInteractRelic)) {
            TriggerOnInteractRelic onInteractRelic = (TriggerOnInteractRelic) relic;
            if (relicManager.isRelic(interactedRelic, relic) && onInteractRelic.shouldTriggerFromPlayerInteractEvent(event)) {
                onInteractRelic.onInteract(event);
            }
        }

    }

}
