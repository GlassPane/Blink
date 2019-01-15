package com.github.glasspane.blink.mixin.client;

import com.github.glasspane.blink.client.BlinkClientHandler;
import com.mojang.blaze3d.platform.GlStateManager;
import net.fabricmc.api.*;
import org.lwjgl.opengl.GL14;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(targets = "com/mojang/blaze3d/platform/GlStateManager$RenderMode$2")
public abstract class MixinRenderMode {

    @Inject(method = "begin()V", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/platform/GlStateManager;enableBlend()V", shift = At.Shift.AFTER), cancellable = true, remap = false)
    private void begin(CallbackInfo ci) {
        if(BlinkClientHandler.isRendering()) {
            GlStateManager.blendFunc(GlStateManager.SrcBlendFactor.SRC_COLOR, GlStateManager.DstBlendFactor.CONSTANT_COLOR);
            GL14.glBlendColor(0.2F, 0.9F, 0.9F, 0.7F);
            ci.cancel();
        }
    }
}
