package world.sc2.shadowcraftrelics.commands;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import world.sc2.config.Config;
import world.sc2.shadowcraftrelics.managers.RelicManager;

import static org.mockito.Mockito.*;


class GiveRelicTagCommandTest {

    @Mock
    Player mockPlayerCommandSender;

    private GiveRelicTagCommand giveRelicTagCommand;

    @BeforeEach
    public void setup() {

        MockitoAnnotations.openMocks(this);

        // setup mockConsoleCommandSender

        // setup mockPlayerCommandSender
        ItemStack mockHeldItem = Mockito.mock(ItemStack.class);
        PlayerInventory mockPlayerInventory = Mockito.mock(PlayerInventory.class);
        when(mockPlayerInventory.getItemInMainHand()).thenReturn(mockHeldItem);

        when(mockPlayerCommandSender.getInventory()).thenReturn(mockPlayerInventory);

        // Setup RelicManager
        RelicManager mockRelicManager = Mockito.mock(RelicManager.class);
        Config mockConfig = Mockito.mock(Config.class);

        giveRelicTagCommand = new GiveRelicTagCommand(mockConfig, mockRelicManager);
    }

    @DisplayName("onCommand(): If a Player CommandSender gives a wrong relic tag, do not change their held item")
    @Test
    void whenOnCommand_ifInvalidRelicTag_thenDoNotChangeItemStack() {
        ItemStack mockHeldItem = mockPlayerCommandSender.getInventory().getItemInMainHand();

        String[] args = {"purger"};

        giveRelicTagCommand.onCommand(mockPlayerCommandSender, args);

        verify(mockHeldItem, never()).setItemMeta(null);
    }

}