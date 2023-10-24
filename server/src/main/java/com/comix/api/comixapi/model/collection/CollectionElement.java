package com.comix.api.comixapi.model.collection;

import java.util.Set;

public interface CollectionElement {
    public double getValue();

    public int getNumberOfIssues();

    public String getPublisher();

    public String getSeriesTitle();

    public String getVolumeNumber();

    public String getIssueNumber();

    public void addElement(CollectionElement element);

    public void removeElement(CollectionElement element);

    public Set<CollectionElement> getElements();
}
