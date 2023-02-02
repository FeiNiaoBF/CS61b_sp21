package gitlet;

import com.sun.java.accessibility.util.GUIInitializedListener;

import java.io.*;
import java.nio.file.Path;
import java.util.List;

import static gitlet.Utils.*;
import static gitlet.StageRepo.*;
import static gitlet.Commit.*;



/** Represents a gitlet repository.
 *  does at a high level.
 * <p>
 *
 *        .gitlet:
 *            |--objects*
 *            |     |--commit and blob
 *            |--refs*
 *            |     |--heads
 *            |          |--master
 *            |--HEAD
 *            |--stage*
 *            |--removestage*
 *            |...
 * <p>
 *
 *  1.objects
 *          ———— 存放每一个commit and 每一份文件的映射blob
 *  2.refs
 *          ———— 存放和分支
 *  3.HEAD
 *          ———— 存放当前commit指针的地址
 *  4.stage
 *          ———— 为暂存区
 *  5.removestage
 *          ———— 删除文件的地方
 *  6.addstage
 *          ———— 为暂存区文件夹
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
    public static  final File STAGE_DIR = join(GITLET_DIR, "stageadd");
    public static  final File STAGE = join(GITLET_DIR, "stage");
    public static  final File REMOVES_DIR = join(GITLET_DIR, "remove");

    /**
     *  |  ======================================================================================= |
     *  |  ======================================================================================= |
     *  |  ======================================================================================= |
     */


    /**
     *  java gitlet.Main init
     */
    public static void init() {
        if (validateFileDire(GITLET_DIR)) {
            exitFile("A Gitlet version-control system already exists in the current directory");
        }
        // create directories
        GITLET_DIR.mkdir();
        OBJ_DIR.mkdir();
        REFS_DIR.mkdir();
        REMOVES_DIR.mkdir();
        STAGE_DIR.mkdir();
        StageRepo.writeStage(new StageRepo());
        // create initial commit
        Commit initCommit = new Commit();
        createInitialCommit(initCommit);
        String IDC = initCommit.getID();
        createBranch("heads", IDC);
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
        String fileID = twoCommitId(commit.getID());
        File commitID = join(OBJ_DIR, fileID);
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

    private static boolean validateFileDire(File f) {
        return f.exists() && f.isDirectory();
    }

    public static void validateInit() {
        if (!validateFileDire(GITLET_DIR) && !validateFileDire(OBJ_DIR) && !validateFileDire(REFS_DIR) && !validateFileDire(REMOVES_DIR)) {
            exitFile("A Gitlet version-control system already exists in the current directory");
        }
    }


    /**
     *   java gitlet.Main add <FILENAME>
     */

    /**
     * 1.Adds a copy of the file as it currently exists to the staging area
     * 2.将需要 add 的文件以 blob 的形式存入 staging 中；
     *   如果 file 和当前 commit 中跟踪的文件相同（ blob 的 id 相同），则不将其添加到 staging 中；
     *
     *
     */
    public static void add(String filename) {
        //如果要添加的文件不存在
        File file = join(CWD, filename);
        if (!file.exists()) {
            exitFile("File does not exist.");
        }
        // 读入要存入缓存区的文件将其成为Blob
        Blob b = new Blob(file);
        String blobId = b.getId();
        // 判断 commit和 stag area 里面是否有文件
        Commit head = getHeadCommit();
        StageRepo stag = getStage();
        // 将 Commit & stage里面的文件id找出来
        String headId = head.getBlob().getOrDefault(filename, "");
        String stagFileId = stag.getAddBlob().getOrDefault(filename, "");
        /**
         * 该文件的当前工作版本(stageadd)与当前提交中的版本(commit)相同时，不要将其添加到暂存区，
         * 如果它已经存在，则将其从暂存区中删除。
         */
        // commit 里面有没有该文件
        if (blobId.equals(headId)) {
            // 有，不需要 add in stage
            if (!blobId.equals(stagFileId)) {
                // 从 stage 里面删除文件
                join(STAGE_DIR, stagFileId).delete();
                stag.getAddBlob().remove(stagFileId);
                stag.getRemovBlob().remove(filename);
                StageRepo.writeStage(stag);
            }
            // 没有
        } else if (!blobId.equals(stagFileId)) {
            // update staging
            // delete original, add the new version
            if (!stagFileId.equals("")) {
                join(STAGE_DIR, stagFileId).delete();
            }
            writeObject(join(STAGE_DIR, blobId), b);
            // change stage added files
            stag.addFile(filename, blobId);
            StageRepo.writeStage(stag);
        }
    }

    /**
     *  java gitlet.Main commit <message>
     *
     */

    public static void commit(String message) {
        if (message.equals("")) {
            exitFile("Please enter a commit message.");
        }
        Commit head = getHeadCommit();
        updateCommit(message, List.of(head));
    }

    private static void updateCommit(String message, List<Commit> parent) {
        // 检查暂缓区
        StageRepo stage = getStage();
        if (stage.isEmpty()) {
            exitFile("No changes added to the commit.");
        }
        // 创建新提交点， 并更新
        Commit c = new Commit(message, parent, stage);
        // 将commit存入commits文件夹中.
        createInitialCommit(c);
        // 让当前branch指向新的commit.
        String braName = getBranch();
        File braFile = getBranchFile(braName);
        writeContents(braFile, c.getID());
        // 清除缓存区


    }

    /**
     *  java gitlet.Main rm <FILENAME>
     *
     */

    public static void rm(String filename) {
        File file = join(CWD, filename);
        Blob blob = new Blob(file);
        String blobId = blob.getId();
        // 如果在本地文件里面没有这个文件就退出
        Commit head = getHeadCommit();
        StageRepo stage = StageRepo.readStage();
        String headId = head.getBlob().getOrDefault(filename, "");
        String stagFileId = stage.getAddBlob().getOrDefault(filename, "");

        if (headId.equals("") && stagFileId.equals("")) {
            exitFile("No reason to remove the file.");
        }
        // 如果文件在 add 区域，则将其中缓存区删除；
        if (!stagFileId.equals("")) {
            stage.getAddBlob().remove(filename, blobId);
        }
        // 如果文件被当前commit跟踪，则将其存入stage for removal区域.
        if (headId.equals(blobId)) {
            restrictedDelete(file);
        }
        writeStage(stage);
    }



    /**
     *  java gitlet.Main log
     *
     */

    public static void log() {
        Commit head = getHeadCommit();
        StringBuilder logs = new StringBuilder();
        while(head != null) {
            logs.append(head.toString());
            head = getOldCommit(head.oldId());
        }
        System.out.println(logs);
    }

    /**
     * java gitlet.Main global-log
     */
    public static void globalLog() {
        StringBuilder gLogs = new StringBuilder();
        List<String> commitIds = plainFilenamesIn(OBJ_DIR);

    }




    private static Commit getOldCommit(String commitId) {
        File file = getTwoC(commitId);
        // 如果commit不存在
        if (commitId.equals("") || !file.exists()) {
            return null;
        }
        return readObject(file, Commit.class);
    }

    /**
     *  取得 OBJ_DIR
     *          |——twoFile
     *          |     |——<commitId>
     */
    private static File getTwoC(String commitId) {
        return join(OBJ_DIR, twoCommitId(commitId), commitId);
    }


//    private static Commit readCommit(File f) {
//        if (!f.exists()) {
//            return null;
//        }
//
//        return readObject(f, Commit.class);
//    }
//
//    private static void writeCommit(Commit commit) {
//        File f = getTwoC(commit.getID());
//        writeObject(f, commit);
//    }


    // 找到当前commit
    private static Commit getHeadCommit() {
        // 找到当前的分支。
        String headBra = getBranch();
        // 分支所对应的文件。
        File branchFile = getBranchFile(headBra);
        // 根据文件找到此时 commit.
        Commit head = getCommitFromBranchFile(branchFile);
        if (head == null) {
            exitFile("No Commit!");
        }
        return head;
    }

    private static StageRepo getStage() {
        return StageRepo.readStage();
    }

//    private static void clearStage(Commit c, StageRepo stag) {
//        File[] files = STAGE_DIR.listFiles();
//        if (files == null) {
//            return;
//        }
//        Blob blob = c.getBlob();
//        stag.
//
//
//    }

    private static String getBranch() {
        return readContentsAsString(HEAD);
    }

    private static File getBranchFile(String fileName) {
        // refs
        File file = null;
        String[] tmp = fileName.split(": ", 2);
        String[] branches = tmp[1].split("/");
        if (branches.length == 2) {
            file = join(GITLET_DIR, branches[0], branches[1]);
        }
        if (branches.length == 3) {
            file = join(GITLET_DIR, branches[0], branches[1], branches[2]);
        }
        return file;
    }

    private static Commit getCommitFromBranchFile(File file) {
        String commitID = readContentsAsString(file);
        File commit = getTwoC(commitID);
        return readObject(commit, Commit.class);
    }

    private static String twoCommitId(String s) {
        return sha1(s).substring(0, 2);
    }


    private static void exitFile(String s) {
        System.out.println(s);
        System.exit(0);
    }
}
