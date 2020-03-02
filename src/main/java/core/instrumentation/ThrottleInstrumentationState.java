package core.instrumentation;

import graphql.execution.instrumentation.InstrumentationState;

public class ThrottleInstrumentationState implements InstrumentationState {

    private int totalCost = 0;

    void addToCost(int cost) {
        totalCost += cost;
    }
}
