package world.sc2.shadowcraftrelics.relics.on_consume;

import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import live.chillytheeevee.chillylib.config.Config;
import world.sc2.shadowcraftrelics.relics.Relic;

/**
 * A food {@link Relic} that has the special property of banning the {@link Player} that consumes it.
 */
public class ForbiddenFruit extends Relic implements TriggerOnConsumeRelic {

    private final static String BAN_MESSAGE_KEY = "uniqueProperties.banMessage";

    public ForbiddenFruit(String name, Config config) {
        super(name, config);
    }

    @Override
    public void onConsume(PlayerItemConsumeEvent event) {
        Player dirtySinner = event.getPlayer();

        dirtySinner.banPlayer(config.get().getString(BAN_MESSAGE_KEY));
    }
}
