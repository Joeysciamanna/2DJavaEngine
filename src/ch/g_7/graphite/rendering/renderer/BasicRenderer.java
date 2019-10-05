package ch.g_7.graphite.rendering.renderer;

import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_INT;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glDrawElements;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL30.glBindVertexArray;

import org.joml.Matrix4f;
import org.joml.Vector3f;

import ch.g_7.graphite.base.object.AbstractGameEntity;
import ch.g_7.graphite.base.object.Camera;
import ch.g_7.graphite.core.Engine;
import ch.g_7.graphite.core.Window;
import ch.g_7.graphite.rendering.Dimension;
import ch.g_7.graphite.rendering.shaderprogram.BasicShaderProgram;
import ch.g_7.graphite.util.Pos3d;

public class BasicRenderer extends AbstractRenderer {
    
	private BasicShaderProgram shaderProgram;
 

	private Matrix4f viewMatrix;
	
	private Matrix4f modelViewMatrix;
	
	public BasicRenderer(BasicShaderProgram shaderProgram) {
		this.shaderProgram = shaderProgram;
		viewMatrix = new Matrix4f();
		modelViewMatrix = new Matrix4f();
	}

	@Override
	public void init(Engine engine) {
		shaderProgram.init();
	}

	@Override
	public void render(Window window, Camera camera, Dimension dimension) {
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

		shaderProgram.bind();
	    
	    viewMatrix.identity();
	    viewMatrix.translate((float)-camera.getPosition().getX(),(float) -camera.getPosition().getY(),(float)-camera.getPosition().getZ());
	        
		shaderProgram.setTextureSampler(0);
	    
		// Render each gameItem
		for (AbstractGameEntity object : dimension.getGameObjects()) {
			if (object.render()) {
				
				// Set model view matrix for this item
		    	Pos3d pos = object.getPosition();
		        modelViewMatrix.identity().translate(new Vector3f((float) pos.getX(),(float) pos.getY(),(float) pos.getZ()))
		        .scale(object.getScale());
		        Matrix4f viewCurr = new Matrix4f(viewMatrix);
		        modelViewMatrix = viewCurr.mul(modelViewMatrix);
				
			
				shaderProgram.setModelViewMatrix(modelViewMatrix);

				shaderProgram.setColor(object.getViewModel().getColor());
						
				if(object.getViewModel().getTexture() != null) {
					// Render the mes for this game item
					// Activate firs texture bank
					glActiveTexture(GL_TEXTURE0);
					// Bind the texture
					glBindTexture(GL_TEXTURE_2D, object.getViewModel().getTexture().getId());
				}
					// Draw the mesh
					glBindVertexArray(object.getViewModel().getMesh().getVaoId());
					glEnableVertexAttribArray(0);
					glEnableVertexAttribArray(1);
	
					glDrawElements(GL_TRIANGLES, object.getViewModel().getMesh().getVertexCount(), GL_UNSIGNED_INT, 0);
	

				
					if(object.getViewModel().getTexture() != null) {
						// Restore state
						glDisableVertexAttribArray(0);
						glDisableVertexAttribArray(1);
						glBindVertexArray(0);
					}
			}

		}

		shaderProgram.unbind();
	}

	@Override
	public void close() {
		shaderProgram.close();
	}

}