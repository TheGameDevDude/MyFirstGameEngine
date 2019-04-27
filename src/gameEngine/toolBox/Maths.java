package gameEngine.toolBox;

import gameEngine.entities.Camera;
import gameEngine.math.Matrix4f;
import gameEngine.math.Vector3f;

public class Maths {
	public static Matrix4f createTranformationMatrix(Vector3f translation, float rx, float ry, float rz, float scale) {
		Matrix4f translationMatrix = Matrix4f.translate(translation);
		Matrix4f rotationMatrixX = Matrix4f.rotateX(rx);
		Matrix4f rotationMatrixY = Matrix4f.rotateY(ry);
		Matrix4f rotationMatrixZ = Matrix4f.rotateZ(rz);
		Matrix4f scaleMatrix = Matrix4f.scale(scale);
		return scaleMatrix.multiply(rotationMatrixX).multiply(rotationMatrixY).multiply(rotationMatrixZ)
				.multiply(translationMatrix);
	}

	public static Matrix4f createViewMatrix(Camera camera) {
		Vector3f translation = new Vector3f(camera.getPosition().Xpos, camera.getPosition().Ypos,
				camera.getPosition().Zpos);
		float rx = camera.getPitch();
		float ry = camera.getYaw();
		Matrix4f translationMatrix = Matrix4f
				.translate(new Vector3f(-translation.Xpos, -translation.Ypos, -translation.Zpos));
		Matrix4f rotationMatrixX = Matrix4f.rotateX(-rx);
		Matrix4f rotationMatrixY = Matrix4f.rotateY(-ry);
		return translationMatrix.multiply(rotationMatrixY).multiply(rotationMatrixX);
	}

	public static Matrix4f inverse3(Matrix4f matrix) {
		Matrix4f result = Matrix4f.identity();

		Matrix4f coFactor = Matrix4f.identity();

		coFactor.matrix[0 + 0 * 4] = (matrix.matrix[1 + 1 * 4] * matrix.matrix[2 + 2 * 4]
				- matrix.matrix[1 + 2 * 4] * matrix.matrix[2 + 1 * 4]);

		coFactor.matrix[1 + 0 * 4] = -(matrix.matrix[0 + 1 * 4] * matrix.matrix[2 + 2 * 4]
				- matrix.matrix[0 + 2 * 4] * matrix.matrix[2 + 1 * 4]);

		coFactor.matrix[2 + 0 * 4] = (matrix.matrix[0 + 1 * 4] * matrix.matrix[1 + 2 * 4]
				- matrix.matrix[0 + 2 * 4] * matrix.matrix[1 + 1 * 4]);

		coFactor.matrix[0 + 1 * 4] = -(matrix.matrix[1 + 0 * 4] * matrix.matrix[2 + 2 * 4]
				- matrix.matrix[1 + 2 * 4] * matrix.matrix[2 + 0 * 4]);

		coFactor.matrix[1 + 1 * 4] = (matrix.matrix[0 + 0 * 4] * matrix.matrix[2 + 2 * 4]
				- matrix.matrix[0 + 2 * 4] * matrix.matrix[2 + 0 * 4]);

		coFactor.matrix[2 + 1 * 4] = -(matrix.matrix[0 + 0 * 4] * matrix.matrix[1 + 2 * 4]
				- matrix.matrix[0 + 2 * 4] * matrix.matrix[1 + 0 * 4]);

		coFactor.matrix[0 + 2 * 4] = (matrix.matrix[1 + 0 * 4] * matrix.matrix[2 + 1 * 4]
				- matrix.matrix[1 + 1 * 4] * matrix.matrix[2 + 0 * 4]);

		coFactor.matrix[1 + 2 * 4] = -(matrix.matrix[0 + 0 * 4] * matrix.matrix[2 + 1 * 4]
				- matrix.matrix[0 + 1 * 4] * matrix.matrix[2 + 0 * 4]);

		coFactor.matrix[2 + 2 * 4] = (matrix.matrix[0 + 0 * 4] * matrix.matrix[1 + 1 * 4]
				- matrix.matrix[0 + 1 * 4] * matrix.matrix[1 + 0 * 4]);

		Matrix4f adj = Matrix4f.identity();

		adj.matrix[1 + 0 * 4] = coFactor.matrix[0 + 1 * 4];
		adj.matrix[2 + 0 * 4] = coFactor.matrix[0 + 2 * 4];
		adj.matrix[2 + 1 * 4] = coFactor.matrix[1 + 2 * 4];

		adj.matrix[0 + 1 * 4] = coFactor.matrix[1 + 0 * 4];
		adj.matrix[0 + 2 * 4] = coFactor.matrix[2 + 0 * 4];
		adj.matrix[1 + 2 * 4] = coFactor.matrix[2 + 1 * 4];

		float det = (matrix.matrix[0 + 0 * 4] * coFactor.matrix[0 + 0 * 4])
				+ (matrix.matrix[1 + 0 * 4] * coFactor.matrix[1 + 0 * 4])
				+ (matrix.matrix[2 + 0 * 4] * coFactor.matrix[2 + 0 * 4]);

		result.matrix[0 + 0 * 4] = adj.matrix[0 + 0 * 4] / det;
		result.matrix[1 + 0 * 4] = adj.matrix[1 + 0 * 4] / det;
		result.matrix[2 + 0 * 4] = adj.matrix[2 + 0 * 4] / det;

		result.matrix[0 + 1 * 4] = adj.matrix[0 + 1 * 4] / det;
		result.matrix[1 + 1 * 4] = adj.matrix[1 + 1 * 4] / det;
		result.matrix[2 + 1 * 4] = adj.matrix[2 + 1 * 4] / det;

		result.matrix[0 + 2 * 4] = adj.matrix[0 + 2 * 4] / det;
		result.matrix[1 + 2 * 4] = adj.matrix[1 + 2 * 4] / det;
		result.matrix[2 + 2 * 4] = adj.matrix[2 + 2 * 4] / det;

		return result;
	}

}
