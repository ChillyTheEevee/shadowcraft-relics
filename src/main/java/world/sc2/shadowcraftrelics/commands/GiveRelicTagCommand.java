package world.sc2.shadowcraftrelics.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import world.sc2.command.Command;
import world.sc2.config.Config;
import world.sc2.shadowcraftrelics.managers.RelicManager;
import world.sc2.shadowcraftrelics.relics.Relic;
import world.sc2.utility.ChatUtils;
import world.sc2.utility.ItemUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class GiveRelicTagCommand extends Command {

    private final static String APPLICATION_SUCCESSFUL_KEY = "messages.application_successful";
    private final static String WARNING_NON_PLAYER_SENDER_KEY = "messages.warning_non_player_sender";
    private final static String WARNING_INVALID_ITEM_KEY = "messages.warning_invalid_item";
    private final static String WARNING_INVALID_RELIC_TAG_KEY = "messages.warning_invalid_relic_tag";
    private final RelicManager relicManager;

    public GiveRelicTagCommand(Config config, RelicManager relicManager) {
        super(config);
        this.relicManager = relicManager;
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

        Relic wantedRelic = relicManager.getRelicFromName(args[1]);

        if (wantedRelic == null) {
            player.sendMessage(String.format(ChatUtils.chat(config.get().getString(WARNING_INVALID_RELIC_TAG_KEY))));
            return true;
        }

        relicManager.applyRelicNBTTags(heldItem, wantedRelic);

        player.sendMessage(ChatUtils.chat(config.get().getString(APPLICATION_SUCCESSFUL_KEY)));

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, String[] args) {
        if (args.length != 2)
            return null;
        Collection<Relic> allRelics = relicManager.getRelicsMatchingFilter(r -> true);
        ArrayList<String> allRelicNames = new ArrayList<>();
        for (Relic relic : allRelics) {
            allRelicNames.add(relic.getName());
        }
        return allRelicNames;
    }
}
