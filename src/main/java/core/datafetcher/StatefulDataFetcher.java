package core.datafetcher;

import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;

public interface StatefulDataFetcher<T> extends DataFetcher<T> {

    default T get(DataFetchingEnvironment environment) throws Exception {
        return get(environment, new DataFetcherProperties());
    }

    T get(DataFetchingEnvironment environment, DataFetcherProperties props) throws Exception;


    final class DataFetcherProperties {
        // Cost associated with fetch.
        private int cost = 0;

        public int getCost() {
            return cost;
        }

        public void setCost(int cost) {
            this.cost = cost;
        }
    }
}
