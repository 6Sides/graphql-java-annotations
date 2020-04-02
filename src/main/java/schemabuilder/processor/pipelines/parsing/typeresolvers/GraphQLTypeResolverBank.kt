package schemabuilder.processor.pipelines.parsing.typeresolvers;

import java.util.HashSet;
import java.util.Set;

public class GraphQLTypeResolverBank {

    private static Set<GraphQLTypeResolverType> typeResolvers = new HashSet<>();


    private static GraphQLTypeResolverBank instance = null;

    public static GraphQLTypeResolverBank getInstance() {
        if (instance == null) {
            instance = new GraphQLTypeResolverBank();
        }
        return instance;
    }

    private GraphQLTypeResolverBank() {}


    public void addTypeResolver(GraphQLTypeResolverType typeResolver) {
        typeResolvers.add(typeResolver);
    }

    public Set<GraphQLTypeResolverType> getTypeResolvers() {
        return typeResolvers;
    }
}