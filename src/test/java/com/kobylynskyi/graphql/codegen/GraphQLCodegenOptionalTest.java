package com.kobylynskyi.graphql.codegen;

import com.kobylynskyi.graphql.codegen.model.MappingConfig;
import com.kobylynskyi.graphql.codegen.supplier.SchemaFinder;
import com.kobylynskyi.graphql.codegen.utils.Utils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.nio.file.Paths;
import java.util.Objects;

import static com.kobylynskyi.graphql.codegen.TestUtils.assertSameTrimmedContent;
import static com.kobylynskyi.graphql.codegen.TestUtils.getFileByName;

class GraphQLCodegenOptionalTest {

    private final File outputBuildDir = new File("build/generated");
    private final File outputJavaClassesDir = new File("build/generated");
    private final MappingConfig mappingConfig = new MappingConfig();
    private final SchemaFinder schemaFinder = new SchemaFinder(Paths.get("src/test/resources"));

    @BeforeEach
    void before() {
        schemaFinder.setIncludePattern("github.*\\.graphqls");
        mappingConfig.setUseOptionalForNullableReturnTypes(true);
    }

    @AfterEach
    void cleanup() {
        Utils.deleteDir(outputBuildDir);
    }

    @Test
    void generate_Optional() throws Exception {
        new GraphQLCodegen(schemaFinder.findSchemas(), outputBuildDir, mappingConfig, TestUtils.getStaticGeneratedInfo())
                .generate();

        File[] files = Objects.requireNonNull(outputJavaClassesDir.listFiles());

        // meta: GitHubMetadata!
        assertSameTrimmedContent(new File("src/test/resources/expected-classes/optional/MetaQueryResolver.java.txt"),
                getFileByName(files, "MetaQueryResolver.java"));

        // node(id: ID!): Node
        assertSameTrimmedContent(new File("src/test/resources/expected-classes/optional/NodeQueryResolver.java.txt"),
                getFileByName(files, "NodeQueryResolver.java"));

        // nodes(ids: [ID!]!): [Node]!
        assertSameTrimmedContent(new File("src/test/resources/expected-classes/optional/NodesQueryResolver.java.txt"),
                getFileByName(files, "NodesQueryResolver.java"));

        // check root query
        assertSameTrimmedContent(new File("src/test/resources/expected-classes/optional/QueryResolver.java.txt"),
                getFileByName(files, "QueryResolver.java"));
    }

}