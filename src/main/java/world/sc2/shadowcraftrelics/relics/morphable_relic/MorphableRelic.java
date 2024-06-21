package world.sc2.shadowcraftrelics.relics.morphable_relic;

import org.bukkit.inventory.PlayerInventory;
import world.sc2.shadowcraftrelics.events.RelicMorphEvent;
import world.sc2.shadowcraftrelics.relics.Relic;

/**
 * Represents a {@link Relic} with the functionality to cycle through a list of several states. This change in state
 * could be caused by a variety of different things, such as player damage or player movement. Relics can only morph
 * if contained within a {@link PlayerInventory}.
 */
public interface MorphableRelic {

    /**
     * Morphs the MorphableRelic into its next state.
     * @param event The {@link RelicMorphEvent} in which the MorphableRelic is transformed into its next state.
     */
    void morph(RelicMorphEvent event);

}
