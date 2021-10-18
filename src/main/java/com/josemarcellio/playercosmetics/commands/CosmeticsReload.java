package com.josemarcellio.playercosmetics.commands;

import com.josemarcellio.playercosmetics.utils.PlayerCosmeticsUtils;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class CosmeticsReload implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(sender.hasPermission("playercosmetics.admin")) {
            //sender.sendMessage(ChatColor.YELLOW + "Cosmetics Reloaded!");
            if(PlayerCosmeticsUtils.isSpigot()) {
                sender.sendMessage(LegacyComponentSerializer.builder().build().serialize(PlayerCosmeticsUtils.getUserFriendlyRefreshedCosmeticsList()));
            } else {
                sender.sendMessage(PlayerCosmeticsUtils.getUserFriendlyRefreshedCosmeticsList());
            }
            return true;
        }
        return false;
    }
}
