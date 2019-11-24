package schemabuilder.processor.wiring;

import schemabuilder.processor.exceptions.InvalidWiringOptionsException;

public final class GraphQLWiringBuilderOptions {

    private final String basePackage;
    private final boolean shouldPrintHierarchy;
    private final InstanceFetcher instanceFetcher;

    private GraphQLWiringBuilderOptions(String packageName, boolean shouldPrintHierarchy, InstanceFetcher fetcher) {
        this.basePackage = packageName;
        this.shouldPrintHierarchy = shouldPrintHierarchy;
        this.instanceFetcher = fetcher;
    }

    public String getBasePackage() {
        return basePackage;
    }

    public boolean shouldPrintHierarchy() {
        return shouldPrintHierarchy;
    }

    public final InstanceFetcher getInstanceFetcher() {
        return instanceFetcher;
    }

    public static class Builder {

        private String basePackage = null;
        private boolean printHierarchy = false;
        private InstanceFetcher instanceFetcher;

        public Builder() {}

        /**
         * The base package to recursively scan for GraphQL annotations
         *
         * @param packageName
         */
        public Builder basePackage(String packageName) {
            this.basePackage = packageName;
            return this;
        }

        /**
         * A flag specifying if the GraphQL schema should be printed to the console upon
         * completion of the parser
         *
         * @param shouldPrintHierarchy
         * @return
         */
        public Builder shouldPrintHierarchy(boolean shouldPrintHierarchy) {
            this.printHierarchy = shouldPrintHierarchy;
            return this;
        }

        /**
         * The instance fetcher for the {@link GraphQLWiringBuilder} to use.
         *
         * @param fetcher
         * @return
         */
        public Builder instanceFetcher(InstanceFetcher fetcher) {
            this.instanceFetcher = fetcher;
            return this;
        }

        public GraphQLWiringBuilderOptions build() {
            if(this.basePackage == null) {
                throw new InvalidWiringOptionsException("You must provide a base package name");
            }

            if(this.instanceFetcher == null) {
                this.instanceFetcher = new DefaultInstanceFetcher();
            }

            return new GraphQLWiringBuilderOptions(basePackage, printHierarchy, instanceFetcher);
        }
    }
}
