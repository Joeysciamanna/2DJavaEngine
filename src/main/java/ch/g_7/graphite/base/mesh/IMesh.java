package ch.g_7.graphite.base.mesh;

import java.io.Closeable;
import java.util.List;

import org.joml.Vector3fc;

import ch.g_7.util.stuff.Initializable;


//TODO Pleas remake the Mesh System, its stupdi
public interface IMesh extends Initializable, Closeable{

	
	List<Vector3fc> getVertices();
	
	int getVerticesCount();
	
	Vector3fc getMaxPoint();
	
	Vector3fc getMinPoint();
	
	int getVaoId();
	
	
	@Override
    void close();



}
