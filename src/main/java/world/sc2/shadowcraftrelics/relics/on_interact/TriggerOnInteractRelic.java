package world.sc2.shadowcraftrelics.relics.on_interact;

import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import world.sc2.shadowcraftrelics.relics.Relic;

/**
 * An interface that represents a {@link Relic} with functionality that triggers when the relic is used in an
 * {@link PlayerInteractEvent}. The definition of a Relic being "used" varies depending on the Relic and is
 * defined by the method shouldTriggerFromPlayerInteractEvent(PlayerInteractEvent event).
 */
public interface TriggerOnInteractRelic {

    /**
     * The check that must be run to decide whether a TriggerOnInteractRelic's onInteract() method should be
     * called. This method is called when a {@link PlayerInteractEvent} is thrown and the item that the attacker is
     * holding is an instance of the specified TriggerOnInteractRelic. Past that, it is up to each
     * implementation of {@code TriggerOnAttackRelic} to decide when onInteract() should be called.
     * @param event The instance of PlayerInteractEvent
     * @return true if onInteract(PlayerInteractEvent event) should be called.
     */
    boolean shouldTriggerFromPlayerInteractEvent(PlayerInteractEvent event);

    /**
     * The code to be run when a TriggerOnInteractRelic decides it should be triggered.
     * @param event The instance of EntityDamageByEntityEvent
     */
    void onInteract(PlayerInteractEvent event);

}
