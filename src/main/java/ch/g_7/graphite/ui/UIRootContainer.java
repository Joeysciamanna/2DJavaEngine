package ch.g_7.graphite.ui;

import java.util.concurrent.CompletableFuture;

import org.joml.Vector2i;
import org.joml.Vector2ic;

import ch.g_7.graphite.core.window.ResizeEvent;
import ch.g_7.graphite.core.window.Window;
import ch.g_7.graphite.ui.util.MouseManager;
import ch.g_7.graphite.ui.util.ScreenDimension;

public class UIRootContainer extends UIContainer implements IUIRootContainer{

	private final Window window;
	private final MouseManager mouseManager;
	
	public UIRootContainer(Window window) {
		this.window = window;
		this.window.addResizeListner(this);
		this.width.addPW(100);
		this.height.addPW(100);
		this.mouseManager = new MouseManager();
	}
	
	@Override
	public void recalculate() {
		recalculate(window.getSize(), window.getSize());
	}
	
	@Override
	protected void recalculateDimension(ScreenDimension dimension, Vector2ic screenSize) {
		dimension.recalculate(screenSize, screenSize);
	}
	
	@Override
	public void requestRecalculation(IUIContainer container) {
		container.recalculate(window.getSize(), window.getSize());
	}
	
	@Override
	public void onResize(ResizeEvent action) {
		CompletableFuture.runAsync(() -> {
			Vector2i size = new Vector2i(action.getWidth(), action.getHeight());
			recalculate(size, size);
		});
	}
	
	@Override
	protected void doInit() {
		recalculate();
		window.addMouseListner(mouseManager);
	}
	
	@Override
	protected void doClose() {
		window.removeResizeListner(this);
		super.doClose();
	}

	@Override
	public void add(IUIPanel panel) {
		super.add(panel);
	}
	
	@Override
	public void remove(IUIPanel panel) {
		super.remove(panel);
	}
	
	@Override
	public void clear() {
		super.clear();
	}
	
	@Override
	public Window getWindow() {
		return window;
	}

	@Override
	public IUIRootContainer getRootContainer() {
		return this;
	}

	public MouseManager getMouseManager() {
		return mouseManager;
	}


}