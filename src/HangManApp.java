import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import game.HangmanGame;
import game.HangmanGameInterface;
import lexicon.HangmanLexicon;

public class HangManApp extends JFrame {
	private static final int WIDTH = 800;
	private static final int HEIGHT = 300;

	private HangmanGameInterface hangmanGame;
	private HangmanLexicon hangmanLexicon;

	private JLabel wordLabel;
	private JTextField letterField;
	private JButton guessButton;
	private JLabel messageLabel;
	private JLabel guessedLettersLabel;
	private JLabel guessesLeftLabel;

	public HangManApp() {
		setTitle("Hangman Game");
		setSize(WIDTH, HEIGHT);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);

		setup();
		initializeUI();

		setVisible(true);
	}

	public void setup() {
		hangmanLexicon = new HangmanLexicon();
		hangmanGame = new HangmanGame(hangmanLexicon);
	}

	private void initializeUI() {
		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BorderLayout(10, 10));
		mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

		wordLabel = new JLabel(hangmanGame.getPartlyGuessedWord(), JLabel.CENTER);
		wordLabel.setFont(new Font("Monospaced", Font.BOLD, 36));
		mainPanel.add(wordLabel, BorderLayout.NORTH);

		JPanel inputPanel = new JPanel();
		inputPanel.add(new JLabel("Введіть літеру: "));

		letterField = new JTextField(5);
		letterField.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					processGuess();
				}
			}
		});
		inputPanel.add(letterField);

		guessButton = new JButton("Вгадати");
		guessButton.addActionListener(e -> processGuess());
		inputPanel.add(guessButton);

		mainPanel.add(inputPanel, BorderLayout.CENTER);

		JPanel statusPanel = new JPanel();
		statusPanel.setLayout(new GridLayout(3, 1, 0, 10));

		messageLabel = new JLabel("Удачі! Спробуй вгадати слово.");
		messageLabel.setHorizontalAlignment(JLabel.CENTER);
		statusPanel.add(messageLabel);

		guessedLettersLabel = new JLabel("Загаданні літери: ");
		guessedLettersLabel.setHorizontalAlignment(JLabel.CENTER);
		statusPanel.add(guessedLettersLabel);

		guessesLeftLabel = new JLabel("Спроб залишилось: " + hangmanGame.getGuessesLeft());
		guessesLeftLabel.setHorizontalAlignment(JLabel.CENTER);
		statusPanel.add(guessesLeftLabel);

		mainPanel.add(statusPanel, BorderLayout.SOUTH);

		add(mainPanel);
	}

	private void processGuess() {
		String input = letterField.getText().trim();
		letterField.setText("");

		if (input.isEmpty()) {
			return;
		}

		char letter = input.charAt(0);

		if (hangmanGame.isGameWon() || hangmanGame.isGameLost()) {
			newGame();
			return;
		}

		boolean correct = hangmanGame.guess(letter);
		updateUI();

		if (hangmanGame.isGameWon()) {
			messageLabel.setText("Ви вийграли! Загаданне слово: " + hangmanGame.getHangmanWord());
		} else if (hangmanGame.isGameLost()) {
			messageLabel.setText("Ви програли! Загаданне слово: " + hangmanGame.getHangmanWord());
		} else {
			messageLabel.setText(correct ? "Правильно!" : "Не правильно!");
		}
	}

	private void updateUI() {
		wordLabel.setText(hangmanGame.getPartlyGuessedWord());
		guessedLettersLabel.setText("Загаданні літери: " + hangmanGame.getGuessedLetters());
		guessesLeftLabel.setText("Спроб залишилось: " + hangmanGame.getGuessesLeft());
	}

	private void newGame() {
		setup();
		updateUI();
		messageLabel.setText("Нова гра розпочата. Удачі!");
	}

	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_ENTER) {
			processGuess();
		}
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> new HangManApp());
	}
}