import java.io.*;

public class Main {
    public static void main(String[] args) throws IOException {
        // This is main class codes excacudes are here by using zooManagment
        FileWriter writer = new FileWriter(args[4],true);

        zooManagment zoo = new zooManagment();

        try (BufferedReader br = new BufferedReader(new FileReader(args[0]))) {
            String line;
            writer.write("***********************************\n");
            writer.write( "***Initializing Animal information***\n");
            writer.close();
            while((line = br.readLine()) != null){
                String[] parts = line.split(",");
                if(parts[0].equals("Lion")){
                    zoo.addLion(parts[1], Integer.parseInt(parts[2]),5,"bones and refreshing sand",args[4]);
                }
                else if(parts[0].equals("Elephant")){
                    zoo.addElephant(parts[1], Integer.parseInt(parts[2]),10,"the water area",args[4]);
                }
                else if(parts[0].equals("Penguin")){
                    zoo.addPenguin(parts[1], Integer.parseInt(parts[2]),3,"ice and scrubbing walls",args[4]);
                }
                else if(parts[0].equals("Chimpanzee")){
                    zoo.addChimpanzee(parts[1], Integer.parseInt(parts[2]),3,"the enclosure and replacing branches",args[4]);
                }

            }
        }catch (IOException e) {
            e.printStackTrace();
        }
        FileWriter writer2 = new FileWriter(args[4],true);
        writer2.write("***********************************\n" +
                "***Initializing Visitor and Personnel information***\n");
        writer2.close();
        try (BufferedReader br = new BufferedReader(new FileReader(args[1]))) {
            String line;

            while((line = br.readLine()) != null){
                String[] parts = line.split(",");
                if(parts[0].equals("Visitor")){
                    zoo.addVisitor(parts[1],parts[2],args[4] );
                }
                else if(parts[0].equals("Personnel")){
                    zoo.addPersonnel(parts[1],parts[2],args[4]);
                }

            }
        }catch (IOException e) {
            e.printStackTrace();
        }
        FileWriter writer3 = new FileWriter(args[4],true);
        writer3.write("***********************************\n" +
                "***Initializing Food Stock***\n");
        writer3.close();
        try (BufferedReader br = new BufferedReader(new FileReader(args[2]))) {
            String line;

            while((line = br.readLine()) != null){
                String[] parts = line.split(",");
                zoo.addFood(parts[0],Double.parseDouble(parts[1]),args[4] );
            }
        }catch (IOException e) {
            e.printStackTrace();
        }
        try (BufferedReader br = new BufferedReader(new FileReader(args[3]))) {
            String line;
            FileWriter writer4 = new FileWriter(args[4],true);
            while ((line = br.readLine()) != null){
                String[] parts = line.split(",");
                if(parts[0].equals("List Food Stock")){
                    zoo.listFood(args[4]);
                }
                else if(parts[0].equals("Animal Visitation")){

                    zoo.visitation(parts[1],parts[2],args[4]);
                }
                try{

                    if(parts[0].equals("Feed Animal")){
                        zoo.feed(parts[1],parts[2], Integer.parseInt(parts[3]),args[4]);
                    }
                }
                catch (NumberFormatException exp){
                    writer4.write("***********************************\n" +
                            "***Processing new Command***\n");
                    writer4.write("Error processing command: "+parts[0]+","+parts[1]+","+parts[2]+","+parts[3]+"\n");
                    writer4.write("Error:For input string: "+"'"+parts[3]+"'"+"\n");
                    writer4.close();
                }

            }
        }catch (IOException e) {
            e.printStackTrace();
        }

    }


}

