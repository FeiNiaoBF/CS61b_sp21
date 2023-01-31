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
            case "init": {
                validateNumArgs(args, 1);
                Repository.init();
            }

                break;
            case "add":
                // TODO: handle the `add [filename]` command
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
}
