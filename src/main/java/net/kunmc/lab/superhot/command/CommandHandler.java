package net.kunmc.lab.superhot.command;

import net.kunmc.lab.superhot.GameManager;
import net.kunmc.lab.superhot.SuperhotState;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CommandHandler implements CommandExecutor, TabCompleter {
    private final GameManager manager = GameManager.getInstance();

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length < 1) return false;

        switch (args[0]) {
            case "start":
                if (SuperhotState.isEnabled) {
                    sender.sendMessage(ChatColor.RED + "Superhotは既に有効です.");
                    break;
                }

                if (args.length < 2) {
                    sender.sendMessage(ChatColor.RED + "usage: /superhot start <player>");
                    break;
                }

                Player target = Bukkit.getPlayer(args[1]);
                if (target == null) {
                    sender.sendMessage(ChatColor.RED + args[1] + "はオフラインです.");
                    break;
                }

                manager.start(target);
                break;
            case "stop":
                if (!SuperhotState.isEnabled) {
                    sender.sendMessage(ChatColor.RED + "Superhotは既に無効です.");
                    break;
                }

                manager.stop();
                break;
            default:
                sender.sendMessage(ChatColor.RED + "不明なコマンドです.");
        }
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        if (args.length == 1)
            return Stream.of("start", "stop").filter(x -> x.startsWith(args[0])).collect(Collectors.toList());

        if (args.length == 2 && args[0].equalsIgnoreCase("start"))
            return Bukkit.getOnlinePlayers().stream().map(Player::getName).collect(Collectors.toList());

        return Collections.emptyList();
    }
}
