package cs1302.api;

import java.util.List;

/**
 * Stores info about artist from
 *    MusicBrainz API.
 */
public class Artist {

    String name;
    String country;
    List<Tag> tags;

    /**
     * Stores names of each tag in the list.
     */
    static class Tag {
        String name;

        @Override
        public String toString() {
            return name;
        }
    }

}
