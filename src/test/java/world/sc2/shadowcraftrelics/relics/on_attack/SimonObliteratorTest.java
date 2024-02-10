package world.sc2.shadowcraftrelics.relics.on_attack;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import world.sc2.config.Config;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.*;

class SimonObliteratorTest {

    // Test variables
    private final double damageDealtOnEvent = 5.0f;
    private final double simonDamageMultiplier = 3.0;
    private final double annoyingDamageMultiplier = 1.5;
    private final String simonUUID = "this-is-the-uuid-of-simon";
    private List<String> annoyingPlayerUUIDs;
    private final String normalEntityUUID = "this-is-the-uuid-of-anything-other-than-simon-or-an-annoying-person";

    @Mock
    Entity mockNormalEntity;
    @Mock
    Player mockSimon;
    @Mock
    Player mockAnnoyingPlayer;
    private SimonObliterator simonObliterator;

    @BeforeEach
    public void setup() {
        annoyingPlayerUUIDs = new ArrayList<>();
        annoyingPlayerUUIDs.add("this-is-the-uuid-of-an-annoying-person");

        MockitoAnnotations.openMocks(this);

        // Mock normal UUID
        UUID mockNormalEntityUUID = mock(UUID.class);
        when(mockNormalEntityUUID.toString()).thenReturn(normalEntityUUID);

        // Mock normal Entity
        when(mockNormalEntity.getUniqueId()).thenReturn(mockNormalEntityUUID);

        // Mock Simon UUID
        UUID mockSimonUUID = mock(UUID.class);
        when(mockSimonUUID.toString()).thenReturn(simonUUID);

        // Mock Simon Player
        when(mockSimon.getUniqueId()).thenReturn(mockSimonUUID);

        // Mock annoying player UUID
        UUID mockAnnoyingUUID = mock(UUID.class);
        when(mockAnnoyingUUID.toString()).thenReturn(annoyingPlayerUUIDs.get(0));

        // Mock annoying player
        when(mockAnnoyingPlayer.getUniqueId()).thenReturn(mockAnnoyingUUID);

        /* Instantiate required mock dependencies */

        YamlConfiguration mockYAMLConfig = mock(YamlConfiguration.class);

        when(mockYAMLConfig.getBoolean("isEnabled")).thenReturn(true);
        when(mockYAMLConfig.getString("displayName")).thenReturn("Simon Obliterator");
        when(mockYAMLConfig.getDouble("uniqueProperties.simonDamageMultiplier"))
                .thenReturn(simonDamageMultiplier);
        when(mockYAMLConfig.getDouble("uniqueProperties.annoyingPersonDamageMultiplier"))
                .thenReturn(annoyingDamageMultiplier);
        when(mockYAMLConfig.getString("uniqueProperties.simonUUID")).thenReturn(simonUUID);
        when(mockYAMLConfig.getStringList("uniqueProperties.annoyingPeopleUUIDs")).thenReturn(annoyingPlayerUUIDs);

        Config mockConfig = mock(Config.class);

        when(mockConfig.get()).thenReturn(mockYAMLConfig);

        // Instantiate object to test
        simonObliterator = new SimonObliterator("simon_obliterator", mockConfig);
    }

    @DisplayName("onAttack(): if Simon is attacked then multiply damage by simonDamageMultiplier")
    @Test
    void whenOnAttack_ifEntityHitIsSimon_thenMultiplyDamageBySimonMultiplier() {
        EntityDamageByEntityEvent simonHitEvent = mock(EntityDamageByEntityEvent.class);
        when(simonHitEvent.getEntity()).thenReturn(mockSimon);
        when(simonHitEvent.getDamage()).thenReturn(damageDealtOnEvent);

        simonObliterator.onAttack(simonHitEvent);

        verify(simonHitEvent).getDamage();
        verify(simonHitEvent, times(1)).setDamage(damageDealtOnEvent * simonDamageMultiplier);
    }

    @DisplayName("onAttack(): if annoying person is attacked then multiply damage by annoyingDamageMultiplier")
    @Test
    void whenOnAttack_ifEntityHitIsAnnoying_thenMultiplyDamageByAnnoyingMultiplier() {
        EntityDamageByEntityEvent annoyingPlayerHitEvent = mock(EntityDamageByEntityEvent.class);
        when(annoyingPlayerHitEvent.getEntity()).thenReturn(mockAnnoyingPlayer);
        when(annoyingPlayerHitEvent.getDamage()).thenReturn(damageDealtOnEvent);

        simonObliterator.onAttack(annoyingPlayerHitEvent);

        verify(annoyingPlayerHitEvent).getDamage();
        verify(annoyingPlayerHitEvent, times(1)).setDamage(damageDealtOnEvent * annoyingDamageMultiplier);
    }

    @DisplayName("onAttack(): if normal Entity is attacked then do nothing")
    @Test
    void whenOnAttack_ifNormalEntityIsHit_thenDoNothing() {
        EntityDamageByEntityEvent simonHitEvent = mock(EntityDamageByEntityEvent.class);
        when(simonHitEvent.getEntity()).thenReturn(mockNormalEntity);
        when(simonHitEvent.getDamage()).thenReturn(damageDealtOnEvent);

        simonObliterator.onAttack(simonHitEvent);

        verify(simonHitEvent).getEntity();
        verifyNoMoreInteractions(simonHitEvent);
    }

}