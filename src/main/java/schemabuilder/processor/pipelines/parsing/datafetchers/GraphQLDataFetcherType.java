package schemabuilder.processor.pipelines.parsing.datafetchers;

import graphql.schema.DataFetcher;
import java.util.Objects;

public class GraphQLDataFetcherType {

    private String typeName;
    private String name;
    private int cost;
    private DataFetcher<?> dataFetcher;

    public GraphQLDataFetcherType(String typeName, int cost, String name, DataFetcher<?> dataFetcher) {
        this.typeName = typeName;
        this.cost = cost;
        this.name = name;
        this.dataFetcher = dataFetcher;
    }

    public String getName() {
        return name;
    }

    public DataFetcher<?> getDataFetcher() {
        return dataFetcher;
    }

    public String getTypeName() {
        return typeName;
    }

    public int getCost() {
        return cost;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        GraphQLDataFetcherType that = (GraphQLDataFetcherType) o;
        return name.equals(that.name) && typeName.equals(that.typeName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, typeName);
    }

    @Override
    public String toString() {
        return "GraphQLDataFetcherType{" +
                "typeName='" + typeName + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
