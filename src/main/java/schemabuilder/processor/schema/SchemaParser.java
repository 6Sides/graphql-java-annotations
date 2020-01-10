package schemabuilder.processor.schema;

import graphql.schema.idl.TypeDefinitionRegistry;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.Set;
import java.util.function.Consumer;
import java.util.regex.Pattern;
import org.reflections.Reflections;
import org.reflections.scanners.ResourcesScanner;

/**
 * A class that parses a directory for graphql schema definition
 * files and creates a valid {@link TypeDefinitionRegistry}
 */
public class SchemaParser {

    private static final String defaultSchemaFileExtension = "graphqls";

    private final String directory;
    private final String schemaFileExtension;

    /**
     * Create a schema parser for the specified directory using
     * the {@link SchemaParser#defaultSchemaFileExtension} value.
     *
     * @param directory The directory to check for .graphqls files. Note that the
     *                  parser will only check for directories in the `resources` folder.
     */
    private SchemaParser(String directory) {
        this(directory, defaultSchemaFileExtension);
    }

    /**
     * Create a schema parser for the specified directory.
     * @param directory The directory to check for .graphqls files. Note that the
     *                  parser will only check for directories in the `resources` folder.
     * @param schemaFileExtension The file type to look for. Only files ending in the provided
     *                            extension will be merged into the schema. Note that the extension
     *                            should not contain a "." E.g. "graphqls", "graphql", etc.
     */
    public SchemaParser(String directory, String schemaFileExtension) {
        this.directory = directory;
        this.schemaFileExtension = schemaFileExtension;
    }

    /**
     * Creates the TypeDefinitionRegistry from the .graphqls files in the schema directory
     *
     * @return A TypeDefinitionRegistry object constructed from the valid graphql schema definition files
     */
    public TypeDefinitionRegistry getRegistry() {
        TypeDefinitionRegistry typeRegistry = new TypeDefinitionRegistry();

        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        URL url = loader.getResource(this.directory);
        assert url != null;
        //String path = url.getPath();
        buildRegistryRecursively(typeRegistry, null);

        return typeRegistry;
    }

    /**
     * Parses the specified directory and merges all valid file contents into the {@link TypeDefinitionRegistry}
     *
     * @param registry The context to merge .graphqls file schemas into
     * @param directory The root directory to search through
     */
    private void buildRegistryRecursively(TypeDefinitionRegistry registry, File directory) {
        Set<String> files = new Reflections("", new ResourcesScanner()).getResources(Pattern.compile(".*\\.graphqls"));

        InputStream inputStream;
        for (String path : files) {
            inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(path);
            java.util.Scanner s = new java.util.Scanner(inputStream).useDelimiter("\\A");

            String fileContents = s.hasNext() ? s.next() : "";
            TypeDefinitionRegistry contents = new graphql.schema.idl.SchemaParser().parse(fileContents);

            registry.merge(contents);
        }

        /*for(File file : Objects.requireNonNull(directory.listFiles())) {
            if(file.isDirectory()) {
                buildRegistryRecursively(registry, file);
                continue;
            }

            if(!file.getName().substring(file.getName().indexOf(".") + 1).equalsIgnoreCase(schemaFileExtension)) {
                continue;
            }

            // For windows file system
            String schemaDirectory = "/" + this.directory;
            if(System.getProperty("os.name").toLowerCase().contains("win")) {
                schemaDirectory = "\\" + this.directory;
            }

            String pathName = file.getPath().substring(file.getPath().indexOf(schemaDirectory) + 1);
            URL url = this.getResource(pathName);
            String fileContents = this.readFile(url.getPath());

            TypeDefinitionRegistry contents = new graphql.schema.idl.SchemaParser().parse(fileContents);

            registry.merge(contents);
        }*/
    }

    /**
     * Load a resource given its name
     * @param resourceName The name of the resource
     * @return A URL pointing to the resource
     */
    private URL getResource(String resourceName) {
        ClassLoader loader =
                this.firstNonNull(
                        Thread.currentThread().getContextClassLoader(), SchemaParser.class.getClassLoader());

        URL url = loader.getResource(resourceName);

        if(url == null) {
            throw new RuntimeException("Resource " + resourceName + " not found.");
        }

        return url;
    }

    /**
     * Returns the first non-null element provided
     * @param first An object
     * @param second Another object
     * @param <T> The type of each object
     * @return The first non-null element
     */
    private <T> T firstNonNull(T first, T second) {
        if (first != null) {
            return first;
        }
        if (second != null) {
            return second;
        }
        throw new NullPointerException("Both parameters are null");
    }

    /**
     * Reads a file and returns its contents as a String
     * @param path The path to the file
     * @return The contents of the file
     * @throws IOException Thrown if the file cannot be read
     */
    private String readFile(String path) throws IOException {
        byte[] encoded = Files.readAllBytes(Paths.get(path));
        return new String(encoded, StandardCharsets.US_ASCII);
    }
}
