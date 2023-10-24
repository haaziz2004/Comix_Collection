package com.comix.api.comixapi.model.collection;

import java.util.HashSet;
import java.util.Set;

public class Collection implements CollectionElement {

    private Set<CollectionElement> elements = new HashSet<>();

    public Collection() {
    }

    public Collection(Set<CollectionElement> elements) {
        this.elements = elements;
    }

    // Getters
    public Set<CollectionElement> getElements() {
        return elements;
    }

    public double getValue() {
        return elements.stream().mapToDouble(CollectionElement::getValue).sum();
    }

    public int getNumberOfIssues() {
        return elements.stream().mapToInt(CollectionElement::getNumberOfIssues).sum();
    }

    // Setters
    public void setElements(Set<CollectionElement> elements) {
        this.elements = elements;
    }

    // Methods
    public void addElement(CollectionElement element) {
        elements.add(element);
    }

    public void removeElement(CollectionElement element) {
        elements.remove(element);
    }

    public void clearElements() {
        elements.clear();
    }

    @Override
    public String getPublisher() {
        Set<String> publishers = new HashSet<>();
        for (CollectionElement element : elements) {
            publishers.add(element.getPublisher());
        }
        return String.join(", ", publishers);
    }

    @Override
    public String getSeriesTitle() {
        Set<String> seriesTitles = new HashSet<>();
        for (CollectionElement element : elements) {
            seriesTitles.add(element.getSeriesTitle());
        }
        return String.join(", ", seriesTitles);
    }

    @Override
    public String getVolumeNumber() {
        Set<String> volumeNumbers = new HashSet<>();
        for (CollectionElement element : elements) {
            volumeNumbers.add(element.getVolumeNumber());
        }
        return String.join(", ", volumeNumbers);
    }

    @Override
    public String getIssueNumber() {
        Set<String> issueNumbers = new HashSet<>();
        for (CollectionElement element : elements) {
            issueNumbers.add(element.getIssueNumber());
        }
        return String.join(", ", issueNumbers);
    }
}
