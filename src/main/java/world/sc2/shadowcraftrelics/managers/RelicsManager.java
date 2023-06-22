package world.sc2.shadowcraftrelics.managers;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import org.bukkit.NamespacedKey;
import world.sc2.shadowcraftrelics.ShadowcraftRelics;
import world.sc2.shadowcraftrelics.relics.Relic;

public class RelicsManager {

    private static RelicsManager manager;

    private static final NamespacedKey relicsKey = new NamespacedKey(ShadowcraftRelics.getPlugin(), "relic_type");
    private final BiMap<Integer, Relic> relics;

    private RelicsManager() {
        relics = HashBiMap.create();

        registerRelics();
    }

    public static RelicsManager getInstance() {
        if (manager == null) manager = new RelicsManager();
        return manager;
    }

    private void registerRelics() {

    }

    private void registerRelic(Integer index, Relic relic) {
        if (relic.isEnabled()) {
            relics.put(relic.getId(), relic);
        }
    }

}
