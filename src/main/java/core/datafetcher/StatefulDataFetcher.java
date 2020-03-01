package core.datafetcher;

import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;
import java.util.HashMap;
import java.util.Map;

public interface StatefulDataFetcher<T> extends DataFetcher<T> {

    Map<StatefulDataFetcher<?>, Integer> costs = new HashMap<>();

    @Override
    T get(DataFetchingEnvironment environment) throws Exception;

    default void setCost(int cost) {
        costs.put(this, cost);
    }

    default int getCost() {
        return costs.getOrDefault(this, 0);
    }
}
