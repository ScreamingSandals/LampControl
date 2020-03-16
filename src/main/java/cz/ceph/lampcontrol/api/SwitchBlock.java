package cz.ceph.lampcontrol.api;

import org.bukkit.block.Block;

/**
 * @author ScreamingSandals team
 */
public interface SwitchBlock {
    /**
     * Sets the provided block's lit state
     * @param block
     * @param lit
     */
    void setLit(Block block, boolean lit);

    /**
     * Sets the provided block's power state
     * @param block
     * @param powered
     */
    void setPowered(Block block, boolean powered);
}
