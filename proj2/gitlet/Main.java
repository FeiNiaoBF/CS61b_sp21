package gitlet;

import static gitlet.Repository.*;

/** Driver class for Gitlet, a subset of the Git version-control system.
 *  @author Yeelight
 */
public class Main {

    /** Usage: java gitlet.Main ARGS, where ARGS contains
     *  <COMMAND> <OPERAND1> <OPERAND2> ... 
     */
    public static void main(String[] args) {
        String firstArg = args[0];
        switch(firstArg) {
            case "init":
                // java gitlet.Main init
                validateNumArgs(args, 1);
                Repository.init();
                break;
            case "add":
                // java gitlet.Main add <FILENAME>
                validateNumArgs(args, 2);
                Repository.validateInit();
                Repository.add(args[1]);
                break;
            case "commit":
                // java gitlet.Main commit <message>
                validateNumArgs(args, 2);
                String message = args[1];
                if (message.length() == 0) {
                    System.out.println("Please enter a commit message.");
                    System.exit(0);
                }
                Repository.commit(message);
                break;
            case "rm":
                validateNumArgs(args, 2);
                Repository.rm(args[1]);
                break;
            case "log":
                validateNumArgs(args, 1);
                Repository.log();
                break;
        }
    }

    /**
     * Checks the number of arguments versus the expected number,
     * throws a RuntimeException if they do not match.
     *
     * @param args Argument array from command line
     * @param n Number of expected arguments
     */
    public static void validateNumArgs(String[] args, int n) {
        if (args.length != n) {
            System.out.println("Incorrect operands.");
        }
    }
}
