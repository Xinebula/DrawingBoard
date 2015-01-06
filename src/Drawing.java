
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by xiny on 2015/1/3.
 */
public class Drawing {
    public static void main(String[] args){
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                OpenWindow();
            }
        });
    }
    public static void OpenWindow(){
        JFrame frame = new JFrame("Hello Swing");
        MouseComponent mouse = new MouseComponent();
        frame.add(mouse);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
        frame.setBounds(env.getCenterPoint().x-300, env.getCenterPoint().y-200,600,400);
        Image img = new ImageIcon("icon.gif").getImage();
        frame.setIconImage(img);
        frame.setTitle("Painting");
        frame.getContentPane().setBackground(Color.WHITE);

        frame.setVisible(true);

    }
}


class Painting{
    private class ImgScale{
        int x;
        int y;
        int width;
        int height;
    }
    private RectangularShape dot;
    private Paint color;
    private int brushType;
    private ImgScale scale;
    public Painting(RectangularShape d,Paint c,int b){
        this.dot = d;
        this.color = c;
        this.brushType = b;
    }
    public Painting(int x1,int y1,int w,int h,int b){
        this.scale = new ImgScale();
        this.scale.x = x1;
        this.scale.y = y1;
        this.scale.width = w;
        this.scale.height = h;
        this.brushType = b;
    }
    public RectangularShape getDot(){
        return this.dot;
    }
    public Paint getColor(){
        return this.color;
    }
    public int getBrushType(){
        return this.brushType;
    }
    public int getScaleX(){
        return this.scale.x;
    }
    public int getScaleY(){
        return this.scale.y;
    }
    public int getScaleWidth(){
        return this.scale.width;
    }
    public int getScaleHeight(){
        return this.scale.height;
    }
}
class MouseComponent extends JComponent{
    private ArrayList<Painting> pencil;
    private int brushType = 1;
    private int brushSize = 10;
    private int colorR = 0;
    private int colorG = 0;
    private int colorB = 0;
    BufferedImage image;
    private Point2D mousePosition = new Point2D.Float(0,0);
    public MouseComponent(){
        try {
            image = ImageIO.read(new File("newpen.gif"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        pencil = new ArrayList<Painting>();
        addMouseListener(new MouseHandler());
        addMouseMotionListener(new MouseMotionHandler());
        JButton clearAll = new JButton("Clear All");
        clearAll.setBounds(2,20,100,50);
        ClearMotion clear = new ClearMotion();
        clearAll.addActionListener(clear);
        this.add(clearAll);
        JButton eraser = new JButton("Eraser");
        eraser.setBounds(308,20,100,50);
        SetEraser aEraser = new SetEraser();
        eraser.addActionListener(aEraser);
        this.add(eraser);
        JButton pencil = new JButton("Pencil");
        pencil.setBounds(104,20,100,50);
        SetPencil aPencil = new SetPencil();
        pencil.addActionListener(aPencil);
        this.add(pencil);
        JButton brush = new JButton("Brush");
        brush.setBounds(206,20,100,50);
        SetBrush aBrush = new SetBrush();
        brush.addActionListener(aBrush);
        this.add(brush);
        JButton newPen = new JButton("newPen");
        newPen.setBounds(410,20,100,50);
        SetNewPen aNewPen = new SetNewPen();
        newPen.addActionListener(aNewPen);
        this.add(newPen);
        JSlider aBrushSize = new JSlider(2,100,10);
        aBrushSize.setBounds(10,340,500,20);
        SizeChanged aSize = new SizeChanged();
        aBrushSize.addChangeListener(aSize);
        this.add(aBrushSize);
        JSlider colorR = new JSlider(SwingConstants.VERTICAL,0,255,0);
        colorR.setBounds(520,1,20,290);
        ColorRChanged aColorR = new ColorRChanged();
        colorR.addChangeListener(aColorR);
        this.add(colorR);
        JSlider colorG = new JSlider(SwingConstants.VERTICAL,0,255,0);
        colorG.setBounds(540,1,20,290);
        ColorGChanged aColorG = new ColorGChanged();
        colorG.addChangeListener(aColorG);
        this.add(colorG);
        JSlider colorB = new JSlider(SwingConstants.VERTICAL,0,255,0);
        colorB.setBounds(560,1,20,290);
        ColorBChanged aColorB = new ColorBChanged();
        colorB.addChangeListener(aColorB);
        this.add(colorB);
    }
    public void paintComponent(Graphics g){
        Graphics2D g2 = (Graphics2D) g;
        Ellipse2D nowPencil = new Ellipse2D.Double(mousePosition.getX()-brushSize/2,mousePosition.getY()-brushSize/2,brushSize,brushSize);
        Rectangle2D nowEraser = new Rectangle2D.Double(mousePosition.getX()-brushSize/2,mousePosition.getY()-brushSize/2,brushSize,brushSize);
        Rectangle2D nowBrush = new Rectangle2D.Double(mousePosition.getX()-3,mousePosition.getY()-brushSize/2,6,brushSize);
        Rectangle2D colorShower = new Rectangle2D.Double(527,313,45,45);
        Rectangle2D colorShower_Frame = new Rectangle2D.Double(525,311,48,48);
        Color nowColor = new Color(colorR,colorG,colorB);
        for(Painting p : pencil){
            if(p.getBrushType()!=3){
                g2.setPaint(p.getColor());
                g2.fill(p.getDot());
            }
            else{
                g2.drawImage(image,p.getScaleX(),p.getScaleY(),p.getScaleWidth(),p.getScaleHeight(),null);
            }
        }
        g2.setPaint(nowColor);
        g2.fill(colorShower);
        g2.setPaint(Color.BLACK);
        g2.draw(colorShower_Frame);
        g2.setPaint(Color.RED);
        g2.drawString("R", 524, 305);
        g2.setPaint(Color.GREEN);
        g2.drawString("G", 544, 305);
        g2.setPaint(Color.BLUE);
        g2.drawString("B",564,305);
        switch(brushType){
            case 1:
                g2.setPaint(nowColor);
                g2.fill(nowPencil);
                break;
            case 0:
                g2.setPaint(Color.WHITE);
                g2.fill(nowEraser);
                g2.setPaint(Color.BLACK);
                g2.draw(nowEraser);
                break;
            case 2:
                g2.setPaint(nowColor);
                g2.fill(nowBrush);
                break;
            case 3:
                g2.drawImage(image,(int)mousePosition.getX()-brushSize/2,(int)mousePosition.getY()-brushSize/2,brushSize,brushSize,null);
                break;
            default:
                break;
        }

    }

    class MouseHandler extends MouseAdapter{
        public void mousePressed(MouseEvent event){
            add(event.getPoint());
        }
    }
    public void add(Point2D p){
        double x = p.getX();
        double y = p.getY();
        switch (brushType) {
            case 1 :
                Ellipse2D aPencil = new Ellipse2D.Double(x-brushSize/2,y-brushSize/2,brushSize,brushSize);
                Painting aPainting = new Painting(aPencil,new Color(colorR,colorG,colorB),1);
                pencil.add(aPainting);
                break;
            case 0 :
                Rectangle2D aEraser = new Rectangle2D.Double(x-brushSize/2,y-brushSize/2,brushSize,brushSize);
                Painting aEraserPaint = new Painting(aEraser,Color.WHITE,0);
                pencil.add(aEraserPaint);
                break;
            case 2 :
                Rectangle2D aBrush = new Rectangle2D.Double(x-3,y-brushSize/2,6,brushSize);
                Painting aBrushPaint = new Painting(aBrush,new Color(colorR,colorG,colorB),2);
                pencil.add(aBrushPaint);
                break;
            case 3:
                Painting aNewBrush = new Painting((int)x-brushSize/2,(int)y-brushSize/2,brushSize,brushSize,3);
                pencil.add(aNewBrush);
            default:
                break;
        }
        repaint();
    }

    class MouseMotionHandler extends MouseMotionAdapter{
        public void mouseMoved(MouseEvent event){
            mousePosition = event.getPoint();
            repaint();
        }
        public void mouseDragged(MouseEvent event){
            mousePosition = event.getPoint();
            add(event.getPoint());
            repaint();
        }
    }

    void clear(){
        pencil.clear();
        repaint();
    }
    class ClearMotion implements ActionListener{
        public void actionPerformed(ActionEvent event){
            clear();
        }
    }

    class SetEraser implements ActionListener{
        public void actionPerformed(ActionEvent event){
            brushType = 0;
        }
    }

    class SetPencil implements ActionListener{
        public void actionPerformed(ActionEvent event){
            brushType = 1;
        }
    }

    class SetBrush implements ActionListener{
        public void actionPerformed(ActionEvent event){
            brushType = 2;
        }
    }

    class SetNewPen implements ActionListener{
        public void actionPerformed(ActionEvent event){
            brushType = 3;
        }
    }
    class SizeChanged implements ChangeListener{
        public void stateChanged(ChangeEvent event){
            JSlider slider =(JSlider) event.getSource();
            brushSize = slider.getValue();
            repaint();
        }
    }

    class ColorRChanged implements ChangeListener{
        public void stateChanged(ChangeEvent event){
            JSlider slider =(JSlider) event.getSource();
            colorR = slider.getValue();
            repaint();
        }
    }
    class ColorGChanged implements ChangeListener{
        public void stateChanged(ChangeEvent event){
            JSlider slider =(JSlider) event.getSource();
            colorG = slider.getValue();
            repaint();
        }
    }
    class ColorBChanged implements ChangeListener{
        public void stateChanged(ChangeEvent event){
            JSlider slider =(JSlider) event.getSource();
            colorB = slider.getValue();
            repaint();
        }
    }
}


