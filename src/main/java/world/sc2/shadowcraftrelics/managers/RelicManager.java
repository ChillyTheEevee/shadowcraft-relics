package world.sc2.shadowcraftrelics.managers;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import world.sc2.shadowcraftrelics.ShadowcraftRelics;
import world.sc2.shadowcraftrelics.relics.Relic;
import world.sc2.shadowcraftrelics.relics.on_attack.SimonObliterator;
import world.sc2.shadowcraftrelics.utils.ItemUtils;

import java.util.Collection;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class RelicManager {

    private static RelicManager manager;

    private static final NamespacedKey relicKey = new NamespacedKey(ShadowcraftRelics.getPlugin(), "relic_type");
    private final BiMap<Integer, Relic> allRelics;

    private RelicManager() {
        allRelics = HashBiMap.create();

        registerRelics();
    }

    public static RelicManager getInstance() {
        if (manager == null) manager = new RelicManager();
        return manager;
    }

    private void registerRelics() {
        registerRelic(new SimonObliterator(1, "simon_obliterator"));
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
     * Tests if an {@link ItemStack} represents a {@link Relic}.
     * @param item The item to test
     * @param relicName The internal name of the Relic that is to be tested
     * @return true if the item is the specified Relic
     */
    public boolean isRelic(ItemStack item, String relicName) {
        if (ItemUtils.isAirOrNull(item))
            return false;
        Relic relic = getRelicFromName(relicName);
        if (relic == null)
            return false;

        PersistentDataContainer itemContainer = item.getItemMeta().getPersistentDataContainer();

        if (!itemContainer.has(relicKey))
            return false;

        return !Objects.equals(itemContainer.get(relicKey, PersistentDataType.STRING), relic.getName());
    }

    public Collection<Relic> getRelicsMatchingFilter(Predicate<Relic> filter) {
        return allRelics.values().stream().filter(filter).distinct().collect(Collectors.toList());
    }

}
