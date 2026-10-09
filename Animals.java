public abstract class Animals {
    // It is abstract class of animals and their superclass but I do not use abstract class effectivly
    private String Name;
    private int Age;
    private double mealSize;
    private final String cleanType;
    public Animals(String Name, int Age, double mealSize, String cleanType) {
        this.Name = Name;
        this.Age = Age;
        this.mealSize = mealSize;
        this.cleanType = cleanType;
    }

    public String getName() {
        return Name;
    }

    public double getMealSize() {
        return mealSize;
    }

    public String getCleanType() {
        return cleanType;
    }






}
