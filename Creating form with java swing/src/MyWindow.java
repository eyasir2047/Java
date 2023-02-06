import javax.swing.*;
import java.awt.*;

public class MyWindow extends JFrame {

    //variable declaring

    private JLabel heading;
    Font font=new Font("", Font.BOLD,30);

    private JPanel mainPanel;
    private JLabel nameLabel,passwordLabel;
    private JTextField nameTextField;
    private JPasswordField passwordField;
    private JButton button1,button2;





    //constructor

    public MyWindow(){
        super.setTitle("My First Form...");
        super.setSize(500,500);
        super.setLocation(100,100);

        super.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        this.createGUI();
        super.setVisible(true);
        System.out.println("this is constructor...");
        //level, panel->JPanel,grid layout->row & column,
        //level-5 ta, north,south,east,west,center



    }

    public void createGUI(){
        //this method create our GUI

        this.setLayout(new BorderLayout());
        heading= new JLabel("My First Form...");
        heading.setFont(font);
        heading.setHorizontalAlignment(JLabel.CENTER);

        this.add(heading,BorderLayout.NORTH);

        //////////working with JPanel

        mainPanel = new JPanel();

        //setting layout of mainPanel
        mainPanel.setLayout(new GridLayout(3,2));

        nameLabel= new JLabel("Enter name:");
        nameLabel.setFont(font);

        passwordLabel=new JLabel("Enter password:");
        passwordLabel.setFont(font);

        nameTextField= new JTextField();
        nameTextField.setFont(font);

        passwordField=new JPasswordField();
        passwordField.setFont(font);

        button1=new JButton("Submit");
        button1.setFont(font);

        button2 =new JButton("Reset");
        button2.setFont(font);

        mainPanel.add(nameLabel);
        mainPanel.add(nameTextField);
        mainPanel.add(passwordLabel);
        mainPanel.add(passwordField);
        mainPanel.add(button1);
        mainPanel.add(button2);

        this.add(mainPanel,BorderLayout.CENTER);







    }

}
