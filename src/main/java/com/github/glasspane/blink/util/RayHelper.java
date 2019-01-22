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
import net.minecraft.util.math.*;
import net.minecraft.world.*;

public class RayHelper {

    //TODO if facing = up or down still apply unstuck logic
    //FIXME need better logic for moving player BB out of adjacent blocks!
    public static Vec3d raytrace(Entity entity, float deltaTime) {
        World world = entity.world;
        HitResult trace = MeshHelper.rayTraceEntity(entity, Blink.BLINK_RANGE, RayTraceContext.ShapeType.COLLIDER, RayTraceContext.FluidHandling.SOURCE_ONLY, deltaTime);
        switch(trace.getType()) {
            case ENTITY:
                //TODO back up by entity collision box size
                break;
            case NONE:
                trace = MeshHelper.rayTrace(world, entity, trace.getPos(), trace.getPos().subtract(0, 1, 0), RayTraceContext.ShapeType.COLLIDER, RayTraceContext.FluidHandling.SOURCE_ONLY);
                break;
        }
        Vec3d pos = trace.getPos();
        if(trace.getType() == HitResult.Type.BLOCK) {
            BlockHitResult result = (BlockHitResult) trace;
            switch(result.getSide()) {
                case UP:
                    break;
                case DOWN:
                    pos = pos.subtract(0, entity.getHeight(), 0);
                default:
                    if(pos.y - (int) pos.y >= 0.5D) {
                        BlockPos testPos;
                        switch(result.getSide()) {
                            case EAST:
                                testPos = new BlockPos(pos.x - 1, pos.y + 1, pos.z);
                                break;
                            case SOUTH:
                                testPos = new BlockPos(pos.x, pos.y + 1, pos.z - 1);
                                break;
                            default:
                                testPos = new BlockPos(pos.x, pos.y + 1, pos.z);
                        }
                        Vec3d entityPos = entity.getCameraPosVec(deltaTime);
                        Vec3d toTarget = pos.subtract(entityPos);
                        if(world.isAir(testPos) && world.isAir(testPos.up())) {
                            toTarget.multiply(Math.max((toTarget.length() + 0.8D) / toTarget.length(), 1.0D));
                            pos = new Vec3d(entityPos.x + toTarget.x, testPos.getY(), entityPos.z + toTarget.z);
                        }
                    }
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
        return pos;
    }
}
