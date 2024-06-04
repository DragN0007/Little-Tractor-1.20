package com.dragn0007.littletractor;

import net.minecraft.client.model.geom.ModelPart;

import static com.dragn0007.littletractor.LittleTractorMain.mod;

public final class Animation {

    public record Vec3f(float x, float y, float z) {}

    static class KeyFrame {
        public final float time;
        public final Vec3f rot;

        KeyFrame(float time, float x, float y, float z) {
            this.time = time * 20;
            this.rot = new Vec3f(x * (float)Math.PI / 180f, y * (float)Math.PI / 180f, z * (float)Math.PI / 180f);
        }
    }

    public final KeyFrame[] keyFrames;
    public final float length;

    public Animation(float length, KeyFrame[] keyFrames) {
        this.length = length * 20;
        this.keyFrames = keyFrames;
    }

    public static void animate(ModelPart modelPart, Animation animation, float tick, float partialTick, float speed) {
        float t = mod((tick + partialTick) * speed, animation.length);
        int i = 0;

        for(; i < animation.keyFrames.length; i++) {
            if(t < animation.keyFrames[i].time) {
                break;
            }
        }

        int idx1 = (int) mod(i - 1, animation.keyFrames.length);
        int idx2 = i % animation.keyFrames.length;

        KeyFrame k1 = animation.keyFrames[Math.min(idx1, idx2)];
        KeyFrame k2 = animation.keyFrames[Math.max(idx1, idx2)];

        float f = (t - k1.time) / (k2.time - k1.time);

        modelPart.xRot = k1.rot.x + (k2.rot.x - k1.rot.x) * f;
        modelPart.yRot = k1.rot.y + (k2.rot.y - k1.rot.y) * f;
        modelPart.zRot = k1.rot.z + (k2.rot.z - k1.rot.z) * f;
    }
}
