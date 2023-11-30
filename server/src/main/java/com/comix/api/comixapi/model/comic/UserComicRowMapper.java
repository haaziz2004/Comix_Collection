package com.comix.api.comixapi.model.comic;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.lang.Nullable;

import com.comix.api.comixapi.model.character.ComicCharacter;
import com.comix.api.comixapi.model.creator.ComicCreator;

public class UserComicRowMapper implements RowMapper<UserComic> {

    @Override
    @Nullable
    public UserComic mapRow(ResultSet resultSet, int i) throws SQLException {
        Long id = resultSet.getLong("id");
        String publisher = resultSet.getString("publisher");
        String seriesTitle = resultSet.getString("series_title");
        String volumeNumber = resultSet.getString("volume_number");
        String issueNumber = resultSet.getString("issue_number");
        Timestamp publicationDate = resultSet.getTimestamp("publication_date");
        String storyTitle = resultSet.getString("story_title");
        String description = resultSet.getString("description");
        String[] creatorNamesArray = resultSet.getString("creators").replace("[", "").replace("]", "")
                .replace("\"", "").split(",");
        Set<ComicCreator> creators = new HashSet<ComicCreator>();
        for (String creatorName : creatorNamesArray) {
            creators.add(new ComicCreator(creatorName));
        }
        String[] principleCharactersNamesArray = resultSet.getString("characters").replace("[", "")
                .replace("]", "")
                .replace("\"", "").split(",");
        Set<ComicCharacter> principleCharacters = new HashSet<ComicCharacter>();
        for (String characterName : principleCharactersNamesArray) {
            principleCharacters.add(new ComicCharacter(characterName));
        }
        Double value = resultSet.getDouble("value");
        Integer grade = resultSet.getInt("grade");
        Boolean slabbed = resultSet.getBoolean("slabbed");
        Long userId = resultSet.getLong("user_id");

        return new UserComic(id, publisher, seriesTitle, volumeNumber, issueNumber, publicationDate, storyTitle,
                description, creators, principleCharacters, value, grade, slabbed, userId);
    }

}
