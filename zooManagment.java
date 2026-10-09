import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
// This class is brain of the system everything is controlled here
public class zooManagment {
    // These are lists and support variables which I used
    private final ArrayList<Personnel> personnel;
    private final ArrayList<Visitor> visitor;
    private final ArrayList<Lion>lion;
    private final ArrayList<Elephant> elephant;
    private final ArrayList<Chimpanzee>chimpanzee;
    private final ArrayList<Penguin>penguins;
    private final ArrayList<Food>food;
    private boolean t; // I used for controlled the are there people or animal
    private boolean f; // I used for controlled the are there people or animal
    private double support;// I used it to find when chimpanzee eat meal, meal is enough
    private double support2;// I used it to find when chimpanzee eat meal, meal is enough
    public void setF(boolean f) {
        this.f = f;
    }
    public void setT(boolean t) {
        this.t = t;
    }
    public zooManagment(){
        personnel = new ArrayList<>();
        visitor = new ArrayList<>();
        lion = new ArrayList<>();
        elephant = new ArrayList<>();
        chimpanzee = new ArrayList<>();
        penguins = new ArrayList<>();
        food = new ArrayList<>();
    }
    public void addPersonnel(String Name,String id,String args) throws IOException {
        setT(true);
        // here this code add personnel and below codes controlled are there same Ids people but I do not know message of the exception because this not the exampesl
        try {
            for (Personnel p : personnel){
                if(p.getId().equals(id)){
                    setT(false);
                    throw new sameId("");
                }
            }
            for (Visitor v : visitor){
                if(v.getId().equals(id)){
                    setT(false);
                    throw new sameId("");
                }
            }


        } catch (sameId e) {
            System.out.println(e.getMessage());
        }


        if (t){
            FileWriter writer = new FileWriter( args,true);
            writer.write("Added new Personnel with id "+id+" and name "+Name+".\n");
            // I define a personnel object by using polymorphism
            People p=new Personnel(Name,id);
            personnel.add((Personnel) p);
            writer.close();
        }

    }
    public void addVisitor(String Name,String id,String args) throws IOException {
        setT(true);
        // here this code add visitor and below codes controlled are there same Ids people but I do not know message of the exception because this not the exampesl
        try {
            for (Personnel p : personnel){
                if(p.getId().equals(id)){
                    setT(false);
                    throw new sameId("");
                }
            }
            for (Visitor v : visitor){
                if(v.getId().equals(id)){
                    setT(false);
                    throw new sameId("");
                }
            }


        } catch (sameId e) {
            System.out.println(e.getMessage());
        }
        if (t){
            FileWriter writer = new FileWriter(args,true);
            writer.write("Added new Visitor with id "+id+" and name "+Name+".\n");
            People p=new Visitor(Name,id);
            // I define a visitor object by using polymorphism
            visitor.add((Visitor) p);
            writer.close();
        }
    }
    public void addLion(String Name, int Age, double mealSize, String cleanType,String args) throws IOException {
        FileWriter writer = new FileWriter(args,true);
        // This code add lion and determine mealsize according to assignment
        writer.write("Added new Lion with name "+Name+" aged "+Age+".\n");
        if(Age!=5){
            mealSize = mealSize + (Age-5)*0.05;
        }
        Animals a=new Lion(Name,Age,mealSize,cleanType);
        // I define a lion object by using polymorphism
        lion.add((Lion) a);
        writer.close();

    }
    public void addElephant(String Name, int Age, double mealSize, String cleanType,String args) throws IOException {
        FileWriter writer = new FileWriter(args,true);
        // This code add elephant and determine mealsize according to assignment
        writer.write("Added new Elephant with name "+Name+" aged "+Age+".\n");
        if(Age!=20){
            mealSize = mealSize + (Age-20)*0.015;
        }
        Animals a=new Elephant(Name,Age,mealSize,cleanType);
        // I define a elephant object by using polymorphism
        elephant.add((Elephant) a);
        writer.close();
    }
    public void addChimpanzee(String Name, int Age, double mealSize, String cleanType,String args) throws IOException {
        FileWriter writer = new FileWriter(args,true);
        // This code add chimpanzee and determine mealsize according to assignment
        writer.write("Added new Chimpanzee with name "+Name+" aged "+Age+".\n");
        if(Age!=10){
            mealSize = mealSize + (Age-10)*0.0125;
        }
        Animals a=new Chimpanzee(Name,Age,mealSize,cleanType);
        // I define a chimpanzee object by using polymorphism
        chimpanzee.add((Chimpanzee) a);
        writer.close();
    }
    public void addPenguin(String Name, int Age, double mealSize, String cleanType,String args) throws IOException {
        FileWriter writer = new FileWriter(args,true);
        // This code add penguin and determine mealsize according to assignment
        writer.write("Added new Penguin with name "+Name+" aged "+Age+".\n");
        if(Age!=4){
            mealSize = mealSize + (Age-4)*0.04;
        }
        Animals a=new Penguin(Name,Age,mealSize,cleanType);
        // I define a penguin object by using polymorphism
        penguins.add((Penguin) a);
        writer.close();
    }
    public void addFood(String Name,double Amount,String args) throws IOException {
        FileWriter writer = new FileWriter(args,true);
        // This code add food
        writer.write("There are "+String.format("%.3f",Amount)+" kg of "+Name+" in stock\n");
        writer.close();
        food.add(new Food(Name,Amount));
    }

