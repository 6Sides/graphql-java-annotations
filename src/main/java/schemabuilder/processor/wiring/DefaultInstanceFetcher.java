package schemabuilder.processor.wiring;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class DefaultInstanceFetcher implements InstanceFetcher {

    // TODO: Take appropriate action based on exception thrown
    @Override
    public Object getInstance(Class<?> clazz) {
        try {
            Constructor<?> constructor = clazz.getDeclaredConstructor();
            constructor.setAccessible(true);
            return constructor.newInstance();
        } catch(IllegalAccessException | InstantiationException | NoSuchMethodException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }
}
