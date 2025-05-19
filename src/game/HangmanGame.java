package game;

import lexicon.LexiconInterface;

public class HangmanGame implements HangmanGameInterface {
	private String hangmanWord;
	private boolean[] guessedPositions;
	private StringBuilder guessedLetters;
	private int incorrectGuesses;
	private int correctGuessCount;
	private static final int MAX_GUESSES = 9;

	public HangmanGame(LexiconInterface l) {
		hangmanWord = l.getRandomWord().toUpperCase();
		guessedPositions = new boolean[hangmanWord.length()];
		guessedLetters = new StringBuilder();
		incorrectGuesses = 0;
		correctGuessCount = 0;
	}

	@Override
	public boolean guess(char letter) {
		char upperLetter = Character.toUpperCase(letter);

		if (guessedLetters.indexOf(String.valueOf(upperLetter)) != -1) {
			return false;
		}

		guessedLetters.append(upperLetter);

		boolean found = false;
		for (int i = 0; i < hangmanWord.length(); i++) {
			if (hangmanWord.charAt(i) == upperLetter && !guessedPositions[i]) {
				guessedPositions[i] = true;
				found = true;
				correctGuessCount++;
			}
		}

		if (!found) {
			incorrectGuesses++;
		}

		return found;
	}

	@Override
	public String getPartlyGuessedWord() {
		StringBuilder result = new StringBuilder();
		for (int i = 0; i < hangmanWord.length(); i++) {
			if (guessedPositions[i]) {
				result.append(hangmanWord.charAt(i));
			} else {
				result.append('-');
			}
		}
		return result.toString();
	}

	@Override
	public String getHangmanWord() {
		return hangmanWord;
	}

	@Override
	public String getGuessedLetters() {
		return guessedLetters.toString().replace("", ", ").substring(2);
	}

	@Override
	public boolean isGameLost() {
		return incorrectGuesses >= MAX_GUESSES;
	}

	@Override
	public boolean isGameWon() {
		for (boolean guessed : guessedPositions) {
			if (!guessed) {
				return false;
			}
		}
		return true;
	}

	@Override
	public int getGuessesLeft() {
		return MAX_GUESSES - incorrectGuesses;
	}

	@Override
	public int getIncorrectGuesses() {
		return incorrectGuesses;
	}

	@Override
	public int getCorrectGuesses() {
		return correctGuessCount;
	}
}