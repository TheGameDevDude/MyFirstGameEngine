package gameEngine.collision;

import java.io.Serializable;

import org.lwjgl.input.Keyboard;

import gameEngine.mainGameLoop.Main;
import gameEngine.math.Vector3f;

public class AABB implements Serializable {
	private static final long serialVersionUID = 1L;
	public Vector3f farLeft;
	public Vector3f nearRight;
	public float x;
	public float y;
	public float z;
	private final static float SPEED = 0.1f;

	public boolean selected = false;

	public AABB(Vector3f farLeft, Vector3f nearRight) {
		this.farLeft = farLeft;
		this.nearRight = nearRight;
		x = Math.abs((nearRight.Xpos - farLeft.Xpos) / 2);
		y = Math.abs((nearRight.Ypos - farLeft.Ypos) / 2);
		z = Math.abs((nearRight.Zpos - farLeft.Zpos) / 2);

	}

	public boolean check(AABB aabb) {
		Vector3f thisaabb = getCenter();
		Vector3f otheraabb = aabb.getCenter();
		float Xcenter = Math.abs(thisaabb.Xpos - otheraabb.Xpos);
		float Ycenter = Math.abs(thisaabb.Ypos - otheraabb.Ypos);
		float Zcenter = Math.abs(thisaabb.Zpos - otheraabb.Zpos);
		if (checkX(Xcenter, aabb) && checkY(Ycenter, aabb) && checkZ(Zcenter, aabb)) {
			return true;
		}

		return false;
	}

	float m = 0.0f;

	public void edit(int chunkX, int chunkZ, AABBManager aabbManager) {
		if (Keyboard.isKeyDown(Keyboard.KEY_Q)) {
			if (Keyboard.isKeyDown(Keyboard.KEY_I)) {
				farLeft.Zpos -= SPEED * Main.getFrameTime();
			} else if (Keyboard.isKeyDown(Keyboard.KEY_K)) {
				farLeft.Zpos += SPEED * Main.getFrameTime();
			}

			if (Keyboard.isKeyDown(Keyboard.KEY_L)) {
				farLeft.Xpos += SPEED * Main.getFrameTime();
			} else if (Keyboard.isKeyDown(Keyboard.KEY_J)) {
				farLeft.Xpos -= SPEED * Main.getFrameTime();
			}

			if (Keyboard.isKeyDown(Keyboard.KEY_U)) {
				farLeft.Ypos += SPEED * Main.getFrameTime();
			} else if (Keyboard.isKeyDown(Keyboard.KEY_O)) {
				farLeft.Ypos -= SPEED * Main.getFrameTime();
			}
		} else if (Keyboard.isKeyDown(Keyboard.KEY_E)) {
			if (Keyboard.isKeyDown(Keyboard.KEY_I)) {
				nearRight.Zpos -= SPEED * Main.getFrameTime();
			} else if (Keyboard.isKeyDown(Keyboard.KEY_K)) {
				nearRight.Zpos += SPEED * Main.getFrameTime();
			}

			if (Keyboard.isKeyDown(Keyboard.KEY_L)) {
				nearRight.Xpos += SPEED * Main.getFrameTime();
			} else if (Keyboard.isKeyDown(Keyboard.KEY_J)) {
				nearRight.Xpos -= SPEED * Main.getFrameTime();
			}

			if (Keyboard.isKeyDown(Keyboard.KEY_U)) {
				nearRight.Ypos += SPEED * Main.getFrameTime();
			} else if (Keyboard.isKeyDown(Keyboard.KEY_O)) {
				nearRight.Ypos -= SPEED * Main.getFrameTime();
			}
		} else {
			if (Keyboard.isKeyDown(Keyboard.KEY_I)) {
				nearRight.Zpos -= SPEED * Main.getFrameTime();
				farLeft.Zpos -= SPEED * Main.getFrameTime();
			} else if (Keyboard.isKeyDown(Keyboard.KEY_K)) {
				nearRight.Zpos += SPEED * Main.getFrameTime();
				farLeft.Zpos += SPEED * Main.getFrameTime();
			}

			if (Keyboard.isKeyDown(Keyboard.KEY_J)) {
				nearRight.Xpos -= SPEED * Main.getFrameTime();
				farLeft.Xpos -= SPEED * Main.getFrameTime();
			} else if (Keyboard.isKeyDown(Keyboard.KEY_L)) {
				nearRight.Xpos += SPEED * Main.getFrameTime();
				farLeft.Xpos += SPEED * Main.getFrameTime();
			}

			if (Keyboard.isKeyDown(Keyboard.KEY_U)) {
				nearRight.Ypos += SPEED * Main.getFrameTime();
				farLeft.Ypos += SPEED * Main.getFrameTime();
			} else if (Keyboard.isKeyDown(Keyboard.KEY_O)) {
				nearRight.Ypos -= SPEED * Main.getFrameTime();
				farLeft.Ypos -= SPEED * Main.getFrameTime();
			}
		}

//		Vector2f topLeft = new Vector2f(chunkX * aabbManager.getChunkSize(), chunkZ * aabbManager.getChunkSize());
//		Vector2f bottomRight = new Vector2f((chunkX + 1) * aabbManager.getChunkSize(),
//				(chunkZ + 1) * aabbManager.getChunkSize());

//		if (farLeft.Xpos <= topLeft.Xpos) {
//			farLeft.Xpos = topLeft.Xpos + 0.001f;
//		} else if (farLeft.Xpos >= bottomRight.Xpos) {
//			farLeft.Xpos = bottomRight.Xpos - 0.001f;
//		}
//		if (farLeft.Zpos <= topLeft.Ypos) {
//			farLeft.Zpos = topLeft.Ypos + 0.001f;
//		} else if (farLeft.Zpos >= bottomRight.Ypos) {
//			farLeft.Zpos = bottomRight.Ypos - 0.001f;
//		}
//
//		if (nearRight.Xpos <= topLeft.Xpos) {
//			nearRight.Xpos = topLeft.Xpos + 0.001f;
//		} else if (nearRight.Xpos >= bottomRight.Xpos) {
//			nearRight.Xpos = bottomRight.Xpos - 0.001f;
//		}
//		if (nearRight.Zpos <= topLeft.Ypos) {
//			nearRight.Zpos = topLeft.Ypos + 0.001f;
//		} else if (nearRight.Zpos >= bottomRight.Ypos) {
//			nearRight.Zpos = bottomRight.Ypos - 0.001f;
//		}
	}

	private boolean checkX(float Xcenter, AABB aabb) {
		if (Xcenter < (x + aabb.x)) {
			return true;
		}
		return false;
	}

	private boolean checkY(float Ycenter, AABB aabb) {
		if (Ycenter < (y + aabb.y)) {
			return true;
		}
		return false;
	}

	private boolean checkZ(float Zcenter, AABB aabb) {
		if (Zcenter < (z + aabb.z)) {
			return true;
		}
		return false;
	}

	public Vector3f getCenter() {
		Vector3f result = new Vector3f();
		result.Xpos = farLeft.Xpos + (nearRight.Xpos - farLeft.Xpos) / 2;
		result.Ypos = farLeft.Ypos + (nearRight.Ypos - farLeft.Ypos) / 2;
		result.Zpos = farLeft.Zpos + (nearRight.Zpos - farLeft.Zpos) / 2;
		return result;
	}
}
