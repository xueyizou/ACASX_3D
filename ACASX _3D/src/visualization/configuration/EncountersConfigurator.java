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

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;



public class EncountersConfigurator extends JPanel 
{
	private static final long serialVersionUID = 1L;
	
	public static Configuration config= Configuration.getInstance();
	
	public EncountersConfigurator() 
	{
		setLayout(null);
		
		final DefaultListModel<String> listModel= new DefaultListModel<>();
		for(String alias:config.encountersConfig.keySet())
		{
			listModel.addElement(alias);
		}
		
		JButton btnAdd = new JButton("add an intruder");
		btnAdd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {				
				EncounterConfig encounterConfig = new EncounterConfig();
				encounterConfig = new EncounterConfig();				
				String alias = "intruder"+ (config.encountersConfig.size()+1);
				config.encountersConfig.put(alias,encounterConfig);
				listModel.addElement(alias);				
			}
		});
		btnAdd.setBounds(10, 45, 280, 23);
		add(btnAdd);
		
		final JList<String> list = new JList<String>(listModel);
		list.setBorder(new TitledBorder(null, "List of Intruders", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		list.setBackground(Color.ORANGE);
		list.setBounds(10, 102, 280, 160);
		add(list);
		
		JButton btnConfigureSelected = new JButton("Configure Selected");
		btnConfigureSelected.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(list.getSelectedIndex()>=0)
				{
					new EncounterConfigurator(list.getSelectedValue().toString());
				}
				
			}
		});
		btnConfigureSelected.setBounds(10, 273, 129, 23);
		add(btnConfigureSelected);
		
		JButton btnRemoveSelected = new JButton("Remove Selected");
		btnRemoveSelected.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (list.getSelectedIndex()>=0)
				{
					System.out.println(list.getSelectedValue().toString());
					config.encountersConfig.remove(list.getSelectedValue().toString());	
					listModel.removeElementAt(list.getSelectedIndex());				
				}
			}
		});
		btnRemoveSelected.setBounds(175, 273, 115, 23);
		add(btnRemoveSelected);
	}
	
}
