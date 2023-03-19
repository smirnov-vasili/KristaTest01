import java.util.ArrayList;

public class Program {

    public static void main(String[] args) {
        GlobalMap globalMap = new GlobalMap();
        globalMap.loadFromFile("Input.txt");
        globalMap.boildPaths();
        globalMap.saveToFile("Output.txt");
    }
}
