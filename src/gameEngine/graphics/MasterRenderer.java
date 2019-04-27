package gameEngine.graphics;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import gameEngine.entities.Camera;
import gameEngine.entities.Entity;
import gameEngine.entities.Light;
import gameEngine.model.TexturedModel;
import gameEngine.shaders.StaticShader;

public class MasterRenderer {
	private StaticShader shader = new StaticShader();
	private Renderer renderer = new Renderer(shader);


	public static float FOV = Renderer.FOV;
	public static float NEAR_PLANE = Renderer.NEAR_PLANE;
	public static float FAR_PLANE = Renderer.FAR_PLANE;

	private Map<TexturedModel, List<Entity>> entities = new HashMap<TexturedModel, List<Entity>>();

	public MasterRenderer(Camera cam) {

	}

	public void render(List<Light> lights, Camera camera) {
		renderer.prepare();
		shader.start();
		shader.loadLights(lights);
		shader.loadViewMatrix(camera);
		renderer.render(entities);
		shader.stop();
		entities.clear();
	}

	public void processEntity(Entity entity) {
		TexturedModel entityModel = entity.getModel();
		List<Entity> batch = entities.get(entityModel);
		if (batch != null) {
			batch.add(entity);
		} else {
			List<Entity> newBatch = new ArrayList<Entity>();
			newBatch.add(entity);
			entities.put(entityModel, newBatch);
		}
	}



	public void cleanUp() {
		shader.cleanUp();

	}

}
