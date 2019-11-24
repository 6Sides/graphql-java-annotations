package schemabuilder.processor.pipelines.parsing;

public class FilterInterfaces extends GraphQLWiringParserStage {

    @Override
    protected void handle(Class<?> clazz) {
        if (!clazz.isInterface()) {
            handleNext(clazz);
        }
    }
}
