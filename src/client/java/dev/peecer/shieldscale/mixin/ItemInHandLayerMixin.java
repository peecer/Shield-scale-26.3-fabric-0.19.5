package dev.peecer.shieldscale.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.peecer.shieldscale.ShieldScaleTransform;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.client.renderer.entity.state.ArmedEntityRenderState;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemInHandLayer.class)
public abstract class ItemInHandLayerMixin {
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
            ArmedEntityRenderState state,
            ItemStackRenderState itemState,
            ItemStack itemStack,
            HumanoidArm arm,
            PoseStack poseStack,
            SubmitNodeCollector collector,
            int lightCoords,
            CallbackInfo ci) {
        customShieldScale$posePushed = false;

        if (!ShieldScaleTransform.shouldTransform(itemStack)) {
            return;
        }

        Minecraft minecraft = Minecraft.getInstance();
        boolean self = minecraft.player != null
                && state instanceof AvatarRenderState avatar
                && avatar.id == minecraft.player.getId();

        ShieldScaleTransform.push(poseStack, self);
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
            ArmedEntityRenderState state,
            ItemStackRenderState itemState,
            ItemStack itemStack,
            HumanoidArm arm,
            PoseStack poseStack,
            SubmitNodeCollector collector,
            int lightCoords,
            CallbackInfo ci) {
        if (customShieldScale$posePushed) {
            poseStack.popPose();
            customShieldScale$posePushed = false;
        }
    }
}
