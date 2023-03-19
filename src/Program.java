public class Program {

    public static void main(String[] args) {
        GlobalMap globalMap = new GlobalMap();
        globalMap.loadFromFile("Input.txt");
        globalMap.buildPaths();
        globalMap.saveToFile("Output.txt");
    }
}
