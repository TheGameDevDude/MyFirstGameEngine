package gameEngine.entities;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;

import gameEngine.collision.AABB;
import gameEngine.collision.AABBManager;
import gameEngine.graphics.Loader;
import gameEngine.graphics.MasterRenderer;
import gameEngine.mainGameLoop.Main;
import gameEngine.math.Vector3f;
import gameEngine.model.ModelData;
import gameEngine.model.OBJFileLoader;
import gameEngine.model.RawModel;
import gameEngine.model.TexturedModel;
import gameEngine.textures.ModelTexture;

public class Player extends Camera {
	private Vector3f playerPosition;
	private Vector3f prevPlayerPosition;

	private TexturedModel grid;

	private final static float SPEED_CAP = 0.055f;
	private final static float MOUSE_SENSE = 0.115f;
	private final static float GRAVITY = 0.004f;
	private final static float JUMP_FORCE = 0.1f;
	private final static float CAM_HEIGHT = 0.2f;
	private final static float BOBBING_SPEED = 0.15f;

	private float acceleration = 0;
	private float playerAngleYaw = 0.0f;
	private float playerAnglePitch = 0;
	private float bobbingValue = 0;
	private float bobbingAngle = 0;
	private int aabbIndex = 0;
	private int delay = 0;

	private boolean jumping = false;
	private boolean mouse_pos_player = true;
	private boolean enableBobbing = true;
	private boolean right;
	private boolean left;
	private boolean front;
	private boolean back;
	private boolean up;
	private boolean down;
	private boolean viewAABB = false;

	private int[] grid00 = new int[2];// middle

	private List<AABB> aabbs = new ArrayList<AABB>();
	private List<Entity> collisionGrid = new ArrayList<Entity>();

	private boolean enableCam = false;// development purpose

	public Player(Loader loader, Vector3f position, AABBManager aabbManager) {
		this.playerPosition = position;
		prevPlayerPosition = new Vector3f(playerPosition);

		aabbManager.playerAabb = new AABB(
				new Vector3f(playerPosition.Xpos - 0.07f, playerPosition.Ypos + 0.27f, playerPosition.Zpos - 0.07f),
				new Vector3f(playerPosition.Xpos + 0.07f, playerPosition.Ypos, playerPosition.Zpos + 0.07f));

		ModelData gridModelData = OBJFileLoader.loadOBJ("/models/renderGrid");
		RawModel gridModel = loader.loadToVAO(gridModelData.getVertices(), gridModelData.getTextureCoords(),
				gridModelData.getNormals(), gridModelData.getIndices());
		ModelTexture gridTexture = new ModelTexture(loader.loadTexture("/textures/gridTexture"));
		gridTexture.setShineDamper(25);
		gridTexture.setReflectivity(100);
		grid = new TexturedModel(gridModel, gridTexture);
		grid.getTexture().setSpecularMap(loader.loadTexture("/textures/gridTextureSpecularMap"));
	}

	public void tick(AABBManager aabbManager, Loader loader) {
		delay += 1;
		if (Keyboard.isKeyDown(Keyboard.KEY_C) && enableCam == true && delay >= 20) {
			enableCam = false;
			delay = 0;
		} else if (Keyboard.isKeyDown(Keyboard.KEY_C) && enableCam == false && delay >= 20) {
			enableCam = true;
			delay = 0;
		}

		if (enableCam == true) {
			getAABBFromManagerForCam(aabbManager, 5, loader);
			move();
		}
		if (enableCam == false) {
			getAABBFromManager(aabbManager, 5);
			control(aabbManager);
		}

		if (delay >= 20) {
			delay = 20;
		}
	}

	public void render(MasterRenderer renderer, AABBManager aabbManager) {
		for (Entity entity : collisionGrid) {
			renderer.processEntity(entity);
		}

		if (viewAABB == true || enableCam == true) {

			for (AABB aabb : aabbs) {
				aabbManager.render(renderer, aabb);
			}
			if (enableCam == true) {
				aabbManager.render(renderer, aabbManager.playerAabb);
			}
		}
	}

