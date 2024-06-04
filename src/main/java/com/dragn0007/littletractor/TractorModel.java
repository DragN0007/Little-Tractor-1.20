package com.dragn0007.littletractor;


import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class TractorModel extends EntityModel<Tractor> {
	private final ModelPart body;
	private final ModelPart frontWheels;
	private final ModelPart backWheels;
	private final ModelPart tiller;

	public TractorModel(ModelPart root) {
		this.body = root.getChild("Body");
		this.frontWheels = root.getChild("FrontWheels");
		this.backWheels = root.getChild("Backwheels");
		this.tiller = root.getChild("Tiller");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition Body = partdefinition.addOrReplaceChild("Body", CubeListBuilder.create().texOffs(0, 0).addBox(-17.9167F, 20.4909F, -30.5492F, 30.0F, 3.0F, 55.0F, new CubeDeformation(0.0F))
				.texOffs(0, 148).addBox(-13.9167F, 9.4909F, -0.5492F, 22.0F, 4.0F, 20.0F, new CubeDeformation(0.0F))
				.texOffs(0, 127).addBox(-12.9167F, -3.5091F, 15.3508F, 20.0F, 17.0F, 4.0F, new CubeDeformation(0.0F))
				.texOffs(176, 70).addBox(10.0833F, -0.6332F, 3.2931F, 4.0F, 2.0F, 12.0F, new CubeDeformation(0.0F))
				.texOffs(64, 148).addBox(-19.9167F, -0.6332F, 3.2931F, 4.0F, 2.0F, 12.0F, new CubeDeformation(0.0F))
				.texOffs(159, 167).addBox(-19.9167F, 9.3668F, -22.7069F, 34.0F, 2.0F, 9.0F, new CubeDeformation(0.0F))
				.texOffs(115, 0).addBox(-15.9167F, -17.5091F, -3.5492F, 26.0F, 2.0F, 29.0F, new CubeDeformation(0.0F))
				.texOffs(104, 58).addBox(-13.9167F, 3.4909F, -28.5492F, 22.0F, 17.0F, 28.0F, new CubeDeformation(0.0F))
				.texOffs(0, 0).addBox(-13.9167F, -15.5091F, 19.4508F, 22.0F, 36.0F, 4.0F, new CubeDeformation(0.0F))
				.texOffs(0, 58).addBox(8.0833F, -15.5091F, -1.5492F, 1.0F, 15.0F, 23.0F, new CubeDeformation(0.0F))
				.texOffs(0, 40).addBox(-13.9167F, -15.5091F, -0.5492F, 22.0F, 15.0F, 0.0F, new CubeDeformation(0.0F))
				.texOffs(0, 58).addBox(-14.9167F, -15.5091F, -1.5492F, 1.0F, 15.0F, 23.0F, new CubeDeformation(0.0F))
				.texOffs(52, 79).addBox(-15.9167F, -0.5091F, -26.5492F, 2.0F, 21.0F, 48.0F, new CubeDeformation(0.0F))
				.texOffs(0, 58).addBox(8.0833F, -0.5091F, -26.5492F, 2.0F, 21.0F, 48.0F, new CubeDeformation(0.0F))
				.texOffs(158, 216).addBox(8.0833F, -22.5091F, -0.7492F, 4.0F, 26.0F, 4.0F, new CubeDeformation(0.0F))
				.texOffs(44, 40).addBox(9.0833F, -33.5091F, 0.2508F, 2.0F, 11.0F, 2.0F, new CubeDeformation(0.0F))
				.texOffs(0, 58).addBox(9.0833F, 3.4909F, 0.2508F, 3.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
				.texOffs(123, 121).addBox(-13.9167F, -1.5091F, -29.5492F, 22.0F, 5.0F, 29.0F, new CubeDeformation(0.0F)), PartPose.offset(2.9167F, -5.4909F, 2.5492F));

		PartDefinition cube_r1 = Body.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(24, 96).addBox(13.0F, -2.5F, -16.0F, 4.0F, 2.0F, 8.0F, new CubeDeformation(0.0F))
				.texOffs(0, 96).addBox(-17.0F, -2.5F, -16.0F, 4.0F, 2.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.9167F, 11.9909F, 25.4508F, -0.7418F, 0.0F, 0.0F));

		PartDefinition cube_r2 = Body.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(0, 172).addBox(-17.0F, -2.5F, -16.0F, 34.0F, 2.0F, 9.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.9167F, 21.9909F, -3.5492F, -0.7418F, 0.0F, 0.0F));

		PartDefinition FrontWheels = partdefinition.addOrReplaceChild("FrontWheels", CubeListBuilder.create().texOffs(215, 82).addBox(13.0F, -8.5F, -3.5F, 6.0F, 17.0F, 7.0F, new CubeDeformation(0.0F))
				.texOffs(196, 121).addBox(-19.0F, -8.5F, -3.5F, 6.0F, 17.0F, 7.0F, new CubeDeformation(0.0F))
				.texOffs(115, 41).addBox(-20.0F, -2.5F, -2.5F, 40.0F, 5.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 15.5F, -15.5F));

		PartDefinition cube_r3 = FrontWheels.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(106, 210).addBox(-2.0F, -8.5F, -3.5F, 6.0F, 17.0F, 7.0F, new CubeDeformation(0.0F))
				.texOffs(104, 58).addBox(-34.0F, -8.5F, -3.5F, 6.0F, 17.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(15.0F, 0.0F, 0.0F, -0.7854F, 0.0F, 0.0F));

		PartDefinition cube_r4 = FrontWheels.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(132, 210).addBox(-2.0F, -8.5F, -3.5F, 6.0F, 17.0F, 7.0F, new CubeDeformation(0.0F))
				.texOffs(196, 0).addBox(-34.0F, -8.5F, -3.5F, 6.0F, 17.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(15.0F, 0.0F, 0.0F, -1.5708F, 0.0F, 0.0F));

		PartDefinition cube_r5 = FrontWheels.addOrReplaceChild("cube_r5", CubeListBuilder.create().texOffs(80, 210).addBox(-2.0F, -8.5F, -3.5F, 6.0F, 17.0F, 7.0F, new CubeDeformation(0.0F))
				.texOffs(115, 0).addBox(-34.0F, -8.5F, -3.5F, 6.0F, 17.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(15.0F, 0.0F, 0.0F, -2.3562F, 0.0F, 0.0F));

		PartDefinition Backwheels = partdefinition.addOrReplaceChild("Backwheels", CubeListBuilder.create().texOffs(195, 178).addBox(13.0F, -13.5F, -5.5F, 9.0F, 27.0F, 11.0F, new CubeDeformation(0.0F))
				.texOffs(52, 58).addBox(-22.0F, -13.5F, -5.5F, 9.0F, 27.0F, 11.0F, new CubeDeformation(0.0F))
				.texOffs(115, 31).addBox(-23.0F, -2.5F, -2.5F, 46.0F, 5.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 10.5F, 11.5F));

		PartDefinition cube_r6 = Backwheels.addOrReplaceChild("cube_r6", CubeListBuilder.create().texOffs(40, 199).addBox(-2.0F, -13.5F, -5.5F, 9.0F, 27.0F, 11.0F, new CubeDeformation(0.0F))
				.texOffs(115, 172).addBox(-37.0F, -13.5F, -5.5F, 9.0F, 27.0F, 11.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(15.0F, 0.0F, 0.0F, -1.5708F, 0.0F, 0.0F));

		PartDefinition cube_r7 = Backwheels.addOrReplaceChild("cube_r7", CubeListBuilder.create().texOffs(155, 178).addBox(-7.0F, -13.5F, -5.5F, 9.0F, 27.0F, 11.0F, new CubeDeformation(0.0F))
				.texOffs(198, 44).addBox(28.0F, -13.5F, -5.5F, 9.0F, 27.0F, 11.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-15.0F, 0.0F, 0.0F, -2.3562F, 0.0F, 0.0F));

		PartDefinition cube_r8 = Backwheels.addOrReplaceChild("cube_r8", CubeListBuilder.create().texOffs(75, 172).addBox(-7.0F, -13.5F, -5.5F, 9.0F, 27.0F, 11.0F, new CubeDeformation(0.0F))
				.texOffs(0, 183).addBox(28.0F, -13.5F, -5.5F, 9.0F, 27.0F, 11.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-15.0F, 0.0F, 0.0F, -0.7854F, 0.0F, 0.0F));

		PartDefinition Tiller = partdefinition.addOrReplaceChild("Tiller", CubeListBuilder.create().texOffs(104, 103).addBox(-24.0F, 1.8333F, 10.25F, 48.0F, 3.0F, 15.0F, new CubeDeformation(0.0F))
				.texOffs(154, 159).addBox(-23.0F, 4.8333F, 11.25F, 46.0F, 3.0F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(154, 159).addBox(-23.0F, 4.8333F, 23.25F, 46.0F, 3.0F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(154, 159).addBox(-23.0F, 4.8333F, 20.25F, 46.0F, 3.0F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(154, 159).addBox(-23.0F, 4.8333F, 14.25F, 46.0F, 3.0F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(84, 155).addBox(-14.0F, -0.1667F, 0.25F, 28.0F, 3.0F, 14.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 16.1667F, 18.75F));

		return LayerDefinition.create(meshdefinition, 256, 256);
	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		this.body.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		this.frontWheels.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		this.backWheels.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		this.tiller.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}

	@Override
	public void prepareMobModel(Tractor tractor, float p_102615_, float p_102616_, float partialTick) {
		Animation.animate(this.body, TractorRenderer.BODY_ANIMATION, tractor.driveTick, tractor.lastDrivePartialTick, tractor.forwardMotion);
		Animation.animate(this.frontWheels, TractorRenderer.FRONT_WHEEL_ANIMATION, tractor.driveTick, tractor.lastDrivePartialTick, tractor.forwardMotion);
		Animation.animate(this.backWheels, TractorRenderer.BACK_WHEEL_ANIMATION, tractor.driveTick, tractor.lastDrivePartialTick, tractor.forwardMotion);
		Animation.animate(this.tiller, TractorRenderer.TILLER_ANIMATION, tractor.driveTick, tractor.lastDrivePartialTick, tractor.forwardMotion);
	}

	@Override
	public void setupAnim(Tractor tractor, float partialTick, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		tractor.updateLastDrivePartialTick(partialTick);
		Animation.animate(this.body, TractorRenderer.BODY_ANIMATION, tractor.driveTick, tractor.lastDrivePartialTick, tractor.forwardMotion);
		Animation.animate(this.frontWheels, TractorRenderer.FRONT_WHEEL_ANIMATION, tractor.driveTick, tractor.lastDrivePartialTick, tractor.forwardMotion);
		Animation.animate(this.backWheels, TractorRenderer.BACK_WHEEL_ANIMATION, tractor.driveTick, tractor.lastDrivePartialTick, tractor.forwardMotion);
		Animation.animate(this.tiller, TractorRenderer.TILLER_ANIMATION, tractor.driveTick, tractor.lastDrivePartialTick, tractor.forwardMotion);

		this.frontWheels.yRot = tractor.getFrontWheelRotation(partialTick);
	}
}