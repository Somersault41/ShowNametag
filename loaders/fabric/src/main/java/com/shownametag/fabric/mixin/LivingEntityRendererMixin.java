package com.shownametag.fabric.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.shownametag.fabric.OwnNametag;

import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.world.entity.LivingEntity;

/**
 * Target written directly with intermediary names (remap = false):
 * LivingEntityRenderer.shouldShowName = method_4055, LivingEntity = class_1309 (same in 1.20 - 1.20.6).
 */
@Mixin(LivingEntityRenderer.class)
public abstract class LivingEntityRendererMixin {
	@Inject(method = "method_4055(Lnet/minecraft/class_1309;)Z", at = @At("HEAD"), cancellable = true, remap = false)
	private void shownametag$showOwnName(LivingEntity entity, CallbackInfoReturnable<Boolean> cir) {
		if (OwnNametag.shouldShow(entity)) {
			cir.setReturnValue(true);
		}
	}
}
