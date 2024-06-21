package world.sc2.shadowcraftrelics.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
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

    /**
     * @param player The player whose inventory contains the {@link MorphableRelic}
     * @param currentRelicState The {@link ItemStack} representation of the MorphableRelic's current state
     * @param equipmentSlot The EquipmentSlot that "currentRelicState" is in within the Player's
     *                      {@link PlayerInventory}.
     */
    public RelicMorphEvent(Player player, ItemStack currentRelicState, EquipmentSlot equipmentSlot) {
        this.player = player;
        this.currentRelicState = currentRelicState;
        this.equipmentSlot = equipmentSlot;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLERS;
    }

    /**
     * @return The player whos inventory contains the {@link MorphableRelic}
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * @return The {@link ItemStack} representation of the MorphableRelic's current state
     */
    public ItemStack getCurrentRelicState() {
        return currentRelicState;
    }

    /**
     * @return The EquipmentSlot that "currentRelicState" is in within the player's {@link PlayerInventory}.
     */
    public EquipmentSlot getEquipmentSlot() {
        return equipmentSlot;
    }

    // This is required, don't touch it
    public static HandlerList getHandlerList() {
        return HANDLERS;
    }
}
