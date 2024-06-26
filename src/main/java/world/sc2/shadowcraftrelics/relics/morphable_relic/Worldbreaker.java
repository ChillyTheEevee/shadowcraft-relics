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
 * A {@link MorphableRelic} with the special property to morph into its next state upon right click. Worldbreakers
 * have three separate states - That of a pickaxe, an axe, and of a shovel.
 */
public class Worldbreaker extends NBTMorphableRelic implements TriggerOnInteractRelic {

    public Worldbreaker(String name, Config config, NBTTag<byte[], byte[]> morphableRelicQueueTag) {
        super(name, config, morphableRelicQueueTag);
    }

    @Override
    public void onInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();

        EquipmentSlot equipmentSlot = EquipmentSlot.HAND;
        ItemStack currentWorldbreaker = player.getInventory().getItem(equipmentSlot);

        if (event.getAction().isRightClick()) {
            RelicMorphEvent relicMorphEvent = new RelicMorphEvent(player, currentWorldbreaker, equipmentSlot);
            relicMorphEvent.callEvent();
        }
    }

}
