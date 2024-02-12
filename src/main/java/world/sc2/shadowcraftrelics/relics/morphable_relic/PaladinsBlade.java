package world.sc2.shadowcraftrelics.relics.morphable_relic;

import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import world.sc2.config.Config;
import world.sc2.config.ConfigManager;
import world.sc2.nbt.NBTTag;
import world.sc2.shadowcraftrelics.events.RelicMorphEvent;
import world.sc2.shadowcraftrelics.relics.NBTStorageRelic;
import world.sc2.shadowcraftrelics.relics.on_interact.TriggerOnInteractRelic;

import java.time.Instant;

public class PaladinsBlade extends ConfigMorphableRelic implements TriggerOnInteractRelic, NBTStorageRelic {

    private final String COOLDOWN_IN_SECONDS_KEY = "uniqueProperties.cooldown_in_seconds";
    private final NBTTag<Long, Long> lastActivationTimeTag;

    public PaladinsBlade(String name, Config config, ConfigManager configManager,
                         NBTTag<String, String> morphConfigIDTag, NBTTag<Integer, Integer> morphIndexTag,
                         NBTTag<Long, Long> lastActivationTimeTag) {
        super(name, config, configManager, morphConfigIDTag, morphIndexTag);

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
                morph(relicMorphEvent);
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
