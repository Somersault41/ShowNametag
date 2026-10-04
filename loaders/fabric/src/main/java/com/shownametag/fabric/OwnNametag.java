package com.shownametag.fabric;

import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.LivingEntity;

/** Fabric: derlemede intermediary isimlere cevrilir. */
public final class OwnNametag {
	private OwnNametag() {
	}

	public static boolean shouldShow(LivingEntity entity) {
		Minecraft mc = Minecraft.getInstance();
		return entity == mc.player
			&& !mc.options.getCameraType().isFirstPerson()
			&& !entity.isInvisible()
			&& !mc.options.hideGui;
	}
}
