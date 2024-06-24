package com.py.javaf1.domain;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

public class SelectableBookWrapper {
    private BookWrapper book;
    private BooleanProperty selected = new SimpleBooleanProperty(false);

    public SelectableBookWrapper(BookWrapper book) {
        this.book = book;
    }

    public BookWrapper getBook() {
        return book;
    }

    public BooleanProperty selectedProperty() {
        return selected;
    }

    public boolean isSelected() {
        return selected.get();
    }

    public void setSelected(boolean selected) {
        this.selected.set(selected);
    }
}
