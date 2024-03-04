package gitlet.model;

import gitlet.model.Commit;

import java.io.File;

import java.io.IOException;
import static gitlet.Utils.*;
import static gitlet.constant.Consist.*;


/** Represents a gitlet repository.
 *  does at a high level.
 * <p>
 *
 *        .gitlet:
 *            |--objects            // store commit and blob
 *            |     |--commit and blob
 *            |--refs               // store branch
 *            |     |--heads
 *            |          |--master
 *            |--HEAD               // HEAD main
 *            |--stage              // Staging area --> gitlet add xxx
 *            |--removestage        // Remove Staging Area
 * <p>
 *
 *  1.objects
 *          ———— 存放每一个commit and 每一份文件的映射blob
 *  2.refs
 *          ———— 存放HEAD和分支
 *  3.HEAD
 *          ———— 存放当前commit指针的地址
 *  4.stage
 *          ———— 为暂存区
 *  5.removestage
 *          ———— 删除暂存文件的地方
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
     *   java gitlet.Main init
     *   init
     */
    public static void init() throws IOException {
        // validateDire(GITLET_DIR, "Reinitialized existing Gitlet repository in " + GITLET_DIR);
        gitletMkdir();
        createBranchMaster();
    }

    private static void gitletMkdir() throws IOException {
        GITLET_DIR.mkdir();
        OBJ_DIR.mkdir();
        REFS_DIR.mkdir();
        REMOVES_DIR.mkdir();
        HEAD.createNewFile();
        STAGE.createNewFile();
    }

    // 创建第一个分支需要有一个 commit
    private static void createBranchMaster() throws IOException {
        // Create initial commit
        Commit initCommit = new Commit();
        // Create Master Branch
        createBranch("master", false, initCommit);
    }


    // 创建一个分支需要有一个 commit
    private static void createBranch(String branchName, Boolean isRemote, Commit commit) throws IOException {
        validateDire(REFS_DIR, "Not in an initialized refs directory.");
        validateFile(HEAD, "Not in an initialized HEAD file");

        String headPath = HEAD_BRANCH_REF_PREFIX + branchName;
        HEADS_DIR.mkdir();
        REMOTES_DIR.mkdir();
        File branchFile;
        if (isRemote) {
            branchFile = join(REMOTES_DIR, branchName);
            branchFile.createNewFile();
        }
        else {
            branchFile = join(HEADS_DIR, branchName);
            branchFile.createNewFile();
        }
        writeContents(branchFile, commit.getID());
        writeContents(HEAD, headPath);
    }



    // Exit if file exists
    private static void validateFile(File f, String error) {
        if (!f.exists() && !f.isDirectory()) {
            printError(error);
        }
    }

    // Exit if Directory exists
    private static void validateDire(File f, String error) {
        if (!f.exists()) {
            printError(error);
        }
    }

    // Print error and exit system
    private static void printError(String error) {
        System.out.println(error);
        System.exit(0);
    }
}
