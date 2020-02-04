package schemabuilder.processor.pipelines.parsing.dataloaders;

import java.util.Objects;
import org.dataloader.BatchLoader;

public class GraphQLDataLoaderType {

    private String name;
    private BatchLoader<?,?> loader;

    public GraphQLDataLoaderType(String name, BatchLoader<?,?> directive) {
        this.name = name;
        this.loader = directive;
    }

    public String getName() {
        return name;
    }

    public BatchLoader<?,?> getLoader() {
        return loader;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        GraphQLDataLoaderType that = (GraphQLDataLoaderType) o;
        return name.equals(that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public String toString() {
        return "GraphQLDataLoaderType{" +
                "name='" + name + '\'' +
                '}';
    }

}
