package com.cak.pattern_schematics.mixin;

import com.cak.pattern_schematics.foundation.mixin_accessors.SchematicTableBlockEntityMixinAccessor;
import com.simibubi.create.content.schematics.ServerSchematicLoader;
import com.simibubi.create.content.schematics.table.SchematicTableBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value = ServerSchematicLoader.class, remap = false)
public class ServerSchematicLoaderMixin {
  
  @Unique
  private static SchematicTableBlockEntity pattern_Schematics$uploadTargetTable;
  
  @Redirect(method = "handleFinishedUpload", at = @At(value = "INVOKE", target = "Lcom/simibubi/create/content/schematics/ServerSchematicLoader;getTable(Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;)Lcom/simibubi/create/content/schematics/table/SchematicTableBlockEntity;"))
  private SchematicTableBlockEntity injected(ServerSchematicLoader instance, Level world, BlockPos pos) {
    pattern_Schematics$uploadTargetTable = instance.getTable(world, pos);
    return instance.getTable(world, pos);
  }
  
  @Redirect(method = "handleFinishedUpload", at = @At(value = "INVOKE", target = "Lcom/simibubi/create/content/schematics/SchematicItem;create(Lnet/minecraft/world/level/Level;Ljava/lang/String;Ljava/lang/String;)Lnet/minecraft/world/item/ItemStack;"))
  private ItemStack injected(Level lookup, String schematic, String owner) {
    return ((SchematicTableBlockEntityMixinAccessor) pattern_Schematics$uploadTargetTable).getSchematicSource()
        .getFactory().create(lookup, schematic, owner);
  }

}
