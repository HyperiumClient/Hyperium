package cc.hyperium.handlers.handlers;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import cc.hyperium.event.InvokeEvent;
import cc.hyperium.event.RenderEvent;
import cc.hyperium.gui.HyperiumGui;
import cc.hyperium.gui.settings.items.AnimationSettings;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelPlayer;

public class FlossDanceHandler {

	private ConcurrentHashMap<UUID, DanceState> danceStates = new ConcurrentHashMap<>();
	private float state = 0;
	private boolean right = true;
	private boolean asc = true;
	private ArmsDirection armsDirection = ArmsDirection.HORIZONTAL;
	// x, y, z values
	private final Random random = new Random();
	private float[] randomHeadMovement = new float[3];

	public FlossDanceHandler() {
		fillRandomHeadMovementArray();
	}

	enum ArmsDirection {
		HORIZONTAL, BACK, FRONT
	}

	@InvokeEvent
	public void onRender(RenderEvent e) {
		danceStates.values().forEach(DanceState::update);

		float speed = AnimationSettings.flossDanceSpeed * 3;
		state = HyperiumGui.clamp(state + (asc ? speed : -speed), 0.0f, 100.0f);

		if (state <= 0) {
			asc = true;
			right = !right;
			armsDirection = ArmsDirection.values()[armsDirection.ordinal() + 1 >= ArmsDirection.values().length ? 0 : armsDirection.ordinal() + 1];
			fillRandomHeadMovementArray();
		} else if (state >= 100) {
			asc = false;
		}
	}

	private void fillRandomHeadMovementArray() {
		randomHeadMovement[0] = (2.0f * random.nextFloat() - 1.0f) * 15.0f;
		randomHeadMovement[1] = (2.0f * random.nextFloat() - 1.0f) * 15.0f;
		randomHeadMovement[2] = (2.0f * random.nextFloat() - 1.0f) * 15.0f;
	}

	public DanceState get(UUID uuid) {
		return danceStates.computeIfAbsent(uuid, DanceState::new);
	}

	public void startDacing(UUID uuid) {
		get(uuid).ensureDancingFor(60);
	}

	public void stopDancing(UUID uuid) {
		get(uuid).setToggled(false);
		get(uuid).stopDancing();
	}

