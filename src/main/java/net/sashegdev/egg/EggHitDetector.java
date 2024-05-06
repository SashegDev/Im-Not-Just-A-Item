package net.sashegdev.egg;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import static net.sashegdev.egg.SoundManager.playSound;

public class EggHitDetector {
    //Я потом реализую оптимизацию этому говну

    public static void luch(Player player) {
        int baseDamage = 11 * 2;
        int dist = 50;
        Location playerLocation = player.getEyeLocation();
        Location targetLocation = playerLocation.clone().add(playerLocation.getDirection().multiply(dist));
        double distance = playerLocation.distance(targetLocation);
        int damageMultiplier = 1;
        if (player.isSneaking() && player.isOnGround()) {
            damageMultiplier = 5;
        }
        if (player.isSneaking() && player.isOnGround()) {
            distance = distance * 2;
        }

        for (double i = 0; i < distance; i += 0.1) {

            Location particleLocation = playerLocation.clone().add(playerLocation.getDirection().multiply(i));
            if (!(player.isSneaking() && player.isOnGround())) {
                player.getWorld().spawnParticle(Particle.END_ROD, particleLocation, 1, 0, 0, 0, 0, null);
                player.getWorld().spawnParticle(Particle.SOUL_FIRE_FLAME, particleLocation, 1, 0, 0, 0, 0, null);
                playSound(playerLocation, Sound.ENTITY_SHULKER_SHOOT, 0.1F, 0, 1,40);
            }
            if (player.isSneaking() && player.isOnGround()) {
                particleLocation.getWorld().spawnParticle(Particle.END_ROD, particleLocation.add(0, 0, 0), 1, 0, 0, 0, 0, null);
                particleLocation.getWorld().spawnParticle(Particle.FLAME, particleLocation.add(0, 0, 0), 1, 0, 0, 0, 0, null);
                particleLocation.getWorld().spawnParticle(Particle.SOUL_FIRE_FLAME, particleLocation.add(0, 0, 0), 1, 0, 0, 0, 0, null);
                particleLocation.getWorld().spawnParticle(Particle.CAMPFIRE_SIGNAL_SMOKE, particleLocation.add(0, 0, 0), 1, 0, 0, 0, 0, null);
                playSound(playerLocation, Sound.ENTITY_SHULKER_SHOOT, 0.1F, 0, 1,60);
            }


            // Проверка на блок перед лучом
            if (!particleLocation.getBlock().isPassable()) {
                if (player.isSneaking() && player.isOnGround()) {
                    particleLocation.getWorld().spawnParticle(Particle.FLAME, particleLocation.add(0, 0, 0), 40, 0.25, 0.4, 0.25, 0.003);
                    particleLocation.getWorld().spawnParticle(Particle.PORTAL, particleLocation.add(0, 0, 0), 40, 0.25, 0.4, 0.25, 0.003);
                    particleLocation.getWorld().spawnParticle(Particle.CAMPFIRE_COSY_SMOKE, particleLocation.add(0, 1, 0), 40, 0.25, 0.4, 0.25, 0.003);
                    TNTPrimed tnt = particleLocation.getWorld().spawn(particleLocation, TNTPrimed.class);
                    tnt.setFuseTicks(0);
                    TNTPrimed tnt1 = particleLocation.getWorld().spawn(particleLocation, TNTPrimed.class);
                    tnt1.setFuseTicks(0);
                    TNTPrimed tnt2 = particleLocation.getWorld().spawn(particleLocation, TNTPrimed.class);
                    tnt2.setFuseTicks(0);
                    TNTPrimed tnt3 = particleLocation.getWorld().spawn(particleLocation, TNTPrimed.class);
                    tnt3.setFuseTicks(0);
                    TNTPrimed tnt4 = particleLocation.getWorld().spawn(particleLocation, TNTPrimed.class);
                    tnt4.setFuseTicks(0);
                    TNTPrimed tnt5 = particleLocation.getWorld().spawn(particleLocation, TNTPrimed.class);
                    tnt5.setFuseTicks(0);
                }
                else {
                    particleLocation.getWorld().spawnParticle(Particle.LAVA, particleLocation.add(0, 1, 0), 80, 0.25, 0.4, 0.25, 0.003);
                    //particleLocation.getWorld().spawnParticle(Particle.PORTAL, particleLocation.add(0, 1, 0), 40, 0.25, 0.4, 0.25, 0.003);
                    particleLocation.getWorld().spawnParticle(Particle.CAMPFIRE_COSY_SMOKE, particleLocation.add(0, 0.5, 0), 90, 0.25, 0.4, 0.25, 0.02);
                    particleLocation.getWorld().spawnParticle(Particle.CAMPFIRE_SIGNAL_SMOKE, particleLocation.add(0, 0.5, 0), 30, 0.25, 0.4, 0.25, 0.015);
                }
                break;
            }

            // Проверяем, попали ли мы в существо
            Entity hitEntity = null;
            for (Entity entity : player.getWorld().getNearbyEntities(particleLocation, 0.7, 0.3, 0.7)) {
                if (entity instanceof LivingEntity && !entity.equals(player)) {
                    hitEntity = entity;
                    break;
                }
            }
            if (hitEntity != null) {
                // Действия при попадании в существо
                playSound(particleLocation, Sound.ENTITY_GENERIC_EXTINGUISH_FIRE, 3, 0, 0,30);
                ((LivingEntity) hitEntity).setFireTicks(200);
                ((LivingEntity) hitEntity).addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, 220, 1));
                ((LivingEntity) hitEntity).addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 220, 3));
                ((LivingEntity) hitEntity).addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 220, 6));
                ((LivingEntity) hitEntity).addPotionEffect(new PotionEffect(PotionEffectType.LEVITATION, 220, 0));
                ((LivingEntity) hitEntity).addPotionEffect(new PotionEffect(PotionEffectType.SLOW_FALLING, 800, 3));
                if (!player.getGameMode().equals(GameMode.SPECTATOR)) {
                    ((LivingEntity) hitEntity).damage(baseDamage * damageMultiplier, player);
                } else {
                    ((LivingEntity) hitEntity).damage(baseDamage * damageMultiplier);
                }
                particleLocation.getWorld().spawnParticle(Particle.LAVA, particleLocation.add(0, 0, 0), 40, 0.25, 0.4, 0.25, 0.003);
                particleLocation.getWorld().spawnParticle(Particle.SMOKE_LARGE, particleLocation.add(0, 0, 0), 40, 0.25, 0.4, 0.25, 0.003);
                particleLocation.getWorld().spawnParticle(Particle.END_ROD, particleLocation.add(0, 0, 0), 40, 0.25, 0.4, 0.25, 0.003);
                particleLocation.getWorld().spawnParticle(Particle.PORTAL, particleLocation.add(0, 0, 0), 80, 0.5, 0.5, 0.5, 0.003);
                if (player.isSneaking() && player.isOnGround()) {
                    TNTPrimed tnt = particleLocation.getWorld().spawn(particleLocation, TNTPrimed.class);
                    tnt.setFuseTicks(0);
                    TNTPrimed tnt1 = particleLocation.getWorld().spawn(particleLocation, TNTPrimed.class);
                    tnt1.setFuseTicks(0);
                    TNTPrimed tnt2 = particleLocation.getWorld().spawn(particleLocation, TNTPrimed.class);
                    tnt2.setFuseTicks(0);
                    TNTPrimed tnt3 = particleLocation.getWorld().spawn(particleLocation, TNTPrimed.class);
                    tnt3.setFuseTicks(0);
                    TNTPrimed tnt4 = particleLocation.getWorld().spawn(particleLocation, TNTPrimed.class);
                    tnt4.setFuseTicks(0);
                    TNTPrimed tnt5 = particleLocation.getWorld().spawn(particleLocation, TNTPrimed.class);
                    tnt5.setFuseTicks(0);
                }
                break;
            }
        }
    }



}
