package com.comix.api.comixapi.model.comic;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.lang.Nullable;
import com.comix.api.comixapi.model.creator.ComicCreator;

public class ComicRowMapper implements RowMapper<Comic> {

    @Override
    @Nullable
    public Comic mapRow(ResultSet resultSet, int i) throws SQLException {
        Long id = resultSet.getLong("id");
        String publisher = resultSet.getString("publisher");
        String seriesTitle = resultSet.getString("series_title");
        String volumeNumber = resultSet.getString("volume_number");
        String issueNumber = resultSet.getString("issue_number");
        Timestamp publicationDate = resultSet.getTimestamp("publication_date");
        String storyTitle = resultSet.getString("story_title");
        String description = resultSet.getString("description");
        String[] creatorNamesArray = resultSet.getString("comic_creators").replace("[", "").replace("]", "")
                .replace("\"", "").split(",");
        Set<ComicCreator> creators = new HashSet<ComicCreator>();
        for (String creatorName : creatorNamesArray) {
            creators.add(new ComicCreator(creatorName));
        }

        return new Comic(id, publisher, seriesTitle, volumeNumber, issueNumber, publicationDate, storyTitle,
                description, creators);
    }

}
