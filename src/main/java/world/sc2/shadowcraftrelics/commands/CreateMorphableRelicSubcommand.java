package world.sc2.shadowcraftrelics.commands;

import org.bukkit.NamespacedKey;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.persistence.PersistentDataType;
import world.sc2.command.subcommand.Subcommand;
import world.sc2.config.Config;
import world.sc2.nbt.NBTTag;
import world.sc2.shadowcraftrelics.ShadowcraftRelics;
import world.sc2.shadowcraftrelics.relics.morphable_relic.MorphableRelic;
import world.sc2.utility.ChatUtils;
import world.sc2.utility.ItemUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.LinkedList;
import java.util.List;

/**
 * A {@link Subcommand} for merging multiple Relic states into one singular {@link MorphableRelic}. Debug command.
 */
public class CreateMorphableRelicSubcommand extends Subcommand {
    private static final String SUCCESS_KEY = "messages.success";
    private static final String WARNING_NON_PLAYER_SENDER_KEY = "messages.warning_non_player_sender";
    private static final String WARNING_INVALID_NUMBER_OF_ITEMS_KEY = "messages.warning_invalid_number_of_items";
    private final NBTTag<byte[], byte[]> morphableRelicQueueTag;

    public CreateMorphableRelicSubcommand(Config config, ShadowcraftRelics plugin) {
        super(config);

        this.morphableRelicQueueTag = new NBTTag<>(
                new NamespacedKey(plugin, "morphableRelicQueue"), PersistentDataType.BYTE_ARRAY);
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
        LinkedList<ItemStack> relicStatesQueue = new LinkedList<>();

        // Adds all items in the hot bar into relicStates
        for (int i = 0; i < 9; i++) {
            ItemStack item = playerInventory.getItem(i);
            if (!ItemUtils.isAirOrNull(item)) {
                relicStatesQueue.add(playerInventory.getItem(i));
            }
        }

        if (relicStatesQueue.size() < 2) {
            player.sendMessage(ChatUtils.chat(config.get().getString(WARNING_INVALID_NUMBER_OF_ITEMS_KEY)));
            return true;
        }

        LinkedList<byte[]> serializedRelicStatesQueue = new LinkedList<>();

        for (ItemStack relicState : relicStatesQueue) {
            serializedRelicStatesQueue.add(ItemUtils.serializeItemStack(relicState));
        }
        serializedRelicStatesQueue.poll(); // remove base ItemStack from the Queue

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try (ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream)) {
            objectOutputStream.writeObject(serializedRelicStatesQueue);
        } catch (IOException e) {
            e.printStackTrace(); // todo more robust logging
            return true;
        }

        ItemStack firstState = relicStatesQueue.peek();
        morphableRelicQueueTag.applyTag(firstState, byteArrayOutputStream.toByteArray());

        // Update player's hot bar
        playerInventory.setItem(0, firstState);
        for (int i = 1; i < 9; i++) {
            playerInventory.setItem(i, null);
        }

        player.sendMessage(ChatUtils.chat(config.get().getString(SUCCESS_KEY)));

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, String[] args) {
        return null;
    }

}
