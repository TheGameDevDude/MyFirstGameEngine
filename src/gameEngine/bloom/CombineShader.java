package gameEngine.bloom;

import gameEngine.shaders.ShaderProgram;

public class CombineShader extends ShaderProgram {

	private static final String VERTEX_FILE = "src/gameEngine/bloom/simpleVertex.txt";
	private static final String FRAGMENT_FILE = "src/gameEngine/bloom/combineFragment.txt";

	private int location_colourTexture;
	private int location_highlightTexture;
	private int location_highlightTexture1;
	private int location_highlightTexture2;

	protected CombineShader() {
		super(VERTEX_FILE, FRAGMENT_FILE);
	}

	@Override
	protected void getAllUniformLocations() {
		location_colourTexture = super.getUniformLocation("colourTexture");
		location_highlightTexture = super.getUniformLocation("highlightTexture");
		location_highlightTexture1 = super.getUniformLocation("highlightTexture1");
		location_highlightTexture2 = super.getUniformLocation("highlightTexture2");
	}

	protected void connectTextureUnits() {
		super.loadInt(location_colourTexture, 0);
		super.loadInt(location_highlightTexture, 1);
		super.loadInt(location_highlightTexture1, 2);
		super.loadInt(location_highlightTexture2, 3);
	}

	@Override
	protected void bindAttributes() {
		super.bindAttribute(0, "position");
	}

}
