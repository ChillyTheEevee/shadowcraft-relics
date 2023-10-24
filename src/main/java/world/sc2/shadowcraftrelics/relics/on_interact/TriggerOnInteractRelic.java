package world.sc2.shadowcraftrelics.relics.on_interact;

import org.bukkit.event.player.PlayerInteractEvent;
import world.sc2.shadowcraftrelics.relics.Relic;

/**
 * An interface that represents a {@link Relic} with functionality that triggers when the relic is used in an
 * {@link PlayerInteractEvent}. A Relic is being "used" in an EntityDamageByEntityEvent if it is held within
 *  the main hand of a Player during a PlayerInteractEvent
 */
public interface TriggerOnInteractRelic {

    /**
     * This method is called whenever it is determined that this {@link Relic} was interacted with.
     * @param event The instance of EntityDamageByEntityEvent in which this Relic was interacted with.
     */
    void onInteract(PlayerInteractEvent event);

}
