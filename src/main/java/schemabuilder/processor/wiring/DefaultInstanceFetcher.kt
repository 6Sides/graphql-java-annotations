package schemabuilder.processor.wiring

class DefaultInstanceFetcher : InstanceFetcher {

    override fun <T> getInstance(clazz: Class<T>): T {
        return clazz.getDeclaredConstructor().run {
            isAccessible = true
            newInstance()
        }
    }
}