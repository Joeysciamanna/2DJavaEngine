package ch.g_7.graphite.core.window;

import static org.lwjgl.glfw.GLFW.GLFW_CONTEXT_VERSION_MAJOR;
import static org.lwjgl.glfw.GLFW.GLFW_CONTEXT_VERSION_MINOR;
import static org.lwjgl.glfw.GLFW.GLFW_OPENGL_CORE_PROFILE;
import static org.lwjgl.glfw.GLFW.GLFW_OPENGL_FORWARD_COMPAT;
import static org.lwjgl.glfw.GLFW.GLFW_OPENGL_PROFILE;
import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.GLFW_RESIZABLE;
import static org.lwjgl.glfw.GLFW.GLFW_VISIBLE;
import static org.lwjgl.glfw.GLFW.glfwCreateWindow;
import static org.lwjgl.glfw.GLFW.glfwDefaultWindowHints;
import static org.lwjgl.glfw.GLFW.glfwGetCursorPos;
import static org.lwjgl.glfw.GLFW.glfwGetKey;
import static org.lwjgl.glfw.GLFW.glfwGetPrimaryMonitor;
import static org.lwjgl.glfw.GLFW.glfwGetVideoMode;
import static org.lwjgl.glfw.GLFW.glfwHideWindow;
import static org.lwjgl.glfw.GLFW.glfwInit;
import static org.lwjgl.glfw.GLFW.glfwMakeContextCurrent;
import static org.lwjgl.glfw.GLFW.glfwPollEvents;
import static org.lwjgl.glfw.GLFW.glfwSetFramebufferSizeCallback;
import static org.lwjgl.glfw.GLFW.glfwSetKeyCallback;
import static org.lwjgl.glfw.GLFW.glfwSetMouseButtonCallback;
import static org.lwjgl.glfw.GLFW.glfwSetWindowPos;
import static org.lwjgl.glfw.GLFW.glfwSetWindowPosCallback;
import static org.lwjgl.glfw.GLFW.glfwShowWindow;
import static org.lwjgl.glfw.GLFW.glfwSwapBuffers;
import static org.lwjgl.glfw.GLFW.glfwWindowHint;
import static org.lwjgl.glfw.GLFW.glfwWindowShouldClose;
import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_CULL_FACE;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_TEST;
import static org.lwjgl.opengl.GL11.GL_FALSE;
import static org.lwjgl.opengl.GL11.GL_ONE_MINUS_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_TRUE;
import static org.lwjgl.opengl.GL11.glBlendFunc;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glViewport;
import static org.lwjgl.system.MemoryUtil.NULL;

import java.nio.DoubleBuffer;
import java.util.ArrayList;
import java.util.List;

import org.joml.Vector2i;
import org.joml.Vector2ic;
import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;

import ch.g_7.graphite.util.Color;
import ch.g_7.util.stuff.Initializable;
import ch.g_7.util.task.TaskInputBuffer;
import ch.g_7.util.task.ValueChangeNotifier;

public class Window implements Initializable, ResizeListner {

	private final String title;

	private List<KeyListner> keyListners;
	private TaskInputBuffer<KeyEvent> keyPressBuffer;

	private List<MouseListner> mouseListners;
	private TaskInputBuffer<MouseEvent> mouseClickBuffer;

	private long windowId;

	private int width;
	private int height;
	private ValueChangeNotifier<ResizeEvent> resizeNotifier;

	private int x;
	private int y;
	private boolean repositioned;

	public Window(String title, int width, int height) {
		this.title = title;
		this.width = width;
		this.height = height;
		keyListners = new ArrayList<>();
		keyPressBuffer = new TaskInputBuffer<KeyEvent>((i) -> keyListners.forEach((l) -> l.onKeyPress(i)));
		mouseListners = new ArrayList<>();
		mouseClickBuffer = new TaskInputBuffer<MouseEvent>((i) -> mouseListners.forEach((l) -> l.onMouseClick(i)));
		resizeNotifier = new ValueChangeNotifier<>();
		resizeNotifier.addListner(this);
	}

