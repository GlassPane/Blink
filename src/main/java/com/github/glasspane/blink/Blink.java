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
package com.github.glasspane.blink;

import com.github.glasspane.blink.handler.BlinkHandler;
import com.github.glasspane.blink.net.TeleportHandler;
import com.github.glasspane.mesh.util.logging.PrefixMessageFactory;
import nerdhub.textilelib.eventhandlers.EventRegistry;
import nerdhub.textilelib.events.block.BlockBreakEvent;
import nerdhub.textilelib.events.tick.PlayerTickEvent;
import net.fabricmc.fabric.networking.CustomPayloadPacketRegistry;
import org.apache.logging.log4j.*;

public class Blink {
    public static final String MODID = "blink";
    public static final String MOD_NAME = "Blink";
    public static final String VERSION = "${version}";
    public static final double BLINK_RANGE = 15.0D; //TODO move to config

    private static final Logger log = LogManager.getLogger(MODID, new PrefixMessageFactory(MOD_NAME));

    public static Logger getLogger() {
        return log;
    }

    public static void onLoad() {
        log.info("Too fast!");
        CustomPayloadPacketRegistry.SERVER.register(TeleportHandler.PACKET_ID, TeleportHandler::onPacketReceived);
        EventRegistry.INSTANCE.registerEventHandler(PlayerTickEvent.class, BlinkHandler::onEntityTick);
        EventRegistry.INSTANCE.registerEventHandler(BlockBreakEvent.class, BlinkHandler::onBreakBlock);
    }
}
