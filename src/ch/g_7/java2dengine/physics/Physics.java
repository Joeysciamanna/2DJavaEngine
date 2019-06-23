package ch.g_7.java2dengine.physics;

import ch.g_7.java2dengine.core.Engine;
import ch.g_7.java2dengine.core.Initializable;
import ch.g_7.java2dengine.process.ProcessIntervalBuffer;

public class Physics implements Initializable{

	private PhysicsCalculator physicsCalculator;
	private ColliderClasses colliderClasses;
	private boolean enabled = true;
	private ProcessIntervalBuffer<Engine> physicsCalculationProccess;
	
	public Physics() {
		physicsCalculator = new PhysicsCalculator();
		colliderClasses = new ColliderClasses();
		physicsCalculationProccess = new ProcessIntervalBuffer<Engine>(physicsCalculator, 20);
	}
	
	public ColliderClasses getColliderClasses() {
		return colliderClasses;
	}
	
	public PhysicsCalculator getPhysicsCalculator() {
		return physicsCalculator;
	}
	
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
	
	public boolean isEnabled() {
		return enabled;
	}

	@Override
	public void init(Engine engine) {
		engine.getGameLoop().addProcessInterval(physicsCalculationProccess);
	}
	
	public void setUpdateIntervall(long intervall) {
		physicsCalculationProccess.setIntervall(intervall);
	}
	
	public void setCallBuffer(long callBuffer) {
		physicsCalculationProccess.setCallBuffer(callBuffer);
	}
}
