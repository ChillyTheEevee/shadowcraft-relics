package world.sc2.shadowcraftrelics.managers;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import world.sc2.shadowcraftrelics.ShadowcraftRelics;
import world.sc2.shadowcraftrelics.config.ConfigManager;
import world.sc2.shadowcraftrelics.relics.Relic;
import world.sc2.shadowcraftrelics.relics.on_attack.SimonObliterator;
import world.sc2.shadowcraftrelics.utils.ItemUtils;

import java.util.Collection;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class RelicManager {

    private final ConfigManager configManager;
    private final NamespacedKey relicTypeKey;
    private final BiMap<Integer, Relic> allRelics;

    public RelicManager(ShadowcraftRelics plugin, ConfigManager configManager) {
        this.configManager = configManager;

        // Create NamespacedKeys
        relicTypeKey = new NamespacedKey(plugin, "relic_type");

        allRelics = HashBiMap.create();
        registerRelics();
    }

    private void registerRelics() {
        registerRelic(new SimonObliterator(1, "simon_obliterator",
                configManager.getConfig("relicProperties/simonObliterator.yml")));
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

        return !Objects.equals(itemContainer.get(relicTypeKey, PersistentDataType.STRING), relic.getName());
    }

    /**
     * Creates and returns an instance of an {@link ItemStack} that has the properties of a specified Relic relicName.
     * relicName represents the internal name of a {@link Relic} as described by Relic.getName().
     * @param relicName The name of a {@link Relic} as described by Relic.getName().
     * @return An instance of an ItemStack with a Relic's special NBT Tag applied. This method will return null if
     * relicName does not match a valid Relic.
     */
    // TODO make Relic have enchants on creation
    // TODO create EnchantsSquared integration
    // TODO make Relic match name on creation
    public ItemStack createRelic(String relicName) {
        Relic relic = getRelicFromName(relicName);
        if (relic == null) return null;

        ItemStack item = new ItemStack(relic.getMaterial(), 1);
        ItemMeta itemMeta = item.getItemMeta();

        PersistentDataContainer container = itemMeta.getPersistentDataContainer();
        container.set(relicTypeKey, PersistentDataType.STRING, relic.getName());
        item.setItemMeta(itemMeta);
        return item;
    }

    public Collection<Relic> getRelicsMatchingFilter(Predicate<Relic> filter) {
        return allRelics.values().stream().filter(filter).distinct().collect(Collectors.toList());
    }

}
