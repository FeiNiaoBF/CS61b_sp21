package gitlet;

/**
 * Driver class for Gitlet, a subset of the Git version-control system.
 *
 * @author Yeelight
 */
public class Main {

    /**
     * Usage: java gitlet.Main ARGS, where ARGS contains
     * <COMMAND> <OPERAND1> <OPERAND2> ...
     */
    public static void main(String[] args) {
        validateNoArgs(args);
        String firstArg = args[0];
        switch (firstArg) {
            case "init":
                // java gitlet.Main init
                validateNumArgs(args, 1);
                Repository.init();
                break;
            case "add":
                // java gitlet.Main add <FILENAME>
                validateNumArgs(args, 2);
                Repository.validateUnit();
                Repository.add(args[1]);
                break;
            case "commit":
                // java gitlet.Main commit <message>
                validateNumArgs(args, 2);
                Repository.validateUnit();
                String message = args[1];
                if (message.length() == 0) {
                    System.out.println("Please enter a commit message.");
                    System.exit(0);
                }
                Repository.commit(message);
                break;
            case "rm":
                validateNumArgs(args, 2);
                Repository.validateUnit();
                Repository.rm(args[1]);
                break;
            case "log":
                validateNumArgs(args, 1);
                Repository.validateUnit();
                Repository.log();
                break;
            case "global-log":
                validateNumArgs(args, 1);
                Repository.validateUnit();
                Repository.globalLog();
                break;
            case "find":
                validateNumArgs(args, 2);
                Repository.validateUnit();
                String mes = args[1];
                Repository.find(mes);
                break;
            case "status":
                validateNumArgs(args, 1);
                Repository.validateUnit();
                Repository.status();
                break;
        }
        //validateNoExist();
    }

    /**
     * Checks the number of arguments versus the expected number,
     * throws a RuntimeException if they do not match.
     *
     * @param args Argument array from command line
     * @param n    Number of expected arguments
     */
    public static void validateNumArgs(String[] args, int n) {
        if (args.length != n) {
            System.out.println("Incorrect operands.");
            System.exit(0);
        }
    }

    public static void validateNoExist() {
        System.out.println("No command with that name exists.");
        System.exit(0);
    }

    public static void validateNoArgs(String[] args) {
        if (args.length == 0) {
            System.out.println("Please enter a command.");
            System.exit(0);
        }
    }
}
