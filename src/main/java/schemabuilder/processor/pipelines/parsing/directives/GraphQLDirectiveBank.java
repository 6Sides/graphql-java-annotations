package schemabuilder.processor.pipelines.parsing.directives;

import java.util.HashSet;
import java.util.Set;

public class GraphQLDirectiveBank {

    private static Set<GraphQLDirectiveType> directives = new HashSet<>();

    private static GraphQLDirectiveBank instance = null;

    public static GraphQLDirectiveBank getInstance() {
        if (instance == null) {
            instance = new GraphQLDirectiveBank();
        }
        return instance;
    }

    private GraphQLDirectiveBank() {}

    public void addDirective(GraphQLDirectiveType directive) {
        directives.add(directive);
    }

    public Set<GraphQLDirectiveType> getDirectives() {
        return directives;
    }
}
