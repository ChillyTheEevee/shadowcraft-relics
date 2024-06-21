package world.sc2.shadowcraftrelics.relics.morphable_relic;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import live.chillytheeevee.chillylib.config.Config;
import live.chillytheeevee.chillylib.nbt.NBTTag;
import world.sc2.shadowcraftrelics.events.RelicMorphEvent;
import world.sc2.shadowcraftrelics.relics.on_attack.TriggerOnDirectAttackRelic;
import world.sc2.shadowcraftrelics.relics.on_interact.TriggerOnInteractRelic;

/**
 * A {@link MorphableRelic} that has the special property of morphing between two separate states - sword state
 * and bow state - to best reflect the situation. A Purger morphs from bow state to sword state when used to attack
 * in a {@link EntityDamageByEntityEvent} or when held and left-clicked in a {@link PlayerInteractEvent}. A Purger
 * transitions from sword state back to bow state upon right click in a PlayerInteractEvent.
 */
public class Purger extends NBTMorphableRelic implements TriggerOnInteractRelic, TriggerOnDirectAttackRelic {

    public Purger(String name, Config config, NBTTag<byte[], byte[]> morphableRelicQueueTag) {
        super(name, config, morphableRelicQueueTag);
    }

    @Override
    public void onInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();

        EquipmentSlot equipmentSlot = EquipmentSlot.HAND;
        ItemStack currentPurger = player.getInventory().getItem(equipmentSlot);

        if ((currentPurger.getType() == Material.BOW && event.getAction().isLeftClick())
        || (currentPurger.getType() != Material.BOW && event.getAction().isRightClick())) {
            RelicMorphEvent relicMorphEvent = new RelicMorphEvent(player, currentPurger, equipmentSlot);
            relicMorphEvent.callEvent();
        }
    }

    @Override
    public void onAttack(EntityDamageByEntityEvent event) {
        Player player = ((Player) event.getDamager());
        EquipmentSlot equipmentSlot = EquipmentSlot.HAND;
        ItemStack currentPurger = player.getEquipment().getItem(equipmentSlot);

        if (currentPurger.getType() == Material.BOW) {
            RelicMorphEvent relicMorphEvent = new RelicMorphEvent(player, currentPurger, equipmentSlot);
            relicMorphEvent.callEvent();
        }
    }

}
