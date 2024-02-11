package world.sc2.shadowcraftrelics.managers;

import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import world.sc2.config.ConfigManager;
import world.sc2.nbt.NBTTag;
import world.sc2.shadowcraftrelics.ShadowcraftRelics;
import world.sc2.shadowcraftrelics.relics.NBTStorageRelic;
import world.sc2.shadowcraftrelics.relics.Relic;
import world.sc2.shadowcraftrelics.relics.on_attack.SimonObliterator;
import world.sc2.shadowcraftrelics.relics.on_consume.ForbiddenFruit;
import world.sc2.shadowcraftrelics.relics.morphable_relic.Purger;
import world.sc2.shadowcraftrelics.relics.on_move.Voidwalkers;
import world.sc2.utility.ItemUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class RelicManager {

    // Dependencies
    private final ShadowcraftRelics plugin;
    private final ConfigManager configManager;

    // Properties
    private final NBTTag relicTypeTag;
    private final ArrayList<Relic> allRelics;

    public RelicManager(ShadowcraftRelics plugin, ConfigManager configManager) {
        this.plugin = plugin;
        this.configManager = configManager;

        // Create NBT Tags
        var relicTypeKey = new NamespacedKey(plugin, "relic_type");
        relicTypeTag = new NBTTag(relicTypeKey, PersistentDataType.STRING);

        allRelics = new ArrayList<>();
        registerRelics();
    }

    private void registerRelics() {
        // Relics
        registerRelic(new SimonObliterator("simon_obliterator",
                configManager.getConfig("relicProperties/simonObliterator.yml")));
        registerRelic(new Voidwalkers("voidwalkers",
                configManager.getConfig("relicProperties/voidwalkers.yml"), plugin));
        registerRelic(new ForbiddenFruit("forbidden_fruit",
                configManager.getConfig("relicProperties/forbidden_fruit.yml")));

        // Morphable Relics

        registerRelic(new Purger("purger", configManager.getConfig("relicProperties/purger.yml")));
    }

    /**
     * Registers the specified {@link Relic} with this RelicManager if it is enabled.
     * @param relic The relic to register
     */
    private void registerRelic(Relic relic) {
        if (relic.isEnabled()) {
            allRelics.add(relic);
        }
    }

    /**
     * @param name The internal name of a Relic as given on instantiation
     * @return the Relic associated with the given internal name, null if name is invalid.
     */
    public Relic getRelicFromName(String name) {
        for (Relic relic : allRelics) {
            if (relic.getName().equals(name)) {
                return relic;
            }
        }
        return null;
    }
    /**
     * Returns the {@link Relic} type that this item represents.
     * @param item The item to get the Relic functionality of
     * @return the type of Relic that the {@link ItemStack} represents, or null if the ItemStack is not a Relic
     */
    public Relic getRelicType(ItemStack item) {
        if(ItemUtils.isAirOrNull(item)) {
            return null;
        }

        PersistentDataContainer itemContainer = item.getItemMeta().getPersistentDataContainer();
        NamespacedKey relicTypeKey = relicTypeTag.getNamespacedKey();

        if (itemContainer.has(relicTypeKey)) {
            return getRelicFromName(itemContainer.get(relicTypeKey, PersistentDataType.STRING));
        }

        return null;
    }

    /**
     * Tests if an {@link ItemStack} represents an instance of this type of {@link Relic}.
     * @param item The item to test
     * @param relic The relic to compare to
     * @return true if the item is the specified Relic
     */
    public boolean isRelic(ItemStack item, Relic relic) {
        if (ItemUtils.isAirOrNull(item))
            return false;

        PersistentDataContainer itemContainer = item.getItemMeta().getPersistentDataContainer();
        NamespacedKey relicTypeKey = relicTypeTag.getNamespacedKey();

        if (!itemContainer.has(relicTypeKey))
            return false;

        return Objects.equals(itemContainer.get(relicTypeKey, PersistentDataType.STRING), relic.getName());
    }

    /**
     * Applies a {@link Relic}'s necessary NBT tags to the given {@link ItemStack}. Does not currently work for
     * {@link Purger}.
     *
     * @param item The item to add the NBT tags to
     * @param relic The relic to get the NBT tags from
     */
    public void applyRelicNBTTags(@NotNull ItemStack item, @NotNull Relic relic) {
        relicTypeTag.applyTag(item, relic.getName());

        if (relic instanceof NBTStorageRelic nbtStorageRelic) {
            for (NBTTag nbtTag : nbtStorageRelic.getRelicNBTTags()) {
                if (nbtTag.getDefaultData() != null)
                    nbtTag.applyTag(item);
            }
        }
    }

    public Collection<Relic> getRelicsMatchingFilter(Predicate<Relic> filter) {
        return allRelics.stream().filter(filter).distinct().collect(Collectors.toList());
    }

}
