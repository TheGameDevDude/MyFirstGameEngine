package gameEngine.math;

import java.io.Serializable;

@SuppressWarnings("serial")
public class Vector4f implements Serializable {
	public float Xpos;
	public float Ypos;
	public float Zpos;
	public float Wpos;

	public Vector4f() {
		this.Xpos = 0.0f;
		this.Ypos = 0.0f;
		this.Zpos = 0.0f;
		this.Wpos = 0.0f;
	}

	public Vector4f(float Xpos, float Ypos, float Zpos, float Wpos) {
		this.Xpos = Xpos;
		this.Ypos = Ypos;
		this.Zpos = Zpos;
		this.Wpos = Wpos;
	}

	public Vector4f(Vector4f vector) {
		this.Xpos = vector.Xpos;
		this.Ypos = vector.Ypos;
		this.Zpos = vector.Zpos;
		this.Wpos = vector.Wpos;
	}
}
