package mono;

public record User (String name, int age, Address address) {}

record Address (String city, String street) {}