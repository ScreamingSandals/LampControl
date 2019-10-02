package cz.ceph.lampcontrol.events;

import cz.ceph.lampcontrol.LampControl;
import cz.ceph.lampcontrol.utils.ChatWriter;
import cz.ceph.lampcontrol.utils.SoundPlayer;
import cz.ceph.lampcontrol.utils.GetBlock;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import static cz.ceph.lampcontrol.LampControl.getMain;

/**
 * Created by Ceph on 09.12.2018
 */

public class ReflectPlayerInteractEvent implements ReflectEvent.Callback {

    private LampControl plugin;
    public ReflectPlayerInteractEvent(LampControl plugin) {
        this.plugin = plugin;
    }

    @Override
    public void execute(Event instance) {
        PlayerInteractEvent event = (PlayerInteractEvent) instance;
        Block block = event.getClickedBlock();
        Player player = event.getPlayer();
        Location location = player.getLocation();

        if (!event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) return;
        if (block == null) return;
        if (player.isSneaking()) return;

        if (block.getType().equals(Material.REDSTONE_LAMP)) {
            if (checkPermissions(event)) {
                if (GetBlock.getLampStatus(false, block)) {
                    plugin.getSwitchBlock().initWorld(block.getWorld());
                    plugin.getSwitchBlock().switchLamp(block, true);

                    SoundPlayer.play(location, SoundPlayer.success(), 0.5F, 0F);
                } else {
                    plugin.getSwitchBlock().initWorld(block.getWorld());
                    plugin.getSwitchBlock().switchLamp(block, false);

                    SoundPlayer.play(location, SoundPlayer.success(), 0.5F, 0F);
                }
            } else {
                player.sendMessage(ChatWriter.prefix(LampControl.localization.get("error.no_permissions")));
                SoundPlayer.play(location, SoundPlayer.fail(), 0.5F, 0F);
            }

        } else if (block.getType().equals(Material.POWERED_RAIL)) {
            if (checkPermissions(event)) {
                if (GetBlock.getRailStatus(false, block)) {
                    plugin.getSwitchBlock().initWorld(block.getWorld());
                    plugin.getSwitchBlock().switchRail(block, true);

                    SoundPlayer.play(location, SoundPlayer.success(), 0.5F, 0F);
                } else {
                    plugin.getSwitchBlock().initWorld(block.getWorld());
                    plugin.getSwitchBlock().switchRail(block, false);

                    SoundPlayer.play(location, SoundPlayer.success(), 0.5F, 0F);
                }
            } else {
                player.sendMessage(ChatWriter.prefix(LampControl.localization.get("error.no_permissions")));
                SoundPlayer.play(location, SoundPlayer.fail(), 0.5F, 0F);
            }
        }
    }

    private boolean checkPermissions(PlayerInteractEvent event) {
        if (getMain().cachedBooleanValues.get("enable-permissions") || checkCachedValues("control-OP") || checkCachedValues("enable-lightItem")) {
            if (event.getPlayer().hasPermission("lampcontrol.use") || event.getPlayer().isOp() || checkLightItem(event.getPlayer())) {
                return true;
            }
        }
        return false;
    }

    private boolean checkLightItem(Player player) {
        return (player.getItemOnCursor().getType() == Material.FLINT_AND_STEEL);
    }

    private boolean checkCachedValues(String value) {
        return getMain().cachedBooleanValues.get(value);
    }
}
