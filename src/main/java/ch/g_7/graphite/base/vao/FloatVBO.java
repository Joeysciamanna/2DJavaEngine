package ch.g_7.graphite.base.vao;

import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL15.glBufferData;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;

import java.nio.FloatBuffer;

import org.lwjgl.opengl.GL11;
import org.lwjgl.system.MemoryUtil;

public class FloatVBO extends VBO {
	
	protected float[] floats;
	
	public FloatVBO(VBOType type, float[] floats) {
		super(type);
		this.floats = floats;
		if(type.glNumber != GL11.GL_FLOAT) {
			throw new IllegalArgumentException("VBOType " + type.toString() + " is not for float");
		}
	}

	@Override
	protected void init(VAO vao) {
		FloatBuffer indicesBuffer = MemoryUtil.memAllocFloat(floats.length);
		indicesBuffer.put(floats).flip();
		glBindBuffer(type.glBufferTarget, getId());
		glBufferData(type.glBufferTarget, indicesBuffer, GL_STATIC_DRAW);
		glVertexAttribPointer(vao.nextIndex(), type.size, type.glNumber , false, 0, 0);
		floats = null;
	}


}