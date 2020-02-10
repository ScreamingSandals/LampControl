package cz.ceph.lampcontrol.environment;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.bukkit.Material;
import org.bukkit.block.data.Lightable;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ScreamingSandals team
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class MainEnvironment extends Environment {

    public MainEnvironment(Plugin plugin) {
        super(plugin);

        loadLightable();
        loadPowerable();
    }

    private void loadLightable() {
        List<Material> lightable = new ArrayList<>();
        lightable.add(Material.REDSTONE_LAMP);
        lightable.add(Material.REDSTONE_TORCH);
        lightable.add(Material.REDSTONE_WALL_TORCH);
        lightable.add(Material.BLAST_FURNACE);
        lightable.add(Material.FURNACE);
        lightable.add(Material.CAMPFIRE);

        setLIGHTABLE(lightable);
    }

    private void loadPowerable() {
        List<Material> powerable = new ArrayList<>();
        powerable.add(Material.POWERED_RAIL);

        setPOWERABLE(powerable);
    }

}
