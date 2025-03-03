package com.cak.pattern_schematics.foundation.mirror;

import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.AllSpecialTextures;
import com.simibubi.create.content.schematics.client.SchematicHandler;
import net.createmod.catnip.animation.AnimationTickHolder;
import net.createmod.catnip.outliner.AABBOutline;
import net.createmod.catnip.render.SuperRenderTypeBuffer;
import net.minecraft.world.phys.Vec3;

public class SimpleSchematicOutlineRenderer {
  
  public static void render(PoseStack ms, SchematicHandler schematicHandler, SuperRenderTypeBuffer buffer) {
    ms.pushPose();
    AABBOutline outline = schematicHandler.getOutline();
    outline.getParams()
        .colored(0x6886c5)
        .withFaceTexture(AllSpecialTextures.CHECKERED)
        .lineWidth(1 / 16f);
    applyOutlineModification(schematicHandler);
    outline.render(ms, buffer, Vec3.ZERO, AnimationTickHolder.getPartialTicks());
    outline.getParams()
        .clearTextures();
    ms.popPose();
  }
  
  public static void applyOutlineModification(SchematicHandler schematicHandler) {
    if (schematicHandler instanceof PatternSchematicHandler) {
      AABBOutline outline = schematicHandler.getOutline();
      PatternSchematicHandler patternSchematicHandler = (PatternSchematicHandler) schematicHandler;
      outline.getParams()
          .colored(patternSchematicHandler.isRenderingMain() ? 0x9352a3 : 0xa6a1af)
          .lineWidth(
              patternSchematicHandler.isRenderingMain() ?
                  (patternSchematicHandler.isRenderingMultiple() ? 3/32f : 1/16f)
                  : 1/32f
          );
    }
  }
  
}
