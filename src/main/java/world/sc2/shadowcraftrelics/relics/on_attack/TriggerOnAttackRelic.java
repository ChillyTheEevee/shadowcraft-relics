package world.sc2.shadowcraftrelics.relics.on_attack;

import org.bukkit.event.entity.EntityDamageByEntityEvent;
import world.sc2.shadowcraftrelics.relics.Relic;

/**
 * An interface that represents a {@link Relic} with functionality that triggers when the relic is used in an
 * {@link EntityDamageByEntityEvent}. The definition of a Relic being "used" varies depending on the Relic and is
 * defined by the method shouldTriggerFromEntityDamageByEntityEvent(EntityDamageByEntityEvent event).
 */
public interface TriggerOnAttackRelic {

    /**
     * The check that must be run to decide whether a {@link TriggerOnAttackRelic}'s onAttack() method should be called.
     * This method is called every time an {@link EntityDamageByEntityEvent} is thrown, and it is up to each
     * implementation of {@code TriggerOnAttackRelic} to decide when onAttack() should be called.
     * @param event The instance of EntityDamageByEntityEvent
     * @return true if onAttack(EntityDamageByEntityEvent event) should be called.
     */
    boolean shouldTriggerFromEntityDamageByEntityEvent(EntityDamageByEntityEvent event);

    /**
     * The code to be run when a {@link TriggerOnAttackRelic} decides it should be triggered.
     * @param event The instance of EntityDamageByEntityEvent
     */
    void onAttack(EntityDamageByEntityEvent event);

}
