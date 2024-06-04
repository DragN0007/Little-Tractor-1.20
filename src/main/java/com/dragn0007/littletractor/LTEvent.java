package com.dragn0007.littletractor;

import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;


@Mod.EventBusSubscriber(modid = LittleTractorMain.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class LTEvent {
    @SubscribeEvent
    public static void registerLayerDefinitionEvent(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(TractorRenderer.LAYER_LOCATION, TractorModel::createBodyLayer);
    }

    @SubscribeEvent
    public static void registerRendererEvent(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(LittleTractorMain.TRACTOR.get(), TractorRenderer::new);
    }
}




