import javafx.application.Application;
import javafx.beans.property.*;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.Map;

public class GUI extends Application {
    private Message message = new Message();

    @Override
    public void start(Stage stage) {
        // Title
        Label title = new Label("Echotext Tester");
        title.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

        // Map of Hosts
        Map<String, String> hostMap = new LinkedHashMap<>();
        hostMap.put("localhost", "localhost");
        hostMap.put("host", "10.0.1.1");

        // Host and Port
        ComboBox<String> hostCombo = new ComboBox<>();
        hostCombo.getItems().addAll(hostMap.keySet());
        hostCombo.setValue("localhost");
        TextField hostField = new TextField("localhost");
        hostField.setText(hostMap.get("localhost"));
        hostCombo.valueProperty().addListener((obs, oldVal, newVal) -> {
            hostField.setText(hostMap.getOrDefault(newVal, "localhost"));
        });
        Label portLabel = new Label("Port:");
        TextField portField = new TextField("12345");

        HBox hostBox = new HBox(10, hostCombo, hostField, portLabel, portField);
        hostBox.setPadding(new Insets(10));

        // Separator
        Separator sep1 = new Separator();

        // Case selector
        ComboBox<String> caseCombo = new ComboBox<>();
        Map<String, Message> caseMap = createSampleCases();
        caseCombo.getItems().addAll(caseMap.keySet());
        caseCombo.valueProperty().addListener((obs, old, val) -> {
            if (val != null) message.copyFrom(caseMap.get(val));
        });
        HBox caseBox = new HBox(10, new Label("Case:"), caseCombo);
        caseBox.setPadding(new Insets(10));

        // Separator
        Separator sep2 = new Separator();

        // Message fields
        GridPane form = new GridPane();
        form.setPadding(new Insets(10));
        form.setHgap(10);
        form.setVgap(8);

        String[] labels = {
          "fallid", "patid", "pat_name",
          "pat_vorname", "case_id", "date", "diagnose"
        };
        for (int i = 0; i < labels.length; i++) {
            Label lbl = new Label(labels[i] + ":");
            TextField tf = new TextField();
            switch (labels[i]) {
                case "fallid":       bind(tf, message.fallIdProperty()); break;
                case "patid":        bind(tf, message.patIdProperty());  break;
                case "pat_name":     bind(tf, message.patNameProperty()); break;
                case "pat_vorname":  bind(tf, message.patVornameProperty()); break;
                case "case_id":      bind(tf, message.caseIdProperty());    break;
                case "date":         bind(tf, message.dateProperty());      break;
                case "diagnose":     bind(tf, message.diagnoseProperty());  break;
            }
            form.add(lbl, 0, i);
            form.add(tf, 1, i);
        }

        // Layout assembly
        VBox root = new VBox(8,
            title, hostBox, sep1, caseBox, sep2, form
        );
        root.setPadding(new Insets(15));

        Scene scene = new Scene(root, 600, 450);
        scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());

        stage.setScene(scene);
        stage.setTitle("Echotext Tester");
        stage.show();
    }

    private void bind(TextField tf, StringProperty prop) {
        tf.textProperty().bindBidirectional(prop);
    }

    private Map<String, Message> createSampleCases() {
        Map<String, Message> map = new LinkedHashMap<>();
        map.put("1.1 case1", new Message("F11","P11","Müller","Anna","C11", LocalDate.now(), "DX1"));
        map.put("1.2 case2", new Message("F12","P12","Schmidt","Bernd","C12", LocalDate.now().minusDays(1), "DX2"));
        // … add up to 3.3
        return map;
    }

    public static void main(String[] args) {
        launch();
    }

    // Message model
    public static class Message {
        private StringProperty fallId = new SimpleStringProperty();
        private StringProperty patId  = new SimpleStringProperty();
        private StringProperty patName = new SimpleStringProperty();
        private StringProperty patVorname = new SimpleStringProperty();
        private StringProperty caseId = new SimpleStringProperty();
        private StringProperty date   = new SimpleStringProperty();
        private StringProperty diagnose = new SimpleStringProperty();

        public Message() {}
        public Message(String f, String p, String pn, String pv, String c, LocalDate d, String dx) {
            this.fallId.set(f);
            this.patId.set(p);
            this.patName.set(pn);
            this.patVorname.set(pv);
            this.caseId.set(c);
            this.date.set(d.toString());
            this.diagnose.set(dx);
        }
        public void copyFrom(Message o) {
            setFallId(o.getFallId());
            setPatId(o.getPatId());
            setPatName(o.getPatName());
            setPatVorname(o.getPatVorname());
            setCaseId(o.getCaseId());
            setDate(o.getDate());
            setDiagnose(o.getDiagnose());
        }
        // Getters, setters, and properties
        public String getFallId() { return fallId.get(); }
        public void setFallId(String v) { fallId.set(v); }
        public StringProperty fallIdProperty() { return fallId; }

        public String getPatId() { return patId.get(); }
        public void setPatId(String v) { patId.set(v); }
        public StringProperty patIdProperty() { return patId; }

        public String getPatName() { return patName.get(); }
        public void setPatName(String v) { patName.set(v); }
        public StringProperty patNameProperty() { return patName; }

        public String getPatVorname() { return patVorname.get(); }
        public void setPatVorname(String v) { patVorname.set(v); }
        public StringProperty patVornameProperty() { return patVorname; }

        public String getCaseId() { return caseId.get(); }
        public void setCaseId(String v) { caseId.set(v); }
        public StringProperty caseIdProperty() { return caseId; }

        public String getDate() { return date.get(); }
        public void setDate(String v) { date.set(v); }
        public StringProperty dateProperty() { return date; }

        public String getDiagnose() { return diagnose.get(); }
        public void setDiagnose(String v) { diagnose.set(v); }
        public StringProperty diagnoseProperty() { return diagnose; }
    }
}
