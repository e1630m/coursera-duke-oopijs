/**
 * Finds a protein within a strand of DNA represented as a string of c,g,t,a letters.
 * A protein is a part of the DNA strand marked by start and stop codons (DNA triples)
 * that is a multiple of 3 letters long.
 *
 * @author Duke Software Team
 */
import edu.duke.*;
import java.io.*;

public class TagFinder {
	public String findProtein(String dna) {
		int start = dna.indexOf("atg");
		if (start == -1) {
			return "";
		}
		int stop = dna.indexOf("tag", start+3);
		if ((stop - start) % 3 == 0) {
			return dna.substring(start, stop+3);
		}
		else {
			return "";
		}
	}

	public void testing() {
		String a = "cccatggggtttaaataataataggagagagagagagagttt";
		String ap = "atggggtttaaataataatag";
		/*
		// String a = "atgcctag";
		// String ap = "";
		// String a = "ATGCCCTAG";
		// String ap = "ATGCCCTAG";
		*/
		String result = findProtein(a);
		if (ap.equals(result)) {
			System.out.println("success for " + ap + " length " + ap.length());
		}
		else {
			System.out.println("mistake for input: " + a);
			System.out.println("got: " + result);
			System.out.println("not: " + ap);
		}
	}

	public void realTesting() {
		DirectoryResource dr = new DirectoryResource();
		for (File f : dr.selectedFiles()) {
			FileResource fr = new FileResource(f);
			String s = fr.asString();
			System.out.println("read " + s.length() + " characters");
			String result = findSimpleGene(s);
			System.out.println("found " + result);
		}
	}

	// Finding a Gene in DNA - Part 1
	public String findSimpleGene(String dna) {
		int i = 0, idxATG = -1;
		for (; i < dna.length() - 2; i++)
			if (dna.substring(i, i + 3).equalsIgnoreCase("ATG")) {
				idxATG = i;
				break;
			}
		if (idxATG < 0)
			return "";
		for (i += 3; i < dna.length() - 2; i += 3)
			if (dna.substring(i, i + 3).equalsIgnoreCase("TAA"))
				return dna.substring(idxATG, i + 3);
		return "";
	}

	// Finding a Gene in DNA - Part 2
	public String findCustomGene(String dna, String start, String stop) {
		int i = 0, idxStart = -1, len = stop.length();
		for (; i < dna.length() - len + 1; i++)
			if (dna.substring(i, i + 3).equalsIgnoreCase(start)) {
				idxStart = i;
				break;
			}
		if (idxStart < 0)
			return "";
		for (i += start.length(); i < dna.length() - len + 1; i += len)
			if (dna.substring(i, i + len).equalsIgnoreCase(stop))
				return dna.substring(idxStart, i + len);
		return "";
	}

	// Finding a Gene in DNA - Part 3
	public boolean twoOccurrences(String a, String b) {
		int count = 0, la = a.length(), lb = b.length();
		if (la * 2 > lb)
			return false;
		for (int i = 0; i < lb - la + 1; i++)
			if (b.substring(i, i + la).equalsIgnoreCase(a))
				count++;
		return count >= 2;
	}

	public String lastPart(String a, String b) {
		return b.contains(a) ? b.substring(b.indexOf(a) + a.length()) : b;
	}

	public void testingFindCustomGene(String start, String stop) {
		String c = "CASE Without ATG\n\t";
		String dna = "cccaggggTTTaAataataataggagAGAgagagagaGTTt", expected = "";
		System.out.printf("%s dna: '%s'\n\t result: '%s', expected: '%s'\n",
							c, dna, findCustomGene(dna, start, stop), expected);
		c = "CASE Without TAA\n\t";
		dna = "ATgCCCatggggttTATATAtatatatggagagagagagagAGttt";
		System.out.printf("%s dna: '%s'\n\t result: '%s', expected: '%s'\n",
				c, dna, findCustomGene(dna, start, stop), expected);

		c = "Case Without ATG AND TAA\n\t";
		dna = "ccca!)%#( ss9a8f;h'ztttatatat717atatasp a..:ftatagga845gagagagagagagttt";
		System.out.printf("%s dna: '%s'\n\t result: '%s', expected: '%s'\n",
				c, dna, findCustomGene(dna, start, stop), expected);

		c = "CASE With Both ATG and TAA, and the substring length is multiples of 3\n\t";
		dna = "ATgATGgggTaAtaatatataatataggggggggtttta";
		expected = "ATgATGgggTaA";
		System.out.printf("%s dna: '%s'\n\t result: '%s', expected: '%s'\n",
				c, dna, findCustomGene(dna, start, stop), expected);

		c = "CASE With Both ATG and TAA, and the substring length is multiples of 3\n\t";
		dna = "ATGgTAATAATAATAATAATAAttttttggggATGgggTAA";
		expected = "";
		System.out.printf("%s dna: '%s'\n\t result: '%s', expected: '%s'\n",
				c, dna, findCustomGene(dna, start, stop), expected);
	}

	public void testingTwoOccurrences() {
		String a = "by", b = "A story by Abby Long";
		System.out.printf("\ntwoOccurrences('%s', '%s'): %b, expected: %b\n", a, b, twoOccurrences(a, b), true);

		a = "a";
		b = "banana";
		System.out.printf("twoOccurrences('%s', '%s'): %b, expected: %b\n", a, b, twoOccurrences(a, b), true);

		a = "atg";
		b = "ctgtatgta";
		System.out.printf("twoOccurrences('%s', '%s'): %b, expected: %b\n", a, b, twoOccurrences(a, b), false);
	}

	public void testingLastPart() {
		String a = "an", b = "banana";
		System.out.printf("\nThe part of the string after %s in %s is %s\n", a, b, lastPart(a, b));

		a = "zoo";
		b = "forest";
		System.out.printf("The part of the string after %s in %s is %s\n", a, b, lastPart(a, b));
	}

	public static void main(String[] args) {
		TagFinder tf = new TagFinder();
		tf.testingFindCustomGene("ATG", "TTT");
		// tf.realTesting();
		tf.testingTwoOccurrences();
		tf.testingLastPart();
	}
}
