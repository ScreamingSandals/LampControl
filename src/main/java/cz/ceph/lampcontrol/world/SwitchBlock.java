package cz.ceph.lampcontrol.world;

import cz.ceph.lampcontrol.Main;
import cz.ceph.lampcontrol.utils.Utils;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.data.Lightable;
import org.bukkit.block.data.Powerable;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

@Data
@NoArgsConstructor
public class SwitchBlock implements cz.ceph.lampcontrol.api.SwitchBlock {
    private Object craftWorld = null;
    private Field clientSide = null;

    @Override
    public void setLit(Block block, boolean light) {
        World world = block.getWorld();
        Lightable lightable = (Lightable) block.getState().getBlockData();

        setStatic(world,true);
        lightable.setLit(light);
        block.setBlockData(lightable);
        setStatic(world,false);
    }

    @Override
    public void setPowered(Block block, boolean power) {
        World world = block.getWorld();
        Powerable powerable = (Powerable) block.getState().getBlockData();

        setStatic(world, true);
        powerable.setPowered(power);
        block.setBlockData(powerable);
        setStatic(world, false);
    }

    @Override
    public void setStatic(World world, boolean value) {
        try {
            craftWorld = getNMCWorld(getInstanceOfCW(getCraftWorld(world)));
            clientSide = craftWorld.getClass().getField("isClientSide");
        } catch (ClassNotFoundException | NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            try {
                clientSide = craftWorld.getClass().getField("isStatic");
            } catch (NoSuchFieldException e1) {
                e1.printStackTrace();
            }
        }

        clientSide.setAccessible(true);

        try {
            clientSide.set(craftWorld, value);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    private Object getNMCWorld(Object craftWorld) throws ClassNotFoundException {
        return Class.forName("net.minecraft.server." + Utils.getNMSVersion() + ".World", false, Main.class.getClassLoader()).cast(craftWorld);
    }

    private Object getCraftWorld(Object worldInstance) throws ClassNotFoundException {
        return Class.forName("org.bukkit.craftbukkit." + Utils.getNMSVersion() + ".CraftWorld", false, Main.class.getClassLoader()).cast(worldInstance);
    }

    private Object getInstanceOfCW(Object craftWorld) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        return craftWorld.getClass().getDeclaredMethod("getHandle").invoke(craftWorld);
    }
}
