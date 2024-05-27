package world.sc2.shadowcraftrelics.listeners.custom;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import world.sc2.shadowcraftrelics.events.RelicMorphEvent;
import world.sc2.shadowcraftrelics.managers.RelicManager;
import world.sc2.shadowcraftrelics.relics.morphable_relic.MorphableRelic;

public class RelicMorphListener implements Listener {

    private final RelicManager relicManager;

    public RelicMorphListener(RelicManager relicManager) {
        this.relicManager = relicManager;
    }

    @EventHandler (priority = EventPriority.MONITOR)
    public void onRelicMorphEvent(RelicMorphEvent event) {
        ItemStack morphableRelic = event.getCurrentRelicState();
        assert relicManager.getRelicType(morphableRelic) instanceof MorphableRelic;
        ((MorphableRelic) relicManager.getRelicType(morphableRelic)).morph(event);
        Bukkit.getLogger().warning("Relic morphed into next state!"); // todo remove debugging code
    }

}
