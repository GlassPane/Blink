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
package com.github.glasspane.blink.net;

import com.github.glasspane.blink.Blink;
import com.github.glasspane.blink.handler.BlinkHandler;
import io.netty.buffer.Unpooled;
import net.fabricmc.api.*;
import net.fabricmc.fabric.networking.PacketContext;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.packet.CustomPayloadServerPacket;
import net.minecraft.util.*;
import net.minecraft.util.math.Vec3d;

public class TeleportHandler {

    public static Identifier PACKET_ID = new Identifier(Blink.MODID, "blink");

    @SuppressWarnings("ConstantConditions")
    @Environment(EnvType.CLIENT)
    public static void sendToServer(Vec3d targetPos) {
        PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
        buf.writeDouble(targetPos.x);
        buf.writeDouble(targetPos.y);
        buf.writeDouble(targetPos.z);
        MinecraftClient.getInstance().getNetworkHandler().sendPacket(new CustomPayloadServerPacket(PACKET_ID, buf));
    }

    public static void onPacketReceived(PacketContext ctx, PacketByteBuf buf) {
        PlayerEntity player = ctx.getPlayer();
        if(BlinkHandler.canBlink(player)) {
            Vec3d targetPos = new Vec3d(buf.readDouble(), buf.readDouble(), buf.readDouble());
            synchronized (BlinkHandler.PLAYER_TARGETS) {
                BlinkHandler.PLAYER_TARGETS.put(player, targetPos); //TODO verify that data
            }
        }
    }
}
