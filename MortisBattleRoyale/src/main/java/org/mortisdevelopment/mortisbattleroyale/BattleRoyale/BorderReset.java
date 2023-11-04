package org.mortisdevelopment.mortisbattleroyale.BattleRoyale;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class BorderReset implements CommandExecutor {

    private final JavaPlugin plugin;
    private String borderworldName;
    private int orgSize;

    public BorderReset(JavaPlugin plugin) {
        this.plugin = plugin;
        loadConfig4();
    }

    private void loadConfig4(){
        FileConfiguration config = plugin.getConfig();
        borderworldName = config.getString("spawnpoint.world");
        orgSize = config.getInt("originalSize", 2000);
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("brbreset")){
            if (sender.hasPermission("mortis.battleroyale.borderreset")){
                Bukkit.getWorld(borderworldName).getWorldBorder().setSize(orgSize);
            }
        }
        return false;
    }
}
