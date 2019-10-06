package ch.g_7.graphite.core;

import ch.g_7.graphite.base.entity.Camera;
import ch.g_7.graphite.rendering.Dimension;

public abstract class Application implements Runnable {

	private Dimension dimension;

	private final Window window;

	private Camera camera;

	private Thread thread;

	private boolean running;
	

	public Application(String name) {
		this.dimension = new Dimension();
		this.window = new Window(name, 200, 200);
		this.camera = new Camera();
	}

	public final void setRunning(boolean running) {
		if (running && !this.running) {
			this.running = true;
			String osName = System.getProperty("os.name");
			if (osName.contains("Mac")) {
				run();
			} else {
				thread = new Thread(this);
				thread.start();
			}
		} else if (!running && this.running) {
			this.running = false;
			thread = null;
		}

	}

	@Override
	public final void run() {
		try {
			window.init();
			init();
			while (running && !window.windowShouldClose()) {
				dimension.render(window, camera);
				window.update();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			close();
		}
	}
	
	protected void close(){}

	protected abstract void init();
	
	public Dimension getDimension() {
		return dimension;
	}
	
	public void setDimension(Dimension dimension) {
		this.dimension = dimension;
	}
	
	public Camera getCamera() {
		return camera;
	}
	
	public Window getWindow() {
		return window;
	}
	
}