package dev.peecer.shieldscale;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public final class ShieldScaleTransform {
    private ShieldScaleTransform() {
    }

    public static boolean shouldTransform(ItemStack stack) {
        return ShieldScaleConfig.get().enabled && stack.is(Items.SHIELD);
    }

    public static void push(PoseStack poseStack, boolean self) {
        ShieldScaleConfig config = ShieldScaleConfig.get();

        double scale = self ? config.selfScale : config.othersScale;
        double offsetX = self ? config.selfOffsetX : config.othersOffsetX;
        double offsetY = self ? config.selfOffsetY : config.othersOffsetY;

        poseStack.pushPose();
        poseStack.translate(offsetX, offsetY, 0.0);
        poseStack.scale((float) scale, (float) scale, (float) scale);
    }
}
