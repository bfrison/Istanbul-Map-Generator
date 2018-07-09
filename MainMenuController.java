package istanbulmapgenerator;

import istanbulmapgenerator.IstanbulMap.GameVersion;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;

public class MainMenuController implements Initializable {

    @FXML
    private Label label;
    @FXML
    private ToggleGroup toggleGroup1;
    @FXML
    private RadioButton rb_original;
    @FXML
    private RadioButton rb_mnb;

    @FXML
    private void generateMap(ActionEvent event) throws IOException {
        Toggle toggle = toggleGroup1.getSelectedToggle();
        GameVersion gameVersion;
        Parent root;
        if (toggle.equals(rb_original)) {
            gameVersion = GameVersion.ORIGINAL;
            root = FXMLLoader.load(getClass().getResource("DisplayOriginalMap.fxml"));
        } else if (toggle.equals(rb_mnb)) {
            gameVersion = GameVersion.MOCHA_BAKSHEESH;
            root = FXMLLoader.load(getClass().getResource("DisplayMochaBaksheeshMap.fxml"));
        } else {
            throw new IllegalArgumentException("Game Version not Supported");
        }
        Scene scene = new Scene(root);
        createMap(scene, gameVersion);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.show();

    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {}

    private void createMap(Scene scene, GameVersion gameVersion) {
        IstanbulMap istanbulMap = new IstanbulMap(gameVersion);
        for (int i = 0; i < istanbulMap.getMapSize(); i++) {
            Label mapValue = (Label) scene.lookup("#index" + i);
            mapValue.setText(Integer.toString(istanbulMap.getTiles().get(i)));
        }
    }
}
