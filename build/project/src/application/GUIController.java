package application;

import java.net.URL;
import java.util.Arrays;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class GUIController implements Initializable {

	Powerball pb;
	Main m; //I don't like this code...

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		pb = new Powerball();
		pb.importWinningNumbers();
		dateLabel.setText(pb.latestDate());
		m = new Main();
	}

	@FXML
	public Button generateBtn;

	@FXML
	public Button updateBtn;

	@FXML
	public Label PBLabel;

	@FXML
	public Label dateLabel;

	@FXML
	public void getPBNums(){

		int[][] pbNums = pb.multiPBNums(1);
		PBLabel.setText(Arrays.toString(pbNums[0]));
	}

	@FXML
	public void updateWinners(){

		pb.update();
		pb.importWinningNumbers();
		dateLabel.setText(pb.latestDate());
		m.popup();
	}


}
