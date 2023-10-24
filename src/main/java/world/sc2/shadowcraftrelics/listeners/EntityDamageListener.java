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
import world.sc2.shadowcraftrelics.util.EntityUtils;

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
        LivingEntity realAttacker = EntityUtils.getRealAttacker(event.getDamager());
        if (realAttacker == null)
            return;

        ItemStack weapon = Objects.requireNonNull(realAttacker.getEquipment()).getItemInMainHand();

        Relic relic = relicManager.getRelicType(weapon);
        if (relic instanceof TriggerOnAttackRelic triggerOnAttackRelic) {
            triggerOnAttackRelic.onAttack(event);
        }

    }

}
