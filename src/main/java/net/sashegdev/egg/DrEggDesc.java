package net.sashegdev.egg;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class DrEggDesc {
    // потом реализую норм дескрипшин метод

    public static void lore(Player player) {

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


        lore.add(ChatColor.RESET + "Тип артефакта: " + ChatColor.GOLD + ChatColor.BOLD + tip);
        lore.add("");
        lore.add(ChatColor.RESET + "Урон: " + ChatColor.RED + damage + " HP" + ChatColor.RESET + " + " + ChatColor.RED + ChatColor.BOLD + "ПОДОЖЖЁН!");

        //if (player.getLevel() >= cost || player.getGameMode() == GameMode.CREATIVE)
        if (player.getLevel() >= cost) {
            lore.add(ChatColor.RESET + "Тип урона: " + ChatColor.YELLOW + "Одиночная цель" + ChatColor.WHITE + " | | " + ChatColor.RED + "(ALT-Режим)" + ChatColor.YELLOW + " Урон по площади");
        } else {
            lore.add(ChatColor.RESET + "Тип урона: " + ChatColor.YELLOW + "Одиночная цель");
        }

        //if (player.getLevel() >= cost || player.getGameMode() == GameMode.CREATIVE)
        if (player.getLevel() >= cost) {
            lore.add(ChatColor.RESET + "Вид атаки: " + ChatColor.GOLD + ChatColor.BOLD + "ЛУЧ" + ChatColor.RESET + ChatColor.WHITE + " | | " + ChatColor.RESET + "Дистанция: " + ChatColor.RED + ChatColor.BOLD + distantion + " метров");
        } else {
            lore.add(ChatColor.RESET + "Вид Атаки: " + ChatColor.GOLD + "Характеристика заблокирована");
        }

        lore.add("");
        lore.add(ChatColor.RESET + "Стоимость за использование: " + ChatColor.GOLD + ChatColor.BOLD + cost + ChatColor.RESET + ChatColor.GOLD + " Уровней(XP)");

        //if (player.getLevel() >= cost || player.getGameMode() == GameMode.CREATIVE)
        if (player.getLevel() >= cost) {
            lore.add(ChatColor.RESET + "Перезарядка: " + ChatColor.AQUA + lesscooldown + " секунд" + ChatColor.WHITE + " | | " + ChatColor.RED + "(ALT-Режим) " + ChatColor.RESET + cooldown + " секунд");
        } else {
            lore.add(ChatColor.RESET + "Перезарядка: " + ChatColor.AQUA + lesscooldown + " секунд" + ChatColor.WHITE);
        }

        //if (player.getLevel() >= cost || player.getGameMode() == GameMode.CREATIVE)
        if (player.getLevel() >= cost) {
            lore.add(ChatColor.RESET + "ALT-Режим: " + ChatColor.RED + ChatColor.BOLD + "Огненный потрошитель" + ChatColor.BLUE + "(x5 Урон, x3 Стоимость)");
        } else {
            lore.add(ChatColor.RESET + "ALT-Режим: " + ChatColor.RED + "Характеристика заблокирована");
        }

        lore.add("");

        //if (player.getLevel() >= cost || player.getGameMode() == GameMode.CREATIVE)
        if (player.getLevel() >= cost) {
            lore.add(ChatColor.RESET + "Дебафф: " + ChatColor.DARK_RED + ChatColor.BOLD + ChatColor.UNDERLINE + debuff);
        } else {
            lore.add(ChatColor.RESET + "Дебафф: " + ChatColor.RED + "Характеристика заблокирова");
        }

        //if (player.getLevel() >= cost || player.getGameMode() == GameMode.CREATIVE)
        if (player.getLevel() >= cost) {
            lore.add(ChatColor.GOLD + "Доп механика: " + ChatColor.DARK_PURPLE + ChatColor.BOLD + "Пустотный поглатитель" + ChatColor.RED + " (ПКМ по энтити)");
        } else {
            lore.add(ChatColor.GOLD + "Доп механика: " + ChatColor.RED + "Характеристика заблокирована");
        }

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