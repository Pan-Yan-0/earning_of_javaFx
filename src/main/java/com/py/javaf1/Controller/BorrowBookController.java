package com.py.javaf1.Controller;

import com.py.javaf1.domain.Reader;
import com.py.javaf1.domain.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

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
    private Reader nowReader = new Reader();
    private final ObservableList<SelectableBookWrapper> bookData = FXCollections.observableArrayList();

    @FXML
    private void initialize() {
        setupTableColumns();
        loadBookData();  // 加载书籍数据
        loadReaders();
        attributeComboBox.getSelectionModel().selectFirst(); // 默认选择第一个属性
    }

    /**
     * 设置表格列属性和按钮
     */
    private void setupTableColumns() {
        // 设置表格列与数据的绑定
        bookIDColumn.setCellValueFactory(cellData -> cellData.getValue().getBook().bookIDProperty());
        titleColumn.setCellValueFactory(cellData -> cellData.getValue().getBook().titleProperty());
        authorColumn.setCellValueFactory(cellData -> cellData.getValue().getBook().authorProperty());
        publisherColumn.setCellValueFactory(cellData -> cellData.getValue().getBook().publisherProperty());
        totalCopiesColumn.setCellValueFactory(cellData -> cellData.getValue().getBook().totalCopiesProperty().asObject());
        availableCopiesColumn.setCellValueFactory(cellData -> cellData.getValue().getBook().availableCopiesProperty().asObject());
        locationColumn.setCellValueFactory(cellData -> cellData.getValue().getBook().locationProperty());
        borrowCountColumn.setCellValueFactory(cellData -> cellData.getValue().getBook().borrowCountProperty().asObject());

        // 向表格中的选择列添加按钮
        addButtonToTable();
    }

    /**
     * 向表格中的选择列添加按钮
     */
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

    /**
     * 加载书籍数据
     */
    private void loadBookData() {
        List<Book> books = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader("src/main/java/com/py/javaf1/Data/BookData"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] bookDetails = line.split(",");
                if (bookDetails.length == 8) {
                    // 解析书籍数据并添加到列表中
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

    /**
     * 设置表格中显示的书籍数据
     *
     * @param books 要显示的书籍列表
     */
    public void setBookData(List<Book> books) {
        bookData.clear();
        for (Book book : books) {
            bookData.add(new SelectableBookWrapper(new BookWrapper(book)));
        }
        bookTable.setItems(bookData);
    }

    @FXML
    private void handleSearch() {
        // 获取搜索关键字和选定的搜索属性
        String keyword = searchField.getText().trim().toLowerCase();
        String selectedAttribute = attributeComboBox.getValue();

        // 过滤书籍数据并更新表格显示
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
        // 显示所有书籍
        bookTable.setItems(bookData);
    }

    @FXML
    private void handleBorrowSelectedBooks() {
        // 获取选择的书籍
        List<SelectableBookWrapper> selectedBooks = bookTable.getItems().stream()
                .filter(SelectableBookWrapper::isSelected)
                .collect(Collectors.toList());

        List<BorrowRecord> borrowRecords = new ArrayList<>();
        // 检查是否超过读者的借阅上限
        if (selectedBooks.size() > nowReader.getBorrowLimit()) {
            showAlert("失败", "已经超过借阅书籍的上限了！！！" + "上限为：" + nowReader.getBorrowLimit());
            return;
        }
        // 处理每本选择的书籍
        for (SelectableBookWrapper wrapper : selectedBooks) {
            Book book = wrapper.getBook().getBook();
            if (book.getAvailableCopies() > 0) {
                // 更新书籍的可借数量和借阅次数
                book.setAvailableCopies(book.getAvailableCopies() - 1);
                book.setBorrowCount(book.getBorrowCount() + 1);
                // 创建借阅记录并添加到列表中
                BorrowRecord borrowRecord = new BorrowRecord(LoginController.readerId, book.getBookID(), new Date().toString(),
                        null);
                borrowRecords.add(borrowRecord);
            } else {
                showAlert("失败", "书籍《" + book.getTitle() + "》已无可借阅的副本。");
                return;
            }
        }
        // 更新读者的借阅上限、保存借阅记录和书籍数据，并刷新表格显示
        updateReaderBorrowLimit(nowReader.getReaderID(), nowReader.getBorrowLimit() - selectedBooks.size());
        saveBorrowRecords(borrowRecords);
        saveBookData();  // 保存书籍数据
        loadBookData();  // 重新加载书籍数据，刷新展示
        showAlert("成功", "已经成功借阅书籍！！！");
        bookTable.refresh();
    }

    /**
     * 保存借阅记录到文件
     *
     * @param borrowRecords 要保存的借阅记录列表
     */
    private void saveBorrowRecords(List<BorrowRecord> borrowRecords) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("src/main/java/com/py/javaf1/Data" +
                "/BorrowRecord", true))) {
            for (BorrowRecord borrowRecord : borrowRecords) {
                // 将借阅记录转换为字符串并写入文件
                String writeDetails = String.join(",", borrowRecord.getReaderID(), borrowRecord.getBookID(),
                        borrowRecord.getBorrowDate().toString());
                writer.write(writeDetails);
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 保存书籍数据到文件
     */
    private void saveBookData() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("src/main/java/com/py/javaf1/Data/BookData"))) {
            for (SelectableBookWrapper wrapper : bookData) {
                Book book = wrapper.getBook().getBook();
                // 将书籍对象转换为字符串并写入文件
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

    /**
     * 显示警告对话框
     *
     * @param title   对话框标题
     * @param message 对话框消息内容
     */
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * 更新读者的借阅上限
     *
     * @param readerID       读者ID
     * @param newBorrowLimit 新的借阅上限
     */
    private void updateReaderBorrowLimit(String readerID, int newBorrowLimit) {
        List<Reader> readers = loadReaders();
        for (Reader reader : readers) {
            if (reader.getReaderID().equals(readerID)) {
                reader.setBorrowLimit(newBorrowLimit);
                break;
            }
        }
        saveReaders(readers);
    }

    /**
     * 加载读者信息
     *
     * @return 读者列表
     */
    private List<Reader> loadReaders() {
        List<Reader> readers = new ArrayList<>();
        try (BufferedReader reader =
                     new BufferedReader(new FileReader("src/main/java/com/py/javaf1/Data/ReaderData"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] readerDetails = line.split(",");
                if (readerDetails.length == 7) {
                    // 解析读者数据并添加到列表中
                    Reader newReader = new Reader(readerDetails[0], readerDetails[1], readerDetails[2],
                            readerDetails[3],
                            Integer.parseInt(readerDetails[4]), Integer.parseInt(readerDetails[5]),
                            Double.parseDouble(readerDetails[6]));
                    readers.add(newReader);
                    if (newReader.getReaderID().equals(LoginController.readerId)) {
                        nowReader = newReader;  // 找到当前登录的读者信息
                    }
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return readers;
    }

    /**
     * 保存读者信息到文件
     *
     * @param readers 要保存的读者列表
     */
    private void saveReaders(List<Reader> readers) {
        try (BufferedWriter writer =
                     new BufferedWriter(new FileWriter("src/main/java/com/py/javaf1/Data/ReaderData"))) {
            for (Reader reader : readers) {
                // 将读者对象转换为字符串并写入文件
                String readerDetails = String.join(",",
                        reader.getName(), reader.getPassword(), reader.getReaderID(), reader.getStudentID(),
                        String.valueOf(reader.getBorrowLimit()), String.valueOf(reader.getHasOverdue()),
                        String.valueOf(reader.getHasDebt()));
                writer.write(readerDetails);
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
