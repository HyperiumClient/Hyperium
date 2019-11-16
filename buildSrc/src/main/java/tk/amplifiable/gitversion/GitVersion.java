package tk.amplifiable.gitversion;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Constants;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevWalk;
import org.gradle.api.Project;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GitVersion {
    public static String gitVersion(File dir) throws GitAPIException, IOException {
        Git git = Git.init().setDirectory(dir).call();
        ObjectId headId = git.getRepository().resolve(Constants.HEAD);
        List<String> revs = new ArrayList<>();
        Repository repo = git.getRepository();
        try (RevWalk walk = new RevWalk(repo)) {
            RevCommit head = walk.parseCommit(headId);
            while (true) {
                revs.add(head.getName());
                RevCommit[] parents = head.getParents();
                if (parents == null || parents.length == 0) break;
                head = walk.parseCommit(parents[0]);
            }
        }
        Map<String, RefWithTagName> commitHashToTag = new HashMap<>();
        RefWithTagName.RefComparator comparator = new RefWithTagName.RefComparator(git);
        for (Ref ref : git.getRepository().getRefDatabase().getRefsByPrefix(Constants.R_TAGS)) {
            RefWithTagName withTagName = new RefWithTagName(ref, ref.getName().substring(Constants.R_TAGS.length()));
            ObjectId peeled = withTagName.getRef().getPeeledObjectId();
            if (peeled == null) {
                updateMap(commitHashToTag, comparator, ref.getObjectId(), withTagName);
            } else {
                updateMap(commitHashToTag, comparator, peeled, withTagName);
            }
        }

        for (int depth = 0; depth < revs.size(); depth++) {
            String rev = revs.get(depth);
            if (commitHashToTag.containsKey(rev)) {
                String exactTag = commitHashToTag.get(rev).getTag();
                return depth == 0 ? exactTag : String.format("%s-%s-SNAPSHOT", exactTag, revs.get(0).substring(0, 7));
            }
        }
        return headId.getName().substring(0, 7) + "-SNAPSHOT";
    }

    private static void updateMap(Map<String, RefWithTagName> map, RefWithTagName.RefComparator comparator, ObjectId id, RefWithTagName ref) {
        String commitHash = id.getName();
        if (map.containsKey(commitHash)) {
            if (comparator.compare(ref, map.get(commitHash)) < 0) {
                map.put(commitHash, ref);
            }
        } else {
            map.put(commitHash, ref);
        }
    }
}
