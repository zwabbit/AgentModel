package agentsimulation.GUI;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.HashMap;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;

public class GUIPatchInfo extends JFrame{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static JTextArea patchVars;
	private static JTextArea agentVars;
	private JPanel panel;
	public GUIPatchInfo(){ 
		
		panel = new JPanel();
		Dimension cpSize = new Dimension(300,300);
		getContentPane().add(panel);
		panel.setPreferredSize(cpSize);
		panel.setLayout(null);	

		patchVars = new JTextArea(5, 20);
		patchVars.setBounds(0, 0, 300, 100);
		patchVars.setEditable(false);

		agentVars = new JTextArea(5, 20);
		agentVars.setBounds(0, 110, 300, 190);
		agentVars.setEditable(false);
		

		//Add Components to this panel.
		panel.add(patchVars);
		panel.add(agentVars);
		
		//add(scrollPane, c);
	}
	

	public static void updatePatchInfo(String p){
		patchVars.setText(p);
	}
	
	public static void updateAgentInfo(String a){
		agentVars.setText(a);
	}
	
}
