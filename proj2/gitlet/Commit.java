package gitlet;

// TODO: any imports you need here

import java.io.File;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import static gitlet.Utils.*;

/** Represents a gitlet commit object.
 *  does at a high level.
 *
 *  @author Yeelight
 */
public class Commit implements Serializable {
    /**
     * List all instance variables of the Commit class here with a useful
     * comment above them describing what that variable represents and how that
     * variable is used. We've provided one example for `message`.
     */

    /** The message of this Commit. */
    private String message;
    /** The timestamp of this Commit. */
    private Date timestamp;
    /** The parents commit. */
    private List<String> parents;
    /** The commit ID. */
    private String idsha;
    /** The blob files Map with file path as key and SHA1 id as value. */
    private HashMap<String, String> blob;


    public Commit() {
        this.message = "Initial commit";  // first commit
        this.timestamp = new Date(0); // Thursday, 1 January 1970
        this.parents = new LinkedList<>();
        this.idsha = getIDSHA("0");
        this.blob = new HashMap<>();
    }

    public Commit(String message, List<Commit> parents, StageRepo stage) {
        this.message = message;
        this.timestamp = new Date();
        this.parents = new LinkedList<>();
        for (Commit p : parents) {
            this.parents.add(p.getID());
        }
        // using first parent blobs
        this.blob = parents.get(0).getBlob();
        // update blob
        for (Map.Entry<String, String> item : stage.getAddBlob().entrySet()) {
            String filename = item.getKey();
            String blobId = item.getValue();
            blob.put(filename, blobId);
        }
        // remove file on blob
        for (String filename : stage.getRemovBlob()) {
            blob.remove(filename);
        }
        this.idsha = getIDSHA();
    }

    /**
     *  From:
     *  https://zhuanlan.zhihu.com/p/533852291#:~:text=(0)%3B-,%E6%AD%A4,-%E5%A4%84%E7%94%9F%E6%88%90%E7%9A%84
     */
    private String getIDSHA() {
        return getIDSHA(this.blob.toString());
    }
    private String getIDSHA(String s) {
        return sha1(getDateFormat(), message, parents.toString(), s);
    }

    private String getDateFormat() {
        DateFormat time = new SimpleDateFormat("EEE MMM d HH:mm:ss yyyy Z", Locale.US);
        return time.format(timestamp);
    }


    // method
    public String getMessage() {
        return message;
    }

    public Date getTime() {
        return timestamp;
    }

    public List<String> getParents() {
        return parents;
    }

    public String getID() {
        return idsha;
    }

    public HashMap<String, String> getBlob() {
        return blob;
    }

    // commit 的一段完整信息
    @Override
    public String toString() {
        StringBuilder logString = new StringBuilder();
        logString.append("===").append("\n");
        logString.append("commit ").append(idsha).append("\n");
        if (parents.size() > 1) {
            logString.append("Merge:");
            for (String par : parents) {
                logString.append(" ").append(par, 0, 7);
            }
            logString.append("\n");
        }
        logString.append("Date:").append(" ").append(timestamp).append("\n");
        logString.append(message).append("\n");
        return logString.toString();
    }

    //
    public String oldId() {
        if (parents.isEmpty()) {
            return "null";
        }
        return parents.get(0);
    }






}
