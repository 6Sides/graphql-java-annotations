package schemabuilder.processor.pipelines.parsing.dataloaders

import org.dataloader.BatchLoader

data class GraphQLDataLoaderType(val name: String, val loader: BatchLoader<*, *>)