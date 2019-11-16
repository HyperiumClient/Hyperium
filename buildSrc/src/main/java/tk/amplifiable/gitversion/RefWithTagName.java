package tk.amplifiable.gitversion;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.lib.PersonIdent;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.revwalk.RevTag;
import org.eclipse.jgit.revwalk.RevWalk;

import java.io.IOException;
import java.util.Comparator;
import java.util.Date;

public class RefWithTagName {
    private final Ref ref;
    private final String tag;

    public RefWithTagName(Ref ref, String tag) {
        this.ref = ref;
        this.tag = tag;
    }

    public Ref getRef() {
        return ref;
    }

    public String getTag() {
        return tag;
    }

    public static class RefComparator implements Comparator<RefWithTagName> {
        private final RevWalk walk;

        public RefComparator(Git git) {
            walk = new RevWalk(git.getRepository());
        }

        @Override
        public int compare(RefWithTagName o1, RefWithTagName o2) {
            boolean annotated1 = isAnnotated(o1.getRef());
            boolean annotated2 = isAnnotated(o2.getRef());

            if (annotated1 && !annotated2) {
                return -1;
            }
            if (!annotated1 && annotated2) {
                return 1;
            }

            if (!annotated1) { // both aren't annotated
                return o1.getRef().getName().compareTo(o2.getRef().getName());
            }

            Date time1 = getAnnotatedTagDate(o1.getRef());
            Date time2 = getAnnotatedTagDate(o2.getRef());
            if (time1 != null && time2 != null) {
                return time2.compareTo(time1);
            }

            return o1.getRef().getName().compareTo(o2.getRef().getName());
        }

        private Date getAnnotatedTagDate(Ref ref) {
            try {
                RevTag tag = walk.parseTag(ref.getObjectId());
                PersonIdent identity = tag.getTaggerIdent();
                return identity.getWhen();
            } catch (IOException e) {
                return null;
            }
        }

        private static boolean isAnnotated(Ref ref) {
            return ref.getPeeledObjectId() != null;
        }
    }
}
