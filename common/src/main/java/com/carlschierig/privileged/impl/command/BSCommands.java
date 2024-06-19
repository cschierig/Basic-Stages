package com.carlschierig.privileged.impl.command;

import com.carlschierig.privileged.api.stage.StageMap;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

import java.util.Collection;
import java.util.function.BiFunction;

public class BSCommands {
    private static final String PLAYERS = "players";
    private static final String PLAYER_SINGULAR = "player";
    private static final String STAGE = "stage";

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(
                Commands.literal("stage")
                        .requires(css -> css.hasPermission(2))
                        .then(
                                Commands.literal("grant")
                                        .then(Commands.argument(PLAYERS, EntityArgument.players())
                                                .then(Commands.argument(STAGE, StringArgumentType.word())
                                                        .executes(
                                                                context -> perform(
                                                                        EntityArgument.getPlayers(context, PLAYERS),
                                                                        StringArgumentType.getString(context, STAGE),
                                                                        (player, string) -> StageMap.getInstance().addStage(player, string)
                                                                )
                                                        )
                                                )
                                        )
                        ).then(
                                Commands.literal("revoke")
                                        .then(Commands.argument(PLAYERS, EntityArgument.players())
                                                .then(Commands.argument(STAGE, StringArgumentType.word())
                                                        .executes(
                                                                context -> perform(
                                                                        EntityArgument.getPlayers(context, PLAYERS),
                                                                        StringArgumentType.getString(context, STAGE),
                                                                        (player, string) -> StageMap.getInstance().removeStage(player, string)
                                                                )
                                                        )
                                                )
                                        )
                        ).then(
                                Commands.literal("list")
                                        .then(Commands.argument(PLAYER_SINGULAR, EntityArgument.player())
                                                .executes(
                                                        context -> {
                                                            var player = EntityArgument.getPlayer(context, PLAYER_SINGULAR);
                                                            var stages = StageMap.getInstance().getStages(player);

                                                            var source = context.getSource();
                                                            source.sendSuccess(() -> {
                                                                // TODO: Translation
                                                                var component = Component.literal("Player ").append(player.getName()).append(" has the following stages:\n");

                                                                for (var stage : stages) {
                                                                    component.append(stage).append("\n");
                                                                }
                                                                return component;
                                                            }, true);
                                                            return 1;
                                                        }
                                                )
                                        )
                        )
        );
    }

    private static int perform(Collection<ServerPlayer> players, String stage, BiFunction<ServerPlayer, String, Boolean> action) {
        int result = 0;

        for (var player : players) {
            if (action.apply(player, stage)) {
                result++;
            }
        }

        return result;
    }
}
