package ch.g_7.graphite.base.ui;

import java.util.List;

import org.joml.Vector2d;
import org.joml.Vector2f;

import ch.g_7.graphite.base.mesh.Mesh;
import ch.g_7.graphite.base.mesh.MeshBuilder;
import ch.g_7.graphite.base.mesh.MeshFactory;
import ch.g_7.graphite.base.texture.Texture;
import ch.g_7.graphite.util.Color;

public class UIPanel implements IUIPanel{

	
	private static final Mesh SQUARE_MESH = MeshFactory.getSquare(1).setCenter(MeshBuilder.CENTER_TOP_LEFT).build();
	
	private Vector2f position;
	private Vector2f size;
	private Color color;
	private Texture texture;
	
	private UIPanel fatherPanel;
	private List<UIPanel> childPanels;
	
	
	public UIPanel(Vector2f position, Vector2f size) {
		this.position = position;
		this.size = size;
		this.color = new Color(255, 0, 0, 0);
	}
	
	public void add(UIPanel uiPanel) {
		childPanels.add(uiPanel);
		uiPanel.fatherPanel = this;
	}

	@Override
	public Mesh getMesh() {
		return SQUARE_MESH;
	}

	@Override
	public Vector2f getSize() {
		return size;
	}
	
	public void setSize(Vector2f size) {
		this.size = size;
	}

	@Override
	public Vector2f getPosition() {
		if(fatherPanel != null) {
			return position.add(fatherPanel.getPosition());
		}
		return position;
	}
	
	public void setPosition(Vector2f position) {
		this.position = position;
	}

	@Override
	public Color getColor() {
		return color;
	}

	@Override
	public Texture getTexture() {
		return texture;
	}
	
	
	


}