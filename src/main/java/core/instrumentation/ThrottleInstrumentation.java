package core.instrumentation;

import graphql.execution.instrumentation.InstrumentationState;
import graphql.execution.instrumentation.SimpleInstrumentation;
import graphql.execution.instrumentation.parameters.InstrumentationCreateStateParameters;
import graphql.execution.instrumentation.parameters.InstrumentationFieldFetchParameters;
import graphql.schema.DataFetcher;
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
}
