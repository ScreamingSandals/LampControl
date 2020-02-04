package cz.ceph.lampcontrol.api;

import org.bukkit.World;
import org.bukkit.block.Block;

/**
 * @author ScreamingSandals team
 */
public interface SwitchBlock {
    /**
     *
     * @param block
     * @param lit
     */
    void setLit(Block block, boolean lit);

    /**
     *
     * @param block
     * @param powered
     */
    void setPowered(Block block, boolean powered);

    /**
     *
     * @param value static or not
     */
    void setStatic(World world, boolean value);
}
