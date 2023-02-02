package gitlet;

import java.io.File;
import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;

import static gitlet.Repository.*;
import static gitlet.Utils.*;

/**
 * Represents the staging area.
 */
public class StageRepo implements Serializable {
    // <filename, blob's id>
    // 用 filename 作为 key
    private HashMap<String, String> addBlob;
    // <filename>
    private HashSet<String> removBlob;

    public StageRepo() {
        this.addBlob = new HashMap<>();
        this.removBlob = new HashSet<>();
    }

    public HashMap<String, String> getAddBlob() {
        return addBlob;
    }

    public HashSet<String> getRemovBlob() {
        return removBlob;
    }

    public void addFile(String filename, String blobID) {
        addBlob.put(filename, blobID);
        removBlob.add(filename);
    }

    public boolean isEmpty() {
        return addBlob.isEmpty() && removBlob.isEmpty();
    }

    public void clear() {
        addBlob.clear();
        removBlob.clear();
    }


    public static void writeStage(StageRepo s) {
        writeObject(STAGE, s);
    }

    public static StageRepo readStage() {
        return readObject(STAGE, StageRepo.class);
    }

}
