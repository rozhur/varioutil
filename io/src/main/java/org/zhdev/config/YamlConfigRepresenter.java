package org.zhdev.config;

import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.representer.Representer;

public class YamlConfigRepresenter extends Representer {
    public YamlConfigRepresenter(DumperOptions options) {
        super(options);
    }
}