	private void getAABBFromManagerForCam(AABBManager aabbManager, int noOfGrid, Loader loader) {
		collisionGrid.clear();
		aabbs.clear();
		grid00[0] = (int) Math.floor(getPosition().Xpos / aabbManager.getChunkSize());
		grid00[1] = (int) Math.floor(getPosition().Zpos / aabbManager.getChunkSize());

		makeWithinGrid(grid00, aabbManager);

		aabbs.addAll(aabbManager.chunks.get(grid00[0] + grid00[1] * aabbManager.getSizeX()).aabbs);

		noOfGrid = (noOfGrid / 2);

		for (int z = grid00[1] - noOfGrid; z <= grid00[1] + noOfGrid; z++) {
			for (int x = grid00[0] - noOfGrid; x <= grid00[0] + noOfGrid; x++) {
				if (x < 0 || x > aabbManager.getSizeX()) {
					continue;
				}
				if (z < 0 || z > aabbManager.getSizeZ()) {
					continue;
				}
				collisionGrid.add(new Entity(grid,
						new Vector3f(x * aabbManager.getChunkSize(), 0, z * aabbManager.getChunkSize()), 0, 0, 0,
						0.1f));
			}
		}

		if (Keyboard.isKeyDown(Keyboard.KEY_UP) && delay > 20) {
			aabbIndex += 1;
			delay = 0;
		} else if (Keyboard.isKeyDown(Keyboard.KEY_DOWN) && delay > 20) {
			aabbIndex -= 1;
			delay = 0;
		}

		if (aabbIndex <= 0) {
			aabbIndex = 0;
		}

		if (aabbIndex >= aabbManager.chunks.get(grid00[0] + grid00[1] * aabbManager.getSizeX()).aabbs.size() - 1) {
			aabbIndex = aabbManager.chunks.get(grid00[0] + grid00[1] * aabbManager.getSizeX()).aabbs.size() - 1;
		}

		if (delay >= 20) {
			delay = 20;
		}

		int i = 0;
		for (AABB aabb : aabbManager.chunks.get(grid00[0] + grid00[1] * aabbManager.getSizeX()).aabbs) {
			if (i == aabbIndex) {
				aabb.selected = true;
				int aabbX = (int) Math.floor(aabb.getCenter().Xpos / aabbManager.getChunkSize());
				int aabbZ = (int) Math.floor(aabb.getCenter().Zpos / aabbManager.getChunkSize());
				aabb.edit(aabbX, aabbZ, aabbManager);
			} else {
				aabb.selected = false;
			}
			i++;
		}

		if (Keyboard.isKeyDown(Keyboard.KEY_N) && delay >= 20) {
			delay = 0;
			AABB newAABB = new AABB(new Vector3f(), new Vector3f());
			newAABB.farLeft.Xpos = getPosition().Xpos - 0.07f;
			newAABB.farLeft.Ypos = getPosition().Ypos + 0.27f;
			newAABB.farLeft.Zpos = getPosition().Zpos - 0.07f;

			newAABB.nearRight.Xpos = getPosition().Xpos + 0.07f;
			newAABB.nearRight.Ypos = getPosition().Ypos;
			newAABB.nearRight.Zpos = getPosition().Zpos + 0.07f;
			aabbManager.chunks.get(grid00[0] + grid00[1] * aabbManager.getSizeX()).aabbs.add(newAABB);
		} else if (Keyboard.isKeyDown(Keyboard.KEY_X) && delay >= 20) {
			delay = 0;
			List<AABB> tempAABB = new ArrayList<AABB>();
			for (AABB aabb : aabbManager.chunks.get(grid00[0] + grid00[1] * aabbManager.getSizeX()).aabbs) {
				if (aabb.selected != true) {
					tempAABB.add(aabb);
				}
			}
			aabbManager.chunks.get(grid00[0] + grid00[1] * aabbManager.getSizeX()).aabbs.clear();
			aabbManager.chunks.get(grid00[0] + grid00[1] * aabbManager.getSizeX()).aabbs.addAll(tempAABB);
			tempAABB.clear();
		}
	}

