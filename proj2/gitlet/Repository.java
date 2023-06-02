package gitlet;

import java.io.File;
import java.util.List;

import static gitlet.StageRepo.writeStage;
import static gitlet.Utils.*;


/**
 * Represents a gitlet repository.
 * does at a high level.
 * <p>
 * .gitlet:
 * |--objects*
 * |     |--commit and blob
 * |--refs*
 * |     |--heads
 * |          |--master
 * |--HEAD
 * |--stage*
 * |--removestage*
 * |...
 * <p>
 * 1.objects
 * ———— 存放每一个commit and 每一份文件的映射blob
 * 2.refs
 * ———— 存放和分支
 * 3.HEAD
 * ———— 存放当前commit指针的地址
 * 4.stage
 * ———— 为暂存区
 * 5.removestage
 * ———— 删除文件的地方
 * 6.addstage
 * ———— 为暂存区文件夹
 * <p>
 *
 * @author Yeelight
 */
public class Repository {
    /**
     *
     * List all instance variables of the Repository class here with a useful
     * comment above them describing what that variable represents and how that
     * variable is used. We've provided two examples for you.
     */

    /**
     * The current working directory.
     */
    public static final File CWD = new File(System.getProperty("user.dir"));
    /**
     * The .gitlet directory.
     */
    public static final File GITLET_DIR = join(CWD, ".gitlet");
    /**
     * The object dire in .gitlet.
     */
    public static final File OBJ_DIR = join(GITLET_DIR, "object");
    /**
     * The refs dire in .gitlet.
     */
    public static final File REFS_DIR = join(GITLET_DIR, "refs");
    /**
     * The HEAD  in .gitlet.
     */
    public static final File HEAD = join(GITLET_DIR, "HEAD");
    /**
     * The stage dire.
     */
    public static final File STAGE_DIR = join(GITLET_DIR, "stageadd");
    public static final File STAGE = join(GITLET_DIR, "stage");
    public static final File REMOVES_DIR = join(GITLET_DIR, "remove");
    /**
     * Default branch name.
     */
    private static final String DEFAULT_BRANCH_NAME = "master";
    /**
     * HEAD ref prefix.
     */
    private static final String HEAD_BRANCH_REF_PREFIX = "ref: refs/heads/";

    /**
     *  |  ======================================================================================= |
     *  |  ======================================================================================= |
     *  |  ======================================================================================= |
     */

