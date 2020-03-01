package core.datafetcher;

import graphql.schema.DataFetcher;
import java.util.HashMap;
import java.util.Map;

public interface StatefulDataFetcher<T> extends DataFetcher<T> {

    Map<StatefulDataFetcher<?>, Integer> costs = new HashMap<>();


    default void setCost(int cost) {
        costs.put(this, cost);
    }

    default int getCost() {
        return costs.getOrDefault(this, 0);
    }
}
