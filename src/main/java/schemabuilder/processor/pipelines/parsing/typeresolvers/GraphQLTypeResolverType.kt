package schemabuilder.processor.pipelines.parsing.typeresolvers

import graphql.schema.TypeResolver
import java.util.*

class GraphQLTypeResolverType(val name: String, val resolver: TypeResolver?) {

    override fun equals(o: Any?): Boolean {
        if (this === o) {
            return true
        }
        if (o == null || javaClass != o.javaClass) {
            return false
        }
        val that = o as GraphQLTypeResolverType
        return name == that.name
    }

    override fun hashCode(): Int {
        return Objects.hash(name)
    }

    override fun toString(): String {
        return "GraphQLTypeResolverType{" +
                "name='" + name + '\'' +
                '}'
    }

}