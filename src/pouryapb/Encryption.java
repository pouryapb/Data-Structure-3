package pouryapb;

import java.util.ArrayList;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Encrypts words. Does NOT work fine for sentences! Just words.
 * 
 * @author Pourya
 *
 */

public class Encryption {

	private Key key = new Key();
	private PriorityQueue<Character> charSet;
	private Random random = new Random();
	private static final Logger LOGGER = Logger.getLogger(Encryption.class.getName());

	/**
	 * Encryption key for Encryption Class!
	 * 
	 * @author Pourya
	 *
	 */
	public class Key {
		private int[] alocatedKey = null;
		private char[] orderKey = null;

		private void setKey(int[] allocatedKey, char[] orderKey) {
			if (allocatedKey.length != 26 || orderKey.length != 26)
				throw new IllegalArgumentException("alocatedKey and orderKey size must be 26!");
			else {
				this.alocatedKey = allocatedKey;
				this.orderKey = orderKey;
			}
		}
	}

	private int[] shuffleArray(int[] arr) {

		for (int i = arr.length - 1; i > 0; i--) {

			var index = random.nextInt(i + 1);

			// Simple swap
			int temp = arr[index];
			arr[index] = arr[i];
			arr[i] = temp;
		}

		return arr;
	}

	private char[] shuffleArray(char[] arr) {

		for (int i = arr.length - 1; i > 0; i--) {

			int index = random.nextInt(i + 1);

			// Simple swap
			char temp = arr[index];
			arr[index] = arr[i];
			arr[i] = temp;
		}

		return arr;
	}

	/**
	 * Makes an encryption key automatically.
	 */
	public void setKey() {
		key.alocatedKey = new int[] { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22,
				23, 24, 25 };
		key.orderKey = new char[] { 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q',
				'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z' };
		key.alocatedKey = shuffleArray(key.alocatedKey);
		key.orderKey = shuffleArray(key.orderKey);

		setPQ();
	}

	/**
	 * Manually set an Encryption key
	 * 
	 * @param allocatedKey - An array of numbers allocated to a character. Index of
	 *                     array means the place of characters in alphabet.
	 * @param orderKey     - An array of characters in order of their priority.
	 */
	public void setKey(int[] allocatedKey, char[] orderKey) {
		key.setKey(allocatedKey, orderKey);

		setPQ();
	}

	/**
	 * Set encryptions key with the key itself.
	 * 
	 * @param key
	 */
	public void setKey(Key key) {
		if (key == null)
			throw new NullPointerException("Key must not be null!");
		this.key = key;

		setPQ();
	}

	private void setPQ() {
		charSet = new PriorityQueue<>();
		for (var i = 0; i < key.orderKey.length; i++) {
			charSet.enqueue(key.orderKey[i], 26 - i);
		}
	}

	/**
	 * 
	 * @return The encryption key.
	 */
	public Key getKey() {
		return key;
	}

	/**
	 * Encrypts the given word with encryption key
	 * 
	 * @param word
	 * @return encrypted word.
	 */
	public String encrypt(String word) {

		var codedLetter = new StringBuilder();

		if (key.alocatedKey == null || key.orderKey == null) {
			throw new NullPointerException("No key defined yet!");
		}

		for (var i = 0; i < word.length(); i++) {

			int c = word.charAt(i);

			if (c < 'a' || c > 'z') {
				codedLetter.append((char) c);
				continue;
			}

			int alocated = key.alocatedKey[c - 97];

			if (alocated > charSet.size())
				setPQ();

			for (var j = 0; j < alocated; j++) {
				char ch = charSet.dequeue();

				if (j == alocated - 1)
					codedLetter.append(ch);
			}
		}

		setPQ();
		return codedLetter.toString();
	}

	/**
	 * Decrypts an encrypted word using the encryption key.
	 * 
	 * @param codedWord
	 * @return Decrypted word.
	 */
	public String decrypt(String codedWord) {
		var letter1 = new StringBuilder();
		var letter2 = new StringBuilder();

		if (key.alocatedKey == null || key.orderKey == null) {
			throw new NullPointerException("No key defined yet!");
		}

		for (var i = 0; i < codedWord.length(); i++) {
			int c = codedWord.charAt(i);

			if (c < 'a' || c > 'z') {
				letter1.append((char) c);
				letter2.append((char) c);
				continue;
			}

			var count = 0;
			if (charSet.isEmpty())
				setPQ();

			while (c != charSet.dequeue()) {
				count++;
				if (charSet.isEmpty()) {
					count = 0;
					setPQ();
				}
			}
			count++;

			letter1.append((char) (arrayIndexOf(count, key.alocatedKey) + 97));
			letter2.append((char) (arrayIndexOf(26 - charSet.size(), key.alocatedKey) + 97));
		}

		setPQ();

		return combinations(letter1.toString(), letter2.toString());
	}

	private int arrayIndexOf(int a, int[] arr) {
		for (int i = 0; i < arr.length; i++) {
			if (arr[i] == a)
				return i;
		}
		return -1;
	}

	private String combinations(String a, String b) {

		var result = new ArrayList<String>();
		var temp = "";

		result.add(a);
		result.add(b);

		for (var i = 0; i < a.length(); i++) {
			if (a.charAt(i) != b.charAt(i)) {
				temp = a.substring(0, i) + b.charAt(i) + a.substring(i + 1);
				result.add(temp);
				temp = b.substring(0, i) + a.charAt(i) + b.substring(i + 1);
				result.add(temp);
			}
		}

		return result.toString();
	}

	// test cases
	public static void main(String[] args) {

		var e = new Encryption();
		e.setKey();

		String s = e.encrypt("salam");
		LOGGER.log(Level.INFO, s);
		LOGGER.log(Level.INFO, () -> e.decrypt(s));
	}
}