	public void modify(AbstractClientPlayer entity, ModelPlayer player) {
		int ticks = get(entity.getUniqueID()).danceFrames;

		if (ticks <= 0)
			return;

		float heldPercent = state / 100F;

		player.bipedBody.rotateAngleZ = (float) Math.toRadians((right ? 10f : -10f) * heldPercent);
		player.bipedBodyWear.rotateAngleZ = (float) Math.toRadians((right ? 10f : -10f) * heldPercent);

		player.bipedRightLeg.rotateAngleZ = (float) Math.toRadians((right ? -10f : 10f) * heldPercent);
		player.bipedRightLegwear.rotateAngleZ = (float) Math.toRadians((right ? -10f : 10f) * heldPercent);
		player.bipedLeftLeg.rotateAngleZ = (float) Math.toRadians((right ? -10f : 10f) * heldPercent);
		player.bipedLeftLegwear.rotateAngleZ = (float) Math.toRadians((right ? -10f : 10f) * heldPercent);
		player.bipedRightLeg.offsetX = (right ? -0.17f : 0.17f) * heldPercent;
		player.bipedRightLegwear.offsetX = (right ? -0.17f : 0.17f) * heldPercent;
		player.bipedLeftLeg.offsetX = (right ? -0.17f : 0.17f) * heldPercent;
		player.bipedLeftLegwear.offsetX = (right ? -0.17f : 0.17f) * heldPercent;

		player.bipedHead.rotateAngleX = (float) Math.toRadians(randomHeadMovement[0] * heldPercent);
		player.bipedHeadwear.rotateAngleX = (float) Math.toRadians(randomHeadMovement[0] * heldPercent);
		player.bipedHead.rotateAngleY = (float) Math.toRadians(randomHeadMovement[1] * heldPercent);
		player.bipedHeadwear.rotateAngleY = (float) Math.toRadians(randomHeadMovement[1] * heldPercent);
		player.bipedHead.rotateAngleZ = (float) Math.toRadians(randomHeadMovement[2] * heldPercent);
		player.bipedHeadwear.rotateAngleZ = (float) Math.toRadians(randomHeadMovement[2] * heldPercent);

		switch (armsDirection) {
			case HORIZONTAL:
			case FRONT:
				player.bipedRightArm.rotateAngleZ = (float) Math.toRadians((right ? -50f : 50f) * heldPercent);
				player.bipedRightArmwear.rotateAngleZ = (float) Math.toRadians((right ? -50f : 50f)  * heldPercent);
				player.bipedRightArm.rotateAngleX = (float) Math.toRadians(-30f * heldPercent);
				player.bipedRightArmwear.rotateAngleX = (float) Math.toRadians(-30f * heldPercent);

				player.bipedLeftArm.rotateAngleZ = (float) Math.toRadians((right ? -50f : 50f)  * heldPercent);
				player.bipedLeftArmwear.rotateAngleZ = (float) Math.toRadians((right ? -50f : 50f)  * heldPercent);
				player.bipedLeftArm.rotateAngleX = (float) Math.toRadians(-30f * heldPercent);
				player.bipedLeftArmwear.rotateAngleX = (float) Math.toRadians(-30f * heldPercent);
				break;
			case BACK:
				player.bipedRightArm.rotateAngleZ = (float) Math.toRadians((right ? -50f : 50f) * heldPercent);
				player.bipedRightArmwear.rotateAngleZ = (float) Math.toRadians((right ? -50f : 50f)  * heldPercent);
				player.bipedRightArm.rotateAngleX = (float) Math.toRadians(30f * heldPercent);
				player.bipedRightArmwear.rotateAngleX = (float) Math.toRadians(30f * heldPercent);

				player.bipedLeftArm.rotateAngleZ = (float) Math.toRadians((right ? -50f : 50f)  * heldPercent);
				player.bipedLeftArmwear.rotateAngleZ = (float) Math.toRadians((right ? -50f : 50f)  * heldPercent);
				player.bipedLeftArm.rotateAngleX = (float) Math.toRadians(30f * heldPercent);
				player.bipedLeftArmwear.rotateAngleX = (float) Math.toRadians(30f * heldPercent);
				break;
			default:
				break;
		}
	}
	public void modify(AbstractClientPlayer entity, ModelBiped player) {
		int ticks = get(entity.getUniqueID()).danceFrames;

		if (ticks <= 0)
			return;

		float heldPercent = state / 100F;

		player.bipedBody.rotateAngleZ = (float) Math.toRadians((right ? 10f : -10f) * heldPercent);

		player.bipedRightLeg.rotateAngleZ = (float) Math.toRadians((right ? -10f : 10f) * heldPercent);
		player.bipedLeftLeg.rotateAngleZ = (float) Math.toRadians((right ? -10f : 10f) * heldPercent);
		player.bipedRightLeg.offsetX = (right ? -0.17f : 0.17f) * heldPercent;
		player.bipedLeftLeg.offsetX = (right ? -0.17f : 0.17f) * heldPercent;

		player.bipedHead.rotateAngleX = (float) Math.toRadians(randomHeadMovement[0] * heldPercent);
		player.bipedHeadwear.rotateAngleX = (float) Math.toRadians(randomHeadMovement[0] * heldPercent);
		player.bipedHead.rotateAngleY = (float) Math.toRadians(randomHeadMovement[1] * heldPercent);
		player.bipedHeadwear.rotateAngleY = (float) Math.toRadians(randomHeadMovement[1] * heldPercent);
		player.bipedHead.rotateAngleZ = (float) Math.toRadians(randomHeadMovement[2] * heldPercent);
		player.bipedHeadwear.rotateAngleZ = (float) Math.toRadians(randomHeadMovement[2] * heldPercent);

		switch (armsDirection) {
			case HORIZONTAL:
			case FRONT:
				player.bipedRightArm.rotateAngleZ = (float) Math.toRadians((right ? -50f : 50f) * heldPercent);
				player.bipedRightArm.rotateAngleX = (float) Math.toRadians(-30f * heldPercent);

				player.bipedLeftArm.rotateAngleZ = (float) Math.toRadians((right ? -50f : 50f)  * heldPercent);
				player.bipedLeftArm.rotateAngleX = (float) Math.toRadians(-30f * heldPercent);
				break;
			case BACK:
				player.bipedRightArm.rotateAngleZ = (float) Math.toRadians((right ? -50f : 50f) * heldPercent);
				player.bipedRightArm.rotateAngleX = (float) Math.toRadians(30f * heldPercent);

				player.bipedLeftArm.rotateAngleZ = (float) Math.toRadians((right ? -50f : 50f)  * heldPercent);
				player.bipedLeftArm.rotateAngleX = (float) Math.toRadians(30f * heldPercent);
				break;
		}

	}

	public class DanceState {
		private UUID uuid;
		private int danceFrames = 0;
		private long systemTime;
		private boolean toggled;

		public DanceState(UUID uuid) {
			this.uuid = uuid;
			this.systemTime = Minecraft.getSystemTime();
			this.toggled = false;
		}

		private void update() {
			while (this.systemTime < Minecraft.getSystemTime() + (1000 / 60)) {
				this.danceFrames--;
				this.systemTime += (1000 / 60);
			}

			if (danceFrames <= 0) {
				if (this.toggled) {
					ensureDancingFor(60);
				} else {
					danceFrames = 0;
				}
			}
		}

		public void ensureDancingFor(int seconds) {
			danceFrames = Math.max(danceFrames, seconds * 60);
		}

		public void stopDancing() {
			danceFrames = 0;
		}

		public boolean isDancing() {
			return danceFrames > 0;
		}

		public void setToggled(boolean toggled) {
			this.toggled = toggled;
		}
	}
}
