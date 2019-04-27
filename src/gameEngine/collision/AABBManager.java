package gameEngine.collision;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.input.Keyboard;

import gameEngine.entities.Entity;
import gameEngine.graphics.Loader;
import gameEngine.graphics.MasterRenderer;
import gameEngine.math.Vector3f;
import gameEngine.model.ModelData;
import gameEngine.model.OBJFileLoader;
import gameEngine.model.RawModel;
import gameEngine.model.TexturedModel;
import gameEngine.textures.ModelTexture;

public class AABBManager {

	public List<AABB> aabbs = new ArrayList<AABB>();
	public List<Chunk> chunks = new ArrayList<Chunk>();
	public AABB playerAabb;

	private TexturedModel aabb1;
	private TexturedModel aabb2;

	private static int CHUNK_SIZE = 10;
	private static int sizeX = 10;
	private static int sizeZ = 10;

	public AABBManager(Loader loader) {
		aabbs.add(new AABB(new Vector3f(0, 3.5f, 0), new Vector3f(10, 0, 50)));
		aabbs.add(new AABB(new Vector3f(0, 10, 0), new Vector3f(10, 6.5f, 50)));
		aabbs.add(new AABB(new Vector3f(0, 10, 0), new Vector3f(3, 0, 50)));
		aabbs.add(new AABB(new Vector3f(7, 10, 0), new Vector3f(10, 0, 50)));

		ModelData aabbModelData = OBJFileLoader.loadOBJ("/models/AABB");
		RawModel aabbModel = loader.loadToVAO(aabbModelData.getVertices(), aabbModelData.getTextureCoords(),
				aabbModelData.getNormals(), aabbModelData.getIndices());
		ModelTexture aabbTexture = new ModelTexture(loader.loadTexture("/textures/AABB"));
		ModelTexture aabbTexture2 = new ModelTexture(loader.loadTexture("/textures/AABB2"));
		aabbTexture.setShineDamper(25);
		aabbTexture.setReflectivity(100);
		aabb1 = new TexturedModel(aabbModel, aabbTexture);
		aabb1.getTexture().setSpecularMap(loader.loadTexture("/textures/gridTextureSpecularMap"));
		aabb2 = new TexturedModel(aabbModel, aabbTexture2);
		aabb2.getTexture().setSpecularMap(loader.loadTexture("/textures/gridTextureSpecularMap"));

		placeAllAabbInChunks();
	}

