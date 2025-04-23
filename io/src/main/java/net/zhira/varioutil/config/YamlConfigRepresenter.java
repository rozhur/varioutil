package net.zhira.varioutil.config;

import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.representer.Representer;

final class YamlConfigRepresenter extends Representer {
    public YamlConfigRepresenter(DumperOptions options) {
        super(options);
    }
}
