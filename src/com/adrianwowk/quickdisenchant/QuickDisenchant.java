package com.adrianwowk.quickdisenchant;

import com.adrianwowk.quickdisenchant.commands.CommandHandler;
import com.adrianwowk.quickdisenchant.commands.QDTabCompleter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.command.ConsoleCommandSender;
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

        saveDefaultConfig();

        // Register command tab completer and executer

        getCommand("disenchant").setTabCompleter(new QDTabCompleter());
        getCommand("disenchant").setExecutor(new CommandHandler(this));

        // Server Console Message
        this.getLogger().info(ChatColor.GREEN + "=================================");
        this.getLogger().info(ChatColor.GREEN + "         [QuickDisenchant]          ");
        this.getLogger().info(ChatColor.GREEN + "  Has been successfuly enabled!  ");
        this.getLogger().info(ChatColor.GREEN + "     Author - Adrian Wowk        ");
        this.getLogger().info(ChatColor.GREEN + "=================================");
    }
    public void onDisable(){

    }

    public String getPrefix() {
        return ChatColor.translateAlternateColorCodes('&', this.getConfig().getString("messages.prefix"));
    }

    public String translate(String path) {
        return getPrefix() + ChatColor.translateAlternateColorCodes('&', this.getConfig().getString(path));
    }
}
