package gameEngine.textures;

import java.io.Serializable;

@SuppressWarnings("serial")
public class ModelTexture implements Serializable{
	private int textureID;
	private int specularMap;

	private float shineDamper = 1;
	private float reflectivity = 0;
	private boolean hasSpecularMap = false;

	public ModelTexture(int textureID) {
		this.textureID = textureID;
	}

	//also contains extra info for glowing
	public void setSpecularMap(int specMap) {
		this.specularMap = specMap;
		hasSpecularMap = true;
	}

	public boolean hasSpecularMap() {
		return hasSpecularMap;
	}

	public int getSpecularMap() {
		return specularMap;
	}

	public int getTextureID() {
		return textureID;
	}

	public float getShineDamper() {
		return shineDamper;
	}

	public void setShineDamper(float shineDamper) {
		this.shineDamper = shineDamper;
	}

	public float getReflectivity() {
		return reflectivity;
	}

	public void setReflectivity(float reflectivity) {
		this.reflectivity = reflectivity;
	}

}
