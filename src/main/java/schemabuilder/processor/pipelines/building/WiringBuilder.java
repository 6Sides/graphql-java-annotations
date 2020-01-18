package schemabuilder.processor.pipelines.building;

import graphql.schema.idl.RuntimeWiring;
import graphql.schema.idl.TypeRuntimeWiring;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import schemabuilder.processor.pipelines.parsing.GraphQLClassParser;
import schemabuilder.processor.pipelines.parsing.datafetchers.GraphQLDataFetcherBank;
import schemabuilder.processor.pipelines.parsing.datafetchers.GraphQLDataFetcherType;
import schemabuilder.processor.pipelines.parsing.directives.GraphQLDirectiveBank;
import schemabuilder.processor.pipelines.parsing.directives.GraphQLDirectiveType;
import schemabuilder.processor.pipelines.parsing.scalars.GraphQLScalarBank;
import schemabuilder.processor.pipelines.parsing.scalars.GraphQLScalarType;
import schemabuilder.processor.pipelines.parsing.typeresolvers.GraphQLTypeResolverBank;
import schemabuilder.processor.pipelines.parsing.typeresolvers.GraphQLTypeResolverType;

public class WiringBuilder {

    private GraphQLDataFetcherBank dataFetchers = GraphQLDataFetcherBank.getInstance();
    private GraphQLDirectiveBank directives = GraphQLDirectiveBank.getInstance();
    private GraphQLScalarBank scalars = GraphQLScalarBank.getInstance();
    private GraphQLTypeResolverBank typeResolvers = GraphQLTypeResolverBank.getInstance();


    public static WiringBuilder withOptions(String basePackage, Set<Class<?>> clazzes) {
        return new WiringBuilder(basePackage, clazzes);
    }

    private WiringBuilder(String basePackage, Set<Class<?>> clazzes) {
        new GraphQLClassParser(basePackage, clazzes).parseClasses();
    }


    public RuntimeWiring.Builder buildWiring() {
        Map<String, Set<GraphQLDataFetcherType>> typeMap = new HashMap<>();

        for (GraphQLDataFetcherType dataFetcher : this.dataFetchers.getDataFetchers()) {
            typeMap.computeIfAbsent(dataFetcher.getTypeName(), k -> new HashSet<>());
            typeMap.get(dataFetcher.getTypeName()).add(dataFetcher);
        }


        RuntimeWiring.Builder builder = RuntimeWiring.newRuntimeWiring();

        for (String typeName : typeMap.keySet()) {
            TypeRuntimeWiring.Builder typeBuilder = TypeRuntimeWiring.newTypeWiring(typeName);

            for (GraphQLDataFetcherType dataFetcher : this.dataFetchers.getDataFetchers()) {
                typeBuilder.dataFetcher(dataFetcher.getName(), dataFetcher.getDataFetcher());
            }

            builder.type(typeBuilder);
        }

        for (GraphQLTypeResolverType resolver : this.typeResolvers.getTypeResolvers()) {
            TypeRuntimeWiring.Builder typeResolverBuilder = TypeRuntimeWiring.newTypeWiring(resolver.getName());
            typeResolverBuilder.typeResolver(resolver.getResolver());
            builder.type(typeResolverBuilder);
        }


        for (GraphQLScalarType scalar : this.scalars.getScalars()) {
            builder.scalar(scalar.getScalar());
        }

        for (GraphQLDirectiveType directive : this.directives.getDirectives()) {
            builder.directive(directive.getName(), directive.getDirective());
        }

        return builder;
    }
}