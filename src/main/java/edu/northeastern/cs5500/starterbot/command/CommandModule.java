package edu.northeastern.cs5500.starterbot.command;

import dagger.Module;
import dagger.Provides;
import dagger.multibindings.IntoMap;
import dagger.multibindings.StringKey;

@Module
public class CommandModule {

    @Provides
    @IntoMap
    @StringKey(PreferredNameCommand.NAME)
    public SlashCommandHandler providePreferredNameCommand(
            PreferredNameCommand preferredNameCommand) {
        return preferredNameCommand;
    }

    @Provides
    @IntoMap
    @StringKey(MoveCommand.NAME)
    public StringSelectHandler provideMoveCommandMenuHandler(MoveCommand command) {
        return command;
    }

    @Provides
    @IntoMap
    @StringKey(CatchCommand.NAME)
    public ButtonHandler provideCatchCommandMenuHandler(CatchCommand command) {
        return command;
    }

    @Provides
    @IntoMap
    @StringKey(TestBattleView.NAME)
    public ButtonHandler providetestBattleViewClickHandler(TestBattleView command) {
        return command;
    }

    @Provides
    @IntoMap
    @StringKey(TestBattleView.NAME)
    public StringSelectHandler provideTestBattleViewMenuHandler(TestBattleView command) {
        return command;
    }

    @Provides
    @IntoMap
    @StringKey(SwitchPokemonCommand.NAME)
    public StringSelectHandler provideSwitchPokemonCommandMenuHandler(
            SwitchPokemonCommand command) {
        return command;
    }

    @Provides
    @IntoMap
    @StringKey(CheckPokeCommand.NAME)
    public ButtonHandler provideCheckPokeCommandClickHandler(CheckPokeCommand command) {
        return command;
    }

    @Provides
    @IntoMap
    @StringKey(TestBattle.NAME)
    public SlashCommandHandler provideTestBattle(TestBattle command) {
        return command;
    }

    @Provides
    @IntoMap
    @StringKey(MainViewCommand.NAME)
    public SlashCommandHandler provideMainViewCommand(MainViewCommand command) {
        return command;
    }

    @Provides
    @IntoMap
    @StringKey(MainViewCommand.NAME)
    public StringSelectHandler provideMainViewCommandMenuHandler(MainViewCommand command) {
        return command;
    }

    @Provides
    @IntoMap
    @StringKey(MainViewCommand.NAME)
    public ButtonHandler provideMainViewCommandClickHandler(MainViewCommand command) {
        return command;
    }

    @Provides
    @IntoMap
    @StringKey(ShopCommand.NAME)
    public StringSelectHandler provideShopCommandMenuHandler(ShopCommand command) {
        return command;
    }

    @Provides
    @IntoMap
    @StringKey(ItemCommand.NAME)
    public SlashCommandHandler provideItemCommand(ItemCommand command) {
        return command;
    }

    @Provides
    @IntoMap
    @StringKey(HealCommand.NAME)
    public StringSelectHandler provideHealCommandMenuHandler(HealCommand command) {
        return command;
    }
}
