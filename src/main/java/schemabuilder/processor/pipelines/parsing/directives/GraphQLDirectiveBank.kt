package schemabuilder.processor.pipelines.parsing.directives

object GraphQLDirectiveBank {

    fun addDirective(directive: GraphQLDirectiveType?) {
        directives.add(directive)
    }

    val directives: MutableSet<GraphQLDirectiveType?> = mutableSetOf()

}