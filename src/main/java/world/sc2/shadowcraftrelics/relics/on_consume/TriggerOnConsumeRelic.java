package world.sc2.shadowcraftrelics.relics.on_consume;

import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import world.sc2.shadowcraftrelics.relics.Relic;

/**
 * An interface that represents a {@link Relic} with functionality that triggers when the relic is consumed in a
 * {@link PlayerItemConsumeEvent}.
 */
public interface TriggerOnConsumeRelic {

    /**
     * This method is called whenever a Player consumes this relic in a {@link PlayerItemConsumeEvent}.
     * @param event The instance of {@link EntityDamageByEntityEvent} in which this Relic was used to attack.
     */
    void onConsume(PlayerItemConsumeEvent event);

}
