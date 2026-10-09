//Those are exceptions which I used
public class invalidPersonType extends Exception {
    public invalidPersonType(String message) {
        super(message);
    }
}
class invalidProgram extends Exception {
    public invalidProgram(String message) {
        super(message);
    }
}
class wrongCase extends Exception {
    public wrongCase(String message) {
        super(message);
    }
}
