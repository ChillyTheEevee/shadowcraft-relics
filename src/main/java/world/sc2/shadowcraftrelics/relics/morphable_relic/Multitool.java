package world.sc2.shadowcraftrelics.relics.morphable_relic;

import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import world.sc2.config.Config;
import world.sc2.config.ConfigManager;
import world.sc2.nbt.NBTTag;
import world.sc2.shadowcraftrelics.events.RelicMorphEvent;
import world.sc2.shadowcraftrelics.relics.on_interact.TriggerOnInteractRelic;

public class Multitool extends ConfigMorphableRelic implements TriggerOnInteractRelic {

    public Multitool(String name, Config config, ConfigManager configManager, NBTTag<String, String> morphConfigIDTag, NBTTag<Integer, Integer> morphIndexTag) {
        super(name, config, configManager, morphConfigIDTag, morphIndexTag);
    }

    @Override
    public void onInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();

        EquipmentSlot equipmentSlot = EquipmentSlot.HAND;
        ItemStack currentMultiool = player.getInventory().getItem(equipmentSlot);

        if (event.getAction().isRightClick()) {
            RelicMorphEvent relicMorphEvent = new RelicMorphEvent(player, currentMultiool, equipmentSlot);
            morph(relicMorphEvent);
        }
    }

}
