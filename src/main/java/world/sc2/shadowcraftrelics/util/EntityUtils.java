package world.sc2.shadowcraftrelics.util;

import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Projectile;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class EntityUtils {

    /**
     * @param entity The entity returned by getDamager() in an {@link EntityDamageByEntityEvent}.
     * @return The entity given if it is an instance of {@link LivingEntity}, its shooter if it is a {@link Projectile},
     * or null if neither is the case.
     */
    public static LivingEntity getRealAttacker(Entity entity) {
        if (entity instanceof Projectile) {
            if (((Projectile) entity).getShooter() instanceof LivingEntity)
                return (LivingEntity) ((Projectile) entity).getShooter();
        }
        if (entity instanceof LivingEntity) return (LivingEntity) entity;
        return null;
    }

}
