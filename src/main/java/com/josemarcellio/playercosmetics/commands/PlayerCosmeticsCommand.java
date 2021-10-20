package com.josemarcellio.playercosmetics.commands;

import com.josemarcellio.playercosmetics.manager.PlayerCosmeticsManager;
import com.josemarcellio.playercosmetics.utils.Cosmetics;
import com.josemarcellio.playercosmetics.utils.PlayerCosmeticsUtils;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class PlayerCosmeticsCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length == 0) {
            sender.sendMessage ( ChatColor.translateAlternateColorCodes ( '&', "&bPlayerCosmetics by JoseMarcellio" ) );
            sender.sendMessage ( ChatColor.translateAlternateColorCodes ( '&', "&7- &b/playercosmetics give <cosmetics> <player>" ) );
            sender.sendMessage ( ChatColor.translateAlternateColorCodes ( '&', "&7- &b/playercosmetics set <modeldata>" ) );
            sender.sendMessage ( ChatColor.translateAlternateColorCodes ( '&', "&7- &b/playercosmetics reload" ) );
        }
        if (args.length == 1) {
            if (args[0].equalsIgnoreCase ( "reload" )) {
                if (sender.hasPermission ( "playercosmetics.admin" )) {
                    new PlayerCosmeticsManager ().getCosmeticsFromConfig ();
                    sender.sendMessage ( ChatColor.translateAlternateColorCodes ( '&', "&7[&bPlayerCosmetics&7] &bSuccessfully reload config!" ) );
                    return true;
                }
            }
        }
        if (args.length == 3) {
        if (args[0].equalsIgnoreCase ( "give" )) {
            if (sender.hasPermission ( "playercosmetics.admin" )) {
                Cosmetics cosmetic = PlayerCosmeticsUtils.getCosmeticFromName ( args[1] );
                ItemStack cosmeticItem;
                Player toPlayer;

                if (args.length == 3) {
                    toPlayer = Bukkit.getPlayer ( args[2] );
                } else if (sender instanceof Player) {
                    toPlayer = (Player) sender;
                } else {
                    sender.sendMessage ( ChatColor.translateAlternateColorCodes ( '&', "&7[&bPlayerCosmetics&7] &bYou can only give cosmetics to players!" ) );
                    return true;
                }

                if (toPlayer == null) {
                    sender.sendMessage ( ChatColor.translateAlternateColorCodes ( '&', "&7[&bPlayerCosmetics&7] &bPlayer not found or is not online!" ) );
                    return true;
                }

                if (cosmetic == null) {
                    sender.sendMessage ( ChatColor.translateAlternateColorCodes ( '&', "&7[&bPlayerCosmetics&7] &bCan't find cosmetics or cosmetics not found!" ) );
                    return true;
                }
                cosmeticItem = cosmetic.getCosmeticItemStack ();
                toPlayer.getInventory ().addItem ( cosmeticItem );
                sender.sendMessage ( ChatColor.translateAlternateColorCodes ( '&', "&7[&bPlayerCosmetics&7] &bGive cosmetics to " + toPlayer.getName () ) );
                return true;
            }
        }
        }
        if (args.length == 2 && PlayerCosmeticsUtils.isInt ( args[1] )) {
        if (args[0].equalsIgnoreCase ( "set" )) {
            if (sender.hasPermission ( "playercosmetics.admin" )) {
                if (sender instanceof Player) {
                    Player p = (Player) sender;
                    if (p.getInventory ().getItemInMainHand () == null || p.getInventory ().getItemInMainHand ().getType ().equals ( Material.AIR )) {
                        p.sendMessage ( ChatColor.translateAlternateColorCodes ( '&', "&7[&bPlayerCosmetics&7] &bYou can't set item model data to null" ) );
                    } else {
                            Integer argAsInt = Integer.parseInt ( args[1] );
                            ItemStack i = p.getItemInHand ();
                            ItemMeta im = i.getItemMeta ();
                            Objects.requireNonNull ( im ).setCustomModelData ( argAsInt );
                            i.setItemMeta ( im );
                            p.sendMessage ( ChatColor.translateAlternateColorCodes ( '&', "&7[&bPlayerCosmetics&7] &bSet item model data to " + argAsInt ) );
                            return true;
                        }
                }
            }
        }
        }
        return true;
    }
}
