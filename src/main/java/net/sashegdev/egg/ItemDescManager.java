package net.sashegdev.egg;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import java.util.List;

public class ItemDescManager {
    //Этот класс - типо апишка, я потом реализую мультилорность

    public static void setDesc(Player player, ItemStack item, List<String> description, String comparison, int level) {
        int playerLevel = player.getLevel();

        if (comparison.equals(">=") && playerLevel >= level) {
            List<String> lore = item.getItemMeta().getLore();
            if (lore == null) {
                lore = description;
            } else {
                lore.addAll(description);
            }
            item.getItemMeta().setLore(lore);
        } else if (comparison.equals("<=") && playerLevel <= level) {
            List<String> lore = item.getItemMeta().getLore();
            if (lore == null) {
                lore = description;
            } else {
                lore.addAll(description);
            }
            item.getItemMeta().setLore(lore);
        }
    }
}
