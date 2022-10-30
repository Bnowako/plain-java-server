package mono.klakson.common;

public record User (String name, int age, Address address) {
    public static User withAddress(String name, int age, String city, String street) {
        Address address1 = new Address(city, street);
        return new User(name, age, address1);
    }
}

record Address (String city, String street) {}
