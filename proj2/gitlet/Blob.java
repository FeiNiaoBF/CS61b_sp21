package gitlet;


import java.io.File;
import java.io.Serializable;


import static gitlet.Utils.*;

/**
 *  Keep track of every commit file, Blob Object
 * 文件的保存内容。由于 Gitlet 保存多个版本的文件，
 * 单个文件可能对应多个 blob：每个都在不同的提交中被跟踪。
 * @author Yeelight
 */

/**
 *  commit0: NODE
 *  commit1: file_x --> b1  file_y --> b2
 *  commit2: file_z --> b3
 *  commit3: file_x --> b4  file_z --> b5
 *  ...
 */
public class Blob implements Serializable {

    // This is ID of Blob
    private String id;
    // The FILE byte serialize
    private byte[] bytes;
    // Goal file
    private File goalFile;
    // Goal file path
    private String filePath;
    private String fileName;

    public Blob(File goalFile) {
        this.goalFile = goalFile;
        this.filePath = goalFile.getPath(); // 绝对路径
        this.fileName = goalFile.getName();
        this.bytes = readContents(goalFile);
        this.id = sha1(filePath, bytes);
    }

    public String getId() {
        return this.id;
    }

    public byte[] getBytes() {
        return this.bytes;
    }

    public File getGoalFile() {
        return this.goalFile;
    }

    public String getFilePath() {
        return this.filePath;
    }

    public String getFileName() {
        return this.fileName;
    }
}
