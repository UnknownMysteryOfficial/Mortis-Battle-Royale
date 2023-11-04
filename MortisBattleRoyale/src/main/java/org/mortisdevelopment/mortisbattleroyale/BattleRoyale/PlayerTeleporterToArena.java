package org.mortisdevelopment.mortisbattleroyale.BattleRoyale;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;


public class PlayerTeleporterToArena implements CommandExecutor{

    private final JavaPlugin plugin;
    private String locationx;
    private String locationy;
    private String locationz;
    private String world;

    public PlayerTeleporterToArena(JavaPlugin plugin) {
        this.plugin = plugin;
        loadConfig1();

    }

    private void loadConfig1(){
        FileConfiguration config = plugin.getConfig();
        locationx = config.getString("spawnpoint.x");
        locationy = config.getString("spawnpoint.y");
        locationz = config.getString("spawnpoint.z");
        world = config.getString("spawnpoint.world");

        int borderSize = config.getInt("border.size", 2000);
        int borderCenterX = config.getInt("border.center-x", 1000);
        int borderCenterZ = config.getInt("border.center-z", 1000);

        World world1 = Bukkit.getWorld(config.getString("spawnpoint.world"));


        if (world == null) {
            plugin.getLogger().warning("World name not found in the configuration file!");
        }

        world1.getWorldBorder().setSize(borderSize);
        world1.getWorldBorder().setCenter(borderCenterX, borderCenterZ);
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        // /brstart: teleport all player to the arena to start the match
        if (command.getName().equalsIgnoreCase("brstart")){
            if (sender instanceof Player && sender.hasPermission("mortis.battleroyale.start")){
                Player player = ((Player) sender).getPlayer();
                player.sendMessage(ChatColor.GREEN + "Teleporting all the players to the arena!");
                for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                    onlinePlayer.teleport(new Location(Bukkit.getWorld(world),
                        Double.parseDouble(locationx),
                        Double.parseDouble(locationy),
                        Double.parseDouble(locationz)));
                    onlinePlayer.sendMessage(ChatColor.RED + "You have been teleported to the arena! Get ready for the war and be the last one standing to win the match.");
                }
            }
            return true; // Command was processed successfully :)
        }
        return false; // Command wasn't processed successfully :(
    }
}
