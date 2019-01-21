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
import net.minecraft.util.*;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.*;

public class RayHelper {

    //TODO if facing = up or down still apply unstuck logic
    //FIXME need better logic for moving player BB out of adjacent blocks!
    public static Vec3d raytrace(Entity entity, float deltaTime) {
        World world = entity.world;
        HitResult result = MeshHelper.rayTraceEntity(entity, Blink.BLINK_RANGE, RayTraceContext.ShapeType.COLLIDER, RayTraceContext.FluidHandling.SOURCE_ONLY, deltaTime);
        switch(result.getType()) {
            case ENTITY:
                //TODO back up by entity collision box size
                break;
            case BLOCK:
                break;
            case NONE: //TODO trace down by 1 block
                result = MeshHelper.rayTrace(world, entity, result.getPos(), result.getPos().subtract(0, 1, 0), RayTraceContext.ShapeType.COLLIDER, RayTraceContext.FluidHandling.SOURCE_ONLY);
                break;
        }
        Vec3d ret = result.getPos();
        if(result.getType() == HitResult.Type.BLOCK) {
            BlockHitResult hitResult = (BlockHitResult) result;
            switch(hitResult.getSide()) {
                case UP:
                    break;
                case DOWN:
                    ret = ret.subtract(0, entity.getHeight(), 0);
                default:
                    if(ret.y >= 0.5D) { //TODO check cliffs

                    }


//                    Vec3d playerPos = entity.getPosVector();
//                    Vec3d playerToTarget = result.getPos().subtract(playerPos);
//                    //check edge
//                    if(result.pos.y - (int) result.pos.y >= 0.5D) {
//                        BlockPos testPos = new BlockPos(result.pos.x, result.pos.y + 1, result.pos.z);
//                        //band-aid fix for flooring errors
//                        if(result.side == Direction.EAST) {
//                            testPos = testPos.offset(Direction.WEST);
//                        }
//                        else if(result.side == Direction.SOUTH) {
//                            testPos = testPos.offset(Direction.NORTH);
//                        }
//                        if(entity.world.isAir(testPos)) {
//                            playerToTarget = playerToTarget.multiply(Math.max((playerToTarget.length() + 0.8D) / playerToTarget.length(), 1.0D));
//                            return new Vec3d(playerPos.x + playerToTarget.x, testPos.getY(), playerPos.z + playerToTarget.z);
//                        }
//                    }
//                    //unstuck from wall
//                    playerToTarget = playerToTarget.multiply(Math.max((playerToTarget.length() - (entity.width / 2.0F) - 0.0001D) / playerToTarget.length(), 0.0001D));
//                    Vec3d newOrigin = playerPos.add(playerToTarget);
//                    Vec3d newEnd = newOrigin.add(0.0D, -1.0D, 0.0D);
//
//                    result = MeshHelper.rayTraceEntity(entity.world, newOrigin, newEnd, RayTraceContext.ShapeType.COLLIDER, RayTraceContext.FluidHandling.SOURCE_ONLY);
//                    if(result == null) {
//                        Vec3d diff = newOrigin.subtract(newEnd);
//                        result = BlockHitResult.createMissed(newEnd, Direction.getFacing(diff.x, diff.y, diff.z), new BlockPos(newEnd)); //new HitResult(HitResult.Type.NONE, newEnd, null, new BlockPos(newEnd));
//                    }
//                    //TODO actually we should raytrace UP from here, then check if length() >= player height
//                    Vec3d ret;
//                    loop:
//                    for(int i = 0; i < 3; i++) {
//                        ret = result.pos.subtract(0.0D, i, 0.0D);
//                        for(int j = 0; j <= entity.height + 1; j++) {
//                            if(!entity.world.isAir(new BlockPos(ret.x, ret.y + j, ret.z))) {
//                                continue loop;
//                            }
//                        }
//                        return ret;
//                    }
            }
        }

        //TODO unstuck entity box here
        return ret;
    }
}
