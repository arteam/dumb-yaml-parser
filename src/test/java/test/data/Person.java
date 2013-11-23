package test.data;

import org.dumb.yaml.annotation.EnumConverter;
import org.dumb.yaml.annotation.Name;

import java.util.List;

/**
 * Date: 11/23/13
 * Time: 4:41 PM
 *
 * @author Artem Prigoda
 */
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Person person = (Person) o;

        if (age != person.age) return false;
        if (email != null ? !email.equals(person.email) : person.email != null) return false;
        if (name != null ? !name.equals(person.name) : person.name != null) return false;
        if (network != person.network) return false;
        if (types != null ? !types.equals(person.types) : person.types != null) return false;

        return true;
    }

    @Override
    public String toString() {
        return "Person{" +
                "name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", age=" + age +
                ", types=" + types +
                ", network=" + network +
                '}';
    }
}
