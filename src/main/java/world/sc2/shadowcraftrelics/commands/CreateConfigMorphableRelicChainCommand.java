package world.sc2.shadowcraftrelics.commands;

import org.bukkit.NamespacedKey;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.persistence.PersistentDataType;
import world.sc2.config.Config;
import world.sc2.config.ConfigManager;
import world.sc2.nbt.NBTTag;
import world.sc2.shadowcraftrelics.ShadowcraftRelics;
import world.sc2.utility.ChatUtils;
import world.sc2.utility.ItemUtils;

import java.util.ArrayList;
import java.util.List;

public class CreateConfigMorphableRelicChainCommand extends world.sc2.command.Command {

    private static final String MORPHABLE_RELIC_DATA_DIRECTORY = "data/morphableRelics/";
    private static final String RELIC_STATE_LIST_CONFIG_KEY = "relicStates";

    private static final String NUM_ENTRIES_KEY = "numEntries";

    private static final String SUCCESS_KEY = "messages.success";
    private static final String WARNING_NON_PLAYER_SENDER_KEY = "messages.warning_non_player_sender";
    private static final String WARNING_INVALID_NUMBER_OF_ITEMS_KEY = "messages.warning_invalid_number_of_items";

    private static final String DATA_FILE_NAME = "list";

    private final Config numEntriesConfig;
    private final ConfigManager configManager;
    private final NBTTag<String, String> morphConfigIDTag;
    private final NBTTag<Integer, Integer> morphIndexTag;

    public CreateConfigMorphableRelicChainCommand(Config config, ShadowcraftRelics plugin, ConfigManager configManager) {
        super(config);

        morphConfigIDTag = new NBTTag<>(new NamespacedKey(plugin, "morphConfigID"),
                PersistentDataType.STRING);
        morphIndexTag = new NBTTag<>(new NamespacedKey(plugin, "morphIndex"),
                PersistentDataType.INTEGER);

        this.numEntriesConfig = configManager.getConfig(MORPHABLE_RELIC_DATA_DIRECTORY + "numEntries.yml");
        this.configManager = configManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, String[] args) {
        if (args.length != 1) {
            return false;
        }

        if (!(sender instanceof Player player)) {
            sender.sendMessage(ChatUtils.chat(config.get().getString(WARNING_NON_PLAYER_SENDER_KEY)));
            return true;
        }

        PlayerInventory playerInventory = player.getInventory();
        List<ItemStack> relicStates = new ArrayList<>();

        // Adds all items in the hot bar into relicStates
        for (int i = 0; i < 9; i++) {
            ItemStack item = playerInventory.getItem(i);
            if (!ItemUtils.isAirOrNull(item)) {
                relicStates.add(playerInventory.getItem(i));
            }
        }

        if (relicStates.size() < 2) {
            player.sendMessage(ChatUtils.chat(config.get().getString(WARNING_INVALID_NUMBER_OF_ITEMS_KEY)));
            return true;
        }

        int numEntries = numEntriesConfig.get().getInt(NUM_ENTRIES_KEY);

        String newListConfigName = DATA_FILE_NAME + numEntries + ".yml";

        numEntriesConfig.set(NUM_ENTRIES_KEY, numEntries + 1);

        for (int i = 0; i < relicStates.size(); i++) {
            ItemStack relicState = relicStates.get(i);

            morphConfigIDTag.applyTag(relicState, newListConfigName);
            morphIndexTag.applyTag(relicState, i);
        }

        player.sendMessage(ChatUtils.chat(config.get().getString(SUCCESS_KEY)));

        Config newConfig = configManager.getConfig(MORPHABLE_RELIC_DATA_DIRECTORY + newListConfigName);

        List<byte[]> serializedRelicStates = new ArrayList<>(relicStates.size());

        for (ItemStack relicState : relicStates) {
            serializedRelicStates.add(ItemUtils.serializeItemStack(relicState));
        }

        newConfig.set(RELIC_STATE_LIST_CONFIG_KEY, serializedRelicStates);

        // Update player's hot bar
        playerInventory.setItem(0, relicStates.get(0));
        for (int i = 1; i < 9; i++) {
            playerInventory.setItem(i, null);
        }

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, String[] args) {
        return null;
    }

}
