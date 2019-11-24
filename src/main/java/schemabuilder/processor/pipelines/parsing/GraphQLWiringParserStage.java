package schemabuilder.processor.pipelines.parsing;

import java.util.List;
import schemabuilder.processor.wiring.DefaultInstanceFetcher;
import schemabuilder.processor.wiring.InstanceFetcher;

/**
 * Abstract class for middleware related to processing incoming
 * GraphQL requests.
 */
public abstract class GraphQLWiringParserStage {

    protected static InstanceFetcher fetcher;
    protected static ParsedGraphQLData parsedResults = new ParsedGraphQLData();

    private GraphQLWiringParserStage next;

    /**
     * Builds chains of middleware objects.
     */
    public GraphQLWiringParserStage linkWith(GraphQLWiringParserStage next) {
        this.next = next;
        return next;
    }

    public ParsedGraphQLData kickoff(List<Class<?>> classes, InstanceFetcher fetcher_) {
        fetcher = fetcher_;
        for (Class<?> clazz : classes) {
            this.handle(clazz);
        }
        return parsedResults;
    }

    public ParsedGraphQLData kickoff(List<Class<?>> classes) {
        return this.kickoff(classes, new DefaultInstanceFetcher());
    }

    /**
     * Subclasses will implement this method with concrete checks.
     */
    protected abstract void handle(Class<?> clazz);

    /**
     * Runs check on the next object in chain or ends traversing if we're in
     * last object in chain.
     */
    protected void handleNext(Class<?> clazz) {
        if (next == null) {
            return;
        }
        next.handle(clazz);
    }

}
