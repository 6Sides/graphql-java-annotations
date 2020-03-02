package schemabuilder.processor.pipelines.parsing.datafetchers;

import graphql.schema.DataFetcher;
import java.util.HashMap;
import java.util.Map;

public class DataFetcherCostMap {

    private static final Map<DataFetcher, Integer> map = new HashMap<>();

    public static int getCostFor(DataFetcher dataFetcher) {
        System.out.println("Getting cost for: " + dataFetcher);
        return map.getOrDefault(dataFetcher, 0);
    }

    public static void setCostFor(DataFetcher dataFetcher, int cost) {
        System.out.println("Setting cost for: " + dataFetcher);
        map.put(dataFetcher, cost);
    }
}
