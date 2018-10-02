package cz.ceph.lampcontrol.workers;

import cz.ceph.lampcontrol.LampControl;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Lightable;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

/**
 * Created by Ceph on 17.07.2016.
 */

public class SwitchBlock {

    private Object craftWorld;
    private Field isClientSideField;


    public void initWorld(World world) {
        try {
            craftWorld = getNMCWorld(getInstanceOfCW(getCraftWorld(world)));
            isClientSideField = craftWorld.getClass().getField("isClientSide");
        } catch (ClassNotFoundException | NoSuchMethodException | InvocationTargetException | IllegalAccessException | InstantiationException e) {
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

    public void setStatic(boolean value) {
        try {
            isClientSideField.set(craftWorld, value);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public void switchLamp(Block block, boolean light) {
        if (light) {
            setStatic(true);
            SwitchMaterial.vLampSwitcher(true, block);
            setStatic(false);
        } else {
            SwitchMaterial.vLampSwitcher(false, block);
        }
    }

    private Object getNMCWorld(Object cW) throws ClassNotFoundException {
        return Class.forName("net.minecraft.server." + LampControl.bukkitVersion + ".World", false, LampControl.class.getClassLoader()).cast(cW);
    }

    private Object getCraftWorld(Object worldInstance) throws ClassNotFoundException, InstantiationException, IllegalAccessException {
        return Class.forName("org.bukkit.craftbukkit." + LampControl.bukkitVersion + ".CraftWorld", false, LampControl.class.getClassLoader()).cast(worldInstance);
    }

    private Object getInstanceOfCW(Object cW) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        return cW.getClass().getDeclaredMethod("getHandle").invoke(cW);
    }

    /*
    // NEEDS TO BE DONE
    @SuppressWarnings("deprecation")
    public void switchRail(Block block, boolean power) {
        try {
            int data = (int) block.getData();
            if (power) {
                data = data + 8;
                setStatic(true);
                block.setTypeIdAndData(27, (byte) data, false);
                setStatic(false);
            } else {
                data = data - 8;
                block.setTypeIdAndData(27, (byte) data, false);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    */

}