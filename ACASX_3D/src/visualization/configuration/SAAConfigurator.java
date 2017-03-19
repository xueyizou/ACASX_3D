/*******************************************************************************
 *  Copyright (C) Xueyi Zou - All Rights Reserved
 *  Written by Xueyi Zou <xz972@york.ac.uk>, 2015
 *  You are free to use/modify/distribute this file for whatever purpose!
 *  -----------------------------------------------------------------------
 *  |THIS FILE IS DISTRIBUTED "AS IS", WITHOUT ANY EXPRESS OR IMPLIED
 *  |WARRANTY. THE USER WILL USE IT AT HIS/HER OWN RISK. THE ORIGINAL
 *  |AUTHORS AND COPPELIA ROBOTICS GMBH WILL NOT BE LIABLE FOR DATA LOSS,
 *  |DAMAGES, LOSS OF PROFITS OR ANY OTHER KIND OF LOSS WHILE USING OR
 *  |MISUSING THIS SOFTWARE.
 *  ------------------------------------------------------------------------
 *******************************************************************************/
package visualization.configuration;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import sim.display.GUIState;
import sim.engine.SimState;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.BorderLayout;


public class SAAConfigurator extends JFrame
{	
	private static final long serialVersionUID = 1L;
	public JTabbedPane tabbedPane=null;
	public JPanel modelBuilderConfigPanel=null;
	public JPanel ownshipConfigPanel=null;
	public JPanel intrudersPanel=null;
	

	/**
	 * Create the frame.
	 */
	public SAAConfigurator(SimState state, GUIState stateWithUI) 
	{
		super("SAA Configurator");
		this.setBounds(1500+80, 404, 340,742);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);		
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setBounds(0, 12, 326, 818);
				
		modelBuilderConfigPanel = new GlobalConfigurator(state, stateWithUI);		
		modelBuilderConfigPanel.setLayout(null);
		tabbedPane.addTab("ModelBuilder", null, modelBuilderConfigPanel, null);
		
		JButton btnRefresh = new JButton("Refresh");
		getContentPane().add(btnRefresh, BorderLayout.NORTH);
		btnRefresh.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				refresh();
			}
		});
		
		ownshipConfigPanel = new OwnshipConfigurator();
		ownshipConfigPanel.setLayout(null);
		tabbedPane.addTab("Own-ship", null, ownshipConfigPanel, null);
		
					
		intrudersPanel = new EncountersConfigurator();
		intrudersPanel.setLayout(null);
		tabbedPane.addTab("Intruders", null, intrudersPanel, null);
	
		this.getContentPane().add(tabbedPane);
		

	}
	
	public void refresh()
	{
		JTabbedPane tabbedPane= (JTabbedPane)(this.getContentPane().getComponent(1));
		tabbedPane.remove(1);
		tabbedPane.remove(1);
		
		ownshipConfigPanel = new OwnshipConfigurator();
		tabbedPane.addTab("Own-ship", null, ownshipConfigPanel, null);
					
		intrudersPanel = new EncountersConfigurator();
		tabbedPane.addTab("Intruders", null, intrudersPanel, null);
	}
}

