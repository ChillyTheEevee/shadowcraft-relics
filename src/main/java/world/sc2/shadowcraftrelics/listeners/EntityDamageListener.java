package world.sc2.shadowcraftrelics.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import world.sc2.shadowcraftrelics.managers.RelicManager;
import world.sc2.shadowcraftrelics.relics.Relic;
import world.sc2.shadowcraftrelics.relics.on_attack.TriggerOnAttackRelic;

public class EntityDamageListener implements Listener {

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {

    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        // Attack relics
        for (Relic relic : RelicManager.getInstance().getRelicsMatchingFilter(r -> r instanceof TriggerOnAttackRelic)) {
            TriggerOnAttackRelic onAttackRelic = (TriggerOnAttackRelic) relic;
            if (onAttackRelic.shouldTriggerFromEntityDamageByEntityEvent(event)) {
                onAttackRelic.onAttack(event);
            }
        }
    }

}
