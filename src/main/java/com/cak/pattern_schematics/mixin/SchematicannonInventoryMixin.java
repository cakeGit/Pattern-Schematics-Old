package com.cak.pattern_schematics.mixin;

import com.cak.pattern_schematics.registry.PatternSchematicsItems;
import com.simibubi.create.AllItems;
import com.simibubi.create.content.schematics.cannon.SchematicannonInventory;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = SchematicannonInventory.class, remap = false)
public class SchematicannonInventoryMixin {
  
  @Inject(method = "isItemValid", at = @At("RETURN"), cancellable = true)
  public void isItemValid(int slot, ItemStack stack, CallbackInfoReturnable<Boolean> cir) {
    if (slot == 0) {
      cir.setReturnValue(
          PatternSchematicsItems.PATTERN_SCHEMATIC.isIn(stack)
          || AllItems.SCHEMATIC.isIn(stack)
      );
    }
  }
  
}
