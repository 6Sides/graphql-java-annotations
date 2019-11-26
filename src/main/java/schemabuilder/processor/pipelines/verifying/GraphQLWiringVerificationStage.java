package schemabuilder.processor.pipelines.verifying;

import schemabuilder.processor.exceptions.InvalidSchemaException;
import schemabuilder.processor.pipelines.parsing.ParsedGraphQLData;

/**
 * Abstract class for middleware related to processing incoming
 * GraphQL requests.
 */
public abstract class GraphQLWiringVerificationStage {

    static String issues = "";

    private GraphQLWiringVerificationStage next;

    /**
     * Builds chains of middleware objects.
     */
    public GraphQLWiringVerificationStage linkWith(GraphQLWiringVerificationStage next) {
        this.next = next;
        return next;
    }

    public void kickoff(ParsedGraphQLData data) {
        this.handle(data);
    }

    /**
     * Subclasses will implement this method with concrete checks.
     */
    protected abstract void handle(ParsedGraphQLData data);

    /**
     * Runs check on the next object in chain or ends traversing if we're in
     * last object in chain.
     */
    void handleNext(ParsedGraphQLData data) {
        if (next == null) {
            if (!issues.equals("")) {
                throw new InvalidSchemaException(issues);
            }
            return;
        }
        next.handle(data);
    }
}
