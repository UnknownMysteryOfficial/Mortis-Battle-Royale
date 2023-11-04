package org.mortisdevelopment.mortisbattleroyale.BattleRoyaleEnds;

import org.bukkit.*;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.UUID;

public class PlayerDeathHandler implements Listener {

    private final JavaPlugin plugin;
    private String deathWorld;
    private final HashMap<UUID, Location> playerLocations;

    public PlayerDeathHandler(JavaPlugin plugin) {
        this.plugin = plugin;
        this.playerLocations = new HashMap<>();
        loadConfig9();
    }

    private void loadConfig9(){
        FileConfiguration config = plugin.getConfig();
        deathWorld = config.getString("spawnpoint.world");
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event){
        Player player = event.getEntity();
        if (player.getWorld().getName().equals(deathWorld)){
            playerLocations.put(player.getUniqueId(), player.getLocation());

            player.setGameMode(GameMode.SPECTATOR);

            event.getDrops().clear();
            event.setDeathMessage("");

            player.sendMessage(ChatColor.RED + "You have died! Now you can spectate or leave the game using '/leave' command");
        }
    }

    public Location getPlayerLocation(UUID playerId) {
        return playerLocations.get(playerId);
    }

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        Player player = event.getPlayer();
        Location respawnLocation = getPlayerLocation(player.getUniqueId());

        if (respawnLocation != null) {
            event.setRespawnLocation(respawnLocation);

            playerLocations.remove(player.getUniqueId());
        } else {
            event.setRespawnLocation(Bukkit.getWorld(deathWorld).getSpawnLocation());
        }
    }
}
