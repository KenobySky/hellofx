package br.sky.controllers;

import br.sky.DAO.FruitDAO;
import br.sky.DAO.HibernateUtil;
import br.sky.models.Fruit;
import br.sky.tools.Executor;
import java.awt.Toolkit;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.converter.LongStringConverter;
import javax.persistence.EntityManager;

public class FruitsController  {

    @FXML
    private TableColumn<Fruit, Long> fruitId;

    @FXML
    private TableColumn<Fruit, String> fruitColor;

    @FXML
    private TableView<Fruit> fruitsTable;

    @FXML
    private Label labelTitle;

    @FXML
    private Button buttonSave;

    @FXML
    private Button undoAction;

    @FXML
    private MenuItem addFruitButton;

    @FXML
    private MenuItem removeFruitButton;

    public FruitsController() {

    }

    @FXML
    void addFruit(ActionEvent event) {
        //
        Fruit fruit = new Fruit();
        fruit.setColor("?");
        //

        Task task = new Task() {
            @Override
            public Boolean call() throws Exception {
                EntityManager em = HibernateUtil.getEntityManager();

                FruitDAO fruitDAO = new FruitDAO();
                fruitDAO.add(fruit, em);
                fruitsTable.getItems().add(fruit);

                return true;
            }
        };

        task.setOnRunning(new EventHandler() {
            @Override
            public void handle(Event event) {
                fruitsTable.getScene().setCursor(Cursor.WAIT);
            }
        });

        task.setOnFailed(new EventHandler() {
            @Override
            public void handle(Event event) {
                task.getException().printStackTrace();
                fruitsTable.getScene().setCursor(Cursor.DEFAULT);
            }
        });

        task.setOnSucceeded(new EventHandler() {
            @Override
            public void handle(Event event) {
                fruitsTable.getScene().setCursor(Cursor.DEFAULT);
            }
        });

        Executor.getExecutorService().execute(task);
    }

    @FXML
    void removeFruit(ActionEvent event) {

        int i = fruitsTable.getSelectionModel().getSelectedIndex();

        if (i >= 0) {
            Task task = new Task() {
                @Override
                public Boolean call() throws Exception {
                    Fruit selectedItem = fruitsTable.getSelectionModel().getSelectedItem();

                    EntityManager em = HibernateUtil.getEntityManager();

                    FruitDAO fruitDAO = new FruitDAO();
                    fruitDAO.delete(selectedItem, em);

                    fruitsTable.getItems().remove(i);

                    return true;
                }
            };

            task.setOnRunning(new EventHandler() {
                @Override
                public void handle(Event event) {
                    fruitsTable.getScene().setCursor(Cursor.WAIT);
                }
            });

            task.setOnFailed(new EventHandler() {
                @Override
                public void handle(Event event) {
                    task.getException().printStackTrace();
                    fruitsTable.getScene().setCursor(Cursor.DEFAULT);
                }
            });

            task.setOnSucceeded(new EventHandler() {
                @Override
                public void handle(Event event) {
                    fruitsTable.getScene().setCursor(Cursor.DEFAULT);
                }
            });

            Executor.getExecutorService().execute(task);

        } else {
            Toolkit.getDefaultToolkit().beep();
        }
    }

    @FXML
    void saveFruits(ActionEvent event) {

        Task task = new Task() {
            @Override
            public Boolean call() throws Exception {
                EntityManager em = HibernateUtil.getEntityManager();
                FruitDAO dao = new FruitDAO();
                dao.updateAll(fruitsTable.getItems(), em);
                //loadFruits();

                return true;
            }
        };

        task.setOnRunning(new EventHandler() {
            @Override
            public void handle(Event event) {
                fruitsTable.getScene().setCursor(Cursor.WAIT);
            }
        });

        task.setOnFailed(new EventHandler() {
            @Override
            public void handle(Event event) {
                task.getException().printStackTrace();
                fruitsTable.getScene().setCursor(Cursor.DEFAULT);
            }
        });

        task.setOnSucceeded(new EventHandler() {
            @Override
            public void handle(Event event) {
                fruitsTable.getScene().setCursor(Cursor.DEFAULT);
            }
        });

        Executor.getExecutorService().execute(task);

    }

    @FXML
    void undoLastAction(ActionEvent event) {
        EntityManager em = HibernateUtil.getEntityManager();
        em.getTransaction().begin();
        em.getTransaction().rollback();

    }

    @FXML
    public void initialize() {
        fruitId.setCellValueFactory(new PropertyValueFactory<>("id"));
        fruitColor.setCellValueFactory(new PropertyValueFactory<>("color"));

        fruitId.setCellFactory(TextFieldTableCell.forTableColumn(new LongStringConverter()));
        fruitId.setOnEditCommit((CellEditEvent<Fruit, Long> t) -> {
            ((Fruit) t.getTableView().getItems().get(
                    t.getTablePosition().getRow())).setId(t.getNewValue());
        });

        fruitColor.setCellFactory(TextFieldTableCell.forTableColumn());
        fruitColor.setOnEditCommit((CellEditEvent<Fruit, String> t) -> {
            ((Fruit) t.getTableView().getItems().get(
                    t.getTablePosition().getRow())).setColor(t.getNewValue());
        });

        fruitColor.setEditable(true);
        fruitsTable.setEditable(true);

        HibernateUtil.initHibernate();

        loadFruits();

        
        //GlyphFont fontAwesome = GlyphFontRegistry.font("FontAwesome");
        //fontAwesome.create("fa-address-card");
        
    }

    /**
     * *
     * It returns and load from hibernate
     *
     * @return
     */
    private ObservableList<Fruit> loadFruits() {

        Task task = new Task() {
            @Override
            public Boolean call() throws Exception {

                EntityManager em = HibernateUtil.getEntityManager();
                FruitDAO fruitDAO = new FruitDAO();
                ObservableList<Fruit> observableArrayList = FXCollections.observableArrayList(fruitDAO.getList(em));
                fruitsTable.setItems(observableArrayList);

                return true;
            }
        };

        task.setOnRunning(new EventHandler() {
            @Override
            public void handle(Event event) {
                fruitsTable.getScene().setCursor(Cursor.WAIT);
            }
        });

        task.setOnFailed(new EventHandler() {
            @Override
            public void handle(Event event) {
                task.getException().printStackTrace();
                fruitsTable.getScene().setCursor(Cursor.DEFAULT);
            }
        });

        task.setOnSucceeded(new EventHandler() {
            @Override
            public void handle(Event event) {
                fruitsTable.getScene().setCursor(Cursor.DEFAULT);
            }
        });

        Executor.getExecutorService().execute(task);

        return null;
    }

    private void dispose() {

    }

}
