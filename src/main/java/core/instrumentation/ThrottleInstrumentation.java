package core.instrumentation;

import graphql.ExecutionResult;
import graphql.execution.instrumentation.InstrumentationState;
import graphql.execution.instrumentation.SimpleInstrumentation;
import graphql.execution.instrumentation.parameters.InstrumentationCreateStateParameters;
import graphql.execution.instrumentation.parameters.InstrumentationExecutionParameters;
import graphql.execution.instrumentation.parameters.InstrumentationFieldFetchParameters;
import graphql.schema.DataFetcher;
import java.util.concurrent.CompletableFuture;
import schemabuilder.processor.pipelines.parsing.datafetchers.DataFetcherCostMap;

public class ThrottleInstrumentation extends SimpleInstrumentation {

    @Override
    public InstrumentationState createState(InstrumentationCreateStateParameters parameters) {
        return new ThrottleInstrumentationState();
    }

    @Override
    public DataFetcher<?> instrumentDataFetcher(DataFetcher<?> dataFetcher, InstrumentationFieldFetchParameters parameters) {
        ((ThrottleInstrumentationState) parameters.getInstrumentationState()).addToCost(DataFetcherCostMap.getCostFor(dataFetcher));
        return dataFetcher;
    }

    @Override
    public CompletableFuture<ExecutionResult> instrumentExecutionResult(
            ExecutionResult executionResult, InstrumentationExecutionParameters parameters) {
        System.out.println("Query cost: " + ((ThrottleInstrumentationState) parameters.getInstrumentationState()).getTotalCost());
        return CompletableFuture.completedFuture(executionResult);
    }
}
