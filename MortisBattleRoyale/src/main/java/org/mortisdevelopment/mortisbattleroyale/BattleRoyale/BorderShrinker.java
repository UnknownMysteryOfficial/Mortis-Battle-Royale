package org.mortisdevelopment.mortisbattleroyale.BattleRoyale;

import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.WorldBorder;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public class BorderShrinker implements CommandExecutor {

    private final JavaPlugin plugin;

    private int originalSize;

    public BorderShrinker(JavaPlugin plugin) {
        this.plugin = plugin;
        loadoriginalSize();
    }
    private void loadoriginalSize(){
        FileConfiguration config = plugin.getConfig();
        originalSize = config.getInt("originalSize");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player && sender.hasPermission("mortis.battleroyale.bordershrink")) {
            Player player = (Player) sender;
            World world = player.getWorld();
            WorldBorder worldBorder = world.getWorldBorder();

            // BukkitRunnable to gradually shrink the world border
            new BukkitRunnable() {
                double targetSize = 0;
                double shrinkAmount = 1;

                @Override
                public void run() {
                    double currentSize = worldBorder.getSize();
                    if (currentSize > targetSize) {
                        worldBorder.setSize(currentSize - shrinkAmount);
                    } else {
                        cancel();
                        if (world.getPlayers().isEmpty()) {
                            worldBorder.setSize(originalSize);
                            player.sendMessage(ChatColor.GREEN + "World border reset to its original size.");
                        }
                    }
                }
            }.runTaskTimer(plugin, 0, 20);

            return true;
        } else {
            sender.sendMessage(ChatColor.RED + "Kindly run this command as a player!");
            return false;
        }
    }
}
