package gameEngine.entities;

import gameEngine.graphics.Loader;
import gameEngine.model.RawModel;
import gameEngine.model.TexturedModel;
import gameEngine.textures.ModelTexture;

public class BlockModel {
	private TexturedModel model;

	public BlockModel(Loader loader, String path) {
		float[] vertices = {
				// front
				-0.5f, 1, 0.5f, // v0
				0.5f, 1, 0.5f, // v1
				0.5f, 0, 0.5f, // v2
				-0.5f, 0, 0.5f, // v3

				// back
				-0.5f, 1, -0.5f, // v4
				0.5f, 1, -0.5f, // v5
				0.5f, 0, -0.5f, // v6
				-0.5f, 0, -0.5f, // v7

				// right
				0.5f, 1, 0.5f, // v8
				0.5f, 1, -0.5f, // v9
				0.5f, 0, -0.5f, // v10
				0.5f, 0, 0.5f, // v11

				// left
				-0.5f, 1, 0.5f, // v12
				-0.5f, 1, -0.5f, // v13
				-0.5f, 0, -0.5f, // v14
				-0.5f, 0, 0.5f, // v15

				// top
				0.5f, 1, 0.5f, // v16
				-0.5f, 1, 0.5f, // v17
				-0.5f, 1, -0.5f, // v18
				0.5f, 1, -0.5f, // v19

				// bottom
				-0.5f, 0, 0.5f, // v20
				0.5f, 0, 0.5f, // v21
				0.5f, 0, -0.5f, // v22
				-0.5f, 0, -0.5f// v23

		};

		float[] textureCoords = {
				// front
				0, 0, // t0
				1, 0, // t1
				1, 1, // t2
				0, 1, // t3

				// back
				0, 0, // t0
				1, 0, // t1
				1, 1, // t2
				0, 1, // t3

				// right
				0, 0, // t0
				1, 0, // t1
				1, 1, // t2
				0, 1, // t3

				// left
				0, 0, // t0
				1, 0, // t1
				1, 1, // t2
				0, 1, // t3

				// top
				0, 0, // t0
				1, 0, // t1
				1, 1, // t2
				0, 1, // t3

				// bottom
				0, 0, // t0
				1, 0, // t1
				1, 1, // t2
				0, 1, // t3
		};

		float[] normals = {
				// front
				-0.57f, 0.57f, 0.57f, // n1
				0.57f, 0.57f, 0.57f, // n2
				0.57f, -0.57f, 0.57f, // n3
				-0.57f, -0.57f, 0.57f, // n4

				// back
				-0.57f, 0.57f, -0.57f, // n6
				0.57f, 0.57f, -0.57f, // n7
				0.57f, -0.57f, -0.57f, // n8
				-0.57f, -0.57f, -0.57f, // n9

				// right
				0.57f, 0.57f, 0.57f, // n10
				0.57f, 0.57f, -0.57f, // n11
				0.57f, -0.57f, -0.57f, // n12
				0.57f, -0.57f, 0.57f, // n13

				// left
				-0.57f, 0.57f, 0.57f, // n14
				-0.57f, 0.57f, -0.57f, // n15
				-0.57f, -0.57f, -0.57f, // n16
				-0.57f, -0.57f, 0.57f, // n17

				// top
				0.57f, 0.57f, 0.57f, // n18
				-0.57f, 0.57f, 0.57f, // n19
				-0.57f, 0.57f, -0.57f, // n20
				0.57f, 0.57f, -0.57f, // n21

				// bottom
				-0.57f, -0.57f, 0.57f, // n22
				0.57f, -0.57f, 0.57f, // n23
				0.57f, -0.57f, -0.57f, // n24
				-0.57f, -0.57f, -0.57f// n25
		};
		int[] indices = {
				// front
				0, 3, 1, // t1
				1, 3, 2, // t2

				// back
				5, 6, 4, // t1
				4, 6, 7, // t2

				// right
				8, 11, 9, // t1
				9, 11, 10, // t2

				// left
				13, 14, 12, // t1
				12, 14, 15, // t2

				// top
				18, 17, 19, // t1
				19, 17, 16, // t2

				// bottom
				20, 23, 21, // t1
				21, 23, 22// t2
		};

		RawModel rawModel = loader.loadToVAO(vertices, textureCoords, normals, indices);
		ModelTexture texture = new ModelTexture(loader.loadTexture(path));
		model = new TexturedModel(rawModel, texture);
	}

	public TexturedModel getTexturedModel() {
		return model;
	}
}
