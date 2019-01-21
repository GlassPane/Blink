/*
 * Blink
 * Copyright (C) 2019-2019 GlassPane
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses>.
 */
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
            GlStateManager.blendFunc(GlStateManager.class_1033.SRC_COLOR, GlStateManager.class_1027.CONSTANT_COLOR);
            //GlStateManager.blendFunc(GlStateManager.SrcBlendFactor.SRC_COLOR, GlStateManager.DstBlendFactor.CONSTANT_COLOR); //TODO mappings broke
            GL14.glBlendColor(0.2F, 0.9F, 0.9F, 0.7F);
            ci.cancel();
        }
    }
}
