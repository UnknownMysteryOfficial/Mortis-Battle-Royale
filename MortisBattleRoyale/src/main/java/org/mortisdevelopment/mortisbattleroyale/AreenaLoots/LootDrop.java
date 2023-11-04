package org.mortisdevelopment.mortisbattleroyale.AreenaLoots;

import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;
import java.util.Random;

public class LootDrop {

    private final JavaPlugin plugin;
    private final FileConfiguration config;
    private final int x1, z1, x2, z2;

    public LootDrop(JavaPlugin plugin) {
        this.plugin = plugin;
        this.config = plugin.getConfig();

        // Load coordinates from the config
        x1 = config.getInt("region.x1");
        z1 = config.getInt("region.z1");
        x2 = config.getInt("region.x2");
        z2 = config.getInt("region.z2");

        scheduleLootDrops();
    }

    private void scheduleLootDrops() {
        new BukkitRunnable() {
            @Override
            public void run() {
                dropLootChest();
            }
        }.runTaskTimer(plugin, 0, config.getInt("loot-drop.interval") * 20); // 20 ticks = 1 second
    }

    private void dropLootChest() {
        World world = Bukkit.getWorld(config.getString("spawnpoint.world"));
        if (world == null) {
            plugin.getLogger().warning("Invalid world specified for loot drops in config.yml.");
            return;
        }

        Random random = new Random();
        int x = random.nextInt(config.getInt("border.size") * 2) - config.getInt("border.size");
        int z = random.nextInt(config.getInt("border.size") * 2) - config.getInt("border.size");

        x = Math.max(x, -config.getInt("border.size"));
        x = Math.min(x, config.getInt("border.size"));
        z = Math.max(z, -config.getInt("border.size"));
        z = Math.min(z, config.getInt("border.size"));

        int y = world.getHighestBlockYAt(x, z);

        Location chestLocation = new Location(world, x, y, z);

        Block block = world.getBlockAt(chestLocation);
        block.setType(Material.CHEST);
        Chest chest = (Chest) block.getState();

        Inventory chestInventory = chest.getInventory();
        List<String> lootItems = config.getStringList("loot-drop.items");
        for (String item : lootItems) {
            try {
                String[] parts = item.split(":");
                Material material = Material.valueOf(parts[0]);
                int amount = Integer.parseInt(parts[1]);
                int probability = Integer.parseInt(parts[2]);

                // Check if the random number falls within the probability range for this item
                if (random.nextInt(100) < probability) {
                    ItemStack itemStack = new ItemStack(material, amount);
                    chestInventory.addItem(itemStack);
                }
            } catch (Exception e) {
                plugin.getLogger().warning("Invalid loot item format in config.yml: " + item);
            }
        }

        if (world.getName().equals(chestLocation.getWorld().getName())) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    if (block.getType() == Material.CHEST) {
                        block.setType(Material.AIR);
                    }
                }
            }.runTaskLater(plugin, 6000); // 6000 ticks = 5 minutes
        }


        boolean shouldBroadcast = config.getBoolean("broadcast");
        if (shouldBroadcast) {
            String message = config.getString("broadcast-message")
                    .replace("{x}", String.valueOf(x))
                    .replace("{y}", String.valueOf(y))
                    .replace("{z}", String.valueOf(z));
            Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', message + " "));
        }
    }

    private boolean isLocationInRegion(int x, int z) {
        return x >= x1 && x <= x2 && z >= z1 && z <= z2;
    }
}
