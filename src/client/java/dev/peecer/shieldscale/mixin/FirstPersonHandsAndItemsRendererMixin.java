package dev.peecer.shieldscale.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.peecer.shieldscale.ShieldScaleTransform;
import net.minecraft.client.renderer.FirstPersonHandsAndItemsRenderer;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.state.level.FirstPersonHandsAndItemsRenderState;
import net.minecraft.client.renderer.state.level.PlayerRenderState;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(FirstPersonHandsAndItemsRenderer.class)
public abstract class FirstPersonHandsAndItemsRendererMixin {
    @Unique
    private boolean customShieldScale$posePushed;

    @Inject(
            method = "submitArmWithItem",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/renderer/item/ItemStackRenderState;submit(" +
                            "Lcom/mojang/blaze3d/vertex/PoseStack;" +
                            "Lnet/minecraft/client/renderer/SubmitNodeCollector;III)V",
                    shift = At.Shift.BEFORE
            )
    )
    private void customShieldScale$beforeShieldSubmit(
            PlayerRenderState playerState,
            FirstPersonHandsAndItemsRenderState state,
            float partialTicks,
            float xRot,
            InteractionHand hand,
            float attack,
            ItemStack itemStack,
            float inverseArmHeight,
            PoseStack poseStack,
            SubmitNodeCollector submitNodeCollector,
            int lightCoords,
            CallbackInfo ci) {
        customShieldScale$posePushed = false;

        if (!ShieldScaleTransform.shouldTransform(itemStack)) {
            return;
        }

        ShieldScaleTransform.push(poseStack, true);
        customShieldScale$posePushed = true;
    }

    @Inject(
            method = "submitArmWithItem",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/renderer/item/ItemStackRenderState;submit(" +
                            "Lcom/mojang/blaze3d/vertex/PoseStack;" +
                            "Lnet/minecraft/client/renderer/SubmitNodeCollector;III)V",
                    shift = At.Shift.AFTER
            )
    )
    private void customShieldScale$afterShieldSubmit(
            PlayerRenderState playerState,
            FirstPersonHandsAndItemsRenderState state,
            float partialTicks,
            float xRot,
            InteractionHand hand,
            float attack,
            ItemStack itemStack,
            float inverseArmHeight,
            PoseStack poseStack,
            SubmitNodeCollector submitNodeCollector,
            int lightCoords,
            CallbackInfo ci) {
        if (customShieldScale$posePushed) {
            poseStack.popPose();
            customShieldScale$posePushed = false;
        }
    }
}
