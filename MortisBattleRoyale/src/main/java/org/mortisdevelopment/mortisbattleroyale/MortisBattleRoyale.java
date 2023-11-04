package org.mortisdevelopment.mortisbattleroyale;

import org.bukkit.plugin.java.JavaPlugin;
import org.mortisdevelopment.mortisbattleroyale.AreenaLoots.LootDrop;
import org.mortisdevelopment.mortisbattleroyale.BattleRoyale.BorderReset;
import org.mortisdevelopment.mortisbattleroyale.BattleRoyale.BorderShrinker;
import org.mortisdevelopment.mortisbattleroyale.BattleRoyale.FallDamageListener;
import org.mortisdevelopment.mortisbattleroyale.BattleRoyale.PlayerTeleporterToArena;
import org.mortisdevelopment.mortisbattleroyale.BattleRoyaleEnds.LeaveHandler;
import org.mortisdevelopment.mortisbattleroyale.BattleRoyaleEnds.PlayerDeathHandler;
import org.mortisdevelopment.mortisbattleroyale.BattleRoyaleEnds.WinnerAnnouncer;

public final class MortisBattleRoyale extends JavaPlugin {


    @Override
    public void onEnable() {
        // Plugin startup logic
        saveDefaultConfig();
        getCommand("brstart").setExecutor(new PlayerTeleporterToArena(this));
        getCommand("brshrink").setExecutor(new BorderShrinker(this));
        getCommand("brbreset").setExecutor(new BorderReset(this));
        getCommand("brleave").setExecutor(new LeaveHandler(this));
        getServer().getPluginManager().registerEvents(new FallDamageListener(this), this);
        getServer().getPluginManager().registerEvents(new PlayerDeathHandler(this), this);
        new LootDrop(this);
        new WinnerAnnouncer(this);
        getLogger().info("Thank you for using Mortis BattleRoyale v1.0! Hope you enjoys the plugin! :D");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        getLogger().info("Mortis BattleRoyale v1.0 was successfully disabled!");
    }
}
