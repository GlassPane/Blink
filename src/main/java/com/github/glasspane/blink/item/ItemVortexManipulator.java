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
package com.github.glasspane.blink.item;

import com.github.glasspane.blink.handler.BlinkHandler;
import com.github.glasspane.blink.net.TeleportHandler;
import com.github.glasspane.blink.util.RayHelper;
import net.fabricmc.api.*;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.item.TooltipOptions;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.text.*;
import net.minecraft.util.*;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class ItemVortexManipulator extends Item {

    public ItemVortexManipulator() {
        super(new Item.Settings().itemGroup(ItemGroup.TOOLS));
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
        player.setCurrentHand(hand);
        return new TypedActionResult<>(ActionResult.SUCCESS, player.getStackInHand(hand));
    }

    @Override
    public int getMaxUseTime(ItemStack itemStack_1) {
        return 72000;
    }

    @Override
    public void onItemStopUsing(ItemStack stack, World world, LivingEntity entity, int durationLeft) {
        if(world.isClient && entity instanceof PlayerEntity && BlinkHandler.canBlink((PlayerEntity) entity)) {
            TeleportHandler.sendToServer(RayHelper.raytrace(entity, MinecraftClient.getInstance().getTickDelta()));
        }
    }

    @Override
    public ItemStack onItemFinishedUsing(ItemStack stack, World world, LivingEntity entity) {
        if(world.isClient && entity instanceof PlayerEntity && BlinkHandler.canBlink((PlayerEntity) entity)) {
            TeleportHandler.sendToServer(RayHelper.raytrace(entity, MinecraftClient.getInstance().getTickDelta()));
        }
        return stack;
    }

    @Override
    @Environment(EnvType.CLIENT)
    public void buildTooltip(ItemStack stack, @Nullable World world, List<TextComponent> tooltip, TooltipOptions options) {
        super.buildTooltip(stack, world, tooltip, options);
        tooltip.add(new TranslatableTextComponent("item.blink.vortex_manipulator.tooltip").setStyle(new Style().setColor(TextFormat.GRAY).setItalic(true)));
    }
}
