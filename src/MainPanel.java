/*
 *  
 *   File          :   MainPanel.java
 *  
 *   Project       :   atkpanel generic java application
 *  
 *   Description   :   The main frame of atkpanel
 *  
 *   Author        :   Faranguiss Poncet
 *  
 *   Original      :   Mars 2002
 *  
 *   $Revision$				$Author$
 *   $Date$					$State$
 *  
 *   $Log$
 *  
 *   Copyright (c) 2002 by European Synchrotron Radiation Facility,
 *  		       Grenoble, France
 *  
 *                         All Rights Reserved
 *  
 *  
 */
 
package atkpanel;


/**
 *
 * @author  poncet
 */
import java.util.*;
import java.lang.*;
import javax.swing.*;
import javax.swing.event.*;
import java.awt.event.*;
import fr.esrf.tangoatk.core.*;
import fr.esrf.tangoatk.widget.util.Splash;
import fr.esrf.tangoatk.widget.util.ErrorHistory;
import fr.esrf.tangoatk.widget.util.ErrorPopup;
import fr.esrf.tangoatk.widget.attribute.*;

public class MainPanel extends javax.swing.JFrame {

    private  final Splash                           splash = new Splash();
    private  boolean                                standAlone = false;
    
    private  fr.esrf.tangoatk.core.AttributeList    scalar_atts; /* used in the Scalar Tab */
    private  fr.esrf.tangoatk.core.AttributeList    string_scalar_atts; /* used for string scalar refreshing */
    private  fr.esrf.tangoatk.core.AttributeList    number_scalar_atts; /* used in the global trend */
    private  fr.esrf.tangoatk.core.AttributeList    number_spectrum_atts; /* used in spectrum tabs */
    private  fr.esrf.tangoatk.core.AttributeList    number_image_atts; /* used in number image tabs */
    private  CommandList                            cmdl;
    private  Device                                 panelDev;
    private  ErrorHistory                           errorHistory;
    private  ErrorPopup                             errorPopup;
    private  JFrame                                 trendFrame;

