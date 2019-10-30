import java.util.Random;
import java.util.*;
import java.io.*;

/**
 * Implements a 3-d word search puzzle program.
 */
public class WordSearch3D {
	public WordSearch3D () {
	}

	/**
	 * Searches for all the words in the specified list in the specified grid.
	 * You should not need to modify this method.
	 * @param grid the grid of characters comprising the word search puzzle
	 * @param words the words to search for
	 * @param a list of lists of locations of the letters in the words
	 */
	public int[][][] searchForAll (char[][][] grid, String[] words) {
		final int[][][] locations = new int[words.length][][];
		for (int i = 0; i < words.length; i++) {
			locations[i] = search(grid, words[i]);
		}
		return locations;
	}
	
	// abc
	// def
	// ad
	// af

	/**
	 * Searches for the specified word in the specified grid.
	 * @param grid the grid of characters comprising the word search puzzle
	 * @param word the word to search for
	 * @return If the grid contains the
	 * word, then the method returns a list of the (3-d) locations of its letters; if not, 
	 */
	public int[][] search (char[][][] grid, String word) {
		if (word.length() == 0)
		{
			int[][] wordPosition = new int[0][0];
			return wordPosition;
		}
		for (int x = 0; x < grid.length; x ++)
		{
			for (int y = 0; y < grid[0].length; y ++)
			{
				for (int z = 0; z < grid[0][0].length; z ++)
				{
					if (grid[x][y][z] == word.charAt(0))
					{
						int[][] wordPosition = searchHelper(grid, word, x, y, z);
						if (wordPosition != null)
						{
							return wordPosition;
						}
					}
				}
			}
		}
		return null;
	}
	
	public int[][] searchHelper (char[][][] grid, String word, int xStart, int yStart, int zStart)
	{
		int[][] wordPosition = new int[word.length()][3];
		for (int i = -1; i <= 1; i ++)
		{
			for (int j = -1; j <= 1; j ++)
			{
				for (int k = -1; k <= 1; k ++)
				{
					if (i == 0 && j == 0 && k == 0)
					{
						continue;
					}
					if (!outOfBounds(grid, xStart + i * (word.length() - 1), yStart + j * (word.length() - 1), zStart + k * (word.length() - 1)))
					{
						if (grid[xStart + i][yStart + j][zStart + k] == word.charAt(1))
						{
							String wordInDirection = "";
							for (int a = 0; a < word.length(); a ++)
							{
								wordInDirection = wordInDirection + grid[xStart + i * a][yStart + j * a][zStart + k * a];
							}
							if (word.equals(wordInDirection))
							{
								for (int b = 0; b < word.length(); b ++)
								{
									wordPosition[b][0] = xStart + i * b;
									wordPosition[b][1] = yStart + j * b;
									wordPosition[b][2] = zStart + k * b;
								}
								return wordPosition;
							}
						}
					}
				}
			}
		}
		return null;
	}
	
	public boolean outOfBounds (char[][][] grid, int x, int y, int z)
	{
		return (x >= grid.length || y >= grid[0].length || z >= grid[0][0].length || x < 0 || y < 0 || z < 0);
	}

	/**
	 * Tries to create a word search puzzle of the specified size with the specified
	 * list of words.
	 * @param words the list of words to embed in the grid
	 * @param sizeX size of the grid along first dimension
	 * @param sizeY size of the grid along second dimension
	 * @param sizeZ size of the grid along third dimension
	 * @return a 3-d char array if successful that contains all the words, or <tt>null</tt> if
	 * no satisfying grid could be found.
	 */
	public char[][][] make (String[] words, int sizeX, int sizeY, int sizeZ) {
		for (String word : words)
		{
			if (word.length() > Math.floor(Math.sqrt(Math.pow(sizeX, 2) + Math.pow(sizeY, 2) + Math.pow(sizeZ, 2))))
			{
				return null;
			}
		}
		char[][][] grid = new char[sizeX][sizeY][sizeZ];
		boolean[][][] populateGrid = new boolean[sizeX][sizeY][sizeZ];
		final Random rng = new Random();
		return makeHelper(words, grid, populateGrid, 0, 0, rng);
	}
	
