package world.sc2.shadowcraftrelics.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import world.sc2.shadowcraftrelics.relics.morphable_relic.MorphableRelic;

/**
 * Contains data for when a {@link MorphableRelic} is morphed.
 */
public class RelicMorphEvent extends Event {

    private static final HandlerList HANDLERS = new HandlerList();

    private final Player player;
    private final ItemStack currentRelicState;
    private final EquipmentSlot equipmentSlot;

    public RelicMorphEvent(Player player, ItemStack currentRelicState, EquipmentSlot equipmentSlot) {
        this.player = player;
        this.currentRelicState = currentRelicState;
        this.equipmentSlot = equipmentSlot;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLERS;
    }

    public Player getPlayer() {
        return player;
    }

    public ItemStack getCurrentRelicState() {
        return currentRelicState;
    }

    public EquipmentSlot getEquipmentSlot() {
        return equipmentSlot;
    }
}
