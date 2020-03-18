package org.screamingsandals.lampcontrol.player;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

/**
 * @author ScreamingSandals team
 */
@Data
@AllArgsConstructor
public class PlayerData {
    private UUID uuid;
    private boolean useRightClick;
    private boolean useShiftClick;
}
