package ch.g_7.graphite.draw;

import java.util.ArrayList;
import java.util.List;

import ch.g_7.graphite.base.mesh.IMesh;
import ch.g_7.graphite.base.mesh.MeshBuilder2d;
import org.joml.Vector2fc;
import org.joml.Vector3f;
import org.lwjgl.opengl.GL11;

import ch.g_7.graphite.base.mesh.Mesh;
import ch.g_7.graphite.base.mesh.MeshFactory2d;
import ch.g_7.graphite.base.texture.ITexture;
import ch.g_7.graphite.util.Color;
import ch.g_7.util.resource.Resource;

public class DrawContext2d extends Resource {

	private final static IMesh SQUARE_MESH = MeshFactory2d.getSquare(1).setCenter(MeshBuilder2d.CENTER_TOP_LEFT).build();


	private List<IDrawObject> drawObjects;
	private DrawObject next;

	public DrawContext2d() {
		this.drawObjects = new ArrayList<>();
		next = new DrawObject();
		next.bind(this);
	}

	public void setBrushColor(Color color) {
		next.getViewModel().setColor(color);
	}

	public void setBrushTexture(ITexture texture) {
		next.getViewModel().setTexture(texture);
	}

	public void addLine(Vector2fc from, Vector2fc to) {
		next.getViewModel().setMesh(new Mesh(new float[] { from.x(), from.y(), 0, to.x(), to.y(), 0 },
				new int[] { 0, 1 }, new float[] { 0, 0, 1, 1 }));
		next.setGLDrawMethod(GL11.GL_LINES);
		next();
	}

	public void addTexture(ITexture texture, Vector2fc position, Vector2fc size) {
		setBrushTexture(texture);
		addRectangle(position, size);
		clean();
	}

	public void addRectangle(Vector2fc position, Vector2fc size) {
		next.getViewModel().setMesh(SQUARE_MESH);
		next.getTransformation().setScale(new Vector3f(size.x(), size.y(),0));
		next.getTransformation().setPosition(new Vector3f(position.x(), position.y(), 0));
		next.setGLDrawMethod(GL11.GL_TRIANGLES);
		next();
	}

	private void next() {
		if (!next.isEmpty()) {
			add(next);
			next = new DrawObject(next);
			next.bind(this);
		}

	}

	@Override
	protected void doInit() {
		SQUARE_MESH.bind(this);
	}

	@Override
	protected void doClose() {
		SQUARE_MESH.unbind(this);
		for (IDrawObject drawObject : drawObjects) {
			unbindFrom(drawObject);
		}
		unbindFrom(next);
	}

	public void clean(){
		next.clean();
	}

	public void clear() {
		for (IDrawObject drawObject : drawObjects) {
			drawObject.unbind(this);
		}
		drawObjects.clear();
	}

	public void add(IDrawObject entity) {
		entity.bind(this);
		drawObjects.add(entity);
	}

	public List<IDrawObject> getDrawObjects() {
		return drawObjects;
	}
}
