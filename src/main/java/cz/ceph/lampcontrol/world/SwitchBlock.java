package cz.ceph.lampcontrol.world;

import cz.ceph.lampcontrol.Main;
import cz.ceph.lampcontrol.utils.Utils;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.data.Lightable;
import org.bukkit.block.data.Powerable;
import org.bukkit.material.PoweredRail;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

@Data
@NoArgsConstructor
public class SwitchBlock implements cz.ceph.lampcontrol.api.SwitchBlock {
    @Override
    public void setLit(Block block, boolean light) {
        World world = block.getWorld();
        if (Main.isLegacy()) {
            Material material = block.getType();
            Handler handler = new Handler();
            handler.init(world);

            handler.setStatic(true);
            block.setType(getLegacyMaterial(material));
            handler.setStatic(false);
            return;
        }

        Lightable lightable = (Lightable) block.getState().getBlockData();
        Handler handler = new Handler();
        handler.init(world);

        handler.setStatic(true);
        lightable.setLit(light);
        block.setBlockData(lightable);
        handler.setStatic(false);
    }

    @Override
    @SuppressWarnings("deprecation")
    public void setPowered(Block block, boolean power) {
        World world = block.getWorld();
        Material material = block.getType();
        if (Main.isLegacy() && material == Material.LEGACY_POWERED_RAIL) {
            Handler handler = new Handler();
            handler.init(world);

            handler.setStatic(true);
            PoweredRail poweredRail = new PoweredRail(material);
            poweredRail.setPowered(power);
            handler.setStatic(false);
            return;
        }

        Powerable powerable = (Powerable) block.getState().getBlockData();
        Handler handler = new Handler();
        handler.init(world);

        handler.setStatic(true);
        powerable.setPowered(power);
        block.setBlockData(powerable);
        handler.setStatic(false);
    }

    @Data
    public static class Handler {
        private Object craftWorld;
        private Field clientSide;

        public void init(World world) {
            try {
                craftWorld = getMinecraftWorld(world);
                clientSide = craftWorld.getClass().getField("isClientSide");
            } catch (ClassNotFoundException | NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
                e.printStackTrace();
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
                try {
                    clientSide = craftWorld.getClass().getField("isStatic");
                } catch (NoSuchFieldException e1) {
                    e1.printStackTrace();
                }
            }

            clientSide.setAccessible(true);
        }

        public void setStatic(boolean value) {
            try {
                clientSide.set(craftWorld, value);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }

        }

        private Object getMinecraftWorld(Object world) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
            String nmsVersion = Utils.getNMSVersion();
            Object craftWorld = Class.forName("org.bukkit.craftbukkit." + nmsVersion + ".CraftWorld", false, Main.class.getClassLoader()).cast(world);
            Object worldServerInstance = craftWorld.getClass().getDeclaredMethod("getHandle").invoke(craftWorld);

            return Class.forName("net.minecraft.server." + nmsVersion + ".World", false, Main.class.getClassLoader()).cast(worldServerInstance);
        }
    }

    private Material getLegacyMaterial(Material material) {
        String materialName = material.toString();
        for (Material selected : Main.getEnvironment().getLIGHTABLE()) {
            String selectedName = selected.toString();
            if (materialName.equalsIgnoreCase("LEGACY_REDSTONE_TORCH_OFF")
                    && selectedName.equalsIgnoreCase(materialName)) {
                return Material.getMaterial("LEGACY_REDSTONE_TORCH_ON");
            }
            if (materialName.equalsIgnoreCase("LEGACY_REDSTONE_TORCH_ON")
                    && selectedName.equalsIgnoreCase(materialName)) {
                return Material.getMaterial("LEGACY_REDSTONE_TORCH_OFF");
            }
            if (materialName.equalsIgnoreCase("LEGACY_REDSTONE_LAMP_OFF")
                    && selectedName.equalsIgnoreCase(materialName)) {
                return Material.getMaterial("LEGACY_REDSTONE_LAMP_ON");
            }
            if (materialName.equalsIgnoreCase("LEGACY_REDSTONE_LAMP_ON")
                    && selectedName.equalsIgnoreCase(materialName)) {
                return Material.getMaterial("LEGACY_REDSTONE_LAMP_OFF");
            }
            if (materialName.equalsIgnoreCase("LEGACY_FURNACE")
                    && selectedName.equalsIgnoreCase(materialName)) {
                return Material.getMaterial("LEGACY_BURNING_FURNACE");
            }
            if (materialName.equalsIgnoreCase("LEGACY_BURNING_FURNACE")
                    && selectedName.equalsIgnoreCase(materialName)) {
                return Material.getMaterial("LEGACY_FURNACE");
            }
        }
        return Material.AIR;
    }
}
