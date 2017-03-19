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
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class OwnshipConfigurator extends JPanel
{
	private static final long serialVersionUID = 1L;
	
	public static Configuration config= Configuration.getInstance();
	
	public OwnshipConfigurator() 
	{	
		setLayout(null);
	
		{
			JPanel otherPanel = new JPanel();
			otherPanel.setBackground(Color.LIGHT_GRAY);
			otherPanel.setBounds(29, 38, 290, 62);
			add(otherPanel);
			otherPanel.setLayout(null);
			
			JLabel lblVy = new JLabel("VY");
			lblVy.setBounds(10, 11, 37, 15);
			otherPanel.add(lblVy);
				
			final JLabel vyLabel = new JLabel(""+config.ownshipConfig.ownshipVy);
			vyLabel.setBounds(226, 11, 58, 15);
			otherPanel.add(vyLabel);
		
			JSlider ownshipVySlider = new JSlider();
			ownshipVySlider.setBounds(57, 11, 161, 16);
			otherPanel.add(ownshipVySlider);
			ownshipVySlider.setSnapToTicks(true);
			ownshipVySlider.setPaintLabels(true);		
			ownshipVySlider.setMaximum(58);
			ownshipVySlider.setMinimum(-67);
			ownshipVySlider.setValue((int)(config.ownshipConfig.ownshipVy));
			ownshipVySlider.addChangeListener(new ChangeListener() {
				public void stateChanged(ChangeEvent e) {
					JSlider source = (JSlider) e.getSource();
					config.ownshipConfig.ownshipVy = source.getValue();
					vyLabel.setText(""+config.ownshipConfig.ownshipVy);

				}
			});
			
			JLabel lblGs = new JLabel("GS");
			lblGs.setBounds(10, 32, 37, 15);
			otherPanel.add(lblGs);
			
			final JLabel gsLabel = new JLabel(""+config.ownshipConfig.ownshipGs);
			gsLabel.setBounds(226, 32, 58, 15);
			otherPanel.add(gsLabel);
			
			JSlider ownshipGsSlider = new JSlider();
			ownshipGsSlider.setBounds(57, 32, 161, 16);
			otherPanel.add(ownshipGsSlider);
			ownshipGsSlider.setSnapToTicks(true);
			ownshipGsSlider.setPaintLabels(true);		
			ownshipGsSlider.setMaximum(304);
			ownshipGsSlider.setValue((int)(config.ownshipConfig.ownshipGs));
			ownshipGsSlider.addChangeListener(new ChangeListener() {
				public void stateChanged(ChangeEvent e) {
					JSlider source = (JSlider) e.getSource();
					config.ownshipConfig.ownshipGs = source.getValue();
					gsLabel.setText(""+config.ownshipConfig.ownshipGs);
				}
			});			
			
			
		}
		
		
	
					
	}

}
