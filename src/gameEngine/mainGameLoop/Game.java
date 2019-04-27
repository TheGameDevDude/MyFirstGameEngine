package gameEngine.mainGameLoop;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.Display;

import gameEngine.collision.AABBManager;
import gameEngine.collision.Chunk;
import gameEngine.entities.Entity;
import gameEngine.entities.Light;
import gameEngine.entities.Player;
import gameEngine.graphics.Loader;
import gameEngine.graphics.MasterRenderer;
import gameEngine.math.Vector3f;
import gameEngine.model.ModelData;
import gameEngine.model.OBJFileLoader;
import gameEngine.model.RawModel;
import gameEngine.model.TexturedModel;
import gameEngine.postProcessing.Fbo;
import gameEngine.postProcessing.PostProcessing;
import gameEngine.textures.ModelTexture;

public class Game {
	private Player player;
	private TexturedModel spaceShip;
	private AABBManager aabbManager;
	private MasterRenderer renderer;

	private Fbo multisampleFbo = new Fbo(Display.getWidth(), Display.getHeight());
	private Fbo outputFbo = new Fbo(Display.getWidth(), Display.getHeight(), Fbo.DEPTH_TEXTURE);

	private List<Light> lights = new ArrayList<Light>();
	private List<Entity> entities = new ArrayList<Entity>();

	private static final String fileName = "src/gameEngine/saves/aabb.bin";
	private File aabbFile = new File(fileName);

	public Game(Loader loader) {
		aabbManager = new AABBManager(loader);

		if (!aabbFile.exists()) {
			try {
				ObjectOutputStream os = new ObjectOutputStream(new FileOutputStream(fileName));
				os.writeObject(aabbManager.chunks);
				os.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else if (aabbFile.exists()) {
			try {
				ObjectInputStream is = new ObjectInputStream(new FileInputStream(fileName));
				@SuppressWarnings("unchecked")
				List<Chunk> chunkFile = (List<Chunk>) is.readObject();
				aabbManager.chunks = chunkFile;
				is.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}

		player = new Player(loader, new Vector3f(5, 6, 10), aabbManager);
		renderer = new MasterRenderer(player);

		ModelData dataSpaceShip = OBJFileLoader.loadOBJ("/models/spaceshipCorridore");
		RawModel spaceShipModel = loader.loadToVAO(dataSpaceShip.getVertices(), dataSpaceShip.getTextureCoords(),
				dataSpaceShip.getNormals(), dataSpaceShip.getIndices());
		ModelTexture spaceShipTexture = new ModelTexture(loader.loadTexture("/textures/spaceText"));
		spaceShipTexture.setShineDamper(25);
		spaceShipTexture.setReflectivity(0.7f);
		spaceShip = new TexturedModel(spaceShipModel, spaceShipTexture);

		entities.add(new Entity(spaceShip, new Vector3f(5, 5, 20), 0, 0, 0, 4));
		entities.get(0).getModel().getTexture().setSpecularMap(loader.loadTexture("/textures/SpaceTextSpec"));
		PostProcessing.init(loader);
	}
	public void tick(Loader loader) {
		player.tick(aabbManager, loader);
		lights.clear();
		lights.add(new Light(new Vector3f(100, 100, 100), new Vector3f(1f, 0.7f, 0.7f)));

		try {
			ObjectOutputStream os = new ObjectOutputStream(new FileOutputStream(fileName));
			os.writeObject(aabbManager.chunks);
			os.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void render() {
		for (Entity e : entities) {
			renderer.processEntity(e);
		}
		player.render(renderer, aabbManager);

		multisampleFbo.bindFrameBuffer();
		renderer.render(lights, player);
		multisampleFbo.unbindFrameBuffer();
		multisampleFbo.resolveToFbo(outputFbo);
		PostProcessing.doPostProcessing(outputFbo.getColourTexture());
	}

	public void cleanUp() {
		PostProcessing.cleanUp();
		outputFbo.cleanUp();
		multisampleFbo.cleanUp();
		renderer.cleanUp();
	}

}
