package schemabuilder

import graphql.GraphQL
import graphql.execution.instrumentation.ChainedInstrumentation
import graphql.execution.instrumentation.Instrumentation
import graphql.schema.idl.SchemaGenerator
import new.Plugin
import schemabuilder.processor.pipelines.building.WiringBuilder
import schemabuilder.processor.schema.SchemaParser
import schemabuilder.processor.wiring.DefaultInstanceFetcher
import schemabuilder.processor.wiring.InstanceFetcher


class GraphQLConfiguration (
        private val basePackage: String,
        init: GraphQLConfiguration.() -> Unit
) {

    private var fetcher: InstanceFetcher = DefaultInstanceFetcher()
    private val additionalClasses: MutableSet<Class<*>> = mutableSetOf()
    private var schemaFileExtension: String = "graphqls"
    private val instrumentations: MutableList<Instrumentation> = mutableListOf()

    init {
        init(this)
    }

    fun fetcher(init: () -> InstanceFetcher) {
        this.fetcher = init()
    }

    fun schemaFileExtension(init: () -> String) {
        this.schemaFileExtension = init()
    }

    fun registerClass(init: () -> Class<*>) {
        this.additionalClasses.add(init())
    }

    fun withInstrumentation(init: () -> Instrumentation) {
        this.instrumentations.add(init())
    }

    fun registerPlugin(init: () -> Plugin) {
        init().configure(this)
    }


    fun toGraphQL(): GraphQL {
        val builder: WiringBuilder = WiringBuilder.withOptions(basePackage, additionalClasses, fetcher)
        val schemaParser = SchemaParser(schemaFileExtension)

        val schema = SchemaGenerator().makeExecutableSchema(
                schemaParser.registry,
                builder.buildWiring().build()
        )

        return GraphQL.newGraphQL(schema)
                .instrumentation(ChainedInstrumentation(instrumentations))
                .build()
    }
}
