package com.cak.pattern_schematics;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.CreateClient;
import com.simibubi.create.content.contraptions.actors.seat.ContraptionPlayerPassengerRotation;
import com.simibubi.create.content.contraptions.minecart.CouplingRenderer;
import com.simibubi.create.content.kinetics.chainConveyor.ChainConveyorInteractionHandler;
import com.simibubi.create.content.trains.entity.CarriageCouplingRenderer;
import com.simibubi.create.content.trains.track.TrackBlockOutline;
import com.simibubi.create.content.trains.track.TrackTargetingClient;
import net.createmod.catnip.animation.AnimationTickHolder;
import net.createmod.catnip.render.DefaultSuperRenderTypeBuffer;
import net.createmod.catnip.render.SuperRenderTypeBuffer;
import net.minecraft.client.Minecraft;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.RegisterGuiOverlaysEvent;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import net.minecraftforge.client.gui.overlay.VanillaGuiOverlay;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(Dist.CLIENT)
public class PatternSchematicsClientEvents {
  
  @SubscribeEvent
  public static void onTick(TickEvent.ClientTickEvent event) {
    if (Minecraft.getInstance().level == null || Minecraft.getInstance().player == null)
      return;
    PatternSchematicsClient.PATTERN_SCHEMATIC_HANDLER.tick();
  }

  @SubscribeEvent
  public static void onRenderWorld(RenderLevelStageEvent event) {
    if (event.getStage() != RenderLevelStageEvent.Stage.AFTER_PARTICLES)
      return;

    PoseStack ms = event.getPoseStack();
    ms.pushPose();
    SuperRenderTypeBuffer buffer = DefaultSuperRenderTypeBuffer.getInstance();
    Vec3 camera = Minecraft.getInstance().gameRenderer.getMainCamera()
        .getPosition();

    PatternSchematicsClient.PATTERN_SCHEMATIC_HANDLER.render(ms, buffer, camera);

    buffer.draw();
    RenderSystem.enableCull();
    ms.popPose();

    ContraptionPlayerPassengerRotation.frame();
  }
  
  @SubscribeEvent
  public static void onKeyInput(InputEvent.Key event) {
    boolean pressed = !(event.getAction() == 0);
    PatternSchematicsClient.PATTERN_SCHEMATIC_HANDLER.onKeyInput(event.getKey(), pressed);
  }
  
  @SubscribeEvent
  public static void onMouseScrolled(InputEvent.MouseScrollingEvent event) {
    if (Minecraft.getInstance().screen != null)
      return;
    
    double delta = event.getScrollDelta();
    boolean cancelled = PatternSchematicsClient.PATTERN_SCHEMATIC_HANDLER.mouseScrolled(delta);
    event.setCanceled(cancelled);
  }
  
  @SubscribeEvent
  public static void onMouseInput(InputEvent.MouseButton.Pre event) {
    if (Minecraft.getInstance().screen != null)
      return;
    
    int button = event.getButton();
    boolean pressed = !(event.getAction() == 0);
    
    if (PatternSchematicsClient.PATTERN_SCHEMATIC_HANDLER.onMouseInput(button, pressed))
      event.setCanceled(true);
  }
  
  @Mod.EventBusSubscriber(value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
  public static class ModBusEvents {
    
    @SubscribeEvent
    public static void registerGuiOverlays(RegisterGuiOverlaysEvent event) {
      event.registerAbove(VanillaGuiOverlay.HOTBAR.id(), "pattern_schematic", PatternSchematicsClient.PATTERN_SCHEMATIC_HANDLER);
    }
    
  }
  
}
