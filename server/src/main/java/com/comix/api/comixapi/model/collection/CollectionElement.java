package com.comix.api.comixapi.model.collection;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.Table;

@Entity
@Table(name = "collection_elements")
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class CollectionElement {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    public abstract double getValue();

    public abstract int getNumberOfIssues();
}
