package dev.replenish.enchant;
import org.bukkit.plugin.java.JavaPlugin;
public class ReplenishPlugin extends JavaPlugin {
    private static ReplenishPlugin instance;
    @Override
    public void onEnable() {
        instance = this;
        getServer().getPluginManager().registerEvents(new ReplenishListener(this), this);
        getServer().getPluginManager().registerEvents(new AnvilListener(), this);
        getCommand("replenish").setExecutor(new ReplenishCommand());
        getLogger().info("ReplenishEnchant enabled!");
    }
    @Override
    public void onDisable() { getLogger().info("ReplenishEnchant disabled."); }
    public static ReplenishPlugin getInstance() { return instance; }
}
