package net.zhira.varioutil.config;

import org.yaml.snakeyaml.LoaderOptions;
import org.yaml.snakeyaml.constructor.SafeConstructor;
import org.yaml.snakeyaml.nodes.Node;

final class YamlConfigConstructor extends SafeConstructor {
    public YamlConfigConstructor(LoaderOptions loaderOptions) {
        super(loaderOptions);
    }

    @Override
    protected Object constructObject(Node node) {
        return super.constructObject(node);
    }
}
