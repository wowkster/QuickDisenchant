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

        Metrics metrics = new Metrics(this, 10039);

        // Server Console Message
        console.sendMessage(getPrefix() + "Successfully Enabled :)");
    }
    public void onDisable(){
        console.sendMessage(getPrefix() + "Successfully Disabled :)");
    }

    public String getPrefix() {
        return ChatColor.translateAlternateColorCodes('&', this.getConfig().getString("messages.prefix"));
    }

    public String translate(String path) {
        return getPrefix() + ChatColor.translateAlternateColorCodes('&', this.getConfig().getString(path));
    }
}
