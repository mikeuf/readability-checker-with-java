package readability.checker;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.web.HTMLEditor;
import javafx.stage.Stage;

/**
 * ReadabilityChecker.java
 * 
 * The Readability Checker performs a Flesch-Kincaid (FK)
 * readability analysis: Scans the characters in the text and counts the number
 * of words, syllables and sentences in order to determine the readability.
 *
 * Classes 
 * - RCScanner: Scans the text and counts the number of items found,
 * such as words, syllables and sentences. 
 * - RCTracker: Stores counters of items found by the scanner and performs 
 * Flesch-Kincaid calculations using the counters
 *
 * @author Mike Black
 */
public class ReadabilityChecker extends Application {

  @Override
    public void start(Stage primaryStage) {

      // BorderPane will be the main GUI controller for the scene
      BorderPane borderPane = new BorderPane();
      Scene scene = new Scene(borderPane, 1200, 745);

      // top bar that will contain the "Check Readability" button
      HBox hb = new HBox();
      hb.setSpacing(10);
      hb.setStyle("-fx-background-color: #336699;");
      hb.setPadding(new Insets(20, 25, 20, 50));

			// bottom bar
			HBox hb2 = new HBox();
			hb2.setStyle("-fx-background-color: #336699;");
      hb2.setPadding(new Insets(15, 25, 15, 50));

      // large rich text editor in center of screen
      HTMLEditor htmlEditor = new HTMLEditor();
			

      // left sidebar with statistics
      GridPane grid = new GridPane();
      grid.setAlignment(Pos.TOP_LEFT);
      grid.setHgap(10);
      grid.setVgap(1);
      grid.setPadding(new Insets(40, 25, 25, 50));

      // add the controllers to the main borderPane
      borderPane.setTop(hb);
      borderPane.setLeft(grid);
      borderPane.setCenter(htmlEditor);
			borderPane.setBottom(hb2);

      // text labels and fields used in the left sidebar for statistics
      Label lblScoreAndAnalysis = new Label("Score and Analysis");
      lblScoreAndAnalysis.setFont(Font.font("Verdana", FontWeight.BOLD, 18));
      Label lblDetails = new Label("Details");
      lblDetails.setFont(Font.font("Verdana", FontWeight.BOLD, 18));
      final Label lblWords = new Label("Word count: ");
      Label lblWordCount = new Label();
      final Label lblSyllables = new Label("Syllable count: ");
      Label lblSyllableCount = new Label();
      final Label lblSentences = new Label("Sentence count: ");
      Label lblSentenceCount = new Label();
      final Label lblFKGrade = new Label("Flesch-Kincaid Grade: ");
      Label lblFKGradeNumber = new Label();
      final Label lblFKScore = new Label("Flesch-Kincaid Score: ");
      Label lblFKScoreNumber = new Label();
      final Label lblSyllablesPerWord = new Label("Syllables Per Word: ");
      Label lblSyllablesPerWordCount = new Label();
      final Label lblWordsPerSentence = new Label("Words Per Sentence: ");
      Label lblWordsPerSentenceCount = new Label();
      final Label lblStatus = new Label("Status");
      lblStatus.setFont(Font.font("Verdana", FontWeight.BOLD, 18));
      Label lblStatusDetails = new Label("Ready to scan");

      // progress bar is used as a visual gauge for the user's score
      ProgressBar meterFKScore = new ProgressBar(0);
      meterFKScore.setMinWidth(185);

      // the pane objects are just used for padding in the left sidebar
      Pane spring = new Pane();
      Pane spring2 = new Pane();
      spring.setPrefSize(210, 25);
      spring2.setPrefSize(210, 25);

      // populate the left sidebar with label and text fields
      grid.add(lblScoreAndAnalysis, 0, 0, 2, 1);
      grid.add(meterFKScore, 0, 1, 2, 1);
      grid.add(lblFKGrade, 0, 2);
      grid.add(lblFKGradeNumber, 1, 2);
      grid.add(lblFKScore, 0, 3);
      grid.add(lblFKScoreNumber, 1, 3);
      grid.add(spring, 0, 4, 2, 1);
      grid.add(lblDetails, 0, 5, 2, 1);
      grid.add(lblWords, 0, 6);
      grid.add(lblWordCount, 1, 6);
      grid.add(lblSyllables, 0, 7);
      grid.add(lblSyllableCount, 1, 7);
      grid.add(lblSentences, 0, 8);
      grid.add(lblSentenceCount, 1, 8);
      grid.add(lblSyllablesPerWord, 0, 9);
      grid.add(lblSyllablesPerWordCount, 1, 9);
      grid.add(lblWordsPerSentence, 0, 10);
      grid.add(lblWordsPerSentenceCount, 1, 10);
      grid.add(spring2, 0, 11, 2, 1);
      grid.add(lblStatus, 0, 12, 2, 1);
      grid.add(lblStatusDetails, 0, 13);

      // add the "Check Readability" button to the top hbox 
      Button btnCheckReadability = new Button("Check Readability");
      hb.getChildren().add(btnCheckReadability);
	
			/**
			 * When the Check Readability button is clicked it instantiates the
       * RCScanner and RCTracker classes objects. It then populates the form 
			 * labels with the various counts and scores and updates the meter
			 * (progress bar) to reflect the score
			 */
      btnCheckReadability.setOnAction((ActionEvent event) -> {
          RCScanner scanner = new RCScanner();
          RCTracker tracker = new RCTracker();
					StringBuilder plainText = new StringBuilder();

					// append a newline at the end to ensure all sentences are counted
					plainText.append(stripHtmlTags(htmlEditor.getHtmlText()));
					plainText.append(System.getProperty("line.separator"));
					
					// scan the text and populate the counters
          tracker.setNumSentences(scanner.countSentences(plainText.toString()));
          tracker.setNumWords(scanner.countWords(plainText.toString()));
          tracker.setNumSyllables(scanner.countSyllables(plainText.toString()));
          tracker.setNumIPs(scanner.countIPs(plainText.toString()));

         /* Adjust some counters for special cases, such as IP addresses.
          * Then perform the calculations.
          */
          tracker.applyModifiers();
          tracker.calculateNumSyllablesPerWord();
          tracker.calculateNumWordsPerSentence();
          tracker.calculateFKGrade();
          tracker.calculateFKScore();

					// declaring a local variable here for the sake of readability
          double FKScore = tracker.getFKScore(); 
          
				 /* update the meter, which is a progress bar control, according
          * to the FKScore. The bar changes color as well 
		      */
          meterFKScore.setProgress(FKScore / 100);

         /* set a minimum bar length for really low scores, so the bar is 
			    * not completely empty
          */
          if (FKScore <= 5) {
            meterFKScore.setProgress(0.05);
          }

          // change bar color and status depending on the score
          if (FKScore >= 0 && FKScore < 40) {
            meterFKScore.setStyle(" -fx-accent: #ff1a53");
            lblStatusDetails.setText("Difficult Readability:\n"
                + "Break-up paragraphs\n"
                + "into bullet lists or\n"
                + "steps. Write shorter\n"
                + "sentences with simpler\n"
                + "words.");
          } else if (FKScore >= 40 && FKScore < 60) {
            meterFKScore.setStyle(" -fx-accent: gold");
            lblStatusDetails.setText("Medium Readability:\n"
                + "Break-up paragraphs\n"
                + "into bullet lists or\n"
                + "steps. Write shorter\n"
                + "sentences with simpler\n"
                + "words.");
          } else {
            meterFKScore.setStyle(" -fx-accent: lightGreen");
            lblStatusDetails.setText("Good Readability:\n"
                + "This text should be\n"
                + "understood by most\n"
                + "readers.");
          }
					
          // if the word count is low, warn the user that the score may not be useful
          if (tracker.getNumWords() <= 20) {
              lblStatusDetails.setText("Low Word Count:\n" +
                  "The sample size is small.\n" +
                  "This may result in\n" +
                  "unreliable scores.");
          }

					// populate sidebar statistcs
          DecimalFormat decimal = new DecimalFormat("#.##");
          decimal.setRoundingMode(RoundingMode.HALF_UP);

          lblWordCount.setText(tracker.getNumWords().toString());
          lblSyllableCount.setText(tracker.getNumSyllables().toString());
          lblSentenceCount.setText(tracker.getNumSentences().toString());
          lblSyllablesPerWordCount.setText(decimal.format(tracker.getNumSyllablesPerWord()));
          lblWordsPerSentenceCount.setText(decimal.format(tracker.getNumWordsPerSentence()));
          lblFKGradeNumber.setText(decimal.format(tracker.getFKGrade()));
          lblFKScoreNumber.setText(decimal.format(tracker.getFKScore()));
      });

      primaryStage.setTitle("Readability Checker");
      primaryStage.setScene(scene);
      primaryStage.show();
    }  // end start

  // converts HTML formatted text to plain text
  private String stripHtmlTags(String htmlText) {
    Pattern pattern = Pattern.compile("<[^>]*>");
    Matcher matcher = pattern.matcher(htmlText);
    final StringBuffer sb = new StringBuffer(htmlText.length());
    while (matcher.find()) {
      matcher.appendReplacement(sb, " ");
    }
    matcher.appendTail(sb);

    return sb.toString().trim();
  }

  public static void main(String[] args) {
    launch(args);
  }
}
