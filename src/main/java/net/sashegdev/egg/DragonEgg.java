package net.sashegdev.egg;

import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.HashMap;

import static net.sashegdev.egg.EggHitDetector.luch;
import static net.sashegdev.egg.ParticleManager.particleSpawn;
import static net.sashegdev.egg.SoundManager.playSound;

public class DragonEgg implements Listener {
    // знакомся - это то, как будет выглядеть каждый класс для нового предмета)

    HashMap<Player, Long> cooldowns = new HashMap<>();

    @EventHandler
    public void interact(PlayerInteractEntityEvent event) {
        Player pl = event.getPlayer();
        Entity victim = event.getRightClicked();

        if (pl.getLevel() < 17 && pl.getGameMode() != GameMode.CREATIVE) return;

        if (pl.getInventory().getItemInMainHand().getType().equals(Material.DRAGON_EGG)) {
            Player kick = victim.getServer().getPlayer(victim.getUniqueId());

            double distance = pl.getLocation().distance(victim.getLocation());

            for (double i = 0; i < distance; i += 0.5) {
                Location particleLocation = pl.getEyeLocation().add(pl.getEyeLocation().getDirection().multiply(i));

                // Проверка на блок перед лучом
                if (!particleLocation.getBlock().isPassable()) {
                    break;
                }

                pl.getWorld().spawnParticle(Particle.FLAME, particleLocation, 1, 0, 0, 0, 0, null);
                pl.getWorld().spawnParticle(Particle.SMALL_FLAME, particleLocation, 1, 0, 0, 0, 0, null);
                pl.getWorld().spawnParticle(Particle.CAMPFIRE_COSY_SMOKE, particleLocation, 1, 0, 0, 0, 0, null);
            }


            if (!(kick instanceof Player)) {
                victim.remove();
                pl.damage(5);
                playSound(pl.getLocation(), Sound.ENTITY_WITHER_SKELETON_DEATH, 1, 0, 0,10);
                pl.addPotionEffect(new PotionEffect(PotionEffectType.SATURATION, 20, 3));
            } else {

                //kick.teleport(vic_loc);
                //kick.kickPlayer("Server error: Couldn't find a player " + kick.getName());
                // по поводу этого(2 строки выше) это был концепт того, как должо было яйцо работать

                if (kick.getBedSpawnLocation() != null) {
                    kick.teleport(kick.getBedSpawnLocation());
                } else kick.teleport((kick.getWorld().getSpawnLocation()));
                kick.damage(18);
                pl.damage(5);
                pl.addPotionEffect(new PotionEffect(PotionEffectType.SATURATION, 20, 3));
            }
        }
    }


    @EventHandler
    public void attack(PlayerInteractEvent event) {
        Player pl = event.getPlayer();
        int cost = 17;
        int cooldownTicks = 35 * 20;
        int lesscooldownticks = 12 * 20;

        if (((event.getAction().equals(Action.LEFT_CLICK_AIR) || event.getAction().equals(Action.LEFT_CLICK_BLOCK)) && pl.getInventory().getItemInMainHand().getType().equals(Material.DRAGON_EGG)) && (pl.getGameMode() == GameMode.CREATIVE && pl.hasPotionEffect(PotionEffectType.INVISIBILITY) || pl.getGameMode().equals(GameMode.SPECTATOR))) {
            luch(pl);
            return;
        }

        // если у игрока недостаточнгоо уровней, то выходим из метода)
        if (((event.getAction().equals(Action.LEFT_CLICK_AIR) || event.getAction().equals(Action.LEFT_CLICK_BLOCK)) && pl.getInventory().getItemInMainHand().getType().equals(Material.DRAGON_EGG)) && (pl.getLevel() < cost)) {
            particleSpawn(pl, Particle.SMOKE_NORMAL,40);
            playSound(pl.getLocation(), Sound.ENTITY_BLAZE_SHOOT, 1, 0, 0,10);
            return;
        }

        if ((event.getAction().equals(Action.LEFT_CLICK_AIR) || event.getAction().equals(Action.LEFT_CLICK_BLOCK)) && pl.getInventory().getItemInMainHand().getType().equals(Material.DRAGON_EGG)) {
            if (cooldowns.containsKey(pl)) {
                long lastUse = cooldowns.get(pl);
                long currentTime = System.currentTimeMillis();
                long cooldownTime = pl.isSneaking() && pl.isOnGround() ? cooldownTicks : lesscooldownticks; // Checking if the player is sneaking and on the ground
                if ((currentTime - lastUse) < (cooldownTime * 50)) {
                    long timeLeft = (lastUse + cooldownTime * 50 - currentTime) / 1000;
                    particleSpawn(pl, Particle.LAVA,5);
                    playSound(pl.getLocation(), Sound.BLOCK_LAVA_EXTINGUISH, 1, 0, 0,10);
                    pl.sendTitle(ChatColor.DARK_RED + "Перегрев!", ChatColor.GRAY + "Подожди: " + ChatColor.GOLD + timeLeft + ChatColor.GRAY + " секунд", 10, 10, 10);
                    return; // выходим из метода если у игрока кд не прошло
                }
            }

            if (!(pl.getGameMode().equals(GameMode.CREATIVE))) {
                int cur_level = pl.getLevel();
                int lost_level = cur_level - cost;
                if (pl.isSneaking() && pl.isOnGround()) {
                    if (pl.getLevel() > cost * 3) {
                        lost_level -= cost+cost; // Если альт-режим - тройная стоимость(выше уже вычитали 1 стоимость, тут ещё 2)
                    } else {
                        particleSpawn(pl, Particle.SMOKE_NORMAL,40);
                        playSound(pl.getLocation(), Sound.ENTITY_BLAZE_SHOOT, 1, 0, 0,10);
                        return;
                    }
                }
                pl.setLevel(lost_level);
                luch(pl);
                cooldowns.put(pl, System.currentTimeMillis()); // сохраняем время использования
            }

            // Apply cooldown even if the player is in creative mode
            long cooldownTime = pl.isSneaking() && pl.isOnGround() ? cooldownTicks : lesscooldownticks;
            if (cooldownTime > 0) {
                cooldowns.put(pl, System.currentTimeMillis()); // сохраняем время использования
            }

            if ((event.getAction().equals(Action.LEFT_CLICK_AIR) || event.getAction().equals(Action.LEFT_CLICK_BLOCK)) && pl.getInventory().getItemInMainHand().getType().equals(Material.DRAGON_EGG) && pl.getGameMode().equals(GameMode.CREATIVE)) {
                luch(pl);
                cooldowns.put(pl, System.currentTimeMillis()); // сохраняем время использования
            }
            cooldownTime = pl.isSneaking() && pl.isOnGround() ? cooldownTicks : lesscooldownticks;
            if (cooldownTime > 0) {
                cooldowns.put(pl, System.currentTimeMillis()); // сохраняем время использования
            }
        }
    }

}
