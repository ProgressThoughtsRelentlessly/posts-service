package com.pthore.service.posts.utils;

import java.awt.AlphaComposite;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.WindowConstants;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("singleton") // Just for verbosity
public class ImageUtils {
	
	private final static Dimension DEFAULT_THUMBNAIL_DIMENSION = new Dimension(200, 200);
	private final static Dimension DEFAULT_MOBILE_DIMENSION = new Dimension(400, 600);
	private final static Dimension DEFAULT_DESKTOP_DIMENSION = new Dimension(800, 600);
	
	
	private Dimension resizeDimensionForSameAspectRatio(Dimension boundary, 
			Dimension originalDimension) {
		
		int boundaryWidth = boundary.width;
		int boundaryHeight = boundary.height;
		
		int originalWidth = originalDimension.width;
		int originalHeight = originalDimension.height;
		
		int newWidth = originalWidth;
		int newHeight = originalHeight;

		if(originalWidth > boundaryWidth) {

			newWidth = boundaryWidth;
			newHeight = (newWidth  * originalHeight) / originalWidth;
			// to maintain height according to the resized width you do this.

		}
		if (newHeight > boundaryHeight) {
			// if newly calculated height is more than boundary then adjust the width accordingly
			// note: draw on a paper to understand the concept.
			newHeight = boundaryHeight;
			newWidth = (newHeight * newWidth) / originalHeight;			
		}
		return new Dimension(newWidth, newHeight);
	}
	
	public List<BufferedImage> resizeToMobileAndDesktopSize(BufferedImage originalImage, 
			Dimension mobileDimension, Dimension desktopDimension, boolean maintainAspectRatio ) {
		
		List<BufferedImage> images = new ArrayList<>();
		if(mobileDimension == null ) {
			mobileDimension = DEFAULT_MOBILE_DIMENSION;
		}
		if (desktopDimension == null) {
			desktopDimension = DEFAULT_DESKTOP_DIMENSION;
		}
		
		// Set the Image Rectangular Container.
		BufferedImage resizedMobileImage = new BufferedImage(mobileDimension.width, mobileDimension.height, BufferedImage.TYPE_INT_ARGB);
		Graphics2D mobileGraphics2D = resizedMobileImage.createGraphics();
		mobileGraphics2D.setComposite(AlphaComposite.Src);
		
		BufferedImage resizedDesktopImage = new BufferedImage(desktopDimension.width, desktopDimension.height, BufferedImage.TYPE_INT_ARGB);
		Graphics2D desktopGraphics2D = resizedDesktopImage.createGraphics();
		
		
		// Set the image within the RectangularContainer.
		if(maintainAspectRatio) {
			
			// get imageDimension which maintains AspectRatio.
			Dimension newMobileDimension = 
					this.resizeDimensionForSameAspectRatio(mobileDimension, 
							new Dimension(originalImage.getWidth(), originalImage.getHeight()));
			Dimension newDesktopDimension = 
					this.resizeDimensionForSameAspectRatio(desktopDimension, 
							new Dimension(originalImage.getWidth(), originalImage.getHeight()));
			
			
			mobileGraphics2D.drawImage(originalImage, 0, 0, newMobileDimension.width, newMobileDimension.height, null);
			desktopGraphics2D.drawImage(originalImage, 0, 0, newDesktopDimension.width, newDesktopDimension.height, null);
		
		} else {
			mobileGraphics2D.drawImage(originalImage, 0, 0, mobileDimension.width, mobileDimension.height, null);
			desktopGraphics2D.drawImage(originalImage, 0, 0, desktopDimension.width, desktopDimension.height, null);
		}
		
		mobileGraphics2D.dispose();
		desktopGraphics2D.dispose();
		
		images.add(resizedMobileImage);
		images.add(resizedDesktopImage);
		
		return images;
	}
	
	public BufferedImage createThumbnail(BufferedImage originalImage ) {
		List<BufferedImage> bufferedImages = this.resizeToMobileAndDesktopSize(originalImage, DEFAULT_THUMBNAIL_DIMENSION, null, false);
		return bufferedImages.get(0); // 0th index is thumbnail
	}
	
	
	public BufferedImage readImage(File file) {
		BufferedImage bufferedImage = null;
		
		try {
			bufferedImage = ImageIO.read(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return bufferedImage;
	}
	
	public void displayImageOnFrame(BufferedImage bufferedImage) {
		
		JFrame frame = new JFrame();
		JLabel label = new JLabel();
		frame.setTitle("image-preview");
		frame.setSize(bufferedImage.getWidth(), bufferedImage.getHeight());
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		label.setIcon(new ImageIcon(bufferedImage));
		
		frame.getContentPane().add(label, BorderLayout.CENTER);
		frame.setLocationRelativeTo(null);
		frame.pack();
		frame.setVisible(true);

	}
	
	public void saveAsFile(String filename, BufferedImage image) {
		File file = new File(filename);
		try {
			ImageIO.write(image, "png", file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public final static void demo () {
		String filename = "C:/Users/sudar/space-0/college stuff/gateApplication/resized_DP_480x620.jpg";
		
		ImageUtils imageUtils = new ImageUtils();
		BufferedImage image = imageUtils.readImage(new File(filename));
		
		List<BufferedImage> images = imageUtils.resizeToMobileAndDesktopSize(image, null, null, true);
		BufferedImage thumbnail = imageUtils.createThumbnail(image);
		
		imageUtils.displayImageOnFrame(images.get(0));
		imageUtils.displayImageOnFrame(images.get(1));
		imageUtils.displayImageOnFrame(thumbnail);
		
		imageUtils.saveAsFile("original.png", image);
		imageUtils.saveAsFile("mobile-version.png", images.get(0));
		imageUtils.saveAsFile("desktop-version.png", images.get(1));
		imageUtils.saveAsFile("thumbnail.png", thumbnail);
		
	}
}
