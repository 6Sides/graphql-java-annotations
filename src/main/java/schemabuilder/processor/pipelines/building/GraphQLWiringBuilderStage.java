package schemabuilder.processor.pipelines.building;

import graphql.schema.idl.RuntimeWiring;
import schemabuilder.processor.pipelines.parsing.ParsedGraphQLData;

/**
 * Abstract class for middleware related to processing incoming
 * GraphQL requests.
 */
public abstract class GraphQLWiringBuilderStage {

    static final RuntimeWiring.Builder builder = RuntimeWiring.newRuntimeWiring();
    private boolean shouldPrintDebug = false;

    private GraphQLWiringBuilderStage next;

    /**
     * Builds chains of middleware objects.
     */
    public GraphQLWiringBuilderStage linkWith(GraphQLWiringBuilderStage next) {
        this.next = next;
        return next;
    }

    public RuntimeWiring.Builder kickoff(ParsedGraphQLData data, boolean shouldPrintDebug) {
        this.shouldPrintDebug = shouldPrintDebug;
        return this.handle(data);
    }

    /**
     * Subclasses will implement this method with concrete checks.
     */
    protected abstract RuntimeWiring.Builder handle(ParsedGraphQLData data);

    /**
     * Runs check on the next object in chain or ends traversing if we're in
     * last object in chain.
     */
    RuntimeWiring.Builder handleNext(ParsedGraphQLData data) {
        if (next == null) {
            return builder;
        }
        return next.handle(data);
    }

    void printIfNecessary(String text) {
        if(!this.shouldPrintDebug) {
            return;
        }
        System.out.println(text);
    }
}
