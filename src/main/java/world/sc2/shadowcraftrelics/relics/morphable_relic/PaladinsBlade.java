package world.sc2.shadowcraftrelics.relics.morphable_relic;

import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import live.chillytheeevee.chillylib.config.Config;
import live.chillytheeevee.chillylib.nbt.NBTTag;
import world.sc2.shadowcraftrelics.events.RelicMorphEvent;
import world.sc2.shadowcraftrelics.relics.NBTStorageRelic;
import world.sc2.shadowcraftrelics.relics.on_interact.TriggerOnInteractRelic;

import java.time.Instant;

/**
 * A {@link MorphableRelic} that has the special property of morphing into a {@link HolyStrike} when used to
 * right-click in a {@link PlayerInteractEvent} if a long enough time has passed since the last time it morphed. This
 * cooldown is implemented through an {@link NBTTag} lastActivationTimeTag, which stores the epoch seconds that the
 * PaladinsBlade last morphed into a HolyStrike.
 */
public class PaladinsBlade extends NBTMorphableRelic implements TriggerOnInteractRelic, NBTStorageRelic {

    private final String COOLDOWN_IN_SECONDS_KEY = "uniqueProperties.cooldown_in_seconds";
    private final NBTTag<Long, Long> lastActivationTimeTag;

    public PaladinsBlade(String name, Config config, NBTTag<byte[], byte[]> morphableRelicQueueTag,
                         NBTTag<Long, Long> lastActivationTimeTag) {
        super(name, config, morphableRelicQueueTag);
        this.lastActivationTimeTag = lastActivationTimeTag;
    }

    @Override
    public void onInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();

        EquipmentSlot equipmentSlot = EquipmentSlot.HAND;
        ItemStack paladinsBlade = player.getInventory().getItem(equipmentSlot);

        if (event.getAction().isRightClick()) {
            long currentTime = Instant.now().getEpochSecond();

            Long previousTime = lastActivationTimeTag.getStoredData(paladinsBlade);

            long deltaTime = currentTime - previousTime;

            long cooldownInSeconds = config.get().getLong(COOLDOWN_IN_SECONDS_KEY);

            if (deltaTime >= cooldownInSeconds) {
                lastActivationTimeTag.applyTag(paladinsBlade, currentTime);

                RelicMorphEvent relicMorphEvent = new RelicMorphEvent(player, paladinsBlade, equipmentSlot);
                relicMorphEvent.callEvent();
            }
        }
    }

    @Override
    public NBTTag[] getRelicNBTTags() {
        NBTTag[] parentNBTTags = super.getRelicNBTTags();
        NBTTag[] nbtTags = new NBTTag[parentNBTTags.length + 1];

        System.arraycopy(parentNBTTags, 0, nbtTags, 0, parentNBTTags.length);

        nbtTags[nbtTags.length-1] = lastActivationTimeTag;
        return nbtTags;
    }
}
