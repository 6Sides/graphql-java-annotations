package core.instrumentation;

import graphql.ExecutionResult;
import graphql.ExecutionResultImpl;
import graphql.GraphQLException;
import graphql.execution.instrumentation.InstrumentationState;
import graphql.execution.instrumentation.SimpleInstrumentation;
import graphql.execution.instrumentation.parameters.InstrumentationCreateStateParameters;
import graphql.execution.instrumentation.parameters.InstrumentationExecutionParameters;
import graphql.execution.instrumentation.parameters.InstrumentationFieldFetchParameters;
import graphql.schema.DataFetcher;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import schemabuilder.processor.GraphQLBuilder;
import schemabuilder.processor.pipelines.parsing.datafetchers.DataFetcherCostMap;

public class ThrottleInstrumentation extends SimpleInstrumentation {

    private boolean addExtension;

    public ThrottleInstrumentation() {
        this(false);
    }

    public ThrottleInstrumentation(boolean addExtension) {
        this.addExtension = addExtension;
    }

    @Override
    public InstrumentationState createState(InstrumentationCreateStateParameters parameters) {
        return new ThrottleInstrumentationState();
    }

    @Override
    public DataFetcher<?> instrumentDataFetcher(DataFetcher<?> dataFetcher, InstrumentationFieldFetchParameters parameters) {
        ThrottleInstrumentationState state = parameters.getInstrumentationState();

        state.addToCost(DataFetcherCostMap.getCostFor(dataFetcher));

        if (state.getTotalCost() > GraphQLBuilder.getMaxQueryCost()) {
            throw new GraphQLException("Query exceeded allotted cost!");
        }

        return dataFetcher;
    }

    @Override
    public CompletableFuture<ExecutionResult> instrumentExecutionResult(ExecutionResult executionResult, InstrumentationExecutionParameters parameters) {
        if (addExtension) {
            int totalCost = ((ThrottleInstrumentationState) parameters.getInstrumentationState()).getTotalCost();

            Map<Object, Object> currentExt = executionResult.getExtensions();
            Map<Object, Object> costMap = new LinkedHashMap<>(currentExt == null ? Collections.emptyMap() : currentExt);
            costMap.put("query-cost", totalCost);

            return CompletableFuture.completedFuture(
                    new ExecutionResultImpl(executionResult.getData(), executionResult.getErrors(), costMap)
            );
        }

        return CompletableFuture.completedFuture(executionResult);
    }
}
