package ch.g_7.graphite.xjfx.injfx;

import ch.g_7.graphite.xjfx.injfx.input.JfxKeyInput;
import ch.g_7.graphite.xjfx.injfx.input.JfxMouseInput;
import com.jme3.input.JoyInput;
import com.jme3.input.TouchInput;
import com.jme3.opencl.Context;
import com.jme3.renderer.Renderer;
import com.jme3.system.AppSettings;
import com.jme3.system.JmeContext;
import com.jme3.system.SystemListener;
import com.jme3.system.Timer;

import java.util.Objects;

/**
 * The implementation of the {@link JmeContext} for integrating to JavaFX.
 *
 * @author empirephoenix
 */
public class JmeOffscreenSurfaceContext implements JmeContext {

    /**
     * The settings.
     */
    protected final AppSettings settings;

    /**
     * The key input.
     */
    protected final JfxKeyInput keyInput;

    /**
     * The mouse input.
     */
    protected final JfxMouseInput mouseInput;

    /**
     * The current width.
     */
    private volatile int width;

    /**
     * The current height.
     */
    private volatile int height;

    /**
     * The background context.
     */
    protected JmeContext backgroundContext;

    public JmeOffscreenSurfaceContext() {
        this.keyInput = new JfxKeyInput(this);
        this.mouseInput = new JfxMouseInput(this);
        this.settings = createSettings();
        this.backgroundContext = createBackgroundContext();
        this.height = 1;
        this.width = 1;
    }

    /**
     * Gets the current height.
     *
     * @return the current height.
     */
    public int getHeight() {
        return height;
    }

    /**
     * Sets the current height.
     *
     * @param height the current height.
     */
    public void setHeight(final int height) {
        this.height = height;
    }

    /**
     * Gets the current width.
     *
     * @return the current width.
     */
    public int getWidth() {
        return width;
    }

    /**
     * Sets the current width.
     *
     * @param width the current width.
     */
    public void setWidth(final int width) {
        this.width = width;
    }

    /**
     * Creates a new app settings.
     *
     * @return the new app settings.
     */
    protected AppSettings createSettings() {
        AppSettings settings = new AppSettings(true);
        settings.setRenderer(AppSettings.LWJGL_OPENGL3);
        return settings;
    }

    /**
     * Creates a new background jme context.
     *
     * @return the new background jme context.
     */
    protected JmeContext createBackgroundContext() {
        return new JmeBackgroundContext(settings);
    }

    /**
     * Gets the current background context.
     *
     * @return the current background context.
     */
    protected JmeContext getBackgroundContext() {
        return backgroundContext;
    }

    @Override
    public Type getType() {
        return Type.OffscreenSurface;
    }

    @Override
    public void setSettings(AppSettings settings) {
        this.settings.copyFrom(settings);
        this.settings.setRenderer(AppSettings.LWJGL_OPENGL3);

        Objects.requireNonNull(getBackgroundContext())
                .setSettings(settings);
    }

    @Override
    public void setSystemListener(SystemListener listener) {
        Objects.requireNonNull(getBackgroundContext())
                .setSystemListener(listener);
    }

    @Override
    public AppSettings getSettings() {
        return settings;
    }

    @Override
    public Renderer getRenderer() {
        return Objects.requireNonNull(getBackgroundContext())
                .getRenderer();
    }

    @Override
    public Context getOpenCLContext() {
        return null;
    }

    @Override
    public JfxMouseInput getMouseInput() {
        return mouseInput;
    }

    @Override
    public JfxKeyInput getKeyInput() {
        return keyInput;
    }

    @Override
    public JoyInput getJoyInput() {
        return null;
    }

    @Override
    public TouchInput getTouchInput() {
        return null;
    }

    @Override
    public Timer getTimer() {
        return Objects.requireNonNull(getBackgroundContext())
                .getTimer();
    }

    @Override
    public void setTitle(String title) {
    }

    @Override
    public boolean isCreated() {
        return backgroundContext != null && backgroundContext.isCreated();
    }

    @Override
    public boolean isRenderable() {
        return backgroundContext != null && backgroundContext.isRenderable();
    }

    @Override
    public void setAutoFlushFrames(boolean enabled) {
        // TODO Auto-generated method stub
    }

    @Override
    public void create(boolean waitFor) {
        String render = System.getProperty("jfx.background.render", AppSettings.LWJGL_OPENGL3);
        JmeContext backgroundContext = Objects.requireNonNull(getBackgroundContext());
        backgroundContext.getSettings().setRenderer(render);
        backgroundContext.create(waitFor);
    }

    @Override
    public void restart() {
    }

    @Override
    public void destroy(boolean waitFor) {

        if (backgroundContext == null) {
            throw new IllegalStateException("Not created");
        }

        // destroy wrapped context
        backgroundContext.destroy(waitFor);
    }
}