package com.shownametag;

import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.LivingEntity;

/**
 * Same logic as {@link OwnNametag}, for loaders that run Minecraft with SRG names
 * (Forge 1.20 - 1.20.4, NeoForge 1.20.1). The released jar takes this class from the
 * Forge build's reobfJar output, so its Minecraft references are SRG names there.
 */
public final class OwnNametagSrg {
	private OwnNametagSrg() {
	}

	public static boolean shouldShow(LivingEntity entity) {
		Minecraft mc = Minecraft.getInstance();
		return entity == mc.player
			&& !mc.options.getCameraType().isFirstPerson()
			&& !entity.isInvisible()
			&& !mc.options.hideGui;
	}
}
