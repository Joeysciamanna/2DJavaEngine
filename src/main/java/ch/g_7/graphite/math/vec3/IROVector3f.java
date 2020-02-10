package ch.g_7.graphite.math.vec3;

import ch.g_7.graphite.math.vec2.IVector2f;
import ch.g_7.graphite.math.vec2.IVector2i;

public interface IROVector3f {

	
	IVector3f add(IROVector3f vec, IVector3f des);
	
	IVector3f add(float x, float y, float z, IVector3f des);
	
	
	IVector3f sub(IROVector3f vec, IVector3f des);
	
	IVector3f sub(float x, float y, float z, IVector3f des);
	
	
	IVector3f mul(IROVector3f vec, IVector3f des);
	
	IVector3f mul(float x, float y, float z, IVector3f des);
	
	
	IVector3f div(IROVector3f vec, IVector3f des);
	
	IVector3f div(float x, float y, float z, IVector3f des);
	
	
	IVector3f max(IROVector3f vec, IVector3f des);
	
	IVector3f max(float x, float y, float z, IVector3f des);
	
	
	IVector3f min(IROVector3f vec, IVector3f des);
	
	IVector3f min(float x, float y, float z, IVector3f des);
	
	
	float dot(IROVector3f vec);
	
	float dot(float x, float y, float z);
	
	float angle(IROVector3f vec);
	
	float angle(float x, float y, float z);
	
	float distance(IROVector3f vec);
	
	float distance(float x, float y, float z);
			
	float distanceX(float x);
	
	float distanceY(float y);
	
	float distanceZ(float z);
	
	
	IVector3i transform3i();
	
	IVector2f transform2f();
	
	IVector2i transform2i();
	
	
	float getX();
	
	float getY();
	
	float getZ();
	
	
	IVector3f clone();
	
	boolean equals(Object obj);
	
	String toString();
	

}
