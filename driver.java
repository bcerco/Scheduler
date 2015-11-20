public class driver{
    public static void main(String []args){
        if (0 < args.length)
            ClassParser cp = new ClassParser(args[0]);
        else{
            System.out.println("File has to be command line arg.");
            System.exit(-1);
        }
    }
}
