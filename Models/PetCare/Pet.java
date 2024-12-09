package Models.PetCare;

import Models.Mappable;

public class Pet extends Mappable<Pet> {
    private String name;
    private int age;
    private String breed;
    private String behavior;
    private String feedingSchedule;

    public Pet() {}

    public Pet(String name, String breed, String behavior, String feedingSchedule, int age) {
        this.name = name;
        this.breed = breed;
        this.behavior = behavior;
        this.feedingSchedule = feedingSchedule;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public String getBreed() {
        return breed;
    }

    public String getBehavior() {
        return behavior;
    }

    public String getFeedingSchedule() {
        return feedingSchedule;
    }

    public int getAge() {
        return age;
    }

    @Override
    public String toString() {
        return "Pet name: " + name + ", Age: " + age + ", Breed: " + breed + ", Behavior: " + behavior + ", Feeding Schedule: " + feedingSchedule;
    }
}