    public void feed(String Id, String animalName, int numberOfMeals,String args) throws IOException {
        try {
            FileWriter writer = new FileWriter( args,true);
            writer.write("***********************************\n" + "***Processing new Command***\n");
            setF(true);
            setT(true);
            // This code controlled personel or animals are exist and if they exist t and f which are support boolean chance to false and code work normally otherwise in below code calls the error messages
            for (Personnel per : personnel) {
                if(per.getId().equals(Id)){
                    writer.write(per.getName() + " attempts to feed " + animalName + "\n");
                    setF(false);
                    for (Lion l : lion) {
                        if (l.getName().equals(animalName)) {
                            setT(false);
                            for (Food f : food) {
                                if (f.name.equals("Meat")) {
                                    try{
                                        // this code controlled the meal size if it is not enough to animal it calls error code otherwise code work normally
                                        // This code same with the other animals
                                        if (f.amount < numberOfMeals*l.getMealSize()) {
                                            throw new notEnoughFood("Not enough Meat");
                                        }
                                        else {
                                            f.setAmount(f.getAmount() - numberOfMeals*l.getMealSize());
                                            writer.write(animalName+" has been given "+String.format("%.3f", numberOfMeals*l.getMealSize())+" kgs of meat\n");

                                        }
                                    } catch (notEnoughFood e) {
                                        writer.write("Error: " + e.getMessage() + "\n");
                                    }

                                }
                            }
                        }
                    }
                    for (Elephant e : elephant) {
                        if (e.getName().equals(animalName)) {
                            setT(false);
                            for (Food f : food) {
                                if (f.name.equals("Plant")) {
                                    try{
                                        if (f.amount < numberOfMeals*e.getMealSize()) {
                                            throw new notEnoughFood("Not enough Plant");
                                        }
                                        else {
                                            f.setAmount(f.getAmount() - numberOfMeals*e.getMealSize());
                                            writer.write(animalName+" has been given "+String.format("%.3f", numberOfMeals*e.getMealSize())+" kgs of fruits and hay\n");
                                        }
                                    } catch (notEnoughFood ex) {
                                        writer.write("Error: " + ex.getMessage() + "\n");
                                    }
                                }
                            }
                        }
                    }
                    for (Penguin p : penguins) {
                        if (p.getName().equals(animalName)) {
                            setT(false);
                            for (Food f : food) {
                                if (f.name.equals("Fish")) {
                                    try{
                                        if (f.amount < numberOfMeals*p.getMealSize()) {
                                            throw new notEnoughFood("Not enough Fish");
                                        }
                                        else {
                                            f.setAmount(f.getAmount() - numberOfMeals*p.getMealSize());
                                            writer.write(animalName+" has been given "+String.format("%.3f", numberOfMeals*p.getMealSize())+" kgs of various kinds of fish\n");
                                        }
                                    } catch (notEnoughFood e) {
                                        writer.write("Error: " + e.getMessage() + "\n");
                                    }
                                }
                            }
                        }
                    }
                    for (Chimpanzee c : chimpanzee) {
                        if (c.getName().equals(animalName)) {
                            setT(false);
                            for (Food f : food) {
                                if (f.name.equals("Meat")) {
                                    support=f.getAmount();
                                }
                            }
                            for (Food f : food) {
                                if (f.name.equals("Plant")) {
                                    support2=f.getAmount();
                                }
                            }
                            try {
                                if (support < numberOfMeals * c.getMealSize() || support2 < numberOfMeals * c.getMealSize()) {
                                    if (support < numberOfMeals * c.getMealSize()) {
                                        throw new notEnoughFood("Not enough Meat");
                                    } else if (support2 < numberOfMeals * c.getMealSize()) {
                                        throw new notEnoughFood("Not enough Plant");
                                    }
                                } else {
                                    writer.write(animalName+" has been given "+String.format("%.3f", numberOfMeals*c.getMealSize())+" kgs of meat and "+String.format("%.3f", numberOfMeals*c.getMealSize())+" kgs of leaves\n");
                                    for (Food f : food) {
                                        if (f.name.equals("Meat")) {
                                            f.setAmount(f.getAmount() - numberOfMeals * c.getMealSize());
                                        }
                                    }
                                    for (Food f : food) {
                                        if (f.name.equals("Plant")) {
                                            f.setAmount(f.getAmount() - numberOfMeals * c.getMealSize());
                                        }

                                    }
                                }
                            } catch (notEnoughFood e) {
                                writer.write("Error: " + e.getMessage() + "\n");
                            }
                        }
                    }
                    if (t){
                        // if (t) means t is true firstly if animal exist t will be false and this code will not excatude
                        try {

                            throw new noAnimal("Error: There are no animals with the name "+animalName+"."+"\n");
                        } catch (noAnimal e) {
                            writer.write(e.getMessage()+"\n");
                        }
                    }
                }
            }
            for (Visitor v : visitor) {
                // For visitors if visitor exist this code call error messaage because visitor cannot feed animals.
                try {
                    if (v.getId().equals(Id)){
                        setF(false);
                        writer.write(v.getName()+" tried to feed "+animalName+"\n");
                        throw new noPermission("Error: Visitors do not have the authority to feed animals.\n");
                    }
                } catch (noPermission e) {
                    writer.write(  e.getMessage());
                }
            }
            try {
                if (f){
                    // İf no people this code excacude
                    throw new noPerson("Error: There are no visitors or personnel with the id "+Id+"\n");
                }
            } catch (noPerson e) {
                writer.write(  e.getMessage() );
            }
            writer.close();

        }

        catch (NumberFormatException exp){
        }



    }
    public void  visitation(String Id,String animalName,String args) throws IOException {
        // This code controlled thi visitation if visitor is come to visit animal they can visit animals if personnel visit animals they clean animals habitat according the assigment and if they do not exist code calls error below
        FileWriter writer = new FileWriter(args,true);
        writer.write("***********************************\n" + "***Processing new Command***\n");
        setF(true);
        setT(true);
        for (Personnel per : personnel) {
            if (per.getId().equals(Id)){
                writer.write(per.getName()+" attempts to clean "+animalName+"'s habitat.\n");
                setF(false);
                for (Lion l : lion) {
                    if (l.getName().equals(animalName)) {
                        setT(false);
                        writer.write(per.getName()+" started cleaning "+animalName+"'s habitat.\n");
                        writer.write("Cleaning "+animalName+"'s habitat: Removing "+l.getCleanType()+".\n");
                    }
                }
                for (Elephant e : elephant) {
                    if (e.getName().equals(animalName)) {
                        setT(false);
                        writer.write(per.getName()+" started cleaning "+animalName+"'s habitat.\n");
                        writer.write("Cleaning "+animalName+"'s habitat: Washing "+e.getCleanType()+".\n");
                    }
                }
                for (Chimpanzee c : chimpanzee) {
                    if (c.getName().equals(animalName)) {
                        setT(false);
                        writer.write(per.getName()+" started cleaning "+animalName+"'s habitat.\n");
                        writer.write("Cleaning "+animalName+"'s habitat: Sweeping "+c.getCleanType()+".\n");
                    }
                }
                for (Penguin p : penguins) {
                    if (p.getName().equals(animalName)) {
                        setT(false);
                        writer.write(per.getName()+" started cleaning "+animalName+"'s habitat.\n");
                        writer.write("Cleaning "+animalName+"'s habitat: Replenishing "+p.getCleanType()+".\n");
                    }
                }
                try {
                    if (t){
                        throw new noAnimal("Error: There are no animals with the name "+animalName+"."+"\n");
                    }
                } catch (noAnimal e) {
                    writer.write(e.getMessage());
                }
            }
        }
        for (Visitor v : visitor) {
            if (v.getId().equals(Id)){
                writer.write(v.getName()+" tried  to register for a visit to "+animalName+".\n");
                setF(false);
                for (Lion l : lion) {
                    if (l.getName().equals(animalName)) {
                        setT(false);
                        writer.write(v.getName()+" successfully visited "+animalName+".\n");
                    }
                }
                for (Elephant e : elephant) {
                    if (e.getName().equals(animalName)) {
                        setT(false);
                        writer.write(v.getName()+" successfully visited "+animalName+".\n");

                    }
                }
                for (Chimpanzee c : chimpanzee) {
                    if (c.getName().equals(animalName)) {
                        setT(false);
                        writer.write(v.getName()+" successfully visited "+animalName+".\n");

                    }
                }
                for (Penguin p : penguins) {
                    if (p.getName().equals(animalName)) {
                        setT(false);
                        writer.write(v.getName()+" successfully visited "+animalName+".\n");
                    }
                }
                try {
                    if (t){
                        throw new noAnimal("Error: There are no animals with the name "+animalName+"."+"\n");
                    }
                } catch (noAnimal e) {
                    writer.write(e.getMessage());
                }
            }
        }
        try {
            if (f){
                throw new noPerson("Error: There are no visitors or personnel with the id "+Id+"\n");
            }
        } catch (noPerson e) {
            writer.write(e.getMessage());
        }
        writer.close();
    }
    public void listFood(String args) throws IOException {
        // This code show the food stock in order the assigment
        FileWriter writer = new FileWriter(args,true);
        writer.write("***********************************\n" +
                "***Processing new Command***\n");
        writer.write("Listing available Food Stock:\n");

        for (Food f : food) {
            if (f.name.equals("Plant")) {
                writer.write(f.name + ": " + String.format("%.3f", f.amount) + " kgs\n");
            }
        }
        for (Food f : food) {
            if (f.name.equals("Fish")) {
                writer.write(f.name + ": " + String.format("%.3f", f.amount) + " kgs\n");
            }
        }
        for (Food f : food) {
            if (f.name.equals("Meat")){
                writer.write(f.name+": "+String.format("%.3f", f.amount)+ " kgs\n");
            }
        }
        writer.close();
    }

}

