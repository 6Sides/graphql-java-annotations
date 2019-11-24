# graphql-java-annotations

The [graphql-java](https://github.com/graphql-java/graphql-java) library is a tool for making executable GraphQL schemas, but defining wiring GraphQL SDL with your code can be a cumbersome process. This library aims to ease that process by providing annotations to specify how your code should be wired into the schema.

## Annotations

### @GraphQLSchemaConfiguration
Defines a class as a general schema configuration class

```java
@GraphQLSchemaConfiguration
public class GraphQLConfig {
    ...
}
```

All classes containing this annotation will be parsed by the package scanner.

### @GraphQLTypeConfiguration
Defines a class as a type configuration class

```java
@GraphQLTypeConfiguration("Query")
public class QueryConfiguration {
    ...
}
```

All classes containing this annotation will be treated as a config file for the specified graphql type (In this case the "Query" type). If multiple classes have the same annotation, their configurations will be merged.

### @GraphQLDataFetcher
Defines a method as a DataFetcher
```java
@GraphQLTypeConfiguration("Query")
public class QueryConfiguration {
    
    @GraphQLDataFetcher
    private DataFetcher<String> testOne {
        ...
    }

    @GraphQLDataFetcher("test")
    private DataFetcher<String> testTwo {
        ...
    }

}
```

All methods annotated with `@GraphQLDataFetcher` must have a return type of type `DataFetcher<?>`. By default, the name of the method is used as the field to be associated with the `DataFetcher`, but a field name can also be provided to the annotation.
Building off of the previous example, the following query will run the appropriate DataFetchers specified in the `QueryConfiguration` class above.

```graphql
query {
    testOne
    test
}
```

### @GraphQLScalar
Defines a method as a Scalar

```java
@GraphQLSchemaConfiguration
public class IDScalar {

    @GraphQLScalar
    public static GraphQLScalarType getCustomScalar() {
        ...
    }
}
```

The above code will add the scalar returned from the `getCustomScalar` method to the schema.

### @GraphQLDirective
Defines a class as a Directive

```java
@GraphQLDirective("auth")
public class AuthorizationDirective implements SchemaDirectiveWiring {
    ...
}
```

The above code will add a directive named `auth` to the schema. Note that any class annotated with the `@GraphQLDirective` annotation must implement the `SchemaDirectiveWiring` interface.

### @GraphQLTypeResolver
Defines a class as a Type Resolver

```java
@GraphQLTypeResolver("Node")
public class NodeResolver implements TypeResolver {
    ...
}
```

The above code will add a type resolver to the `Node` type. Note that any class annotated with the `@GraphQLTypeResolver` annotation must implement the `TypeResolver` interface.

## Putting It All Together

Generating an executable schema is easy. Just use the `GraphQLSchemaBuilder` class.

```java

// Generate the schema
GraphQLSchema schema = new GraphQLSchemaBuilder(
            "graphql_schema", // Directory containing graphql SDL files (Must be in resources path!)
            "graphqls", // File extension of graphql SDL files.
            "example") // Package containing all graphql-java-annotation annotated classes, or "" if you want to scan the entire project.
        .getSchema();

// Create new GraphQL instance using schema
GraphQL graphql = GraphQL.newGraphQL(schema).build();
```

If there is a conflict in the schema, it will fail to build and a message will be printed detailing where the issue is coming from.

Example error message:
```
Multiple definitions of the *** User->age *** data fetcher:
	UserFieldFetchers#getAge
	OtherUserFieldFetchers#getAge
```

If you want more control over building the schema, you can also use the `GraphQLWiringBuilder` and `SchemaParser` classes.

```java
SchemaParser schemaParser = new SchemaParser(SCHEMA_DIRECTORY, SCHEMA_FILE_EXTENSION);

GraphQLWiringBuilderOptions options = new GraphQLWiringBuilderOptions.Builder()
        .basePackage(BASE_PACKAGE)
        .shouldPrintHierarchy(true)
        .instanceFetcher(new InstanceFetcherImpl())
        .build();

GraphQLWiringBuilder builder = new GraphQLWiringBuilder(options);
```

## Using with Dependency Injection Framework

To make this library compatible with DI frameworks (e.g. Spring), you can provide a custom implementation of the `InstanceFetcher` interface. For example, if you are using Spring you could do something like this:

```java
@Component
public class ApplicationContextProvider implements ApplicationContextAware {

    private static org.springframework.context.ApplicationContext context;

    public static org.springframework.context.ApplicationContext getApplicationContext() {
        return context;
    }

    @Override
    public void setApplicationContext(org.springframework.context.ApplicationContext ac) throws BeansException {
        context = ac;
    }
}

// Return the associated bean managed by Spring
public class SpringBeanFetcher implements InstanceFetcher {
    @Override
    public Object getInstance(Class<?> clazz) {
        return ApplicationContextProvider.getApplicationContext().getBean(clazz);
    }
}
```