package world.sc2.shadowcraftrelics.relics;

import live.chillytheeevee.chillylib.nbt.NBTTag;

/**
 * Represents an {@link Relic} that stores further NBT data aside from the relicType NBT Tag.
 */
public interface NBTStorageRelic {

    /**
     * @return All {@link NBTTag}s that the Relic uses to function.
     */
    NBTTag[] getRelicNBTTags();

}
