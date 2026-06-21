package dev.replenish.enchant;
import org.bukkit.ChatColor;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import java.util.ArrayList;
import java.util.List;
public class ReplenishEnchantment {
    private static final NamespacedKey KEY = new NamespacedKey(ReplenishPlugin.getInstance(), "replenish_level");
    public static int getLevel(ItemStack item) {
        if (item == null || !item.hasItemMeta()) return 0;
        PersistentDataContainer pdc = item.getItemMeta().getPersistentDataContainer();
        Integer level = pdc.get(KEY, PersistentDataType.INTEGER);
        return level != null ? level : 0;
    }
    public static ItemStack applyEnchantment(ItemStack item, int level) {
        if (item == null || level < 1) return item;
        ItemMeta meta = item.getItemMeta();
        if (meta == null) return item;
        meta.getPersistentDataContainer().set(KEY, PersistentDataType.INTEGER, level);
        List<String> lore = meta.hasLore() ? new ArrayList<>(meta.getLore()) : new ArrayList<>();
        lore.removeIf(line -> ChatColor.stripColor(line).startsWith("Replenish "));
        lore.add(0, ChatColor.GRAY + "Replenish I");
        meta.setLore(lore);
        item.setItemMeta(meta);
        return item;
    }
}
