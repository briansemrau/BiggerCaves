package com.github.briansemrau.biggercaves.mixin;

import com.mojang.datafixers.Dynamic;
import net.minecraft.world.gen.ProbabilityConfig;
import net.minecraft.world.gen.carver.Carver;
import net.minecraft.world.gen.carver.CaveCarver;
import org.spongepowered.asm.lib.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.Random;
import java.util.function.Function;

import static java.lang.Math.*;

@Mixin(CaveCarver.class)
public abstract class CaveCarverMixin extends Carver<ProbabilityConfig> {

    public CaveCarverMixin(Function<Dynamic<?>, ? extends ProbabilityConfig> function_1, int int_1) {
        super(function_1, int_1);
    }

    private float adjustSize(float size) {
        size = max(size, 1);
        size *= 5.0F;
        size = min(size, 12);
        return size;
    }

    @Inject(method = "getTunnelSystemWidth",
            at = @At("TAIL"),
            locals = LocalCapture.CAPTURE_FAILHARD,
            cancellable = true)
    private void onGetTunnelSystemWidth(Random random, CallbackInfoReturnable<Float> cir, float float_1) {
        cir.setReturnValue(adjustSize(float_1));
    }

    @ModifyArg(method = "method_12673",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/gen/carver/CaveCarver;carveCave(Lnet/minecraft/world/chunk/Chunk;JIIIDDDFDLjava/util/BitSet;)V"),
            index = 8)
    private float caveSize(float float_1) {
        return adjustSize(float_1);
    }

    @ModifyArg(method = "carveTunnels",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/gen/carver/CaveCarver;carveTunnels(Lnet/minecraft/world/chunk/Chunk;JIIIDDDFFFIIDLjava/util/BitSet;)V"),
            index = 8)
    private float tunnelSize(float float_1) {
        return adjustSize(float_1);
    }

    @Inject(method = "getTunnelSystemHeightWidthRatio",
            at = @At("TAIL"), cancellable = true)
    private void onGetTunnelSystemHeightWidthRatio(CallbackInfoReturnable<Double> cir) {
        cir.setReturnValue(1.0); // 0.5 to 1.5 is a good range
    }

    @Redirect(method = "method_12676",
            at = @At(value = "FIELD",
                    target = "Lnet/minecraft/world/gen/ProbabilityConfig;probability:F",
                    opcode = Opcodes.GETFIELD))
    private float getProbability(ProbabilityConfig probabilityConfig) {
        return probabilityConfig.probability / (float) sqrt(5.0F);
    }

}
