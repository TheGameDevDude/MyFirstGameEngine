package gameEngine.math;

import static java.lang.Math.*;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import org.lwjgl.opengl.Display;

public class Matrix4f {

	public float[] matrix = new float[4 * 4];

	public static Matrix4f identity() {
		Matrix4f result = new Matrix4f();
		for (int i = 0; i < 4 * 4; i++) {
			result.matrix[i] = 0;
		}
		result.matrix[0 + 0 * 4] = 1.0f;
		result.matrix[1 + 1 * 4] = 1.0f;
		result.matrix[2 + 2 * 4] = 1.0f;
		result.matrix[3 + 3 * 4] = 1.0f;
		return result;
	}

	public Matrix4f multiply(Matrix4f matrix) {
		Matrix4f result = new Matrix4f();
		for (int y = 0; y < 4; y++) {
			for (int x = 0; x < 4; x++) {
				float sum = 0.0f;
				for (int e = 0; e < 4; e++) {
					sum += this.matrix[e + y * 4] * matrix.matrix[x + e * 4];
				}
				result.matrix[x + y * 4] = sum;
			}
		}
		return result;
	}

	public static Matrix4f scale(float scaleFactor) {
		Matrix4f result = new Matrix4f();
		for (int i = 0; i < 4 * 4; i++) {
			result.matrix[i] = 0;
		}
		result.matrix[0 + 0 * 4] = scaleFactor;
		result.matrix[1 + 1 * 4] = scaleFactor;
		result.matrix[2 + 2 * 4] = scaleFactor;
		result.matrix[3 + 3 * 4] = 1;
		return result;
	}

	public static Matrix4f translate(Vector3f vector) {
		Matrix4f result = identity();
		result.matrix[0 + 3 * 4] = vector.Xpos;
		result.matrix[1 + 3 * 4] = vector.Ypos;
		result.matrix[2 + 3 * 4] = vector.Zpos;
		return result;
	}

	public static Matrix4f rotateX(float angle) {
		Matrix4f result = identity();
		float sin = (float) sin(toRadians(angle));
		float cos = (float) cos(toRadians(angle));
		result.matrix[1 + 1 * 4] = cos;
		result.matrix[1 + 2 * 4] = -sin;
		result.matrix[2 + 1 * 4] = sin;
		result.matrix[2 + 2 * 4] = cos;
		return result;
	}

	public static Matrix4f rotateY(float angle) {
		Matrix4f result = identity();
		float sin = (float) sin(toRadians(angle));
		float cos = (float) cos(toRadians(angle));
		result.matrix[0 + 0 * 4] = cos;
		result.matrix[0 + 2 * 4] = sin;
		result.matrix[2 + 0 * 4] = -sin;
		result.matrix[2 + 2 * 4] = cos;
		return result;
	}

	public static Matrix4f rotateZ(float angle) {
		Matrix4f result = identity();
		float sin = (float) sin(toRadians(angle));
		float cos = (float) cos(toRadians(angle));
		result.matrix[0 + 0 * 4] = cos;
		result.matrix[0 + 1 * 4] = -sin;
		result.matrix[1 + 0 * 4] = sin;
		result.matrix[1 + 1 * 4] = cos;
		return result;
	}

	public static Matrix4f rotate(float angle, float x, float y, float z) {
		Matrix4f result = identity();

		float r = (float) toRadians(angle);
		float cos = (float) cos(r);
		float sin = (float) sin(r);
		float omc = 1.0f - cos;

		result.matrix[0 + 0 * 4] = x * omc + cos;
		result.matrix[1 + 0 * 4] = y * x * omc + z * sin;
		result.matrix[2 + 0 * 4] = x * z * omc - y * sin;

		result.matrix[0 + 1 * 4] = x * y * omc - z * sin;
		result.matrix[1 + 1 * 4] = y * omc + cos;
		result.matrix[2 + 1 * 4] = y * z * omc + x * sin;

		result.matrix[0 + 2 * 4] = x * z * omc + y * sin;
		result.matrix[1 + 2 * 4] = y * z * omc - x * sin;
		result.matrix[2 + 2 * 4] = z * omc + cos;

		return result;
	}

	public static Matrix4f perspectiveProjection(double fov, float aspectRatio, float near, float far) {
		Matrix4f result = new Matrix4f();

		aspectRatio = (float) Display.getWidth() / (float) Display.getHeight();
		float y_scale = (float) ((1f / tan(toRadians(fov / 2f))));
		float x_scale = y_scale / aspectRatio;
		float frustum_length = far - near;

		result.matrix[0 + 0 * 4] = x_scale;
		result.matrix[1 + 0 * 4] = 0;
		result.matrix[2 + 0 * 4] = 0;
		result.matrix[3 + 0 * 4] = 0;

		result.matrix[0 + 1 * 4] = 0;
		result.matrix[1 + 1 * 4] = y_scale;
		result.matrix[2 + 1 * 4] = 0;
		result.matrix[3 + 1 * 4] = 0;

		result.matrix[0 + 2 * 4] = 0;
		result.matrix[1 + 2 * 4] = 0;
		result.matrix[2 + 2 * 4] = -(far + near) / frustum_length;
		result.matrix[3 + 2 * 4] = -1;

		result.matrix[0 + 3 * 4] = 0;
		result.matrix[1 + 3 * 4] = 0;
		result.matrix[2 + 3 * 4] = -2 * far * near / frustum_length;
		result.matrix[3 + 3 * 4] = 0;

		return result;
	}

	public FloatBuffer buffer() {
		FloatBuffer buffer = ByteBuffer.allocateDirect(matrix.length << 2).order(ByteOrder.nativeOrder())
				.asFloatBuffer();
		buffer.put(matrix).flip();
		return buffer;
	}
}
