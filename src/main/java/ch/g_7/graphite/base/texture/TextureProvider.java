package ch.g_7.graphite.base.texture;

import ch.g_7.graphite.resource.BasicResourceProvider;
import ch.g_7.graphite.resource.IResourceKey;
import ch.g_7.graphite.resource.IResourceProvider;
import ch.g_7.util.io.IFileLoader;

import java.io.IOException;
import java.io.InputStream;


public class TextureProvider extends BasicResourceProvider<ITexture, TextureKey> {

    @Override
    protected ITexture loadResource(TextureKey resourceKey, IFileLoader fileLoader) throws IllegalArgumentException {
    	return loadTexture(fileLoader.loadFile(resourceKey.getName()));
    }

    private Texture loadTexture(InputStream inputStream) {
        Texture image = null;
        try {
            image = TextureLoader.loadImage(inputStream);
        } catch (IOException e) {
            throw new RuntimeException("Exception at loading Texture", e);
        }
        return image;
    }

    @Override
    public boolean canProvide(IResourceKey resourceKey) {
    	return containsResourceNames(resourceKey, TextureKey.NAME);
    }

    @Override
    public IResourceProvider<ITexture, TextureKey> newInstance() {
        return new TextureProvider();
    }

}
