package com.bookishsentimentanalysis;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.MoreObjects;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

public class BookSearch {

    @NotEmpty
    private String searchField;

    public BookSearch(String searchField) {
        this.searchField = searchField;
    }

    @JsonProperty("searchField")
    public String getSearchField() {
        return searchField;
    }

}
