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

import java.util.List;

public class CreateRelic implements CommandExecutor, TabCompleter {

    private final RelicManager relicManager;

    public CreateRelic(RelicManager relicManager) {
        this.relicManager = relicManager;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (strings.length != 1) {
            commandSender.sendMessage(ChatColor.RED + "Invalid arguments.");
            return true;
        }
        if (!(commandSender instanceof Player player)) {
            commandSender.sendMessage(ChatColor.RED + "Only players can use this command!");
            return true;
        }

        ItemStack relic = relicManager.createRelic(strings[0]);
        if (relic == null) {
            player.sendMessage(ChatColor.RED + "Invalid relic!");
            return true;
        }

        player.getInventory().addItem(relic);
        player.sendMessage(ChatColor.GREEN + "Relic created!");

        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        return null;
    }
}
