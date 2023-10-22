package com.comix.api.comixapi.model.collection;

import java.util.HashSet;
import java.util.Set;

import com.comix.api.comixapi.model.user.User;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;

@Entity(name = "personal_collection")
public class PersonalCollection extends CollectionElement {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "personal_collection_elements", joinColumns = @JoinColumn(name = "personal_collection_id"), inverseJoinColumns = @JoinColumn(name = "collection_element_id"))
    private Set<CollectionElement> elements = new HashSet<>();

    @OneToOne(mappedBy = "personalCollection")
    private User user;

    public PersonalCollection() {
    }

    public PersonalCollection(Set<CollectionElement> elements) {
        this.elements = elements;
    }

    // Getters
    public User getUser() {
        return user;
    }

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

    public void setUser(User user) {
        this.user = user;
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
