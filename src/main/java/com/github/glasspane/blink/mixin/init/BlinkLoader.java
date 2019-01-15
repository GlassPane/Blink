package com.github.glasspane.blink.mixin.init;

import com.github.glasspane.blink.Blink;
import com.github.glasspane.mesh.util.CalledByReflection;
import net.fabricmc.api.ModInitializer;

@CalledByReflection
public class BlinkLoader implements ModInitializer {
    @Override
    public void onInitialize() {
        Blink.onLoad();
    }
}
