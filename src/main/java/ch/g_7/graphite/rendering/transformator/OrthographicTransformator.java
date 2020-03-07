package ch.g_7.graphite.rendering.transformator;

import org.joml.Matrix4f;
import org.joml.Vector3f;

import ch.g_7.graphite.base.transformation.ITransform;
import ch.g_7.graphite.core.Camera;
import ch.g_7.graphite.core.window.Window;

public class OrthographicTransformator implements ITransformator<ITransform> {

	private Matrix4f projectionMatrix;
	private Matrix4f modelViewMatrix;

	public OrthographicTransformator() {
			projectionMatrix = new Matrix4f();
			modelViewMatrix = new Matrix4f();
		}

	@Override
	public Matrix4f getProjectionMatrix(Window window, Camera camera) {
		return projectionMatrix.identity().translate(-camera.getPosition().x(),
				-camera.getPosition().y(), -camera.getPosition().z());
	}

	@Override
	public Matrix4f getModelViewMatrix(ITransform transformation) {
		return modelViewMatrix.identity().translate(transformation.getPosition()).rotateXYZ(new Vector3f(transformation.getRotation()))
				.scale(transformation.getScale());
	}

}
