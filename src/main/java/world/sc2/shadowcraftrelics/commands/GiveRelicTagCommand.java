package world.sc2.shadowcraftrelics.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import world.sc2.shadowcraftrelics.managers.RelicManager;
import world.sc2.shadowcraftrelics.relics.Relic;
import world.sc2.shadowcraftrelics.util.ItemUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class GiveRelicTagCommand implements CommandExecutor, TabCompleter {

    private final RelicManager relicManager;

    public GiveRelicTagCommand(RelicManager relicManager) {
        this.relicManager = relicManager;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command,
                             @NotNull String label, @NotNull String[] args) {
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

        Relic wantedRelic = relicManager.getRelicFromName(args[0]);

        if (wantedRelic == null) {
            player.sendMessage(ChatColor.RED + "\"" + args[0] + "\" is not a valid relic name!");
            return true;
        }

        relicManager.applyRelicNBTTags(heldItem, wantedRelic);

        player.sendMessage(ChatColor.GREEN + "NBT Tags successfully applied!");

        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command,
                                                @NotNull String label, @NotNull String[] args) {
        if (args.length != 1)
            return null;
        Collection<Relic> allRelics = relicManager.getRelicsMatchingFilter(r -> true);
        ArrayList<String> allRelicNames = new ArrayList<>();
        for (Relic relic : allRelics) {
            allRelicNames.add(relic.getName());
        }
        return allRelicNames;
    }
}
