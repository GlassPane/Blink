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
package com.github.glasspane.blink.util;

import com.github.glasspane.blink.Blink;
import com.github.glasspane.mesh.util.MeshHelper;
import net.minecraft.entity.Entity;
import net.minecraft.util.HitResult;
import net.minecraft.util.math.*;
import net.minecraft.world.FluidRayTraceMode;

public class RayHelper {

    //TODO if facing = up or down still apply unstuck logic
    //FIXME need better logic for moving player BB out of adjacent blocks!
    public static Vec3d raytrace(Entity entity, float deltaTime) {
        HitResult result = MeshHelper.rayTrace(entity, Blink.BLINK_RANGE, deltaTime, FluidRayTraceMode.SOURCE_ONLY);
        if(result.side != null) {
            switch(result.side) {
                case UP:
                    return result.pos;
                case DOWN:
                    return result.pos.subtract(0, entity.height, 0);
                default:
                    Vec3d playerPos = entity.getPosVector();
                    Vec3d playerToTarget = result.pos.subtract(playerPos);
                    //check edge
                    if(result.pos.y - (int) result.pos.y >= 0.5D) {
                        BlockPos testPos = new BlockPos(result.pos.x, result.pos.y + 1, result.pos.z);
                        //band-aid fix for flooring errors
                        if(result.side == Direction.EAST) {
                            testPos = testPos.offset(Direction.WEST);
                        }
                        else if(result.side == Direction.SOUTH) {
                            testPos = testPos.offset(Direction.NORTH);
                        }
                        if(entity.world.isAir(testPos)) {
                            playerToTarget = playerToTarget.multiply(Math.max((playerToTarget.length() + 0.8D) / playerToTarget.length(), 1.0D));
                            return new Vec3d(playerPos.x + playerToTarget.x, testPos.getY(), playerPos.z + playerToTarget.z);
                        }
                    }
                    //unstuck from wall
                    playerToTarget = playerToTarget.multiply(Math.max((playerToTarget.length() - (entity.width / 2.0F) - 0.0001D) / playerToTarget.length(), 0.0001D));
                    Vec3d newOrigin = playerPos.add(playerToTarget);
                    Vec3d newEnd = newOrigin.add(0.0D, -1.0D, 0.0D);
                    result = entity.world.rayTrace(newOrigin, newEnd, FluidRayTraceMode.SOURCE_ONLY);
                    if(result == null) {
                        result = new HitResult(HitResult.Type.NONE, newEnd, null, new BlockPos(newEnd));
                    }
                    //TODO actually we should raytrace UP from here, then check if length() >= player height
                    Vec3d ret;
                    loop:
                    for(int i = 0; i < 3; i++) {
                        ret = result.pos.subtract(0.0D, i, 0.0D);
                        for(int j = 0; j <= entity.height + 1; j++) {
                            if(!entity.world.isAir(new BlockPos(ret.x, ret.y + j, ret.z))) {
                                continue loop;
                            }
                        }
                        return ret;
                    }
            }
        }
        return result.pos;
    }
}
