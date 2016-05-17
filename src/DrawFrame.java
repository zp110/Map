import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;

import javax.swing.*;



public class DrawFrame extends JFrame{
	private MyPanel myPanel = null;
	
	public DrawFrame(){
		myPanel = new MyPanel();
		this.add(myPanel);
		this.setSize(800, 800);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setTitle("nihao");
		this.setVisible(true);
	}
	
	class MyPanel extends JPanel{
/*		public void paint(Graphics g){
			super.paint(g);
			Reconstruction re = new Reconstruction();
			re.findBoundary();
			re.formDensityMap();
			re.locateSaddlePoint();
			re.locateMaxPoint();
			
			re.computeRidge();
			re.findConnected();
			
			re.computeShowBasedThree();

			
			double[][] densitymap = re.getDensityMap();
			
			ArrayList<ArrayList<Node>> allpathlist = re.getShowpathlist();
			for(int i = 0; i< allpathlist.size();i++){
				for(int j = 0; j< allpathlist.get(i).size();j++){
					g.drawRect(allpathlist.get(i).get(j).getX(), allpathlist.get(i).get(j).getY(), 1, 1);
				}
			}
			allpathlist = re.getAllconnectedlines();
			Graphics2D g1 = (Graphics2D) g;
			g1.setStroke(new BasicStroke(2));
			for(int i = 0; i< allpathlist.size();i++){
				g.drawLine(allpathlist.get(i).get(0).getX(), allpathlist.get(i).get(0).getY(), allpathlist.get(i).get(1).getX(), allpathlist.get(i).get(1).getY());
				
			}
			System.out.println("done");
*/	
/*
			ArrayList<Node> maxlist = re.getMaxlist();
			for(int i =0 ; i < maxlist.size();i++){

					g.drawRect(maxlist.get(i).getX(), maxlist.get(i).getY(), 1, 1);
			}

			ArrayList<Node> ridge = re.getRidge();
			for(int i =0 ; i < ridge.size();i++){			
					g.drawRect(ridge.get(i).getX(), ridge.get(i).getY(), 1, 1);
			}
*/
			
//		}
	
	}
	
	public static void main(String args[]){
		new DrawFrame();
		}
	
}
