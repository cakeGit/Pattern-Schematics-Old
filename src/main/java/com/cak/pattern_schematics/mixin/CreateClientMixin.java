package com.cak.pattern_schematics.mixin;

import com.cak.pattern_schematics.PatternSchematicsClient;
import com.simibubi.create.CreateClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CreateClient.class)
public class CreateClientMixin {

    @Inject(method = "invalidateRenderers", at = @At("HEAD"))
    private static void invalidateRenderers(CallbackInfo ci) {
        PatternSchematicsClient.invalidateRenderers();
    }

}
