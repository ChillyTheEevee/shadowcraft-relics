package world.sc2.shadowcraftrelics.relics.on_attack;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import live.chillytheeevee.chillylib.config.Config;
import world.sc2.shadowcraftrelics.relics.Relic;

import java.util.List;

/**
 * A melee {@link Relic} that has the special property of dealing extra damage to users with config-defined UUIDs.
 */
public class SimonObliterator extends Relic implements TriggerOnDirectAttackRelic {

    private final float simonDamageMultiplier;
    private final float annoyingDamageMultiplier;
    private final String simonUUID;
    private final List<String> annoyingPeopleUUIDs;

    public SimonObliterator(String name, Config config) {
        super(name, config);

        // Relic-specific properties
        simonDamageMultiplier = (float)
                config.get().getDouble("uniqueProperties.simonDamageMultiplier");
        annoyingDamageMultiplier = (float)
                config.get().getDouble("uniqueProperties.annoyingPersonDamageMultiplier");

        simonUUID = config.get().getString("uniqueProperties.simonUUID");
        annoyingPeopleUUIDs = config.get().getStringList("uniqueProperties.annoyingPeopleUUIDs");
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
