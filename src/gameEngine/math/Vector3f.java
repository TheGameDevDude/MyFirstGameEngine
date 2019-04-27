package gameEngine.math;

import java.io.Serializable;

@SuppressWarnings("serial")
public class Vector3f implements Serializable{
	public float Xpos;
	public float Ypos;
	public float Zpos;

	public Vector3f() {
		this.Xpos = 0.0f;
		this.Ypos = 0.0f;
		this.Zpos = 0.0f;
	}

	public Vector3f(float Xpos, float Ypos, float Zpos) {
		this.Xpos = Xpos;
		this.Ypos = Ypos;
		this.Zpos = Zpos;
	}

	public Vector3f(Vector3f vector) {
		this.Xpos = vector.Xpos;
		this.Ypos = vector.Ypos;
		this.Zpos = vector.Zpos;
	}

	public Vector3f add(Vector3f a, Vector3f b) {
		Vector3f result = new Vector3f();
		result.Xpos = a.Xpos + b.Xpos;
		result.Xpos = a.Ypos + b.Ypos;
		result.Xpos = a.Zpos + b.Zpos;
		return result;
	}

	public void normalize() {
		float mag = getMagnitude();
		Xpos /= mag;
		Ypos /= mag;
		Zpos /= mag;
	}

	public void scale(float scaleFactor) {
		Xpos *= scaleFactor;
		Ypos *= scaleFactor;
		Zpos *= scaleFactor;
	}

	public float getMagnitude() {
		return (float) Math.sqrt(Xpos * Xpos + Ypos * Ypos + Zpos * Zpos);
	}
}
