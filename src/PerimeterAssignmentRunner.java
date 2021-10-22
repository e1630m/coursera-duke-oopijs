import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

class Point {
    private int x;
    private int y;

    public Point (int startx, int starty) {
        x = startx;
        y = starty;
    }
    public int getX () {
        return x;
    }
    public int getY () {
        return y;
    }

    public double distance (Point otherPt) {
        int dx = x - otherPt.getX();
        int dy = y - otherPt.getY();
        return Math.sqrt(dx * dx + dy * dy);
    }

    public String toString(){
        return "("+x+","+y+")";
    }

    // for testing only!
    public static void main (String[] args) {
        Point p1 = new Point(3, 4);
        Point p2 = new Point(6, 8);
        System.out.println(p1.distance(p2));
    }
}

class Shape {
    private ArrayList<Point> points;

    public Shape () {
        points = new ArrayList<Point>();
    }
    public Shape (File file) {
        this();
        try {
            Scanner reader = new Scanner(file);
            while (reader.hasNextLine()) {
                String line = reader.nextLine();
                int comma = line.indexOf(",");
                int x = Integer.parseInt(line.substring(0, comma).trim());
                int y = Integer.parseInt(line.substring(comma + 1).trim());
                addPoint(new Point(x, y));
            }
        } catch (FileNotFoundException e) {
            System.out.println("File not found");
        }
    }

    public void addPoint (Point p) { points.add(p); }
    public Point getLastPoint () { return points.get(points.size() - 1); }
    public Iterable<Point> getPoints () { return points; }
}

public class PerimeterAssignmentRunner {
    public double getPerimeter (Shape s) {
        double perimeter = 0.0;
        Point prev = s.getLastPoint();
        for (Point cur : s.getPoints()) {
            double dist = prev.distance(cur);
            perimeter += dist;
            prev = cur;
        }
        return perimeter;
    }

    public int getNumPoints (Shape s) {
        int numPoints = 0;
        for (Point p : s.getPoints())
            numPoints++;
        return numPoints;
    }

    public double getAverageLength(Shape s) {
        return getPerimeter(s) / getNumPoints(s);
    }

    public double getLargestSide(Shape s) {
        double longest = 0.0;
        Point prev = s.getLastPoint();
        for (Point p : s.getPoints()) {
            if (prev.distance(p) > longest)
                longest = prev.distance(p);
            prev = p;
        }
        return longest;
    }

    public double getLargestX(Shape s) {
        double largestX = Double.NEGATIVE_INFINITY;
        for (Point p : s.getPoints())
            if (p.getX() > largestX)
                largestX = p.getX();
        return largestX;
    }

    public double getLargestPerimeterMultipleFiles(Iterable<File> files) {
        double largest = 0.0;
        for (File f : files) {
            double tPerimeter = getPerimeter(new Shape(f));
            if (tPerimeter > largest)
                largest = tPerimeter;
        }
        return largest;
    }

    public File getFileWithLargestPerimeter(Iterable<File> files) {
        double largest = 0.0;
        File largestFile = null;
        for (File f : files) {
            double tPerimeter = getPerimeter(new Shape(f));
            if (tPerimeter > largest){
                largest = tPerimeter;
                largestFile = f;
            }
        }
        return largestFile;
    }

    public void testPerimeter (Shape s) {
        System.out.printf("peri: %.2f, numPoints: %d, avgLen: %.2f, longestSide: %.2f, largestX: %.2f",
                getPerimeter(s), getNumPoints(s), getAverageLength(s), getLargestSide(s), getLargestX(s));
    }
    
    public void testPerimeterMultipleFiles(Iterable<File> files) {
        for (File f : files) {
            System.out.printf(f.getName() + " - ");
            testPerimeter(new Shape(f));
            System.out.printf("\n");
        }
    }

    public void testFileWithLargestPerimeter(Iterable<File> files) {
        File largest = getFileWithLargestPerimeter(files);
        ArrayList<String> names = new ArrayList<String>();
        for (File f : files)
            names.add(f.getName());
        System.out.println("\nlargest perimeter in " + names);
        System.out.printf("%s: %.2f", largest.getName(), getLargestPerimeterMultipleFiles(files));
    }

    // This method creates a triangle that you can use to test your other methods
    public void triangle() {
        Shape triangle = new Shape();
        triangle.addPoint(new Point(0,0));
        triangle.addPoint(new Point(6,0));
        triangle.addPoint(new Point(3,6));
        for (Point p : triangle.getPoints()){
            System.out.println(p);
        }
        double peri = getPerimeter(triangle);
        System.out.println("perimeter = "+peri);
    }

    // This method returns all chosen files in the directory
    public Iterable<File> getFiles() {
        String path = PerimeterAssignmentRunner.class.getProtectionDomain().getCodeSource().getLocation().getPath();
        // String root = new File(new File(path).getParentFile().getParentFile().getParent()).getPath(); // for IntelliJ
        String root = new File(new File(path).getParent()).getPath(); // for javac -> java
        String dataPath = root + "/data/PerimeterAssignmentRunner/";
        File[] files = new File(dataPath).listFiles();
        return Arrays.asList(files);
    }

    public static void main (String[] args) {
        PerimeterAssignmentRunner pr = new PerimeterAssignmentRunner();
        Iterable<File> allFiles = pr.getFiles();
        ArrayList<File> dataTests = new ArrayList<File>();
        ArrayList<File> examples = new ArrayList<File>();
        for (File f : allFiles) {
            String tmp = f.getName();
            if (tmp.startsWith("datatest"))
                dataTests.add(f);
            else if (tmp.startsWith("example"))
                examples.add(f);
        }
        pr.testPerimeterMultipleFiles(allFiles);
        pr.testFileWithLargestPerimeter(allFiles);
        pr.testFileWithLargestPerimeter(dataTests);
        pr.testFileWithLargestPerimeter(examples);
    }
}
