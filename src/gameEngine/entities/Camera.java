package gameEngine.entities;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;

import gameEngine.mainGameLoop.Main;
import gameEngine.math.Vector3f;

public class Camera {
	private Vector3f position = new Vector3f(0, 3, 5);
	private float pitch;
	private float yaw;
	private float roll;

	private final static float MOUSE_SENSITIVITY = 0.12f;
	private final static float SPEED_CAP = 0.1f;
	private boolean mouse_pos = true;

	public Camera() {
		pitch = 0;
		yaw = 0;
		roll = 0;
	}

	public void move() {
		if (Keyboard.isKeyDown(Keyboard.KEY_W)) {
			position.Zpos -= SPEED_CAP * Math.cos(Math.toRadians(yaw)) * Main.getFrameTime();
			position.Xpos += SPEED_CAP * Math.sin(Math.toRadians(-yaw)) * Main.getFrameTime();
		} else if (Keyboard.isKeyDown(Keyboard.KEY_S)) {
			position.Zpos += SPEED_CAP * Math.cos(Math.toRadians(yaw)) * Main.getFrameTime();
			position.Xpos -= SPEED_CAP * Math.sin(Math.toRadians(-yaw)) * Main.getFrameTime();
		}

		if (Keyboard.isKeyDown(Keyboard.KEY_D)) {
			position.Zpos += SPEED_CAP * Math.cos(Math.toRadians(90 + yaw)) * Main.getFrameTime();
			position.Xpos += SPEED_CAP * Math.sin(Math.toRadians(90 - yaw)) * Main.getFrameTime();
		} else if (Keyboard.isKeyDown(Keyboard.KEY_A)) {
			position.Zpos -= SPEED_CAP * Math.cos(Math.toRadians(90 + yaw)) * Main.getFrameTime();
			position.Xpos -= SPEED_CAP * Math.sin(Math.toRadians(90 - yaw)) * Main.getFrameTime();
		}

		if (Keyboard.isKeyDown(Keyboard.KEY_SPACE)) {
			position.Ypos += SPEED_CAP * Main.getFrameTime();
		}

		else if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
			position.Ypos -= SPEED_CAP * Main.getFrameTime();
		}

		if (mouse_pos == true) {
			Mouse.setCursorPosition(Display.getWidth() / 2, Display.getHeight() / 2);
			Mouse.setGrabbed(true);
			mouse_pos = false;
		}

		if (Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) {
			Mouse.setGrabbed(false);
		}

		pitch += Mouse.getDY() * MOUSE_SENSITIVITY * Main.getFrameTime();
		yaw += -Mouse.getDX() * MOUSE_SENSITIVITY * Main.getFrameTime();
		

		if (pitch > 90) {
			pitch = 90;
		} else if (pitch < -90) {
			pitch = -90;
		}
	}

	public Vector3f getPosition() {
		return position;
	}

	public void setPosition(Vector3f position) {
		this.position.Xpos = position.Xpos;
		this.position.Ypos = position.Ypos;
		this.position.Zpos = position.Zpos;
	}

	public float getPitch() {
		return pitch;
	}

	public void setPitch(float pitch) {
		this.pitch = pitch;
	}

	public float getYaw() {
		return yaw;
	}

	public void setYaw(float yaw) {
		this.yaw = yaw;
	}

	public float getRoll() {
		return roll;
	}

	public void setRoll(float roll) {
		this.roll = roll;
	}
}
