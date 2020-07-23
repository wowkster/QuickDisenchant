package com.adrianwowk.quickdisenchant.commands;

import com.adrianwowk.quickdisenchant.QuickDisenchant;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class CommandHandler implements CommandExecutor {
    public boolean onCommand(final CommandSender sender, final Command cmd, final String label, final String[] args) {
        if (sender instanceof Player) {
            try {
                playerDisenchantCommand((Player)sender, cmd, args);
            }
            catch (InstantiationException | IllegalAccessException ex2) {
                final ReflectiveOperationException ex = null;
                final ReflectiveOperationException e = ex;
                e.printStackTrace();
            }
        }
        else {
            Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "You must be a player to utilize these commands!");
        }
        return true;
    }
    public boolean playerDisenchantCommand(final Player p, final Command cmd, final String[] args) throws InstantiationException, IllegalAccessException {
        if (cmd.getName().equalsIgnoreCase("disenchant")) {
            if (cmd.getName().equalsIgnoreCase("disenchant") && args.length >= 2) {
                Player target = Bukkit.getPlayer(args[0]);
                if (p.hasPermission("quickdisenchant.disenchant")) {
                    // invalid argument enchantment
                    String[] values = args[1].split(":");
                    if (values.length != 2){
                        p.sendMessage(QuickDisenchant.prefix + ChatColor.YELLOW +
                                "The specified enchantment is invalid. Usage: /disenchant <target> <enchantment>");
                        return false;
                    }
                    Enchantment enchant = Enchantment.getByKey(new NamespacedKey(values[0], values[1]));
                    if (enchant == null) {
                        p.sendMessage(QuickDisenchant.prefix + ChatColor.YELLOW +
                                "The specified enchantment is invalid. Usage: /disenchant <target> <enchantment>");
                    } else {
                        // good
                        if (p.getInventory().getItemInMainHand().containsEnchantment(enchant)){
                            p.getInventory().getItemInMainHand().removeEnchantment(enchant);
                            p.playSound(p.getLocation(), Sound.BLOCK_GRINDSTONE_USE, SoundCategory.PLAYERS, 1.0f, 1.0f);
                            if (p.getInventory().getItemInMainHand().getItemMeta().hasDisplayName()) {
                                p.sendMessage(QuickDisenchant.prefix + ChatColor.YELLOW + "Removed " + ChatColor.AQUA + args[1] + ChatColor.YELLOW + " from "
                                        + p.getInventory().getItemInMainHand().getItemMeta().getDisplayName());
                            } else {
                                p.sendMessage(QuickDisenchant.prefix + ChatColor.YELLOW + "Removed " + ChatColor.AQUA + args[1]);
                            }
                        } else {
                            p.sendMessage(QuickDisenchant.prefix + ChatColor.YELLOW +
                                    "The specified item does not conatin the specified enchantment.");

                        }

                    }

                } else {
                    p.sendMessage(QuickDisenchant.prefix + ChatColor.YELLOW + "You do not have permission to use this command");
                }
            } else if (cmd.getName().equalsIgnoreCase("disenchant") && args.length == 1) {
                if (p.hasPermission("quickdisenchant.disenchant")) {
                    //command inputed wrong
                    if (Bukkit.getPlayerExact(args[0]) != null){
                        // no enchantments or improper usage
                        Player target = Bukkit.getPlayer(args[0]);
                        if (/* has enchants */ !target.getInventory().getItemInMainHand().getEnchantments().isEmpty()){
                            // improper usage
                            p.sendMessage(QuickDisenchant.prefix + ChatColor.YELLOW +
                                    "Improper Usage of command. Proper Usage: /disenchant <target> <enchantment>");
                        } else {
                            // no enchants
                            p.sendMessage(QuickDisenchant.prefix + ChatColor.YELLOW +
                                    "The selected item is not enchanted");
                        }

                    } else {
                        // player invalid
                        p.sendMessage(QuickDisenchant.prefix + ChatColor.YELLOW +
                                "Selected target " + ChatColor.AQUA + args[0] + ChatColor.YELLOW + " could not be found");
                    }

                } else {
                    //invalid perms
                    p.sendMessage(QuickDisenchant.prefix + ChatColor.YELLOW + "You do not have permission to use this command");
                }
            } else if (cmd.getName().equalsIgnoreCase("disenchant") && args.length == 0) {
                if (p.hasPermission("quickdisenchant.disenchant")) {
                    p.sendMessage(QuickDisenchant.prefix + ChatColor.YELLOW + "This command removes a specified enchantment from an item. Usage: /disenchant <target> <enchantment>");
                } else {
                    p.sendMessage(QuickDisenchant.prefix + ChatColor.YELLOW + "You do not have permission to use this command");
                }
            }
        }
        return false;
    }
}
