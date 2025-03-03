package com.cak.pattern_schematics.mixin;

import com.cak.pattern_schematics.foundation.mirror.PatternSchematicLevel;
import com.cak.pattern_schematics.registry.PatternSchematicsItems;
import com.simibubi.create.content.kinetics.deployer.DeployerMovementBehaviour;
import com.simibubi.create.content.schematics.SchematicItem;
import com.tterrag.registrate.util.entry.ItemEntry;
import net.createmod.catnip.levelWrappers.SchematicLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value = DeployerMovementBehaviour.class, remap = false)
public class DeployerMovementBehaviorMixin {

  @Unique
  private ItemStack pattern_Schematics$currentBlueprint;

  @Redirect(method = "activate", at = @At(value = "INVOKE", target = "Lcom/tterrag/registrate/util/entry/ItemEntry;isIn(Lnet/minecraft/world/item/ItemStack;)Z"))
  public boolean isIn(ItemEntry<SchematicItem> instance, ItemStack stack) {
    pattern_Schematics$currentBlueprint = stack;
    return instance.isIn(stack) || PatternSchematicsItems.PATTERN_SCHEMATIC.isIn(stack);
  }
  
  @Redirect(method = "activateAsSchematicPrinter", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/levelgen/structure/BoundingBox;isInside(Lnet/minecraft/core/Vec3i;)Z", remap = true))
  public boolean isInside(BoundingBox instance, Vec3i vec3i) {
    if (PatternSchematicsItems.PATTERN_SCHEMATIC.isIn(pattern_Schematics$currentBlueprint)) {
      return true;
    }
    return instance.isInside(vec3i);
  }
  
  @Redirect(method = "activateAsSchematicPrinter", at = @At(value = "INVOKE", target = "Lnet/createmod/catnip/levelWrappers/SchematicLevel;getBlockState(Lnet/minecraft/core/BlockPos;)Lnet/minecraft/world/level/block/state/BlockState;", remap = true))
  public BlockState getBlockState(SchematicLevel instance, BlockPos globalPos) {
    if (instance instanceof PatternSchematicLevel patternSchematicLevel) {
      return instance.getBlockState(pattern_Schematics$getSourceOfLocal(globalPos, patternSchematicLevel));
    }
    else return instance.getBlockState(globalPos);
  }
  
  @Unique
  public BlockPos pattern_Schematics$getSourceOfLocal(BlockPos position, PatternSchematicLevel patternSchematicLevel) {
    position = position.subtract(patternSchematicLevel.anchor);
    BoundingBox box = patternSchematicLevel.getBounds();
    return new BlockPos(
        pattern_Schematics$repeatingBounds(position.getX(), box.minX(), box.maxX()),
        pattern_Schematics$repeatingBounds(position.getY(), box.minY(), box.maxY()),
        pattern_Schematics$repeatingBounds(position.getZ(), box.minZ(), box.maxZ())
    ).offset(patternSchematicLevel.anchor);
  }
  
  @Unique
  private int pattern_Schematics$repeatingBounds(int source, int min, int max) {
    return (Math.floorMod(source, (max-min)+1) + min);
  }
  
  @Redirect(method = "activateAsSchematicPrinter", at = @At(value = "INVOKE", target = "Lnet/createmod/catnip/levelWrappers/SchematicLevel;getBlockEntity(Lnet/minecraft/core/BlockPos;)Lnet/minecraft/world/level/block/entity/BlockEntity;", remap = true))
  public BlockEntity getBlockEntity(SchematicLevel instance, BlockPos globalPos) {
    if (instance instanceof PatternSchematicLevel patternSchematicLevel)
      return instance.getBlockEntity(pattern_Schematics$getSourceOfLocal(globalPos, patternSchematicLevel));
    return instance.getBlockEntity(globalPos);
  }

}
