package agentsimulation.GUI;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import agentsimulation.AgentSimulation;

import java.awt.*;
import java.awt.event.*;

public class GUIControls extends JFrame implements ItemListener, ChangeListener{

	JButton playButton;
	JButton pauseButton;
	JButton updateButton;
	JPanel panel;	
	JButton stepButton;
	JCheckBox waitForGui;
	JSlider speedSlider;

	//JButton resetButton;
	
	//JTextArea stepSpeed;
	//int stepSize = 400;
	boolean doStep = false;
	boolean play = false;
	boolean reset = true;
	
	public GUIControls(){
		init();
	}
	
	public void setPlay(boolean p){
		play = p;
	}
	
	private final void init(){
		panel = new JPanel();
		Dimension cpSize = new Dimension(300,300);
		getContentPane().add(panel);
		panel.setPreferredSize(cpSize);
		panel.setLayout(null);		
		playButton = new JButton("Play");
		playButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e){
				AgentSimulation.startSim();
			}
		});
		
		playButton.setBounds(0,0,60,30);
		
		pauseButton = new JButton("Pause");
		pauseButton.setBounds(0,30,90,30);
		pauseButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e){
				
			}
		});
		
		updateButton = new JButton("Force Update");
		updateButton.setBounds(0,60,110,30);
		updateButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e){
				
			}
		});
		
		stepButton = new JButton("Step");
		stepButton.setBounds(0, 120, 60, 30);
		stepButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e){
				
			}
		});
		
		waitForGui = new JCheckBox("Wait for GUI?");
		waitForGui.setSelected(false);
		waitForGui.addItemListener(this);
		waitForGui.setBounds(60, 100, 120, 30);
		
		 //Create the slider.
        speedSlider = new JSlider(JSlider.HORIZONTAL, 0, 100, 0);
         
 
        speedSlider.addChangeListener(this);
 
        //Turn on labels at major tick marks.
 
        speedSlider.setMajorTickSpacing(25);
        speedSlider.setMinorTickSpacing(1);
        speedSlider.setPaintTicks(true);
        speedSlider.setPaintLabels(true);
        speedSlider.setBounds(0, 160, 300, 45);
        
		/*
		resetButton = new JButton("Reset");
		resetButton.setBounds(120, 40, 80, 30);
		resetButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e){
				play = false;
				reset = true;
			}
		});
		*/
		
		panel.add(pauseButton);
		panel.add(playButton);
		panel.add(stepButton);
		panel.add(updateButton);
		panel.add(waitForGui);
		panel.add(speedSlider);
		
		setTitle("Control Panel");
		setSize(300,200);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	}
	
	public void pauseCheck(){
		//stepSpeed.setText(Integer.toString(stepSize));
		if (reset){
			//World.resetWorld();
			reset = false;
		}
		//while(true){
			//if (play == true || doStep == true){
				//doStep = false;
				//return;
			//}
			
		//}
	}

	@Override
	public void itemStateChanged(ItemEvent e) {
		// TODO Auto-generated method stub
        Object source = e.getItemSelectable();
 
        if (source == waitForGui) {
        	if (e.getStateChange() == ItemEvent.DESELECTED) {
        		//actor.tell(new WaitForGUI(false));
            }
        	else{
        		//actor.tell(new WaitForGUI(true));
        	}
        }
	}

	@Override
	public void stateChanged(ChangeEvent e) {
		// TODO Auto-generated method stub
		JSlider source = (JSlider)e.getSource();
		if (!source.getValueIsAdjusting()) {
			int speed = (int)source.getValue();
			GUIMain.setSimSpeed(speed);
		}
	}
}
