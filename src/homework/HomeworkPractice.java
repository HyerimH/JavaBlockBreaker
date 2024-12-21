package homework;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.LinkedList;

import javax.swing.JFrame;
import javax.swing.JPanel;

abstract class MyGameObject {
	float x,y;
	Color color;
	MyGameObject(){
		
	}
	abstract void draw(Graphics g);
	void update(float dt) {};
	void collisionResolution(MyGameObject o) {}
}

class MyWall extends MyGameObject{
	float w, h;
	MyWall(int _x, int _y, int _w, int _h){
		x = _x;
		y = _y;
		w = _w;
		h = _h;
		color = Color.gray;
	}
	@Override
	void draw(Graphics g) {
		g.setColor(color);
		g.fillRect((int)x, (int)y, (int)w, (int)h);
		g.setColor(Color.black);
		g.drawRect((int)x, (int)y, (int)w, (int)h);
	}
}

class MyHWBall extends MyGameObject{
	float vx, vy;
	float prev_x, prev_y;
	float r;
	MyHWBall(){
		x = 100;
		y = 100;
		prev_x = x;
		prev_y = y;
		r = 5;
		color = Color.black;
		double angle = Math.random()*3.141592*2;
		vx = (float)(200*Math.cos(angle));
		vy = (float)(200*Math.sin(angle));
	}
	@Override
	void draw(Graphics g) {
		
		g.setColor(color);
		g.fillOval((int)(x-r), (int)(y-r), (int)(2*r), (int)(2*r));
	}
	@Override
	void update(float dt) {
		prev_x = x;
		prev_y = y;
		x += vx*dt;
		y += vy*dt;
	}
	@Override
	void collisionResolution(MyGameObject o) {
		if((o instanceof MyWall)!=true) return;
		
		MyWall wall = (MyWall) o;
		float left = wall.x - r;						// 공의 반지름이 r 이므로 반지름 고려한 충돌 계산
		float right = wall.x + wall.w + r;
		float top = wall.y - r;
		float bottom = wall.y + wall.h + r;
		
		
		if(x>left && x<right && y>top && y<bottom) {
			//color = color.red;
			if(prev_y < top) 	{ y = top - r; 		vy = -vy;	}
			if(prev_y > bottom) { y = bottom + r; 	vy = -vy;	}
			if(prev_x < left) 	{ x = left - r;		vx = -vx;	}
			if(prev_x > right)  { x = right + r; 	vx = -vx;	}	
			
		}
		
	}
}

class HomeworkPracticePanel extends JPanel implements KeyListener, Runnable{
	LinkedList <MyGameObject> objs;	
	
	HomeworkPracticePanel(){
		objs = new LinkedList<>();
		
		objs.add(new MyWall(0,0,600,30));
		objs.add(new MyWall(0,430,600,30));
		objs.add(new MyWall(0,30,30,400));
		objs.add(new MyWall(560,30,30,400));
		
		objs.add(new MyWall(300,100,200,100));
		objs.add(new MyWall(100,300,200,50));
		
		setFocusable(true);
		requestFocus();
		addKeyListener(this);
		
		Thread t = new Thread(this);
		t.start();
		
	}
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		var it = objs.iterator();
		while(it.hasNext()) 
			it.next().draw(g);
	
	}
	public void keyTyped(KeyEvent e) { 	}
	public void keyReleased(KeyEvent e) { 	}
	
	public void keyPressed(KeyEvent e) { 	
		if(e.getKeyCode() == KeyEvent.VK_SPACE)
			objs.add(new MyHWBall());
	}

	public void run() { 	
		while(true) {
			try {
				
				for(var o : objs)
					o.update(0.033f);
				
				for(var o1 : objs)
					for(var o2 : objs)
						o1.collisionResolution(o2);
				
				repaint();
				
				Thread.sleep(33);				
			}catch(Exception e) {
				return;
			}			
		}		
	}
}

public class HomeworkPractice extends JFrame{
	HomeworkPractice(){
		setSize(600,500);
		setTitle("Homework Practice");
		setDefaultCloseOperation(EXIT_ON_CLOSE);

		add(new HomeworkPracticePanel());
		setVisible(true);
		
	}

	public static void main(String[] args) {
		new HomeworkPractice();

	}

}
