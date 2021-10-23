/**
 * Finds a protein within a strand of DNA represented as a string of c,g,t,a letters.
 * A protein is a part of the DNA strand marked by start and stop codons (DNA triples)
 * that is a multiple of 3 letters long.
 *
 * @author Duke Software Team + e1630m
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

	// 1. Finding a Gene in DNA - Part 1
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

	// 1. Finding a Gene in DNA - Part 2
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

	// 1. Finding a Gene in DNA - Part 3
	public boolean twoOccurrences(String a, String b) {
		int count = 0, la = a.length(), lb = b.length();
		if (la * 2 > lb)
			return false;
		for (int i = 0; i < lb - la + 1; i++)
			if (b.substring(i, i + la).equalsIgnoreCase(a))
				count++;
		return count >= 2;
	}

	// 1. Finding a Gene in DNA - Part 3 Cont. (1. Part 4 is on URLFinder.java)
	public String lastPart(String a, String b) {
		return b.contains(a) ? b.substring(b.indexOf(a) + a.length()) : b;
	}

	// 2. Finding All Genes in DNA - Part 1
	public String findStopCodon(String dna, int startIndex, String stopCodon) {
		int len = stopCodon.length();
		for (int i = startIndex; i < dna.length() - len + 1; i += 3)
			if (dna.substring(i, i + len).toUpperCase().equals(stopCodon))
				return dna.substring(startIndex, i + len);
		return "";
	}

	// 2. Finding All Genes in DNA - Part 1 Cont.
	public String findGene(String dna) {
		int startIndex = dna.toUpperCase().indexOf("ATG");
		if (startIndex < 0)
			return "";
		String TAA = findStopCodon(dna, startIndex, "TAA");
		String TAG = findStopCodon(dna, startIndex, "TAG");
		String TGA = findStopCodon(dna, startIndex, "TGA");
		String[] result = {TAA, TAG, TGA};
		int[] length = {TAA.length(), TAG.length(), TGA.length()};
		if (length[0] == length[1] && length[1] == length[2])
			return "";
		int shortest = Integer.MAX_VALUE, ans = -1;
		for (int i = 0; i < 3; i++) {
			if (length[i] > 0 && length[i] < shortest) {
				shortest = length[i];
				ans = i;
			}
		}
		return result[ans];
	}

	// 2. Finding All Genes in DNA - Part 1 Cont.
	public void printAllGene(String dna) {
		System.out.printf("\n\nprintAllGene('%s'):\n", dna);
		while (true) {
			String result = findGene(dna);
			int length = result.length();
			if (length <= 0)
				break;
			System.out.println(result);
			dna = dna.substring(dna.indexOf("ATG") + length);
		}
	}

	// 2. Finding All Genes in DNA - Part 2
	public int howMany(String a, String b) {
		int count = 0, i = 0, la = a.length(), lb = b.length();
		if (la > lb)
			return 0;
		int limit = lb - la + 1;
		while (i < limit)
			if (b.substring(i, i + la).equals(a)) {
				count++;
				i += la;
			} else {
				i++;
			}
		return count;
	}

	// 2. Finding All Genes in DNA - Part 3
	public int countGenes(String dna) {
		int startIndex = dna.toUpperCase().indexOf("ATG");
		if (startIndex < 0)
			return 0;
		int count = 0;
		while (startIndex >= 0) {
			String sub = dna.substring(startIndex);
			int length = findGene(sub).length();
			if (length > 0) {
				startIndex = dna.toUpperCase().indexOf("ATG", startIndex + length);
				count++;
			} else {
				break;
			}
		}
		return count;
	}

	// 3. Storing All Genes - Part 1
	public StorageResource getAllGenes(String dna) {
		StorageResource store = new StorageResource();
		while (true) {
			String result = findGene(dna);
			int length = result.length();
			if (length <= 0)
				break;
			store.add(result);
			dna = dna.substring(dna.indexOf("ATG") + length);
		}
		return store;
	}

	// 3. Storing All Genes - Part 2
	public double cgRatio(String dna) {
		int cg = 0;
		String DNA = dna.toUpperCase();
		for (int i = 0; i < dna.length(); i++)
			cg += (DNA.charAt(i) == 'C' || DNA.charAt(i) == 'G') ? 1 : 0;
		return (double) cg / dna.length();
	}

	// 3. Storing All Genes - Part 2 Cont.
	public int countCTG(String dna) {
		int count = 0, startIndex = dna.indexOf("CTG");
		String DNA = dna.toUpperCase();
		while (startIndex >= 0) {
			count++;
			startIndex = dna.indexOf("CTG", startIndex + 1);
		}
		return count;
	}

	// 3. Storing All Genes - Part 3
	public void processGenes(StorageResource sr) {
		double CGRCut = 0.35;
		int lengthCut = 60, lgst = -1;
		String longest = "";
		StorageResource cgr = new StorageResource();
		StorageResource length = new StorageResource();
		for (String dna : sr.data()) {
			for (String gene : getAllGenes(dna).data()) {
				if (cgRatio(gene) > CGRCut)
					cgr.add(gene);
				int geneLength = gene.length();
				if (geneLength > lengthCut)
					length.add(gene);
				if (geneLength > lgst) {
					lgst = geneLength;
					longest = gene;
				}
			}
		}
		System.out.printf("Genes with more than %d characters: %d gene(s) found\n", lengthCut, length.size());
		for (String dna : length.data())
			System.out.println(dna);
		System.out.printf("Genes with CG Ratio higher than %.2f: %d gene(s) found\n", CGRCut, cgr.size());
		for (String dna : cgr.data())
			System.out.println(dna);
		System.out.printf("The longest gene (length %d): %s\n", lgst, longest);
	}

	public void testingFindCustomGene(String start, String stop) {
		System.out.printf("\n\ntestingFindCustomGene('%s', '%s')\n:", start, stop);
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
		System.out.println("\n\ntestingTwoOccurrences():");
		String a = "by", b = "A story by Abby Long";
		System.out.printf("twoOccurrences('%s', '%s'): %b, expected: %b\n", a, b, twoOccurrences(a, b), true);

		a = "a";
		b = "banana";
		System.out.printf("twoOccurrences('%s', '%s'): %b, expected: %b\n", a, b, twoOccurrences(a, b), true);

		a = "atg";
		b = "ctgtatgta";
		System.out.printf("twoOccurrences('%s', '%s'): %b, expected: %b\n", a, b, twoOccurrences(a, b), false);
	}

	public void testingLastPart() {
		System.out.println("\n\ntestingLastPart():");
		String a = "an", b = "banana";
		System.out.printf("The part of the string after %s in %s is %s\n", a, b, lastPart(a, b));

		a = "zoo";
		b = "forest";
		System.out.printf("The part of the string after %s in %s is %s\n", a, b, lastPart(a, b));
	}

	public void testFindStopCodon () {
		System.out.println("\n\ntestFindStopCodon():");
		String c = "CASE With Both ATG and TAA, and the substring length is the multiples of 3\n\t";
		String dna = "ATgATGgggTaAtaatatataatataggggggggtttta";
		String expected = "ATgATGgggTaA";
		System.out.printf("%s dna: '%s'\n\t result: '%s', expected: '%s'\n",
				c, dna, findStopCodon(dna, dna.toUpperCase().indexOf("ATG"), "TAA"), expected);

		c = "CASE With Both ATG and TAA, and the substring length is not the multiples of 3\n\t";
		dna = "ATGgTAATAATAATAATAATAAttttttggggATGgggTAA";
		expected = "";
		System.out.printf("%s dna: '%s'\n\t result: '%s', expected: '%s'\n",
				c, dna, findStopCodon(dna, dna.toUpperCase().indexOf("ATG"), "TAA"), expected);
	}

	public void testFindGene () {
		System.out.println("\n\ntestFindGene():");
		String c = "CASE Without ATG\n\t";
		String dna = "cccaggggTTTaAataataataTAGtagTTAgagAGAgagagagaGTTt", expected = "";
		System.out.printf("%s dna: '%s'\n\t result: '%s', expected: '%s'\n", c, dna, findGene(dna), expected);

		c = "CASE With ATG and one valid stop codon\n\t";
		dna = "tttggttgtgtgtgttgtATGgtgtatgatgtgtgtttttttTAA";
		expected = "ATGgtgtatgatgtgtgtttttttTAA";
		System.out.printf("%s dna: '%s'\n\t result: '%s', expected: '%s'\n", c, dna, findGene(dna), expected);

		c = "CASE With ATG and multiple valid stop codons\n\t";
		dna = "tttggttgtgtgtgttgtATGgtgtatgatgtgtgtgaTAGttttTAaTAGtGa";
		expected = "ATGgtgtatgatgtgtgtgaTAGttttTAa";
		System.out.printf("%s dna: '%s'\n\t result: '%s', expected: '%s'\n", c, dna, findGene(dna), expected);

		c = "CASE With ATG and no valid stop codons\n\t";
		dna = "ATggTAGTAATAGTAAtagtaaatgtaaATGaTTATAATAGatgttgGGG";
		expected = "";
		System.out.printf("%s dna: '%s'\n\t result: '%s', expected: '%s'\n", c, dna, findGene(dna), expected);

		c = "CASE With ATG and one valid stop codon\n\t";
		dna = "AATGCTAACTAGCTGACTAAT";
		expected = "AATGCTAACTAGCTGA";
		System.out.printf("%s dna: '%s'\n\t result: '%s', expected: '%s'\n", c, dna, findGene(dna), expected);
	}

	public void testHowMany() {
		System.out.println("\n\ntestHowMany():");
		String a = "GAA", b = "ATGAACGAATTGAATC";
		int e = 3;
		System.out.printf("howMany('%s', '%s'): %d, expected: %d\n", a, b, howMany(a, b), e);

		a = "AA";
		b = "ATAAAA";
		e = 2;
		System.out.printf("howMany('%s', '%s'): %d, expected: %d\n", a, b, howMany(a, b), e);
	}

	public void testCountGenes() {
		System.out.println("\n\ntestCountGenes():");
		String dna = "ATGTAAGATGCCCTAGT";
		int e = 2;
		System.out.printf("howMany('%s'): %d, expected: %d\n", dna, countGenes(dna), e);

		dna = "ATGatgTAAgATGtttcccTAGtATGatgTAAatgatag";
		e = 3;
		System.out.printf("howMany('%s'): %d, expected: %d\n", dna, countGenes(dna), e);

		dna = "AATGCTAACTAGCTGACTAAT";
		e = 1;
		System.out.printf("howMany('%s'): %d, expected: %d\n", dna, countGenes(dna), e);
	}

	public void testGetAllGenes(String dna) {
		System.out.printf("\n\ntestGetAllGenes('%s'):\n", dna);
		for (String gene : getAllGenes(dna).data())
			System.out.println(gene);
	}

	public void testCGRatio() {
		System.out.printf("\n\ntestCGRatio():\n");
		String dna = "ATGCCATAG";
		double e = 4.0 / 9.0;
		System.out.printf("testCGRatio('%s'): %f, expected: %f\n", dna, cgRatio(dna), e);
	}

	public void testProcessGenes() {
		System.out.printf("\n\ntestProcessGenes():\n");
		StorageResource sr = new StorageResource();
		sr.add("ATGATGATGCCCCCCTTTTTA"); // gene longer than 9 characters
		sr.add("ATGCCATAGATGTGAATGTTA"); // no gene longer than 9 characters
		sr.add("ATGGGGGGGTAAATGTAATGA"); // some genes have a cg ratio higher than 0.35
		sr.add("ATGTTTTTTTTTTTTTTTTAA"); // no gene has a cg ratio higher than 0.35
		processGenes(sr);
	}

	public void testWithRealDNA() {
		System.out.printf("\ntestWithRealDNA():\nDNA used: brca1line.fa\n");
		FileResource fr = new FileResource("data/TagFinder/brca1line.fa");
		String dna = fr.asString();
		System.out.printf("Number of genes: %d\n", countGenes(dna));
		StorageResource real = new StorageResource();
		real.add(dna);
		processGenes(real);
		System.out.printf("countCTG(brca1line.fa): %d", countCTG(dna));

		System.out.printf("\n\ntestWithRealDNA():\nDNA used: GRch38dnapart.fa\n");
		fr = new FileResource("data/TagFinder/GRch38dnapart.fa");
		dna = fr.asString();
		System.out.printf("Number of genes: %d\n", countGenes(dna));
		real = new StorageResource();
		real.add(dna);
		processGenes(real);
		System.out.printf("countCTG(GRch38dnapart.fa): %d", countCTG(dna));
	}

	public static void main(String[] args) {
		TagFinder tf = new TagFinder();
		// tf.realTesting();

		// 1. Finding a Gene in DNA - Part 2
		tf.testingFindCustomGene("ATG", "TAA");

		// 1. Finding a Gene in DNA - Part 3
		tf.testingTwoOccurrences();
		tf.testingLastPart();

		// 2. Finding All Genes in DNA - Part 1
		tf.testFindStopCodon();
		tf.testFindGene();
		tf.printAllGene("ATGatgTAAgATGtttcccTAGtATGatgTAAaatgtGAtag");

		// 2. Finding All Genes in DNA - Part 2
		tf.testHowMany();

		// 2. Finding All Genes in DNA - Part 3
		tf.testCountGenes();

		// 3. Storing All Genes - Part 1
		tf.testGetAllGenes("ATGatgTAAgATGtttcccTAGtATGatgTAAaatgtGAtag");

		// 3. Storing All Genes - Part 2
		tf.testCGRatio();

		// 3. Storing All Genes - Part 3
		tf.testProcessGenes();

		// 3. Using StorageResource Quiz
		tf.testWithRealDNA();
	}
}
