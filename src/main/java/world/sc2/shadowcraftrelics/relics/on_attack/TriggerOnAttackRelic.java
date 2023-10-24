package world.sc2.shadowcraftrelics.relics.on_attack;

import org.bukkit.event.entity.EntityDamageByEntityEvent;
import world.sc2.shadowcraftrelics.relics.Relic;

/**
 * An interface that represents a {@link Relic} with functionality that triggers when the relic is directly used in an
 * {@link EntityDamageByEntityEvent}. A Relic is being "used" in an EntityDamageByEntityEvent if it is held within
 * the main hand slot of the attacker Entity when the event is declared.
 */
public interface TriggerOnAttackRelic {

    /**
     * This method is called whenever an Entity attacks another Entity with this TriggerOnAttackRelic
     * @param event The instance of {@link EntityDamageByEntityEvent} in which this Relic was used to attack.
     */
    void onAttack(EntityDamageByEntityEvent event);

}
