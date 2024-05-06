package net.sashegdev.egg;

import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;


import static net.sashegdev.egg.DrEggDesc.lore;


public class DragonEggTask extends BukkitRunnable {
    // решил не трогать :)

    public void run() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            boolean hazmat = isHazmat(player);
            if ((player.getInventory().contains(Material.DRAGON_EGG)) && !(player.getGameMode().equals(GameMode.CREATIVE)) && !hazmat) {
            //if ((player.getInventory().contains(Material.DRAGON_EGG)) && !(player.getGameMode().equals(GameMode.CREATIVE))) {
                player.damage(1);
                player.setFreezeTicks(120);
                if (!player.getGameMode().equals(GameMode.SPECTATOR)) {
                    particleSpawn(player, Particle.SOUL_FIRE_FLAME);
                }
                player.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 80, 4, false, false));
                player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 80, 1, false, false));

                if (player.getInventory().getItemInMainHand().getType().equals(Material.DRAGON_EGG)) {
                    player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 80, 1, false, false));
                    player.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 80, 4, false, false));
                    player.damage(2.5);
                    player.setFreezeTicks(520);
                    if (!player.getGameMode().equals(GameMode.SPECTATOR)) {
                        particleSpawn(player, Particle.SOUL_FIRE_FLAME);
                    }
                }
            }
            if ((player.getInventory().contains(Material.DRAGON_EGG)) && !(player.getGameMode().equals(GameMode.CREATIVE)) && hazmat) {
            //if ((player.getInventory().contains(Material.DRAGON_EGG)) && !(player.getGameMode().equals(GameMode.CREATIVE))) {
                player.setFreezeTicks(40);
                if (!player.getGameMode().equals(GameMode.SPECTATOR)) {
                    particleSpawn(player, Particle.SOUL_FIRE_FLAME);
                }
            }
            if (player.getInventory().contains(Material.DRAGON_EGG)) {
                lore(player);
            }

        }
    }

    private static boolean isHazmat(Player player) {
        ItemStack cap = player.getInventory().getHelmet();
        ItemStack curtka = player.getInventory().getChestplate();
        ItemStack shtani = player.getInventory().getLeggings();
        ItemStack boots = player.getInventory().getBoots();

        boolean a = false;
        if (cap != null && curtka != null && shtani != null && boots != null) {
            a = (cap.getType().equals(Material.LEATHER_HELMET) && curtka.getType().equals(Material.LEATHER_CHESTPLATE) && shtani.getType().equals(Material.LEATHER_LEGGINGS) && boots.getType().equals(Material.LEATHER_BOOTS));
        }

        //player.sendMessage(String.valueOf(a));
        return a;
    }


    public void particleSpawn(Player player, Particle particle) {

        player.getWorld().spawnParticle(particle, player.getLocation().add(0, 1, 0), 40, 0.25, 0.4, 0.25, 0.003);

    }
}