package ch.g_7.graphite.resource;

public interface IResourceProvider<T extends IResource, K extends IResourceKey> {

    boolean canProvide(IResourceKey resourceKey);

    @Deprecated
    String getName();

    T get(K resourceKey);

    void closeResources();

    IResourceProvider<T, K> newInstance();
}
