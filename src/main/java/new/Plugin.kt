package new

import core.GraphQLDirectives
import core.GraphQLScalars
import schemabuilder.GraphQLConfiguration


interface Plugin {

    fun configure(config: GraphQLConfiguration)

}

class ScalarPlugin: Plugin {

    override fun configure(config: GraphQLConfiguration) {
        config.apply {
            registerClass { GraphQLDirectives.AUTHORIZATION }
            registerClass { GraphQLScalars.DATE_TIME }
            registerClass { GraphQLScalars.DATE }
            registerClass { GraphQLScalars.UUID }
        }
    }
}