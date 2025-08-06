### Add as maven dependency
```xml
<repositories>
    <repository>
        <id>zhdev-repo</id>
        <url>https://maven.zhira.net/repository/zhdev/</url>
    </repository>
</repositories>
```
```xml
<dependencies>
    <dependency>
        <groupId>org.zhdev.varioutil</groupId>
        <artifactId>all</artifactId>
        <version>1.0-SNAPSHOT</version>
    </dependency>
</dependencies>
```

### Example for reflection
#### Maven dependency
```xml
<dependencies>
    <dependency>
        <groupId>org.zhdev.varioutil</groupId>
        <artifactId>common</artifactId>
        <version>1.0-SNAPSHOT</version>
    </dependency>
</dependencies>
```
#### Code
```java
import java.lang.reflect.Field;

import org.zhdev.varioutil.ReflectionUtils;
import org.bukkit.Bukkit;

public class BukkitFields {
    public static final Field METHOD__CraftServer__getServer;

    static {
        METHOD__CraftServer__getServer = ReflectionUtils.methodSearcher()
                .of(Bukkit.getServer())
                .methodOf("getServer")
                .returns(CLASS__DedicatedServer)
                .search();
    }
}
```

### Example for yaml
#### Maven dependency
```xml
<dependencies>
    <dependency>
        <groupId>org.zhdev.varioutil</groupId>
        <artifactId>io</artifactId>
        <version>1.0-SNAPSHOT</version>
    </dependency>
</dependencies>
```
#### Code

```java
import org.zhdev.varioutil.Config;
import org.zhdev.varioutil.YamlConfig;

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
