package cz.ceph.lampcontrol.world;

import cz.ceph.lampcontrol.LampControl;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.data.Lightable;
import org.bukkit.block.data.Powerable;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

public class SwitchBlock {
    private Object craftWorld;
    private Field isClientSideField;

    public void initWorld(World world) {
        try {
            craftWorld = getNMCWorld(getInstanceOfCW(getCraftWorld(world)));
            isClientSideField = craftWorld.getClass().getField("isClientSide");
        } catch (ClassNotFoundException | NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            try {
                isClientSideField = craftWorld.getClass().getField("isStatic");
            } catch (NoSuchFieldException e1) {
                e1.printStackTrace();
            }
        }

        isClientSideField.setAccessible(true);
    }

    public void switchLamp(Block block, boolean light) {
        Lightable lightable = (Lightable) block.getState().getBlockData();

        setStatic(true);
        lightable.setLit(light);
        block.setBlockData(lightable);
        setStatic(false);
    }

    public void switchRail(Block block, boolean power) {
        Powerable powerable = (Powerable) block.getState().getBlockData();

        setStatic(true);
        powerable.setPowered(power);
        block.setBlockData(powerable);
        setStatic(false);
    }

    private void setStatic(boolean value) {
        try {
            isClientSideField.set(craftWorld, value);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    private Object getNMCWorld(Object cW) throws ClassNotFoundException {
        return Class.forName("net.minecraft.server." + LampControl.getInstance() + ".World", false, LampControl.class.getClassLoader()).cast(cW);
    }

    private Object getCraftWorld(Object worldInstance) throws ClassNotFoundException {
        return Class.forName("org.bukkit.craftbukkit." + LampControl.getInstance() + ".CraftWorld", false, LampControl.class.getClassLoader()).cast(worldInstance);
    }

    private Object getInstanceOfCW(Object cW) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        return cW.getClass().getDeclaredMethod("getHandle").invoke(cW);
    }
}
