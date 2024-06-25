package com.py.javaf1.domain;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;

public class SelectableBookWrapper {
    @FXML
    private final BookWrapper book;
    @FXML
    private final BooleanProperty selected;

    public SelectableBookWrapper(BookWrapper book) {
        this.book = book;
        this.selected = new SimpleBooleanProperty(false);
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
