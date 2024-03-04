package gitlet.model;

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
    private final String message;
    /** The timestamp of this Commit. */
    private final Date timestamp;
    /** The parents commit. */
    private List<String> parents;
    /** The commit ID. */
    private String idsha;

    /** Commit File. */
    private File file;
    /** The blob files Map with file path as key and SHA1 id as value. */
    private HashMap<String, String> blob;


    // Initial Commit
    public Commit() {
        this.message = "Initial commit";
        this.timestamp = new Date(0);
        this.parents = new ArrayList<>();
        this.idsha = getIDSHA1();
        this.blob = new HashMap<>();
        this.file = null;
    }

    public Commit(String message, Date time, String Id, List<String> parents) {
        this.message = message;
        this.timestamp = time;
        this.idsha = Id;
        this.parents = parents;
    }

    /**
     *  From:
     *  https://zhuanlan.zhihu.com/p/533852291#:~:text=(0)%3B-,%E6%AD%A4,-%E5%A4%84%E7%94%9F%E6%88%90%E7%9A%84
     */
    private String getIDSHA1() {
        return sha1(getDateFormat(), message, parents.toString());
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

    public File getFile() {
        return file;
    }

}