    /** Creates new form AtkPanel */
    public MainPanel(String  devName)
    {
	
	splash.setTitle("AtkPanel");
	splash.setVersion("Prototype");
	splash.setAuthor("Faranguiss Poncet (ESRF)");
	splash.setMessage("Waiting for device-name...");
	splash.initProgress();
        splash.setMaxProgress(12);
	
	if (devName == null)
	{
	    devName = javax.swing.JOptionPane.showInputDialog
		(null, "Please type device name");
	} 
		
        scalar_atts = new fr.esrf.tangoatk.core.AttributeList();
	// Keep only scalar attributes
        scalar_atts.setFilter( new IEntityFilter () 
                         {
                            public boolean keep(IEntity entity)
			    {
                               if (entity instanceof IScalarAttribute)
			       {
				  String message = "Adding scalar attributes(";
				  splash.setMessage(message + entity.getNameSansDevice() + ")...");
                                  return true;
                               }
                               return false;
                            }
                         });
	
	
        number_scalar_atts = new fr.esrf.tangoatk.core.AttributeList();
	// Keep only number scalar attributes
        number_scalar_atts.setFilter( new IEntityFilter () 
                         {
                            public boolean keep(IEntity entity)
			    {
                               if (entity instanceof INumberScalar)
			       {
				  String message = "Adding scalar attributes(";
				  splash.setMessage(message + entity.getNameSansDevice() + ")...");
                                  return true;
                               }
                               return false;
                            }
                         });
	
	
        string_scalar_atts = new fr.esrf.tangoatk.core.AttributeList();
	// Keep only number scalar attributes
        string_scalar_atts.setFilter( new IEntityFilter () 
                         {
                            public boolean keep(IEntity entity)
			    {
                               if (entity instanceof IStringScalar)
			       {
				  String message = "Adding scalar attributes(";
				  splash.setMessage(message + entity.getNameSansDevice() + ")...");
                                  return true;
                               }
                               return false;
                            }
                         });
	
	
        number_spectrum_atts = new fr.esrf.tangoatk.core.AttributeList();
	// Keep only number spectrum attributes
        number_spectrum_atts.setFilter( new IEntityFilter () 
                         {
                            public boolean keep(IEntity entity)
			    {
                               if (entity instanceof INumberSpectrum)
			       {
                        	  if (entity instanceof INumberScalar)
				  {
                                     return false;
                        	  }
				  else
				  {
				     String message = "Adding spectrum attributes(";
				     splash.setMessage(message + entity.getNameSansDevice() + ")...");
                                     return true;
				  }
                               }
                               return false;
                            }
                         });
                         
	
	
        number_image_atts = new fr.esrf.tangoatk.core.AttributeList();
	// Keep only number image attributes
        number_image_atts.setFilter( new IEntityFilter () 
                         {
                            public boolean keep(IEntity entity)
			    {
                               if (entity instanceof INumberImage)
			       {
                        	  if (entity instanceof INumberSpectrum)
				  {
                                     return false;
                        	  }
				  else
				  {
				     String message = "Adding image attributes(";
				     splash.setMessage(message + entity.getNameSansDevice() + ")...");
                                     return true;
				  }
                               }
                               return false;
                            }
                         });
                         

        cmdl = new CommandList();
        panelDev = null;
	
        errorHistory = new fr.esrf.tangoatk.widget.util.ErrorHistory();
	errorPopup = new fr.esrf.tangoatk.widget.util.ErrorPopup();
        scalar_atts.addErrorListener(errorHistory);
        scalar_atts.addSetErrorListener(errorPopup);
        scalar_atts.addSetErrorListener(errorHistory);
        

        cmdl.addErrorListener(errorHistory);
	cmdl.addErrorListener(errorPopup);
	
        try
        {
	   String message = "Getting device " + devName + "...";
	   splash.setMessage(message);
	   panelDev = DeviceFactory.getInstance().getDevice(devName);
	   splash.progress(1);
	   splash.setMessage(message + "done");
        }
        catch (Exception   e)
        {
	   javax.swing.JOptionPane.showMessageDialog(
	      null, "Cannot connect to the device.\n"
	           + "Check the device name you entered;"
		   + " Application will abort ...\n"
		   + "Connection Exception : " + e,
		   "Connection to device failed",
		   javax.swing.JOptionPane.ERROR_MESSAGE);
		   
           System.out.println("AtkPanel: Cannot connect to the device.");
           System.out.println("AtkPanel: Check the device name you entered;");
           System.out.println("AtkPanel: Application aborted....");
           System.out.println("AtkPanel: Connection Exception : " + e);
           System.exit(-1);
        }
	

	if (panelDev == null)
	{
	   javax.swing.JOptionPane.showMessageDialog(
	      null, "Cannot initialize the device object.\n"
	           + "Check the device name you entered;"
		   + " Application will abort ...\n",
		   "Device init failed",
		   javax.swing.JOptionPane.ERROR_MESSAGE);
		   
           System.out.println("AtkPanel: cannot initialize the device object.");
           System.out.println("AtkPanel: Check the device name you entered;");
           System.out.println("AtkPanel: Application aborted....");
           System.exit(-1);
	}
	
	
        try
        {
	   String message = "Importing attributes from " + devName + "...";
           try
           {
	      splash.setMessage(message);
	      scalar_atts.add(devName+"/*");
              number_scalar_atts.add(devName+"/*");
	      string_scalar_atts.add(devName+"/*");
	      splash.progress(4);
              number_spectrum_atts.add(devName+"/*");
	      splash.progress(5);
	      number_image_atts.add(devName+"/*");
	      splash.progress(6);
	      splash.setMessage(message + "done");
           }
           catch (Exception   e)
           {
	      javax.swing.JOptionPane.showMessageDialog(
		 null, "Cannot build the attribute list.\n"
		      + "Application will abort ...\n"
		      + "Exception received : " + e,
		      "No Attribute Exception",
		      javax.swing.JOptionPane.ERROR_MESSAGE);

              System.out.println("AtkPanel: Cannot build attribute list.");
              System.out.println("AtkPanel: Application aborted....");
              System.out.println("AtkPanel: Connection Exception : " + e);
              System.exit(-1);
           }
	   message = "Importing commands from " + devName + "...";
	   cmdl.add(devName+"/*");
	   splash.progress(7);
    	   splash.setMessage(message + "done");
        }
        catch (Exception   e)
        {
	   javax.swing.JOptionPane.showMessageDialog(
	      null, "Cannot build the command list.\n"
		   + "Application will abort ...\n"
		   + "Exception received : " + e,
		   "No Command Exception",
		   javax.swing.JOptionPane.ERROR_MESSAGE);

           System.out.println("AtkPanel: Cannot build the command list.");
           System.out.println("AtkPanel: Application aborted....");
           System.out.println("AtkPanel: Connection Exception : " + e);
           System.exit(-1);
        }
	
	this.setTitle("AtkPanel : "+devName);
			
        initComponents();

	String message = "Initializing commands...";
	splash.setMessage(message);
	fillCmdList();
	splash.progress(8);
	splash.setMessage(message + "done");
	message = "Initializing scalar attributes...";
	splash.setMessage(message);
	
	// Set scrolling 
	java.awt.Dimension  viewSize = showScalarAtts();
	if (viewSize != null)
	   jTabbedPane1.setPreferredSize(viewSize);

	splash.progress(10);
	splash.setMessage(message + "done");
	message = "Initializing number spectrum attributes...";
	splash.setMessage(message);
        
        showNumberSpectrumAtts();
	   

	splash.progress(11);
	splash.setMessage(message + "done");
	message = "Initializing number image attributes...";
	splash.setMessage(message);
        
        showNumberImageAtts();
	   
	splash.progress(12);
	splash.setMessage(message + "done");
	splash.setMessage(message);
	pack();
	
	splash.setVisible(false);
        show();
	
	string_scalar_atts.startRefresher();
	number_scalar_atts.startRefresher();
	number_spectrum_atts.startRefresher();
	number_image_atts.startRefresher();
	
	
	
        Trend   globalTrend = new Trend(trendFrame);
        globalTrend.setModel(number_scalar_atts);
	

        /* Put the globalTrend in a frame with a minimum height and width */
        trendFrame = null;
        trendFrame = new JFrame();

	javax.swing.JPanel jp= new javax.swing.JPanel();
        trendFrame.getContentPane().add(jp, java.awt.BorderLayout.CENTER);
	jp.setPreferredSize(new java.awt.Dimension(600, 300));
	jp.setLayout(new java.awt.GridBagLayout());
	
        java.awt.GridBagConstraints trendGbc;
        trendGbc = new java.awt.GridBagConstraints();
        trendGbc.gridx = 0;
        trendGbc.gridy = 0;
        trendGbc.fill = java.awt.GridBagConstraints.BOTH;
        trendGbc.weightx = 1.0;
        trendGbc.weighty = 1.0;
	
	jp.add(globalTrend, trendGbc);
	trendFrame.pack();

    }
    
    
    public MainPanel(String  args[])
    {
       this(args[0]);
    }
    
