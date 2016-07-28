/*
	Code has been adapted from richie3366's LumosMaxima.
	Code is modified by Granik24.
	GNU General Public License version 3 (GPLv3)
*/
package cz.granik24.LampControl.listeners;


import cz.granik24.LampControl.Main;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPhysicsEvent;


public class LampListener implements Listener {
    private Main plugin;

    public LampListener(Main p) {
        this.plugin = p;
    }

    @EventHandler
    public void onBlockPhysics(BlockPhysicsEvent e) {
        boolean lamp = e.getBlock().getType().equals(Material.REDSTONE_LAMP_ON);

        if (!Main.woodPlateControl || !Main.stonePlateControl && lamp && !this.plugin.isInRedstoneMaterials(e.getChangedType())) {
                e.setCancelled(true);
        }
    }
}