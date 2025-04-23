package net.zhira.varioutil.config;

import com.electronwill.nightconfig.core.CommentedConfig;
import com.electronwill.nightconfig.toml.TomlFormat;
import com.electronwill.nightconfig.toml.TomlParser;
import com.electronwill.nightconfig.toml.TomlWriter;

import java.io.Reader;
import java.io.Writer;
import java.nio.file.Path;
import java.util.Map;

public final class VelocityTomlConfig extends MapConfigSection implements Config {
    private static final String DEFAULT_KEY = "config.toml";

    private final String key;

    public VelocityTomlConfig(String key) {
        super();
        this.key = key;
    }

    public VelocityTomlConfig() {
        this(DEFAULT_KEY);
    }

    @Override
    public String getKey() {
        return key;
    }

    private String[] splitComments(String comment) {
        String[] comments = comment.split("\n");
        for (int i = 0; i < comments.length; i++) {
            comments[i] = comments[i].trim();
        }
        return comments;
    }

    private String joinComments(String[] comments) {
        return ' ' + String.join("\n ", comments);
    }

    private void nightConfigToSections(MapConfigSection section, CommentedConfig config) {
        for (CommentedConfig.Entry entry : config.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();

            if (value instanceof CommentedConfig) {
                MapConfigSection childSection;
                ConfigSectionNode node  = section.map.get(key);
                if (node == null || !(node.value instanceof MapConfigSection)) {
                    childSection = new MapConfigSection();
                } else {
                    childSection = (MapConfigSection) node.value;
                }
                nightConfigToSections(childSection, (CommentedConfig) value);
                value = childSection;
            }

            ConfigSectionNode sectionNode = new ConfigSectionNode(value);

            if (entry.getComment() != null) {
                sectionNode.blockComments = splitComments(entry.getComment());
            }

            section.map.put(key, sectionNode);
        }
    }

    @Override
    public void load(Reader reader) {
        TomlParser tomlParser = new TomlParser();
        CommentedConfig commentedConfig = tomlParser.parse(reader);
        nightConfigToSections(this, commentedConfig);
    }

    @Override
    public Path load() throws ConfigException {
        return load(key);
    }

    private void sectionsToNightConfig(MapConfigSection section, CommentedConfig config) {
        for (Map.Entry<String, ConfigSectionNode> entry : section.map.entrySet()) {
            String key = entry.getKey();
            ConfigSectionNode sectionNode = entry.getValue();
            Object value = sectionNode.value;
            if (value instanceof MapConfigSection) {
                CommentedConfig subConfig = config.createSubConfig();
                sectionsToNightConfig((MapConfigSection) value, subConfig);
                value = subConfig;
            }

            config.set(key, value);

            if (sectionNode.blockComments != null) {
                config.setComment(key, joinComments(sectionNode.blockComments));
            }

            if (sectionNode.inlineComments != null) {
                config.setComment(key, config.getComment(key) + "\n\n" + joinComments(sectionNode.inlineComments));
            }
        }
    }

    @Override
    public void save(Writer writer) {
        TomlWriter tomlWriter = new TomlWriter();
        CommentedConfig config = TomlFormat.newConfig();
        sectionsToNightConfig(this, config);
        tomlWriter.write(config, writer);
    }

    @Override
    public Path save() throws ConfigException {
        return save(key);
    }

    @Override
    public Path saveIfEmpty() throws ConfigException {
        return saveIfEmpty(key);
    }

    @Override
    public String toString() {
        return key;
    }
}
