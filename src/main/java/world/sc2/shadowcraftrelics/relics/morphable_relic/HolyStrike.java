package world.sc2.shadowcraftrelics.relics.morphable_relic;

import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import live.chillytheeevee.chillylib.config.Config;
import live.chillytheeevee.chillylib.nbt.NBTTag;
import world.sc2.shadowcraftrelics.events.RelicMorphEvent;
import world.sc2.shadowcraftrelics.relics.on_attack.TriggerOnDirectAttackRelic;

/**
 * A {@link MorphableRelic} that has the special property of morphing into a {@link PaladinsBlade} when used to
 * attack directly in an {@link EntityDamageByEntityEvent}. A HolyStrike can be thought of as a second state of a
 * PaladinsBlade.
 */
public class HolyStrike extends NBTMorphableRelic implements TriggerOnDirectAttackRelic {

    public HolyStrike(String name, Config config, NBTTag<byte[], byte[]> morphableRelicQueueTag) {
        super(name, config, morphableRelicQueueTag);
    }

    @Override
    public void onAttack(EntityDamageByEntityEvent event) {
        Player player = ((Player) event.getDamager());
        EquipmentSlot equipmentSlot = EquipmentSlot.HAND;

        ItemStack holyStrike = player.getEquipment().getItem(equipmentSlot);

        RelicMorphEvent relicMorphEvent = new RelicMorphEvent(player, holyStrike, equipmentSlot);
        relicMorphEvent.callEvent();
    }

}
