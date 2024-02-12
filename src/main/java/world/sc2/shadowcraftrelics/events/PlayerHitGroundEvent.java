package world.sc2.shadowcraftrelics.events;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;
import org.jetbrains.annotations.NotNull;

/**
 * Stores data for when a {@link Player} touches the ground after being airborne
 * @author ChillyTheEevee
 */
public class PlayerHitGroundEvent extends PlayerEvent {

    private static final HandlerList HANDLERS = new HandlerList();

    /**
     * @param who The player that hit the ground
     */
    public PlayerHitGroundEvent(@NotNull Player who) {
        super(who);
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLERS;
    }

    // This is required, don't touch it
    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

}