package cz.ceph.LampControl.events;

import cz.ceph.LampControl.Main;
import cz.ceph.LampControl.utils.MessagesManager;
import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by Ceph on 17.07.2016.
 */

public class ReflectEvent {

    private Main javaPlugin;
    private Listener listener;

    public ReflectEvent(Main javaPlugin) {
        this.javaPlugin = javaPlugin;
    }

    public void initListener(Listener listener) {
        this.listener = listener;
    }

    public interface Callback<E extends Event> {
        void execute(E instance);
    }

    public void registerPlayerInteractEvent(Callback function) {
        boolean isTwoHandVersion = false;
        Class<? extends Event> playerInteractEvent = null;
        try {
            playerInteractEvent = (Class<? extends Event>) Class.forName("org.bukkit.event.player.PlayerInteractEvent", false, javaPlugin.getClass().getClassLoader());
            isTwoHandVersion = playerInteractEvent.getDeclaredMethod("getHand") != null;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            isTwoHandVersion = false;
        }

        if (playerInteractEvent == null)
            try {
                throw new Exception(MessagesManager.PREFIX + "PlayerInteractEvent not found. Try contacting developer with log.");
            } catch (Exception e) {
                e.printStackTrace();
                return;
            }

        boolean finalIsTwoHandVersion = isTwoHandVersion;
        Bukkit.getPluginManager().registerEvent(playerInteractEvent, listener, EventPriority.NORMAL, (listener, event) -> {
            if (finalIsTwoHandVersion) {
                try {
                    Method m = event.getClass().getDeclaredMethod("getHand");
                    Object instance = m.invoke(event);

                    if (instance instanceof Integer) {
                        int equipmentSlot = (int) instance;
                        if (equipmentSlot != 1)
                            function.execute(event);
                    } else {
                        if(instance == null) {
                            return;
                        }
                        if (!instance.toString().equalsIgnoreCase("OFF_HAND")) {
                            function.execute(event);
                        }
                    }
                } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
                    e.printStackTrace();
                }
            } else {
                function.execute(event);
            }
        }, javaPlugin);
    }

}
