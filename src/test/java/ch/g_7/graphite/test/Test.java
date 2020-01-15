package ch.g_7.graphite.test;

import ch.g_7.graphite.base.mesh.MeshBuilder2d;
import ch.g_7.graphite.base.mesh.MeshFactory2d;
import ch.g_7.graphite.base.texture.Image;
import ch.g_7.graphite.base.texture.Sprite;
import ch.g_7.graphite.base.texture.TextureUtil;
import ch.g_7.graphite.base.view_model.ViewModel;
import ch.g_7.graphite.core.Application;
import ch.g_7.graphite.core.RenderType;
import ch.g_7.graphite.entity.Entity;
import ch.g_7.graphite.util.Color;
import ch.g_7.util.helper.AppInitializer;
import ch.g_7.util.task.SecureRunner;

public class Test extends Application {

	private static Test test;

	private Entity entity1;
	
	public Test() {
		super("Test");
	}
	
	public static void main(String[] args) {
		test = new Test();
		test.start();
	}
	
	@Override
	public void init() {
		AppInitializer appInitializer = new AppInitializer("", new Object() {});
		appInitializer.setDebugMode(true);
		appInitializer.initLogger();
		appInitializer.addConsoleLogWriters();


		Image square1 = new SecureRunner<Void, Image>(() -> TextureUtil.loadImage("C:\\Users\\Joey Sciamanna\\git\\Graphite\\src\\test\\resources\\textures\\square.png")).get();

//		Sprite sprite = GlyphFactoryProducer.getGlyphFactory().getSprite('a');
		Sprite sprite = TextureUtil.loadSprite(square1, 4, 4, 12, 12);
		
		ViewModel viewModel = new ViewModel();
		viewModel.setMesh(MeshFactory2d.getSquare(1).setCenter(MeshBuilder2d.CENTER_MIDDLE).build());
		viewModel.setTexture(sprite);
		getWindow().setBackgroundColor(Color.getColor(255, 0, 0));
		entity1 = new Entity();
		entity1.setViewModel(viewModel);
		//entity1.setPosition(new Vector3f(0, 0, 0));
		
		getDimension().addObj(entity1, RenderType.ENTITIES);

		
		getWindow().setVisible(true);
		getWindow().setSize(500, 500);
	}
}