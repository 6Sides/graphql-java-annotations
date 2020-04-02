package schemabuilder.processor.wiring

class DefaultInstanceFetcher : InstanceFetcher {

    override fun getInstance(clazz: Class<*>): Any {
        return clazz.getDeclaredConstructor().run {
            isAccessible = true
            newInstance()
        }
    }
}