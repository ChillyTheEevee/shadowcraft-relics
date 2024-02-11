package world.sc2.shadowcraftrelics.relics.morphable_relic;

import world.sc2.config.Config;
import world.sc2.nbt.NBTTag;
import world.sc2.shadowcraftrelics.events.RelicMorphEvent;
import world.sc2.shadowcraftrelics.relics.NBTStorageRelic;
import world.sc2.shadowcraftrelics.relics.Relic;

// TODO implement MorphableRelicImplementation such that it is a valid implementation of MorphableRelic
/**
 * An implementation of the {@link MorphableRelic} interface
 */
public abstract class MorphableRelicImplementation extends Relic implements MorphableRelic, NBTStorageRelic {

    /**
     * @param name The name of the Relic
     * @param config The {@link Config} assigned to the Relic
     */
    public MorphableRelicImplementation(String name, Config config) {
        super(name, config);
    }

    public void morph(RelicMorphEvent event) {

    }


    /*
    @Override
    public void morph(RelicMorphEvent event) {
        ItemStack currentRelicState = event.getCurrentRelicState();
        ItemMeta currentRelicStateMeta = currentRelicState.getItemMeta();

        PersistentDataContainer currentRelicStatePDC = currentRelicStateMeta.getPersistentDataContainer();

        NamespacedKey storedItemKey = nextMorphStateTag.getNamespacedKey();

        if (!currentRelicStatePDC.has(storedItemKey, PersistentDataType.BYTE_ARRAY)) {
            Bukkit.getLogger().severe("Relic cannot switch because ItemStack does not have storedItemKey!");
            return;
        }

        // Create stored relic ItemStack
        ItemStack nextRelicState = ItemUtils.deserializeItemStack(
                currentRelicStatePDC.get(storedItemKey, PersistentDataType.BYTE_ARRAY));

        ItemMeta nextRelicStateMeta = nextRelicState.getItemMeta();
        PersistentDataContainer nextRelicStatePDC = nextRelicStateMeta.getPersistentDataContainer();

        // remove next relic state's data from the current relic state's PDC
        currentRelicStatePDC.remove(storedItemKey);
        currentRelicState.setItemMeta(currentRelicStateMeta);

        // Serialize and store current relic state inside the next Purger
        nextRelicStatePDC.set(storedItemKey, PersistentDataType.BYTE_ARRAY,
                ItemUtils.serializeItemStack(currentRelicState));

        nextRelicState.setItemMeta(nextRelicStateMeta);


        event.getPlayer().getInventory().setItemInMainHand(nextRelicState);
    }
     */

    @Override
    public NBTTag[] getRelicNBTTags() {
        return new NBTTag[]{};
    }

}
