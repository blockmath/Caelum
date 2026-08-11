package nuparu.caelum.mixin;

import org.objectweb.asm.Opcodes;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.irisshaders.iris.uniforms.CelestialUniforms;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import nuparu.caelum.client.SkyUtils;
import nuparu.caelum.client.MoonController;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Mth;

@Pseudo
@Mixin(value = CelestialUniforms.class, remap = false)
public class IrisCelestialUniformsMixin {

	@ModifyExpressionValue(
		method = "getCelestialPosition",
		at = @At(
			value = "FIELD",
			target = "Lnet/irisshaders/iris/uniforms/CelestialUniforms;sunPathRotation:F",
			opcode = Opcodes.GETFIELD,
			remap = false
		),
		remap = false
	)
	private float injectCelestialLatitude(float value) {
		float new_value = value + (float) SkyUtils.sunLatitudeRotation(Minecraft.getInstance().level, Minecraft.getInstance().getCameraEntity().position().z()) * 180.0f;
		return new_value;
	}
	
	@ModifyExpressionValue(
		method = "getCelestialPositionInWorldSpace",
		at = @At(
			value = "FIELD",
			target = "Lnet/irisshaders/iris/uniforms/CelestialUniforms;sunPathRotation:F",
			opcode = Opcodes.GETFIELD,
			remap = false
		),
		remap = false
	)
	private float injectCelestialLatitudeWorld(float value) {
		float new_value = value + (float) SkyUtils.sunLatitudeRotation(Minecraft.getInstance().level, Minecraft.getInstance().getCameraEntity().position().z()) * 180.0f;
		return new_value;
	}

	@ModifyExpressionValue(
		method = "getCelestialPosition",
		at = @At(
			value = "INVOKE",
			target = "Lnet/irisshaders/iris/uniforms/CelestialUniforms;getSkyAngle()F",
			remap = false
		),
		remap = false
	)
	private float injectMoonAngle(float _value, @Local(argsOnly = true) float y) {
		if (y < 0) {
			long gametime = Minecraft.getInstance().level.getDayTime();
			float moon_mna = (float) MoonController.MOON.getMoonOrbitPosition(gametime);
			return (1.0f + Minecraft.getInstance().level.getTimeOfDay(1.0f) - moon_mna) % 1.0f;
		} else {
			return Minecraft.getInstance().level.getTimeOfDay(1.0f);
		}
	}

	@ModifyExpressionValue(
		method = "getCelestialPositionInWorldSpace",
		at = @At(
			value = "INVOKE",
			target = "Lnet/irisshaders/iris/uniforms/CelestialUniforms;getSkyAngle()F",
			remap = false
		),
		remap = false
	)
	private float injectMoonAngleWorld(float _value, @Local(argsOnly = true) float y) {
		if (y < 0) {
			long gametime = Minecraft.getInstance().level.getDayTime();
			float moon_mna = (float) MoonController.MOON.getMoonOrbitPosition(gametime);
			return (1.0f + Minecraft.getInstance().level.getTimeOfDay(1.0f) - moon_mna) % 1.0f;
		} else {
			return Minecraft.getInstance().level.getTimeOfDay(1.0f);
		}
	}
}
