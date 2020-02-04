package cz.ceph.lampcontrol.environment;

import lombok.Data;
import org.bukkit.Material;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ScreamingSandals team
 */
@Data
public abstract class Environment {
    private Plugin plugin;
    private List<Material> LIGHTABLE = new ArrayList<>();
    private List<Material> POWERABLE = new ArrayList<>();

    public Environment(Plugin plugin) {
        this.plugin = plugin;
    }

    public boolean isLightable(Material material) {
        for (Material mat : LIGHTABLE) {
            if (mat == material) {
                return true;
            }
        }
        return false;
    }

    public boolean isPowerable(Material material) {
        for (Material mat : POWERABLE) {
            if (mat == material) {
                return true;
            }
        }
        return false;
    }
}
