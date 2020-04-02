package schemabuilder.processor.wiring

import kotlin.reflect.KClass
import kotlin.reflect.full.createInstance

class DefaultInstanceFetcher : InstanceFetcher {

    override fun <T : Any> getInstance(clazz: KClass<T>): T {
        return clazz.createInstance()
    }
}