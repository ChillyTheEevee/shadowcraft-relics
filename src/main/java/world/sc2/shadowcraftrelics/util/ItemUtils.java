package world.sc2.shadowcraftrelics.util;

import com.google.gson.Gson;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class ItemUtils {

    public static boolean isAirOrNull(ItemStack item) {
        return item == null || item.getType() == Material.AIR;
    }

    // Serialization and Deserialization
    public static byte[] serializeItemStack(ItemStack itemStack) {
        return itemStack.serializeAsBytes();
    }

    public static ItemStack deserializeItemStack(byte[] serializedItemStack) {
        return ItemStack.deserializeBytes(serializedItemStack);
    }

}
