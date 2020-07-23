package com.adrianwowk.quickdisenchant;

import com.adrianwowk.quickdisenchant.commands.CommandHandler;
import com.adrianwowk.quickdisenchant.commands.QDTabCompleter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public class QuickDisenchant extends JavaPlugin {
    Server server;
    ConsoleCommandSender console;
    public static String prefix;

    public QuickDisenchant() {
        this.server = Bukkit.getServer();
        this.console = this.server.getConsoleSender();
        this.prefix = ChatColor.GRAY + "[" + ChatColor.LIGHT_PURPLE + "QuickDisenchant" + ChatColor.GRAY + "] ";
    }

    public void onEnable() {
        // Register command tab completer and executer

        getCommand("disenchant").setTabCompleter(new QDTabCompleter());
        getCommand("disenchant").setExecutor(new CommandHandler());

        // Register Event Listeners
        //Bukkit.getServer().getPluginManager().registerEvents(new BedrockMinerEvents(), (Plugin) this);

        // Server Console Message
        this.getLogger().info(ChatColor.GREEN + "=================================");
        this.getLogger().info(ChatColor.GREEN + "         [QuickDisenchant]          ");
        this.getLogger().info(ChatColor.GREEN + "  Has been successfuly enabled!  ");
        this.getLogger().info(ChatColor.GREEN + "     Author - Adrian Wowk        ");
        this.getLogger().info(ChatColor.GREEN + "=================================");
    }
    public void onDisable(){

    }
}
