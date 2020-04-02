package schemabuilder.processor.pipelines.parsing

import schemabuilder.processor.wiring.InstanceFetcher

interface GraphQLClassParserStrategy {

    fun parse(clazz: Class<*>, fetcher: InstanceFetcher)

    /**
     * Precondition logic to determine if the class should be passed
     * to the parser.
     *
     * @return true if the class should be parsed, false otherwise.
     */
    fun shouldParse(clazz: Class<*>): Boolean = true

}