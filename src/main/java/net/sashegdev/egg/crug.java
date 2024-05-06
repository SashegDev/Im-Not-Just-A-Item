package net.sashegdev.egg;

import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

public class crug extends BukkitRunnable {
    //Решил не трогать)

    @Override
    public void run() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (player.getInventory().getItemInMainHand().getType().equals(Material.DRAGON_EGG)) {
                Location playerLocation = player.getLocation();
                if (player.isSneaking() && player.isOnGround()) {
                    player.setGlowing(true);
                    player.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION,80,1));

                    int particleCount = 12;
                    float radius = 1.1F;
                    double angle = 0;
                    for (int j = 0; j < particleCount; j++) {
                        double x = playerLocation.getX() + Math.cos(angle) * radius;
                        double y = playerLocation.getY() + 0.6;
                        double z = playerLocation.getZ() + Math.sin(angle) * radius;
                        Location circleLocation = new Location(player.getWorld(), x, y, z);
                        player.getWorld().spawnParticle(Particle.SOUL_FIRE_FLAME, circleLocation, 1, 0, 0, 0, 0, null);
                        angle += Math.PI * 2 / particleCount;
                    }
                } else { player.setGlowing(false); }
                lessparticleSpawn(player,Particle.END_ROD);
            } else { player.setGlowing(false); }
        }
    }
    public void lessparticleSpawn(Player player, Particle particle) {

        if (player.isSneaking()) {player.getWorld().spawnParticle(particle,player.getLocation().add(0,0.65,0),1,0.25,0.15,0.25,0.003);}
        else{player.getWorld().spawnParticle(particle,player.getLocation().add(0,1,0),1,0.25,0.15,0.25,0.003);}
        if (player.getGameMode().equals(GameMode.SPECTATOR)) {player.getWorld().spawnParticle(particle,player.getEyeLocation().add(0,-0.25,0),1,0.25,0.15,0.25,0.003);}

    }
}