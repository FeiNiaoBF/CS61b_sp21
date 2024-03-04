package gitlet.constant;

import java.io.File;
import static gitlet.Utils.*;


public class Consist {

    public static final String DEFAULT_BRANCH_NAME = "master";

    /** HEAD ref prefix */
    public static final String HEAD_BRANCH_REF_PREFIX = "refs/heads/";

    /** The current working directory. */
    public static final File CWD = new File(System.getProperty("user.dir"));
    /** The .gitlet directory. */
    public static final File GITLET_DIR = join(CWD, ".gitlet");
    /** The object dire in .gitlet. */
    public static final File OBJ_DIR = join(GITLET_DIR, "object");
    /** The refs dire in .gitlet. */
    public static final File REFS_DIR = join(GITLET_DIR, "refs");
    // HEADS in refs
    public static final File HEADS_DIR = join(REFS_DIR, "HEADS");

    // remotes in refs
    public static final File REMOTES_DIR = join(REFS_DIR, "remotes");

    /** The HEAD  in .gitlet. */
    public static final File HEAD = join(GITLET_DIR, "HEAD");
    /** The stage dire. */
    public static final File STAGE = join(GITLET_DIR, "stage");
    public static final File REMOVES_DIR = join(GITLET_DIR, "remove");

}
