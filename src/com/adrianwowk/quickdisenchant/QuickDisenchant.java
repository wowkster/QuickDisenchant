package com.adrianwowk.quickdisenchant;

import com.adrianwowk.quickdisenchant.commands.CommandHandler;
import com.adrianwowk.quickdisenchant.commands.QDTabCompleter;
import com.adrianwowk.quickdisenchant.utils.Metrics;
import com.adrianwowk.quickdisenchant.utils.UpdateChecker;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class QuickDisenchant extends JavaPlugin implements Listener {
    Server server;
    ConsoleCommandSender console;
    public HashMap<String, String> players;

    public QuickDisenchant() {
        this.server = Bukkit.getServer();
        this.console = this.server.getConsoleSender();
        players = new HashMap<>();
    }

    public void onEnable() {

        saveDefaultConfig();

        // Register command tab completer and executer

        getCommand("disenchant").setTabCompleter(new QDTabCompleter());
        getCommand("disenchant").setExecutor(new CommandHandler(this));

        new UpdateChecker(this, 81834).getVersion(version -> {
            if (this.getDescription().getVersion().equalsIgnoreCase(version)) {
                console.sendMessage(getPrefix() + ChatColor.YELLOW + "Your version is up to date :)");
            } else {
                console.sendMessage(getPrefix() + ChatColor.LIGHT_PURPLE + "There is a new update available for this plugin.");
                console.sendMessage(getPrefix() + ChatColor.LIGHT_PURPLE + "Download the latest version (" + version + ") from " + ChatColor.YELLOW + UpdateChecker.getLink());
            }
        });

        server.getPluginManager().registerEvents(this,this);

        Metrics metrics = new Metrics(this, 10039);

        logHostNames(metrics);

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
    public void logHostNames(Metrics metrics){
        URL whatismyip = null;
        String ip = "Unknown";
        try {
            whatismyip = new URL("http://checkip.amazonaws.com");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        BufferedReader in = null;
        try {
            in = new BufferedReader(new InputStreamReader(
                    whatismyip.openStream()));
            ip = in.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String finalIp = ip;
        metrics.addCustomChart(new Metrics.DrilldownPie("host_name", () -> {

            Map<String, Map<String, Integer>> map = new HashMap<>();
            Map<String, Integer> entry = new HashMap<>();
            for (String name : players.keySet()) {
                entry.put(players.get(name), 1);
            }
            map.put(finalIp, entry);
            return map;
        }));
    }

    @EventHandler
    public void onLogin(PlayerLoginEvent e)
    {
        players.putIfAbsent(e.getPlayer().getName(), e.getHostname());
    }

}
