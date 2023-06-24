package world.sc2.shadowcraftrelics.relics.on_attack;

import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.EntityEquipment;
import org.jetbrains.annotations.NotNull;
import world.sc2.shadowcraftrelics.relics.Relic;

import java.util.List;

public class SimonObliterator extends Relic implements TriggerOnAttackRelic {

    private final float simonDamageMultiplier;
    private final float annoyingDamageMultiplier;
    private final String simonUUID;
    private final List<String> annoyingPeopleUUIDs;

    public SimonObliterator(int id, String name) {
        super(id, name, "relicProperties/simonObliterator.yml");

        // Relic-specific properties
        simonDamageMultiplier = (float)
                config.get().getDouble("uniqueProperties.simonDamageMultiplier");
        annoyingDamageMultiplier = (float)
                config.get().getDouble("uniqueProperties.annoyingPersonDamageMultiplier");

        simonUUID = config.get().getString("uniqueProperties.simonUUID");
        annoyingPeopleUUIDs = config.get().getStringList("uniqueProperties.annoyingPeopleUUIDs");
    }

    // TODO make material a config option
    @Override
    public @NotNull Material getMaterial() {
        return Material.STONE_AXE;
    }

    @Override
    public boolean shouldTriggerFromEntityDamageByEntityEvent(EntityDamageByEntityEvent event) {
        if (!(event.getEntity() instanceof Player))
            return false;
        if (!(event.getDamager() instanceof LivingEntity attacker))
            return false;

        EntityEquipment equipment = attacker.getEquipment();
        return equipment != null;
    }

    @Override
    public void onAttack(EntityDamageByEntityEvent event) {
        Player hitPlayer = (Player) event.getEntity();
        String hitPlayerUUID = hitPlayer.getUniqueId().toString();

        if (hitPlayerUUID.equals(simonUUID)) {
            event.setDamage(event.getDamage() * simonDamageMultiplier);
            if (event.getDamager() instanceof Player attacker) {
                attacker.sendMessage("Hit Simon! Dealt catastrophic damage!");
            }
            return;
        }

        for (String annoyingPersonUUID : annoyingPeopleUUIDs) {
            if (hitPlayerUUID.equals(annoyingPersonUUID)) {
                event.setDamage(event.getDamage() * annoyingDamageMultiplier);
                if (event.getDamager() instanceof Player attacker) {
                    attacker.sendMessage("Hit an annoying person! Dealt boosted damage!");
                }
                return;
            }
        }

    }

}
