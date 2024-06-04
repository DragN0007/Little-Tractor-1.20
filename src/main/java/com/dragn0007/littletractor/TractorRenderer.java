package com.dragn0007.littletractor;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3f;

public class TractorRenderer extends EntityRenderer<Tractor> {

    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation(LittleTractorMain.MODID, "tractor"), "main");

    public static final Animation BODY_ANIMATION = new Animation(1.5f, new Animation.KeyFrame[]{
            new Animation.KeyFrame(0f, 0f, 0f, 0f),
            new Animation.KeyFrame(0.375f, 1f, 0f, -1f),
            new Animation.KeyFrame(0.75f, 0f, 0f, 0f),
            new Animation.KeyFrame(1.125f, 1f, 0f, 1f),
            new Animation.KeyFrame(1.5f, 0f, 0f, 0f),
    });

    public static final Animation FRONT_WHEEL_ANIMATION = new Animation(1.5f, new Animation.KeyFrame[]{
            new Animation.KeyFrame(0f, 0f, 0f, 0f),
            new Animation.KeyFrame(1.5f, 360f, 0f, 0f)
    });

    public static final Animation BACK_WHEEL_ANIMATION = new Animation(1.5f, new Animation.KeyFrame[]{
            new Animation.KeyFrame(0f, 0f, 0f, 0f),
            new Animation.KeyFrame(1.5f, 360f, 0f, 0f)
    });

    public static final Animation TILLER_ANIMATION = new Animation(1.5f, new Animation.KeyFrame[]{
            new Animation.KeyFrame(0f, -2.5f, 0f, 0f),
            new Animation.KeyFrame(0.375f, 1f, 0f, 1f),
            new Animation.KeyFrame(0.75f, -2.5f, 0f, 0f),
            new Animation.KeyFrame(1.125f, 1, 0, -1),
            new Animation.KeyFrame(1.5f, -2.5f, 0f, 0f)
    });



    private final TractorModel model;

    public TractorRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.model = new TractorModel(context.bakeLayer(LAYER_LOCATION));
    }

    @Override
    public void render(Tractor tractor, float rotation, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        poseStack.pushPose();

        poseStack.scale(-1, -1, 1);
        poseStack.translate(0, -1.5, 0);

        poseStack.mulPose(Vector3f.YP.rotationDegrees(rotation - 180));
        this.model.prepareMobModel(tractor, 0, 0, partialTick);
        this.model.setupAnim(tractor, partialTick, 0, 0, 0, 0);

        VertexConsumer vertexConsumer = bufferSource.getBuffer(this.model.renderType(tractor.getTextureLocation()));
        this.model.renderToBuffer(poseStack, vertexConsumer, packedLight, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);

        poseStack.popPose();
        super.render(tractor, rotation, partialTick, poseStack, bufferSource, packedLight);
    }

    @Override
    @NotNull
    public ResourceLocation getTextureLocation(Tractor tractor) {
        return tractor.getTextureLocation();
    }
}
