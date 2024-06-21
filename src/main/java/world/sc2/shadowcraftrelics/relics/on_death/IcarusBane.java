package world.sc2.shadowcraftrelics.relics.on_death;

import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;
import live.chillytheeevee.chillylib.config.Config;
import world.sc2.shadowcraftrelics.relics.Relic;

import java.util.Objects;

/**
 * A wearable {@link Relic} that has the special property of dropping an item when a player dies to fall damage wearing
 * it.
 */
public class IcarusBane extends Relic implements TriggerOnDeathRelic {

    private static final String ITEM_TO_DROP_ON_FALL_DAMAGE_DEATH_KEY =
            "uniqueProperties.item_to_drop_on_fall_damage_death";

    public IcarusBane(String name, Config config) {
        super(name, config);
    }

    @Override
    public void onDeath(EntityDeathEvent event) {
        LivingEntity entity = event.getEntity();
        EntityDamageEvent lastEntityDamageEvent = entity.getLastDamageCause();
        if (lastEntityDamageEvent == null) {
            return;
        }

        if (lastEntityDamageEvent.getCause() == EntityDamageEvent.DamageCause.FALL) {
            dropItem(event);
        }
    }

    private void dropItem(EntityDeathEvent event) {
        LivingEntity entity = event.getEntity();
        World world = entity.getWorld();
        ItemStack itemToDrop = new ItemStack(Objects.requireNonNull(
                config.get().getItemStack(ITEM_TO_DROP_ON_FALL_DAMAGE_DEATH_KEY)));
        event.getDrops().add(itemToDrop);

        world.spawnParticle(Particle.EXPLOSION_LARGE, entity.getLocation(), 1);
        world.spawnParticle(Particle.LAVA, entity.getLocation(), 7);
        world.spawnParticle(Particle.SOUL, entity.getLocation(), 10);
        world.playSound(entity.getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 1f, 1f);
    }
}
