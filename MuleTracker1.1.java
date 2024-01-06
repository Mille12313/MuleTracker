package track.mule.muletracker;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.Bukkit;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MuleTracker extends JavaPlugin implements CommandExecutor, Listener {

    private Map<String, List<String>> playerLogs = new HashMap<>();
    private Map<String, Location> droppedItemLocations = new HashMap<>();
    private Map<String, Location> chestItemLocations = new HashMap<>();
    private List<Material> trackedItems = new ArrayList<>();

    @Override
    public void onEnable() {
        getLogger().info("MuleTracker has been enabled!");

        // Register command executors
        getCommand("mule_drop").setExecutor(this);
        getCommand("mule_chest").setExecutor(this);
        getCommand("mule_logs").setExecutor(this);

        // Register event listener
        getServer().getPluginManager().registerEvents(this, this);

        // Laad de geconfigureerde tracked items
        loadTrackedItems();
    }

    @Override
    public void onDisable() {
        getLogger().info("MuleTracker has been disabled!");
    }

    private void loadTrackedItems() {
        trackedItems.clear();
        ConfigurationSection trackedItemsSection = getConfig().getConfigurationSection("tracked_items");

        if (trackedItemsSection != null) {
            for (String itemName : trackedItemsSection.getKeys(false)) {
                Material material = Material.matchMaterial(itemName);

                if (material != null) {
                    trackedItems.add(material);
                } else {
                    getLogger().warning("Invalid item name in tracked_items: " + itemName);
                }
            }
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (label.equalsIgnoreCase("mule_drop") || label.equalsIgnoreCase("mule_chest")) {
            if (args.length >= 1 && sender instanceof Player) {
                Player player = (Player) sender;
                String playerName = args[0].toLowerCase();

                if (label.equalsIgnoreCase("mule_drop")) {
                    // ... (rest van de code voor mule_drop)
                } else if (label.equalsIgnoreCase("mule_chest")) {
                    // ... (rest van de code voor mule_chest)
                }

                return true;
            }
        } else if (label.equalsIgnoreCase("mule_logs") && sender instanceof Player) {
            Player player = (Player) sender;
            String playerName = player.getName().toLowerCase();

            if (playerLogs.containsKey(playerName)) {
                List<String> logs = playerLogs.get(playerName);
                int pageSize = 10;
                int pageNum = 1;

                if (args.length == 1) {
                    try {
                        pageNum = Integer.parseInt(args[0]);
                    } catch (NumberFormatException e) {
                        player.sendMessage(ChatColor.RED + "Invalid page number!");
                        return true;
                    }
                }

                int start = (pageNum - 1) * pageSize;
                int end = Math.min(start + pageSize, logs.size());

                player.sendMessage(ChatColor.YELLOW + "=== Mule Logs (Page " + pageNum + ") ===");

                for (int i = start; i < end; i++) {
                    player.sendMessage(logs.get(i));
                }
            } else {
                player.sendMessage(ChatColor.RED + "No logs found for player " + playerName + ".");
            }

            return true;
        }

        return false;
    }

    @EventHandler
    public void onPlayerDropItem(PlayerDropItemEvent event) {
        Player player = event.getPlayer();
        ItemStack droppedItem = event.getItemDrop().getItemStack();

        // Save the coordinates where the item was dropped
        droppedItemLocations.put(player.getName().toLowerCase(), player.getLocation());

        // Log the drop event
        logEvent(player, "Dropped " + droppedItem.getType() + " at "
                + player.getLocation().getBlockX() + ", "
                + player.getLocation().getBlockY() + ", "
                + player.getLocation().getBlockZ());
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        Action action = event.getAction();

        if (action == Action.RIGHT_CLICK_BLOCK) {
            Block clickedBlock = event.getClickedBlock();

            if (clickedBlock != null && clickedBlock.getType() == Material.CHEST) {
                // Save the location of the chest
                Location chestLocation = clickedBlock.getLocation();
                chestItemLocations.put(player.getName().toLowerCase(), chestLocation);

                // Get the item in the player's hand
                ItemStack itemInHand = player.getInventory().getItemInMainHand();

                // Log the chest interaction event with the item information
                logEvent(player, "Interacted with chest at "
                        + chestLocation.getBlockX() + ", "
                        + chestLocation.getBlockY() + ", "
                        + chestLocation.getBlockZ()
                        + " - Item: " + itemInHand.getType() + " x" + itemInHand.getAmount());

                // Check if the item is in the tracked items list
                if (trackedItems.contains(itemInHand.getType())) {
                    // If the item is tracked, log the chest interaction
                    logEvent(player, "Tracked item interaction with chest at "
                            + chestLocation.getBlockX() + ", "
                            + chestLocation.getBlockY() + ", "
                            + chestLocation.getBlockZ()
                            + " - Item: " + itemInHand.getType() + " x" + itemInHand.getAmount());
                }
            }
        }
    }

    private void logEvent(Player player, String log) {
        String playerName = player.getName().toLowerCase();

        // Retrieve logs for the player
        List<String> logs = playerLogs.getOrDefault(playerName, new ArrayList<>());

        // Add the new log entry
        logs.add(log);

        // Update the logs map
        playerLogs.put(playerName, logs);
    }
}

