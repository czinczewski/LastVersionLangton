import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.ImageIcon;
import javax.swing.JButton; 
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
 
public class Langton extends JFrame implements ActionListener{
    
    	public static void main(String[] args){
		Langton window = new Langton();
	}
        
        private JPanel buttonPanel;
        private JButton buttons[];
        JTextField Counter = new JTextField(10); 
        JTextField AddSteps = new JTextField(10);
        JLabel StepLabel = new JLabel("STEPS: ");
        
        private boolean start = false;
        private int steps;                              //liczba kroków
        private int add = 1;                            //ilość mrówek
        private boolean jump = false;                   //ręczne dodawanie kroków (przeskok)
        private int stepsToJump;                        //ilość kroków do przeskoku
        
	private final int ZOOM = 6;                     //powiększenie mrówki (uniknięcie sytuacji 1x1 px)             
        
        private Color[][] colors = new Color[200][110]; //kolorowanie panelu
        private Ant[] ants;                             //tablica mrówek
                
        public class runAnt extends TimerTask{  
                    
            public void run(){
                if(start){
                    steps++;
                    Counter.setText(String.valueOf(steps));   
                    if(jump){
                        for(int j = 0; j < stepsToJump; j++){
                            algorithm();
                        }
                        
                        jump = false;
                        stepsToJump = 0;
                        
                    } else {
                        algorithm();
                    }
                    
                    repaint();
                }
            }
        
        
            void algorithm(){
                for(int i = 0; i < add; i++){
                    if(ants[i].exist()){
                        if(colors[ants[i].getX()][ants[i].getY()] == Color.WHITE){
                                //skręcanie w lewo
                                if(ants[i].getXchange() == 0){ //jeśli jest ustawiony w pionie
                                        ants[i].setXchange(ants[i].getYchange());
                                        ants[i].setYchange(0);
                                }else{ //jeśli jest ustawiony w poziomie
                                        ants[i].setYchange(-ants[i].getXchange());
                                        ants[i].setXchange(0);
                                }
                                 colors[ants[i].getX()][ants[i].getY()] = ants[i].getColoro();
                        }else{
                            if(colors[ants[i].getX()][ants[i].getY()] != Color.BLACK){
                                //skręcanie w prawo
                                if(ants[i].getXchange() == 0){ //jeśli jest ustawiony w pionie
                                        ants[i].setXchange(-ants[i].getYchange());
                                        ants[i].setYchange(0);

                                }else{ //jeśli jest ustawiony w poziomie
                                        ants[i].setYchange(ants[i].getXchange());
                                        ants[i].setXchange(0);
                                }
                                 colors[ants[i].getX()][ants[i].getY()] = Color.WHITE;
                            }else
                                ants[i].dead();                          
                        }
                        //przesunięcie
                        ants[i].setX(ants[i].getX() + ants[i].getXchange());
                        ants[i].setY(ants[i].getY() + ants[i].getYchange());

                        //przechodzenie na drugą stronę ekranu
                        if(ants[i].getX() < 0 ){
                            ants[i].setX((colors.length-1) + ants[i].getXchange());
                        }
                        else if(ants[i].getX() == (colors.length-1)){
                            ants[i].setX(-1 + ants[i].getXchange());
                        }

                        if(ants[i].getY() < 0){
                            ants[i].setY((colors[0].length-1) + ants[i].getYchange());
                        }
                        else if(ants[i].getY() == (colors[0].length-1)){
                            ants[i].setY(-1 + ants[i].getYchange());
                        }
                    }
                } 
            }
        }
        
        public class Ant{
            private int position[] = new int[2];
            private Color antColor;
            private int xChange; 
            private int yChange;
            private boolean existing;
            private boolean parent;
            
            public Ant(int x, int y, Color aColor){
                existing = true;
                position[0] = x;
                position[1] = y;
                antColor = aColor;
                xChange = 0; 
                yChange = -1;
                parent = false;
            }

