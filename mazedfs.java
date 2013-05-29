import java.awt.*;
import java.awt.event.*;
import java.awt.Graphics;
import javax.swing.*;

public class mazedfs extends JFrame implements ActionListener
{
/* default values: */
private int bh = 10; 	// height of a graphical block
private int bw = 10;	// width of a graphical block
private int mh = 41;	// height and width of maze
private int mw = 51;
private int ah, aw;	// height and width of graphical maze
private int yoff = 40;    // init y-cord of maze
private Graphics g;
private int dtime = 40;   // 40 ms delay time
byte[][] M;// the array for the maze
private Button startbutton;
public static final int SOUTH = 0;
public static final int EAST = 1;
public static final int NORTH = 2;
public static final int WEST = 3;

public static boolean showvalue = false; // shows matrix value as text

// args determine block size, maze height, and maze width
public mazedfs(int bh0, int mh0, int mw0)
 { 
   bh = bw = bh0;  mh = mh0;  mw = mw0;
   ah = bh*mh;
   aw = bw*mw;
   startbutton = new Button("Begin");
   startbutton.setBounds((aw/2)-40,yoff+20,80,30);
   Container pane = this.getContentPane(); // the "content pane" of window
   pane.setLayout(null); // else java will place items automatically
   pane.add(startbutton);
   startbutton.addActionListener( this );
   M = new byte[mh][mw];  // initialize maze (all  0's - walls).
   this.setBounds(0,0,aw+10,10+ah+yoff);	
   this.setVisible(true);
   this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
   try{Thread.sleep(500);} catch(Exception e) {} // Synch with system
   g = getGraphics();    //g.setColor(Color.red);
 }

public void paint(Graphics g) {} // override automatic repaint

  public void actionPerformed( ActionEvent e )
   { 
     startbutton.setVisible(false);
     g.setColor(Color.green);
     g.fill3DRect(0,yoff,aw,ah,true);  // fill raised rectangle
     g.setColor(Color.black);
     //     showStatus("Generating maze...");
     digout(mh-2,mw-2);
	   // digout exit
     M[mh-1][mw-2] = 1;
     drawblock(mh-2,mw-1);
     solve();
   }   

    public static void main(String[] args)
    {
       int blocksize = 16, mheight = 41, mwidth = 41; // need to be odd
       if (args.length==3)
	   {
	       mheight=Integer.parseInt(args[0]);
	       mwidth=Integer.parseInt(args[1]);
	       blocksize=Integer.parseInt(args[2]);
	   }
       mazedfs W = new mazedfs(blocksize,mheight,mwidth);
    }


public void drawblock(int y, int x)
    {
	g.setColor(Color.black);
	g.fillRect(x*bw,yoff+(y*bh),bw,bh);
	g.setColor(Color.yellow);
	// following line displays value of M[y][x] in the graphical maze:
	if (showvalue)
	  g.drawString(""+M[y][x],(x*bw)+(bw/2-4),yoff+(y*bh)+(bh/2+6));
    }

    void drawdot(int y, int x)
    {
	g.setColor(Color.red);
	g.fillOval(x*bw,yoff+(y*bh),bw,bh);	    	   
        try{Thread.sleep(60);} catch(Exception e) {} 
    }


public void digout(int y, int x)
 {
	 
     M[y][x] = 1;  // digout maze at coordinate y,x
     drawblock(y,x);  // change graphical display to reflect space dug out
     
     int[] P = {0,1,2,3};
     for (int i =0;i <P.length;i++)
	 {
	     int r = (int)(Math.random()*P.length);
	     int temp = P[i];
	     P[i] = P[r];
	     P[r]=temp;
	 }
     for (int r =0;r<4;r++)
	 {
	     if(P[r]==0)
		 {
		     if (x+2<mw && M[y][x+2]==0 && P[r]==0) //right - 0
			 {
			     M[y][x+1] = 1;//up - 0
			     drawblock(y,x+1);
			     digout(y,x+2);
			 }  
		 }
		 
	     else if(P[r]==1)
		 { 
		     if (y-2>=0 && M[y-2][x]==0 && P[r]==1)//up - 1
			 {
			     M[y-1][x] = 1;
			     drawblock(y-1,x);
			     digout(y-2,x);
			 }
		     
		 }
	     
	     else if(P[r]==2)
		 {
			 
		     if (y+2<mh && M[y+2][x]==0 && P[r]==2)//down - 2
			 {
			     M[y+1][x] = 1;
			     drawblock(y+1,x);
			     digout(y+2,x);
			 }
		 }
		 
	     else if (P[r]==3)
		 {
		     if (x-2>=0 && M[y][x-2]==0 && P[r]==3)//left - 3
			 {
			     M[y][x-1] = 1;
			     drawblock(y,x-1);
			     digout(y,x-2);
			 }   
		 }
	 }//for
 } // digout

	
	class stackcell
		{
			int x;
			int y;
			stackcell tail;
			public stackcell(int a, int b, stackcell t) {y=a; x=b; tail=t;}
			
		} // stack class
	
	
	public void solve()
	{
		int x=1, y=1; // initial position of dot
		drawdot(y,x);  // draws dot graphically.
		while (x!=mw-2 || y!=mh-2)
		{
			int min=50;
			M[y][x]++;
			int dir=1;
			drawblock(y,x); // will erase the dot (for animation)
				if (M[y+1][x]!=0 && M[y+1][x]<min && y+1<mh)
				{min=M[y+1][x];
				dir=2;}
				if (M[y][x+1]!=0 && M[y][x+1]<min && x+1<mw)
				{min=M[y][x+1];
				dir=0;}
				if ( M[y-1][x]!=0 && M[y-1][x]<min && y-1>0)
				{min=M[y-1][x];
				dir=1;}
				if (M[y][x-1]!=0 && M[y][x-1]<min && x-1>0)
				{min=M[y][x-1];
				dir=3;}
				if (dir==0)
				{x++;}
				if (dir==1)
				{y--;}
				if (dir==2)
				{y++;}
				if (dir==3)
				{x--;}
			drawdot(y,x);
		}
	} // solve
	

} // mazedfs
