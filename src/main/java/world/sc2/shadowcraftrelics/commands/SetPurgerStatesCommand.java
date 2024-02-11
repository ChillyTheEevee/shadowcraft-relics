package world.sc2.shadowcraftrelics.commands;

import org.bukkit.NamespacedKey;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import world.sc2.command.Command;
import world.sc2.config.Config;
import world.sc2.shadowcraftrelics.ShadowcraftRelics;
import world.sc2.utility.ChatUtils;
import world.sc2.utility.ItemUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * A debug {@link Command} that grants Purger tags to ItemStacks.
 * @author ChillyTheEevee
 */
public class SetPurgerStatesCommand extends world.sc2.command.Command {

    private static final String SERIALIZATION_SUCCESSFUL_KEY = "messages.serialization_successful";
    private static final String BASE_STORAGE_SUCCESSFUL_KEY = "messages.base_storage_successful";
    private static final String WARNING_NON_PLAYER_SENDER_KEY = "messages.warning_non_player_sender";
    private static final String WARNING_INVALID_ITEM_KEY = "messages.warning_invalid_item";
    private static final String WARNING_INVALID_BASE_STATE_KEY = "messages.warning_invalid_base_state";

    private final NamespacedKey purgerStoredItemKey;
    private ItemStack base;

    public SetPurgerStatesCommand(Config config, ShadowcraftRelics plugin) {
        super(config);

        purgerStoredItemKey = new NamespacedKey(plugin, "storedPurgerItem");
    }

    @Override
    public boolean onCommand(CommandSender sender, String[] args) {
        if (args.length != 2) {
            return false;
        }
        if (!(sender instanceof Player player)) {
            sender.sendMessage(ChatUtils.chat(config.get().getString(WARNING_NON_PLAYER_SENDER_KEY)));
            return true;
        }
        ItemStack heldItem = player.getInventory().getItemInMainHand();
        if (ItemUtils.isAirOrNull(heldItem)) {
            player.sendMessage(ChatUtils.chat(config.get().getString(WARNING_INVALID_ITEM_KEY)));
            return true;
        }

        String string = args[1];

        if (string.equalsIgnoreCase("base")) {
            base = heldItem;
            player.sendMessage(ChatUtils.chat(config.get().getString(BASE_STORAGE_SUCCESSFUL_KEY)));
            return true;
        } else if (string.equalsIgnoreCase("itemtoencode")) {
            if (base == null) {
                player.sendMessage(ChatUtils.chat(config.get().getString(WARNING_INVALID_BASE_STATE_KEY)));
                return true;
            }
            ItemMeta baseItemMeta = base.getItemMeta();
            PersistentDataContainer baseItemPDC = baseItemMeta.getPersistentDataContainer();

            baseItemPDC.set(purgerStoredItemKey, PersistentDataType.BYTE_ARRAY, ItemUtils.serializeItemStack(heldItem));
            base.setItemMeta(baseItemMeta);

            player.sendMessage(ChatUtils.chat(config.get().getString(SERIALIZATION_SUCCESSFUL_KEY)));
            player.getInventory().remove(heldItem);
            return true;
        }

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, String[] args) {
        if (args.length != 2)
            return null;
        List<String> list = new ArrayList<>();
        list.add("base");
        list.add("itemtoencode");
        return list;
    }

}
