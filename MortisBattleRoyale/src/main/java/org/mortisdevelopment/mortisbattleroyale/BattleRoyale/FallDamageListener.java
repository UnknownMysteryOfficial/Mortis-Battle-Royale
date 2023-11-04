package org.mortisdevelopment.mortisbattleroyale.BattleRoyale;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class FallDamageListener implements Listener {

    private final JavaPlugin plugin;
    private String nodamagemsg;
    private String damagemsg;

    public FallDamageListener(JavaPlugin plugin) {
        this.plugin = plugin;
        loadConfig3();
    }

    private void loadConfig3(){
        FileConfiguration config = plugin.getConfig();
        nodamagemsg = ChatColor.translateAlternateColorCodes('&', config.getString("no-damage-msg") + " ");
        damagemsg = ChatColor.translateAlternateColorCodes('&', config.getString("damage-msg") + " ");
    }

    @EventHandler
    public void onPlayerFall(EntityDamageEvent event){
        if (event.getEntity() instanceof Player){
            Player player = (Player) event.getEntity();
            if (event.getCause() == EntityDamageEvent.DamageCause.FALL){
                if (player.hasPermission("mortis.battleroyale.falcon")){
                    event.setCancelled(true);
                    player.sendMessage(nodamagemsg);
                }else{
                    if (!player.hasPermission("mortis.battleroyale.falcon")){
                        event.setCancelled(true);
                        player.setHealth(10.0);
                        player.sendMessage(damagemsg);
                    }
                }
            }
        }
    }
}
