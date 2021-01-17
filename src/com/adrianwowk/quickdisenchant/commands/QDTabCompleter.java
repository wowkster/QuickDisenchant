package com.adrianwowk.quickdisenchant.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EnchantingInventory;

import java.util.ArrayList;
import java.util.List;

public class QDTabCompleter implements TabCompleter {
    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String alias, String[] args) {
        // /disenchant <target> <enchantment>
        if (cmd.getName().equalsIgnoreCase("disenchant") && args.length <= 1){
            Player p = (Player) sender;
            List<String> list = new ArrayList<>();
            if (p.hasPermission("quickdisenchant.disenchant")){
                // Get item in hand and display enchantments
                for (Player player : Bukkit.getOnlinePlayers()) {
                    list.add(player.getDisplayName());
                }
                list.add("reload");
            }

            return list;
        }
        else if (cmd.getName().equalsIgnoreCase("disenchant") && args.length <= 2){
            if (sender instanceof Player){
                Player p = (Player) sender;
                Player target = Bukkit.getPlayer(args[0]);
                List<String> list = new ArrayList<>();
                if (p.hasPermission("quickdisenchant.disenchant")){
                    // Get item in hand and display enchantments
                    //list.add("give");
                    if (Bukkit.getPlayerExact(args[0]) == null){
                        return null;
                    }
                    for (Enchantment ench : target.getInventory().getItemInMainHand().getEnchantments().keySet()){
                        list.add(ench.getKey().toString());
                    }
                }

                return list;
            }
        }
        return null;
    }
}
