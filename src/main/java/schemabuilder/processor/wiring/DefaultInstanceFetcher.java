package schemabuilder.processor.wiring;

public class DefaultInstanceFetcher implements InstanceFetcher {

    // TODO: Take appropriate action based on exception thrown
    @Override
    public Object getInstance(Class<?> clazz) {
        try {
            return clazz.newInstance();
        } catch(IllegalAccessException | InstantiationException e) {
            e.printStackTrace();
        }
        return null;
    }
}
