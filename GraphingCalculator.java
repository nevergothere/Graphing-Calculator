import javax.swing.*;
import java.awt.*;
import java.awt.geom.*;
import java.util.*;
public class GraphingCalculator extends JPanel {
    Color graphColor1 = Color.RED;
    Color graphColor2 = Color.BLUE;
    // function expression
    String expression1 = "";
    String expression2 = "";
    // for something
    String temp;
    // density of the grid lines
    double scale = 25;
    // how much value is each horizontal/vertical unit 
    double scalex=1,scaley=1;
    int originX = 400;
    int originY = 300;

    //scrolling to adjust the density of the grid lines
public GraphingCalculator() {

    addMouseWheelListener(e -> {

        // Wheel up = zoom in
        if (e.getPreciseWheelRotation() < 0) {
            scale *= 1.1;
        }

        // Wheel down = zoom out
        else {
            scale /= 1.1;
        }

        // Prevent scale from becoming too small
        if (scale < 5) {
            scale = 5;
        }

        repaint();
    });
}
    // Convert math x to screen x
    public double toScreenX(double x) {
        return originX + x * scale;
    }

    // Convert math y to screen y
    public double toScreenY(double y) {
        return originY - y * scale;
    }

    // Example evaluator
    public double evaluate(String expr,double x) {

        return Double.parseDouble(myparse.solve(expr,x));

    }


    //initializing the function expression and scale
public void setExpressions(String expr1, String expr2,String val1,String val2) {
    
    if(val1.equals("")){
        val1="1";
    }
    if(val2.equals("")){
        val2="1";
    }      
    expression1 = expr1;
    expression2 = expr2;
    scalex=Double.parseDouble(val1);
    scaley=Double.parseDouble(val2);
    repaint();
}


    @Override
    protected void paintComponent(Graphics g) {

        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g;
        // paint the axis

        // x-axis
        for(int i=((int)(-800/scale));i<((int)(800/scale));i++){
            g2d.setColor(Color.lightGray);            
            g2d.drawLine((int)(i*scale)+originX,0,(int)(i*scale)+originX,600);
            if(i%Math.max(1,(int)((800/scale)/10))==0){
            g2d.setColor(Color.BLACK);  
            temp=scalex*i+"";
            // used for avoiding float point errors
            if(temp.indexOf(".")!=-1 && temp.length()>temp.indexOf(".")+3){
                temp=temp.substring(0,temp.indexOf(".")+3);
            }
            g2d.drawString(temp,(int)(i*scale)+originX+1,originY-3);       
            }
        }   


            //y-axis    
        for(int i=((int)(-600/scale));i<((int)(600/scale));i++){
            g2d.setColor(Color.lightGray);
            g2d.drawLine(0,(int)(i*scale)+originY,800,(int)(i*scale)+originY);
            if(i%Math.max(1,(int)((600/scale)/10))==0 && i!=0){
            g2d.setColor(Color.BLACK); 
            
            temp=-scaley*i+"";  
            
            if(temp.indexOf(".")!=-1 && temp.length()>temp.indexOf(".")+3){
                temp=temp.substring(0,temp.indexOf(".")+3);
            }                          
            g2d.drawString(temp,originX+3,(int)(i*scale)+originY-2);
       
            }
        }
 
     

        g2d.setColor(Color.BLACK);        
        g2d.drawLine(0, originY, 800, originY);
        g2d.drawLine(originX, 0, originX, 600);  
  
        // Draw graph
        // FIRST GRAPH
        if(!expression1.equals("")){
Path2D.Double path1 = new Path2D.Double();

boolean first1 = true;

for(double x=-400/scale; x<=400/scale; x+=1/scale/3) {

    double y = evaluate(expression1, scalex*x)/scaley;

    if(first1) {
        path1.moveTo(toScreenX(x), toScreenY(y));
        first1 = false;
    }
    else {
        path1.lineTo(toScreenX(x), toScreenY(y));
    }
}


g2d.setColor(graphColor1);
g2d.draw(path1);
        }
        if(!expression2.equals("")){
        // SECOND GRAPH
Path2D.Double path2 = new Path2D.Double();

boolean first2 = true;

for(double x=-400/scale; x<=400/scale; x+=1/scale/3) {

    double y = evaluate(expression2, scalex*x)/scaley;

    if(first2) {
        path2.moveTo(toScreenX(x), toScreenY(y));
        first2 = false;
    }
    else {
        path2.lineTo(toScreenX(x), toScreenY(y));
    }
}

g2d.setColor(graphColor2);
g2d.draw(path2);
}
    }

