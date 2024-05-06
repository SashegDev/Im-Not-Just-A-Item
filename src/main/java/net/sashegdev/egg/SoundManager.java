package net.sashegdev.egg;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class SoundManager {
    // тоже самое что с ParticleManager.java

    public static void playSound(Location location, Sound sound, float volume, float pitch, float minVolume, float radius) {
        for (Player player : Bukkit.getServer().getOnlinePlayers()) {
            if (player.getLocation().getWorld() == location.getWorld()) { // Учитываем мир игрока
                if (player.getLocation().distance(location) <= radius) { // Радиус
                    float distance = (float) player.getLocation().distance(location);
                    float playerVolume = volume - (distance * (volume - minVolume) / 100);
                    player.playSound(location, sound, playerVolume, pitch);
                }
            }
        }
    }
}
