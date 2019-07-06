package com.github.briansemrau.biggercaves.mixin;

import net.minecraft.world.gen.carver.RavineCarver;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(RavineCarver.class)
public class RavineCarverMixin {

    @ModifyArg(method = "method_12656",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/gen/carver/RavineCarver;carveRavine(Lnet/minecraft/world/chunk/Chunk;JIIIDDDFFFIIDLjava/util/BitSet;)V"),
            index = 8)
    private float ravineSize(float float_1) {
        return float_1 * 1.5F;
    }

}
