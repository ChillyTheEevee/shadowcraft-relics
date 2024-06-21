package world.sc2.shadowcraftrelics.relics.morphable_relic;

import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import live.chillytheeevee.chillylib.config.Config;
import live.chillytheeevee.chillylib.nbt.NBTTag;
import world.sc2.shadowcraftrelics.events.RelicMorphEvent;
import world.sc2.shadowcraftrelics.relics.on_interact.TriggerOnInteractRelic;

/**
 * A {@link MorphableRelic} that has the special property of switching states on right click.
 */
public class ForeignForgedBlade extends NBTMorphableRelic implements TriggerOnInteractRelic {

    public ForeignForgedBlade(String name, Config config, NBTTag<byte[], byte[]> morphableRelicQueueTag) {
        super(name, config, morphableRelicQueueTag);
    }

    @Override
    public void onInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();

        EquipmentSlot equipmentSlot = EquipmentSlot.HAND;
        ItemStack currentForeignForgedBlade = player.getInventory().getItem(equipmentSlot);

        if (event.getAction().isRightClick()) {
            RelicMorphEvent relicMorphEvent = new RelicMorphEvent(player, currentForeignForgedBlade, equipmentSlot);
            relicMorphEvent.callEvent();
        }
    }

}