	public char[][][] makeHelper (String[] words, char[][][] grid, boolean[][][] populateGrid, int wordIndex, int numTries, Random rng)
	{
		boolean valid = false;
		int tries = 0;
		int wordLength = words[wordIndex].length();
		while (!valid && tries < 1000)
		{
			int x = rng.nextInt(grid.length);
			int y = rng.nextInt(grid[0].length);
			int z = rng.nextInt(grid[0][0].length);
			for (int i = -1; i <= 1 && !valid; i ++)
			{
				for (int j = -1; j <= 1 && !valid; j ++)
				{
					for (int k = -1; k <= 1 && !valid; k ++)
					{
						if (i == 0 && j == 0 && k == 0)
						{
							continue;
						}
						if (!outOfBounds(grid, x + i * (wordLength - 1), y + j * (wordLength - 1), z + k * (wordLength - 1)))
						{
							char[][][] tempGrid = Arrays.copyOfRange(grid, 0, grid.length);
							boolean[][][] tempPopulateGrid = Arrays.copyOfRange(populateGrid, 0, populateGrid.length);
							for (int a = 0; a < words[wordIndex].length(); a ++)
							{
								if (!populateGrid[x + i * a][y + j * a][z + k * a] || grid[x + i * a][y + j * a][z + k * a] == words[wordIndex].charAt(a))
								{
									grid[x + i * a][y + j * a][z + k * a] = words[wordIndex].charAt(a);
									populateGrid[x + i * a][y + j * a][z + k * a] = true;
									if (a == words[wordIndex].length() - 1)
									{
										valid = true;
									}
								}
								else
								{
									grid = tempGrid;
									populateGrid = tempPopulateGrid;
									valid = false;
									break;
								}
							}
						}
					}
				}
			}
			tries ++;
		}
		if (valid)
		{
			if (wordIndex == words.length - 1)
			{
				for (int i = 0; i < populateGrid.length; i ++)
				{
					for (int j = 0; j < populateGrid[0].length; j ++)
					{
						for (int k = 0; k < populateGrid[0][0].length; k ++)
						{
							if (populateGrid[i][j][k] == false)
							{
								grid[i][j][k] = (char) (rng.nextInt(26) + 'a');
							}
						}
					}
				}
				return grid;
			}
			else
			{
				return makeHelper(words, grid, populateGrid, wordIndex + 1, numTries, rng);
			}
		}
		else
		{
			if (numTries < 1000)
			{
				char[][][] cleanGrid = new char[grid.length][grid[0].length][grid[0][0].length];
				boolean[][][] cleanPopulateGrid = new boolean[grid.length][grid[0].length][grid[0][0].length];
				return makeHelper(words, cleanGrid, cleanPopulateGrid, 0, numTries + 1, rng);
			}
		}
		return null;
	}

	/**
	 * Exports to a file the list of lists of 3-d coordinates.
	 * You should not need to modify this method.
	 * @param locations a list (for all the words) of lists (for the letters of each word) of 3-d coordinates.
	 * @param filename what to name the exported file.
	 */
	public static void exportLocations (int[][][] locations, String filename) {
		// First determine how many non-null locations we have
		int numLocations = 0;
		for (int i = 0; i < locations.length; i++) {
			if (locations[i] != null) {
				numLocations++;
			}
		}

		try (final PrintWriter pw = new PrintWriter(filename)) {
			pw.print(numLocations);  // number of words
			pw.print('\n');
			for (int i = 0; i < locations.length; i++) {
				if (locations[i] != null) {
					pw.print(locations[i].length);  // number of characters in the word
					pw.print('\n');
					for (int j = 0; j < locations[i].length; j++) {
						for (int k = 0; k < 3; k++) {  // 3-d coordinates
							pw.print(locations[i][j][k]);
							pw.print(' ');
						}
					}
					pw.print('\n');
				}
			}
			pw.close();
		} catch (FileNotFoundException fnfe) {
			fnfe.printStackTrace();
		}
	}

	/**
	 * Exports to a file the contents of a 3-d grid.
	 * You should not need to modify this method.
	 * @param grid a 3-d grid of characters
	 * @param filename what to name the exported file.
	 */
	public static void exportGrid (char[][][] grid, String filename) {
		try (final PrintWriter pw = new PrintWriter(filename)) {
			pw.print(grid.length);  // height
			pw.print(' ');
			pw.print(grid[0].length);  // width
			pw.print(' ');
			pw.print(grid[0][0].length);  // depth
			pw.print('\n');
			for (int x = 0; x < grid.length; x++) {
				for (int y = 0; y < grid[0].length; y++) {
					for (int z = 0; z < grid[0][0].length; z++) {
						pw.print(grid[x][y][z]);
						pw.print(' ');
					}
				}
				pw.print('\n');
			}
			pw.close();
		} catch (FileNotFoundException fnfe) {
			fnfe.printStackTrace();
		}
	}

	/**
	 * Creates a 3-d word search puzzle with some nicely chosen fruits and vegetables,
	 * and then exports the resulting puzzle and its solution to grid.txt and locations.txt
	 * files.
	 */
	public static void main (String[] args) {
		final WordSearch3D wordSearch = new WordSearch3D();
		final String[] words = new String[] { "apple", "orange", "pear", "peach", "durian", "lemon", "lime", "jackfruit", "plum", "grape", "apricot", "blueberry", "tangerine", "coconut", "mango", "lychee", "guava", "strawberry", "kiwi", "kumquat", "persimmon", "papaya", "longan", "eggplant", "cucumber", "tomato", "zucchini", "olive", "pea", "pumpkin", "cherry", "date", "nectarine", "breadfruit", "sapodilla", "rowan", "quince", "toyon", "sorb", "medlar" };
		final int xSize = 10, ySize = 10, zSize = 10;
		final char[][][] grid = wordSearch.make(words, xSize, ySize, zSize);
		exportGrid(grid, "grid.txt");

		final int[][][] locations = wordSearch.searchForAll(grid, words);
		exportLocations(locations, "locations.txt");
	}
}
