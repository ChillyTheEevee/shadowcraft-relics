package world.sc2.shadowcraftrelics.listeners;

import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;
import world.sc2.shadowcraftrelics.managers.RelicManager;
import world.sc2.shadowcraftrelics.relics.Relic;
import world.sc2.shadowcraftrelics.relics.on_attack.TriggerOnDirectAttackRelic;

import java.util.Objects;

public class EntityDamageListener implements Listener {

    private final RelicManager relicManager;

    public EntityDamageListener(RelicManager relicManager) {
        this.relicManager = relicManager;
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {

    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {

        // Direct attack relics
        Entity attacker = event.getDamager();
        if (attacker instanceof LivingEntity livingAttacker) {
            ItemStack weapon = Objects.requireNonNull(livingAttacker.getEquipment()).getItemInMainHand();

            Relic relic = relicManager.getRelicType(weapon);
            if (relic instanceof TriggerOnDirectAttackRelic triggerOnDirectAttackRelic) {
                triggerOnDirectAttackRelic.onAttack(event);
            }
        }

    }

}
