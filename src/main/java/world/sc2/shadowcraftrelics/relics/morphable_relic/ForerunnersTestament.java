package world.sc2.shadowcraftrelics.relics.morphable_relic;

import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import world.sc2.config.Config;
import world.sc2.config.ConfigManager;
import world.sc2.nbt.NBTTag;
import world.sc2.shadowcraftrelics.events.RelicMorphEvent;
import world.sc2.shadowcraftrelics.relics.on_attack.TriggerOnDirectAttackRelic;

public class ForerunnersTestament extends ConfigMorphableRelic implements TriggerOnDirectAttackRelic {

    public ForerunnersTestament(String name, Config config, ConfigManager configManager, NBTTag<String, String> morphConfigIDTag, NBTTag<Integer, Integer> morphIndexTag) {
        super(name, config, configManager, morphConfigIDTag, morphIndexTag);
    }

    @Override
    public void onAttack(EntityDamageByEntityEvent event) {
        Player player = ((Player) event.getDamager());
        EquipmentSlot equipmentSlot = EquipmentSlot.HAND;

        ItemStack currentForerunnersTestament = player.getEquipment().getItem(equipmentSlot);

        RelicMorphEvent relicMorphEvent = new RelicMorphEvent(player, currentForerunnersTestament, equipmentSlot);
        morph(relicMorphEvent);
    }

}
