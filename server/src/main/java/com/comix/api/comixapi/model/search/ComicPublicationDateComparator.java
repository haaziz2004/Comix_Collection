package com.comix.api.comixapi.model.search;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;
import java.util.Locale;
import com.comix.api.comixapi.model.comic.IComic;

public class ComicPublicationDateComparator implements Comparator<IComic> {
    @Override
    public int compare(IComic c1, IComic c2) {
        String dateStr1 = c1.getPublicationDate();
        String dateStr2 = c2.getPublicationDate();
        SimpleDateFormat[] dateFormats = new SimpleDateFormat[] {
                new SimpleDateFormat("yyyy", Locale.ENGLISH),
                new SimpleDateFormat("MMM, yyyy", Locale.ENGLISH),
                new SimpleDateFormat("MMM dd, yyyy", Locale.ENGLISH),
                new SimpleDateFormat("MMM d, yyyy", Locale.ENGLISH)
        };

        Date date1 = null;
        Date date2 = null;

        for (SimpleDateFormat format : dateFormats) {
            try {
                date1 = format.parse(dateStr1);
                if (date1 != null && date2 != null) {
                    break;
                }
            } catch (ParseException e) {
                // Ignore and try the next format
            }
            try {
                date2 = format.parse(dateStr2);
                if (date1 != null && date2 != null) {
                    break;
                }
            } catch (ParseException e) {
                // Ignore and try the next format
            }
        }

        if (date1 == null || date2 == null) {
            // Handle invalid date strings
            throw new IllegalArgumentException("Invalid date format");
        }

        return date2.compareTo(date1);
    }
}