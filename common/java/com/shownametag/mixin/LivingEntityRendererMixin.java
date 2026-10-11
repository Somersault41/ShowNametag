package com.shownametag.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.shownametag.OwnNametag;
import com.shownametag.OwnNametagSrg;

import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.world.entity.LivingEntity;

/**
 * Targets are written as runtime names (remap = false), so no refmap is needed.
 * Only one of the two hooks matches in a given game; the other is skipped (require = 0)
 * and its helper class is never loaded.
 */
@Mixin(LivingEntityRenderer.class)
public abstract class LivingEntityRendererMixin {
	// Official names: Forge 1.20.6, NeoForge 20.2 - 20.6
	@Inject(method = "shouldShowName(Lnet/minecraft/world/entity/LivingEntity;)Z", at = @At("HEAD"), cancellable = true, require = 0, remap = false)
	private void shownametag$showOwnName(LivingEntity entity, CallbackInfoReturnable<Boolean> cir) {
		if (OwnNametag.shouldShow(entity)) {
			cir.setReturnValue(true);
		}
	}

	// SRG names: Forge 1.20 - 1.20.4, NeoForge 1.20.1 (shouldShowName = m_6512_ in all of them)
	@Inject(method = "m_6512_(Lnet/minecraft/world/entity/LivingEntity;)Z", at = @At("HEAD"), cancellable = true, require = 0, remap = false)
	private void shownametag$showOwnNameSrg(LivingEntity entity, CallbackInfoReturnable<Boolean> cir) {
		if (OwnNametagSrg.shouldShow(entity)) {
			cir.setReturnValue(true);
		}
	}
}
