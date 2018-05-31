package main;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import graphics.Screen;

public class Main {

	public static boolean isPaused = false;
	static JFrame frame = new JFrame();
	public Main() throws InterruptedException {
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Screen s = new Screen(this);
		frame.add(s);
		frame.pack();
		frame.setVisible(true);
		synchronized(Main.class) {
			for(int i = 0; i <= 10000000; i++) {
				s.repaint();
				try {
					Thread.sleep(2);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if(isPaused) {
					Main.class.wait();
				}
			}
		}
	}

	public static void main(String[] args) throws InterruptedException {
		new Main();


	}
	
	public void pause() {
		isPaused = true;
	}
	
	public void resume() {
		isPaused = false;
	}

}