    public MainPanel(String  dev, boolean stda)
    {
       this(dev);
       this.standAlone = stda;
    }

    public MainPanel(String  args[], boolean stda)
    {       
       this(args[0]);
       this.standAlone = stda;
    }
        
    
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents() {//GEN-BEGIN:initComponents
            jMenuBar1 = new javax.swing.JMenuBar();
            jMenu1 = new javax.swing.JMenu();
            jMenuItem1 = new javax.swing.JMenuItem();
            jMenu3 = new javax.swing.JMenu();
            jMenuItem5 = new javax.swing.JMenuItem();
            jMenuItem3 = new javax.swing.JMenuItem();
            jMenu2 = new javax.swing.JMenu();
            jMenuItem6 = new javax.swing.JMenuItem();
            jMenuItem7 = new javax.swing.JMenuItem();
            jMenuItem2 = new javax.swing.JMenuItem();
            jMenu4 = new javax.swing.JMenu();
            jMenuItem4 = new javax.swing.JMenuItem();
            jPanel1 = new javax.swing.JPanel();
            jPanel2 = new javax.swing.JPanel();
            commandComboViewer1 = new fr.esrf.tangoatk.widget.command.CommandComboViewer();
            stateViewer1 = new fr.esrf.tangoatk.widget.device.StateViewer();
            jTabbedPane1 = new javax.swing.JTabbedPane();
            jScrollPane1 = new javax.swing.JScrollPane();
            jPanel3 = new javax.swing.JPanel();
            
            jMenu1.setText("File");
            jMenuItem1.setText("Quit");
            jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    jMenuItem1ActionPerformed(evt);
                }
            });
            
            jMenu1.add(jMenuItem1);
            jMenuBar1.add(jMenu1);
          jMenu3.setText("View");
            jMenuItem5.setText("Trends ");
            jMenuItem5.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    viewTrendActionPerformed(evt);
                }
            });
            
            jMenu3.add(jMenuItem5);
            jMenuItem3.setText("Error History");
            jMenuItem3.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    viewErrHistoryActionPerformed(evt);
                }
            });
            
            jMenu3.add(jMenuItem3);
            jMenuBar1.add(jMenu3);
          jMenu2.setText("Options");
            jMenuItem6.setText("Stop   Refreshing");
            jMenuItem6.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    stopStartRefreshActionPerformed(evt);
                }
            });
            
            jMenu2.add(jMenuItem6);
            jMenuItem7.setText("Refresh  once");
            jMenuItem7.setEnabled(false);
            jMenuItem7.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    refreshOnceActionPerformed(evt);
                }
            });
            
            jMenu2.add(jMenuItem7);
            jMenuItem2.setText("Set refreshing period ...");
            jMenuItem2.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    refPeriodActionPerformed(evt);
                }
            });
            
            jMenu2.add(jMenuItem2);
            jMenuBar1.add(jMenu2);
          jMenu4.setText("Help");
            jMenuItem4.setText("On Version ...");
            jMenuItem4.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    helpVersionActionPerformed(evt);
                }
            });
            
            jMenu4.add(jMenuItem4);
            jMenuBar1.add(jMenu4);
          
            addWindowListener(new java.awt.event.WindowAdapter() {
                public void windowClosing(java.awt.event.WindowEvent evt) {
                    exitForm(evt);
                }
            });
            
            jPanel1.setLayout(new java.awt.GridBagLayout());
            java.awt.GridBagConstraints gridBagConstraints1;
            
            jPanel1.setMinimumSize(new java.awt.Dimension(22, 22));
            jPanel2.setLayout(new java.awt.GridBagLayout());
            java.awt.GridBagConstraints gridBagConstraints2;
            
            gridBagConstraints2 = new java.awt.GridBagConstraints();
            gridBagConstraints2.gridx = 0;
            gridBagConstraints2.gridy = 1;
            gridBagConstraints2.insets = new java.awt.Insets(5, 5, 5, 5);
            jPanel2.add(commandComboViewer1, gridBagConstraints2);
            
            stateViewer1.setModel(panelDev);
            gridBagConstraints2 = new java.awt.GridBagConstraints();
            jPanel2.add(stateViewer1, gridBagConstraints2);
            
            gridBagConstraints1 = new java.awt.GridBagConstraints();
          gridBagConstraints1.fill = java.awt.GridBagConstraints.HORIZONTAL;
          gridBagConstraints1.insets = new java.awt.Insets(5, 5, 5, 5);
          jPanel1.add(jPanel2, gridBagConstraints1);
          
          jTabbedPane1.setTabPlacement(javax.swing.JTabbedPane.BOTTOM);
              jPanel3.setLayout(new java.awt.GridBagLayout());
              java.awt.GridBagConstraints gridBagConstraints3;
              
              jScrollPane1.setViewportView(jPanel3);
              
              jTabbedPane1.addTab("Scalar", jScrollPane1);
            
            gridBagConstraints1 = new java.awt.GridBagConstraints();
          gridBagConstraints1.gridx = 0;
          gridBagConstraints1.gridy = 1;
          gridBagConstraints1.fill = java.awt.GridBagConstraints.BOTH;
          gridBagConstraints1.weightx = 1.0;
          gridBagConstraints1.weighty = 1.0;
          jPanel1.add(jTabbedPane1, gridBagConstraints1);
          
          getContentPane().add(jPanel1, java.awt.BorderLayout.CENTER);
        
        setJMenuBar(jMenuBar1);
        pack();
    }//GEN-END:initComponents

    private void refreshOnceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_refreshOnceActionPerformed
        // Add your handling code here:
	
	string_scalar_atts.refresh();
	number_scalar_atts.refresh();
	number_spectrum_atts.refresh();
	number_image_atts.refresh();
	
    }//GEN-LAST:event_refreshOnceActionPerformed

    private void stopStartRefreshActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_stopStartRefreshActionPerformed
        // Add your handling code here:
	javax.swing.JMenuItem   menuButton;
	
	menuButton = (javax.swing.JMenuItem) evt.getSource();
	if (menuButton.getText().equalsIgnoreCase("Stop   Refreshing"))
	{
	   menuButton.setText("Start   Refreshing");
	   jMenuItem7.setEnabled(true);
	
	   string_scalar_atts.stopRefresher();
	   number_scalar_atts.stopRefresher();
	   number_spectrum_atts.stopRefresher();
	   number_image_atts.stopRefresher();
	}
	else
	{
	   menuButton.setText("Stop   Refreshing");
           jMenuItem7.setEnabled(false);
	   
	   string_scalar_atts.startRefresher();
	   number_scalar_atts.startRefresher();
	   number_spectrum_atts.startRefresher();
	   number_image_atts.startRefresher();
	}
    }//GEN-LAST:event_stopStartRefreshActionPerformed

    private void helpVersionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_helpVersionActionPerformed
        // Add your handling code here:
        String    versionText, versNumber;
        int       colon_idx, dollar_idx;
        
        versionText = new String("$Revision$");
        
        colon_idx = versionText.lastIndexOf(":");
        dollar_idx = versionText.lastIndexOf("$");
        versNumber = versionText.substring(colon_idx+1, dollar_idx);
        try
        {
            javax.swing.JOptionPane.showMessageDialog(this, 
	    "\n\n   atkpanel   : "+versNumber
	   +"\n\n   ESRF  :   Computing  Services \n\n",
            "atkpanel   Version", javax.swing.JOptionPane.PLAIN_MESSAGE );
        }
        catch (Exception e)
        {
        }
        
    }//GEN-LAST:event_helpVersionActionPerformed

    private void viewTrendActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_viewTrendActionPerformed
        // Add your handling code here:
        trendFrame.setVisible(true);
    }//GEN-LAST:event_viewTrendActionPerformed

    private void refPeriodActionPerformed(java.awt.event.ActionEvent evt)
    {//GEN-FIRST:event_refPeriodActionPerformed
        // Add your handling code here:
	
	int  ref_period = -1;
	ref_period = number_scalar_atts.getRefreshInterval();
	if (ref_period == -1)
	   ref_period = string_scalar_atts.getRefreshInterval();
	if (ref_period == -1)
	   ref_period = number_spectrum_atts.getRefreshInterval();
	if (ref_period == -1)
	   ref_period = number_image_atts.getRefreshInterval();
//	System.out.println("Initial ref period = "+(Integer.toString(ref_period)));

        String refp_str = JOptionPane.showInputDialog(this,"Enter refresh interval (ms)",(Object) new Integer(ref_period));
	if ( refp_str != null )
	{
	    try
	    {
        	int it = Integer.parseInt(refp_str);
//		System.out.println("Set ref period to : "+it);

		string_scalar_atts.setRefreshInterval(it);
		number_scalar_atts.setRefreshInterval(it);
		number_spectrum_atts.setRefreshInterval(it);
		number_image_atts.setRefreshInterval(it);
	    }
	    catch ( NumberFormatException e )
	    {
        	JOptionPane.showMessageDialog(this,"Invalid number !","Error",JOptionPane.ERROR_MESSAGE);
		return;
	    }
	}
	
    }//GEN-LAST:event_refPeriodActionPerformed


    private void viewErrHistoryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_viewErrHistoryActionPerformed
        // Add your handling code here:
	errorHistory.setVisible(true);
    }//GEN-LAST:event_viewErrHistoryActionPerformed


    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        // Add your handling code here:
	if (standAlone == true)
           System.exit(0);
	else
	{
	   string_scalar_atts.stopRefresher();
	   number_scalar_atts.stopRefresher();
	   number_spectrum_atts.stopRefresher();
	   number_image_atts.stopRefresher();
	   DeviceFactory.getInstance().stopRefresher();
	   this.dispose();
	}
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    /** Exit the Application */
    private void exitForm(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_exitForm
        if (standAlone == true)
	   System.exit(0);
	else
	{
	   string_scalar_atts.stopRefresher();
	   number_scalar_atts.stopRefresher();
	   number_spectrum_atts.stopRefresher();
	   number_image_atts.stopRefresher();
	   DeviceFactory.getInstance().stopRefresher();
	   this.dispose();
	}
    }//GEN-LAST:event_exitForm



    private void fillCmdList()
    {
       int        nb_cmds;
       
       // Filter the commands to show here :
       
       nb_cmds = cmdl.getSize();
	
       if (nb_cmds == 0)
       {
           commandComboViewer1.setModel(null);
       }
       else
           commandComboViewer1.setModel(cmdl);
    }







    private java.awt.Dimension showScalarAtts()
    {
        java.awt.GridBagConstraints                 gbc;
	int                                         nb_atts, idx;
        AScalarViewer                               att_read;
	IScalarAttribute                            scalar = null;
	java.awt.Font                               font;
	double                                      currWidth = 0,
	                                            maxLabelWidth = 0, 
						    maxUnitWidth = 0,
						    visibleWidth = 0,
						    visibleHeight = 0;
	java.awt.Dimension                          visibleSize;
	final int                                   nbVisibleAtts = 15;
	final int                                   maximumWidth = 600;
						    
        Vector                                      attReadVect;
	
      	
	attReadVect = new Vector();
	font = new java.awt.Font("Helvetica", 0, 12);
	
        gbc = new java.awt.GridBagConstraints();
        gbc.anchor = java.awt.GridBagConstraints.EAST;
        gbc.insets = new java.awt.Insets(0, 5, 0, 5);
     
	nb_atts = scalar_atts.getSize();
	for (idx=0; idx < nb_atts; idx++)
	{
            scalar = (IScalarAttribute) scalar_atts.getElementAt(idx);

            if (scalar instanceof INumberScalar)
	    {
                att_read = new NumberScalarViewer();
                ((NumberScalarViewer)att_read).setModel((INumberScalar)scalar);
            } 
	    else
	    {
                att_read = new StringScalarViewer();
                ((StringScalarViewer)att_read).setModel((IStringScalar)scalar);
            } // end of else
            
	    att_read.setBorder(null);
	    att_read.setValueBorder(javax.swing.BorderFactory.createLoweredBevelBorder());
	    att_read.setForeground(java.awt.Color.black);
	    att_read.setFont(font);
            att_read.setPropertyListEditable(true);

            att_read.setValueMaximumLength(4);

	    if (scalar.isWritable())
	    {
	       att_read.setValueEditable(true);
	    }
	    
	    currWidth = att_read.getLabelWidth();
	    if (maxLabelWidth < currWidth)
	       maxLabelWidth = currWidth;

	    currWidth = att_read.getUnitWidth();
	    if (maxUnitWidth < currWidth)
	       maxUnitWidth = currWidth;
	       

	    gbc.gridx = 0;
	    gbc.gridy = idx;
	    gbc.fill = java.awt.GridBagConstraints.BOTH;
	    gbc.weightx = 1.0;
	    gbc.weighty = 1.0;
	    jPanel3.add(att_read, gbc);
	    attReadVect.add(idx, att_read);
	    
	    if (scalar.isWritable())
	    {

	    }
	}

	for (int i = 0; i < attReadVect.size(); i++)
	{
	    AScalarViewer v = (AScalarViewer)attReadVect.elementAt(i);
	    v.setLabelWidth(maxLabelWidth);
	    v.setUnitWidth(maxUnitWidth);
	} // end of for ()


        /* Compute the size of the window depending on the number of visible */
	/* scalar attributes.If there are lots of scalar attributes the height */
	/* is set to the height necessary to show a maximum number of visible */
	/* scalar attributes defined by nbVisibleAtts constant. */
	if (nb_atts == 0)
	{
           javax.swing.JLabel  noatt = new javax.swing.JLabel();
	   noatt.setText("No   attribute ");
           noatt.setFont(new java.awt.Font("Helvetica", 0, 18));
           noatt.setForeground(java.awt.Color.black);

           gbc.gridx = 0;
	   gbc.gridy = 0;
	   jPanel3.add(noatt, gbc);
	   visibleSize = null;
	}
	else
	{ // Compute the size of visible area (maximum attributes) inside scrollPane
           this.pack();
	   visibleWidth = jTabbedPane1.getPreferredSize().getWidth()+30;
	   if (nb_atts > nbVisibleAtts)
	      visibleHeight = ((jTabbedPane1.getPreferredSize().getHeight() / nb_atts)+4)* nbVisibleAtts;
	   else
	      visibleHeight = jTabbedPane1.getPreferredSize().getHeight()+ (3*nb_atts);
	      
           if (visibleWidth > maximumWidth)
	   {
	      visibleWidth = maximumWidth;
	      visibleHeight = visibleHeight + 50; // reserved for the horiz ScrollBar
	   }
	   visibleSize = new java.awt.Dimension();
	   visibleSize.setSize(visibleWidth, visibleHeight);
	}
	
	return(visibleSize);
    }


    

    private void showNumberSpectrumAtts()
    {
	int                                         nb_atts, idx;
        SpectrumPanel                               att_tab;
	INumberSpectrum                             spectrum_att = null;
	
	
	nb_atts = number_spectrum_atts.getSize();
	for (idx=0; idx < nb_atts; idx++)
	{
            spectrum_att = (INumberSpectrum) number_spectrum_atts.getElementAt(idx);
            // Create one Spectrum Panel per Number Spectrum Attribute
            att_tab = new SpectrumPanel(spectrum_att);
            // Add the spectrum panel as a tab into the tabbed panel of the main frame
            jTabbedPane1.addTab(spectrum_att.getNameSansDevice(), att_tab);
        }
    }


    

    private void showNumberImageAtts()
    {
	int                          nb_atts, idx;
        ImagePanel                   att_tab;
	INumberImage                 image_att = null;
	
	
						        	     
	nb_atts = number_image_atts.getSize();
	for (idx=0; idx < nb_atts; idx++)
	{
            image_att = (INumberImage) number_image_atts.getElementAt(idx);
            /* Create one Spectrum Panel per Number Spectrum Attribute*/
            att_tab = new ImagePanel(image_att);
            /* Add the spectrum panel as a tab into the tabbed panel of the main frame */
            jTabbedPane1.addTab(image_att.getNameSansDevice(), att_tab);
        }
    }
    
     
    /**
    * @param args the command line arguments
    */
    public static void main(String args[])
    {
       if (args.length <= 0)
          new MainPanel((String) null, true);
       else
	  new MainPanel(args[0], true);
        
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenu jMenu3;
    private javax.swing.JMenuItem jMenuItem5;
    private javax.swing.JMenuItem jMenuItem3;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenuItem jMenuItem6;
    private javax.swing.JMenuItem jMenuItem7;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JMenu jMenu4;
    private javax.swing.JMenuItem jMenuItem4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private fr.esrf.tangoatk.widget.command.CommandComboViewer commandComboViewer1;
    private fr.esrf.tangoatk.widget.device.StateViewer stateViewer1;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JPanel jPanel3;
    // End of variables declaration//GEN-END:variables

}
