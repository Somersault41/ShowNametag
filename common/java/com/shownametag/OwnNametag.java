package com.shownametag;

import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.LivingEntity;

/** Kendi isim etiketinin gosterilip gosterilmeyecegini belirler. */
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
