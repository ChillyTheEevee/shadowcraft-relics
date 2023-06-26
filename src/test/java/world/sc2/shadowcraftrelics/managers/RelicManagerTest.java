package world.sc2.shadowcraftrelics.managers;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import world.sc2.shadowcraftrelics.ShadowcraftRelics;
import world.sc2.shadowcraftrelics.config.Config;
import world.sc2.shadowcraftrelics.config.ConfigManager;
import world.sc2.shadowcraftrelics.relics.on_attack.SimonObliterator;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RelicManagerTest {
    @Mock
    private SimonObliterator mockSimonObliteratorRelic;
    private RelicManager relicManager;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);


        // mockSimonObliteratorRelic
        when(mockSimonObliteratorRelic.getName()).thenReturn("simon_obliterator");

        /* Mock dependencies required for RelicManager instantiation */

        // mockPlugin
        ShadowcraftRelics mockPlugin = mock(ShadowcraftRelics.class);
        when(mockPlugin.getName()).thenReturn("ShadowcraftRelics");

        // mockSOYAMLConfig
        YamlConfiguration mockSOYAMLConfig = mock(YamlConfiguration.class);
        when(mockSOYAMLConfig.getBoolean("isEnabled")).thenReturn(true);

        // mockSOConfig
        Config mockSOConfig = mock(Config.class);
        when(mockSOConfig.get()).thenReturn(mockSOYAMLConfig);

        // mockConfigManager
        ConfigManager mockConfigManager = mock(ConfigManager.class);
        when(mockConfigManager.getConfig("relicProperties/simonObliterator.yml")).thenReturn(mockSOConfig);

        relicManager = new RelicManager(mockPlugin, mockConfigManager);
    }

    @DisplayName("getRelicFromName(): If user gives fake relic name return null")
    @Test
    void whenGetRelicFromName_ifNameIsNotARelic_thenReturnNull() {
        assertNull(relicManager.getRelicFromName("this is NOT a Relic's name"));
    }

    @DisplayName("isRelic(): When Item is null return false")
    @Test
    void whenIsRelic_ifItemIsNull_thenReturnFalse() {
        assertFalse(relicManager.isRelic(null, mockSimonObliteratorRelic));
    }

    @DisplayName("isRelic(): If the Item is the specified Relic return true")
    @Test
    void whenIsRelic_ifItemNBTDataMatchesRelicName_thenReturnTrue() {
        ItemStack mockRealSimonObliterator = mock(ItemStack.class);
        ItemMeta mockRealSimonObliteratorMeta = mock(ItemMeta.class);
        PersistentDataContainer mockPDC = mock(PersistentDataContainer.class);

        when(mockRealSimonObliterator.getItemMeta()).thenReturn(mockRealSimonObliteratorMeta);
        when(mockRealSimonObliteratorMeta.getPersistentDataContainer()).thenReturn(mockPDC);
        when(mockPDC.get(any(), eq(PersistentDataType.STRING))).thenReturn("simon_obliterator");
        when(mockPDC.has(any())).thenReturn(true);

        assertTrue(relicManager.isRelic(mockRealSimonObliterator, mockSimonObliteratorRelic));
    }

    @DisplayName("isRelic(): Relics that aren't the specified Relic return false")
    @Test
    void whenIsRelic_ifItemHasTagButIsNotSpecifiedRelic_thenReturnFalse() {
        ItemStack mockFakeSimonObliterator = mock(ItemStack.class);
        ItemMeta mockFakeSimonObliteratorMeta = mock(ItemMeta.class);
        PersistentDataContainer mockPDC = mock(PersistentDataContainer.class);

        when(mockFakeSimonObliterator.getItemMeta()).thenReturn(mockFakeSimonObliteratorMeta);
        when(mockFakeSimonObliteratorMeta.getPersistentDataContainer()).thenReturn(mockPDC);
        when(mockPDC.get(any(), eq(PersistentDataType.STRING))).thenReturn("not_a_real_relic");
        when(mockPDC.has(any())).thenReturn(true);

        assertFalse(relicManager.isRelic(mockFakeSimonObliterator, mockSimonObliteratorRelic));
    }

    @DisplayName("isRelic(): Items with no NBT tags aren't Relics")
    @Test
    void whenIsRelic_ifItemDoesNotHaveRelicNBTTag_thenReturnFalse() {
        ItemStack mockFakeSimonObliterator = mock(ItemStack.class);
        ItemMeta mockFakeSimonObliteratorMeta = mock(ItemMeta.class);
        PersistentDataContainer mockPDC = mock(PersistentDataContainer.class);

        when(mockFakeSimonObliterator.getItemMeta()).thenReturn(mockFakeSimonObliteratorMeta);
        when(mockFakeSimonObliteratorMeta.getPersistentDataContainer()).thenReturn(mockPDC);
        when(mockPDC.has(any())).thenReturn(false);

        assertFalse(relicManager.isRelic(mockFakeSimonObliterator, mockSimonObliteratorRelic));
    }

    @DisplayName("giveItemNBTTag(): If given real Relic and ItemStack item give item tag and return true")
    @Test
    void whenGiveItemNBTTag_ifRelicNameIsValid_thenGiveItemCorrectNBTTagAndReturnTrue() {

        ItemStack stoneAxe = mock(ItemStack.class);
        ItemMeta stoneAxeMeta = mock(ItemMeta.class);
        PersistentDataContainer stoneAxePDC = mock(PersistentDataContainer.class);

        when(stoneAxe.getItemMeta()).thenReturn(stoneAxeMeta);
        when(stoneAxeMeta.getPersistentDataContainer()).thenReturn(stoneAxePDC);

        assertTrue(relicManager.giveItemRelicNBTTag(stoneAxe, mockSimonObliteratorRelic));

        verify(stoneAxePDC).set(any(), eq(PersistentDataType.STRING), eq("simon_obliterator"));
        verify(stoneAxe).setItemMeta(stoneAxeMeta);
    }

    @DisplayName("giveItemNBTTag(): If given null Relic do nothing and return false")
    @Test
    void whenGiveItemNBTTag_ifRelicNameIsNull_thenDoNothingAndReturnFalse() {

        ItemStack stoneAxe = mock(ItemStack.class);
        ItemMeta stoneAxeMeta = mock(ItemMeta.class);
        PersistentDataContainer stoneAxePDC = mock(PersistentDataContainer.class);

        when(stoneAxe.getItemMeta()).thenReturn(stoneAxeMeta);
        when(stoneAxeMeta.getPersistentDataContainer()).thenReturn(stoneAxePDC);

        assertFalse(relicManager.giveItemRelicNBTTag(stoneAxe, null));

        verify(stoneAxePDC, never()).set(any(), eq(PersistentDataType.STRING), eq("simon_obliterator"));
        verify(stoneAxe, never()).setItemMeta(stoneAxeMeta);
    }

    @DisplayName("giveItemNBTTag(): If item is null then return false")
    @Test
    void whenGiveItemNBTTag_ifItemIsNull_thenReturnFalse() {
        assertFalse(relicManager.giveItemRelicNBTTag(null, mockSimonObliteratorRelic));
    }

}