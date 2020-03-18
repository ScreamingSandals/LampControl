package org.screamingsandals.lampcontrol.player;

import com.google.gson.reflect.TypeToken;
import lombok.Data;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.screamingsandals.lampcontrol.utils.JsonUtils;
import org.screamingsandals.lib.debug.Debug;

import java.io.*;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author ScreamingSandals team
 */
@Data
public class DataManager {
    private final Plugin plugin;
    private File dataFile;
    private Map<UUID, PlayerData> playersData = new HashMap<>();

    public DataManager(Plugin plugin) {
        this.plugin = plugin;
        this.dataFile = new File(plugin.getDataFolder(), "userdata.json");
        load();
    }

    public PlayerData getPlayerData(Player player) {
        return playersData.get(player.getUniqueId());
    }

    public void createPlayerData(Player player) {
        PlayerData created = getPlayerData(player);

        if (created == null) {
            final UUID uuid = player.getUniqueId();
            final PlayerData playerData = new PlayerData(uuid, true, false);

            playersData.put(uuid, playerData);
        }
    }

    public void load() {
        if (dataFile.exists()) {
            try (Reader reader = new FileReader(dataFile)) {
                Type type = new TypeToken<HashMap<UUID, PlayerData>>() {
                }.getType();
                playersData = JsonUtils.deserialize(reader, type);
            } catch (IOException e) {
                playersData = new HashMap<>();
                Debug.warn("Some error occurred with loading players data!");
                e.printStackTrace();
            }
        } else {
            try {
                dataFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void save() {
        try (FileWriter writer = new FileWriter(dataFile)) {
            JsonUtils.serialize(playersData, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
