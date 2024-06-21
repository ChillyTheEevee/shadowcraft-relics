package world.sc2.shadowcraftrelics.relics;

import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import live.chillytheeevee.chillylib.config.Config;
import world.sc2.shadowcraftrelics.managers.RelicManager;

/**
 * A representation of an {@link ItemStack} with special properties. These special properties could be triggered by a
 * variety of events.
 *
 * @author ChillyTheEevee
 */
public abstract class Relic {

    protected final Config config;
    protected final String name;

    /**
     * @param name The name of the Relic
     * @param config The {@link Config} assigned to the Relic
     */
    public Relic(String name, Config config) {
        this.name = name.toLowerCase();
        this.config = config;
    }

    /**
     * @return Whether the Relic is enabled within the Relic's respective config file.
     */
    public final boolean isEnabled() {
        return config.get().getBoolean("isEnabled");
    }

    /**
     * @return The internal name of a Relic. By convention, all internal Relic names follow snake_case. This name is
     * used when identifying Relics through NBT tags, and is assigned to the Relic when it is instantiated in
     * {@link RelicManager}
     */
    public final @NotNull String getName() {
        return name;
    }

}
