package schemabuilder.processor.pipelines.parsing.dataloaders

import org.dataloader.BatchLoader
import org.dataloader.DataLoader
import org.dataloader.DataLoaderRegistry
import org.dataloader.MappedBatchLoader

object DataLoaderRepository {

    private val batchLoaders: MutableMap<String, BatchLoader<*, *>> = HashMap()
    private val mappedBatchLoaders: MutableMap<String, MappedBatchLoader<*, *>> = HashMap()

    fun addBatchLoader(name: String, batchLoader: BatchLoader<*, *>) {
        batchLoaders[name] = batchLoader
    }

    fun addBatchLoader(name: String, batchLoader: MappedBatchLoader<*, *>) {
        mappedBatchLoaders[name] = batchLoader
    }

    val dataLoaderRegistry: DataLoaderRegistry
        get() {
            val registry = DataLoaderRegistry()

            batchLoaders.forEach { (name: String, loader: BatchLoader<*, *>) -> registry.register(name, DataLoader.newDataLoader(loader)) }
            mappedBatchLoaders.forEach { (name: String, loader: MappedBatchLoader<*, *>) -> registry.register(name, DataLoader.newMappedDataLoader(loader)) }

            return registry
        }

}