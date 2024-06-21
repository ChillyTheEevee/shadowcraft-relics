package world.sc2.shadowcraftrelics.relics.on_death;

import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDeathEvent;
import world.sc2.shadowcraftrelics.relics.Relic;

/**
 * An interface that represents a {@link Relic} with functionality that triggers whenever a {@link LivingEntity} wearing
 * this {@link Relic} dies in a {@link EntityDeathEvent}
 */
public interface TriggerOnDeathRelic {

    /**
     * This method is called whenever a {@link LivingEntity} wearing this Relic dies in a {@link EntityDeathEvent}.
     * @param event The instance of {@link EntityDeathEvent} generated by the Entity's death
     */
    void onDeath(EntityDeathEvent event);

}