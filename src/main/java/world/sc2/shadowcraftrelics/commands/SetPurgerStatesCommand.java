package world.sc2.shadowcraftrelics.commands;

import org.bukkit.ChatColor;
import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import world.sc2.shadowcraftrelics.ShadowcraftRelics;
import world.sc2.shadowcraftrelics.util.ItemUtils;

import java.util.ArrayList;
import java.util.List;

public class SetPurgerStatesCommand implements CommandExecutor, TabCompleter {

    private final NamespacedKey purgerStoredItemKey;
    private ItemStack base;

    public SetPurgerStatesCommand(ShadowcraftRelics plugin) {
        purgerStoredItemKey = new NamespacedKey(plugin, "storedPurgerItem");
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length != 1) {
            return false;
        }
        if (!(sender instanceof Player player)) {
            sender.sendMessage(ChatColor.RED + "Only players can use this command!");
            return true;
        }
        ItemStack heldItem = player.getInventory().getItemInMainHand();
        if (ItemUtils.isAirOrNull(heldItem)) {
            player.sendMessage(ChatColor.RED + "You must be holding a valid item!");
            return true;
        }

        if (args[0].equalsIgnoreCase("base")) {
            base = heldItem;
            player.sendMessage(ChatColor.GREEN + "Successfully set sword as base state!");
            return true;
        } else if (args[0].equalsIgnoreCase("itemtoencode")) {
            if (base == null) {
                player.sendMessage(ChatColor.RED + "You have not yet defined the base state to encode this item into!");
                return true;
            }
            ItemMeta baseItemMeta = base.getItemMeta();
            PersistentDataContainer baseItemPDC = baseItemMeta.getPersistentDataContainer();

            baseItemPDC.set(purgerStoredItemKey, PersistentDataType.BYTE_ARRAY, ItemUtils.serializeItemStack(heldItem));
            base.setItemMeta(baseItemMeta);

            player.sendMessage(ChatColor.GREEN + "Serialized held item into the Persistent Data Container of base!");
            player.getInventory().remove(heldItem);
            return true;
        }

        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length != 1)
            return null;
        List<String> list = new ArrayList<>();
        list.add("base");
        list.add("itemtoencode");
        return list;
    }

}
