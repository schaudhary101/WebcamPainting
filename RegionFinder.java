import java.awt.*;
import java.awt.image.*;
import java.util.*;
/**
 * Region growing algorithm: finds and holds regions in an image.
 * Each region is a list of contiguous points with colors similar to a target color.
 * Scaffold for PS-1, Dartmouth CS 10, Fall 2016
 * 
 * @author Chris Bailey-Kellogg, Winter 2014 (based on a very different structure from Fall 2012)
 * @author Travis W. Peters, Dartmouth CS 10, Updated Winter 2015
 * @author CBK, Spring 2015, updated for CamPaint
 */
/*
 * Shaket Chaudhary and Juan Castano
 * Fall 2016
 */
public class RegionFinder {
	private static final int maxColorDiff = 20;				// how similar a pixel color must be to the target color, to belong to a region
	private static final int minRegion = 50; 				// how many points in a region to be worth considering

	private int width = 1080;
	private int height = 720;
	
	private BufferedImage image;                            // the image in which to find regions
	private BufferedImage recoloredImage;                   // the image with identified regions recolored
	private BufferedImage visited;

	private ArrayList<ArrayList<Point>> regions;			// a region is a list of points
															// so the identified regions are in a list of lists of points

	public RegionFinder() {
		this.image = null;
	}

	public RegionFinder(BufferedImage image) {
		this.image = image;		
	}

	public void setImage(BufferedImage image) {
		this.image = image;
	}

	public BufferedImage getImage() {
		return image;
	}

	public BufferedImage getRecoloredImage() {
		return recoloredImage;
	}
	

	/**
	 * Sets regions to the flood-fill regions in the image, similar enough to the trackColor.
	 */
	public void findRegions(Color targetColor) {
		// TODO: YOUR CODE HERE
		
		regions = new ArrayList<ArrayList<Point>>();
		visited = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		
		//goes through all the pixels
		for (int y = 0; y < image.getHeight(); y++) {
			for (int x = 0; x < image.getWidth(); x++) {
				
				Color imageColor = new Color(image.getRGB(x,y)); //Picks color from current pixel
				
				if (visited.getRGB(x, y) == 0 && (colorMatch(imageColor,targetColor))) { //checks if we have visited and if the color matches
					
					ArrayList<Point> potentialRegion = new ArrayList<Point>();			
					ArrayList<Point> pointsToExplore = new ArrayList<Point>();
					
					Point xy = new Point(x,y);
					pointsToExplore.add(xy);
					visited.setRGB(x, y, 1);
					
					while (! pointsToExplore.isEmpty()){
						
						Point lastxy = pointsToExplore.get(pointsToExplore.size()-1);
						potentialRegion.add(lastxy);
						pointsToExplore.remove(lastxy);
						
						for (int neighborX = (int)lastxy.getX() -1; neighborX <= (int)lastxy.getX() +1; neighborX++) {
							for (int neighborY = (int)lastxy.getY() -1; neighborY <= (int)lastxy.getY() +1; neighborY++) {
								
								if (neighborX >= 0 && neighborY >= 0 && neighborX < image.getWidth() && neighborY < image.getHeight()){
									
										Color neighborColor = new Color(image.getRGB(neighborX,neighborY));

										if ((colorMatch(neighborColor, targetColor)) && visited.getRGB(neighborX, neighborY) == 0) {
											pointsToExplore.add(new Point(neighborX,neighborY));
										}
										visited.setRGB(neighborX, neighborY, 1);
										
									}
							}
						}
					}
					
					if (potentialRegion.size() > minRegion) {
						regions.add(potentialRegion);
					}
				}
				visited.setRGB(x, y, 1);
			}
			
		}
	}

	/**
	 * Tests whether the two colors are "similar enough" (your definition, subject to the maxColorDiff threshold, which you can vary).
	 */
	private static boolean colorMatch(Color c1, Color c2) {
		// TODO: YOUR CODE HERE
		int redDifference = Math.abs(c1.getRed() - c2.getRed());
		int greenDifference = Math.abs(c1.getGreen() - c2.getGreen());
		int blueDifference = Math.abs(c1.getBlue() - c2.getBlue());
		if (redDifference <= maxColorDiff && greenDifference <= maxColorDiff && blueDifference <= maxColorDiff) {
			return true;
		}
		else return false;
		}
	

	/**
	 * Returns the largest region detected (if any region has been detected)
	 */
	public ArrayList<Point> largestRegion() {
		// TODO: YOUR CODE HERE
		ArrayList<Point> biggest = new ArrayList<Point>();
		int largest = 0;
		for (int i = 0; i < regions.size(); i++ ){
			if (regions.get(i).size() > largest){
				largest = regions.get(i).size();
				biggest = regions.get(i);		
			}	
		}
		return biggest;
	}

	
	/**
	 * Sets recoloredImage to be a copy of image, 
	 * but with each region a uniform random color, 
	 * so we can see where they are
	 */
	public void recolorImage() {
		// First copy the original
		recoloredImage = new BufferedImage(image.getColorModel(), image.copyData(null), image.getColorModel().isAlphaPremultiplied(), null);
		// Now recolor the regions in it
		// TODO: YOUR CODE HERE
		
		for (int i = 0; i < regions.size(); i++ ) {
			Color color = new Color((int)(Math.random()*16777216));
			for(int j = 0; j < regions.get(i).size(); j++) {

				regions.get(i).get(j);
				recoloredImage.setRGB((int)(regions.get(i).get(j).getX()),(int) (regions.get(i).get(j).getY()), color.getRGB());
			}
		}	
	}
}

