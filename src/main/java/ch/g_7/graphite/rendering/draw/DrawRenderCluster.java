package ch.g_7.graphite.rendering.draw;

import ch.g_7.graphite.draw.DrawContext;
import ch.g_7.graphite.draw.drawable.Drawable;
import ch.g_7.graphite.rendering.RenderCluster;

public class DrawRenderCluster extends RenderCluster<Drawable, DrawRenderer>{

	public DrawRenderCluster() {
		super(new DrawRenderer(), "DRAW");
	}

	@Override
	public void update(float deltaMillis) {
		foreach((u)->u.update(deltaMillis));
	}
	
	@Override
	protected void doInit() {
		foreach((d)->d.initDrawContext(new DrawContext()));
		super.doInit();
	}
	
	@Override
	public void addNode(Drawable node) {
		node.initDrawContext(new DrawContext());
		super.addNode(node);
	}
}
