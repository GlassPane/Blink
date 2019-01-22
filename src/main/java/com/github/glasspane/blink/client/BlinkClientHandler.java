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
package com.github.glasspane.blink.client;

import com.github.glasspane.blink.Blink;
import com.github.glasspane.blink.util.RayHelper;
import com.github.glasspane.mesh.util.CalledByReflection;
import com.mojang.blaze3d.platform.GlStateManager;
import nerdhub.textilelib.eventhandlers.EventRegistry;
import nerdhub.textilelib.events.render.RenderWorldEvent;
import net.fabricmc.api.*;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.entity.*;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.*;

@Environment(EnvType.CLIENT)
@CalledByReflection
public class BlinkClientHandler implements ClientModInitializer {

    private static boolean rendering = false;

    public static boolean isRendering() {
        return rendering;
    }

    @Override
    public void onInitializeClient() {
        EventRegistry.INSTANCE.registerEventHandler(RenderWorldEvent.class, BlinkClientHandler::onRenderWorld);
    }

    private static void onRenderWorld(RenderWorldEvent event) {
        if(MinecraftClient.getInstance().player.getActiveItem().getItem() == Blink.VORTEX_MANIPULATOR) {
            GlStateManager.enableCull();
            GlStateManager.disableLighting();
            GlStateManager.pushMatrix();
            rendering = true;
            {
                EntityRenderDispatcher dispatcher = MinecraftClient.getInstance().getEntityRenderManager();
                Entity cameraEntity = MinecraftClient.getInstance().getCameraEntity();
                if(cameraEntity != null) {
                    Vec3d targetPos = RayHelper.raytrace(cameraEntity, event.getPartialTicks());
                    double dX = MathHelper.lerp(event.getPartialTicks(), cameraEntity.prevRenderX, cameraEntity.x);
                    double dY = MathHelper.lerp(event.getPartialTicks(), cameraEntity.prevRenderY, cameraEntity.y);
                    double dZ = MathHelper.lerp(event.getPartialTicks(), cameraEntity.prevRenderZ, cameraEntity.z);
                    EntityRenderer<Entity> renderer = dispatcher.getRenderer(cameraEntity);
                    if(renderer != null) {
                        //TODO mappings
                        boolean backupShadow = dispatcher.method_3951(); //shouldRenderShadow
                        dispatcher.method_3948(false); //setRenderShadow
                        renderer.render(cameraEntity, targetPos.x - dX, targetPos.y - dY, targetPos.z - dZ, 0, event.getPartialTicks());
                        if(backupShadow) {
                            dispatcher.method_3948(true); //setRenderShadow
                        }
                    }
                }
            }
            rendering = false;
            GlStateManager.popMatrix();
            GlStateManager.enableLighting();
            GlStateManager.disableCull();
        }
    }
}