    /**
     * java gitlet.Main init
     */
    public static void init() {
        if (validateFileDire()) {
            exitFile("A Gitlet version-control system already exists in the current directory.");
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
        String Idc = initCommit.getID(); // 记录此时分支的commit
        createFirstBranch("heads", Idc); // 存入heads dire
        // update HEAD    Lab git --> ref: refs/heads/[branch]
        writeContents(HEAD, HEAD_BRANCH_REF_PREFIX + DEFAULT_BRANCH_NAME);
    }

    /**
     * initial commit
     * <p>
     * .gitlet:
     * ...
     * |--objects
     * |     |--commit and blob
     * ...
     */
    private static void createInitialCommit(Commit commit) {
        File commitID = join(OBJ_DIR, commit.getID()); // create commitId of file;
        writeObject(commitID, commit);
    }

    /**
     * create branch: master
     * .gitlet:
     * ...
     * |--refs
     * |    |--heads
     * |          |--master
     * |          |  ....
     * ...
     */
    private static void createFirstBranch(String name, String id) {
        File branch = join(REFS_DIR, name);
        branch.mkdir();
        File branchName = join(branch, DEFAULT_BRANCH_NAME);
        writeContents(branchName, id);
    }

    private static boolean validateFileDire() {
        return Repository.GITLET_DIR.exists() && Repository.GITLET_DIR.isDirectory();
    }

    public static void validateInit() {
        if (validateFileDire()) {
            exitFile("A Gitlet version-control system already exists in the current directory.");
        }
    }

    public static void validateUnit() {
        if (!validateFileDire()) {
            exitFile("Not in an initialized Gitlet directory.");
        }
    }


    /**
     *   java gitlet.Main add <FILENAME>
     */

    /**
     * 1.Adds a copy of the file as it currently exists to the staging area
     * 2.将需要 add 的文件以 blob 的形式存入 staging 中；
     * 如果 file 和当前 commit 中跟踪的文件相同（ blob 的 id 相同），则不将其添加到 staging 中；
     */
    public static void add(String filename) {
        File file = join(CWD, filename);  // 从本地文件夹里面找
        //如果要添加的文件不存在
        if (!file.exists()) {
            exitFile("File does not exist.");
        }
        // 读入要存入缓存区的文件将其成为Blob
        Blob b = new Blob(file);
        String blobId = b.getId();
        // 判断 当前commit和 stagarea 里面是否有文件
        Commit head = getHeadCommit();
        StageRepo stag = getStage();
        // 将 Commit & stage里面的文件id找出来
        String headId = head.getBlob().getOrDefault(filename, "");
        String stagFileId = stag.getAddBlob().getOrDefault(filename, "");
        /**
         *
         * 1. 文件没有add到暂存区里面
         * 2. 文件已经在暂存区里面了，但本地的文件更新了也要add（并且把旧的文件删除）
         * 3. 文件已经在当前跟踪的commit节点里面，则比较文件内容是否与上一次提交的版本相同
         *
         */
        if (blobId.equals(headId)) {
            // 如果 Blob 与当前的 head 相同，则说明文件没有被修改过，只需要将它从暂存区移除即可
            if (!blobId.equals(stagFileId)) {
                stag.setRemovBlob(filename, blobId);
                join(STAGE_DIR, stagFileId).delete();
                writeStage(stag);
            }
        } else if (!blobId.equals(stagFileId)) { // 文件内容不同或者文件未在暂存区中
            if (!stagFileId.equals("")) {   // 文件在暂存区中，删除旧版本
                join(STAGE_DIR, stagFileId).delete();
            }
            writeObject(join(STAGE_DIR, blobId), b); // 将新版本的文件存储到暂存区
            stag.addFile(filename, blobId);   // 将新版本的文件加入到 Stage 中
            writeStage(stag);
        }
    }

    /**
     * java gitlet.Main commit <message>
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
        String braName = getBranchPath();
        File braFile = getBranchFile(braName);
        writeContents(braFile, c.getID());
        // 清除缓存区
        stage.clear();
    }

    /**
     * java gitlet.Main rm <FILENAME>
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
            stage.setRemovBlob(filename, blobId);
        }
        // 如果文件被当前commit跟踪，则将其存入stage for removal区域.
        if (headId.equals(blobId)) {
            restrictedDelete(file);
        }
        writeStage(stage);
    }


    /**
     * java gitlet.Main log
     */

    public static void log() {
        Commit head = getHeadCommit();
        StringBuilder logs = new StringBuilder();
        while (head != null) {
            logs.append(head.toString());
            head = getCommitFromOBJ(head.oldId());
        }
        System.out.println(logs);
    }

    /**
     * java gitlet.Main global-log
     */
    public static void globalLog() {
        StringBuilder gLogs = new StringBuilder();
        List<String> commitIds = plainFilenamesIn(OBJ_DIR);
        assert commitIds != null;
        for (String filename : commitIds) {
            Commit c = getCommitFromOBJ(filename);
            assert c != null;
            gLogs.append(c.toString());
        }
        System.out.println(gLogs.toString());
    }

    private static Commit getCommitFromOBJ(String commitId) {
        File file = join(OBJ_DIR, commitId);
        // 如果commit不存在
        if (commitId.equals("") || !file.exists()) {
            return null;
        }
        return readObject(file, Commit.class);
    }


    /**
     * java gitlet.Main find <commit message>
     */
    public static void find(String message) {
        StringBuilder str = new StringBuilder();
        List<String> commitIds = plainFilenamesIn(OBJ_DIR);
        assert commitIds != null;
        for (String filename : commitIds) {
            Commit c = getCommitFromOBJ(filename);
            assert c != null;
            if (c.getMessage().contains(message)) { // 比 equals 更好用
                str.append("===").append("\n").append(c.getID()).append("\n");
            }
        }
        if (str.length() == 0) {
            exitFile("Found no commit with that message.");
        }
        System.out.println(str.toString());
    }


    /**
     * java gitlet.Main status
     */

    public static void status() {
        String info = statusInfo();
        System.out.println(info);
    }

    private static String statusInfo() {
        StringBuilder info = new StringBuilder();
        // === Branches ===
        info.append(statusBranches()).append("\n");
        // === Staged Files ===
        info.append(statusStaged()).append("\n");
        // === Removed Files ===
        info.append(statusRemoved()).append("\n");
        // === Modifications Not Staged For Commit ===
        info.append("=== Modifications Not Staged For Commit ===").append("\n");
        // === Untracked Files ===
        info.append("=== Untracked Files ===").append("\n");

        return info.toString();
    }

    private static String statusBranches() {
        StringBuilder sb = new StringBuilder();
        sb.append("=== Branches ===").append("\n");
        List<String> branches = plainFilenamesIn(headDir());
        assert branches != null;
        for (String bra : branches) {
            if (bra.equals(mianBranches())) {
                sb.append("*").append(bra).append("\n");
            } else {
                sb.append(bra).append("\n");
            }
        }
        return sb.toString();
    }

    private static File headDir() {
        String headBra = getBranchPath();
        File file = null;
        String[] tmp = headBra.split(": ", 2);
        String[] braDir = tmp[1].split("/");
        if (braDir.length >= 2) {
            file = join(GITLET_DIR, braDir[0], braDir[1]);
        }
        return file;
    }

    private static String mianBranches() {
        // 找到当前的分支。
        String headBra = getBranchPath();
        // 分支所对应的文件。
        File branchFile = getBranchFile(headBra);
        return branchFile.getName();
    }

    private static String statusStaged() {
        StringBuilder sb = new StringBuilder();
        sb.append("=== Staged Files ===").append("\n");
        StageRepo stage = getStage();
        String[] stageFile = stage.getAddBlob().keySet().toArray(new String[0]);
        for (String sta : stageFile) {
            sb.append(sta).append("\n");
        }
        return sb.toString();
    }

    private static String statusRemoved() {
        StringBuilder sb = new StringBuilder();
        sb.append("=== Removed Files ===").append("\n");
        StageRepo stageRepo = getStage();
        String[] stageRemFile = stageRepo.getRemovBlob().toArray(new String[0]);
        for (String remFile : stageRemFile) {
            sb.append(remFile).append("\n");
        }
        return sb.toString();
    }


    /**
     * java gitlet.Main checkout
     * - 1. java gitlet.Main checkout -- [file name]
     * - 2. java gitlet.Main checkout [commit id] -- [file name]
     * - 3. java gitlet.Main checkout [branch name]
     */

    public static void checkoutFromFile(String filename) {
        Commit head = getHeadCommit();
        String blob = head.getBlob().getOrDefault(filename, "");
        checkoutfileFromBolbId(blob);

    }

    public static void checkoutFromFile(Commit commitId, String filename) {
        if (commitId.equals("")) {
            exitFile("No commit with that id exists.");
        }
        String blob = commitId.getBlob().getOrDefault(filename, "");
        checkoutfileFromBolbId(blob);
    }

    public static void checkoutFromBranch(String branchName) {
        if (!hasBranchName(branchName)) {
            exitFile("No such branch exists.");
        }
        String head = getBranchPath();
        File branchFile = getBranchFile(head);
        String branchFileName = branchFile.getName();
        if (branchFileName.equals(branchName)) {
            exitFile("No need to checkout the current branch.");
        }

        Commit commit = getCommitFromBranchFile(branchFile);


    }

    private static void checkoutfileFromBolbId(String blobId) {
        if (blobId.equals("")) {
            exitFile("File does not exist in that commit.");
        }
        Blob blob = getBlobFromId(blobId);
        checkoutFromBlob(blob);
    }

    private static Blob getBlobFromId(String blobId) {
        File file = join(STAGE_DIR, blobId);
        return readObject(file, Blob.class);
    }

    private static void checkoutFromBlob(Blob blob) {
        File file = join(CWD, blob.getFileName());
        writeContents(file, blob.getBytes());
    }


    /**
     * java gitlet.Main branch
     */


    // 找到当前commit
    private static Commit getHeadCommit() {
        // 找到当前的分支。
        String headBra = getBranchPath();
        // 分支所对应的文件。
        File branchFile = getBranchFile(headBra);
        // 根据文件找到此时 commit.
        return getCommitFromBranchFile(branchFile);
    }

    private static StageRepo getStage() {
        return StageRepo.readStage();
    }

    private static String getBranchPath() {
        return readContentsAsString(HEAD);
    }

    private static File getBranchFile(String branchPath) {
        // refs
        File file = null;
        String[] tmp = branchPath.split(": ", 2);
        String[] branches = tmp[1].split("/");
        if (branches.length == 2) {
            file = join(GITLET_DIR, branches[0], branches[1]);
        }
        if (branches.length == 3) {
            file = join(GITLET_DIR, branches[0], branches[1], branches[2]);
        }
        return file;
    }

    private static List<String> getAllBranch() {
        List<String> branches = plainFilenamesIn(headDir());
        return branches;
    }

    private static boolean hasBranchName(String branchName) {
        List<String> branchs = getAllBranch();
        boolean b = branchName.equals("");
        for (String bra : branchs) {
            if (!bra.equals(branchName) || b) {
                return false;
            }
        }
        return true;
    }


    private static Commit getCommitFromBranchFile(File file) {
        String commitID = readContentsAsString(file);
        File commit = join(OBJ_DIR, commitID);
        return readObject(commit, Commit.class);
    }

//    private static String twoCommitId(String s) {
//        return sha1(s).substring(0, 2);
//    }


    private static void exitFile(String s) {
        System.out.println(s);
        System.exit(0);
    }
}
