package mine.block.spoofer.client;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.StringArgumentType;
import mine.block.spoofer.SpooferManager;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.minecraft.text.Text;
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
                        return Command.SINGLE_SUCCESS;
                    }).then(argument("nametag", StringArgumentType.string()).executes(ctx -> {
                        var target = StringArgumentType.getString(ctx, "target");
                        var username = StringArgumentType.getString(ctx, "username");
                        var nametag = StringArgumentType.getString(ctx, "nametag").replace("&", "§");
                        SpooferManager.currentlySpoofed.put(target, new Pair<>(username, Text.of(nametag)));
                        return Command.SINGLE_SUCCESS;
                    }))))


            );

            dispatcher.register(literal("unspoof")
                    .then(argument("target", StringArgumentType.string()).executes(ctx -> {
                        var target = StringArgumentType.getString(ctx, "target");
                        SpooferManager.currentlySpoofed.remove(target);
                        return Command.SINGLE_SUCCESS;
                    }))
            );
        }));
    }
}
