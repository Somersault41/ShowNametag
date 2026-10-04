package com.shownametag;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.LivingEntity;

/**
 * Kendi isim etiketinin gosterilip gosterilmeyecegini belirler.
 * F1 (arayuzu gizle) kontrolu surumler arasinda degistigi icin calisma aninda bulunur:
 *  - 26.1.x : static Minecraft.renderNames()
 *  - 26.2+  : Minecraft.gui.hud.isHidden()
 */
public final class OwnNametag {
	private static final Method RENDER_NAMES = find();
	private static final Field GUI = field(Minecraft.class, "gui");
	private static final Field HUD = GUI == null ? null : field(GUI.getType(), "hud");
	private static final Method IS_HIDDEN = HUD == null ? null : method(HUD.getType(), "isHidden");

	private OwnNametag() {
	}

	public static boolean shouldShow(LivingEntity entity) {
		Minecraft mc = Minecraft.getInstance();
		return entity == mc.player
			&& !mc.options.getCameraType().isFirstPerson()
			&& !entity.isInvisible()
			&& !guiHidden(mc);
	}

	private static boolean guiHidden(Minecraft mc) {
		try {
			if (RENDER_NAMES != null) {
				return !(Boolean) RENDER_NAMES.invoke(null);
			}
			if (GUI != null && HUD != null && IS_HIDDEN != null) {
				return (Boolean) IS_HIDDEN.invoke(HUD.get(GUI.get(mc)));
			}
		} catch (ReflectiveOperationException | RuntimeException ignored) {
		}
		return false;
	}

	private static Method find() {
		return method(Minecraft.class, "renderNames");
	}

	private static Method method(Class<?> c, String name) {
		try {
			return c.getMethod(name);
		} catch (ReflectiveOperationException e) {
			return null;
		}
	}

	private static Field field(Class<?> c, String name) {
		try {
			return c.getField(name);
		} catch (ReflectiveOperationException e) {
			return null;
		}
	}
}
