package cz.fel.ds.gui.medicalRecord;

import cz.fel.ds.database.model.MedicalRecord;
import cz.fel.ds.database.model.Patient;
import cz.fel.ds.database.services.BasicService;
import cz.fel.ds.database.services.SearchService;
import cz.fel.ds.gui.GuiTool;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;

/**
 * Created by Tom on 15. 5. 2015.
 */
public class MedicalRecordController {
    private SearchService searchService = new SearchService();
    private BasicService basicService = new BasicService();

    private Patient p;

    @FXML
    private DatePicker date;
    @FXML
    private TextField height;
    @FXML
    private TextField weight;
    @FXML
    private TextField fatPercentage;
    @FXML
    private TextField waistPerimeter;
    @FXML
    private TextField hipPerimeter;
    @FXML
    private TextField chestPerimeter;
    @FXML
    private TableView<MedicalRecord> medicalRecordTable;
    @FXML
    private Button delete;
    @FXML
    private TableColumn<MedicalRecord, String> mrDateColumn;

    private ObservableList<MedicalRecord> dataMedicalRecords;

    @FXML
    public void addMedicalRecord(ActionEvent event) {
        System.out.println("add");

        try {
            System.err.println(p);

            MedicalRecord mr = new MedicalRecord(p, GuiTool.localDateToDate(date.getValue()));
            mr.setHeight(GuiTool.stringToDouble(height.getText()));
            mr.setWeight(GuiTool.stringToDouble(weight.getText()));
            mr.setFat(GuiTool.stringToDouble(fatPercentage.getText()));
            mr.setWaist(GuiTool.stringToDouble(waistPerimeter.getText()));
            mr.setHip(GuiTool.stringToDouble(hipPerimeter.getText()));
            mr.setChest(GuiTool.stringToDouble(chestPerimeter.getText()));
            basicService.saveMedicalRecord(mr);

            System.err.println(p);
        } catch (Exception e) {
            System.out.println("error input parameters");
        }
        refreshTable();
    }

    @FXML
    public void deleteMedicalRecord(ActionEvent event) {
        try {
            MedicalRecord mr = medicalRecordTable.getSelectionModel().getSelectedItem();
            if (mr == null) throw new Exception();
            basicService.deleteMedicalRecord(mr);
            refreshTable();
        } catch (Exception e) {
            System.out.println("chyba medical record");
        }
    }

    public void init(Patient p) {
        this.p = p;

        if (p == null) {
            System.err.println("Sorry error");
            GuiTool.closeDialog(date);
        }

        System.err.println(p);

        date.setValue(LocalDate.now());
        // medicalRecordTable.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("date"));
        mrDateColumn.setCellValueFactory(cellValue -> {
            DateFormat df = new SimpleDateFormat("dd.MM.yyyy");
            return new SimpleStringProperty(df.format(new Date(cellValue.getValue().getDate().getTime())));
        });
        medicalRecordTable.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("height"));
        medicalRecordTable.getColumns().get(2).setCellValueFactory(new PropertyValueFactory<>("weight"));
        medicalRecordTable.getColumns().get(3).setCellValueFactory(new PropertyValueFactory<>("fat"));
        medicalRecordTable.getColumns().get(4).setCellValueFactory(new PropertyValueFactory<>("waist"));
        medicalRecordTable.getColumns().get(5).setCellValueFactory(new PropertyValueFactory<>("hip"));
        medicalRecordTable.getColumns().get(6).setCellValueFactory(new PropertyValueFactory<>("chest"));


        dataMedicalRecords = FXCollections.observableArrayList();
        medicalRecordTable.setItems(dataMedicalRecords);
        refreshTable();


    }

    public void refreshTable() {
        dataMedicalRecords.clear();
        dataMedicalRecords.addAll(searchService.medicalRecordsByPatient(p));
    }

}
