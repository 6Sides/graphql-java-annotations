package schemabuilder.processor.pipelines.building;

import graphql.schema.DataFetcher;
import graphql.schema.TypeResolver;
import graphql.schema.idl.RuntimeWiring.Builder;
import graphql.schema.idl.TypeRuntimeWiring;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import schemabuilder.processor.pipelines.parsing.ParsedGraphQLData;
import schemabuilder.processor.wiring.MappingContainer;

public class BuildTypeWirings extends GraphQLWiringBuilderStage {

    @Override
    protected Builder handle(ParsedGraphQLData data) {
        Map<String, Map<String, DataFetcher<?>>> wirings = new HashMap<>();
        Map<String, TypeResolver> typeResolverMap = new HashMap<>();

        Map<String, Map<String, List<MappingContainer<DataFetcher<?>>>>> types = data.types;
        Map<String, List<MappingContainer<TypeResolver>>> typeResolvers = data.typeResolvers;

        for(String typeName : types.keySet()) {
            Map<String, List<MappingContainer<DataFetcher<?>>>> fields = types.get(typeName);

            Map<String, DataFetcher<?>> fetchers = new HashMap<>();
            for(String field : fields.keySet()) {
                List<MappingContainer<DataFetcher<?>>> containers = fields.get(field);

                assert containers.size() <= 1;

                fetchers.put(field, containers.get(0).obj);
            }
            wirings.put(typeName, fetchers);
        }

        for(String typeName : typeResolvers.keySet()) {
            List<MappingContainer<TypeResolver>> resolvers = typeResolvers.get(typeName);

            assert resolvers.size() <= 1;

            typeResolverMap.put(typeName, resolvers.get(0).obj);
        }

        List<TypeRuntimeWiring.Builder> result = new ArrayList<>();

        this.printIfNecessary("GraphQL Schema:");

        for(String typeName : wirings.keySet()) {
            TypeRuntimeWiring.Builder builder = new TypeRuntimeWiring.Builder()
                    .typeName(typeName);

            this.printIfNecessary(typeName);

            for(String fieldName : wirings.get(typeName).keySet()) {
                this.printIfNecessary("\u2514\u2500" + fieldName);
                builder.dataFetcher(fieldName, wirings.get(typeName).get(fieldName));
            }

            if(typeResolverMap.containsKey(typeName)) {
                builder.typeResolver(typeResolverMap.remove(typeName));
            }

            this.printIfNecessary("");

            result.add(builder);
        }

        for(String typeName : typeResolverMap.keySet()) {
            TypeRuntimeWiring.Builder builder = new TypeRuntimeWiring.Builder()
                    .typeName(typeName);

            builder.typeResolver(typeResolverMap.get(typeName));

            result.add(builder);
        }

        result.forEach(wiring -> builder.type(wiring));

        return handleNext(data);
    }
}