	public void render(MasterRenderer renderer, AABB aabb) {
		aabb.x = Math.abs((aabb.nearRight.Xpos - aabb.farLeft.Xpos) / 2);
		aabb.y = Math.abs((aabb.nearRight.Ypos - aabb.farLeft.Ypos) / 2);
		aabb.z = Math.abs((aabb.nearRight.Zpos - aabb.farLeft.Zpos) / 2);

		if (aabb.selected == false) {
			renderer.processEntity(new Entity(aabb1,
					new Vector3f(aabb.farLeft.Xpos, aabb.farLeft.Ypos, aabb.farLeft.Zpos), 0, 0, -90, 0.05f));
			renderer.processEntity(new Entity(aabb1,
					new Vector3f(aabb.nearRight.Xpos, aabb.nearRight.Ypos, aabb.nearRight.Zpos), 0, 90, 90, 0.05f));
			renderer.processEntity(new Entity(aabb1,
					new Vector3f(aabb.nearRight.Xpos - aabb.x * 2, aabb.nearRight.Ypos, aabb.nearRight.Zpos), 0, 90, 0,
					0.05f));
			renderer.processEntity(new Entity(aabb1,
					new Vector3f(aabb.farLeft.Xpos + aabb.x * 2, aabb.farLeft.Ypos, aabb.farLeft.Zpos), 0, 0, 180,
					0.05f));
			renderer.processEntity(new Entity(aabb1,
					new Vector3f(aabb.nearRight.Xpos, aabb.nearRight.Ypos, aabb.nearRight.Zpos - aabb.z * 2), 0, 0, 90,
					0.05f));
			renderer.processEntity(new Entity(aabb1,
					new Vector3f(aabb.farLeft.Xpos, aabb.farLeft.Ypos - aabb.y * 2, aabb.farLeft.Zpos), 0, 0, 0,
					0.05f));
			renderer.processEntity(new Entity(aabb1,
					new Vector3f(aabb.nearRight.Xpos, aabb.nearRight.Ypos + aabb.y * 2, aabb.nearRight.Zpos), 0, 90,
					180, 0.05f));
			renderer.processEntity(new Entity(aabb1,
					new Vector3f(aabb.farLeft.Xpos, aabb.farLeft.Ypos, aabb.farLeft.Zpos + aabb.z * 2), 0, 90, -90,
					0.05f));
		} else if (aabb.selected == true && Keyboard.isKeyDown(Keyboard.KEY_Q) == false
				&& Keyboard.isKeyDown(Keyboard.KEY_E) == false) {
			renderer.processEntity(new Entity(aabb2,
					new Vector3f(aabb.farLeft.Xpos, aabb.farLeft.Ypos, aabb.farLeft.Zpos), 0, 0, -90, 0.05f));
			renderer.processEntity(new Entity(aabb2,
					new Vector3f(aabb.nearRight.Xpos, aabb.nearRight.Ypos, aabb.nearRight.Zpos), 0, 90, 90, 0.05f));
			renderer.processEntity(new Entity(aabb2,
					new Vector3f(aabb.nearRight.Xpos - aabb.x * 2, aabb.nearRight.Ypos, aabb.nearRight.Zpos), 0, 90, 0,
					0.05f));
			renderer.processEntity(new Entity(aabb2,
					new Vector3f(aabb.farLeft.Xpos + aabb.x * 2, aabb.farLeft.Ypos, aabb.farLeft.Zpos), 0, 0, 180,
					0.05f));
			renderer.processEntity(new Entity(aabb2,
					new Vector3f(aabb.nearRight.Xpos, aabb.nearRight.Ypos, aabb.nearRight.Zpos - aabb.z * 2), 0, 0, 90,
					0.05f));
			renderer.processEntity(new Entity(aabb2,
					new Vector3f(aabb.farLeft.Xpos, aabb.farLeft.Ypos - aabb.y * 2, aabb.farLeft.Zpos), 0, 0, 0,
					0.05f));
			renderer.processEntity(new Entity(aabb2,
					new Vector3f(aabb.nearRight.Xpos, aabb.nearRight.Ypos + aabb.y * 2, aabb.nearRight.Zpos), 0, 90,
					180, 0.05f));
			renderer.processEntity(new Entity(aabb2,
					new Vector3f(aabb.farLeft.Xpos, aabb.farLeft.Ypos, aabb.farLeft.Zpos + aabb.z * 2), 0, 90, -90,
					0.05f));
		} else if (aabb.selected == true && Keyboard.isKeyDown(Keyboard.KEY_Q)) {
			renderer.processEntity(new Entity(aabb2,
					new Vector3f(aabb.farLeft.Xpos, aabb.farLeft.Ypos, aabb.farLeft.Zpos), 0, 0, -90, 0.05f));
			renderer.processEntity(new Entity(aabb1,
					new Vector3f(aabb.nearRight.Xpos, aabb.nearRight.Ypos, aabb.nearRight.Zpos), 0, 90, 90, 0.05f));
			renderer.processEntity(new Entity(aabb1,
					new Vector3f(aabb.nearRight.Xpos - aabb.x * 2, aabb.nearRight.Ypos, aabb.nearRight.Zpos), 0, 90, 0,
					0.05f));
			renderer.processEntity(new Entity(aabb1,
					new Vector3f(aabb.farLeft.Xpos + aabb.x * 2, aabb.farLeft.Ypos, aabb.farLeft.Zpos), 0, 0, 180,
					0.05f));
			renderer.processEntity(new Entity(aabb1,
					new Vector3f(aabb.nearRight.Xpos, aabb.nearRight.Ypos, aabb.nearRight.Zpos - aabb.z * 2), 0, 0, 90,
					0.05f));
			renderer.processEntity(new Entity(aabb1,
					new Vector3f(aabb.farLeft.Xpos, aabb.farLeft.Ypos - aabb.y * 2, aabb.farLeft.Zpos), 0, 0, 0,
					0.05f));
			renderer.processEntity(new Entity(aabb1,
					new Vector3f(aabb.nearRight.Xpos, aabb.nearRight.Ypos + aabb.y * 2, aabb.nearRight.Zpos), 0, 90,
					180, 0.05f));
			renderer.processEntity(new Entity(aabb1,
					new Vector3f(aabb.farLeft.Xpos, aabb.farLeft.Ypos, aabb.farLeft.Zpos + aabb.z * 2), 0, 90, -90,
					0.05f));
		} else if (aabb.selected == true && Keyboard.isKeyDown(Keyboard.KEY_E)) {
			renderer.processEntity(new Entity(aabb1,
					new Vector3f(aabb.farLeft.Xpos, aabb.farLeft.Ypos, aabb.farLeft.Zpos), 0, 0, -90, 0.05f));
			renderer.processEntity(new Entity(aabb2,
					new Vector3f(aabb.nearRight.Xpos, aabb.nearRight.Ypos, aabb.nearRight.Zpos), 0, 90, 90, 0.05f));
			renderer.processEntity(new Entity(aabb1,
					new Vector3f(aabb.nearRight.Xpos - aabb.x * 2, aabb.nearRight.Ypos, aabb.nearRight.Zpos), 0, 90, 0,
					0.05f));
			renderer.processEntity(new Entity(aabb1,
					new Vector3f(aabb.farLeft.Xpos + aabb.x * 2, aabb.farLeft.Ypos, aabb.farLeft.Zpos), 0, 0, 180,
					0.05f));
			renderer.processEntity(new Entity(aabb1,
					new Vector3f(aabb.nearRight.Xpos, aabb.nearRight.Ypos, aabb.nearRight.Zpos - aabb.z * 2), 0, 0, 90,
					0.05f));
			renderer.processEntity(new Entity(aabb1,
					new Vector3f(aabb.farLeft.Xpos, aabb.farLeft.Ypos - aabb.y * 2, aabb.farLeft.Zpos), 0, 0, 0,
					0.05f));
			renderer.processEntity(new Entity(aabb1,
					new Vector3f(aabb.nearRight.Xpos, aabb.nearRight.Ypos + aabb.y * 2, aabb.nearRight.Zpos), 0, 90,
					180, 0.05f));
			renderer.processEntity(new Entity(aabb1,
					new Vector3f(aabb.farLeft.Xpos, aabb.farLeft.Ypos, aabb.farLeft.Zpos + aabb.z * 2), 0, 90, -90,
					0.05f));
		}
	}

