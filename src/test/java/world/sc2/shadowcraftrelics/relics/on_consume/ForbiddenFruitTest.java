package world.sc2.shadowcraftrelics.relics.on_consume;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import live.chillytheeevee.chillylib.config.Config;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ForbiddenFruitTest {

    @Mock
    private Player mockPlayer;

    private ForbiddenFruit forbiddenFruit;

    private final String banMessage = "Ban message";

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

        // setup mockYAMLForbiddenFruitConfig
        YamlConfiguration mockYAMLForbiddenFruitConfig = Mockito.mock(YamlConfiguration.class);
        when(mockYAMLForbiddenFruitConfig.getString("uniqueProperties.banMessage")).thenReturn(banMessage);

        // setup mockForbiddenFruitConfig
        Config mockForbiddenFruitConfig = Mockito.mock(Config.class);
        when(mockForbiddenFruitConfig.get()).thenReturn(mockYAMLForbiddenFruitConfig);

        forbiddenFruit = new ForbiddenFruit("forbidden_fruit", mockForbiddenFruitConfig);
    }

    @DisplayName("onConsume(): If the Forbidden Fruit is consumed, the consumer is banned with the message defined" +
            "in the Config")
    @Test
    void whenOnConsume_thenBanPlayerWithMessageInConfig() {
        PlayerItemConsumeEvent mockPlayerItemConsumeEvent = Mockito.mock(PlayerItemConsumeEvent.class);
        when(mockPlayerItemConsumeEvent.getPlayer()).thenReturn(mockPlayer);

        forbiddenFruit.onConsume(mockPlayerItemConsumeEvent);

        verify(mockPlayer).banPlayer(banMessage);
    }
}