package net.sashegdev.egg;

import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

public class DragonEggTask extends BukkitRunnable {

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

    public void lore(Player player) {
        ItemStack dragonEgg = new ItemStack(Material.DRAGON_EGG);
        ItemMeta meta = dragonEgg.getItemMeta();
        List<String> lore = new ArrayList<>();

        String tip = "ЛЕГЕНДАРНЫЙ";
        String debuff = "РАДИАЦИОННОЕ ЗАРАЖЕНИЕ";
        int damage = 11;
        int distantion = 50;
        int cost = 17;
        float cooldown = 35F;
        float lesscooldown = 12F;


        lore.add(ChatColor.RESET+"Тип артефакта: " + ChatColor.GOLD +ChatColor.BOLD + tip);
        lore.add("");
        lore.add(ChatColor.RESET+"Урон: " + ChatColor.RED +damage+" HP" + ChatColor.RESET + " + " + ChatColor.RED + ChatColor.BOLD + "ПОДОЖЖЁН!");
        
        //if (player.getLevel() >= cost || player.getGameMode() == GameMode.CREATIVE) 
        if (player.getLevel() >= cost)
        {
            lore.add(ChatColor.RESET+"Тип урона: "+ChatColor.YELLOW+"Одиночная цель"+ChatColor.WHITE+" | | "+ChatColor.RED+"(ALT-Режим)"+ChatColor.YELLOW+" Урон по площади");
        }
        else {lore.add(ChatColor.RESET+"Тип урона: "+ChatColor.YELLOW+"Одиночная цель");}
        
        //if (player.getLevel() >= cost || player.getGameMode() == GameMode.CREATIVE) 
        if (player.getLevel() >= cost)
        {
            lore.add(ChatColor.RESET+"Вид атаки: " + ChatColor.GOLD+ChatColor.BOLD + "ЛУЧ" + ChatColor.RESET+ChatColor.WHITE+ " | | "+ChatColor.RESET+"Дистанция: " + ChatColor.RED + ChatColor.BOLD +distantion+" метров");
        }
        else {lore.add(ChatColor.RESET+"Вид Атаки: "+ChatColor.GOLD + "Характеристика заблокирована");}
        
        lore.add("");
        lore.add(ChatColor.RESET+"Стоимость за использование: " + ChatColor.GOLD + ChatColor.BOLD + cost + ChatColor.RESET + ChatColor.GOLD + " Уровней(XP)");
        
        //if (player.getLevel() >= cost || player.getGameMode() == GameMode.CREATIVE) 
        if (player.getLevel() >= cost)
        {
            lore.add(ChatColor.RESET+"Перезарядка: "+ChatColor.AQUA+lesscooldown+" секунд"+ChatColor.WHITE+ " | | "+ChatColor.RED+"(ALT-Режим) "+ChatColor.RESET+cooldown+" секунд");
        }
        else {lore.add(ChatColor.RESET+"Перезарядка: "+ChatColor.AQUA+lesscooldown+" секунд"+ChatColor.WHITE);}
        
        //if (player.getLevel() >= cost || player.getGameMode() == GameMode.CREATIVE)
        if (player.getLevel() >= cost)
        {
            lore.add(ChatColor.RESET+"ALT-Режим: "+ChatColor.RED+ChatColor.BOLD+"Огненный потрошитель"+ChatColor.BLUE+"(x5 Урон, x3 Стоимость)");
        }
        else {lore.add(ChatColor.RESET+"ALT-Режим: "+ChatColor.RED+"Характеристика заблокирована");}
        
        lore.add("");
        
        //if (player.getLevel() >= cost || player.getGameMode() == GameMode.CREATIVE) 
        if (player.getLevel() >= cost)
        {
            lore.add(ChatColor.RESET+"Дебафф: " + ChatColor.DARK_RED + ChatColor.BOLD + ChatColor.UNDERLINE + debuff);
        }
        else {lore.add(ChatColor.RESET+"Дебафф: " + ChatColor.RED + "Характеристика заблокирова");}
        
        //if (player.getLevel() >= cost || player.getGameMode() == GameMode.CREATIVE)
        if (player.getLevel() >= cost)
        {
            lore.add(ChatColor.GOLD+"Доп механика: "+ChatColor.DARK_PURPLE+ChatColor.BOLD +"Пустотный поглатитель" +ChatColor.RED+ " (ПКМ по энтити)");
        }
        else {lore.add(ChatColor.GOLD+"Доп механика: "+ChatColor.RED+"Характеристика заблокирована");}

        meta.setLore(lore);
        dragonEgg.setItemMeta(meta);

        PlayerInventory inventory = player.getInventory();
        ItemStack[] items = inventory.getContents();

        for (int i = 0; i < items.length; i++) {
            ItemStack item = items[i];
            if (item != null && item.getType() == Material.DRAGON_EGG) {
                items[i] = dragonEgg;
            }
        }

        inventory.setContents(items);
    }
}