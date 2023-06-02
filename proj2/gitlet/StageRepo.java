package gitlet;

import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;

import static gitlet.Repository.STAGE;
import static gitlet.Utils.readObject;
import static gitlet.Utils.writeObject;

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

    public static void writeStage(StageRepo s) {
        writeObject(STAGE, s);
    }

    public static StageRepo readStage() {
        return readObject(STAGE, StageRepo.class);
    }

    public HashMap<String, String> getAddBlob() {
        return addBlob;
    }

    public HashSet<String> getRemovBlob() {
        return removBlob;
    }

    public void addFile(String fileName, String blobID) {
        addBlob.put(fileName, blobID);
    }

    public void setRemovBlob(String fileName, String blobID) {
        addBlob.remove(fileName, blobID);
        removBlob.add(fileName);
    }

    public boolean isEmpty() {
        return addBlob.isEmpty() && removBlob.isEmpty();
    }

    public void clear() {
        addBlob.clear();
        removBlob.clear();
    }

}
