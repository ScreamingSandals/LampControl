package org.screamingsandals.lampcontrol.environment;

import org.bukkit.Material;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ScreamingSandals team
 */
public class LegacyEnvironment extends Environment {

    public LegacyEnvironment(Plugin plugin) {
        super(plugin);

        loadLightable();
        loadPowerable();
    }

    @SuppressWarnings("deprecation")
    private void loadLightable() {
        List<Material> lightable = new ArrayList<>();
        lightable.add(Material.LEGACY_REDSTONE_TORCH_OFF);
        lightable.add(Material.LEGACY_REDSTONE_TORCH_ON);
        lightable.add(Material.LEGACY_REDSTONE_LAMP_OFF);
        lightable.add(Material.LEGACY_REDSTONE_LAMP_ON);
        lightable.add(Material.LEGACY_FURNACE);
        lightable.add(Material.LEGACY_BURNING_FURNACE);

        setLIGHTABLE(lightable);
    }

    @SuppressWarnings("deprecation")
    private void loadPowerable() {
        List<Material> powerable = new ArrayList<>();
        powerable.add(Material.LEGACY_POWERED_RAIL);

        setPOWERABLE(powerable);
    }
}
