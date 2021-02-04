package ch.admin.bit.interview.test;

public class PersonCreationDto {
    public String name;
    public String email;
    public int age;
    public String gender;

    public PersonCreationDto(String name, String email, int age, String gender) {
        this.name = name;
        this.email = email;
        this.age = age;
        this.gender = gender;
    }
}
