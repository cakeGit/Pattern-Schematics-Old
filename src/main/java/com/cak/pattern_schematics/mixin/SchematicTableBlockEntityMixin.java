package com.cak.pattern_schematics.mixin;

import com.cak.pattern_schematics.foundation.SchematicUploadItemSource;
import com.cak.pattern_schematics.foundation.mixin_accessors.SchematicTableBlockEntityMixinAccessor;
import com.simibubi.create.content.schematics.table.SchematicTableBlockEntity;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(value = SchematicTableBlockEntity.class, remap = false)
public class SchematicTableBlockEntityMixin extends SmartBlockEntity implements SchematicTableBlockEntityMixinAccessor {
  
  @Unique
  SchematicUploadItemSource pattern_Schematics$schematicUploadItemSource = null;
  
  public SchematicTableBlockEntityMixin(BlockEntityType<?> type, BlockPos pos, BlockState state) {
    super(type, pos, state);
  }
  
  @Inject(method = "startUpload", at = @At("HEAD"))
  protected void read(String schematic, CallbackInfo ci) {
    pattern_Schematics$schematicUploadItemSource = SchematicUploadItemSource.tryFromItemStack(inventory.getStackInSlot(0));
    if (pattern_Schematics$schematicUploadItemSource == null) {
      //If anyone develops a similar mod, then like ask me to remove this ig
      new RuntimeException(
          "Pattern Schematics -> Warn (?) Tried to get the item source"+
              "type of an uploading schematic but could not find it!")
          .printStackTrace();
    }
  }
  
  @Inject(method = "read", at = @At("TAIL"))
  protected void read(CompoundTag compound, boolean clientPacket, CallbackInfo ci) {
    if (compound.contains("SchematicSource"))
      pattern_Schematics$schematicUploadItemSource = SchematicUploadItemSource.tryFromInt(compound.getInt("SchematicSource"));
  }
  
  @Inject(method = "write", at = @At("TAIL"))
  protected void write(CompoundTag compound, boolean clientPacket, CallbackInfo ci) {
    if (pattern_Schematics$schematicUploadItemSource != null)
      compound.putInt("SchematicSource", pattern_Schematics$schematicUploadItemSource.getNbtValue());
  }
  
  @Shadow
  public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
  
  }
  
  @Shadow
  public SchematicTableBlockEntity.SchematicTableInventory inventory;
  @Override
  public SchematicUploadItemSource getSchematicSource() {
    return pattern_Schematics$schematicUploadItemSource;
  }
  
}
