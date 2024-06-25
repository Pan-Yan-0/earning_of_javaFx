package com.py.javaf1.Controller;

import com.py.javaf1.domain.Book;
import com.py.javaf1.domain.BookWrapper;
import com.py.javaf1.domain.BorrowRecord;
import com.py.javaf1.domain.SelectableBookWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class BorrowBookController {

    @FXML
    private TableView<SelectableBookWrapper> bookTable;
    @FXML
    private TableColumn<SelectableBookWrapper, String> bookIDColumn;
    @FXML
    private TableColumn<SelectableBookWrapper, String> titleColumn;
    @FXML
    private TableColumn<SelectableBookWrapper, String> authorColumn;
    @FXML
    private TableColumn<SelectableBookWrapper, String> publisherColumn;
    @FXML
    private TableColumn<SelectableBookWrapper, Integer> totalCopiesColumn;
    @FXML
    private TableColumn<SelectableBookWrapper, Integer> availableCopiesColumn;
    @FXML
    private TableColumn<SelectableBookWrapper, String> locationColumn;
    @FXML
    private TableColumn<SelectableBookWrapper, Integer> borrowCountColumn;
    @FXML
    private TableColumn<SelectableBookWrapper, Void> selectColumn;

    @FXML
    private ComboBox<String> attributeComboBox;
    @FXML
    private TextField searchField;

    private ObservableList<SelectableBookWrapper> bookData = FXCollections.observableArrayList();

    @FXML
    private void initialize() {
        setupTableColumns();
        loadBookData();  // 加载书籍数据
        attributeComboBox.getSelectionModel().selectFirst(); // 默认选择第一个属性
    }

    private void setupTableColumns() {
        bookIDColumn.setCellValueFactory(cellData -> cellData.getValue().getBook().bookIDProperty());
        titleColumn.setCellValueFactory(cellData -> cellData.getValue().getBook().titleProperty());
        authorColumn.setCellValueFactory(cellData -> cellData.getValue().getBook().authorProperty());
        publisherColumn.setCellValueFactory(cellData -> cellData.getValue().getBook().publisherProperty());
        totalCopiesColumn.setCellValueFactory(cellData -> cellData.getValue().getBook().totalCopiesProperty().asObject());
        availableCopiesColumn.setCellValueFactory(cellData -> cellData.getValue().getBook().availableCopiesProperty().asObject());
        locationColumn.setCellValueFactory(cellData -> cellData.getValue().getBook().locationProperty());
        borrowCountColumn.setCellValueFactory(cellData -> cellData.getValue().getBook().borrowCountProperty().asObject());

        addButtonToTable();
    }

    private void addButtonToTable() {
        selectColumn.setCellFactory(param -> new TableCell<>() {
            private final Button selectButton = new Button("选择");

            {
                selectButton.setOnAction(event -> {
                    SelectableBookWrapper bookWrapper = getTableView().getItems().get(getIndex());
                    bookWrapper.setSelected(!bookWrapper.isSelected());
                    selectButton.setText(bookWrapper.isSelected() ? "取消选择" : "选择");
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    SelectableBookWrapper bookWrapper = getTableView().getItems().get(getIndex());
                    selectButton.setText(bookWrapper.isSelected() ? "取消选择" : "选择");
                    setGraphic(selectButton);
                }
            }
        });
    }

    private void loadBookData() {
        List<Book> books = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader("src/main/java/com/py/javaf1/Data/BookData"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] bookDetails = line.split(",");
                if (bookDetails.length == 8) {
                    Book newBook = new Book(bookDetails[0], bookDetails[1], bookDetails[2], bookDetails[3],
                            Integer.parseInt(bookDetails[4]), Integer.parseInt(bookDetails[5]), bookDetails[6],
                            Integer.parseInt(bookDetails[7]));
                    books.add(newBook);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        setBookData(books);
    }

    public void setBookData(List<Book> books) {
        bookData.clear();
        for (Book book : books) {
            bookData.add(new SelectableBookWrapper(new BookWrapper(book)));
        }
        bookTable.setItems(bookData);
    }

    @FXML
    private void handleSearch() {
        String keyword = searchField.getText().trim().toLowerCase();
        String selectedAttribute = attributeComboBox.getValue();

        List<SelectableBookWrapper> filteredBooks = bookData.stream()
                .filter(wrapper -> {
                    Book book = wrapper.getBook().getBook();
                    switch (selectedAttribute) {
                        case "书籍编号":
                            return book.getBookID().toLowerCase().contains(keyword);
                        case "书名":
                            return book.getTitle().toLowerCase().contains(keyword);
                        case "作者":
                            return book.getAuthor().toLowerCase().contains(keyword);
                        case "出版社":
                            return book.getPublisher().toLowerCase().contains(keyword);
                        default:
                            return false;
                    }
                })
                .collect(Collectors.toList());

        bookTable.setItems(FXCollections.observableArrayList(filteredBooks));
    }

    @FXML
    private void handleShowAll() {
        bookTable.setItems(bookData);
    }

    @FXML
    private void handleBorrowSelectedBooks() {
        List<SelectableBookWrapper> selectedBooks = bookTable.getItems().stream()
                .filter(SelectableBookWrapper::isSelected)
                .collect(Collectors.toList());

        List<BorrowRecord> borrowRecords = loadBorrowRecords();

        for (SelectableBookWrapper wrapper : selectedBooks) {
            Book book = wrapper.getBook().getBook();
            if (book.getAvailableCopies() > 0) {
                book.setAvailableCopies(book.getAvailableCopies() - 1);
                book.setBorrowCount(book.getBorrowCount() + 1);
                BorrowRecord borrowRecord = new BorrowRecord(LoginController.readerId, book.getBookID(), new Date());
                borrowRecords.add(borrowRecord);
            } else {
                System.out.println("书籍《" + book.getTitle() + "》已无可借阅的副本。");
            }
        }

        saveBorrowRecords(borrowRecords);
        saveBookData();
        bookTable.refresh();
    }

    private List<BorrowRecord> loadBorrowRecords() {
        List<BorrowRecord> borrowRecords = new ArrayList<>();
        File file = new File("src/main/java/com/py/javaf1/Data/BorrowRecord");

        if (!file.exists() || file.length() == 0) {
            return borrowRecords; // Return an empty list if the file does not exist or is empty
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            borrowRecords = (List<BorrowRecord>) ois.readObject();
        } catch (EOFException e) {
            // Handle EOFException separately if needed, though this should not occur with the length check above
            System.out.println("BorrowRecord file is empty or corrupted.");
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return borrowRecords;
    }

    private void saveBorrowRecords(List<BorrowRecord> borrowRecords) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("src/main/java/com/py/javaf1/Data/BorrowRecord"))) {
            oos.writeObject(borrowRecords);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void saveBookData() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("src/main/java/com/py/javaf1/Data/BookData"))) {
            for (SelectableBookWrapper wrapper : bookData) {
                Book book = wrapper.getBook().getBook();
                String bookDetails = String.join(",",
                        book.getBookID(), book.getTitle(), book.getAuthor(), book.getPublisher(),
                        String.valueOf(book.getTotalCopies()), String.valueOf(book.getAvailableCopies()),
                        book.getLocation(), String.valueOf(book.getBorrowCount()));
                writer.write(bookDetails);
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
