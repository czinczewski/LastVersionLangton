import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.JButton;
 
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
 
public class Lang extends JFrame implements ActionListener{
    
    	public static void main(String[] args){
		Lang window = new Lang();
	}
        
       // public JFrame frame;
	//private JPanel drawPanel;
        private JPanel buttonPanel;
        private JButton buttons[];
        JTextField Counter = new JTextField(10); 
        JTextField AddSteps = new JTextField(10);
        JLabel StepLabel = new JLabel("STEPS: ");
        private boolean start = false;
        private int steps;
	private final int ZOOM = 4;
                            
        Timer timer;
        int wid = 420;
        int hei = 250;
        private Color[][] colors = new Color[wid][hei];

        private Ant[] ants;
                
        public class runAnt extends TimerTask{    
            public void run(){
                if(start){
                    steps++;
                    Counter.setText(String.valueOf(steps));   
                    for(int i = 0; i < 3; i++){
                        if(colors[ants[i].getY()][ants[i].getX()] == Color.WHITE){
                                //turn left
                                if(ants[i].getXchange() == 0){ //if moving up or down
                                        ants[i].setXchange(ants[i].getYchange());
                                        ants[i].setYchange(0);
                                }else{ //if moving left or right
                                        ants[i].setYchange(-ants[i].getXchange());
                                        ants[i].setXchange(0);
                                }
                                 colors[ants[i].getY()][ants[i].getX()] = ants[i].getColoro();
                        }else{
                                //turn right
                                if(ants[i].getXchange() == 0){ //if moving up or down
                                        ants[i].setXchange(-ants[i].getYchange());
                                        ants[i].setYchange(0);

                                }else{ //if moving left or right
                                        ants[i].setYchange(ants[i].getXchange());
                                        ants[i].setXchange(0);
                                }
                                 colors[ants[i].getY()][ants[i].getX()] = Color.WHITE;
                        }
                    ants[i].setX(ants[i].getX() + ants[i].getXchange());
                    ants[i].setY(ants[i].getY() + ants[i].getYchange());
                    }
                    repaint();
                }
            }
        }
        
        public class Ant{
            private int position[] = new int[2];
            private Color antColor;
            private int xChange; 
            private int yChange;

            public Ant(int x, int y, Color aColor){
                position[0] = x;
                position[1] = y;
                antColor = aColor;
                xChange = 0; 
                yChange = -1;
            }

            public int getX(){
                return position[0];
            }
            public int getY(){
                return position[1];
            }
            public Color getColoro(){
                return antColor;
            }
            public int getXchange(){
                return xChange;
            }
            public int getYchange(){
                return yChange;
            }
            public void setX(int x){
                position[0] = x;
            }
            public void setY(int y){
                position[1] = y;
            }
            public void setColoro(Color aColor){
                antColor = aColor;
            }
            public void setXchange(int xCh){
                xChange = xCh;
            }
            public void setYchange(int yCh){
                yChange = yCh;
            }
    }
     

	public Lang(){
              super("Langton Ant");
              //setSize(800,800);
              setExtendedState(JFrame.MAXIMIZED_BOTH);
              setLayout(null);
              setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
              
              
                add(buttonsPanel());
                drawPanel.setBounds(10,10,wid*ZOOM,hei*ZOOM);
                drawPanel.setBackground(Color.red);
                add(drawPanel);

                for(int i=0; i < wid - 1; i++){
                    for(int j = 0; j < hei - 1; j++){
                        colors[i][j] = Color.WHITE;
                    }
                }

                newAnts();

                setVisible(true);
                
                timer = new Timer();
                timer.schedule(new runAnt(), 0, 20); 
	}

        JPanel buttonsPanel(){
                buttonPanel = new JPanel();
                buttonPanel.setBounds(1700,10,200,800);

                buttons = new JButton[4];
                buttons[0] = new JButton("START");
                buttons[1] = new JButton("STOP");
                buttons[2] = new JButton("ADD ANT");
                buttons[3] = new JButton("DO STEPS");
                
                buttonPanel.setLayout(null);
                
                buttons[0].setBounds(10, 10, 100, 40);
                buttons[1].setBounds(10, 60, 100, 40);
                StepLabel.setBounds(40, 100, 50, 30);                     
                Counter.setBounds(10, 130, 100, 30);
                Counter.setText(String.valueOf(0));   
                buttons[2].setBounds(10, 170, 100, 40);
                AddSteps.setBounds(10, 220, 100, 30);           
                buttons[3].setBounds(10, 260, 100, 40);  

                buttonPanel.add(buttons[0]);
                buttonPanel.add(buttons[1]);
                buttonPanel.add(buttons[2]);
                buttonPanel.add(buttons[3]);
                buttonPanel.add(Counter);
                buttonPanel.add(AddSteps);
                buttonPanel.add(StepLabel); 
                
                buttons[0].addActionListener(this);
                buttons[1].addActionListener(this);
                buttons[2].addActionListener(this);
                buttons[3].addActionListener(this);
            
                buttonPanel.setVisible(true);
                
                return buttonPanel;
        }
        
        public void actionPerformed(ActionEvent e){
            Object click = e.getSource();
            int aStepsToJump;
            
            if(click==buttons[0]) start = true;
            if(click==buttons[1]) start = false;
            if(click==buttons[2]);
            if(click==buttons[3]){
 
            aStepsToJump = Integer.parseInt(AddSteps.getText());
            Counter.setText(String.valueOf(steps += aStepsToJump));
            }
        }
        
        void newAnts(){
            ants = new Ant[4];
                for(int i = 0; i < 3; i++){
                    Random randX = new Random();
                    Random randY = new Random();
                    Color color = Color.BLUE;
                    int x_rand = randX.nextInt(wid-1);
                    int y_rand = randY.nextInt(hei-1);
                    ants[i] = new Ant(x_rand, y_rand, color);
                    System.out.println("Fi x: " + x_rand + " y: " + y_rand);
                }
        }
    JPanel drawPanel = new JPanel(){
         public void paint(Graphics g) {

            for(int x = 0; x < wid - 1; x++){
                for(int y = 0; y < hei - 1; y++){
                        g.setColor(colors[x][y]);
                        g.fillRect(x * ZOOM, y * ZOOM, ZOOM, ZOOM);
                       // System.out.println("hejka");
                }
            }

            g.setColor(Color.GREEN);    //zmienić
            g.fillRect(colors[0].length / 2 * ZOOM, colors.length / 2 * ZOOM, ZOOM/2, ZOOM/2);
            
        }
    };
}

     