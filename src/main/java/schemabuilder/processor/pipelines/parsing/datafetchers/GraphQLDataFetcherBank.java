package schemabuilder.processor.pipelines.parsing.datafetchers;

import java.util.HashSet;
import java.util.Set;

public class GraphQLDataFetcherBank {

    private static Set<GraphQLDataFetcherType> dataFetchers = new HashSet<>();


    private static GraphQLDataFetcherBank instance = null;

    public static GraphQLDataFetcherBank getInstance() {
        if (instance == null) {
            instance = new GraphQLDataFetcherBank();
        }
        return instance;
    }

    private GraphQLDataFetcherBank() {}


    public void addDataFetcher(GraphQLDataFetcherType dataFetcher) {
        DataFetcherCostMap.setCostFor(dataFetcher.getDataFetcher(), dataFetcher.getCost());
        dataFetchers.add(dataFetcher);
    }

    public Set<GraphQLDataFetcherType> getDataFetchers() {
        return dataFetchers;
    }

}