	private void getAABBFromManager(AABBManager aabbManager, int noOfGrid) {
		collisionGrid.clear();
		aabbs.clear();
		grid00[0] = (int) Math.floor(playerPosition.Xpos / aabbManager.getChunkSize());
		grid00[1] = (int) Math.floor(playerPosition.Zpos / aabbManager.getChunkSize());

		makeWithinGrid(grid00, aabbManager);

		aabbs.addAll(aabbManager.chunks.get(grid00[0] + grid00[1] * aabbManager.getSizeX()).aabbs);

		if (viewAABB == true) {

			noOfGrid = (noOfGrid / 2);

			for (int z = grid00[1] - noOfGrid; z <= grid00[1] + noOfGrid; z++) {
				for (int x = grid00[0] - noOfGrid; x <= grid00[0] + noOfGrid; x++) {
					if (x < 0 || x > aabbManager.getSizeX()) {
						continue;
					}
					if (z < 0 || z > aabbManager.getSizeZ()) {
						continue;
					}
					collisionGrid.add(new Entity(grid,
							new Vector3f(x * aabbManager.getChunkSize(), 0, z * aabbManager.getChunkSize()), 0, 0, 0,
							0.1f));
				}
			}
		}
	}

	private void checkCollisionX(AABBManager aabbManager) {
		for (AABB aabb : aabbs) {
			if (aabb.check(aabbManager.playerAabb) == true) {
				if (left == true) {
					playerPosition.Xpos += (aabb.nearRight.Xpos - aabbManager.playerAabb.farLeft.Xpos) + 0.01f;
				} else if (right == true) {
					playerPosition.Xpos -= (aabbManager.playerAabb.nearRight.Xpos - aabb.farLeft.Xpos) + 0.01f;
				}
				return;
			}
		}
	}

