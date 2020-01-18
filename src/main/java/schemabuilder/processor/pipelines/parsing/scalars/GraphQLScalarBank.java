package schemabuilder.processor.pipelines.parsing.scalars;

import java.util.HashSet;
import java.util.Set;

public class GraphQLScalarBank {

    private static Set<GraphQLScalarType> scalars = new HashSet<>();


    private static GraphQLScalarBank instance = null;

    public static GraphQLScalarBank getInstance() {
        if (instance == null) {
            instance = new GraphQLScalarBank();
        }
        return instance;
    }

    private GraphQLScalarBank() {}


    public void addScalar(GraphQLScalarType dataFetcher) {
        scalars.add(dataFetcher);
    }

    public Set<GraphQLScalarType> getScalars() {
        return scalars;
    }
}
