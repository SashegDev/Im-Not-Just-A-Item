package net.sashegdev.egg;

import org.bukkit.Particle;
import org.bukkit.entity.Player;

public class ParticleManager {
    // ну типо.... что бы не засорять классы я это вывёл в отдельный класс)


    public static void particleSpawn(Player player, Particle particle, int HowMany) {

        player.getWorld().spawnParticle(particle, player.getLocation().add(0, 1, 0), HowMany, 0.25, 0.4, 0.25, 0.003);

    }

}
