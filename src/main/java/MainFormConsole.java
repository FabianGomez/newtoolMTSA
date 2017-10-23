public class MainFormConsole {

    public static void main(String[] args){
        Map map = MapParser.parse("C:\\Users\\Fabian\\Documents\\mapexample.txt");

        Model model = new Model("C:\\Users\\Fabian\\Documents\\template.lts", map);

        System.out.println("LTS COMPLETE");
        for(String line : model.getLines())
            System.out.println(line);
    }
}
