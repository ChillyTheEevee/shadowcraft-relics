package world.sc2.shadowcraftrelics.commands;

import org.bukkit.NamespacedKey;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import world.sc2.config.Config;
import world.sc2.shadowcraftrelics.ShadowcraftRelics;
import world.sc2.utility.ChatUtils;
import world.sc2.utility.ItemUtils;

import java.util.ArrayList;
import java.util.List;

public class SetNextMorphableRelicStateCommand extends world.sc2.command.Command {

    private static final String SERIALIZATION_SUCCESSFUL_KEY = "messages.serialization_successful";
    private static final String CURRENT_STATE_STORAGE_SUCCESSFUL_KEY = "messages.current_state_storage_successful";
    private static final String WARNING_NON_PLAYER_SENDER_KEY = "messages.warning_non_player_sender";
    private static final String WARNING_INVALID_ITEM_KEY = "messages.warning_invalid_item";
    private static final String WARNING_INVALID_CURRENT_STATE_KEY = "messages.warning_invalid_current_state";

    private final NamespacedKey nextMorphStateKey;
    private ItemStack base;

    public SetNextMorphableRelicStateCommand(Config config, ShadowcraftRelics plugin) {
        super(config);

        nextMorphStateKey = new NamespacedKey(plugin, "nextMorphState");
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
            player.sendMessage(ChatUtils.chat(config.get().getString(CURRENT_STATE_STORAGE_SUCCESSFUL_KEY)));
            return true;
        } else if (string.equalsIgnoreCase("itemtoencode")) {
            if (base == null) {
                player.sendMessage(ChatUtils.chat(config.get().getString(WARNING_INVALID_CURRENT_STATE_KEY)));
                return true;
            }
            ItemMeta baseItemMeta = base.getItemMeta();
            PersistentDataContainer baseItemPDC = baseItemMeta.getPersistentDataContainer();

            baseItemPDC.set(nextMorphStateKey, PersistentDataType.BYTE_ARRAY, ItemUtils.serializeItemStack(heldItem));
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
