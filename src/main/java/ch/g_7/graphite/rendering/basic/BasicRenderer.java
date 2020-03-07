package ch.g_7.graphite.rendering.basic;

import static org.lwjgl.opengl.GL11.GL_UNSIGNED_INT;
import static org.lwjgl.opengl.GL11.glDrawElements;

import java.util.List;

import ch.g_7.graphite.base.transformation.ITransform;
import ch.g_7.graphite.base.view_model.IViewModel;
import ch.g_7.graphite.core.Camera;
import ch.g_7.graphite.core.window.Window;
import ch.g_7.graphite.node.INode;
import ch.g_7.graphite.node.Renderable;
import ch.g_7.graphite.rendering.IRenderer;
import ch.g_7.graphite.rendering.transformator.ITransformator;

public abstract class BasicRenderer<T extends INode> implements IRenderer<T> {

	private ITransformator<ITransform> transformator;
	protected final BasicShaderProgram shaderProgram;

	public BasicRenderer(BasicShaderProgram shaderProgram, ITransformator<ITransform> transformator) {
		this.shaderProgram = shaderProgram;
		this.transformator = transformator;
	}

	@Override
	public final void render(List<? extends T> nodes, Window window, Camera camera) {
		shaderProgram.bind();
		shaderProgram.setTextureSampler(0);
		shaderProgram.setProjectionMatrix(transformator.getProjectionMatrix(window, camera));
		render(nodes);
		shaderProgram.unbind();
	}

	protected abstract void render(List<? extends T> nodes);

	protected <R extends Renderable> void render(R renderable, int glDrawMethod) {

		IViewModel viewModel = renderable.getViewModel();

		shaderProgram.setModelViewMatrix(transformator.getModelViewMatrix(renderable.getTransformation()));
		shaderProgram.setColor(viewModel.getColor());

		viewModel.bind();
		if (viewModel.getTexture() != null) {
			shaderProgram.setTextureEnabled(true);
		} else {
			shaderProgram.setTextureEnabled(false);
		}

		glDrawElements(glDrawMethod, viewModel.getMesh().getVerticesCount(), GL_UNSIGNED_INT, 0);
		viewModel.unbind();
	}


	public void setTransformator(ITransformator<ITransform> transformator) {
		this.transformator = transformator;
	}

	@Override
	public void init() {
		shaderProgram.init();
	}

	@Override
	public void close() {
		shaderProgram.close();
	}
}
