package gameEngine.math;

import java.io.Serializable;

@SuppressWarnings("serial")
public class Vector2f implements Serializable {
	public float Xpos;
	public float Ypos;

	public Vector2f() {
		this.Xpos = 0.0f;
		this.Ypos = 0.0f;
	}

	public Vector2f(float Xpos, float Ypos) {
		this.Xpos = Xpos;
		this.Ypos = Ypos;
	}

	public Vector2f(Vector2f vector) {
		this.Xpos = vector.Xpos;
		this.Ypos = vector.Ypos;
	}
}
