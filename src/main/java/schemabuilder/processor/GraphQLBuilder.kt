package schemabuilder.processor

import graphql.GraphQL
import graphql.execution.instrumentation.ChainedInstrumentation
import graphql.execution.instrumentation.Instrumentation
import graphql.execution.instrumentation.dataloader.DataLoaderDispatcherInstrumentation
import graphql.execution.instrumentation.dataloader.DataLoaderDispatcherInstrumentationOptions
import graphql.schema.idl.SchemaGenerator
import schemabuilder.annotations.documentation.Stable
import schemabuilder.processor.pipelines.building.WiringBuilder
import schemabuilder.processor.schema.SchemaParser
import schemabuilder.processor.wiring.DefaultInstanceFetcher
import schemabuilder.processor.wiring.InstanceFetcher
import java.io.IOException
import java.util.*

@Stable
class GraphQLBuilder private constructor(fetcher: InstanceFetcher, additionalClasses: Set<Class<*>>, basePackageForClasses: String?, schemaFileExtension: String, instrumentation: ChainedInstrumentation, maxQueryCost: Int) {

    private val builder: WiringBuilder = WiringBuilder.withOptions(basePackageForClasses, additionalClasses, fetcher)
    private val schemaParser: SchemaParser = SchemaParser(schemaFileExtension)

    private val instrumentation: ChainedInstrumentation

    @Throws(IOException::class)
    fun generateGraphQL(): GraphQL {
        val typeRegistry = schemaParser.registry
        val runtimeWiring = builder.buildWiring().build()
        val schemaGenerator = SchemaGenerator()
        val schema = schemaGenerator.makeExecutableSchema(typeRegistry, runtimeWiring)
        return GraphQL.newGraphQL(schema)
                .instrumentation(instrumentation)
                .build()
    }

    class Builder {
        private var fetcher: InstanceFetcher = DefaultInstanceFetcher()
        private val additionalClasses: MutableSet<Class<*>>
        private var basePackageForClasses: String?
        private var schemaFileExtension: String
        private var instrumentation: ChainedInstrumentation
        private var maxQueryCost = 100

        fun setInstanceFetcher(injector: InstanceFetcher): Builder {
            fetcher = injector
            return this
        }

        fun addClass(clazz: Class<*>): Builder {
            additionalClasses.add(clazz)
            return this
        }

        fun setBasePackageForClasses(basePackage: String?): Builder {
            basePackageForClasses = basePackage
            return this
        }

        fun setSchemaFileExtension(extension: String): Builder {
            schemaFileExtension = extension
            return this
        }

        fun setInstrumentaiton(instrumentation: ChainedInstrumentation): Builder {
            this.instrumentation = instrumentation
            return this
        }

        fun setMaxQueryCost(maxQueryCost: Int): Builder {
            this.maxQueryCost = maxQueryCost
            return this
        }

        fun build(): GraphQLBuilder {
            return GraphQLBuilder(
                    fetcher,
                    additionalClasses,
                    basePackageForClasses,
                    schemaFileExtension,
                    instrumentation,
                    this.maxQueryCost
            )
        }

        init {
            additionalClasses = HashSet()
            basePackageForClasses = null
            schemaFileExtension = "graphqls"
            val insts: MutableList<Instrumentation> = ArrayList()
            insts.add(
                    DataLoaderDispatcherInstrumentation(
                            DataLoaderDispatcherInstrumentationOptions.newOptions()
                                    .includeStatistics(true)
                    )
            )
            instrumentation = ChainedInstrumentation(insts)
        }
    }

    companion object {
        @kotlin.jvm.JvmStatic
        var maxQueryCost = 0
            private set

        @kotlin.jvm.JvmStatic
        fun newGraphQLBuilder(): Builder {
            return Builder()
        }

    }

    init {
        this.instrumentation = instrumentation
        Companion.maxQueryCost = maxQueryCost
    }
}