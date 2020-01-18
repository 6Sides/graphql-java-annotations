package schemabuilder.processor.schema;

import com.google.common.reflect.ClassPath;
import com.google.common.reflect.ClassPath.ResourceInfo;
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
        this.schemaFileExtension = String.format(".%s", schemaFileExtension);
    }

    /**
     * Creates the TypeDefinitionRegistry from the .graphqls files in the schema directory
     *
     * @return A TypeDefinitionRegistry object constructed from the valid graphql schema definition files
     */
    public TypeDefinitionRegistry getRegistry() throws IOException {
        TypeDefinitionRegistry typeRegistry = new TypeDefinitionRegistry();

        buildRegistryRecursively(typeRegistry, null);

        return typeRegistry;
    }

    /**
     * Parses the specified directory and merges all valid file contents into the {@link TypeDefinitionRegistry}
     *
     * @param registry The context to merge .graphqls file schemas into
     * @param directory The root directory to search through
     */
    private void buildRegistryRecursively(TypeDefinitionRegistry registry, File directory)
            throws IOException {

        ClassPath cp = ClassPath.from(Thread.currentThread().getContextClassLoader());

        for (ResourceInfo info : cp.getResources()) {
            try {
                if (info.getResourceName().contains(this.schemaFileExtension)) {
                    System.out.println(info.getResourceName());

                    java.util.Scanner s = new java.util.Scanner(info.asByteSource().openBufferedStream()).useDelimiter("\\A");

                    String fileContents = s.hasNext() ? s.next() : "";
                    TypeDefinitionRegistry contents = new graphql.schema.idl.SchemaParser().parse(fileContents);

                    registry.merge(contents);
                }
            } catch(Exception e) {
                e.printStackTrace();
            }
        }
    }
}
