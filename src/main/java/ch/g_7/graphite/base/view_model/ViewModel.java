package ch.g_7.graphite.base.view_model;

import ch.g_7.graphite.base.mesh.IMesh;
import ch.g_7.graphite.base.mesh.MeshProvider;
import ch.g_7.graphite.base.mesh.vao.VAO;
import ch.g_7.graphite.base.texture.ITexture;
import ch.g_7.graphite.base.texture.Image;
import ch.g_7.graphite.base.texture.Sprite;
import ch.g_7.graphite.resource.IResource;
import ch.g_7.graphite.resource.ResourceManager;
import ch.g_7.graphite.util.Color;
import ch.g_7.util.common.Closeable;

import java.util.Objects;
import java.util.UUID;


public class ViewModel implements IViewModel, IResource {

	private final VAO vao;
	private IMesh mesh;
	private ITexture texture;
	private Color color;

	@Deprecated
	ViewModel(IMesh mesh, ITexture texture, Color color) {
		this.vao = new VAO();
		setMesh(mesh);
		setTexture(texture);
		setColor(color);
		ResourceManager.getActive().getResourceProvdier(MeshProvider.NAME, ViewModelProvider.class).register(this, UUID.randomUUID().toString());
		init();
	}

	public ViewModel(IMesh mesh, ITexture texture) {
		this(mesh, texture, Color.TRANSPARENT);
	}

	public ViewModel(IMesh mesh, Color color) {
		this(mesh, null, color);
	}

	@Override
	public ViewModel clone() {
		return new ViewModel(mesh, texture, color);
	}


	@Override
	public void bind() {
		if (texture != null)
			texture.bind();
		vao.bind();
	}

	@Override
	public void unbind() {
		if (texture != null)
			texture.unbind();
		vao.unbind();
	}

	public ITexture getTexture() {
		return texture;
	}

	public void setTexture(ITexture texture) {
		this.texture = texture;
		if (texture != null)
			this.vao.replace(texture.getTextureCoordinatesVBO());
	}
	
	public Color getColor() {
		return color;
	}
	
	public void setColor(Color color) {
		Objects.requireNonNull(color);
		this.color = color;
	}

	public IMesh getMesh() {
		return mesh;
	}

	public void setMesh(IMesh mesh) {
		this.mesh = mesh;
		this.vao.replace(mesh.getPositionVBO());
	}


	@Override
	public void close() {
		vao.close();
	}

	@Override
	public void init() { }
}