	int count = 0;

	private void placeAllAabbInChunks() {
		List<AABB> tempAABB = new ArrayList<AABB>();
		for (int z = 0; z < sizeZ; z++) {
			for (int x = 0; x < sizeX; x++) {

				float xCenterGrid = x * CHUNK_SIZE / 2;
				float zCenterGrid = z * CHUNK_SIZE / 2;

				for (AABB aabb : aabbs) {

					float xDist = Math.abs(xCenterGrid - aabb.x);
					float zDist = Math.abs(zCenterGrid - aabb.z);

					float xx = (CHUNK_SIZE / 2) + ((aabb.nearRight.Xpos - aabb.farLeft.Xpos) / 2);
					float zz = (CHUNK_SIZE / 2) + ((aabb.nearRight.Zpos - aabb.farLeft.Zpos) / 2);

					if ((xDist < xx) && (zDist < zz)) {
						tempAABB.add(aabb);
					}

				}
				chunks.add(new Chunk(CHUNK_SIZE, tempAABB));
				tempAABB.clear();
			}
		}
	}

	public int getChunkSize() {
		return CHUNK_SIZE;
	}

	public int getSizeX() {
		return sizeX;
	}

	public int getSizeZ() {
		return sizeZ;
	}

	public AABB getPlayerAABB() {
		return playerAabb;
	}
}
