package dev.replenish.enchant;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.persistence.PersistentDataType;
import java.util.ArrayList;
import java.util.List;
public class ReplenishCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) { sender.sendMessage(ChatColor.RED + "Only players can use this."); return true; }
        if (!player.hasPermission("replenish.give")) { player.sendMessage(ChatColor.RED + "No permission."); return true; }
        ItemStack book = new ItemStack(Material.ENCHANTED_BOOK);
        EnchantmentStorageMeta meta = (EnchantmentStorageMeta) book.getItemMeta();
        meta.getPersistentDataContainer().set(new NamespacedKey(ReplenishPlugin.getInstance(), "replenish_level"), PersistentDataType.INTEGER, 1);
        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.GRAY + "Replenish I");
        lore.add(ChatColor.DARK_GRAY + "Right-click any tool to apply.");
        meta.setLore(lore);
        meta.setDisplayName(ChatColor.YELLOW + "Replenish I");
        book.setItemMeta(meta);
        player.getInventory().addItem(book);
        player.sendMessage(ChatColor.GREEN + "You received a " + ChatColor.YELLOW + "Replenish I" + ChatColor.GREEN + " enchanted book!");
        return true;
    }
}
