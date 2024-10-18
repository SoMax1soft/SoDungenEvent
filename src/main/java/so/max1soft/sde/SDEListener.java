package so.max1soft.sde;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.entity.WitherSkeleton;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;

public class SDEListener implements Listener {

    private final Map<String, Long> lastMessageTime = new HashMap<>();
    private final long MESSAGE_COOLDOWN = 3000L;
    private boolean globalMessageSent = false;

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        if (event.getEntity() instanceof WitherSkeleton) {
            WitherSkeleton skeleton = (WitherSkeleton) event.getEntity();
            String customName = skeleton.getCustomName();

            if (customName != null) {
                String nameWithoutColor = ChatColor.stripColor(customName);
                Player killer = skeleton.getKiller();

                if (killer != null) {
                    if (nameWithoutColor.contains("МЕГА") || nameWithoutColor.contains("ВЛАСТИТЕЛЬ")) {
                        handleSpecialSkeleton(skeleton, nameWithoutColor, killer);
                    } else if (isGuard(skeleton)) {
                        handleGuardSkeleton(skeleton, killer);
                    }
                }
            }
        }
    }

    private void handleSpecialSkeleton(WitherSkeleton skeleton, String nameWithoutColor, Player killer) {
        int reward;
        String message;
        if (nameWithoutColor.contains("МЕГА")) {
            reward = 25;
            message = "§x§F§F§9§B§0§0§l╫ §fИгрок %player%§f §x§F§F§8§6§0§0у§x§F§F§D§9§0§0б§x§F§F§B§0§0§0и§x§F§F§8§6§0§0л§f мегастражника!";
        } else {
            reward = 35;
            message = "§x§F§F§9§B§0§0§l╫ §fИгрок %player%§f §x§F§F§8§6§0§0у§x§F§F§D§9§0§0б§x§F§F§B§0§0§0и§x§F§F§8§6§0§0л§f властителя!";
        }

        long currentTime = System.currentTimeMillis();
        long lastMessage = lastMessageTime.getOrDefault("global", 0L);

        if (currentTime - lastMessage > MESSAGE_COOLDOWN) {
            globalMessageSent = true;
            lastMessageTime.put("global", currentTime);


            String formattedMessage = message.replace("%player%", killer.getName());
            sendMessageToAllPlayers(formattedMessage, reward);


            giveReward(killer, reward);


            new BukkitRunnable() {
                @Override
                public void run() {
                    globalMessageSent = false;
                }
            }.runTaskLater(SDEPlugin.getInstance(), 60L);

        } else if (globalMessageSent) {

            String formattedMessage = message.replace("%player%", killer.getName());
            sendMessageToNearbyPlayers(skeleton.getLocation(), formattedMessage, reward);
        }
    }

    private void handleGuardSkeleton(WitherSkeleton skeleton, Player killer) {
        int reward = 15;
        String message = "§x§F§F§9§B§0§0§l╫ §fИгрок %player%§f §x§F§F§8§6§0§0у§x§F§F§D§9§0§0б§x§F§F§B§0§0§0и§x§F§F§8§6§0§0л§f стражника!";

        long currentTime = System.currentTimeMillis();
        long lastMessage = lastMessageTime.getOrDefault("global", 0L);

        if (currentTime - lastMessage > MESSAGE_COOLDOWN) {
            globalMessageSent = true;
            lastMessageTime.put("global", currentTime);


            String formattedMessage = message.replace("%player%", killer.getName());
            sendMessageToAllPlayers(formattedMessage, reward);


            giveReward(killer, reward);


            new BukkitRunnable() {
                @Override
                public void run() {
                    globalMessageSent = false;
                }
            }.runTaskLater(SDEPlugin.getInstance(), 60L);

        } else if (globalMessageSent) {

            String formattedMessage = message.replace("%player%", killer.getName());
            sendMessageToNearbyPlayers(skeleton.getLocation(), formattedMessage, reward);
        }
    }

    private boolean isGuard(WitherSkeleton skeleton) {
        String nameWithoutColor = ChatColor.stripColor(skeleton.getCustomName());
        return SDEPlugin.getInstance().getConfig().getConfigurationSection("spawn-locations").getKeys(false).stream()
                .anyMatch(locationKey -> {
                    String name = SDEPlugin.getInstance().getConfig().getString("spawn-locations." + locationKey + ".name");
                    return ChatColor.stripColor(name).equals(nameWithoutColor);
                });
    }

    private void sendMessageToAllPlayers(String message, int reward) {
        Bukkit.broadcastMessage("");
        Bukkit.broadcastMessage(message);
        Bukkit.broadcastMessage("§x§F§F§9§B§0§0§l╫ §fИ получил " + reward + " кубиков!");
        Bukkit.broadcastMessage("§x§F§F§9§B§0§0§l╫ §fСкорее беги фармить крипов - §x§F§F§9§1§0§0/§x§F§F§A§8§0§0ᴅ§x§F§F§C§0§0§0ᴜ§x§F§F§D§7§0§0ɴ§x§F§F§E§E§0§0ɢ§x§F§F§C§F§0§0ᴇ§x§F§F§B§0§0§0ᴏ§x§F§F§9§1§0§0ɴ");
        Bukkit.broadcastMessage("");
    }

    private void sendMessageToNearbyPlayers(Location location, String message, int reward) {
        for (Player player : location.getWorld().getPlayers()) {
            if (player.getLocation().distance(location) <= 100) {
                player.sendMessage(message);
                player.sendMessage("§x§F§F§9§B§0§0§l╫ §fИ получил " + reward + " кубиков!");
                player.sendMessage("§x§F§F§9§B§0§0§l╫ §fСкорее беги фармить крипов - §x§F§F§9§1§0§0/§x§F§F§A§8§0§0ᴅ§x§F§F§C§0§0§0ᴜ§x§F§F§D§7§0§0ɴ§x§F§F§E§E§0§0ɢ§x§F§F§C§F§0§0ᴇ§x§F§F§B§0§0§0ᴏ§x§F§F§9§1§0§0ɴ");
                player.sendMessage("");
            }
        }
    }

    private void giveReward(Player player, int amount) {

        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "p give " + player.getName() + " " + amount);
    }
}
