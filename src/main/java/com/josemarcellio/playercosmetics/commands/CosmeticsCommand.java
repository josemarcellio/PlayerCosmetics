package com.josemarcellio.playercosmetics.commands;

import com.josemarcellio.playercosmetics.utils.Cosmetics;
import com.josemarcellio.playercosmetics.utils.PlayerCosmeticsUtils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import static net.kyori.adventure.text.format.NamedTextColor.GOLD;
import static net.kyori.adventure.text.format.NamedTextColor.YELLOW;

/**
 * Basically a testing command to give the specified Cosmetic to the player
 *
 */
public class CosmeticsCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(sender.hasPermission("playercosmetics.admin") && (args.length == 1 || args.length == 2)) {
            Cosmetics cosmetic = PlayerCosmeticsUtils.getCosmeticFromName(args[0]);
            ItemStack cosmeticItem;
            Player toPlayer;

            if (args.length == 2) {
                toPlayer = Bukkit.getPlayer(args[1]);
            } else if (sender instanceof Player) {
                toPlayer = (Player) sender;
            } else {
                sender.sendMessage(ChatColor.YELLOW + "You can only give cosmetics to players!");
                return true;
            }

            if(toPlayer == null) {
                sender.sendMessage(ChatColor.YELLOW + "Player not found!");
                return true;
            }

            if(cosmetic == null) {
                sender.sendMessage(ChatColor.YELLOW + "Cosmetics not found!");
                return true;
            }

            cosmeticItem = cosmetic.getCosmeticItemStack();
            toPlayer.getInventory().addItem(cosmeticItem);
            Component gave = Component.text("Give cosmetics").color(YELLOW)
                    .append(Component.text(" to ").color(TextColor.color(YELLOW))
                            .append(Component.text(toPlayer.getName()).color(TextColor.color(GOLD))));
            if(PlayerCosmeticsUtils.isSpigot()) {
                sender.sendMessage(LegacyComponentSerializer.builder().build().serialize(gave));
            } else {
                sender.sendMessage(gave);
            }
            return true;
        }
        return false;
    }
}
