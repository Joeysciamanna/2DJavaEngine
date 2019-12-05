package ch.g_7.graphite.rendering.renderer;

import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_INT;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glDrawElements;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL30.glBindVertexArray;

import java.util.List;

import org.joml.Matrix4f;

import ch.g_7.graphite.core.Camera;
import ch.g_7.graphite.core.window.Window;
import ch.g_7.graphite.entity.object.IObject;
import ch.g_7.graphite.rendering.Dimension;
import ch.g_7.graphite.rendering.shaderprogram.BasicShaderProgram;

public class Basic2dRenderer implements IRenderer<IObject> {

	private BasicShaderProgram shaderProgram;

	private Matrix4f viewMatrix;

	private Matrix4f modelViewMatrix;

	public Basic2dRenderer() {
		shaderProgram = new BasicShaderProgram();
		viewMatrix = new Matrix4f();
		modelViewMatrix = new Matrix4f();
	}

	@Override
	public void init() {
		shaderProgram.init();
	}

	@Override
	public void render(List<IObject> renderables, Dimension dimension, Window window, Camera camera) {

		
		shaderProgram.bind();

		viewMatrix.identity();
		viewMatrix.translate((float) -camera.getPosition().x(), (float) -camera.getPosition().y(),
				(float) -camera.getPosition().z());

		shaderProgram.setTextureSampler(0);

		// Render each gameItem
		for (IObject object : renderables) {

			// Set model view matrix for this item
			Matrix4f viewCurr = new Matrix4f(viewMatrix);
			modelViewMatrix = viewCurr.mul(object.getModelViewMatrix());

			shaderProgram.setModelViewMatrix(modelViewMatrix);

			shaderProgram.setColor(object.getViewModel().getColor());

			if (object.getViewModel().getTexture() != null) {
				// Render the mes for this game item
				// Activate firs texture bank
				glActiveTexture(GL_TEXTURE0);
				// Bind the texture
				glBindTexture(GL_TEXTURE_2D, object.getViewModel().getTexture().getId());
				
				shaderProgram.setTextureEnabled(true);
			}else {
				shaderProgram.setTextureEnabled(false);
			}
			// Draw the mesh
			glBindVertexArray(object.getViewModel().getMesh().getVaoId());
			glEnableVertexAttribArray(0);
			glEnableVertexAttribArray(1);

			glDrawElements(GL_TRIANGLES, object.getViewModel().getMesh().getVertexCount(), GL_UNSIGNED_INT, 0);

			glDisableVertexAttribArray(0);
			glDisableVertexAttribArray(1);
			glBindVertexArray(0);

		}

		shaderProgram.unbind();
	}

	@Override
	public void close() {
		shaderProgram.close();
	}

}