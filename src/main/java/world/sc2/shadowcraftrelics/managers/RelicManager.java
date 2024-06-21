package world.sc2.shadowcraftrelics.managers;

import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import live.chillytheeevee.chillylib.config.ConfigManager;
import live.chillytheeevee.chillylib.nbt.NBTTag;
import world.sc2.shadowcraftrelics.relics.NBTStorageRelic;
import world.sc2.shadowcraftrelics.relics.Relic;
import world.sc2.shadowcraftrelics.relics.morphable_relic.*;
import world.sc2.shadowcraftrelics.relics.on_attack.SimonObliterator;
import world.sc2.shadowcraftrelics.relics.on_consume.ForbiddenFruit;
import world.sc2.shadowcraftrelics.relics.on_death.IcarusBane;
import world.sc2.shadowcraftrelics.relics.on_move.Voidwalkers;
import live.chillytheeevee.chillylib.utility.ItemUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * A class used for managing the various {@link Relic} classes in SC2. Contains several functions for accessing data on
 * and manipulating Relics.
 * @author ChillyTheEevee
 */
public class RelicManager {

    // Dependencies
    private final JavaPlugin plugin;
    private final ConfigManager configManager;

    // Properties
    private final NBTTag relicTypeTag;
    private final List<Relic> allRelics;

    /**
     * Constructs a new RelicManager for the given plugin and ConfigManager
     * @param plugin The Plugin that this RelicManager belongs to
     * @param configManager The ConfigManager that belongs to the plugin
     */
    public RelicManager(JavaPlugin plugin, ConfigManager configManager) {
        this.plugin = plugin;
        this.configManager = configManager;

        // Create NBT Tags
        var relicTypeKey = new NamespacedKey(plugin, "relic_type");
        relicTypeTag = new NBTTag(relicTypeKey, PersistentDataType.STRING);

        allRelics = new ArrayList<>();
        registerRelics();
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

    /**
     * Returns all registered Relics matching the predicate filter
     * @param filter The Predicate in which to test
     * @return A Collection of Relics that match the filter provided
     */
    public Collection<Relic> getRelicsMatchingFilter(Predicate<Relic> filter) {
        return allRelics.stream().filter(filter).distinct().collect(Collectors.toList());
    }

    /**
     * Registers all enabled Relics with this RelicManager.
     */
    private void registerRelics() {
        // Relics
        registerRelic(new SimonObliterator("simon_obliterator",
                configManager.getConfig("relicProperties/simon_obliterator.yml")));
        registerRelic(new Voidwalkers("voidwalkers",
                configManager.getConfig("relicProperties/voidwalkers.yml"), plugin));
        registerRelic(new ForbiddenFruit("forbidden_fruit",
                configManager.getConfig("relicProperties/forbidden_fruit.yml")));
        registerRelic(new IcarusBane("icarus_bane",
                configManager.getConfig("relicProperties/icarus_bane.yml")));

        // Morphable Relics
        NBTTag<byte[], byte[]> morphableRelicQueueTag =
                new NBTTag<>(new NamespacedKey(plugin, "morphableRelicQueue"), PersistentDataType.BYTE_ARRAY);

        registerRelic(new Purger("purger", configManager.getConfig("relicProperties/purger.yml"),
                morphableRelicQueueTag));

        // Paladin's blade
        NBTTag<Long, Long> lastActivationTimeTag = new NBTTag<>(new NamespacedKey(plugin, "lastActivationTime"),
                PersistentDataType.LONG, 0L);
        registerRelic(new PaladinsBlade("paladins_blade",
                configManager.getConfig("relicProperties/paladins_blade.yml"), morphableRelicQueueTag,
                lastActivationTimeTag));

        registerRelic(new HolyStrike("holy_strike",
                configManager.getConfig("relicProperties/holy_strike.yml"), morphableRelicQueueTag));

        // Forerunner's Testament
        registerRelic(new ForerunnersTestament("forerunners_testament",
                configManager.getConfig("relicProperties/forerunnerstestament.yml"), morphableRelicQueueTag));

        // Foreign Forged Blade
        registerRelic(new ForeignForgedBlade("foreign_forged_blade",
                configManager.getConfig("relicProperties/foreign_forged_blade.yml"), morphableRelicQueueTag));

        // Multitool
        registerRelic(new Multitool("multitool",
                configManager.getConfig("relicProperties/multitool.yml"), morphableRelicQueueTag));

        // Worldbreaker
        registerRelic(new Worldbreaker("worldbreaker",
                configManager.getConfig("relicProperties/worldbreaker.yml"), morphableRelicQueueTag));
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

}
