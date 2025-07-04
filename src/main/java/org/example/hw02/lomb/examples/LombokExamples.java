package org.example.hw02.lomb.examples;

import org.example.hw02.lomb.Book;
import org.example.hw02.lomb.Car;
import org.example.hw02.lomb.Configuration;
import org.example.hw02.lomb.House;
import org.example.hw02.lomb.Person;
import org.example.hw02.lomb.Product;
import org.example.hw02.lomb.Util;

import java.math.BigDecimal;

public class LombokExamples {
    public static void init() {

        //@Data - Equivalent to @Getter @Setter @RequiredArgsConstructor @ToString @EqualsAndHashCode.
        Person person1 = new Person();
        person1.setAge(1);
        person1.setName("Alex");
        Person person2 = new Person();
        person2.setAge(1);
        person2.setName("Alex");
        //getter
        String name = person1.getName();
        System.out.printf("Person1 hashCode: %s%n", person1.hashCode());
        System.out.printf("Person2 hashCode: %s%n", person2.hashCode());
        System.out.printf("Person1.hasCode() equals Person2.hasCode(): %s%n", person1.hashCode() == person2.hashCode());
        //toString and equals
        System.out.printf("%s equals %s %s %n", person1, person2, person1.equals(person2));

        //@RequiredArgsConstructor - generate constructor for required (final) fields
        Car car1 = new Car("Ford", "Mustang");

        //@AllArgsConstructor - generate constructor for all arguments
        Book book = new Book("book1", "John", 230, BigDecimal.valueOf(84.50));
        //toString not implemented
        System.out.printf("Book: %s%n", book);

        //@Builder - for implementation "Builder pattern"
        House house1 = House
                .builder()
                .address("65B Lucky St")
                .build();
        House house2 = House
                .builder()
                .address("65B Lucky St")
                .rooms(7)
                .area(300)
                .price(BigDecimal.valueOf(100_000))
                .build();

        //@Getter - generate getters
        //@Setter - generate setters
        Product product = new Product();
        product.setPrice(BigDecimal.valueOf(100.35));
        BigDecimal price = product.getPrice();

        //@Accessors(chain = true) - use for chain style approach -- add return this to setters
        Configuration configuration = new Configuration();
        configuration
                .setFlag1(true)
                .setSetting2("test2")
                .setSetting1("test1");
        System.out.printf("Configuration: %s%n", configuration);

        //@SneakyThrows - sneakily throw checked exceptions
        Util util = new Util();
        util.test1(); //ok test1 with @SneakyThrows
        //util.test0(); //throw exception!!! test0 with @SneakyThrows

        //util.test0_exception(); //exception must be handled
        //util.test1_exception(); //exception must be handled

    }
}