    public static void main(String[] args) {

        JFrame frame = new JFrame("Graphing Calculator");

        GraphingCalculator panel = new GraphingCalculator();

        JTextField input1 = new JTextField(10);
        JTextField input2 = new JTextField(10);

        JTextField xScaleField = new JTextField( 5);
        JTextField yScaleField = new JTextField( 5);

        JButton colorButton1 = new JButton("Color 1");
        JButton colorButton2 = new JButton("Color 2");
        JButton button = new JButton("Graph");


//color panel
colorButton1.addActionListener(e -> {

    Color chosen = JColorChooser.showDialog(
        frame,
        "Choose Graph 1 Color",
        panel.graphColor1
    );

    if(chosen != null) {
        panel.graphColor1 = chosen;
        panel.repaint();
    }
});

colorButton2.addActionListener(e -> {

    Color chosen = JColorChooser.showDialog(
        frame,
        "Choose Graph 2 Color",
        panel.graphColor2
    );

    if(chosen != null) {
        panel.graphColor2 = chosen;
        panel.repaint();
    }
});

        // Button action
        button.addActionListener(e -> {

        String expr1 = input1.getText();
        String expr2 = input2.getText();    
        String scalex= xScaleField.getText();
        String scaley= yScaleField.getText();
        panel.setExpressions(expr1,expr2,scalex,scaley);
        });



        JPanel topPanel = new JPanel(new GridLayout(2,1));

        JPanel row1 = new JPanel();
        JPanel row2 = new JPanel();

        row1.add(new JLabel("y1 = "));
        row1.add(input1);

        row1.add(new JLabel("y2 = "));
        row1.add(input2);

        row2.add(new JLabel("X Scale"));
        row2.add(xScaleField);

        row2.add(new JLabel("Y Scale"));
        row2.add(yScaleField);

        row2.add(button);
        row2.add(colorButton1);
        row2.add(colorButton2);

        topPanel.add(row1);
        topPanel.add(row2);

        frame.setLayout(new BorderLayout());

        frame.add(topPanel, BorderLayout.NORTH);

        frame.add(panel, BorderLayout.CENTER);

        frame.setSize(800,600);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.setVisible(true);
    }
}









class myparse{
    public static String solve(String s,double x){
        // primary handling of the string expression
        s=strip(s);
        s=s.toLowerCase();
        ArrayList<String> arr=new ArrayList<String>();
        String temp="";
        String cur="";
        for(int i=0;i<s.length();i++){
            cur=s.substring(i,i+1);
            if(isOper(cur)>0 || isPare(cur)>0){
                if(!temp.equals(""))
                arr.add(temp);
                arr.add(cur);
                temp="";                
            }
            else if(cur.equals("x")){
                if(!temp.equals(""))
                arr.add(temp);
                arr.add(x+"");
                temp="";                  
            }
            else{
                temp+=cur;
            }
        }
        if(!temp.equals(""))
            arr.add(temp);        
        return rec(arr);
    }

