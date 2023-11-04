package org.mortisdevelopment.mortisbattleroyale.BattleRoyaleEnds;

import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class LeaveHandler implements CommandExecutor {

    private final JavaPlugin plugin;
    private String tpworld;
    private int tpx;
    private int tpy;
    private int tpz;
    private String tpbackworld;

    public LeaveHandler(JavaPlugin plugin) {
        this.plugin = plugin;
        loadConfig6();
    }

    private void loadConfig6(){
        FileConfiguration config = plugin.getConfig();
        tpworld = config.getString("tp-world");
        tpx = config.getInt("tp-x");
        tpy = config.getInt("tp-y");
        tpz = config.getInt("tp-z");
        tpbackworld = config.getString("spawnpoint.world");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (player.getGameMode().equals(GameMode.SPECTATOR)) {
                World playerWorld = player.getWorld();
                World specifiedWorld = Bukkit.getWorld(tpbackworld);
                World world = Bukkit.getWorld(tpworld);

                if (playerWorld.equals(specifiedWorld)) {
                    Location teleportLocation = new Location(world, tpx + 0.5, tpy, tpz + 0.5);
                    player.teleport(teleportLocation);
                    player.sendMessage(ChatColor.GREEN + "You have been teleported back to the lobby!");
                    return true;
                } else if (specifiedWorld == null) {
                    player.sendMessage(ChatColor.RED + "Invalid teleport world specified in the configuration!");
                } else {
                    player.sendMessage(ChatColor.RED + "You can only use this command in the arena!");
                }
            } else {
                player.sendMessage(ChatColor.RED + "You cannot use this command while in battle royale!");
            }
        } else {
            sender.sendMessage(ChatColor.RED + "This command can only be executed by a player!");
        }
        return false;
    }
}
