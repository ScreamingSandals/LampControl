package cz.ceph.lampcontrol.environment;

import lombok.Data;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ScreamingSandals team
 */
@Data
public abstract class Environment {
    @Getter
    private static Environment instance;
    private final Plugin plugin;
    private List<Material> LIGHTABLE = new ArrayList<>();
    private List<Material> POWERABLE = new ArrayList<>();

    public Environment(Plugin plugin) {
        this.plugin = plugin;
        instance = this;
    }
}
