import edu.duke.*;
import java.io.File;
import java.util.ArrayList;

public class PerimeterAssignmentRunner {
    public double getPerimeter (Shape s) {
        // Start with totalPerim = 0
        double totalPerim = 0.0;
        // Start wth prevPt = the last point 
        Point prevPt = s.getLastPoint();
        // For each point currPt in the shape,
        for (Point currPt : s.getPoints()) {
            // Find distance from prevPt point to currPt 
            double currDist = prevPt.distance(currPt);
            // Update totalPerim by currDist
            totalPerim = totalPerim + currDist;
            // Update prevPt to be currPt
            prevPt = currPt;
        }
        // totalPerim is the answer
        return totalPerim;
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
            double peri = getPerimeter(new Shape(new FileResource(f)));
            if (peri > largest)
                largest = peri;
        }
        return largest;
    }

    public String getFileWithLargestPerimeter(Iterable<File> files) {
        double largest = 0.0;
        String largestFileName = null;
        for (File f : files) {
            double peri = getPerimeter(new Shape(new FileResource(f)));
            if (peri > largest){
                largest = peri;
                largestFileName = f.getName();
            }
        }
        return largestFileName;
    }

    public void testPerimeter (Shape s) {
        System.out.printf("peri: %.2f, numPoints: %d, avgLen: %.2f, longestSide: %.2f, largestX: %.2f",
                getPerimeter(s), getNumPoints(s), getAverageLength(s), getLargestSide(s), getLargestX(s));
    }
    
    public void testPerimeterMultipleFiles(Iterable<File> files) {
        for (File f : files) {
            System.out.printf(f.getName() + " - ");
            testPerimeter(new Shape(new FileResource(f)));
            System.out.printf("\n");
        }
    }

    public void testFileWithLargestPerimeter(Iterable<File> files) {
        System.out.printf("largest perimeter: %.2f (%s)", getLargestPerimeterMultipleFiles(files)
                                                        , getFileWithLargestPerimeter(files));
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
        DirectoryResource dr = new DirectoryResource();
        System.out.printf("(Selected files = ");
        ArrayList<File> files = new ArrayList<File>();
        for (File f : dr.selectedFiles()) {
            System.out.printf("%s ", f.getName());
            files.add(f);
        }
        System.out.printf(")\n");
        return files;
    }

    public static void main (String[] args) {
        String dataFolder = "data/PerimeterAssignmentRunner/";
        String fileName = "datatest4.txt";

        System.out.println("Single File Test (" + fileName + ")");
        PerimeterAssignmentRunner pr = new PerimeterAssignmentRunner();
        Shape s = new Shape(new FileResource(dataFolder + fileName));
        pr.testPerimeter(s);

        System.out.printf("\nMulti File Test ");
        Iterable<File> dir = pr.getFiles();
        pr.testPerimeterMultipleFiles(dir);
        pr.testFileWithLargestPerimeter(dir);
    }
}
