/**
 * Prints out all links within the HTML source of a web page.
 * 
 * @author Duke Software Team + e1630m
 */
import edu.duke.*;

public class URLFinder {
	public StorageResource findURLs(String url) {
		URLResource page = new URLResource(url);
		String source = page.asString();
		StorageResource store = new StorageResource();
		int start = 0;
		while (true) {
			int index = source.indexOf("href=", start);
			if (index == -1) {
				break;
			}
			int firstQuote = index+6; // after href="
			int endQuote = source.indexOf("\"", firstQuote);
			String sub = source.substring(firstQuote, endQuote);
			if (sub.startsWith("http")) {
				store.add(sub);
			}
			start = endQuote + 1;
		}
		return store;
	}

	public void testURL() {
		StorageResource s1 = findURLs("https://www.whitehouse.gov");
		StorageResource s2 = findURLs("http://www.doctorswithoutborders.org");
		StorageResource s3 = findYouTubeLinks("https://www.dukelearntoprogram.com//course2/data/manylinks.html");
		for (String link : s3.data()) {
			System.out.println(link);
		}
		System.out.println("size s1 = " + s1.size());
		System.out.println("size s2 = " + s2.size());
		System.out.println("size s3 = " + s3.size());
	}

	// 1. Finding a Gene in DNA - Part 4
	public StorageResource findYouTubeLinks(String url) {
		URLResource page = new URLResource(url);
		String source = page.asString();
		StorageResource store = new StorageResource();
		int start = 0;
		while (true) {
			int index = source.indexOf("href=", start);
			if (index == -1)
				break;
			int qStart = index + 6;
			int qEnd = source.indexOf(source.charAt(qStart - 1), qStart);
			String sub = source.substring(qStart, qEnd);
			String ls = sub.toLowerCase();
			if (ls.startsWith("http") && (ls.contains("youtube.com") || ls.contains("youtu.be"))) {
				// store.add(sub);  // basic version which stores the url as-is
				store.add(getVideoID(sub));  // improved version which stores the url in youtu.be/id format
			}
			start = qEnd + 1;
		}
		return store;
	}

	private String getVideoID(String sub) {
		String ls = sub.toLowerCase();
		int idLength = 11;  // current YouTube video ID length
		String improved = "https://youtu.be/";
		if (ls.contains("watch?v=")) {
			improved += sub.substring(sub.lastIndexOf("watch?v=") + 8,
					    sub.lastIndexOf("watch?v=") + 8 + idLength);
		} else if (ls.substring(0, ls.lastIndexOf("/")).contains("youtu.be") ||
				   ls.contains("/embed/") || ls.contains("/v/")) {
			improved += sub.substring(sub.lastIndexOf("/") + 1,
					    sub.lastIndexOf("/") + 1 + idLength);
		} else if (ls.contains("&v=")) {
			improved += sub.substring(sub.indexOf("&v=") + 3, sub.indexOf("&v=") + 3 + idLength);
		}
		return improved;
	}

	public static void main(String[] args) {
		URLFinder uf = new URLFinder();
		uf.testURL();
	}
}
