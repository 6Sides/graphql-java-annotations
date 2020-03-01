package core.instrumentation;

import core.datafetcher.StatefulDataFetcher;
import graphql.ExecutionResult;
import graphql.execution.instrumentation.InstrumentationState;
import graphql.execution.instrumentation.SimpleInstrumentation;
import graphql.execution.instrumentation.parameters.InstrumentationCreateStateParameters;
import graphql.execution.instrumentation.parameters.InstrumentationExecutionParameters;
import graphql.execution.instrumentation.parameters.InstrumentationFieldFetchParameters;
import graphql.schema.DataFetcher;
import java.util.concurrent.CompletableFuture;

public class ThrottleInstrumentation extends SimpleInstrumentation {

    @Override
    public InstrumentationState createState(InstrumentationCreateStateParameters parameters) {
        return new ThrottleInstrumentationState();
    }

    @Override
    public DataFetcher<?> instrumentDataFetcher(DataFetcher<?> dataFetcher, InstrumentationFieldFetchParameters parameters) {
        return env -> {
            ThrottleInstrumentationState state = parameters.getInstrumentationState();
            if (dataFetcher instanceof StatefulDataFetcher) {
                state.addToCost(((StatefulDataFetcher<?>) dataFetcher).getCost());
            }

            return dataFetcher;
        };
    }

    @Override
    public CompletableFuture<ExecutionResult> instrumentExecutionResult(ExecutionResult executionResult, InstrumentationExecutionParameters parameters) {
        System.out.println("Cost for query: " + ((ThrottleInstrumentationState) parameters.getInstrumentationState()).getTotalCost());
        return CompletableFuture.completedFuture(executionResult);
    }
}
