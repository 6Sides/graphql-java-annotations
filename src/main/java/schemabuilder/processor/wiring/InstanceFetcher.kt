package schemabuilder.processor.wiring;

public interface InstanceFetcher {

    /**
     * Returns an instance of the provided class. If using a dependency injection
     * framework use it to return the instance. E.g. if using Spring,
     * return a bean of the specified class type.
     *
     * @param clazz The class to create an instance of
     * @return An instance of the provided class
     */
    Object getInstance(Class<?> clazz);

}
