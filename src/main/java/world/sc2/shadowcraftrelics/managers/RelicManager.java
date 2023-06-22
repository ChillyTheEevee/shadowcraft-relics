package world.sc2.shadowcraftrelics.managers;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import org.bukkit.NamespacedKey;
import world.sc2.shadowcraftrelics.ShadowcraftRelics;
import world.sc2.shadowcraftrelics.relics.Relic;

public class RelicManager {

    private static RelicManager manager;

    private static final NamespacedKey relicsKey = new NamespacedKey(ShadowcraftRelics.getPlugin(), "relic_type");
    private final BiMap<Integer, Relic> relics;

    private RelicManager() {
        relics = HashBiMap.create();

        registerRelics();
    }

    public static RelicManager getInstance() {
        if (manager == null) manager = new RelicManager();
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
