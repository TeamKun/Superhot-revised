package net.kunmc.lab.superhot.command;

import net.kunmc.lab.superhot.Config;
import net.kunmc.lab.superhot.GameManager;
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
                if (manager.isSuperhotEnabled()) {
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
                sender.sendMessage(ChatColor.GREEN + "Superhotを有効化しました.");
                break;
            case "stop":
                if (!manager.isSuperhotEnabled()) {
                    sender.sendMessage(ChatColor.RED + "Superhotは既に無効です.");
                    break;
                }

                manager.stop();
                sender.sendMessage(ChatColor.GREEN + "Superhotを無効化しました.");
                break;
            case "config":
                if (args.length < 3) {
                    sender.sendMessage(ChatColor.RED + "usage: /superhot config <configItem> <value>");
                    break;
                }

                switch (args[1]) {
                    case "AmmoAmount":
                        int amount;
                        try {
                            amount = Integer.parseInt(args[2]);
                        } catch (NumberFormatException e) {
                            sender.sendMessage(ChatColor.RED + args[2] + "は不正な値です.");
                            break;
                        }
                        if (amount < 1) {
                            sender.sendMessage(ChatColor.RED + "AmmoAmountの値は1以上の値を指定してください.");
                            break;
                        }

                        Config.ammoAmount = amount;
                        sender.sendMessage(ChatColor.GREEN + "AmmoAmountの値を" + amount + "に変更しました.");
                        break;
                    case "GlowMode":
                        Config.isGlowModeEnabled = Boolean.parseBoolean(args[2]);
                        sender.sendMessage(ChatColor.GREEN + "GlowModeの値を" + Config.isGlowModeEnabled + "に変更しました.");
                        break;
                    default:
                        sender.sendMessage(ChatColor.RED + "不明な項目です.");
                }
                break;
            default:
                sender.sendMessage(ChatColor.RED + "不明なコマンドです.");
        }
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        if (args.length == 1) {
            return Stream.of("start", "stop", "config").filter(x -> x.startsWith(args[0])).collect(Collectors.toList());
        }

        if (args.length == 2) {
            switch (args[0]) {
                case "start":
                    return Bukkit.getOnlinePlayers().stream().map(Player::getName).filter(x -> x.startsWith(args[1])).collect(Collectors.toList());
                case "config":
                    return Stream.of("AmmoAmount", "GlowMode").filter(x -> x.startsWith(args[1])).collect(Collectors.toList());
            }
        }

        if (args.length == 3) {
            switch (args[1]) {
                case "AmmoAmount":
                    return Collections.singletonList("<amount>");
                case "GlowMode":
                    return Stream.of("true", "false").filter(x -> x.startsWith(args[2])).collect(Collectors.toList());
            }
        }

        return Collections.emptyList();
    }
}
