package org.mortisdevelopment.mortisbattleroyale.BattleRoyaleEnds;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public class WinnerAnnouncer {

    private final JavaPlugin plugin;
    private String spawnWorld;
    private String winningWorld;
    private int winningX;
    private int winningY;
    private int winningZ;

    public WinnerAnnouncer(JavaPlugin plugin) {
        this.plugin = plugin;
        loadConfig();

        new BukkitRunnable() {
            @Override
            public void run() {
                checkWinningConditions();
            }
        }.runTaskTimer(plugin, 0, 20);
    }

    private void loadConfig() {
        FileConfiguration config = plugin.getConfig();
        spawnWorld = config.getString("spawnpoint.world");
        winningWorld = config.getString("winning-world");
        winningX = config.getInt("winning-x");
        winningY = config.getInt("winning-y");
        winningZ = config.getInt("winning-z");
    }

    private void checkWinningConditions() {
        World spawnWorldObj = Bukkit.getWorld(spawnWorld);
        if (spawnWorldObj != null) {
            int onlinePlayers = 0;
            Player winner = null;

            for (Player player : spawnWorldObj.getPlayers()) {
                if (player.getGameMode() == org.bukkit.GameMode.SURVIVAL && !player.isOp()) {
                    onlinePlayers++;
                    winner = player;
                }
            }

            if (onlinePlayers == 1) {
                announceWinner(winner);
                teleportAllPlayers(winner);
            }
        }
    }

    private void announceWinner(Player winner) {
        if (winner != null) {
            Bukkit.broadcastMessage(ChatColor.GREEN + winner.getName() + " has won the game!");
        }
    }

    private void teleportAllPlayers(Player winner) {
        World arena = Bukkit.getWorld(spawnWorld);
        World world = Bukkit.getWorld(winningWorld);
        if (arena != null) {
            for (Player player : Bukkit.getOnlinePlayers()) {
                player.teleport(new org.bukkit.Location(world, winningX + 0.5, winningY, winningZ + 0.5));
            }
        }
    }
}
