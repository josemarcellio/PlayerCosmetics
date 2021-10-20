package com.josemarcellio.playercosmetics.commands;

import com.josemarcellio.playercosmetics.utils.PlayerCosmeticsUtils;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

public class CosmeticsSet implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender.hasPermission ( "playercosmetics.admin" )) {
            Player p = (Player) sender;
            if (sender instanceof Player) {
                if (p.getInventory ().getItemInMainHand () == null || p.getInventory ().getItemInMainHand ().getType ().equals ( Material.AIR )) {
                    p.sendMessage ( ChatColor.translateAlternateColorCodes ( '&', "&eYou can't set modeldata to AIR" ) );
                } else {
                    if (args.length == 1 && PlayerCosmeticsUtils.isInt ( args[0] )) {
                        Integer argAsInt = Integer.parseInt ( args[0] );
                        //sender.sendMessage(ChatColor.YELLOW + "Cosmetics Reloaded!");
                        // sender.sendMessage(LegacyComponentSerializer.builder().build().serialize(PlayerCosmeticsUtils.getUserFriendlyRefreshedCosmeticsList()));
                        ItemStack i = p.getItemInHand ();
                        ItemMeta im = i.getItemMeta ();
                        im.setCustomModelData ( argAsInt );
                        i.setItemMeta ( im );
                        p.sendMessage ( ChatColor.translateAlternateColorCodes ( '&', "&eSet Modeldata to " + argAsInt ) );
                    }
                }
            }
        }
        return false;
    }
}
