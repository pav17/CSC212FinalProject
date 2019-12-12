package edu.smith.cs.csc212.fp;

import java.awt.Color;
import java.awt.Image;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class QuadTree {
	
	public QuadTree() {
		String imgPath = "src/main/resources/testimage.png";
		BufferedImage img;
		int[][] pixelData;
		int width;
		int height;
		try {
			img = ImageIO.read(new File(imgPath));
			//code modified from https://stackoverflow.com/a/6524269
			width = img.getWidth();
			height = img.getHeight();
			if (width % 2 != 0 || height % 2 != 0) {
				throw new IllegalArgumentException("make sure image is even on both sides");
			}
			pixelData = new int[width][height];

			for( int i = 0; i < width; i++ ) {
				for( int j = 0; j < height; j++ ) {
					pixelData[i][j] = img.getRGB( i, j );
					System.out.println(pixelData[i][j]);
				}
			}
			
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}   
	}
	
	private static class Quad {
		public Color averageColor;
		public Point topLeft = new Point(0,0);
		public Point bottomRight;
		public Quad TLQuad = null;
		public Quad TRQuad = null;
		public Quad BLQuad = null;
		public Quad BRQuad = null;
		
		public Quad(int[][] pixels) {
			//if down to a single pixel,set color and back out
			if (pixels.length == 1 && pixels[0].length == 1) {
				this.averageColor = new Color(pixels[0][0], true);
				return;
			}
			//if not a single pixel find the dimensions (halved)
			
			int halfWidth = pixels.length/2;
			int halfHeight = pixels[0].length/2;
			//build the child quads
			int[][] TLPixels = new int[halfWidth][halfHeight];
			for (int i = 0; i < halfWidth; i++) {
				for (int y = 0; y < halfHeight; y++ ) {
					TLPixels[i][y] = pixels[i][y];
				}
			}
			TLQuad = new Quad(TLPixels);
			
			int[][] TRPixels = new int[halfWidth][halfHeight];
			for (int i = halfWidth; i < pixels.length; i++) {
				for (int y = 0; y < halfHeight; y++ ) {
					TRPixels[i][y] = pixels[i][y];
				}
			}
			TRQuad = new Quad(TRPixels);
			
			int[][] BLPixels = new int[halfWidth][halfHeight];
			for (int i = 0; i < halfWidth; i++) {
				for (int y = halfHeight; y < pixels[0].length; y++ ) {
					BLPixels[i][y] = pixels[i][y];
				}
			}
			BLQuad = new Quad(BLPixels);
			
			int[][] BRPixels = new int[halfWidth][halfHeight];
			for (int i = halfWidth; i < pixels.length; i++) {
				for (int y = halfHeight; y < pixels[0].length; y++ ) {
					BRPixels[i][y] = pixels[i][y];
				}
			}
			BRQuad = new Quad(BRPixels);
			
			if (TLQuad.averageColor.equals(TRQuad.averageColor) && TRQuad.averageColor.equals(BLQuad.averageColor) && BLQuad.averageColor.equals(BRQuad.averageColor)) {
				this.averageColor = TLQuad.averageColor;
				TLQuad = null;
				TRQuad = null;
				BLQuad = null;
				BRQuad = null;
			}
			else {
				this.averageColor = new Color((int) Math.sqrt((TLQuad.averageColor.getRGB()*TLQuad.averageColor.getRGB())+(TRQuad.averageColor.getRGB()*TRQuad.averageColor.getRGB())+
						(BLQuad.averageColor.getRGB()*BLQuad.averageColor.getRGB())+(BRQuad.averageColor.getRGB()*BRQuad.averageColor.getRGB())));
			}
			
		}
	}
}


