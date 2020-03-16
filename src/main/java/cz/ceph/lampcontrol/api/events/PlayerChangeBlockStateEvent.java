package cz.ceph.lampcontrol.api.events;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

/**
 * @author ScreamingSandals team
 */
@EqualsAndHashCode(callSuper = false)
@Data
public class PlayerChangeBlockStateEvent extends Event implements Cancellable {
    private HandlerList handlerList = new HandlerList();
    private final Player player;
    private final Block block;
    private boolean cancelled;

    @Override
    public @NotNull HandlerList getHandlers() {
        return handlerList;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }
}
