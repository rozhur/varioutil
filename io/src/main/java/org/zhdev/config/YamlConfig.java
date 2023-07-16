package org.zhdev.config;

import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.LoaderOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.comments.CommentLine;
import org.yaml.snakeyaml.comments.CommentType;
import org.yaml.snakeyaml.nodes.*;
import org.yaml.snakeyaml.parser.ParserException;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class YamlConfig extends MapConfigSection implements Config {
    protected static final YamlConfigConstructor CONSTRUCTOR;
    protected static final YamlConfigRepresenter REPRESENTER;
    protected static final Yaml YAML;

    protected final String key;

    protected String[] headerComments;
    protected String[] endComments;

    public YamlConfig(String key) {
        super(null);
        this.key = key;
    }

    public YamlConfig() {
        this(DEFAULT_KEY);
    }

    @Override
    public String getKey() {
        return key;
    }

    @Override
    public String[] getHeaderComments() {
        return headerComments;
    }

    @Override
    public String[] getEndComments() {
        return endComments;
    }

    @Override
    public void setHeaderComments(String... headerComments) {
        this.headerComments = headerComments;
    }

    @Override
    public void setEndComments(String... endComments) {
        this.endComments = endComments;
    }

    private String[] createComments(List<CommentLine> lines) {
        String[] comments = new String[lines.size()];
        for (int i = 0; i < lines.size(); i++) {
            CommentLine line = lines.get(i);
            comments[i] = line.getValue().trim();
        }
        return comments;
    }

    protected Object constructHandle(Node keyNode, Node valueNode, String key, Object value) {
        if (valueNode instanceof MappingNode) {
            MapConfigSection childSection = new MapConfigSection();
            nodesToSections(childSection, (MappingNode) valueNode);
            value = childSection;
        }

        return value;
    }

    private void nodesToSections(MapConfigSection section, MappingNode node) {
        for (NodeTuple tuple : node.getValue()) {
            Node keyNode = tuple.getKeyNode();
            Node valueNode = tuple.getValueNode();
            String key = String.valueOf(CONSTRUCTOR.constructObject(keyNode));
            Object value = constructHandle(keyNode, valueNode, key, CONSTRUCTOR.constructObject(valueNode));

            ConfigSectionNode sectionNode = new ConfigSectionNode(value);
            if (keyNode.getBlockComments() != null && keyNode.getBlockComments().size() > 0) {
                sectionNode.blockComments = createComments(keyNode.getBlockComments());
            }

            if (keyNode.getInLineComments() != null && keyNode.getInLineComments().size() > 0) {
                sectionNode.inlineComments = createComments((valueNode instanceof MappingNode || valueNode instanceof SequenceNode ? keyNode : valueNode).getInLineComments());
            }

            section.map.put(key, sectionNode);
        }
    }

    @Override
    public void load(Reader reader) throws ConfigException {
        try {
            MappingNode node = (MappingNode) YAML.compose(reader);
            if (node == null) return;
            if (node.getBlockComments() != null && node.getBlockComments().size() > 0) {
                headerComments = createComments(node.getBlockComments());
            }
            if (node.getEndComments() != null && node.getEndComments().size() > 0) {
                endComments = createComments(node.getEndComments());
            }
            nodesToSections(this, node);
        } catch (ParserException e) {
            throw new ConfigException(e);
        }
    }

    @Override
    public void load(InputStream stream) {
        load(new InputStreamReader(stream, StandardCharsets.UTF_8));
    }

    @Override
    public void load(File file) throws IOException, ConfigException {
        load(Files.newInputStream(file.toPath()));
    }

    @Override
    public File load(String path) throws ConfigException {
        File file = new File(path);
        try {
            if (file.exists()) load(file);
        } catch (IOException e) {
            throw new ConfigException(e);
        }
        return file;
    }

    @Override
    public File load() throws ConfigException {
        return load(key);
    }

    private List<CommentLine> createCommentLines(String[] comments, CommentType commentType) {
        List<CommentLine> lines = new ArrayList<>(comments.length);
        for (String comment : comments) {
            lines.add(new CommentLine(null, null, comment.isEmpty() ? comment : " " + comment, commentType));
        }
        return lines;
    }

    protected Node representHandle(String key, Object value) {
        Node valueNode;
        if (value instanceof MapConfigSection) {
            valueNode = sectionsToNodes((MapConfigSection) value);
        } else {
            valueNode = REPRESENTER.represent(value);
        }
        return valueNode;
    }

    private MappingNode sectionsToNodes(MapConfigSection section) {
        List<NodeTuple> tuples = new ArrayList<>();
        for (Map.Entry<String, ConfigSectionNode> entry : section.map.entrySet()) {
            String key = entry.getKey();
            ConfigSectionNode sectionNode = entry.getValue();
            Object value = sectionNode.value;

            Node keyNode = REPRESENTER.represent(key);
            Node valueNode = representHandle(key, value);

            keyNode.setBlockComments(sectionNode.blockComments == null ? null : createCommentLines(sectionNode.blockComments, CommentType.BLOCK));

            (valueNode instanceof MappingNode || valueNode instanceof SequenceNode ? keyNode : valueNode).setInLineComments(sectionNode.inlineComments == null ? null : createCommentLines(sectionNode.inlineComments, CommentType.IN_LINE));
            tuples.add(new NodeTuple(keyNode, valueNode));
        }

        return new MappingNode(Tag.MAP, tuples, DumperOptions.FlowStyle.BLOCK);
    }

    @Override
    public void save(Writer writer) {
        MappingNode node = sectionsToNodes(this);
        node.setBlockComments(headerComments == null ? null : createCommentLines(headerComments, CommentType.BLOCK));
        node.setEndComments(endComments == null ? null : createCommentLines(endComments, CommentType.BLOCK));
        YAML.serialize(node, writer);
    }

    @Override
    public void save(OutputStream stream) {
        save(new OutputStreamWriter(stream, StandardCharsets.UTF_8));
    }

    @Override
    public void save(File file) throws IOException {
        save(Files.newOutputStream(file.toPath()));
    }

    @Override
    public File save(String path) throws ConfigException {
        File file = new File(path);
        if (!file.exists()) {
            File parent = file.getParentFile();
            if (parent != null) parent.mkdirs();
            try {
                file.createNewFile();
                save(file);
            } catch (IOException e) {
                throw new ConfigException(e);
            }
        }
        return file;
    }

    @Override
    public File save() throws ConfigException {
        return save(key);
    }

    @Override
    public File saveIfEmpty(String path) throws ConfigException {
        File file = new File(path);
        if (file.length() == 0) {
            File parent = file.getParentFile();
            if (parent != null) parent.mkdirs();
            try {
                file.createNewFile();
                save(file);
            } catch (IOException e) {
                throw new ConfigException(e);
            }
        }
        return file;
    }

    @Override
    public File saveIfEmpty() throws ConfigException {
        return saveIfEmpty(key);
    }

    static {
        LoaderOptions loaderOptions = new LoaderOptions();
        loaderOptions.setProcessComments(true);

        DumperOptions dumperOptions = new DumperOptions();
        dumperOptions.setProcessComments(true);
        dumperOptions.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
        dumperOptions.setDefaultScalarStyle(DumperOptions.ScalarStyle.PLAIN);

        YamlConfigRepresenter representer = new YamlConfigRepresenter(dumperOptions);
        representer.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
        representer.setDefaultScalarStyle(DumperOptions.ScalarStyle.PLAIN);

        REPRESENTER = representer;

        YAML = new Yaml(CONSTRUCTOR = new YamlConfigConstructor(loaderOptions), representer, dumperOptions, loaderOptions);
    }
}
