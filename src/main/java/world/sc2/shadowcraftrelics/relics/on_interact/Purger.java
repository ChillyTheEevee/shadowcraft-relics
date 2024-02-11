package world.sc2.shadowcraftrelics.relics.on_interact;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import world.sc2.config.Config;
import world.sc2.nbt.NBTTag;
import world.sc2.shadowcraftrelics.ShadowcraftRelics;
import world.sc2.shadowcraftrelics.relics.NBTStorageRelic;
import world.sc2.shadowcraftrelics.relics.Relic;
import world.sc2.shadowcraftrelics.relics.on_attack.TriggerOnDirectAttackRelic;
import world.sc2.utility.ItemUtils;

/**
 * A {@link Relic} that has the ability to morph between a sword and a bow.
 */
public class Purger extends Relic implements TriggerOnInteractRelic, TriggerOnDirectAttackRelic, NBTStorageRelic {

    private final NBTTag<byte[], byte[]> purgerStoredItemNBTTag;

    public Purger(String name, Config config, ShadowcraftRelics plugin) {
        super(name, config);

        var purgerStoredItemKey = new NamespacedKey(plugin, "storedPurgerItem");
        purgerStoredItemNBTTag = new NBTTag<>(purgerStoredItemKey, PersistentDataType.BYTE_ARRAY);
    }

    @Override
    public void onInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack currentPurger = player.getInventory().getItemInMainHand();
        Bukkit.getLogger().info("onInteract called for material " + currentPurger.getType());
        if (currentPurger.getType() == Material.BOW && event.getAction().isLeftClick()) {
            switchCurrentPurger(player, currentPurger);
        } else if (currentPurger.getType() != Material.BOW && event.getAction().isRightClick()) {
            switchCurrentPurger(player, currentPurger);
        }
    }

    @Override
    public void onAttack(EntityDamageByEntityEvent event) {
        Player player = ((Player) event.getDamager());
        ItemStack currentPurger = player.getEquipment().getItemInMainHand();
        Bukkit.getLogger().info("onAttack called for material " + currentPurger.getType());
        if (currentPurger.getType() == Material.BOW) {
            switchCurrentPurger(player, currentPurger);
        }
    }

    @Override
    public NBTTag[] getRelicNBTTags() {
        NBTTag[] nbtTags = new NBTTag[1];
        nbtTags[0] = purgerStoredItemNBTTag;
        return nbtTags;
    }

    private void switchCurrentPurger(Player player, ItemStack currentPurger) {
        ItemMeta currentPurgerMeta = currentPurger.getItemMeta();
        PersistentDataContainer currentPurgerPDC = currentPurgerMeta.getPersistentDataContainer();

        NamespacedKey purgerStoredItemKey = purgerStoredItemNBTTag.getNamespacedKey();

        if (!currentPurgerPDC.has(purgerStoredItemKey, PersistentDataType.BYTE_ARRAY)) {
            Bukkit.getLogger().severe("Purger cannot switch because Purger does not have purgerStoredItemKey!");
            return;
        }

        // Create stored Purger ItemStack
        ItemStack nextPurger = ItemUtils.deserializeItemStack(
                    currentPurgerPDC.get(purgerStoredItemKey, PersistentDataType.BYTE_ARRAY));

        ItemMeta nextPurgerMeta = nextPurger.getItemMeta();
        PersistentDataContainer nextPurgerPDC = nextPurgerMeta.getPersistentDataContainer();

        // remove next Purger's data from the current Purger's PDC
        currentPurgerPDC.remove(purgerStoredItemKey);
        currentPurger.setItemMeta(currentPurgerMeta);

        // Serialize and store current Purger inside the next Purger
        nextPurgerPDC.set(purgerStoredItemKey, PersistentDataType.BYTE_ARRAY, ItemUtils.serializeItemStack(currentPurger));

        nextPurger.setItemMeta(nextPurgerMeta);

        // Replace the current Purger with the next Purger
        player.getInventory().setItemInMainHand(nextPurger);

        // todo  a d d  D o p a m i n e  s o u n d

    }

}
