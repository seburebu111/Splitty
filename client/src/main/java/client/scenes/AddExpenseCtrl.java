/*
 * Copyright 2021 Delft University of Technology
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Event;
import commons.Expense;
import commons.Participant;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.stage.Modality;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class AddExpenseCtrl {

    private final ServerUtils server;
    private final MainCtrl mainCtrl;
    private Event event;
    private Expense expense;
    private boolean en;

    ObservableList<String> types = FXCollections.observableArrayList("food", "venue", "transport", "activities", "other");
    ObservableList<String> currencies = FXCollections.observableArrayList("EUR", "USD");

    ObservableList<String> participants = FXCollections.observableArrayList();
    ObservableList<CheckBox> splitOptions = FXCollections.observableArrayList();

    @FXML
    private ChoiceBox<String> name;

    @FXML
    private TextField content;

    @FXML
    private TextField amount;

    @FXML
    private ChoiceBox<String> type;
    @FXML
    private ChoiceBox<String> currency;
    @FXML
    private DatePicker date;

    @FXML
    private Label title;
    @FXML
    private Label paid;
    @FXML
    private Label what;
    @FXML
    private Label howMany;
    @FXML
    private Label when;
    @FXML
    private Label howSplit;
    @FXML
    private CheckBox everyone;
    @FXML
    private CheckBox somePeople;
    @FXML
    private Label typeL;
    @FXML
    private Button addButton;
    @FXML
    private Button cancelButton;
    @FXML
    private ListView<CheckBox> menu;


    @Inject
    public AddExpenseCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.mainCtrl = mainCtrl;
        this.server = server;
    }

    @FXML
    void initialize(Event event, boolean en){
        if(event.getTypes() != null && event.getTypes().size() > 0)
            types = FXCollections.observableArrayList(event.getTypes());
        type.setValue("other");
        type.setItems(types);
        currency.setValue("EUR");
        currency.setItems(currencies);
        participants = FXCollections.observableArrayList();
        splitOptions = FXCollections.observableArrayList();
        for(Participant p : event.getParticipants()){
            participants.add(p.getName());
            splitOptions.add(new CheckBox(p.getName()));
        }
        menu.setItems(splitOptions);
        name.setItems(participants);
        everyone.setSelected(true);
        if(expense == null){
            name.setValue(" ");
        }
        else{
            content.setText(expense.getTitle());
            amount.setText(Float.toString(expense.getAmount()));
            name.setValue(expense.getPayer().getName());
            addButton.setText("Edit");
            if(!en) addButton.setText("Bewerk");
            //TODO: load the list of Splitters for the visual
        }

        this.en = en;
        language();
        this.event = server.getEvent(event.getId());
    }

    public void cancel() {
        clearFields();
        expense = null;
        mainCtrl.showEventOverview(event, en);
    }

    public void add() {
        try {
            //Expense expense = createExpense();
            LocalDate date = LocalDate.of(2024, 12, 12);
            Participant participant = new Participant("John", event);
            Expense expense = new Expense(event, date, participant, List.of(participant), "parking", 12.5f);

            if(expense != null) server.editExpense(expense);
            else server.addExpense(expense);

            //TODO: edit debts tied to expense
        } catch (Exception e) {

            var alert = new Alert(Alert.AlertType.ERROR);
            alert.initModality(Modality.APPLICATION_MODAL);
            if(en)
                alert.setContentText(e.getMessage() + "- internal error in adding the expense");
            else
                alert.setContentText(e.getMessage() + "- interne fout bij het toevoegen van de kosten");
            alert.showAndWait();
            return;
        }

        clearFields();
        mainCtrl.showEventOverview(event, en);
    }

    private Expense createExpense() {
        float amount;
        LocalDate date;
        List<Participant> debtors = new ArrayList<>();
        Participant payer = null;
        try{
            amount = Float.parseFloat(this.amount.getText());
            date = this.date.getValue();

            for(Participant p : event.getParticipants()){
                if(p.getName().equals(name.getValue())) payer = p;
            }

            if(everyone.isSelected()){
                debtors = event.getParticipants();
            }
            else if(somePeople.isSelected()){
                for(CheckBox c : splitOptions){
                    if(c.isSelected()){
                        for(Participant p : event.getParticipants()){
                            if(p.getName().equals(c.getText())) debtors.add(p);
                        }
                    }
                }
            }
        } catch (Exception e){
            var alert = new Alert(Alert.AlertType.ERROR);
            alert.initModality(Modality.APPLICATION_MODAL);
            alert.setContentText(e.getMessage());
            alert.showAndWait();
            return null;
        }
        //return new Expense(LocalDate localDate, Participant payer, List<Participant> debtors, String title, float amount);
        if(expense != null){
            if(expense.getLocalDate() != null) date = expense.getLocalDate();
            if(expense.getSplitters() != null) debtors = expense.getSplitters();
        }
        return new Expense(event, date, payer, debtors, content.getText(), amount);
    }

    @FXML
    private void handleEveryone(){
        if(everyone.isSelected()){
            somePeople.setSelected(false);
        }
    }
    @FXML
    private void handleSomePeople(){
        if(somePeople.isSelected()){
            everyone.setSelected(false);
        }
    }

    private void clearFields() {
        name.setValue(" ");
        content.clear();
        amount.clear();
        currency.setValue("EUR");
        //type.clear();
    }

    public void keyPressed(KeyEvent e) {
        switch (e.getCode()) {
            case ENTER:
                add();
                break;
            case ESCAPE:
                cancel();
                break;
            default:
                break;
        }
    }

    public void language(){
        if(en) en();
        else nl();
    }

    public void en(){
        title.setText("Add Expense");
        paid.setText("Who paid?");
        what.setText("What for?");
        howMany.setText("How much?");
        typeL.setText("Type:");
        addButton.setText("Add");
        cancelButton.setText("Cancel");
        when.setText("When?");
        howSplit.setText("How to split?");
        everyone.setText("With Everyone");
        somePeople.setText("Only some people");
    }
    public void nl(){
        title.setText("Kosten Toevoegen");
        paid.setText("Wie heeft betaald?");
        what.setText("Waarvoor?");
        howMany.setText("Hoe veel?");
        typeL.setText("Type:");
        addButton.setText("Toevoegen");
        cancelButton.setText("Annuleren");
        when.setText("Wanneer?");
        howSplit.setText("Hoe splitsen?");
        everyone.setText("Met iedereen");
        somePeople.setText("Slechts enkele mensen");
    }

    public void setExpense(Expense expense) {
        this.expense = expense;
    }
}