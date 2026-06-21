package dev.replenish.enchant;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import java.util.ArrayList;
import java.util.List;
public class AnvilListener implements Listener {
    private final NamespacedKey key;
    public AnvilListener() {
        this.key = new NamespacedKey(ReplenishPlugin.getInstance(), "replenish_level");
    }
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player player)) return;
        ItemStack cursor = event.getCursor();
        if (cursor == null || cursor.getType() != Material.ENCHANTED_BOOK) return;
        if (!(cursor.getItemMeta() instanceof EnchantmentStorageMeta bookMeta)) return;
        Integer level = bookMeta.getPersistentDataContainer().get(key, PersistentDataType.INTEGER);
        if (level == null) return;
        ItemStack clicked = event.getCurrentItem();
        if (clicked == null || clicked.getType().isAir() || clicked.getType() == Material.ENCHANTED_BOOK) return;
        if (!event.getClick().isRightClick()) return;
        ItemMeta toolMeta = clicked.getItemMeta();
        if (toolMeta == null) return;
        if (toolMeta.getPersistentDataContainer().has(key, PersistentDataType.INTEGER)) {
            player.sendMessage(ChatColor.RED + "This item already has Replenish!");
            event.setCancelled(true);
            return;
        }
        event.setCancelled(true);
        toolMeta.getPersistentDataContainer().set(key, PersistentDataType.INTEGER, level);
        List<String> lore = toolMeta.hasLore() ? new ArrayList<>(toolMeta.getLore()) : new ArrayList<>();
        lore.add(0, ChatColor.GRAY + "Replenish I");
        toolMeta.setLore(lore);
        clicked.setItemMeta(toolMeta);
        if (cursor.getAmount() > 1) cursor.setAmount(cursor.getAmount() - 1);
        else event.getView().setCursor(null);
        player.sendMessage(ChatColor.GREEN + "✔ Replenish I applied!");
    }
}
