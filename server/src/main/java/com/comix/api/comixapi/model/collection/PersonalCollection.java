package com.comix.api.comixapi.model.collection;

import java.util.HashSet;
import java.util.Set;

public class PersonalCollection implements CollectionElement {

    private Set<CollectionElement> elements = new HashSet<>();

    public PersonalCollection() {
    }

    public PersonalCollection(Set<CollectionElement> elements) {
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
}
