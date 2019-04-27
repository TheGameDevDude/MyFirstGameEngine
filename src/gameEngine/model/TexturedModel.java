package gameEngine.model;

import java.io.Serializable;

import gameEngine.textures.ModelTexture;

@SuppressWarnings("serial")
public class TexturedModel implements Serializable{
	private RawModel rawModel;
	private ModelTexture texture;

	public TexturedModel(RawModel rawModel, ModelTexture texture) {
		this.rawModel = rawModel;
		this.texture = texture;
	}

	public RawModel getRawModel() {
		return rawModel;
	}

	public ModelTexture getTexture() {
		return texture;
	}
}
