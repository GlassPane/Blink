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

    public static Vec3d raytrace(Entity entity, float deltaTime) {
        World world = entity.world;
        HitResult trace = MeshHelper.rayTraceEntity(entity, Blink.BLINK_RANGE, RayTraceContext.ShapeType.COLLIDER, RayTraceContext.FluidHandling.SOURCE_ONLY, deltaTime);
        boolean secondPass;
        if(trace.getType() == HitResult.Type.NONE) {
            trace = MeshHelper.rayTrace(world, entity, trace.getPos(), trace.getPos().subtract(0, 1, 0), RayTraceContext.ShapeType.COLLIDER, RayTraceContext.FluidHandling.SOURCE_ONLY);
            secondPass = false;
        }
        else {
            secondPass = true;
        }
        Vec3d pos = trace.getPos();
        if(trace.getType() == HitResult.Type.BLOCK) {
            BlockHitResult result = (BlockHitResult) trace;
            switch(result.getSide()) {
                case DOWN:
                    pos = pos.subtract(0, entity.getHeight(), 0);
                    break;
                case UP:
                    secondPass = false;
                    break;
                default:
                    Vec3d entityPos = entity.getCameraPosVec(deltaTime);
                    Vec3d toTarget = pos.subtract(entityPos);
                    if(pos.y - (int) pos.y >= 0.5D) {
                        BlockPos testPos;
                        switch(result.getSide()) {
                            case EAST:
                                testPos = new BlockPos(pos.x - 1, pos.y + 1, pos.z);
                                break;
                            case WEST:
                            case NORTH:
                                testPos = new BlockPos(pos.x, pos.y + 1, pos.z);
                                break;
                            case SOUTH:
                                testPos = new BlockPos(pos.x, pos.y + 1, pos.z - 1);
                                break;
                                default: //should never happen, but better safe than sorry
                                    throw new RuntimeException("hit result had wrong value: " + result.getSide());
                        }
                        if(world.isAir(testPos) && world.isAir(testPos.up())) { //TODO check all blocks in BB
                            toTarget = toTarget.multiply(Math.max((toTarget.length() + 0.8D) / toTarget.length(), 1.0D));
                            pos = new Vec3d(entityPos.x + toTarget.x, testPos.getY() + 0.1D, entityPos.z + toTarget.z);
                            HitResult result1 = MeshHelper.rayTrace(world, entity, pos, pos.subtract(0.0D, 1.0D, 0.0D), RayTraceContext.ShapeType.COLLIDER, RayTraceContext.FluidHandling.SOURCE_ONLY);
                            pos = result1.getPos();
                            secondPass = false;
                        }
                    }
                    if(secondPass) {
                        toTarget = toTarget.multiply((toTarget.length() - (entity.getWidth() * 1.3F)) / toTarget.length());
                        pos = entityPos.add(toTarget);
                        HitResult result1 = MeshHelper.rayTrace(world, entity, pos, pos.subtract(0.0D, 1.0D, 0.0D), RayTraceContext.ShapeType.COLLIDER, RayTraceContext.FluidHandling.SOURCE_ONLY);
                        pos = result1.getPos();
                    }
            }
        }
        if(secondPass) {
            Vec3d tempPos = pos.subtract(0.0D, 0.0001D, 0.0D);
            HitResult flagTrace = MeshHelper.rayTrace(world, entity, tempPos, pos.add(0.0D, entity.getHeight(), 0.0D), RayTraceContext.ShapeType.COLLIDER, RayTraceContext.FluidHandling.SOURCE_ONLY);
            if(flagTrace.getPos().y - tempPos.y < entity.getHeight()) {
                pos = flagTrace.getPos().subtract(0, entity.getHeight(), 0);
            }
        }
        return pos;
    }
}
