package com.mineblock11.spoofer.client;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mineblock11.spoofer.SpooferManager;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Pair;

import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.argument;
import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.literal;

@Environment(EnvType.CLIENT)
public class SpooferClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ClientCommandRegistrationCallback.EVENT.register(((dispatcher, registryAccess) -> {
            dispatcher.register(literal("spoof")
                    .then(argument("target", StringArgumentType.string()).then(argument("username", StringArgumentType.string()).executes(ctx -> {
                        var target = StringArgumentType.getString(ctx, "target");
                        var username = StringArgumentType.getString(ctx, "username");
                        SpooferManager.currentlySpoofed.put(target, new Pair<>(username, Text.of(username)));
                        ctx.getSource().sendFeedback(Text.literal("Spoofed ").append(Text.literal(target).formatted(Formatting.GRAY).append(" as ").append(Text.literal(username).formatted(Formatting.GRAY))));
                        return Command.SINGLE_SUCCESS;
                    }).then(argument("nametag", StringArgumentType.string()).executes(ctx -> {
                        var target = StringArgumentType.getString(ctx, "target");
                        var username = StringArgumentType.getString(ctx, "username");
                        var nametag = StringArgumentType.getString(ctx, "nametag").replace("&", "§");
                        SpooferManager.currentlySpoofed.put(target, new Pair<>(username, Text.of(nametag)));
                        ctx.getSource().sendFeedback(Text.literal("Spoofed ").append(Text.literal(target).formatted(Formatting.GRAY)).append(" as ").append(Text.literal(username).formatted(Formatting.GRAY)));
                        return Command.SINGLE_SUCCESS;
                    }))))


            );

            dispatcher.register(literal("unspoof")
                    .then(argument("target", StringArgumentType.string()).executes(ctx -> {
                        var target = StringArgumentType.getString(ctx, "target");
                        SpooferManager.currentlySpoofed.remove(target);
                        ctx.getSource().sendFeedback(Text.literal("Unspoofed ").append(Text.literal(target).formatted(Formatting.GRAY)));
                        return Command.SINGLE_SUCCESS;
                    }))
            );

            dispatcher.register(literal("togglechatspoof").executes(ctx -> {
                SpooferManager.ENABLE_CHAT_SPOOF = !SpooferManager.ENABLE_CHAT_SPOOF;
                ctx.getSource().sendFeedback(Text.literal("Chat Spoof is now ").append(Text.literal(SpooferManager.ENABLE_CHAT_SPOOF ? "enabled" : "disabled").formatted(SpooferManager.ENABLE_CHAT_SPOOF ? Formatting.GREEN : Formatting.RED).append(".")));
                return Command.SINGLE_SUCCESS;
            }));
        }));
    }
}
