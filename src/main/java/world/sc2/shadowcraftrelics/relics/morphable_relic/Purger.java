package world.sc2.shadowcraftrelics.relics.morphable_relic;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import world.sc2.config.Config;
import world.sc2.shadowcraftrelics.events.RelicMorphEvent;
import world.sc2.shadowcraftrelics.relics.on_attack.TriggerOnDirectAttackRelic;
import world.sc2.shadowcraftrelics.relics.on_interact.TriggerOnInteractRelic;

public class Purger extends MorphableRelicImplementation implements TriggerOnInteractRelic, TriggerOnDirectAttackRelic {

    public Purger(String name, Config config) {
        super(name, config);
    }

    @Override
    public void onInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();

        EquipmentSlot equipmentSlot = EquipmentSlot.HAND;
        ItemStack currentPurger = player.getInventory().getItem(equipmentSlot);

        if ((currentPurger.getType() == Material.BOW && event.getAction().isLeftClick())
        || (currentPurger.getType() != Material.BOW && event.getAction().isRightClick())) {
            RelicMorphEvent relicMorphEvent = new RelicMorphEvent(player, currentPurger, equipmentSlot);
            morph(relicMorphEvent);
        }
    }

    @Override
    public void onAttack(EntityDamageByEntityEvent event) {
        Player player = ((Player) event.getDamager());
        EquipmentSlot equipmentSlot = EquipmentSlot.HAND;
        ItemStack currentPurger = player.getEquipment().getItem(equipmentSlot);

        if (currentPurger.getType() == Material.BOW) {
            RelicMorphEvent relicMorphEvent = new RelicMorphEvent(player, currentPurger, equipmentSlot);
            morph(relicMorphEvent);
        }
    }

}
