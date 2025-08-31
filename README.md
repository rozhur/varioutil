# Various utilities for YAML, JDBC, Bukkit and other things.

Varoutil is a simple API for projects with bulky APIs like YAML, JDBC, Bukkit, Velocity, and others. Its goal is to 
simplify development by making code more consistent.

Note: The Varioutil project is currently under development, there is no stable version yet. The API, package names,
artifacts, and the project name itself may change at any time.

## Add as maven dependency
The project has its own Maven repository, so to access the artifact as a dependency you will need to add it to your 
`pom.xml`.
```xml
<repositories>
    <repository>
        <id>zhdev-repo</id>
        <url>https://maven.zhira.net/repository/zhdev/</url>
    </repository>
</repositories>
```
Then add the artifact as a dependency, for example `common`.
```xml
<dependencies>
    <dependency>
        <groupId>org.zhdev</groupId>
        <artifactId>common</artifactId>
        <version>1.0-SNAPSHOT</version>
    </dependency>
</dependencies>
```

## Examples
Note: The project is primarily focused on making it easier to work with Bukkit and YAML, so only 
examples for those are available at the moment. Public Javadocs and more examples will be available with the first 
release of the Varioutil project.

### Bukkit Plugin and YAML Parsing
Let's say we have a Bukkit plugin called Bukman, and we want to read the `extra-config.yml` file located in the
`./plugins/Bukman` directory and get the value of the `state` field from it.

#### Maven dependency
Add the required `bukkit` dependency.
```xml
<dependencies>
    <dependency>
        <groupId>org.zhdev</groupId>
        <artifactId>bukkit</artifactId>
        <version>1.0-SNAPSHOT</version>
    </dependency>
</dependencies>
```
#### Code
Add the following code.
```java
package org.zhdev;

import org.zhdev.config.BukkitYamlConfig;
import org.zhdev.config.Config;

public class BukmanBukkitPlugin extends BukkitPreparedPlugin {
    private final Config extraConfig = new BukkitYamlConfig("extra-config.yml");

    private int state;

    @Override
    protected void onPostLoad() {
        // parse `./plugins/Bukman/extra-config.yml`
        loadConfig(extraConfig);

        // get the value of the `state` field or get 3 if null
        state = extraConfig.getInteger("state", 5);
    }

    public int getState() {
        return state;
    }
}
```
### More YAML examples
#### Maven dependency
If you only need YAML parsing, just add the `io` maven dependency.
```xml
<dependencies>
    <dependency>
        <groupId>org.zhdev</groupId>
        <artifactId>io</artifactId>
        <version>1.0-SNAPSHOT</version>
    </dependency>
</dependencies>
```
#### Code
```java
import org.zhdev.Config;
import org.zhdev.YamlConfig;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        Config config = new YamlConfig();

        config.load(); // load from ./config.yml file by default
        config.load("test.yml"); // load from ./test.yml by 1st argument

        // set value to "Hello world!" of 'test' key and set block comment from 3rd argument
        config.set("test", "Hello world!", "It's hello world comment!");

        config.save(); // save as ./config.yml by default
        config.save("test.yml"); // save as ./test.yml by 1st argument

        Config anotherConfig = new YamlConfig("another.yml"); // create with default name "another.yml"
        config.load(); // load from ./another.yml by default

        // get value "test" as integer or "5" if it is null
        Integer value = config.getInteger("test", 5);

        // get value "foo->bar->testlist" as list
        List<?> list = config.getSection("foo", "bar").getList("testlist");
    }
}
```
