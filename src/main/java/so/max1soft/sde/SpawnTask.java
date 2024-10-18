package so.max1soft.sde;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.WitherSkeleton;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashSet;
import java.util.Set;

public class SpawnTask extends BukkitRunnable {

    private static final Set<String> existingGuardNames = new HashSet<>();

    @Override
    public void run() {
        Bukkit.broadcastMessage("");
        Bukkit.broadcastMessage("§x§F§F§9§B§0§0§l╫ §fСтражники §x§F§F§8§6§0§0о§x§F§F§9§2§0§0п§x§F§F§9§E§0§0я§x§F§F§A§A§0§0т§x§F§F§B§5§0§0ь §x§F§F§C§D§0§0п§x§F§F§D§9§0§0о§x§F§F§C§F§0§0я§x§F§F§C§4§0§0в§x§F§F§B§A§0§0и§x§F§F§B§0§0§0л§x§F§F§A§5§0§0и§x§F§F§9§B§0§0с§x§F§F§9§0§0§0ь§x§F§F§8§6§0§0!");
        Bukkit.broadcastMessage("§x§F§F§9§B§0§0§l╫ §fСкорее беги §x§F§F§8§6§0§0ф§x§F§F§A§2§0§0а§x§F§F§B§D§0§0р§x§F§F§D§9§0§0м§x§F§F§B§D§0§0и§x§F§F§A§2§0§0т§x§F§F§8§6§0§0ь§f крипов - §x§F§F§9§1§0§0/§x§F§F§A§8§0§0ᴅ§x§F§F§C§0§0§0ᴜ§x§F§F§D§7§0§0ɴ§x§F§F§E§E§0§0ɢ§x§F§F§C§F§0§0ᴇ§x§F§F§B§0§0§0ᴏ§x§F§F§9§1§0§0ɴ");
        Bukkit.broadcastMessage("");
        SDEPlugin plugin = SDEPlugin.getInstance();
        World world = plugin.getSpawnWorld();

        if (world == null) {
            Bukkit.getLogger().warning("Указанный мир не найден в конфиге!");
            return;
        }

        Bukkit.getLogger().info("Запуск задачи спавна стражников");

        // Загрузка имён существующих стражников из конфигурационного файла
        existingGuardNames.clear();
        for (String locationKey : plugin.getConfig().getConfigurationSection("spawn-locations").getKeys(false)) {
            String name = plugin.getConfig().getString("spawn-locations." + locationKey + ".name");
            existingGuardNames.add(ChatColor.RED.toString() + name);
        }

        // Спавним новых стражников на указанных локациях
        for (String locationKey : plugin.getConfig().getConfigurationSection("spawn-locations").getKeys(false)) {
            double x = plugin.getConfig().getDouble("spawn-locations." + locationKey + ".x");
            double y = plugin.getConfig().getDouble("spawn-locations." + locationKey + ".y");
            double z = plugin.getConfig().getDouble("spawn-locations." + locationKey + ".z");
            String name = plugin.getConfig().getString("spawn-locations." + locationKey + ".name");
            double health = plugin.getConfig().getDouble("spawn-locations." + locationKey + ".health");
            double damage = plugin.getConfig().getDouble("spawn-locations." + locationKey + ".damage");

            Location location = new Location(world, x, y, z);

            // Проверяем, есть ли уже стражник с таким именем
            boolean isExistingGuard = world.getEntitiesByClass(WitherSkeleton.class).stream()
                    .anyMatch(skeleton -> {
                        String customName = skeleton.getCustomName();
                        return customName != null && customName.equals(ChatColor.RED.toString() + name);
                    });

            if (isExistingGuard) {
                Bukkit.getLogger().info("Стражник с именем '" + name + "' уже существует, пропускаем.");
                continue; // Пропускаем текущую локацию
            }

            WitherSkeleton skeleton = (WitherSkeleton) location.getWorld().spawnEntity(location, EntityType.WITHER_SKELETON);
            skeleton.setCustomName(ChatColor.RED.toString() + name);
            skeleton.setCustomNameVisible(true);

            // Установка здоровья
            AttributeInstance healthAttr = skeleton.getAttribute(Attribute.GENERIC_MAX_HEALTH);
            if (healthAttr != null) {
                healthAttr.setBaseValue(health); // Устанавливаем максимальное здоровье
                skeleton.setHealth(health); // Устанавливаем текущее здоровье
            }

            // Установка урона
            AttributeInstance damageAttr = skeleton.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE);
            if (damageAttr != null) {
                damageAttr.setBaseValue(damage); // Устанавливаем урон
            }

            Bukkit.getLogger().info("Стражник " + name + " успешно заспавнен на " + location);
        }
    }
}
