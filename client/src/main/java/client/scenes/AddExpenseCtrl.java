package client.scenes;

        import com.google.inject.Inject;

        import client.utils.ServerUtils;
        import commons.Expense;
        import commons.Participant;
        import commons.Person;
        import commons.Quote;
        import jakarta.ws.rs.WebApplicationException;
        import javafx.fxml.FXML;
        import javafx.scene.control.Alert;
        import javafx.scene.control.TextField;
        import javafx.scene.input.KeyEvent;
        import javafx.stage.Modality;

        import static java.lang.Float.parseFloat;

public class AddExpenseCtrl {

    private final ServerUtils server;
    private final MainCtrl mainCtrl;

    @FXML
    private TextField name;

    @FXML
    private TextField title;

    @FXML
    private TextField amount;

    @Inject
    public AddExpenseCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.mainCtrl = mainCtrl;
        this.server = server;

    }

    public void cancel() {
        clearFields();
        mainCtrl.showOverview();
    }

    public void ok() {
        try {
            server.addExpense(getExpense());
        } catch (WebApplicationException e) {

            var alert = new Alert(Alert.AlertType.ERROR);
            alert.initModality(Modality.APPLICATION_MODAL);
            alert.setContentText(e.getMessage());
            alert.showAndWait();
            return;
        }

        clearFields();
        mainCtrl.showOverview();
    }

    private Expense getExpense() {
        var p = new Participant(name.getText());
        var a = parseFloat(amount.getText());
        var t = title.getText();
        return new Expense(p, t, a);
    }

    private void clearFields() {
        name.clear();
        amount.clear();
        title.clear();
    }

    public void keyPressed(KeyEvent e) {
        switch (e.getCode()) {
            case ENTER:
                ok();
                break;
            case ESCAPE:
                cancel();
                break;
            default:
                break;
        }
    }
}