	private void checkCollisionY(AABBManager aabbManager) {
		for (AABB aabb : aabbs) {
			if (aabb.check(aabbManager.playerAabb) == true) {
				if (down == true) {
					playerPosition.Ypos += (aabb.farLeft.Ypos - aabbManager.playerAabb.nearRight.Ypos) + 0.001f;
					acceleration = 0;
					jumping = false;

					if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
						if (aabbManager.playerAabb.farLeft.Xpos < aabb.farLeft.Xpos) {
							playerPosition.Xpos += (-aabbManager.playerAabb.farLeft.Xpos + aabb.farLeft.Xpos);
						}

						if (aabbManager.playerAabb.nearRight.Xpos > aabb.nearRight.Xpos) {
							playerPosition.Xpos += (-aabbManager.playerAabb.nearRight.Xpos + aabb.nearRight.Xpos);
						}

						if (aabbManager.playerAabb.farLeft.Zpos < aabb.farLeft.Zpos) {
							playerPosition.Zpos += (-aabbManager.playerAabb.farLeft.Zpos + aabb.farLeft.Zpos);
						}

						if (aabbManager.playerAabb.nearRight.Zpos > aabb.nearRight.Zpos) {
							playerPosition.Zpos += (-aabbManager.playerAabb.nearRight.Zpos + aabb.nearRight.Zpos);
						}
					}
				} else if (up == true) {
					playerPosition.Ypos -= (aabbManager.playerAabb.farLeft.Ypos - aabb.nearRight.Ypos) + 0.001f;
					acceleration += (aabbManager.playerAabb.farLeft.Ypos - aabb.nearRight.Ypos) + 0.001f;
				}
				return;
			}
		}
	}

	private void checkCollisionZ(AABBManager aabbManager) {
		for (AABB aabb : aabbs) {
			if (aabb.check(aabbManager.playerAabb) == true) {
				if (front == true) {
					playerPosition.Zpos += (aabb.nearRight.Zpos - aabbManager.playerAabb.farLeft.Zpos) + 0.01f;
				} else if (back == true) {
					playerPosition.Zpos -= (aabbManager.playerAabb.nearRight.Zpos - aabb.farLeft.Zpos) + 0.01f;
				}
				return;
			}
		}
	}

	private void control(AABBManager aabbManager) {
		playerKeyboardControl(aabbManager);
		playerMouseControl();
		setCameraReletiveToPlayer();

		// will be removed soon
		if (playerPosition.Ypos < 0.5f) {
			playerPosition.Ypos = 0.5f;
			acceleration = 0;
			jumping = false;
		}
	}

	private void playerKeyboardControl(AABBManager aabbManager) {
		// speed in Z direction
		float Zdir = 0;
		// speed in X direction
		float Xdir = 0;

		float psin = (float) Math.sin(Math.toRadians(playerAngleYaw));
		float pcos = (float) Math.cos(Math.toRadians(playerAngleYaw));

		float nsin = (float) Math.sin(Math.toRadians(-playerAngleYaw));
		float ncos = (float) Math.cos(Math.toRadians(-playerAngleYaw));

		// move the player
		if (Keyboard.isKeyDown(Keyboard.KEY_W)) {
			Zdir = -SPEED_CAP * pcos * Main.getFrameTime();
			Xdir = SPEED_CAP * nsin * Main.getFrameTime();
		} else if (Keyboard.isKeyDown(Keyboard.KEY_S)) {
			Zdir = SPEED_CAP * pcos * Main.getFrameTime();
			Xdir = -SPEED_CAP * nsin * Main.getFrameTime();
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_D)) {
			Zdir += -SPEED_CAP * psin * Main.getFrameTime();
			Xdir += SPEED_CAP * ncos * Main.getFrameTime();
		} else if (Keyboard.isKeyDown(Keyboard.KEY_A)) {
			Zdir += SPEED_CAP * psin * Main.getFrameTime();
			Xdir += -SPEED_CAP * ncos * Main.getFrameTime();
		}

		if (Keyboard.isKeyDown(Keyboard.KEY_B) && viewAABB == false && delay >= 20) {
			viewAABB = true;
			delay = 0;
		} else if (Keyboard.isKeyDown(Keyboard.KEY_B) && viewAABB == true && delay >= 20) {
			viewAABB = false;
			delay = 0;
		}

		// check whether front or back for collision detection
		if (Zdir < 0) {
			front = true;
			back = false;
		} else {
			front = false;
			back = true;
		}

		// move the player in Z direction
		playerPosition.Zpos += Zdir;
		updatePlayerAABB(aabbManager);// update collision box

		// check the collision in the Z direction
		checkCollisionZ(aabbManager);
		updatePlayerAABB(aabbManager);// update collision box

		// check whether right or left for collision detection
		if (Xdir < 0) {
			left = true;
			right = false;
		} else {
			right = true;
			left = false;
		}

		// move the player in X direction
		playerPosition.Xpos += Xdir;
		updatePlayerAABB(aabbManager);// update collision box

		// check collision in X direction
		checkCollisionX(aabbManager);
		updatePlayerAABB(aabbManager);// update collision box

		// enable jumping
		if (Keyboard.isKeyDown(Keyboard.KEY_SPACE) && jumping == false) {
			playerPosition.Ypos += 0.1f;
			updatePlayerAABB(aabbManager);// update collision box
			jumping = true;
		}

		// gravity
		acceleration += GRAVITY * Main.getFrameTime();
		playerPosition.Ypos -= acceleration * Main.getFrameTime();
		updatePlayerAABB(aabbManager);

		// due to gravity down is true
		down = true;
		up = false;

		checkCollisionY(aabbManager);// check collision in the Y direction
		updatePlayerAABB(aabbManager);// update collision box

		// war between the gravity and the JUMP_FORCE
		if (jumping == true) {
			playerPosition.Ypos += JUMP_FORCE * Main.getFrameTime();
			updatePlayerAABB(aabbManager);// update collision box

			down = false;
			// since there is upward JUMP_FORCE, up is true
			up = true;

			checkCollisionY(aabbManager);// check collision in Y direction
			updatePlayerAABB(aabbManager);// update collision box
		}

		Vector3f currentPlayerPosition = new Vector3f(playerPosition);
		// calculating delta to know when to bob
		Vector3f delta = new Vector3f(currentPlayerPosition.Xpos - prevPlayerPosition.Xpos, 0,
				currentPlayerPosition.Zpos - prevPlayerPosition.Zpos);
		if (enableBobbing == true) {
			// only want to bob while moving and not jumping
			if (delta.getMagnitude() != 0 && jumping == false) {
				// increase the bobbing angle
				bobbingAngle += BOBBING_SPEED * Main.getFrameTime();
				// Oscillate the bobbing value
				bobbingValue = (float) Math.sin(bobbingAngle);
			} else {
				bobbingValue = 0;
				bobbingAngle = 0;
			}
		}
		prevPlayerPosition = new Vector3f(playerPosition);
	}

	private void playerMouseControl() {
		if (mouse_pos_player == true) {
			Mouse.setCursorPosition(Display.getWidth() / 2, Display.getHeight() / 2);
			Mouse.setGrabbed(true);
			mouse_pos_player = false;
		}

		if (Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) {
			Mouse.setGrabbed(false);
		}

		playerAngleYaw -= MOUSE_SENSE * Mouse.getDX() * Main.getFrameTime();// rotate along the Y
		playerAnglePitch += MOUSE_SENSE * Mouse.getDY() * Main.getFrameTime();// rotate along the X
		if (playerAngleYaw >= 360) {
			playerAngleYaw = 0;
		}
		if (playerAngleYaw <= -360) {
			playerAngleYaw = 0;
		}

		if (playerAnglePitch > 90) {
			playerAnglePitch = 90;
		} else if (playerAnglePitch < -90) {
			playerAnglePitch = -90;
		}
	}

	private void setCameraReletiveToPlayer() {
		// bob only when bobbing is enabled
		if (enableBobbing == true) {
			// bob in the y direction
			float yOff = bobbingValue * 0.2f;
			// bob in the x direction
			float xOff = bobbingValue * 0.2f;
			if (yOff < 0) {
				yOff = -yOff;
			}
			float pcos = (float) Math.cos(Math.toRadians(playerAngleYaw));
			float nsin = (float) Math.sin(Math.toRadians(-playerAngleYaw));
			setPosition(new Vector3f(playerPosition.Xpos + xOff * pcos, (playerPosition.Ypos + CAM_HEIGHT) + yOff,
					playerPosition.Zpos + xOff * nsin));
		} else {
			setPosition(new Vector3f(playerPosition.Xpos, (playerPosition.Ypos + CAM_HEIGHT), playerPosition.Zpos));
		}

		setYaw(playerAngleYaw);
		setPitch(playerAnglePitch);
	}

	private void updatePlayerAABB(AABBManager aabbManager) {
		aabbManager.playerAabb.farLeft.Xpos = playerPosition.Xpos - 0.07f;
		aabbManager.playerAabb.farLeft.Ypos = playerPosition.Ypos + 0.27f;
		aabbManager.playerAabb.farLeft.Zpos = playerPosition.Zpos - 0.07f;

		aabbManager.playerAabb.nearRight.Xpos = playerPosition.Xpos + 0.07f;
		aabbManager.playerAabb.nearRight.Ypos = playerPosition.Ypos;
		aabbManager.playerAabb.nearRight.Zpos = playerPosition.Zpos + 0.07f;
	}

	private void makeWithinGrid(int[] gridPos, AABBManager aabbManager) {
		if (gridPos[0] < 0) {
			gridPos[0] = 0;
		}

		if (gridPos[1] < 0) {
			gridPos[1] = 0;
		}

		if (gridPos[0] >= aabbManager.getSizeX()) {
			gridPos[0] = aabbManager.getSizeX();
		}

		if (gridPos[1] >= aabbManager.getSizeZ()) {
			gridPos[1] = aabbManager.getSizeZ();
		}
	}

	public Vector3f getPlayerPosition() {
		return playerPosition;
	}
}
