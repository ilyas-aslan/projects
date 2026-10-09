public abstract class People {
    // It is abstract class of people and their superclass
    private final String Name;
    private final String id;
    public People(String Name, String id){
        this.Name = Name;
        this.id = id;
    }
    public String getName() {
        return Name;
    }
    public String getId() {
        return id;
    }


}
