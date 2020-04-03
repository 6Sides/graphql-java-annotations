package schemabuilder.processor.pipelines.parsing.datafetchers

import graphql.schema.DataFetcher
import java.util.*

object DataFetcherCostMap {

    private val map: MutableMap<DataFetcher<*>, Int> = HashMap()

    @kotlin.jvm.JvmStatic
    fun getCostFor(dataFetcher: DataFetcher<*>): Int {
        return map.getOrDefault(dataFetcher, 0)
    }

    @kotlin.jvm.JvmStatic
    fun setCostFor(dataFetcher: DataFetcher<*>, cost: Int) {
        map[dataFetcher] = cost
    }
}