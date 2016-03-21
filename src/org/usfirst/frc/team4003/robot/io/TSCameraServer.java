package org.usfirst.frc.team4003.robot.io;	

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import org.opencv.core.*;
import org.usfirst.frc.team4003.robot.Robot;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.*;

//import org.opencv.highgui.*;
import edu.wpi.first.wpilibj.Timer;

public class TSCameraServer implements Runnable {

    private static final int PORT = 1180;
    private static final byte[] MAGIC_NUMBER = {0x01, 0x00, 0x00, 0x00};
    private double fps = 24; 
    private int quality = 80;
    
	@Override
	public void run() {
		Mat img;
		Timer.delay(3);
		while (!Thread.currentThread().isInterrupted()) {
			System.out.println("Starting TSCameraServer");
			try (ServerSocket serverSocket = new ServerSocket(PORT)) {
				System.out.println("Got the socket!");

				try (Socket socket = serverSocket.accept()) {
					System.out.println("Got connection from " + socket.getInetAddress());

					DataOutputStream socketOutputStream = new DataOutputStream(socket.getOutputStream());
					DataInputStream socketInputStream = new DataInputStream(socket.getInputStream());

					// We'll read these in to be safe but ignore them for now.
					int fpsIgnored = socketInputStream.readInt();
					int compressionIgnored = socketInputStream.readInt();
					int sizeIgnored = socketInputStream.readInt();

					while (!socket.isClosed() && !Thread.currentThread().isInterrupted()) {

						// We don't want to try and read while activeCamera is being swapped out so synch on that object.
						synchronized(Robot.activeCamera) {
							//System.out.println("Pulling image from activeCamera");
							img  = Robot.activeCamera.getDashboardImg();
							//System.out.println("Image size: " + img.size().height + " x " + img.size().width);
							//System.out.println("Got it!");
						}

						if (img != null) {
							long startTime = System.currentTimeMillis();
							
							MatOfByte byteMat = new MatOfByte();
							MatOfInt params = new MatOfInt(Imgcodecs.IMWRITE_JPEG_QUALITY, quality);
							try { // imencode() doesn't always work as we get bad data on our feed shortly after startup.
								Imgcodecs.imencode(".jpeg", img, byteMat, params);
								
								byte[] byteArray = byteMat.toArray();

								// The FRC dashboard image protocol consists of a magic number, the size of the image data,
								// and the image data itself.

								socketOutputStream.write(MAGIC_NUMBER);
								socketOutputStream.writeInt(byteArray.length);
								//System.out.println("image size: " + byteArray.length);
								socketOutputStream.write(byteArray, 0, byteArray.length);
								// Flushing the buffer reduces lag considerably.
								socketOutputStream.flush();
							} catch (Exception ex) {
								System.out.println(ex.getMessage());
							}
							
							// Limit FPS to whatever fps is
							long endTime = (long)(startTime + Math.floor(1000.0 / fps));
							try {
								long now = System.currentTimeMillis();
								/*
								System.out.println("Start: " + startTime);
								System.out.println("  Now: " + now);
								System.out.println("  End: " + endTime);
								*/
								if (endTime > now) {
									long sleepFor = endTime - now;
									//System.out.println("Sleeping for " + sleepFor + "ms");
									Thread.sleep(sleepFor);
								}
							} catch (InterruptedException ex) { }
						}
					}
				}
			} catch (IOException e) {
				System.out.println(e.getMessage());
				e.printStackTrace();
			} finally {
				// There's no cleanup, really.
			}
		}
	}
}