package gitlet;

import java.io.File;
import static gitlet.Utils.*;


/** Represents a gitlet repository.
 *  does at a high level.
 * <p>
 *
 *        .gitlet:
 *            |--objects
 *            |     |--commit and blob
 *            |--refs
 *            |     |--heads
 *            |          |--master
 *            |--HEAD
 *            |--stage
 *            |--removestage
 * <p>
 *
 *  1.objects
 *          ———— 存放每一个commit and 每一份文件的映射blob
 *  2.refs
 *          ———— 存放和分支
 *  3.HEAD
 *          ———— 存放当前commit指针的（即commit的SHA1）
 *  4.stage
 *          ———— 为暂存区
 *  5.removestage
 *          ———— 删除文件的地方
 * <p>
 *
 *
 *  @author Yeelight
 */
public class Repository {
    /**
     *
     * List all instance variables of the Repository class here with a useful
     * comment above them describing what that variable represents and how that
     * variable is used. We've provided two examples for you.
     */

    /**
     * Default branch name.
     */
    private static final String DEFAULT_BRANCH_NAME = "master";

    /**
     * HEAD ref prefix.
     */
    private static final String HEAD_BRANCH_REF_PREFIX = "ref: refs/heads/";

    /** The current working directory. */
    public static final File CWD = new File(System.getProperty("user.dir"));
    /** The .gitlet directory. */
    public static final File GITLET_DIR = join(CWD, ".gitlet");
    /** The object dire in .gitlet. */
    public static final File OBJ_DIR = join(GITLET_DIR, "object");
    /** The refs dire in .gitlet. */
    public static  final File REFS_DIR = join(GITLET_DIR, "refs");
    /** The HEAD  in .gitlet. */
    public static  final File HEAD = join(GITLET_DIR, "HEAD");
    /** The stage dire. */
    public static  final File STAGE_DIR = join(GITLET_DIR, "stage");
    public static  final File REMOVES_DIR = join(GITLET_DIR, "remove");


    public static void init() {
        if (GITLET_DIR.exists()) {
            System.out.println("A Gitlet version-control system already exists in the current directory");
            System.exit(0);
        }
        // create directories
        GITLET_DIR.mkdir();
        OBJ_DIR.mkdir();
        REFS_DIR.mkdir();
        REMOVES_DIR.mkdir();
        // create initial commit
        Commit initCommit = new Commit();
        createInitialCommit(initCommit);
        String IDC = initCommit.getID();
        createBranch("haeds", IDC);
        // update HEAD
        writeContents(HEAD, HEAD_BRANCH_REF_PREFIX + DEFAULT_BRANCH_NAME );
    }

    /**
     * initial commit
     * <p>
     *  .gitlet:
     *   ...
     *   |--objects
     *   |     |--commit and blob
     *   ...
     */
    private static void createInitialCommit(Commit commit) {
        String fileID = sha1(commit.getID());
        File commitID = join(OBJ_DIR, fileID.substring(0, 2));
        commitID.mkdir();
        writeObject(join(commitID, commit.getID()), commit);
    }

    /**
     *  create branch: master
     *  .gitlet:
     *   ...
     *    |--refs
     *    |    |--heads
     *    |          |--master
     *    |          |  ....
     *   ...
     *
     */
    private static void createBranch(String name, String id) {
        File Branch = join(REFS_DIR, name);
        Branch.mkdir();
        File branchName = join(Branch, DEFAULT_BRANCH_NAME);
        writeContents(branchName, id);
    }

    private static void validateFile(File f) {
        if (f.exists() && f.isDirectory()) {
            System.out.println(" ");
        }
    }
}
