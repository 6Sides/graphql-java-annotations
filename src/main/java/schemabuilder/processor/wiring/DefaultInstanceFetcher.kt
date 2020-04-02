package schemabuilder.processor.wiring

import java.lang.reflect.InvocationTargetException

class DefaultInstanceFetcher : InstanceFetcher {
    // TODO: Take appropriate action based on exception thrown
    override fun getInstance(clazz: Class<*>?): Any? {
        try {
            val constructor = clazz?.getDeclaredConstructor()
            constructor?.isAccessible = true
            return constructor?.newInstance()
        } catch (e: InstantiationException) {
            e.printStackTrace()
        } catch (e: NoSuchMethodException) {
            e.printStackTrace()
        } catch (e: InvocationTargetException) {
            e.printStackTrace()
        }
        return null
    }
}