package com.comix.api.comixapi.model.collection;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

import com.comix.api.comixapi.model.character.ComicCharacter;
import com.comix.api.comixapi.model.creator.ComicCreator;

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

    @Override
    public Long getId() {
        return null;
    }

    @Override
    public Timestamp getPublicationDate() {
        return null;
    }

    @Override
    public String getStoryTitle() {
        Set<String> storyTitles = new HashSet<>();
        for (CollectionElement element : elements) {
            storyTitles.add(element.getStoryTitle());
        }
        return String.join(", ", storyTitles);
    }

    @Override
    public String getDescription() {
        Set<String> descriptions = new HashSet<>();
        for (CollectionElement element : elements) {
            descriptions.add(element.getDescription());
        }
        return String.join(", ", descriptions);
    }

    @Override
    public Set<ComicCreator> getCreators() {
        Set<ComicCreator> creators = new HashSet<>();
        for (CollectionElement element : elements) {
            creators.addAll(element.getCreators());
        }
        return creators;
    }

    @Override
    public Set<ComicCharacter> getPrincipleCharacters() {
        Set<ComicCharacter> principleCharacters = new HashSet<>();
        for (CollectionElement element : elements) {
            principleCharacters.addAll(element.getPrincipleCharacters());
        }
        return principleCharacters;
    }

    @Override
    public int getGrade() {
        return (int) Math.round(elements.stream().mapToInt(CollectionElement::getGrade).average().getAsDouble());
    }

    @Override
    public boolean isSlabbed() {
        return elements.stream().allMatch(CollectionElement::isSlabbed);
    }

    @Override
    public Long getUserId() {
        return elements.stream().findFirst().get().getUserId();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((elements == null) ? 0 : elements.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Collection other = (Collection) obj;
        if (elements == null) {
            if (other.elements != null)
                return false;
        } else if (!elements.equals(other.elements))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "Collection [elements=" + elements + "]";
    }
}
