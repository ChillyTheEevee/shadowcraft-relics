package world.sc2.shadowcraftrelics.relics;

import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;
import world.sc2.shadowcraftrelics.config.Config;
import world.sc2.shadowcraftrelics.config.ConfigManager;
import world.sc2.shadowcraftrelics.managers.RelicManager;

public abstract class Relic {

    protected final Config config;

    protected final String name;
    protected final int id;

    public Relic(int id, String name, String configPath) {
        this.id = id;
        this.name = name.toLowerCase();
        this.config = ConfigManager.getInstance().getConfig(configPath);
    }

    /**
     * @return The assigned ID of the Relic. This ID is assigned when the Relic is instantiated in {@link RelicManager}
     */
    public final int getId() {
        return id;
    }

    /**
     * @return The internal name of a Relic. By convention, all internal Relic names follow snake_case. This name is
     * used when identifying Relics through NBT tags, and is assigned to the Relic when it is instantiated in
     * {@link RelicManager}
     */
    public final String getName() {
        return name;
    }

    /**
     * @return The display name of a Relic. This is the name that appears on the actual ItemStack. By default,
     * the display name is taken from the "displayName" key in the Relic's respective {@link Config}.
     */
    public final String getDisplayName() {
        return config.get().getString("displayName");
    }

    /**
     * @return Whether the Relic is enabled within the enabledRelics.yml config.
     */
    public abstract boolean isEnabled();

    /**
     * @return The expected {@link Material} of a Relic.
     */
    public abstract @NotNull Material getMaterial();

}
