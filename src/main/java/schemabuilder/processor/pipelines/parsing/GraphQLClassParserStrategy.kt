package schemabuilder.processor.pipelines.parsing

import schemabuilder.processor.wiring.InstanceFetcher
import kotlin.reflect.KClass

interface GraphQLClassParserStrategy {

    fun parse(clazz: Class<*>, fetcher: InstanceFetcher)

    /**
     * Precondition logic to determine if the class should be passed
     * to the parse function.
     *
     * @return true if the class should be parsed, false otherwise.
     */
    fun shouldParse(clazz: KClass<*>): Boolean

}