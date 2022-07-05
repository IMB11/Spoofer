package mine.block.spoofer.mixin;

import com.google.common.base.MoreObjects;
import com.mojang.authlib.Agent;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.ProfileLookupCallback;
import com.mojang.authlib.minecraft.MinecraftProfileTexture;
import com.mojang.authlib.yggdrasil.YggdrasilGameProfileRepository;
import mine.block.spoofer.SkinManager;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.util.DefaultSkinHelper;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;
import org.checkerframework.checker.units.qual.A;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.HashMap;

import static mine.block.spoofer.SpooferManager.*;

@Mixin(PlayerEntityRenderer.class)
public class PlayerEntityRendererMixin {

    @Inject(method = "getTexture(Lnet/minecraft/client/network/AbstractClientPlayerEntity;)Lnet/minecraft/util/Identifier;", cancellable = true, at = @At("TAIL"))
    public void getTexture(AbstractClientPlayerEntity abstractClientPlayerEntity, CallbackInfoReturnable<Identifier> cir) {
        if(currentlySpoofed.containsKey(abstractClientPlayerEntity.getGameProfile().getName())) {
            var username = currentlySpoofed.get(abstractClientPlayerEntity.getGameProfile().getName()).getLeft();
            if(TEXTURE_CACHE.containsKey(username)) {
                cir.setReturnValue(TEXTURE_CACHE.get(username));
                return;
            }
            try {
                var id = SkinManager.loadFromFile(SkinManager.downloadSkin(username));
                TEXTURE_CACHE.put(username, id);
                cir.setReturnValue(id);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    private AbstractClientPlayerEntity entity;

    @Inject(method = "renderLabelIfPresent(Lnet/minecraft/client/network/AbstractClientPlayerEntity;Lnet/minecraft/text/Text;Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V", at = @At("HEAD"))
    public void renderLabelMixin(AbstractClientPlayerEntity abstractClientPlayerEntity, Text text, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, CallbackInfo ci) {
        entity = abstractClientPlayerEntity;
    }

    @ModifyVariable(method = "renderLabelIfPresent(Lnet/minecraft/client/network/AbstractClientPlayerEntity;Lnet/minecraft/text/Text;Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V", ordinal = 0, at = @At("HEAD"), argsOnly = true)
    public Text setText(Text text) {
        if(currentlySpoofed.containsKey(entity.getGameProfile().getName())) {
            return currentlySpoofed.get(entity.getGameProfile().getName()).getRight();
        }
        return text;
    }
}
