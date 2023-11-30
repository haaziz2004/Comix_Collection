package com.comix.api.comixapi.model.comic;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.lang.Nullable;

public class ComicRowMapperNoCreators implements RowMapper<Comic> {

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

        return new Comic(id, publisher, seriesTitle, volumeNumber, issueNumber, publicationDate, storyTitle,
                description);
    }

}
