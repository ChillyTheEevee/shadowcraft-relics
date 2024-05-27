package world.sc2.shadowcraftrelics.relics.morphable_relic;

import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import world.sc2.config.Config;
import world.sc2.nbt.NBTTag;
import world.sc2.shadowcraftrelics.events.RelicMorphEvent;
import world.sc2.shadowcraftrelics.relics.on_attack.TriggerOnDirectAttackRelic;

/**
 * A {@link MorphableRelic} that has the special property of switching states every time it is used to attack
 * in an EntityDamageByEntityEvent.
 */
public class ForerunnersTestament extends NBTMorphableRelic implements TriggerOnDirectAttackRelic {

    public ForerunnersTestament(String name, Config config, NBTTag<byte[], byte[]> morphableRelicQueueTag) {
        super(name, config, morphableRelicQueueTag);
    }

    @Override
    public void onAttack(EntityDamageByEntityEvent event) {
        Player player = ((Player) event.getDamager());
        EquipmentSlot equipmentSlot = EquipmentSlot.HAND;

        ItemStack currentForerunnersTestament = player.getEquipment().getItem(equipmentSlot);

        RelicMorphEvent relicMorphEvent = new RelicMorphEvent(player, currentForerunnersTestament, equipmentSlot);
        relicMorphEvent.callEvent();
    }

}
