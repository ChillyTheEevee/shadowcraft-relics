package world.sc2.shadowcraftrelics.relics;

import org.bukkit.Material;
import world.sc2.shadowcraftrelics.managers.RelicManager;

public abstract class Relic {

    protected String name;
    protected int id;

    public Relic(int id, String name) {
        this.id = id;
        this.name = name.toLowerCase();
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
    public final String getRelicName() {
        return name;
    }

    /**
     * @return The display name of a Relic. This is the name that appears on the actual ItemStack.
     */
    public abstract String getDisplayName();

    /**
     * @return An array of Strings that represent the lines of lore that a Relic is supposed to have upon creation
     */
    public abstract String[] getRelicLore();

    /**
     * @return Whether the Relic is enabled within the enabledRelics.yml config.
     */
    public abstract boolean isEnabled();

    /**
     * @return The expected {@link Material} of a Relic.
     */
    public abstract Material getMaterial();

}
