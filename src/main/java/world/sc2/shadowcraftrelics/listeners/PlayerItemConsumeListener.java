package world.sc2.shadowcraftrelics.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;
import world.sc2.shadowcraftrelics.managers.RelicManager;
import world.sc2.shadowcraftrelics.relics.Relic;
import world.sc2.shadowcraftrelics.relics.on_consume.TriggerOnConsumeRelic;

/**
 * A {@link Listener} for listening to the {@link PlayerItemConsumeEvent}.
 */
public class PlayerItemConsumeListener implements Listener {

    private final RelicManager relicManager;

    public PlayerItemConsumeListener(RelicManager relicManager) {
        this.relicManager = relicManager;
    }

    @EventHandler
    public void onPlayerItemConsume(PlayerItemConsumeEvent event) {

        // Relics that implement TriggerOnInteractRelic
        ItemStack itemInMainHand = event.getPlayer().getInventory().getItemInMainHand();

        Relic relic = relicManager.getRelicType(itemInMainHand);

        if (relic instanceof TriggerOnConsumeRelic triggerOnConsumeRelic) {
            triggerOnConsumeRelic.onConsume(event);
        }
    }

}