	static {
		GLFWErrorCallback.createPrint(System.err).set();

		if (!glfwInit()) {
			throw new IllegalStateException("Unable to initialize GLFW");
		}

		glfwDefaultWindowHints(); // optional, the current window hints are already the default
		glfwWindowHint(GLFW_VISIBLE, GL_FALSE); // the window will stay hidden after creation
		glfwWindowHint(GLFW_RESIZABLE, GL_TRUE); // the window will be resizable
		glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
		glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 2);
		glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
		glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GL_TRUE);
	}

	@Override
	public void init() {
		windowId = glfwCreateWindow(width, height, title, NULL, NULL);
		if (windowId == NULL) {
			throw new RuntimeException("Failed to create the GLFW window");
		}

		glfwSetFramebufferSizeCallback(windowId,
				(window, width, height) -> resizeNotifier.valueChanged(new ResizeEvent(window, width, height)));

		glfwSetWindowPosCallback(windowId, (window, x, y) -> setPosition(x, y));

		glfwSetKeyCallback(windowId, (window, key, scancode, action, mods) -> keyPressBuffer
				.add(new KeyEvent(window, key, scancode, action, mods)));

		glfwSetMouseButtonCallback(windowId, (long window, int button, int action, int mods) -> {
			DoubleBuffer x = BufferUtils.createDoubleBuffer(1);
			DoubleBuffer y = BufferUtils.createDoubleBuffer(1);
			glfwGetCursorPos(windowId, x, y);
			mouseClickBuffer.add(new MouseEvent(window, button, action, mods, (int) x.get(), (int) y.get()));
		});

		glfwMakeContextCurrent(windowId);

		GL.createCapabilities();
		glEnable(GL_DEPTH_TEST);

		glDisable(GL_CULL_FACE);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		glEnable(GL_BLEND);

		setSize(width, height);

		GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
		setPosition((vidmode.width() - width) / 2, (vidmode.height() - height) / 2);

	}

	public void update() {
		resizeNotifier.runSimple();
		reposition();

		glfwSwapBuffers(windowId);
		glfwPollEvents();
		
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
	}

	public void pullEvents() {
		keyPressBuffer.runSimple();
		mouseClickBuffer.runSimple();
	}

	@Override
	public void onResize(ResizeEvent action) {
		this.width = action.getWidth();
		this.height = action.getHeight();
		glViewport(0, 0, width, height);
		GLFW.glfwSetWindowSize(windowId, width, height);
	}

	public void setSize(int width, int height) {
		resizeNotifier.valueChanged(new ResizeEvent(windowId, width, height));
	}

	public void reposition() {
		if (repositioned) {
			glfwSetWindowPos(windowId, x, y);
		}
	}

	public void setPosition(int x, int y) {
		this.x = x;
		this.y = y;
		this.repositioned = true;
	}

	public void setVisible(boolean visible) {
		if (visible) {
			glfwShowWindow(windowId);
		} else {
			glfwHideWindow(windowId);
		}
	}

	public void setBackgroundColor(Color color) {
		glClearColor(color.getR(), color.getG(), color.getB(), color.getA());
	}

	public long getWindowId() {
		return windowId;
	}

	public void addKeyListner(KeyListner keyListner) {
		keyListners.add(keyListner);
	}

	public void removeKeyListner(KeyListner keyListner) {
		keyListners.remove(keyListner);
	}
	
	public void addMouseListner(MouseListner mouseListner) {
		mouseListners.add(mouseListner);
	}

	public void removeMouseListner(MouseListner mouseListner) {
		mouseListners.remove(mouseListner);
	}

	public void addResizeListner(ResizeListner resizeListner) {
		resizeNotifier.addListner(resizeListner);
	}

	public void removeResizeListner(ResizeListner resizeListner) {
		resizeNotifier.removeListner(resizeListner);
	}

	public boolean isKeyPressed(int keyCode) {
		return glfwGetKey(windowId, keyCode) == GLFW_PRESS;
	}

	public boolean windowShouldClose() {
		return glfwWindowShouldClose(windowId);
	}

	public String getTitle() {
		return title;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public Vector2ic getSize() {
		return new Vector2i(width, height);
	}

}
