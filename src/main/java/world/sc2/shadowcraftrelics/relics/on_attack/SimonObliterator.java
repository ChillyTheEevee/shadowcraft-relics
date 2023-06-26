package world.sc2.shadowcraftrelics.relics.on_attack;

import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import world.sc2.shadowcraftrelics.config.Config;
import world.sc2.shadowcraftrelics.relics.Relic;

import java.util.List;

public class SimonObliterator extends Relic implements TriggerOnAttackRelic {

    private final float simonDamageMultiplier;
    private final float annoyingDamageMultiplier;
    private final String simonUUID;
    private final List<String> annoyingPeopleUUIDs;

    public SimonObliterator(int id, String name, Config config) {
        super(id, name, config);

        // Relic-specific properties
        simonDamageMultiplier = (float)
                config.get().getDouble("uniqueProperties.simonDamageMultiplier");
        annoyingDamageMultiplier = (float)
                config.get().getDouble("uniqueProperties.annoyingPersonDamageMultiplier");

        simonUUID = config.get().getString("uniqueProperties.simonUUID");
        annoyingPeopleUUIDs = config.get().getStringList("uniqueProperties.annoyingPeopleUUIDs");
    }

    @Override
    public boolean shouldTriggerFromEntityDamageByEntityEvent(EntityDamageByEntityEvent event) {
        if (!(event.getEntity() instanceof Player))
            return false;
        return event.getDamager() instanceof LivingEntity;
    }

    @Override
    public void onAttack(EntityDamageByEntityEvent event) {
        Entity hitEntity = event.getEntity();
        String hitEntityUUID = hitEntity.getUniqueId().toString();

        if (hitEntityUUID.equals(simonUUID)) {
            event.setDamage(event.getDamage() * simonDamageMultiplier);
            if (event.getDamager() instanceof Player attacker) {
                attacker.sendMessage("Hit Simon! Dealt catastrophic damage!");
            }
            return;
        }

        for (String annoyingPersonUUID : annoyingPeopleUUIDs) {
            if (hitEntityUUID.equals(annoyingPersonUUID)) {
                event.setDamage(event.getDamage() * annoyingDamageMultiplier);
                if (event.getDamager() instanceof Player attacker) {
                    attacker.sendMessage("Hit an annoying person! Dealt boosted damage!");
                }
                return;
            }
        }

    }

}
