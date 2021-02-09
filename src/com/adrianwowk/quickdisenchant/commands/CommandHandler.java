package com.adrianwowk.quickdisenchant.commands;

import com.adrianwowk.quickdisenchant.QuickDisenchant;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

public class CommandHandler implements CommandExecutor {

    private QuickDisenchant instance;

    public CommandHandler(QuickDisenchant plugin){
        this.instance = plugin;
    }

    public boolean onCommand(final CommandSender sender, final Command cmd, final String label, final String[] args) {

        if (cmd.getName().equalsIgnoreCase("disenchant")) {
            if (sender instanceof Player)
                playerDisenchantCommand((Player) sender, args);
            else
                Bukkit.getConsoleSender().sendMessage(instance.translate("messages.command.invalid.console"));
            return true;
        }
        return false;
    }

    public void playerDisenchantCommand(final Player p, final String[] args) {

        if (args.length == 0) {

            if (!p.hasPermission("quickdisenchant.disenchant")) {
                // no perms
                p.sendMessage(instance.translate("messages.command.invalid.no-permission"));
                return;
            }

            // invalid command
            p.sendMessage(instance.translate("messages.command.description"));
            return;
        } else if (args.length == 1) {

            if (args[0].equalsIgnoreCase("reload") ){
                if (!p.hasPermission("quickdisenchant.reload")){
                    p.sendMessage(instance.translate("messages.command.invalid.no-permission"));
                }

                p.sendMessage(instance.translate("messages.command.reload"));
                instance.reloadConfig();
                return;
            }

            if (!p.hasPermission("quickdisenchant.disenchant")) {
                // no perms
                p.sendMessage(instance.translate("messages.command.invalid.no-permission"));
                return;
            }

            // remove all enchants
            Player target = Bukkit.getPlayer(args[0]);

            if (target == null) {
                p.sendMessage(instance.translate("messages.command.invalid.unknown-target").replace("%TARGET%", args[0]));
                return;
            }

            if (target.getInventory().getItemInMainHand().getEnchantments().isEmpty()) {
                p.sendMessage(instance.translate("messages.command.invalid.item-not-enchanted") );
                return;
            }

            ItemStack inHand = target.getInventory().getItemInMainHand();

            for (Enchantment ench : inHand.getEnchantments().keySet()) {
                inHand.removeEnchantment(ench);
            }

            p.sendMessage(instance.translate("messages.command.success.removed-all"));
        } else if (args.length == 2) {

            if (!p.hasPermission("quickdisenchant.disenchant")) {
                // no perms
                p.sendMessage(instance.translate("messages.command.invalid.no-permission"));
                return;
            }

            // remove specific ench
            removeEnchant(p, args[0], args[1], "-1");

        } else if (args.length == 3) {

            if (!p.hasPermission("quickdisenchant.disenchant")) {
                // no perms
                p.sendMessage(instance.translate("messages.command.invalid.no-permission"));
                return;
            }

            // remove specific ench with specific levels

            removeEnchant(p, args[0], args[1], args[2]);
        } else {

            if (!p.hasPermission("quickdisenchant.disenchant")) {
                // no perms
                p.sendMessage(instance.translate("messages.command.invalid.no-permission"));
                return;
            }

            // to many args
            p.sendMessage(instance.translate("messages.command.invalid.to-many-args"));
        }
    }

    // DRY - DON'T REPEAT YOURSELF!!!!!

    public void removeEnchant(Player sender, String targetStr, String enchStr, String levelsStr){

        // check if target exists if ! then say target not found
        Player target = Bukkit.getPlayer(targetStr);

        if (target == null) {
            sender.sendMessage(instance.translate("messages.command.invalid.unknown-target").replace("%TARGET%", targetStr));
            return;
        }

        ItemStack inHand = target.getInventory().getItemInMainHand();

        // check ench exisits
        String[] values = enchStr.replaceAll("[^a-z0-9:._-]+", "").split(":");
        if (values.length != 2) {
            sender.sendMessage(instance.translate("messages.command.invalid.unknown-ench"));
            for (String str : values) {
                Bukkit.getConsoleSender().sendMessage("§e" + str);
            }
            return;
        }
        Enchantment enchant = Enchantment.getByKey(new NamespacedKey(values[0], values[1]));
        if (enchant == null) {
            sender.sendMessage(instance.translate("messages.command.invalid.unknown-ench"));
            Bukkit.getConsoleSender().sendMessage("§c" + values[0] + "§7:§a" + values[1]);
            return;
        }

        // check if item in hand has enchants

        Map<Enchantment, Integer> enchants = inHand.getEnchantments();

        if (enchants.isEmpty()) {
            sender.sendMessage(instance.translate("messages.command.invalid.item-not-enchanted") );
            return;
        }

        // check if levels is a valid number

        int levels;

        try {
            levels = Integer.parseInt(levelsStr);
        } catch (NumberFormatException nfe) {
            sender.sendMessage(instance.translate("messages.command.invalid.number-format-exception").replace("%ARG%",levelsStr));
            return;
        }

        // check if item has specified enchant

        int levelsRemoved = inHand.removeEnchantment(enchant);

        if (levelsRemoved == 0) {
            sender.sendMessage(instance.translate("messages.command.invalid.item-does-not-contain-ench") );
        } else {

            if (levels  < 0) {
                // remove all levels
                sender.sendMessage(instance.translate("messages.command.success.removed-ench").replace("%ENCH%", enchStr));
            } else {
                int newLevels = levelsRemoved - levels;

                if (newLevels > 0){
                    inHand.addUnsafeEnchantment(enchant, newLevels);
                }

                sender.sendMessage(instance.translate("messages.command.success.removed-level").replace("%ENCH%", enchStr).replace("%LEVELS%", levelsStr));
            }
            sender.playSound(sender.getLocation(), Sound.BLOCK_GRINDSTONE_USE, SoundCategory.PLAYERS, 1.0f, 1.0f);
        }

    }

}



