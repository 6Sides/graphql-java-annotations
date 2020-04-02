package schemabuilder.processor.pipelines.parsing.dataloaders;

import java.util.HashMap;
import java.util.Map;
import org.dataloader.BatchLoader;
import org.dataloader.DataLoader;
import org.dataloader.DataLoaderRegistry;
import org.dataloader.MappedBatchLoader;

public class DataLoaderRepository {

    private static volatile DataLoaderRepository instance;

    private DataLoaderRepository() {
        if (instance != null) {
            throw new RuntimeException(
                    "Use getInstance() method to get the single instance of this class.");
        }

        this.batchLoaders = new HashMap<>();
        this.mappedBatchLoaders = new HashMap<>();
    }

    public static DataLoaderRepository getInstance() {
        if (instance == null) {
            synchronized (DataLoaderRepository.class) {
                if (instance == null) {
                    instance = new DataLoaderRepository();
                }
            }
        }

        return instance;
    }

    private Map<String, BatchLoader<?, ?>> batchLoaders;
    private Map<String, MappedBatchLoader<?, ?>> mappedBatchLoaders;

    public void addBatchLoader(String name, BatchLoader<?, ?> batchLoader) {
        this.batchLoaders.put(name, batchLoader);
    }

    public void addBatchLoader(String name, MappedBatchLoader<?, ?> batchLoader) {
        this.mappedBatchLoaders.put(name, batchLoader);
    }

    public DataLoaderRegistry getDataLoaderRegistry() {
        DataLoaderRegistry registry = new DataLoaderRegistry();

        this.batchLoaders.forEach(
                (name, loader) -> {
                    registry.register(name, DataLoader.newDataLoader(loader));
                });
        this.mappedBatchLoaders.forEach(
                (name, loader) -> {
                    registry.register(name, DataLoader.newMappedDataLoader(loader));
                });

        return registry;
    }
}

