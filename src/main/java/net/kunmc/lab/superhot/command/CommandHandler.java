package net.kunmc.lab.superhot.command;

import net.kunmc.lab.superhot.Config;
import net.kunmc.lab.superhot.Const;
import net.kunmc.lab.superhot.GameManager;
import net.kunmc.lab.superhot.helper.ItemHelper;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
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
                    case "SwapMinDistance":
                        int minDistance;
                        try {
                            minDistance = Integer.parseInt(args[2]);
                        } catch (NumberFormatException e) {
                            sender.sendMessage(ChatColor.RED + args[2] + "は不正な値です.");
                            break;
                        }
                        if (minDistance < 1) {
                            sender.sendMessage(ChatColor.RED + "SwapMinDistanceの値は1以上の値を指定してください.");
                            break;
                        }

                        Config.swapMinDistance = minDistance;
                        sender.sendMessage(ChatColor.GREEN + "SwapMinDistanceの値を" + minDistance + "に変更しました.");
                        break;
                    case "SwapMaxDistance":
                        int maxDistance;
                        try {
                            maxDistance = Integer.parseInt(args[2]);
                        } catch (NumberFormatException e) {
                            sender.sendMessage(ChatColor.RED + args[2] + "は不正な値です.");
                            break;
                        }
                        if (maxDistance < 1) {
                            sender.sendMessage(ChatColor.RED + "SwapMaxDistanceの値は1以上の値を指定してください.");
                            break;
                        }

                        Config.swapMaxDistance = maxDistance;
                        sender.sendMessage(ChatColor.GREEN + "SwapMaxDistanceの値を" + maxDistance + "に変更しました.");
                        break;
                    default:
                        sender.sendMessage(ChatColor.RED + "不明な項目です.");
                }
                break;
            case "give":
                if (args.length < 3) {
                    sender.sendMessage(ChatColor.RED + "/superhot give <target> <item> [amount]");
                    break;
                }
                List<Player> playerList = Bukkit.selectEntities(sender, args[1]).stream()
                        .filter(x -> x instanceof Player)
                        .map(x -> ((Player) x))
                        .collect(Collectors.toList());
                if (playerList.isEmpty()) {
                    sender.sendMessage(ChatColor.RED + "プレイヤーが見つかりませんでした.");
                    break;
                }

                String itemName = args[2];
                ItemStack item = getSuperhotItem(itemName);
                if (item == null) {
                    sender.sendMessage(ChatColor.RED + "アイテムの名前が不正です.");
                    break;
                }

                int amount = 1;
                if (args.length > 3) {
                    try {
                        amount = Integer.parseInt(args[3]);
                    } catch (NumberFormatException e) {
                        sender.sendMessage(ChatColor.RED + "amountの値が不正です.");
                        break;
                    }
                    if (amount < 1) {
                        sender.sendMessage(ChatColor.RED + "amountの値は1以上の整数を指定してください.");
                        break;
                    }
                }
                item.setAmount(amount);

                playerList.forEach(x -> x.getInventory().addItem(item));
                sender.sendMessage(String.format(ChatColor.GREEN + "%d人のプレイヤーに%sを%d個配布しました.", playerList.size(), itemName, amount));
                break;
            default:
                sender.sendMessage(ChatColor.RED + "不明なコマンドです.");
        }
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        if (args.length == 1) {
            return Stream.of("start", "stop", "config", "give").filter(x -> x.startsWith(args[0])).collect(Collectors.toList());
        }

        if (args.length == 2) {
            switch (args[0]) {
                case "start":
                    return Bukkit.getOnlinePlayers().stream()
                            .map(Player::getName)
                            .filter(x -> x.startsWith(args[1]))
                            .collect(Collectors.toList());
                case "config":
                    return Stream.of("AmmoAmount", "GlowMode", "SwapMinDistance", "SwapMaxDistance")
                            .filter(x -> x.startsWith(args[1]))
                            .collect(Collectors.toList());
                case "give":
                    return Stream.concat(Bukkit.getOnlinePlayers().stream().map(Player::getName), Stream.of("@a", "@r"))
                            .filter(x -> x.startsWith(args[1]))
                            .collect(Collectors.toList());
            }
        }

        if (args.length == 3) {
            switch (args[0]) {
                case "config":
                    switch (args[1]) {
                        case "AmmoAmount":
                            return Collections.singletonList("<amount>");
                        case "GlowMode":
                            return Stream.of("true", "false").filter(x -> x.startsWith(args[2])).collect(Collectors.toList());
                        case "SwapMinDistance":
                        case "SwapMaxDistance":
                            return Collections.singletonList("<distance>");
                    }
                    break;
                case "give":
                    return Stream.of("SuperhotGun", "SuperhotAmmo", "SuperhotSwapTool")
                            .filter(x -> x.startsWith(args[2]))
                            .collect(Collectors.toList());
            }
        }

        if (args.length == 4) {
            if (args[0].equals("give")) {
                return Collections.singletonList("[amount]");
            }
        }

        return Collections.emptyList();
    }

    private ItemStack getSuperhotItem(String itemName) {
        switch (itemName) {
            case "SuperhotGun":
                return ItemHelper.createItem(Const.gunMaterial, Const.gunName, 1);
            case "SuperhotAmmo":
                return ItemHelper.createItem(Const.ammoMaterial, Const.ammoName, 1);
            case "SuperhotSwapTool":
                return ItemHelper.createItem(Const.swapToolMaterial, Const.swapToolName, 1);
            default:
                return null;
        }
    }
}
