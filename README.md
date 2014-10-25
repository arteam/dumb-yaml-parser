dumb-yaml-parser
================

Dumb yaml parser/object mapper with support of immutable objects and enums.

Annotate your domain witn `@Name` annotation. It works for constructor parameters and fields.

````java
import org.dumb.yaml.annotation.EnumConverter;
import org.dumb.yaml.annotation.Name;
import java.util.List;

public class Person {

    private final String name;
    private final String email;
    private final int age;
    private final List<JobType> types;
    private final Network network;

    public Person(@Name("name") String name, @Name("email") String email,
                  @Name("age") int age, @Name("types") @EnumConverter List<JobType> types,
                  @Name("network") @EnumConverter("byCode") Network network) {
        this.name = name;
        this.email = email;
        this.age = age;
        this.types = types;
        this.network = network;
    }

    public enum JobType {
        VOD, TV
    }

    public enum Network {

        IPTV("iptv"),
        OTT("ott"),
        DVB("dvb");

        private String code;

        private Network(String code) {
            this.code = code;
        }

        public static Network byCode(String code) {
            for (Network network : values()) {
                if (network.code.equals(code))
                    return network;
            }
            return null;
        }
    }
}    
````

Or use `@Names` annotation. It can be placed on classes and constructors (useful for languages like Kotlin).
````java
import org.dumb.yaml.annotation.EnumConverter;
import org.dumb.yaml.annotation.Names;
import java.util.List;

public class PersonNames {

    private final String name;
    private final String email;
    private final int age;
    private final List<JobType> types;
    private final Network network;

    @Names({"name", "email", "age", "types", "network"})
    public PersonNames(String name,  String email,
                       int age,  @EnumConverter List<JobType> types,
                       @EnumConverter("byCode") Network network) {
        this.name = name;
        this.email = email;
        this.age = age;
        this.types = types;
        this.network = network;
    }

    public enum JobType {
        VOD, TV
    }

    public enum Network {

        IPTV("iptv"),
        OTT("ott"),
        DVB("dvb");

        private String code;

        private Network(String code) {
            this.code = code;
        }

        public static Network byCode(String code) {
            for (Network network : values()) {
                if (network.code.equals(code))
                    return network;
            }
            return null;
        }
    }
}    
````

Parse your YAML file
````java
import org.dumb.yaml.Yaml;
import java.io.*;

public void testPerson() {
    Person test = new Yaml().parse(new File("/test10.yml"), Person.class);
}
````

See more examples in [tests] (https://github.com/arteam/dumb-yaml-parser/tree/master/src/test/java)
