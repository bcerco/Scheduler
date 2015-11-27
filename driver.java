public class driver{
    public static void main(String []args){
	ClassParser cp;
	if (0 < args.length){
            cp = new ClassParser(args[0]);
	    cp.fillclassList();
	    cp.outputclassList();
	}
        else{
            System.out.println("File has to be command line arg.");
            System.exit(-1);
        }
    }
}