    // simplify the brackets expression
    public static String rec(ArrayList<String> arr){
        int cnt=0,t1=-1;
        boolean hasBrac=false;
        ArrayList<String> temp=new ArrayList<String>();
        ArrayList<String> arr2=new ArrayList<String>();
        for(int i=0;i<arr.size();i++){
            if(arr.get(i).equals("(")){
                if(t1==-1){
                t1=i;
                cnt=1;
                continue;
                }
                else{
                    cnt++;
                }
            }
            else if(arr.get(i).equals(")")){
                cnt--;
                if(cnt==0){
                    for(int j=0;j<t1;j++){
                        arr2.add(arr.get(j));
                    }
                    arr2.add(rec(temp));
                    for(int j=i+1;j<arr.size();j++){
                        arr2.add(arr.get(j));
                    }
                    temp.clear();
                    hasBrac=true;
                    break;
                }
            }
            if(t1!=-1){
                temp.add(arr.get(i));
            }            
        }
        if(hasBrac){
            return rec(arr2);
        }
        arr2.addAll(arr);
        String[] sign={"*","/","-","+"};
        unaryPre(arr2);  
        cal(arr2,"^");
        unaryPost(arr2);          
        ImpMul(arr2);
        for(String x:sign){
        cal(arr2,x);
        }
        return arr2.get(0);
    }

    // use the operators to calculate expression
    public static void cal(ArrayList<String> arr,String oper){
        for(int i=1;i<arr.size();i++){
            if(arr.get(i).equals(oper)){
                if(oper.equals("^"))
                arr.set(i-1,""+Math.pow(Double.parseDouble(arr.get(i-1)),Double.parseDouble(arr.get(i+1))));
                else if(oper.equals("/"))
                arr.set(i-1,""+(Double.parseDouble(arr.get(i-1))/Double.parseDouble(arr.get(i+1))));
                else if(oper.equals("*"))
                arr.set(i-1,""+(Double.parseDouble(arr.get(i-1))*Double.parseDouble(arr.get(i+1))));
                else if(oper.equals("-"))
                arr.set(i-1,""+(Double.parseDouble(arr.get(i-1))-Double.parseDouble(arr.get(i+1))));  
                else if(oper.equals("+"))
                arr.set(i-1,""+(Double.parseDouble(arr.get(i-1))+Double.parseDouble(arr.get(i+1))));  
            arr.remove(i);
            arr.remove(i);
            i--;                                                    
            }    
        }
    }

    // combine unary expression
    public static void unaryPre(ArrayList<String> arr){
        for(int i=arr.size()-1;i>=0;i--){
            if(arr.get(i).equals("-") && i>0 && (arr.get(i-1).equals("^"))){
                arr.set(i,""+(-1)*Double.parseDouble(arr.get(i+1)));  
                arr.remove(i+1);                                                 
            }    
        }
    }

    public static void unaryPost(ArrayList<String> arr){
        for(int i=arr.size()-1;i>=0;i--){
            if(arr.get(i).equals("-") && (i==0 || (isOper(arr.get(i-1))>0 || isPare(arr.get(i-1))>0))){
                arr.set(i,""+(-1)*Double.parseDouble(arr.get(i+1)));  
                arr.remove(i+1);                                                 
            }    
        }
    }

    //coombine implicit multiplication 
    public static void ImpMul(ArrayList<String> arr){
        for(int i=arr.size()-2;i>=0;i--){
            if(isNum(arr.get(i)) && isNum(arr.get(i+1))){
                arr.set(i,""+Double.parseDouble(arr.get(i))*Double.parseDouble(arr.get(i+1)));  
                arr.remove(i+1);                                                 
            }    
        }
    }

    public static int isOper(String s){
        if(s.equals("+"))
            return 1;
        else if(s.equals("-"))
            return 2;
        else if(s.equals("*"))
            return 3;
        else if(s.equals("/"))
            return 4;
        else if(s.equals("^"))
            return 5;            
        else 
            return 0;
    }

    public static boolean isNum(String s){
        return !(isOper(s)>0||isPare(s)>0);
    }

    public static int isPare(String s){
        if(s.equals("("))
            return 1;
        else if(s.equals(")"))
            return 2;       
        else 
            return 0;
    }    
    // remove whitespace
    public static String strip(String s){
        String s2="";
        for(int i=0;i<s.length();i++){
            if(s.charAt(i)!=' ')
            s2+=s.charAt(i);
        }
        return s2;
    }
}