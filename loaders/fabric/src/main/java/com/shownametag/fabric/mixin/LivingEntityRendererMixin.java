package com.shownametag.fabric.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.shownametag.fabric.OwnNametag;

import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.world.entity.LivingEntity;

/**
 * Hedefler dogrudan intermediary isimleriyle yazildi (remap = false):
 * LivingEntityRenderer.shouldShowName = method_4055, LivingEntity = class_1309 (1.21 - 1.21.11 ayni).
 */
@Mixin(LivingEntityRenderer.class)
public abstract class LivingEntityRendererMixin {
	// 1.21.2 - 1.21.11
	@Inject(method = "method_4055(Lnet/minecraft/class_1309;D)Z", at = @At("HEAD"), cancellable = true, require = 0, remap = false)
	private void shownametag$showOwnName(LivingEntity entity, double distance, CallbackInfoReturnable<Boolean> cir) {
		if (OwnNametag.shouldShow(entity)) {
			cir.setReturnValue(true);
		}
	}

	// 1.21 - 1.21.1
	@Inject(method = "method_4055(Lnet/minecraft/class_1309;)Z", at = @At("HEAD"), cancellable = true, require = 0, remap = false)
	private void shownametag$showOwnNameOld(LivingEntity entity, CallbackInfoReturnable<Boolean> cir) {
		if (OwnNametag.shouldShow(entity)) {
			cir.setReturnValue(true);
		}
	}
}
