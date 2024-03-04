package gitlet;

import gitlet.model.Repository;
import java.io.IOException;
import static gitlet.constant.Consist.*;

/** Driver class for Gitlet, a subset of the Git version-control system.
 *  @author Yeelight
 */
public class Main {

    /** Usage: java gitlet.Main ARGS, where ARGS contains
     *  <COMMAND> <OPERAND1> <OPERAND2> ... 
     */
    public static void main(String[] args) throws IOException {
        validateNoArgs(args);
        String firstArg = args[0];
        switch(firstArg) {
            case "init": {
                // java gitlet.Main init
                validateGitlet();
                validateNumArgs(args, 1);
                Repository.init();
            }
                break;
            case "add":
                // java gitlet.Main add <FILENAME>
                validateNumArgs(args, 2);
                //Repository.validateUnit();
                //Repository.add(args[1]);
                break;

            default:
                validateNoExist();
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
            throw new RuntimeException("Incorrect operands.");
        }
    }

    public static void validateNoExist() {
        pError("No command with that name exists.");
    }

    public static void validateNoArgs(String[] args) {
        if (args.length == 0) {
            throw new RuntimeException("Please enter a command.");
        }
    }

    public static void validateGitlet() {
        if (GITLET_DIR.exists()) {
            pError("Reinitialized existing Gitlet repository in " + GITLET_DIR);
        }
    }

    private static void pError(String error) {
        System.out.println(error);
        System.exit(0);
    }
}
