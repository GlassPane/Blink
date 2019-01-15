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
package com.github.glasspane.blink.handler;

import nerdhub.textilelib.events.block.BlockBreakEvent;
import nerdhub.textilelib.events.tick.PlayerTickEvent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.Vec3d;

import java.util.WeakHashMap;

public class BlinkHandler {
    public static final WeakHashMap<PlayerEntity, Vec3d> PLAYER_TARGETS = new WeakHashMap<>();

    public static void onEntityTick(PlayerTickEvent event) {
        synchronized (PLAYER_TARGETS) {
            if(event.getPlayer() instanceof ServerPlayerEntity && PLAYER_TARGETS.containsKey(event.getPlayer())) {
                ServerPlayerEntity player = (ServerPlayerEntity) event.getPlayer();
                Vec3d targetPos = PLAYER_TARGETS.get(player);
                //TODO make player leap towards target pos
                //player.move(MovementType.SELF, targetPos.x - player.x, targetPos.y - player.y, targetPos.z - player.z);
                //player.method_5792(); //move pos inside bounding box
                //player.velocityModified = true;
                player.method_5859(targetPos.x, targetPos.y, targetPos.z);
                player.fallDistance = 0.0F;
                PLAYER_TARGETS.remove(player);
            }
        }
    }

    //FIXME remove?
    public static void onBreakBlock(BlockBreakEvent event) {
        if(event.getEntity() instanceof ServerPlayerEntity && canBlink((PlayerEntity) event.getEntity())) {
            event.cancelEvent();
        }
    }

    @SuppressWarnings("all")
    public static boolean canBlink(PlayerEntity player) {
        return true; //TODO add check for whether or not the player is able to teleport, i.e. holds item etc
    }
}
