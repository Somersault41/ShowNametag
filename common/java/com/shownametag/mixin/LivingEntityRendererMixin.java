package com.shownametag.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.shownametag.OwnNametag;

import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.world.entity.LivingEntity;

@Mixin(LivingEntityRenderer.class)
public abstract class LivingEntityRendererMixin {
	// 1.21.2+ ve 26.x
	@Inject(method = "shouldShowName(Lnet/minecraft/world/entity/LivingEntity;D)Z", at = @At("HEAD"), cancellable = true, require = 0)
	private void shownametag$showOwnName(LivingEntity entity, double distance, CallbackInfoReturnable<Boolean> cir) {
		if (OwnNametag.shouldShow(entity)) {
			cir.setReturnValue(true);
		}
	}

	// 1.21 - 1.21.1 (Forge/NeoForge)
	@Inject(method = "shouldShowName(Lnet/minecraft/world/entity/LivingEntity;)Z", at = @At("HEAD"), cancellable = true, require = 0)
	private void shownametag$showOwnNameOld(LivingEntity entity, CallbackInfoReturnable<Boolean> cir) {
		if (OwnNametag.shouldShow(entity)) {
			cir.setReturnValue(true);
		}
	}
}
