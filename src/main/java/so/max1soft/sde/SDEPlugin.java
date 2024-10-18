package so.max1soft.sde;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

public class SDEPlugin extends JavaPlugin implements CommandExecutor  {

    private static SDEPlugin instance;

    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();
        getServer().getPluginManager().registerEvents(new SDEListener(), this);
        startSpawnTask();
    }
    @Override
    public void onDisable() {
    }

    public static SDEPlugin getInstance() {
        return instance;
    }

    public World getSpawnWorld() {
        String worldName = getConfig().getString("world");
        return Bukkit.getWorld(worldName);
    }

    private void startSpawnTask() {
        int interval = getConfig().getInt("spawn-interval", 10);
        new SpawnTask().runTaskTimer(this, 0L, interval * 20L * 60);
    }
}
