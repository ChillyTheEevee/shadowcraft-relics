package world.sc2.shadowcraftrelics.relics.morphable_relic;

import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import live.chillytheeevee.chillylib.config.Config;
import live.chillytheeevee.chillylib.nbt.NBTTag;
import world.sc2.shadowcraftrelics.events.RelicMorphEvent;
import world.sc2.shadowcraftrelics.relics.NBTStorageRelic;
import world.sc2.shadowcraftrelics.relics.Relic;
import live.chillytheeevee.chillylib.utility.ItemUtils;

import java.io.*;
import java.util.LinkedList;

/**
 * An implementation of the {@link MorphableRelic} interface. This implementation uses an {@link NBTTag} to store
 * information about the various states of a MorphableRelic. Every {@link ItemStack} state of a NBTMorphableRelic
 * contains only one {@link NBTTag}: morphableRelicQueue.
 * <p>
 * This NBTTag stores a serialized LinkedList of ItemStack states that the NBTMorphableRelic will cycle through next.
 * The only ItemStack that contains this morphableRelicQueue NBTTag is the ItemStack state that is the present state of
 * the MorphableRelic. When a NBTMorphableRelic is morphed, this morphableRelicQueue tag is removed from the current
 * state, manipulated, and added onto the next state.
 * <p>
 * Only one of these states should ever be deserialized at once. When a ItemStack is morphed, the current ItemStack
 * state is added onto the end of the morphableRelicQueue tag, creating a perfect cycle.
 */
public abstract class NBTMorphableRelic extends Relic implements MorphableRelic, NBTStorageRelic {
    private final NBTTag<byte[], byte[]> morphableRelicQueueTag;

    /**
     * @param name             The name of the Relic
     * @param config           The {@link Config} assigned to the Relic
     * @param morphableRelicQueueTag The NBT tag in which the serialized LinkedList of ItemStack states is stored.
     */
    public NBTMorphableRelic(String name, Config config, NBTTag<byte[], byte[]> morphableRelicQueueTag) {
        super(name, config);
        this.morphableRelicQueueTag = morphableRelicQueueTag;
    }

    public void morph(RelicMorphEvent event) {
        Player player = event.getPlayer();
        ItemStack currentRelicState = event.getCurrentRelicState();
        EquipmentSlot equipmentSlot = event.getEquipmentSlot();

        // 1) Deserialize the currentRelicState's stored Queue
        assert morphableRelicQueueTag.hasTag(currentRelicState);
        byte[] serializedMorphableRelicQueue = morphableRelicQueueTag.getStoredData(currentRelicState);
        LinkedList<byte[]> morphableRelicQueue;
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(serializedMorphableRelicQueue);
        try (ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream)) {
            morphableRelicQueue = (LinkedList<byte[]>) objectInputStream.readObject(); // todo make warnings go away :(
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace(); // todo better debugging
            return;
        }

        // 2) Get next morphable relic state
        byte[] serializedNextRelicState = morphableRelicQueue.poll();
        assert serializedNextRelicState != null;
        ItemStack nextRelicState = ItemUtils.deserializeItemStack(serializedNextRelicState);

        // 3) Process morphableRelicQueue
        morphableRelicQueueTag.removeTag(currentRelicState);
        morphableRelicQueue.add(ItemUtils.serializeItemStack(currentRelicState));

        // 4) Re-serialize the Queue and apply the NBTTag
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try (ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream)) {
            objectOutputStream.writeObject(morphableRelicQueue);
        } catch (IOException e) {
            e.printStackTrace(); // todo better debugging
            return;
        }
        serializedMorphableRelicQueue = byteArrayOutputStream.toByteArray();

        morphableRelicQueueTag.applyTag(nextRelicState, serializedMorphableRelicQueue);

        player.getInventory().setItem(equipmentSlot, nextRelicState);
    }

    @Override
    public NBTTag[] getRelicNBTTags() {
        return new NBTTag[]{morphableRelicQueueTag};
    }

}
