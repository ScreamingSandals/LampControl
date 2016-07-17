package com.jacklinkproductions.LampControl;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;

/**
 * Created by Granik24 on 17.07.2016.
 */
public class SwitchBlock {


    private static Object getNMCWorld(Object cW) throws ClassNotFoundException {
        return Class.forName("net.minecraft.server." + Main.getNMSVersion() + ".World", false, Main.class.getClassLoader()).cast(cW);
    }

    private static Object getCraftWorld(Object worldInstance) throws ClassNotFoundException, InstantiationException, IllegalAccessException {
        return Class.forName("org.bukkit.craftbukkit." + Main.getNMSVersion() + ".CraftWorld", false, Main.class.getClassLoader()).cast(worldInstance);
    }

    private static Object getInstanceOfCW(Object cW) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        return cW.getClass().getDeclaredMethod("getHandle").invoke(cW);
    }

    public static void switchLamp(Block b, boolean light) {
        try {
            Object cW = getNMCWorld(getInstanceOfCW(getCraftWorld(b.getWorld())));
            if (light) {
                setWorldStatic(cW, true);
                b.setType(Material.REDSTONE_LAMP_ON);
                setWorldStatic(cW, false);
            } else {
                b.setType(Material.REDSTONE_LAMP_OFF);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void setWorldStatic(Object cW, boolean static_boolean) throws IllegalAccessException {
        Field field = null;
        try {
            field = cW.getClass().getField("isClientSide");
        } catch (NoSuchFieldException e) {
            try {
                field = cW.getClass().getField("isStatic");
            } catch (NoSuchFieldException e1) {
                e1.printStackTrace();
            }
        }

        field.setAccessible(true);
        field.set(cW, static_boolean);
    }
}