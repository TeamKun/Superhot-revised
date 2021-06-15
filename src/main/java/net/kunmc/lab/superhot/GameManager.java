package net.kunmc.lab.superhot;

import com.destroystokyo.paper.event.player.PlayerLaunchProjectileEvent;
import com.destroystokyo.paper.event.player.PlayerReadyArrowEvent;
import net.kunmc.lab.superhot.event.StateChangeEvent;
import net.kunmc.lab.superhot.helper.ItemHelper;
import net.kunmc.lab.superhot.listener.*;
import net.kunmc.lab.superhot.state.EntityVelocityHolder;
import net.kunmc.lab.superhot.state.IState;
import net.kunmc.lab.superhot.state.Moving;
import net.kunmc.lab.superhot.state.Stopping;
import net.kunmc.lab.superhot.task.MainPlayerMoveObserver;
import net.kunmc.lab.superhot.util.Utils;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.block.*;
import org.bukkit.event.entity.*;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;
import org.bukkit.util.Vector;

import java.util.UUID;

public class GameManager {
    private UUID mainPlayerUUID;
    private boolean isSuperhotEnabled;
    private boolean isMainPlayerMoving;
    private IState state = new Stopping();
    private EntityVelocityHolder holder = EntityVelocityHolder.getInstance();
    private static final GameManager singleton = new GameManager();

    private GameManager() {
    }

    public static GameManager getInstance() {
        return singleton;
    }

    public void start(Player target) {
        mainPlayerUUID = target.getUniqueId();
        isSuperhotEnabled = true;
        isMainPlayerMoving = true;

        JavaPlugin plugin = Superhot.getInstance();
        PluginManager pluginManager = Bukkit.getServer().getPluginManager();
        pluginManager.registerEvents(new BlockChangeListener(), plugin);
        pluginManager.registerEvents(new EntityCombustListener(), plugin);
        pluginManager.registerEvents(new EntityDamageListener(), plugin);
        pluginManager.registerEvents(new EntitySpawnListener(), plugin);
        pluginManager.registerEvents(new ItemDropListener(), plugin);
        pluginManager.registerEvents(new MainPlayerAttackListener(), plugin);
        pluginManager.registerEvents(new NotMainPlayerActListener(), plugin);
        pluginManager.registerEvents(new PlayerAttemptSwapListener(), plugin);
        pluginManager.registerEvents(new PlayerJoinListener(), plugin);
        pluginManager.registerEvents(new PlayerQuitListener(), plugin);
        pluginManager.registerEvents(new ProjectileHitListener(), plugin);
        pluginManager.registerEvents(new ProjectileLaunchListener(), plugin);
        pluginManager.registerEvents(new StateChangeListener(), plugin);
        pluginManager.registerEvents(new SuperhotBulletHitListener(), plugin);
        pluginManager.registerEvents(new SuperhotGunUsedListener(), plugin);

        new MainPlayerMoveObserver()
                .runTaskTimerAsynchronously(Superhot.getInstance(), 0, 0);

        ItemStack gun = ItemHelper.createItem(Const.gunMaterial, Const.gunName, 1);
        ItemStack ammo = ItemHelper.createItem(Const.ammoMaterial, Const.ammoName, Config.ammoAmount);
        ItemStack swapTool = ItemHelper.createItem(Const.swapToolMaterial, Const.swapToolName, 1);

        Bukkit.getOnlinePlayers().forEach(x -> x.getInventory().addItem(gun, ammo));
        target.getInventory().addItem(swapTool);

        Scoreboard scoreboard = Bukkit.getScoreboardManager().getMainScoreboard();
        Team enemyTeam = scoreboard.getTeam(Const.enemyTeamName);
        if (enemyTeam == null) {
            enemyTeam = Bukkit.getScoreboardManager().getMainScoreboard().registerNewTeam(Const.enemyTeamName);
        }
        enemyTeam.color(NamedTextColor.RED);
        Bukkit.getOnlinePlayers().stream()
                .filter(x -> !x.getUniqueId().equals(mainPlayerUUID))
                .filter(x -> !Utils.isCreativeOrSpectator(x))
                .forEach(this::addToTeam);
    }

