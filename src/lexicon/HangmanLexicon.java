package lexicon;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Random;

public class HangmanLexicon implements LexiconInterface {
	private ArrayList<String> words = new ArrayList<>();
	private Random random = new Random();

	public HangmanLexicon() {
		loadLexicon();
	}

	private void loadLexicon() {
		try {
			InputStream is = getClass().getResourceAsStream("/assets/lexicon.txt");

			if (is == null) {
				return;
			}

			BufferedReader reader = new BufferedReader(new InputStreamReader(is));
			String line;
			while ((line = reader.readLine()) != null) {
				if (!line.trim().isEmpty()) {
					words.add(line.trim().toUpperCase());
				}
			}
			reader.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public int getWordCount() {
		return words.size();
	}

	@Override
	public String getWord(int index) {
		if (index >= 0 && index < words.size()) {
			return words.get(index);
		}
		return null;
	}

	@Override
	public String getRandomWord() {
		int index = random.nextInt(words.size());
		return words.get(index);
	}
}