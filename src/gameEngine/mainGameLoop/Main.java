package gameEngine.mainGameLoop;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.ContextAttribs;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.PixelFormat;

import gameEngine.graphics.Loader;

public class Main {
	public final static int WIDTH = 1280;
	public final static int HEIGHT = 720;
	static final int FPS_CAP = 120;

	private Loader loader = new Loader();
	private Game game;

	private static float deltaTime;

	public Main() {

		ContextAttribs attribs = new ContextAttribs(3, 3).withForwardCompatible(true).withProfileCore(true);
		try {
			Display.setDisplayMode(new DisplayMode(WIDTH, HEIGHT));
			Display.setTitle("game");
			Display.setFullscreen(true);
			Display.setResizable(true);
			Display.create(new PixelFormat().withDepthBits(24), attribs);
			GL11.glEnable(GL13.GL_MULTISAMPLE);
		} catch (LWJGLException e) {
			e.printStackTrace();
		}
		prepare();
	}

	private void prepare() {
		game = new Game(loader);
	}

	public void run() {
		long lastTime = System.nanoTime();
		final double ns = 1000000000.0 / 60.0;
		double delta = 0;
		while (!Display.isCloseRequested()) {
			long now = System.nanoTime();
			delta = (now - lastTime) / ns;
			deltaTime = (float) delta;
			lastTime = now;
			tick();
			render();
			Display.sync(FPS_CAP);
			Display.update();
		}

		loader.cleanUP();
		game.cleanUp();
		Display.destroy();
	}

	private void tick() {
		game.tick(loader);
	}

	public static float getFrameTime() {
		return deltaTime;
	}

	private void render() {
		game.render();
	}

	public static void main(String[] args) {
		Main game = new Main();
		game.run();
	}
}