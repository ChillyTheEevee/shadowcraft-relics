package world.sc2.shadowcraftrelics.listeners;

import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;
import world.sc2.shadowcraftrelics.managers.RelicManager;
import world.sc2.shadowcraftrelics.relics.Relic;
import world.sc2.shadowcraftrelics.relics.on_attack.TriggerOnAttackRelic;

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
        if (!(event.getDamager() instanceof LivingEntity attacker))
            return;
        ItemStack weapon = Objects.requireNonNull(attacker.getEquipment()).getItemInMainHand();
        for (Relic relic : relicManager.getRelicsMatchingFilter(r -> r instanceof TriggerOnAttackRelic)) {
            TriggerOnAttackRelic onAttackRelic = (TriggerOnAttackRelic) relic;
            if (relicManager.isRelic(weapon, relic) && onAttackRelic.shouldTriggerFromEntityDamageByEntityEvent(event)) {
                onAttackRelic.onAttack(event);
            }
        }
    }

}
