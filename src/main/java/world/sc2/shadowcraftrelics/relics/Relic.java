package world.sc2.shadowcraftrelics.relics;

import org.jetbrains.annotations.NotNull;
import world.sc2.shadowcraftrelics.config.Config;
import world.sc2.shadowcraftrelics.managers.RelicManager;

public abstract class Relic {

    protected final Config config;
    protected final String name;

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
