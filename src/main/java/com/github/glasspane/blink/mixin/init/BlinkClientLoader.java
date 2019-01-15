package com.github.glasspane.blink.mixin.init;

import com.github.glasspane.blink.client.BlinkClientHandler;
import com.github.glasspane.mesh.util.CalledByReflection;
import net.fabricmc.api.*;

@CalledByReflection
@Environment(EnvType.CLIENT)
public class BlinkClientLoader implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        BlinkClientHandler.onLoad();
    }
}