            public boolean exist(){
                return existing;
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
            public void dead(){
                existing = false;
            }
            public boolean getParent(){
                return parent;
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
            public void setParent(){
                parent = true;
            }
        }
     
	public Langton(){
            super("Langton Ant");
            setExtendedState(JFrame.MAXIMIZED_BOTH);
            setLayout(null);
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            
            add(buttonsPanel());
            drawPanel.setBounds(10,10,colors.length*ZOOM,colors[0].length*ZOOM);
            add(drawPanel);

            whitening();       
            newAnts();
            
            Timer timer;
            timer = new Timer();
            timer.schedule(new runAnt(), 0, 20); 
            
            setVisible(true);
	}
        
        void whitening(){
            for(int i=0; i < (colors.length - 1); i++){
                for(int j = 0; j < (colors[0].length - 1); j++){
                    colors[i][j] = Color.WHITE;
                }
            }
        }

        JPanel buttonsPanel(){
            buttonPanel = new JPanel();
            buttonPanel.setBounds(colors.length * ZOOM,10,200,800);

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
            
            if(e.getSource()==buttons[0]) 
                start = true;
            if(e.getSource()==buttons[1]) 
                start = false;
            if(e.getSource()==buttons[2])
                if(add < 100) add++;
            if(e.getSource()==buttons[3]){
                stepsToJump = Integer.parseInt(AddSteps.getText());
                Counter.setText(String.valueOf(steps += stepsToJump));
                jump = true;          
            }
        }
        
        void newAnts(){
            final Color[] antColor = {Color.GREEN, Color.BLUE, Color.RED, Color.MAGENTA, Color.ORANGE, 
                                      Color.YELLOW, Color.GRAY, Color.CYAN, Color.PINK, Color.LIGHT_GRAY};
            ants = new Ant[100];
                for(int i = 0; i < 100; i++ ){
                    Random randX = new Random();
                    Random randY = new Random();
                    Random col = new Random();
                    int x_rand = randX.nextInt(colors.length-1);
                    int y_rand = randY.nextInt(colors[0].length-1);
                    int color = col.nextInt(10);
                    ants[i] = new Ant(x_rand, y_rand, antColor[color]);
                }
        }
        
    JPanel drawPanel = new JPanel(){
        private Color cellColor;
        private int cellX, cellY;
        Image antImage = new ImageIcon("ant.png").getImage();

         public void paint(Graphics g) {
            
            for(int x = 0; x < (colors.length - 1); x++){
                for(int y = 0; y < (colors[0].length - 1); y++){           
                    g.setColor(colors[x][y]);
                    g.fillRect(x * ZOOM, y * ZOOM, ZOOM, ZOOM);
                       
                    for(int i = 0; i < add; i++){
                        if(ants[i].getX() == x && ants[i].getY() == y)
                            if(ants[i].exist())
                                g.drawImage(antImage, x*ZOOM, y*ZOOM, null); 
                        
                        for(int j = 0; j < add; j++){
                            if(i == j) {
                                break;
                            }else{                          
                                if(ants[i].exist() && ants[j].exist() && 
                                   ants[i].getX() > (ants[j].getX() - 3) && ants[i].getX() < (ants[j].getX() + 3) && 
                                   ants[i].getY() > (ants[j].getY() - 3) && ants[i].getY() < (ants[j].getY() + 3)){
                                        if(!ants[i].getParent()){
                                            ants[i].setParent();       
                                            ants[j].setParent();
                                            if(add < 100)
                                                add++;
                                        }
                                        if(ants[i].getParent() && ants[j].getParent())
                                            ants[j].dead();
                                }
                            }
                        }
                    }
                }
            }                             
            
            addMouseListener(new MouseAdapter() {

                @Override
                public void mousePressed(MouseEvent e) {                   
                    cellX = ((e.getX() + (ZOOM - (e.getX()% ZOOM))) / ZOOM) -1 ;
                    cellY = ((e.getY() + (ZOOM - (e.getY()% ZOOM))) / ZOOM) -1;
                   
                    colors[cellX][cellY] = Color.BLACK;

                }
            });
        }
    };
}


     