    public void stop() {
        mainPlayerUUID = null;
        isSuperhotEnabled = false;
        isMainPlayerMoving = false;

        JavaPlugin plugin = Superhot.getInstance();
        BlockBreakEvent.getHandlerList().unregister(plugin);
        BlockBurnEvent.getHandlerList().unregister(plugin);
        BlockDamageEvent.getHandlerList().unregister(plugin);
        BlockFromToEvent.getHandlerList().unregister(plugin);
        BlockGrowEvent.getHandlerList().unregister(plugin);
        BlockIgniteEvent.getHandlerList().unregister(plugin);
        BlockPlaceEvent.getHandlerList().unregister(plugin);
        EntityChangeBlockEvent.getHandlerList().unregister(plugin);
        EntityCombustByBlockEvent.getHandlerList().unregister(plugin);
        EntityCombustByEntityEvent.getHandlerList().unregister(plugin);
        EntityCombustEvent.getHandlerList().unregister(plugin);
        EntityDamageByBlockEvent.getHandlerList().unregister(plugin);
        EntityDamageByEntityEvent.getHandlerList().unregister(plugin);
        EntityDamageByEntityEvent.getHandlerList().unregister(plugin);
        EntityDamageEvent.getHandlerList().unregister(plugin);
        EntityShootBowEvent.getHandlerList().unregister(plugin);
        EntitySpawnEvent.getHandlerList().unregister(plugin);
        LeavesDecayEvent.getHandlerList().unregister(plugin);
        NotePlayEvent.getHandlerList().unregister(plugin);
        PlayerDropItemEvent.getHandlerList().unregister(plugin);
        PlayerGameModeChangeEvent.getHandlerList().unregister(plugin);
        PlayerInteractEvent.getHandlerList().unregister(plugin);
        PlayerJoinEvent.getHandlerList().unregister(plugin);
        PlayerLaunchProjectileEvent.getHandlerList().unregister(plugin);
        PlayerQuitEvent.getHandlerList().unregister(plugin);
        PlayerReadyArrowEvent.getHandlerList().unregister(plugin);
        PlayerRespawnEvent.getHandlerList().unregister(plugin);
        ProjectileHitEvent.getHandlerList().unregister(plugin);
        ProjectileLaunchEvent.getHandlerList().unregister(plugin);
        StateChangeEvent.getHandlerList().unregister(plugin);

        Bukkit.getScheduler().cancelTasks(Superhot.getInstance());
        Bukkit.selectEntities(Bukkit.getConsoleSender(), "@e").forEach(this::restoreEntityState);

        Team team = Bukkit.getScoreboardManager().getMainScoreboard().getTeam(Const.enemyTeamName);
        if (team != null) {
            team.unregister();
        }
    }

    public void changeState(IState state) {
        this.state = state;
        Bukkit.getScheduler().runTask(Superhot.getInstance(), () -> Bukkit.getPluginManager().callEvent(new StateChangeEvent(state.getClass())));
    }

    public void restoreEntityState(Entity entity) {
        entity.setGravity(true);
        Vector velocity = holder.getVelocity(entity.getUniqueId());
        if (velocity != null) {
            entity.setVelocity(velocity);
        }

        if (entity instanceof LivingEntity) {
            LivingEntity living = ((LivingEntity) entity);
            living.setAI(true);
        }

        if (entity instanceof Player) {
            Player p = ((Player) entity);
            p.setAllowFlight(Utils.isCreativeOrSpectator(p));
            p.setFlying(Utils.isCreativeOrSpectator(p) && p.isFlying());
            p.setWalkSpeed(0.2F);
            p.setFlySpeed(0.1F);
            removeFromTeam(p);
        }

        if (entity instanceof Item) {
            Item item = ((Item) entity);
            item.setCanMobPickup(true);
            item.setCanPlayerPickup(true);
            item.setPickupDelay(20);
        }
    }

    public void updateEntity(Entity entity) {
        state.updateEntity(entity);
    }

    public void updateAllEntities() {
        Player p = Bukkit.getPlayer(mainPlayerUUID);
        if (p == null) return;

        Bukkit.selectEntities(p, "@e").parallelStream().forEach(x -> {
            if (x.equals(p)) {
                return;
            }

            if (x instanceof Player && Utils.isCreativeOrSpectator(((Player) x))) {
                return;
            }

            updateEntity(x);
        });
    }

    public boolean isStateMoving() {
        return this.state.getClass().equals(Moving.class);
    }

    public boolean isMainPlayerMoving() {
        return isMainPlayerMoving;
    }

    public void setMainPlayerMoving(boolean isMainPlayerMoving) {
        this.isMainPlayerMoving = isMainPlayerMoving;
    }

    public boolean isSuperhotEnabled() {
        return isSuperhotEnabled;
    }

    public UUID getMainPlayerUUID() {
        return mainPlayerUUID;
    }

    public void advanceTime(long tick) {
        changeState(new Moving());
        updateAllEntities();

        new BukkitRunnable() {
            @Override
            public void run() {
                changeState(isMainPlayerMoving ? new Moving() : new Stopping());
                updateAllEntities();
            }
        }.runTaskLater(Superhot.getInstance(), tick);
    }

    public void addToTeam(Player target) {
        if (Config.isGlowModeEnabled) {
            Team enemyTeam = Bukkit.getScoreboardManager().getMainScoreboard().getTeam(Const.enemyTeamName);
            if (enemyTeam == null) {
                return;
            }
            enemyTeam.addEntry(target.getName());
            target.setGlowing(true);
        }
    }

    public void removeFromTeam(Player target) {
        Team enemyTeam = Bukkit.getScoreboardManager().getMainScoreboard().getTeam(Const.enemyTeamName);
        if (enemyTeam == null) {
            return;
        }
        enemyTeam.removeEntry(target.getName());
        target.setGlowing(false);
    }
}
