package schemabuilder.processor.pipelines.parsing;

import graphql.schema.DataFetcher;
import graphql.schema.GraphQLScalarType;
import graphql.schema.TypeResolver;
import graphql.schema.idl.SchemaDirectiveWiring;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import schemabuilder.processor.wiring.MappingContainer;

public class ParsedGraphQLData {
    public Map<String, Map<String, List<MappingContainer<DataFetcher<?>>>>> types = new HashMap<>();
    public Map<String, List<MappingContainer<SchemaDirectiveWiring>>> directives = new HashMap<>();
    public Map<String, List<MappingContainer<TypeResolver>>> typeResolvers = new HashMap<>();
    public List<MappingContainer<GraphQLScalarType>> scalars = new ArrayList<>();
}
