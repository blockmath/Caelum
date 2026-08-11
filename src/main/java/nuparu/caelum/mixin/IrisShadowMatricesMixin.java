package nuparu.caelum.mixin;

import net.irisshaders.iris.shadows.ShadowMatrices;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import nuparu.caelum.client.SkyUtils;
import net.minecraft.client.Minecraft;
import nuparu.caelum.client.MoonController;
import nuparu.caelum.config.ClientConfig;

@Pseudo
@Mixin(value = ShadowMatrices.class, remap = false)
public class IrisShadowMatricesMixin {
	@ModifyVariable(
		method = "createBaselineModelViewMatrix",
		at = @At("HEAD"),
		ordinal = 1,
		remap = false
	)
	private static float latitudeInject(float value) {
		float latitude = (float) SkyUtils.sunLatitudeRotation(Minecraft.getInstance().level, Minecraft.getInstance().getCameraEntity().position().z()) * 180.0f;
		return value + latitude;
	}

	@ModifyVariable(
		method = "createBaselineModelViewMatrix",
		at = @At("HEAD"),
		ordinal = 0,
		remap = false
	)
	private static float moonOrbitInject(float value) {
		float daytime = Minecraft.getInstance().level.getTimeOfDay(1.0f);
		if (ClientConfig.accurateMoonShadow.get() && 0.25f < daytime && daytime < 0.75f) {
			long gametime = Minecraft.getInstance().level.getDayTime();
			float moon_mna = (float) MoonController.MOON.getMoonOrbitPosition(gametime);
			return value - moon_mna;
		} else {
			return value;
		}
	}
}
