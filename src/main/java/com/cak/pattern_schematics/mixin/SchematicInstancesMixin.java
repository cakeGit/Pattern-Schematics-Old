package com.cak.pattern_schematics.mixin;

import com.cak.pattern_schematics.content.PatternSchematicItem;
import com.cak.pattern_schematics.foundation.mirror.PatternSchematicLevel;
import com.simibubi.create.content.schematics.SchematicInstances;
import net.createmod.catnip.levelWrappers.SchematicLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = SchematicInstances.class, remap = false)
public class SchematicInstancesMixin {
  
  @Unique
  private static ItemStack pattern_Schematics$lastThreadStack = null;
  @Unique
  private static StructureTemplate pattern_Schematics$lastThreadStructureTemplate = null;
  
  @Inject(method = "loadWorld", at = @At(value = "HEAD"))
  private static void loadWorld(Level wrapped, ItemStack schematic, CallbackInfoReturnable<SchematicLevel> cir) {
    pattern_Schematics$lastThreadStack = schematic;
  }
  
  @ModifyVariable(method = "loadWorld", at = @At(value = "STORE"), ordinal = 0)
  private static StructureTemplate store_activeTemplate(StructureTemplate template) {
    pattern_Schematics$lastThreadStructureTemplate = template;
    return template;
  }
  
  @ModifyVariable(method = "loadWorld", at = @At("STORE"), ordinal = 0)
  private static SchematicLevel loadWorld(SchematicLevel value) {
    if (pattern_Schematics$lastThreadStack.getItem() instanceof PatternSchematicItem) {
      PatternSchematicLevel patternSchematicLevel = new PatternSchematicLevel(value.anchor, value.getLevel());
      patternSchematicLevel.putExtraData(pattern_Schematics$lastThreadStack, pattern_Schematics$lastThreadStructureTemplate);
      return patternSchematicLevel;
    }
    return value;
  }
  
}
