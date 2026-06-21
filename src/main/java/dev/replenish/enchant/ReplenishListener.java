package dev.replenish.enchant;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.data.Ageable;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import java.util.EnumMap;
import java.util.Map;
public class ReplenishListener implements Listener {
    private final ReplenishPlugin plugin;
    private static final Map<Material, Material> CROP_TO_SEED = new EnumMap<>(Material.class);
    static {
        CROP_TO_SEED.put(Material.WHEAT, Material.WHEAT_SEEDS);
        CROP_TO_SEED.put(Material.CARROTS, Material.CARROT);
        CROP_TO_SEED.put(Material.POTATOES, Material.POTATO);
        CROP_TO_SEED.put(Material.BEETROOTS, Material.BEETROOT_SEEDS);
        CROP_TO_SEED.put(Material.NETHER_WART, Material.NETHER_WART);
        CROP_TO_SEED.put(Material.COCOA, Material.COCOA_BEANS);
    }
    public ReplenishListener(ReplenishPlugin plugin) { this.plugin = plugin; }
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        Block block = event.getBlock();
        Material type = block.getType();
        if (!CROP_TO_SEED.containsKey(type)) return;
        ItemStack heldItem = player.getInventory().getItemInMainHand();
        int level = ReplenishEnchantment.getLevel(heldItem);
        if (level <= 0 || !player.hasPermission("replenish.use")) return;
        BlockData data = block.getBlockData();
        if (data instanceof Ageable ageable && ageable.getAge() < ageable.getMaximumAge()) {
            event.setCancelled(true);
            return;
        }
        Location loc = block.getLocation().clone();
        Material cropType = type;
        new BukkitRunnable() {
            @Override
            public void run() {
                Block target = loc.getBlock();
                if (target.getType() != Material.AIR) return;
                target.setType(cropType);
                BlockData nd = target.getBlockData();
                if (nd instanceof Ageable a) { a.setAge(0); target.setBlockData(nd); }
                loc.getWorld().playSound(loc, Sound.ITEM_CROP_PLANT, 0.8f, 1.2f);
                loc.getWorld().spawnParticle(Particle.VILLAGER_HAPPY, loc.clone().add(0.5,0.5,0.5), 5, 0.3, 0.3, 0.3, 0);
            }
        }.runTaskLater(plugin, 1L);
    }
}
