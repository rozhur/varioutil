package org.zhdev.config;

class ConfigSectionNode {
    Object value;
    String[] blockComments;
    String[] inlineComments;

    ConfigSectionNode(Object value, String[] blockComments) {
        this.value = value;
        this.blockComments = blockComments;
    }

    ConfigSectionNode(Object value) {
        this.value = value;
    }

    ConfigSectionNode() {
    }
}
