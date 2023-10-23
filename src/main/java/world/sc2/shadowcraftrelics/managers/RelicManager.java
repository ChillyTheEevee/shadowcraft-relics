package world.sc2.shadowcraftrelics.managers;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import world.sc2.shadowcraftrelics.ShadowcraftRelics;
import world.sc2.shadowcraftrelics.config.ConfigManager;
import world.sc2.shadowcraftrelics.relics.Relic;
import world.sc2.shadowcraftrelics.relics.on_attack.SimonObliterator;
import world.sc2.shadowcraftrelics.relics.on_interact.Purger;
import world.sc2.shadowcraftrelics.util.ItemUtils;

import java.util.Collection;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class RelicManager {

    // Dependencies
    private final ShadowcraftRelics plugin;
    private final ConfigManager configManager;

    // Properties
    private final NamespacedKey relicTypeKey;
    private final BiMap<Integer, Relic> allRelics;

    public RelicManager(ShadowcraftRelics plugin, ConfigManager configManager) {
        this.plugin = plugin;
        this.configManager = configManager;

        // Create NamespacedKeys
        relicTypeKey = new NamespacedKey(plugin, "relic_type");

        allRelics = HashBiMap.create();
        registerRelics();
    }

    private void registerRelics() {
        registerRelic(new SimonObliterator(1, "simon_obliterator",
                configManager.getConfig("relicProperties/simonObliterator.yml")));
        registerRelic(new Purger(2, "purger",
                configManager.getConfig("relicProperties/purger.yml"), plugin));
    }

    private void registerRelic(Relic relic) {
        if (relic.isEnabled()) {
            allRelics.put(relic.getId(), relic);
        }
    }

    /**
     * @param name The internal name of a Relic as given on instantiation
     * @return the Relic associated with the given internal name, null if name is invalid.
     */
    public Relic getRelicFromName(String name) {
        for (Relic relic : allRelics.values()) {
            if (relic.getName().equals(name)) {
                return relic;
            }
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

        if (!itemContainer.has(relicTypeKey))
            return false;

        return Objects.equals(itemContainer.get(relicTypeKey, PersistentDataType.STRING), relic.getName());
    }

    /**
     * Applies a {@link Relic}'s NBT tag to the given {@link ItemStack}.
     */
    public void giveItemRelicNBTTag(@NotNull ItemStack item, @NotNull Relic relic) {
        ItemMeta itemMeta = item.getItemMeta();
        PersistentDataContainer container = itemMeta.getPersistentDataContainer();

        container.set(relicTypeKey, PersistentDataType.STRING, relic.getName());
        item.setItemMeta(itemMeta);
    }

    public Collection<Relic> getRelicsMatchingFilter(Predicate<Relic> filter) {
        return allRelics.values().stream().filter(filter).distinct().collect(Collectors.toList());
    }

}
