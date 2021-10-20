package com.josemarcellio.playercosmetics.commands;

import com.josemarcellio.playercosmetics.manager.PlayerCosmeticsManager;
import com.josemarcellio.playercosmetics.utils.PlayerCosmeticsUtils;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class CosmeticsReload implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(sender.hasPermission("playercosmetics.admin")) {
            //sender.sendMessage(ChatColor.YELLOW + "Cosmetics Reloaded!");
               // sender.sendMessage(LegacyComponentSerializer.builder().build().serialize(PlayerCosmeticsUtils.getUserFriendlyRefreshedCosmeticsList()));
                new PlayerCosmeticsManager().getCosmeticsFromConfig();
                sender.sendMessage( ChatColor.translateAlternateColorCodes ('&', "&eConfig Reloaded!"));

        }
        return false;
    }
}
