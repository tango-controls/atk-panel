/*
 *
 *   Copyright (C) :	2002,2003,2004,2005,2006,2007,2008,2009,2010,2011,2012,
 *                      2013
 *			European Synchrotron Radiation Facility
 *			BP 220, Grenoble 38043
 *			FRANCE
 *
 *   This file is part of Tango.
 *
 *   Tango is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 * 
 *   Tango is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 * 
 *   You should have received a copy of the GNU General Public License
 *   along with Tango.  If not, see <http: //www.gnu.org/licenses/>.
 *
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
 */
 
package atkpanel;


/**
 *
 * @author  poncet
 */

import fr.esrf.Tango.DevFailed;
import java.util.*;
import javax.swing.*;
import javax.swing.event.*;
import java.awt.Component;

import fr.esrf.tangoatk.core.*;
import fr.esrf.tangoatk.widget.util.Splash;
import fr.esrf.tangoatk.widget.util.ErrorHistory;
import fr.esrf.tangoatk.widget.util.ErrorPopup;
import fr.esrf.tangoatk.widget.attribute.*;
import fr.esrf.tangoatk.widget.util.ATKGraphicsUtils;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class MainPanel extends javax.swing.JFrame {

    private  final Splash                           splash = new Splash();
    private  final int                              MIN_WIDTH = 220;
    private  final int                              MAX_WIDTH = 570;
    private  final int                              MIN_HEIGHT = 220;
    private  final int                              MAX_HEIGHT = 570;
    
    
    private  boolean                                standAlone = false;
    private  boolean                                keepStateRefresher = true;
    private  boolean                                operatorView = true;
    private  boolean                                modifPropButton = true; // property button visible
    private  boolean                                roMode = false; // read only mode
    private  String                                 startupTabName = null; // the name of the tab (attribute) to be shown at startup
    private  int                                    startupTabIndex = -1; // The index of the tab to be shown at startup
    
    private  fr.esrf.tangoatk.core.AttributeList    all_scalar_atts; /* used in the scalar tab */
    private  fr.esrf.tangoatk.core.AttributeList    op_scalar_atts;  /* used for operator attributes */
    private  fr.esrf.tangoatk.core.AttributeList    exp_scalar_atts;  /* used for expert attributes */
    private  fr.esrf.tangoatk.core.AttributeList    state_status_atts; /* used for state and status attribute viewers */
    private  fr.esrf.tangoatk.core.AttributePolledList    number_scalar_atts; /* used in the global trend */
    private  fr.esrf.tangoatk.core.AttributePolledList    boolean_scalar_atts; /* used in the boolean trend */

    private  fr.esrf.tangoatk.core.AttributeList    all_spectrum_atts; /* used in spectrum tabs */
    private  fr.esrf.tangoatk.core.AttributeList    op_spectrum_atts; /* used in spectrum tabs */
    private  List<JComponent>                       all_spectrum_panels=null;

    private  fr.esrf.tangoatk.core.AttributeList    all_number_image_atts; /* used in number image tabs */
    private  fr.esrf.tangoatk.core.AttributeList    op_number_image_atts; /* used in number image tabs */
    private  List<ImagePanel>                       all_image_panels=null;

    private  fr.esrf.tangoatk.core.AttributeList    all_string_image_atts; /* used in string image tabs */
    private  fr.esrf.tangoatk.core.AttributeList    op_string_image_atts; /* used in string image tabs */
    private  List<StringImagePanel>                 all_string_image_panels=null;

    private  fr.esrf.tangoatk.core.AttributeList    all_raw_image_atts; /* used in raw image tabs */
    private  fr.esrf.tangoatk.core.AttributeList    op_raw_image_atts; /* used in string image tabs */
    private  List<RawImagePanel>                    all_raw_image_panels=null;

    private  CommandList                            all_cmdl;
    private  CommandList                            op_cmdl;
    
    
    private  Device                                 panelDev;
    private  ErrorHistory                           errorHistory;
    private  ErrorPopup                             errorPopup;
    private  JFrame                                 trendFrame;
    private  JFrame                                 booleanTrendFrame;

    private  ScalarListViewer                       allScalarListViewer;
    private  ScalarListViewer                       operatorScalarListViewer;
    private  IDevStateScalar                        stateAtt=null;
    private  IStringScalar                          statusAtt=null;
    private  Trend                                  globalTrend=null;
    private  BooleanTrend                           booleanTrend=null;
    
    private int iTabbedPaneIndex = 0;

    private boolean refresherActivated = true;

    private static final String                     REVISION="Revision: 4.9 ";
    
    private JDialog                                 tgDevtestDlg = null;
    
    private static int[]                            wpos = null;

    /** Creates new form AtkPanel */
    
    private MainPanel()
    {
        java.awt.GridBagConstraints trendGbc;
        trendGbc = new java.awt.GridBagConstraints();
        trendGbc.gridx = 0;
        trendGbc.gridy = 0;
        trendGbc.fill = java.awt.GridBagConstraints.BOTH;
        trendGbc.weightx = 1.0;
        trendGbc.weighty = 1.0;
	
        trendFrame = new JFrame();
	javax.swing.JPanel jp1= new javax.swing.JPanel();
        trendFrame.getContentPane().add(jp1, java.awt.BorderLayout.CENTER);
	jp1.setPreferredSize(new java.awt.Dimension(600, 300));
	jp1.setLayout(new java.awt.GridBagLayout());

        globalTrend = new Trend(trendFrame);
	jp1.add(globalTrend, trendGbc);
	trendFrame.pack();
	
	
	
        booleanTrendFrame = new JFrame();
	javax.swing.JPanel jp2= new javax.swing.JPanel();
        booleanTrendFrame.getContentPane().add(jp2, java.awt.BorderLayout.CENTER);
	jp2.setPreferredSize(new java.awt.Dimension(600, 300));
	jp2.setLayout(new java.awt.GridBagLayout());
	
        booleanTrend = new BooleanTrend();
	jp2.add(booleanTrend, trendGbc);
	booleanTrendFrame.pack();
    }
    
    
    public MainPanel(String  devName)
    {
	this();
	if (connectDevice(devName) == false)
	{
	   splash.setVisible(false);
	   return;		
        }
	initComponents();
	startUp();
    }
    
    
    public MainPanel(String  args[])
    {
        this(args[0]);
    }
    
    public MainPanel(String  dev, boolean stda)
    {
	this();
        this.standAlone = stda;
	if (connectDevice(dev) == false)
	{
	   splash.setVisible(false);
	   return;		
        }
        initComponents();
	startUp();
    }

    public MainPanel(String  args[], boolean stda)
    {       
	this();
        this.standAlone = stda;
	if (connectDevice(args[0]) == false)
	{
	   splash.setVisible(false);
	   return;		
        }
        initComponents();
	startUp();
    }

    public MainPanel(String  dev, boolean stda, boolean keepStateRef)
    {       
	this();
        this.standAlone = stda;
	this.keepStateRefresher = keepStateRef;
	if (connectDevice(dev) == false)
	{
	   splash.setVisible(false);
	   return;		
        }
        initComponents();
	startUp();
    }

    public MainPanel(String  dev, String tab, boolean stda, boolean keepStateRef)
    {
	this();
        this.startupTabName = tab;
        this.standAlone = stda;
	this.keepStateRefresher = keepStateRef;
	if (connectDevice(dev) == false)
	{
	   splash.setVisible(false);
	   return;
        }
        initComponents();
	startUp();
    }

    public MainPanel(String  dev, boolean stda, boolean keepStateRef, boolean modifProp)
    {       
 	this();
        this.standAlone = stda;
	this.keepStateRefresher = keepStateRef;
	this.modifPropButton = modifProp;
	if (connectDevice(dev) == false)
	{
	   splash.setVisible(false);
	   return;		
        }
        initComponents();
	startUp();
    }

    public MainPanel(String  dev, boolean stda, boolean keepStateRef,
                                  boolean modifProp, boolean ro)
    {       
	this();
        this.standAlone = stda;
	this.keepStateRefresher = keepStateRef;
	this.modifPropButton = modifProp;
	this.roMode = ro;
	if (ro == true)
	   this.modifPropButton = false;
	if (connectDevice(dev) == false)
	{
	   splash.setVisible(false);
	   return;		
        }
        initComponents();
	startUp();
    }

    public MainPanel(String  dev, String tab, boolean stda, boolean keepStateRef,
                                  boolean modifProp, boolean ro)
    {
	this();
        this.startupTabName = tab;
        this.standAlone = stda;
	this.keepStateRefresher = keepStateRef;
	this.modifPropButton = modifProp;
	this.roMode = ro;
	if (ro == true)
	   this.modifPropButton = false;
	if (connectDevice(dev) == false)
	{
	   splash.setVisible(false);
	   return;
        }
        initComponents();
	startUp();
    }

    // This constructor is used in TangoSynopticHandler class of tangoatk
    public MainPanel(String  dev, Boolean stda, Boolean keepStateRef,
                                  Boolean modifProp, Boolean ro)
    {       
	this();
        this.standAlone = stda.booleanValue();
	this.keepStateRefresher = keepStateRef.booleanValue();
	this.modifPropButton = modifProp.booleanValue();
	this.roMode = ro.booleanValue();
	if (ro.booleanValue() == true)
	   this.modifPropButton = false;
	if (connectDevice(dev) == false)
	{
	   splash.setVisible(false);
	   return;		
        }
        initComponents();
	startUp();
    }
 
    
    private void createDevTestPanel(String devName) throws ClassNotFoundException, NoSuchMethodException,
                 InstantiationException, IllegalAccessException, IllegalArgumentException,
                 InvocationTargetException, DevFailed
    {
        Class jiveExecDevClass = Class.forName("jive.ExecDev");

        //Find the appropriate constructor
        Class[] paramCls = new Class[1];
        paramCls[0] = devName.getClass();
        Constructor devTestPanelNew = jiveExecDevClass.getConstructor(paramCls);

        //Instantiate the panel for devName
        Object[] params = new Object[1];
        params[0] = devName;
        Object obj = devTestPanelNew.newInstance(params);

        if (obj != null)
        {
            if (obj instanceof JPanel)
            {
                JPanel devTestPanel = (JPanel) obj;
                tgDevtestDlg.setContentPane(devTestPanel);
                return;
            }
        }
        
        tgDevtestDlg = null;
    }
    
    
   /** Creates new form AtkPanel */
    private boolean connectDevice(String  devName)
    {
        String        versionText, versNumber;
        int           colon_idx, dollar_idx;
        
        versionText = new String(REVISION);
        colon_idx = versionText.lastIndexOf(":");
        dollar_idx = versionText.length();
        versNumber = versionText.substring(colon_idx+1, dollar_idx);
        
        
	splash.setTitle("AtkPanel  "+ versNumber);
	splash.setCopyright("(c) ESRF 2002-2014");
	splash.setMessage("Waiting for device-name...");
	splash.initProgress();
        splash.setMaxProgress(12);
	
	if (devName == null)
	{
	    splash.toBack(); // To workaround the option pane going behind Splash (linux essentially)
	    devName = javax.swing.JOptionPane.showInputDialog (null, "Please enter the device name");
	    splash.toFront();
	} 
		
	// Keeps all scalar attributes operator or expert
        all_scalar_atts = new fr.esrf.tangoatk.core.AttributeList();
        all_scalar_atts.setFilter( new IEntityFilter () 
                         {
                            public boolean keep(IEntity entity)
			    {
                               if (    (entity instanceof IScalarAttribute)
			            || (entity instanceof IBooleanScalar)
			            || (entity instanceof IEnumScalar)
			            || (entity instanceof IDevStateScalar) )
			       {
                                  if (    (entity.getNameSansDevice().equalsIgnoreCase("Status"))
				       || (entity.getNameSansDevice().equalsIgnoreCase("State")) )
				     return false;
				  else
				     return true;
                               }
                               return false;
                            }
                         });
	
	
	// Keeps only operator scalar attributes
        op_scalar_atts = new fr.esrf.tangoatk.core.AttributeList();
        op_scalar_atts.setFilter( new IEntityFilter () 
                         {
                            public boolean keep(IEntity entity)
			    {
                               if (    (entity instanceof IScalarAttribute)
			            || (entity instanceof IBooleanScalar)
			            || (entity instanceof IEnumScalar)
			            || (entity instanceof IDevStateScalar) )
			       {
                                  if (    (entity.getNameSansDevice().equalsIgnoreCase("Status"))
				       || (entity.getNameSansDevice().equalsIgnoreCase("State")) )
				     return false;
				  if (entity.isOperator() == true)
				  {
				     String message = "Adding Operator scalar attributes(";
				     splash.setMessage(message + entity.getNameSansDevice() + ")...");
                                     return true;
				  }
                               }
                               return false;
                            }
                         });
	
	
	// Keeps only expert scalar attributes
        exp_scalar_atts = new fr.esrf.tangoatk.core.AttributeList();
        exp_scalar_atts.setFilter( new IEntityFilter () 
                         {
                            public boolean keep(IEntity entity)
			    {
                               if (    (entity instanceof IScalarAttribute)
			            || (entity instanceof IBooleanScalar)
			            || (entity instanceof IEnumScalar)
			            || (entity instanceof IDevStateScalar) )
			       {
                                  if (    (entity.getNameSansDevice().equalsIgnoreCase("Status"))
				       || (entity.getNameSansDevice().equalsIgnoreCase("State")) )
				     return false;
				  if (entity.isOperator() == false)
				  {
				     String message = "Adding Expert scalar attributes(";
				     splash.setMessage(message + entity.getNameSansDevice() + ")...");
                                     return true;
				  }
                               }
                               return false;
                            }
                         });
	
        state_status_atts = new fr.esrf.tangoatk.core.AttributeList();
	
	// Keep only number scalar attributes operator or expert (used in global Trend)
        number_scalar_atts = new fr.esrf.tangoatk.core.AttributePolledList();
        number_scalar_atts.setFilter( new IEntityFilter () 
                         {
                            public boolean keep(IEntity entity)
			    {
                               if (entity instanceof INumberScalar)
			       {
				  String message = "Adding number scalar attributes(";
				  splash.setMessage(message + entity.getNameSansDevice() + ")...");
                                  return true;
                               }
                               return false;
                            }
                         });
	
	// Keep only number scalar attributes operator or expert (used in boolean Trend)
        boolean_scalar_atts = new fr.esrf.tangoatk.core.AttributePolledList();
        boolean_scalar_atts.setFilter( new IEntityFilter () 
                         {
                            public boolean keep(IEntity entity)
			    {
                               if (entity instanceof IBooleanScalar)
			       {
				  String message = "Adding boolean scalar attributes(";
				  splash.setMessage(message + entity.getNameSansDevice() + ")...");
                                  return true;
                               }
                               return false;
                            }
                         });

	
	// Keep all number spectrum attributes operator or expert
        all_spectrum_atts = new fr.esrf.tangoatk.core.AttributeList();
        all_spectrum_atts.setFilter( new IEntityFilter () 
                         {
                            public boolean keep(IEntity entity)
			    {
                               if (    (entity instanceof INumberSpectrum)
                                    || (entity instanceof IStringSpectrum)
                                    || (entity instanceof IDevStateSpectrum)
                                    || (entity instanceof IBooleanSpectrum)  )
                               
			       {
				     String message = "Adding to all_spectrum_atts : ";
				     splash.setMessage(message + entity.getNameSansDevice() );
                                     return true;
                               }
                               return false;
                            }
                         });

	
	// Keep only number operator spectrum attributes
        op_spectrum_atts = new fr.esrf.tangoatk.core.AttributeList();
        op_spectrum_atts.setFilter( new IEntityFilter () 
                         {
                            public boolean keep(IEntity entity)
			    {
                               if (    (entity instanceof INumberSpectrum)
                                    || (entity instanceof IStringSpectrum)
                                    || (entity instanceof IDevStateSpectrum)
                                    || (entity instanceof IBooleanSpectrum)  )
                               
			       {
				     if (entity.isOperator())
				     {
				        String message = "Adding to op_spectrum_atts : ";
					splash.setMessage(message + entity.getNameSansDevice() + ")...");
                                	return true;
				     }
                               }
                               return false;
                            }
                         });
                         
	
	
        all_number_image_atts = new fr.esrf.tangoatk.core.AttributeList();
	// Keep only number image attributes
        all_number_image_atts.setFilter( new IEntityFilter () 
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
	
	
        op_number_image_atts = new fr.esrf.tangoatk.core.AttributeList();
	// Keep only number image attributes
        op_number_image_atts.setFilter( new IEntityFilter () 
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
				     if (entity.isOperator())
				     {
					String message = "Adding image attributes(";
					splash.setMessage(message + entity.getNameSansDevice() + ")...");
                                	return true;
				     }
				  }
                               }
                               return false;
                            }
                         });
                         
	
	
        all_string_image_atts = new fr.esrf.tangoatk.core.AttributeList();
	// Keep only string image attributes
        all_string_image_atts.setFilter( new IEntityFilter () 
                         {
                            public boolean keep(IEntity entity)
			    {
                               if (entity instanceof IStringImage)
			       {
				     String message = "Adding String Image attributes(";
				     splash.setMessage(message + entity.getNameSansDevice() + ")...");
                                     return true;
                               }
                               return false;
                            }
                         });
	
	
        op_string_image_atts = new fr.esrf.tangoatk.core.AttributeList();
	// Keep only string image attributes
        op_string_image_atts.setFilter( new IEntityFilter () 
                         {
                            public boolean keep(IEntity entity)
			    {
                               if (entity instanceof IStringImage)
			       {
				     if (entity.isOperator())
				     {
					String message = "Adding String Image attributes(";
					splash.setMessage(message + entity.getNameSansDevice() + ")...");
                                	return true;
				     }
                               }
                               return false;
                            }
                         });
                         
	
	
        all_raw_image_atts = new fr.esrf.tangoatk.core.AttributeList();
	// Keep only Raw image attributes
        all_raw_image_atts.setFilter( new IEntityFilter () 
                         {
                            public boolean keep(IEntity entity)
			    {
                               if (entity instanceof IRawImage)
			       {
				     String message = "Adding Raw Image attributes(";
				     splash.setMessage(message + entity.getNameSansDevice() + ")...");
                                     return true;
                               }
                               return false;
                            }
                         });
	
	
        op_raw_image_atts = new fr.esrf.tangoatk.core.AttributeList();
	// Keep only string image attributes
        op_raw_image_atts.setFilter( new IEntityFilter () 
                         {
                            public boolean keep(IEntity entity)
			    {
                               if (entity instanceof IRawImage)
			       {
				     if (entity.isOperator())
				     {
					String message = "Adding Raw Image attributes(";
					splash.setMessage(message + entity.getNameSansDevice() + ")...");
                                	return true;
				     }
                               }
                               return false;
                            }
                         });
                         

        all_cmdl = new CommandList();

        op_cmdl = new CommandList();
	// Keep only operator commands
        op_cmdl.setFilter( new IEntityFilter () 
                         {
                            public boolean keep(IEntity entity)
			    {
                               if (entity instanceof ICommand)
			       {
				  if (entity.isOperator())
				  {
				     String message = "Adding Operator command (";
				     splash.setMessage(message + entity.getNameSansDevice() + ")...");
                                     return true;
				  }
                               }
                               return false;
                            }
                         });


        panelDev = null;
	
        errorHistory = new fr.esrf.tangoatk.widget.util.ErrorHistory();
	errorPopup = ErrorPopup.getInstance(); // ErrorPopup is now a singleton from ATK..2.2.0 and higher
        all_scalar_atts.addErrorListener(errorHistory);
        all_scalar_atts.addSetErrorListener(errorPopup);
        all_scalar_atts.addSetErrorListener(errorHistory);
	
        state_status_atts.addErrorListener(errorHistory);
        
        all_spectrum_atts.addErrorListener(errorHistory);
        all_number_image_atts.addErrorListener(errorHistory);
        all_string_image_atts.addErrorListener(errorHistory);
        all_raw_image_atts.addErrorListener(errorHistory);
        

        all_cmdl.addErrorListener(errorHistory);
	all_cmdl.addErrorListener(errorPopup);
	
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
           //System.exit(-1);
	   abortAppli();
	   return false;
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
           //System.exit(-1);
	   abortAppli();
	   return false;
	}
	
	
        try
        {
	   String message = "Importing attributes from " + devName + "...";
           try
           {
	      splash.setMessage(message);
	      op_scalar_atts.add(devName+"/*");
	      exp_scalar_atts.add(devName+"/*");
              number_scalar_atts.add(devName+"/*");
              boolean_scalar_atts.add(devName+"/*");
	      splash.progress(4);
              all_spectrum_atts.add(devName+"/*");
              op_spectrum_atts.add(devName+"/*");
	      splash.progress(5);
	      all_number_image_atts.add(devName+"/*");
	      op_number_image_atts.add(devName+"/*");
	      splash.progress(6);
	      all_string_image_atts.add(devName+"/*");
	      op_string_image_atts.add(devName+"/*");
	      splash.progress(7);
	      all_raw_image_atts.add(devName+"/*");
	      op_raw_image_atts.add(devName+"/*");
	      splash.progress(7);
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
              //System.exit(-1);
	      abortAppli();
	      return false;
           }
	   message = "Importing commands from " + devName + "...";
	   all_cmdl.add(devName+"/*");
	   op_cmdl.add(devName+"/*");
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
           //System.exit(-1);
	   abortAppli();
	   return false;
        }
        
        if (roMode)
            return true;
	
	this.setTitle("AtkPanel "+versNumber+" : "+devName);
        tgDevtestDlg = new JDialog(this, false);
        tgDevtestDlg.setTitle("Test Device : "+devName);
                
        try
        {
            createDevTestPanel(devName);    
        } 
        catch (ClassNotFoundException ex)
        {
            tgDevtestDlg = null;
        } 
        catch (NoSuchMethodException ex)
        {
            tgDevtestDlg = null;
        } 
        catch (IllegalArgumentException ex)
        {
            tgDevtestDlg = null;
        } 
        catch (InvocationTargetException ex)
        {
            tgDevtestDlg = null;
        } 
        catch (InstantiationException ex)
        {
            tgDevtestDlg = null;
        } 
        catch (IllegalAccessException ex)
        {
            tgDevtestDlg = null;
        } 
        catch (DevFailed ex)
        {
            tgDevtestDlg = null;
        }
        
        return true;
   }
    

    private void startUp()
    {	
	if (tgDevtestDlg == null)
        {
            tgDevTestJMenuItem.setEnabled(false);
        }
	String message = "Initializing commands...";
	splash.setMessage(message);
		
	createAllCmdList();
	if (all_cmdl.getSize() > 0)
	{
	   if (operatorView == true) // display only operator level commands
              show_operator_commands();
	   else // display all commands (operator level + expert level)
              show_all_commands();
	}
	  
	if (roMode == true)
	{
	   commandComboViewer1.setVisible(false);
	}
	
	splash.progress(8);
	splash.setMessage(message + "done");
	message = "Initializing scalar attributes...";
	splash.setMessage(message);

	// Set scrolling 
       
        allScalarListViewer = null;
        operatorScalarListViewer = null;
	createAllScalarListViewers();
	
        globalTrend.setModel(number_scalar_atts);	
        booleanTrend.setModel(boolean_scalar_atts);
	
	
	if ( (all_scalar_atts.getSize() > 0) && (allScalarListViewer != null)
	                                      && (operatorScalarListViewer != null) )
	{
	   if (operatorView == true) // display only operator level scalar attributes
	   {
              RefresherUtil.skippingRefreshForExpertAttributes(all_scalar_atts);
              show_operator_scalars();
	      expertCheckBoxMenuItem.setState(false);
	      operatorCheckBoxMenuItem.setState(true);
	   }
	   else // display all scalar attributes (operator level + expert level)
	   {
              show_all_scalars();
	      expertCheckBoxMenuItem.setState(true);
	      operatorCheckBoxMenuItem.setState(false);	
	   }
	}
	
	
	// Adding state and status attributes
        try
        {
	   stateAtt = (IDevStateScalar) state_status_atts.add(panelDev.getName()+"/State");
	   statusAtt = (IStringScalar) state_status_atts.add(panelDev.getName()+"/Status");
        }
        catch (Exception   e)
        {
           stateAtt=null;
	   statusAtt=null;
        }
	
	if ((stateAtt == null) || (statusAtt == null))
	{
	   System.out.println("AtkPanel: Cannot get the State and / or Status attributes for the device.");
           System.out.println("AtkPanel: May be an old IDL 2 device server....");
	   devStateViewer.setVisible(true);
	   devStatusViewer.setVisible(true);
	   attStateViewer.setVisible(false);
	   attStatusViewer.setVisible(false);
	}
	else
	{
	   devStateViewer.setVisible(false);
	   devStatusViewer.setVisible(false);
	   
           if ((standAlone == true) || (keepStateRefresher == false))
              DeviceFactory.getInstance().stopRefresher();
	   
	   attStateViewer.setModel(stateAtt);
	   attStatusViewer.setModel(statusAtt);
	   attStateViewer.setVisible(true);
	   attStatusViewer.setVisible(true);
	   
	   //if (no events) for one of the state or status attributes then start refresher for state status atts
	   if ( (stateAtt.hasEvents() == false) || (statusAtt.hasEvents() == false) )
	   {
	      state_status_atts.startRefresher();
	   }
	}
	
	
	splash.progress(10);
	splash.setMessage(message + "done");
	message = "Initializing number spectrum attributes...";
	splash.setMessage(message);
        
		
        RefresherUtil.skippingRefreshForAllAttributes(all_spectrum_atts);
        createAllSpectrumTabs();
	if (all_spectrum_atts.getSize() > 0)
	{
	   if (operatorView == true) // display only operator level spectrum attributes 
              show_operator_spectrums();
	   else // display all spectrum attributes (operator level + expert level)
              show_all_spectrums();
	}

	splash.progress(11);
	splash.setMessage(message + "done");
	message = "Initializing number image attributes...";
	splash.setMessage(message);
        
		
        RefresherUtil.skippingRefreshForAllAttributes(all_number_image_atts);
        createAllNumberImageTabs();
	if (all_number_image_atts.getSize() > 0)
	{
	   if (operatorView == true) // display only operator level image attributes 
              show_operator_images();
	   else // display all image attributes (operator level + expert level)
              show_all_images();
	}
        
		
        RefresherUtil.skippingRefreshForAllAttributes(all_string_image_atts);
        createAllStringImageTabs();
	if (all_string_image_atts.getSize() > 0)
	{
	   if (operatorView == true) // display only operator level string image attributes 
              show_operator_string_images();
	   else // display all string image attributes (operator level + expert level)
              show_all_string_images();
	}
        
		
        RefresherUtil.skippingRefreshForAllAttributes(all_raw_image_atts);
        createAllRawImageTabs();
	if (all_raw_image_atts.getSize() > 0)
	{
	   if (operatorView == true) // display only operator level Raw image attributes 
              show_operator_raw_images();
	   else // display all Raw image attributes (operator level + expert level)
              show_all_raw_images();
	}
	
	if (    (all_scalar_atts.getSize() <= 0) && (all_spectrum_atts.getSize() <= 0)
	     && (all_number_image_atts.getSize() <= 0) && (all_string_image_atts.getSize() <= 0)
             && (all_raw_image_atts.getSize() <= 0) )
	{ // No attribute at all for this device
	   jTabbedPane1.setVisible(false);
	}

	startAllRefresher();


	   
	splash.progress(12);
	splash.setMessage(message + "done");
	splash.setMessage(message);

        showStartupTab();
	
	
	splash.setVisible(false);
	
   	pack();
        if (wpos == null)
            ATKGraphicsUtils.centerFrameOnScreen(this);
        else
            ATKGraphicsUtils.positionFrameOnScreen(this, wpos[0], wpos[1]);
        setVisible(true);

        //DeviceFactory.getInstance().setTraceMode(DeviceFactory.TRACE_REFRESHER);
 }

    private void showStartupTab()
    {
        if (startupTabName == null) return;

        startupTabIndex = -1;
        int  nbTabs = jTabbedPane1.getTabCount();
        for (int i=0; i<nbTabs; i++)
        {
            if (jTabbedPane1.getTitleAt(i).equalsIgnoreCase(startupTabName))
            {
                startupTabIndex = i;
                break;
            }
        }

        if (startupTabIndex > 0)
        {
            try
            {
                jTabbedPane1.setSelectedIndex(startupTabIndex);
                //setSelectedTabbedPaneIndex(startupTabIndex);
            }
            catch (IndexOutOfBoundsException iobex)
            {
                return;
            }
        }
    }

        
    
    
    /** This method is called from within the constructor to
      * initialize the form.
      * WARNING: Do NOT modify this code. The content of this method is
      * always regenerated by the Form Editor.
      */
    private void initComponents()
    {//GEN-BEGIN:initComponents
	java.awt.GridBagConstraints gridBagConstraints1;
	
	addWindowListener(
	     new java.awt.event.WindowAdapter()
		 {
		    public void windowClosing(java.awt.event.WindowEvent evt)
		    {
		       exitForm(evt);
		    }
		 });

    
	jMenuBar1 = new javax.swing.JMenuBar();
	jMenu1 = new javax.swing.JMenu();
	jMenuItem1 = new javax.swing.JMenuItem();

	jMenu3 = new javax.swing.JMenu();
	jMenuItem5 = new javax.swing.JMenuItem();
	boolTrendJMenuItem = new javax.swing.JMenuItem();
	jMenuItem3 = new javax.swing.JMenuItem();
	diagJMenuItem = new javax.swing.JMenuItem();
        tgDevTestJMenuItem = new javax.swing.JMenuItem();

	jMenu2 = new javax.swing.JMenu();
	jMenuItem6 = new javax.swing.JMenuItem();
	jMenuItem7 = new javax.swing.JMenuItem();
	setRefPeriodJMenuItem = new javax.swing.JMenuItem();
	timeoutJMenuItem = new javax.swing.JMenuItem();
	expertCheckBoxMenuItem = new javax.swing.JCheckBoxMenuItem();
	operatorCheckBoxMenuItem = new javax.swing.JCheckBoxMenuItem();

	jMenu4 = new javax.swing.JMenu();
	jMenuItem4 = new javax.swing.JMenuItem();

	mainJSplitPane = new javax.swing.JSplitPane();
	mainJSplitPane.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);
	mainJSplitPane.setOneTouchExpandable(true);

	stateAndCmdJPanel = new javax.swing.JPanel();
	stateAndCmdJPanel.setLayout(new java.awt.GridBagLayout());

	topJPanel = new javax.swing.JPanel();
	topJPanel.setLayout(new java.awt.GridBagLayout());
	
	scrollJPanel = new javax.swing.JPanel();
	scrollJPanel.setLayout(new java.awt.GridBagLayout());
	
	commandComboViewer1 = new fr.esrf.tangoatk.widget.command.CommandComboViewer();
	devStateViewer = new fr.esrf.tangoatk.widget.device.StateViewer();
	devStateViewer.setStateInTooltip(true);
	devStatusViewer = new fr.esrf.tangoatk.widget.device.StatusViewer();
	attStateViewer = new fr.esrf.tangoatk.widget.attribute.StateViewer();
	attStateViewer.setStateInTooltip(true);
	attStatusViewer = new fr.esrf.tangoatk.widget.attribute.StatusViewer();

	jTabbedPane1 = new javax.swing.JTabbedPane();
	jTabbedPane1.addChangeListener(new ChangeListener()
	{
		public void stateChanged(ChangeEvent e)
		{ 
		    int iSelectedIndex = jTabbedPane1.getSelectedIndex();
		    setSelectedTabbedPaneIndex(iSelectedIndex);
		}
		
	}
	);

	jScrollPane1 = new javax.swing.JScrollPane();

	devStateViewer.setStateClickable(false);
	devStatusViewer.setPreferredSize(new java.awt.Dimension(50, 100));
	attStatusViewer.setPreferredSize(new java.awt.Dimension(50, 100));

	// File pulldown menu
	jMenu1.setText("File");
	jMenuItem1.setText("Quit");
	jMenuItem1.addActionListener(
	     new java.awt.event.ActionListener()
		 {
		    public void actionPerformed(java.awt.event.ActionEvent evt)
		    {
		       jMenuItem1ActionPerformed(evt);
		    }
		 });
	jMenu1.add(jMenuItem1);
	jMenuBar1.add(jMenu1);


	// View pulldown menu
	jMenu3.setText("View");

	tgDevTestJMenuItem.setText("Test Device");
	tgDevTestJMenuItem.addActionListener(
	     new java.awt.event.ActionListener()
	         {
		    public void actionPerformed(java.awt.event.ActionEvent evt)
		    {
		       testDeviceActionPerformed(evt);
		    }
		 });
	jMenu3.add(tgDevTestJMenuItem);


	jMenuItem5.setText("Numeric Trend ");
	jMenuItem5.addActionListener(
	     new java.awt.event.ActionListener()
	         {
		    public void actionPerformed(java.awt.event.ActionEvent evt)
		    {
		       viewTrendActionPerformed(evt);
		    }
		 });
	jMenu3.add(jMenuItem5);

	boolTrendJMenuItem.setText("Boolean Trend ");
	boolTrendJMenuItem.addActionListener(
	     new java.awt.event.ActionListener()
	         {
		    public void actionPerformed(java.awt.event.ActionEvent evt)
		    {
		       viewBooleanTrendActionPerformed(evt);
		    }
		 });
	jMenu3.add(boolTrendJMenuItem);

	jMenuItem3.setText("Error History");
	jMenuItem3.addActionListener(
	     new java.awt.event.ActionListener()
		 {
		    public void actionPerformed(java.awt.event.ActionEvent evt)
		    {
		       viewErrHistoryActionPerformed(evt);
		    }
		 });
	jMenu3.add(jMenuItem3);

	diagJMenuItem.setText("Diagnostic ...");
	diagJMenuItem.addActionListener(
	     new java.awt.event.ActionListener()
		 {
		    public void actionPerformed(java.awt.event.ActionEvent evt)
		    {
		       fr.esrf.tangoatk.widget.util.ATKDiagnostic.showDiagnostic();
		    }
		 });
	jMenu3.add(diagJMenuItem);

	jMenuBar1.add(jMenu3);


	// View pulldown menu
	jMenu2.setText("Preferences");
	jMenuItem6.setText("Stop   Refreshing");
	jMenuItem6.addActionListener(
	     new java.awt.event.ActionListener()
		 {
		    public void actionPerformed(java.awt.event.ActionEvent evt)
		    {
		       stopStartRefreshActionPerformed(evt);
		    }
		 });
	jMenu2.add(jMenuItem6);


	jMenuItem7.setText("Refresh  once");
	jMenuItem7.setEnabled(false);
	jMenuItem7.addActionListener(
	     new java.awt.event.ActionListener()
		 {
		    public void actionPerformed(java.awt.event.ActionEvent evt)
		    {
		       refreshOnceActionPerformed(evt);
		    }
		 });
	jMenu2.add(jMenuItem7);

	setRefPeriodJMenuItem.setText("Set refreshing period ...");
	setRefPeriodJMenuItem.addActionListener(
	     new java.awt.event.ActionListener()
		 {
		    public void actionPerformed(java.awt.event.ActionEvent evt)
		    {
		       refPeriodActionPerformed(evt);
		    }
		 });
	jMenu2.add(setRefPeriodJMenuItem);

	timeoutJMenuItem.setText("Set device timeout ...");
	timeoutJMenuItem.addActionListener(
	     new java.awt.event.ActionListener()
		 {
		    public void actionPerformed(java.awt.event.ActionEvent evt)
		    {
		       setTimeoutActionPerformed(evt);
		    }
		 });
	jMenu2.add(timeoutJMenuItem);

	javax.swing.JSeparator  jsep = new javax.swing.JSeparator();
	jMenu2.add(jsep);

	operatorCheckBoxMenuItem.setSelected(true);
	operatorCheckBoxMenuItem.setText("Operator  View");
	operatorCheckBoxMenuItem.addActionListener(
	     new java.awt.event.ActionListener()
		 {
		    public void actionPerformed(java.awt.event.ActionEvent evt)
		    {
		       operatorCheckBoxMenuItemActionPerformed(evt);
		    }
		 });
	jMenu2.add(operatorCheckBoxMenuItem);

	expertCheckBoxMenuItem.setSelected(false);
	expertCheckBoxMenuItem.setText("Expert  View");
	expertCheckBoxMenuItem.addActionListener(
	     new java.awt.event.ActionListener()
		 {
		    public void actionPerformed(java.awt.event.ActionEvent evt)
		    {
			expertCheckBoxMenuItemActionPerformed(evt);
		    }
		 });
	jMenu2.add(expertCheckBoxMenuItem);


	jMenuBar1.add(jMenu2);



	// Help pulldown menu
	jMenu4.setText("Help");
	jMenuItem4.setText("On Version ...");
	jMenuItem4.addActionListener(
	     new java.awt.event.ActionListener()
		 {
		    public void actionPerformed(java.awt.event.ActionEvent evt)
		    {
		       helpVersionActionPerformed(evt);
		    }
		 });

	jMenu4.add(jMenuItem4);
	jMenuBar1.add(jMenu4);


	gridBagConstraints1 = new java.awt.GridBagConstraints();

	gridBagConstraints1.gridx = 0;
	gridBagConstraints1.gridy = 0;
	gridBagConstraints1.insets = new java.awt.Insets(5, 5, 5, 5);
	stateAndCmdJPanel.add(attStateViewer, gridBagConstraints1);
	devStateViewer.setModel(panelDev);
	stateAndCmdJPanel.add(devStateViewer, gridBagConstraints1);

	gridBagConstraints1.gridx = 1;
	gridBagConstraints1.gridy = 0;
	gridBagConstraints1.insets = new java.awt.Insets(5, 5, 5, 5);
	stateAndCmdJPanel.add(commandComboViewer1, gridBagConstraints1);


	gridBagConstraints1.gridx = 0;
	gridBagConstraints1.gridy = 0;
	gridBagConstraints1.insets = new java.awt.Insets(5, 5, 5, 5);
	gridBagConstraints1.fill = java.awt.GridBagConstraints.BOTH;
	gridBagConstraints1.weightx = 1.0;
	topJPanel.add(stateAndCmdJPanel, gridBagConstraints1);

	gridBagConstraints1.gridx = 0;
	gridBagConstraints1.gridy = 1;
	gridBagConstraints1.insets = new java.awt.Insets(5, 5, 5, 5);
	gridBagConstraints1.fill = java.awt.GridBagConstraints.BOTH;
	gridBagConstraints1.weightx = 1.0;
	gridBagConstraints1.weighty = 1.0;
	topJPanel.add(attStatusViewer, gridBagConstraints1);
	devStatusViewer.setModel(panelDev);
	topJPanel.add(devStatusViewer, gridBagConstraints1);

	mainJSplitPane.setTopComponent(topJPanel);

	jTabbedPane1.setTabPlacement(javax.swing.JTabbedPane.BOTTOM);

	jScrollPane1.setViewportView(scrollJPanel);

	jTabbedPane1.addTab("Scalar", jScrollPane1);
	mainJSplitPane.setBottomComponent(jTabbedPane1);

	getContentPane().add(mainJSplitPane, java.awt.BorderLayout.CENTER);

	setJMenuBar(jMenuBar1);
	pack();
    }//GEN-END:initComponents
    
    private void setSelectedTabbedPaneIndex(int  tabIndex)
    {
	// skip refreshing for old focus component
	//System.out.println("Previous Index : " + iTabbedPaneIndex);
        if (jTabbedPane1.getTitleAt(iTabbedPaneIndex).equalsIgnoreCase("Scalar"))
        {
            all_scalar_atts.stopRefresher();
        }
        else
        {
            Component comp = jTabbedPane1.getComponent(iTabbedPaneIndex);
            RefresherUtil.skippingRefresh(comp);
        }


	// disable skipping refreshing for the new focus component
	//System.out.println("Selected Index : " + tabIndex);
        if (jTabbedPane1.getTitleAt(jTabbedPane1.getSelectedIndex()).equalsIgnoreCase("Scalar"))
        {
            all_scalar_atts.startRefresher();
        }
        else
        {
            Component comp = jTabbedPane1.getSelectedComponent();
            RefresherUtil.activateRefresh(comp);
            RefresherUtil.refresh(comp);
        }
	iTabbedPaneIndex = tabIndex;
    }

    private void refreshOnceActionPerformed(java.awt.event.ActionEvent evt)
    {//GEN-FIRST:event_refreshOnceActionPerformed
        // Add your handling code here:
	all_scalar_atts.refresh();
	all_spectrum_atts.refresh();
	all_number_image_atts.refresh();
	all_string_image_atts.refresh();
        all_raw_image_atts.refresh();
	
	if (devStateViewer.isVisible() || devStatusViewer.isVisible())
	{
           DeviceFactory.getInstance().refresh();
	}
	else
	{
	   if ((stateAtt != null) && (statusAtt != null))
	   {
		 state_status_atts.refresh();
	   }
	}
    }//GEN-LAST:event_refreshOnceActionPerformed

    private void stopStartRefreshActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_stopStartRefreshActionPerformed
        // Add your handling code here:

	javax.swing.JMenuItem   menuButton;
	
	menuButton = (javax.swing.JMenuItem) evt.getSource();
	if (menuButton.getText().equalsIgnoreCase("Stop   Refreshing"))
	{
	   refresherActivated = false;
	   menuButton.setText("Start   Refreshing");
	   jMenuItem7.setEnabled(true);
	   stopAllRefresher();
	}
	else
	{
	   refresherActivated = true;
	   menuButton.setText("Stop   Refreshing");
           jMenuItem7.setEnabled(false);
	   startAllRefresher();
	   	
	}
    }//GEN-LAST:event_stopStartRefreshActionPerformed
    
    /**
     * Stop all refresher
     *
     */
     private void stopAllRefresher()
     {
	all_scalar_atts.stopRefresher();
	all_spectrum_atts.stopRefresher();
	all_number_image_atts.stopRefresher();
	all_string_image_atts.stopRefresher();
        all_raw_image_atts.stopRefresher();
	number_scalar_atts.stopRefresher(); /* AttributePolledList for trend */
	boolean_scalar_atts.stopRefresher(); /* AttributePolledList for booleanTrend */
        

	if (keepStateRefresher == false)
	{
	   DeviceFactory.getInstance().stopRefresher();
	}

	if ((stateAtt != null) && (statusAtt != null))
	{
	      state_status_atts.stopRefresher();
	}
	// activate all component to accept refresh
	RefresherUtil.activateRefreshForAllComponent(jTabbedPane1);
	// we call the refresh once method to refresh all attributes before stop all refresh
	refreshOnceActionPerformed(null);
     } 

     /**
      * Start All refresher
      *
      */
     private void startAllRefresher()
     {
	   // all component to refuse refresh
	   RefresherUtil.skippingRefreshForAllComponent(jTabbedPane1);
	   // activate only the selected component of JTabbedPane to accept refresh
           if (jTabbedPane1.getTitleAt(jTabbedPane1.getSelectedIndex()).equalsIgnoreCase("Scalar"))
           {
                all_scalar_atts.startRefresher();
           }
           else
           {
                Component comp = jTabbedPane1.getSelectedComponent();
                RefresherUtil.activateRefresh(comp);
                RefresherUtil.refresh(comp);
           }

           //all_scalar_atts.startRefresher();
	   all_spectrum_atts.startRefresher();
	   all_number_image_atts.startRefresher();
	   all_string_image_atts.startRefresher();
           all_raw_image_atts.startRefresher();
	   //number_scalar_atts.startRefresher(); /* AttributePolledList for trend is started by clicking the play button in the trend window */
	   //boolean_scalar_atts.startRefresher(); /* AttributePolledList for booleanTrend is started by clicking the play button in the trend window */

	   if (devStateViewer.isVisible() || devStatusViewer.isVisible())
	   {
	      DeviceFactory.getInstance().startRefresher();
	   }
	   else
	   {
	      if ((stateAtt != null) && (statusAtt != null))
	      {
		 if ( (stateAtt.hasEvents() == false) || (statusAtt.hasEvents() == false) )
		 {
		    state_status_atts.startRefresher();
		 }
	      }
	   }
    }

    private void helpVersionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_helpVersionActionPerformed
        // Add your handling code here:
        String    versionText, versNumber;
        int       colon_idx, dollar_idx;
        
        versionText = new String(REVISION);
        colon_idx = versionText.lastIndexOf(":");
        dollar_idx = versionText.length();
        versNumber = versionText.substring(colon_idx+1, dollar_idx);
        try
        {
            javax.swing.JOptionPane.showMessageDialog(this, 
	    "\n\n   atkpanel   : "+versNumber
	   +"\n\n   ESRF  :   Accelerator Control Unit \n\n",
            "atkpanel   Version", javax.swing.JOptionPane.PLAIN_MESSAGE );
        }
        catch (Exception e)
        {
        }
        
    }//GEN-LAST:event_helpVersionActionPerformed

    private void testDeviceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_testDeviceActionPerformed
        // Add your handling code here:
        if (tgDevtestDlg == null)
        {
            tgDevTestJMenuItem.setEnabled(false);
            return;
        }

        ATKGraphicsUtils.centerDialog(tgDevtestDlg);
        
        tgDevtestDlg.setVisible(true);
    }//GEN-LAST:event_viewTrendActionPerformed

    private void viewTrendActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_viewTrendActionPerformed
        // Add your handling code here:
	fr.esrf.tangoatk.widget.util.ATKGraphicsUtils.centerFrame(mainJSplitPane, trendFrame);
        trendFrame.setVisible(true);
    }//GEN-LAST:event_viewTrendActionPerformed

    private void viewBooleanTrendActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_viewTrendActionPerformed
        // Add your handling code here:
	fr.esrf.tangoatk.widget.util.ATKGraphicsUtils.centerFrame(mainJSplitPane, booleanTrendFrame);
        booleanTrendFrame.setVisible(true);
    }//GEN-LAST:event_viewTrendActionPerformed

    private void refPeriodActionPerformed(java.awt.event.ActionEvent evt)
    {//GEN-FIRST:event_refPeriodActionPerformed
        // Add your handling code here:
	
	int  ref_period = -1;
	ref_period = all_scalar_atts.getRefreshInterval();
	if (ref_period == -1)
	   ref_period = all_spectrum_atts.getRefreshInterval();
	if (ref_period == -1)
	   ref_period = all_number_image_atts.getRefreshInterval();
	if (ref_period == -1)
	   ref_period = all_string_image_atts.getRefreshInterval();
	if (ref_period == -1)
	   ref_period = all_raw_image_atts.getRefreshInterval();
//	System.out.println("Initial ref period = "+(Integer.toString(ref_period)));

        String refp_str = JOptionPane.showInputDialog(this,"Enter refresh interval (ms)",(Object) new Integer(ref_period));
	if ( refp_str != null )
	{
	    try
	    {
        	int it = Integer.parseInt(refp_str);
//		System.out.println("Set ref period to : "+it);

		all_scalar_atts.setRefreshInterval(it);
		all_spectrum_atts.setRefreshInterval(it);
		all_number_image_atts.setRefreshInterval(it);
		all_string_image_atts.setRefreshInterval(it);
                all_raw_image_atts.setRefreshInterval(it);
	
		if ((devStateViewer.isVisible()==false) && (devStatusViewer.isVisible()==false))
		{
		   if ((stateAtt != null) && (statusAtt != null))
		   {
		      if ( (stateAtt.hasEvents() == false) || (statusAtt.hasEvents() == false) )
		      {
			 state_status_atts.setRefreshInterval(it);
		      }
		   }
		}
	    }
	    catch ( NumberFormatException e )
	    {
        	JOptionPane.showMessageDialog(this,"Invalid number !","Error",JOptionPane.ERROR_MESSAGE);
		return;
	    }
	}
	
    }//GEN-LAST:event_refPeriodActionPerformed
    
    

    private void setTimeoutActionPerformed(java.awt.event.ActionEvent evt)
    {//setTimeoutActionPerformed
        // Add your handling code here:
	
	int  devTmout = -1;
	
	if (panelDev == null)
	   return;
	   
	try
	{
	   devTmout = panelDev.getDevTimeout();
	}
	catch (Exception ex)
	{
	   javax.swing.JOptionPane.showMessageDialog(
	      null, "Cannot  get the timeout for the device.\n"
	           + "Check the device still repsonding.\n\n"
		   + "Connection Exception : " + ex
		   ,"getDevTimeout failed"
		   ,javax.swing.JOptionPane.ERROR_MESSAGE);
	   return;
	}
	

        String tmout_str = JOptionPane.showInputDialog(this,"Enter timeout for device (ms)",(Object) new Integer(devTmout));

	if ( tmout_str != null )
	{
	    try
	    {
        	int it = Integer.parseInt(tmout_str);
		//System.out.println("Set timeout to : "+it);
                panelDev.setDevTimeout(it);
	    }
	    catch ( NumberFormatException e )
	    {
        	JOptionPane.showMessageDialog(this,"Cannot set timeout. Invalid number !","Error",JOptionPane.ERROR_MESSAGE);
		return;
	    }
	    catch (Exception ex)
	    {
	       javax.swing.JOptionPane.showMessageDialog(
		  null, "Cannot  set the timeout for the device.\n"
	               + "Check the device still repsonding.\n\n"
		       + "Connection Exception : " + ex
		       ,"setDevTimeout failed"
		       ,javax.swing.JOptionPane.ERROR_MESSAGE);
	       return;
	    }
	}
	
    }//setTimeoutActionPerformed


    private void viewErrHistoryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_viewErrHistoryActionPerformed
        // Add your handling code here:
	errorHistory.setVisible(true);
    }//GEN-LAST:event_viewErrHistoryActionPerformed


    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        // Add your handling code here:
        stopAtkPanel();
    }//GEN-LAST:event_jMenuItem1ActionPerformed


    
    private void expertCheckBoxMenuItemActionPerformed(java.awt.event.ActionEvent evt)
    {
	
	if (all_cmdl.getSize() > 0)
           show_all_commands();
	
	if (all_number_image_atts.getSize() > 0)
           show_all_images();
	
	if (all_string_image_atts.getSize() > 0)
           show_all_string_images();

	if (all_raw_image_atts.getSize() > 0)
           show_all_raw_images();

	if (all_spectrum_atts.getSize() > 0)
           show_all_spectrums();

        if (all_scalar_atts.getSize() > 0)
        {
           RefresherUtil.activateRefreshForExpertAttributes(all_scalar_atts);
           show_all_scalars();
        }
	   
	expertCheckBoxMenuItem.setState(true);
	operatorCheckBoxMenuItem.setState(false);	
    }

    
    private void operatorCheckBoxMenuItemActionPerformed(java.awt.event.ActionEvent evt)
    {
	
	if (all_cmdl.getSize() > 0)
           show_operator_commands();
	
	if (all_number_image_atts.getSize() > 0)
           show_operator_images();
	
	if (all_string_image_atts.getSize() > 0)
           show_operator_string_images();
	
	if (all_raw_image_atts.getSize() > 0)
           show_operator_raw_images();

	if (all_spectrum_atts.getSize() > 0)
           show_operator_spectrums();

        if (all_scalar_atts.getSize() > 0)
        {
	   RefresherUtil.skippingRefreshForExpertAttributes(all_scalar_atts);
           show_operator_scalars();
        }
	   
	expertCheckBoxMenuItem.setState(false);
	operatorCheckBoxMenuItem.setState(true);
    }
    
    private void clearAllModels()
    {
        /* ======= Device viewers if (state attribute does not exist) IDL 2 or taco device ========== */
	devStatusViewer.setModel(null);
	devStateViewer.clearModel();
        
        /* ========= State and Status attributes ============= */
        state_status_atts.removeErrorListener(errorHistory);	
        if ((stateAtt != null) && (statusAtt != null))
	{
            attStateViewer.setModel(null);
            attStatusViewer.setModel(null);
        }
        
        /* ========= Scalar attributes ============ */
        if (globalTrend != null)
           globalTrend.clearModel();

        if (booleanTrend != null)
           booleanTrend.clearModel();
        
        if (allScalarListViewer != null)
           allScalarListViewer.setModel(null);
        if (operatorScalarListViewer != null)
           operatorScalarListViewer.setModel(null);
        all_scalar_atts.removeErrorListener(errorHistory);
        all_scalar_atts.removeSetErrorListener(errorHistory);
	all_scalar_atts.removeSetErrorListener(errorPopup);
        
        /* ========= NumberSpectrum, StringSpectrum, DevStateSpectrum and BooleanSpectrum attributes ============ */
        Iterator<JComponent>  spIt = all_spectrum_panels.iterator();
        while (spIt.hasNext())
        {
            JComponent  sPanel = spIt.next();
            if (sPanel instanceof SpectrumPanel)
            {
                SpectrumPanel  nsPanel = (SpectrumPanel) sPanel;
                nsPanel.clearModel();
            }
            else
               if (sPanel instanceof StringSpectrumPanel)
               {
                   StringSpectrumPanel  ssPanel = (StringSpectrumPanel) sPanel;
                   ssPanel.clearModel();
               }
               else
                  if (sPanel instanceof DevStateSpectrumPanel)
                  {
                      DevStateSpectrumPanel  dsPanel = (DevStateSpectrumPanel) sPanel;
                      dsPanel.clearModel();
                  }
                  else
                     if (sPanel instanceof BooleanSpectrumPanel)
                     {
                         BooleanSpectrumPanel  bsPanel = (BooleanSpectrumPanel) sPanel;
                         bsPanel.clearModel();
                     }
        }
        all_spectrum_atts.removeErrorListener(errorHistory);
	
        
        /* ========= NumberImage attributes ============ */
        Iterator<ImagePanel>  nimIt = all_image_panels.iterator();
        while (nimIt.hasNext())
        {
            ImagePanel  nimPanel = nimIt.next();
            if (nimPanel != null)
                nimPanel.clearModel();
        }
        all_number_image_atts.removeErrorListener(errorHistory);
	
        
        /* ========= StringImage attributes ============ */
        Iterator<StringImagePanel>  simIt = all_string_image_panels.iterator();
        while (simIt.hasNext())
        {
            StringImagePanel  simPanel = simIt.next();
            if (simPanel != null)
                simPanel.clearModel();
        }
        all_string_image_atts.removeErrorListener(errorHistory);
	
        
        /* ========= RawImage attributes ============ */
        Iterator<RawImagePanel>  rimIt = all_raw_image_panels.iterator();
        while (rimIt.hasNext())
        {
            RawImagePanel  rimPanel = rimIt.next();
            if (rimPanel != null)
                rimPanel.clearModel();
        }
        all_raw_image_atts.removeErrorListener(errorHistory);
	
        
        /* ========= Commands ============ */
        all_cmdl.removeErrorListener(errorHistory);	   
    }

    private void stopAtkPanel()
    {
        if (standAlone == true)
	   System.exit(0);
	else
	{
	   all_scalar_atts.stopRefresher();
	   all_spectrum_atts.stopRefresher();
	   all_number_image_atts.stopRefresher();
	   all_string_image_atts.stopRefresher();
           all_raw_image_atts.stopRefresher();
	   number_scalar_atts.stopRefresher(); /* AttributePolledList for trend */
	   boolean_scalar_atts.stopRefresher(); /* AttributePolledList for booleanTrend */
	   
           if (keepStateRefresher == false)
	      DeviceFactory.getInstance().stopRefresher();
	   
           if ((stateAtt != null) && (statusAtt != null))
	   {
	      state_status_atts.stopRefresher();
	      //System.out.println("state and status viewers.setModel(null)");
	      attStateViewer.setModel(null);
	      attStatusViewer.setModel(null);
	   }

           // We need to remove all skippingRefresh flags set to true by atkpanel
           RefresherUtil.activateRefreshForAllAttributes(all_spectrum_atts);
           RefresherUtil.activateRefreshForAllAttributes(all_number_image_atts);
           RefresherUtil.activateRefreshForAllAttributes(all_string_image_atts);
           RefresherUtil.activateRefreshForAllAttributes(all_raw_image_atts);
           RefresherUtil.activateRefreshForAllAttributes(all_scalar_atts);

           /* We need to clear all models on all viewers to save memory usage */
           clearAllModels();
	   
	   this.dispose();
	}
    }

    /** Exit the Application */
    private void exitForm(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_exitForm
          stopAtkPanel();
    }//GEN-LAST:event_exitForm



    /* Aborts the Application. This method is called only within connectDevice private method. */
    /* abortAppli() is called in case of problem connecting to the devices. When abortAppli() is */
    /* called the initComponent() and startUp() methods have not been called yet. So no viewer is */
    /* created and no clearModel call is needed inside abortAppli()                             */
    private void abortAppli()
    {
        if (standAlone == true)
	   System.exit(-1);
	else
	{
	   splash.setVisible(false);
	   all_scalar_atts.stopRefresher();
	   all_spectrum_atts.stopRefresher();
	   all_number_image_atts.stopRefresher();
	   all_string_image_atts.stopRefresher();
           all_raw_image_atts.stopRefresher();
	   number_scalar_atts.stopRefresher(); /* AttributePolledList for trend */
	   boolean_scalar_atts.stopRefresher(); /* AttributePolledList for booleanTrend */
	
	   if (keepStateRefresher == false)
	      DeviceFactory.getInstance().stopRefresher();
	      
	   if ((stateAtt != null) && (statusAtt != null))
	   {
	      state_status_atts.stopRefresher();
	   }
	   this.dispose();
	}
    }







    private void  createAllScalarListViewers()
    {
       java.awt.GridBagConstraints            gbc;

       
       gbc = new java.awt.GridBagConstraints();
       gbc.gridx = 0;
       gbc.gridy = 0;
       gbc.insets = new java.awt.Insets(5, 5, 5, 5);
       gbc.fill = java.awt.GridBagConstraints.BOTH;
       gbc.weightx = 1.0;
       gbc.weighty = 1.0;
       
       allScalarListViewer = null;
       operatorScalarListViewer = null;
       
       if ((op_scalar_atts.getSize() <= 0) && (exp_scalar_atts.getSize() <= 0))
       {
	  javax.swing.JLabel  noatt = new javax.swing.JLabel();
	  noatt.setText("No   attribute ");
	  noatt.setFont(new java.awt.Font("Helvetica", java.awt.Font.PLAIN, 18));
	  noatt.setForeground(java.awt.Color.black);
	  scrollJPanel.add(noatt, gbc);
       }
       else
       { 
	  
	  // Merge operator and expert attributes in all_scalar_atts
	  for (int ind=0; ind < op_scalar_atts.getSize(); ind++)
	  {
	     IEntity ie = (IEntity) op_scalar_atts.get(ind);
	     all_scalar_atts.add(ie);
	  }
	  for (int ind=0; ind < exp_scalar_atts.getSize(); ind++)
	  {
	     IEntity ie = (IEntity) exp_scalar_atts.get(ind);
	     all_scalar_atts.add(ie);
	  }
	  
	  allScalarListViewer = new ScalarListViewer();
	  operatorScalarListViewer = new ScalarListViewer();
	  
	  allScalarListViewer.setToolTipDisplay(ScalarListViewer.TOOLTIP_DISPLAY_ALL);
	  operatorScalarListViewer.setToolTipDisplay(ScalarListViewer.TOOLTIP_DISPLAY_ALL);
	  
	  if (modifPropButton == false)
	  {
	     allScalarListViewer.setPropertyButtonVisible(false);
	     operatorScalarListViewer.setPropertyButtonVisible(false);
	  }
	  
	  if (roMode == true)
	  {
	     allScalarListViewer.setSetterVisible(false);
	     operatorScalarListViewer.setSetterVisible(false);
	  }
	  
          allScalarListViewer.setTheFont(new java.awt.Font("Dialog", java.awt.Font.PLAIN, 14));
          allScalarListViewer.setBooleanSetterType(ScalarListViewer.BOOLEAN_COMBO_SETTER);
	  allScalarListViewer.setModel(all_scalar_atts);

          operatorScalarListViewer.setTheFont(new java.awt.Font("Dialog", java.awt.Font.PLAIN, 14));
          operatorScalarListViewer.setBooleanSetterType(ScalarListViewer.BOOLEAN_COMBO_SETTER);
	  operatorScalarListViewer.setModel(op_scalar_atts);
	  
        }
    }

    
    
    
    private void show_all_scalars()
    {
	int             neededWidth, neededHeight;

	
	neededWidth = allScalarListViewer.getPreferredSize().width;
	neededHeight = allScalarListViewer.getPreferredSize().height;

	if (neededWidth < MIN_WIDTH)
	   neededWidth = MIN_WIDTH;
	if (neededWidth > MAX_WIDTH)
	   neededWidth = MAX_WIDTH;

	if (neededHeight < MIN_HEIGHT)
	   neededHeight = MIN_HEIGHT;
	if (neededHeight > MAX_HEIGHT)
	   neededHeight = MAX_HEIGHT;	      

	jScrollPane1.setViewportView(allScalarListViewer);
	jTabbedPane1.setPreferredSize(new java.awt.Dimension(neededWidth+30, neededHeight+30));
	pack();
    }

    
    
    
    private void show_operator_scalars()
    {
	int             neededWidth, neededHeight;

	
	neededWidth = operatorScalarListViewer.getPreferredSize().width;
	neededHeight = operatorScalarListViewer.getPreferredSize().height;

	if (neededWidth < MIN_WIDTH)
	   neededWidth = MIN_WIDTH;
	if (neededWidth > MAX_WIDTH)
	   neededWidth = MAX_WIDTH;

	if (neededHeight < MIN_HEIGHT)
	   neededHeight = MIN_HEIGHT;
	if (neededHeight > MAX_HEIGHT)
	   neededHeight = MAX_HEIGHT;	      

	jScrollPane1.setViewportView(operatorScalarListViewer);
	jTabbedPane1.setPreferredSize(new java.awt.Dimension(neededWidth+30, neededHeight+30));
	pack();
    }



    

    private void createAllSpectrumTabs()
    {
	int                                         nb_atts, idx;
        SpectrumPanel                               sp_panel;
        StringSpectrumPanel                         str_sp_panel;
        DevStateSpectrumPanel                       ds_sp_panel;
        BooleanSpectrumPanel                        bool_sp_panel;
	IEntity                                     spectrum_att = null;
	INumberSpectrum                             nb_spectrum_att = null;
	IStringSpectrum                             str_spectrum_att = null;
        IDevStateSpectrum                           ds_spectrum_att = null;
        IBooleanSpectrum                            bool_spectrum_att = null;
        fr.esrf.tangoatk.core.AttributeList         all_sorted_spectrum_atts;

	
	all_spectrum_panels = new Vector<JComponent>();
	nb_atts = all_spectrum_atts.getSize();
	
	if (nb_atts > 0)
	{
	   // Sort the operator and the expert attributes in all_spectrum_atts
	   all_sorted_spectrum_atts = new fr.esrf.tangoatk.core.AttributeList();
	   for (int ind=0; ind < op_spectrum_atts.getSize(); ind++)
	   {
	      IEntity ie = (IEntity) op_spectrum_atts.get(ind);
	      all_sorted_spectrum_atts.add(ie);
	   }
	   for (int ind=0; ind < all_spectrum_atts.getSize(); ind++)
	   {
	      IEntity ie = (IEntity) all_spectrum_atts.get(ind);
	      if (ie.isExpert())
		 all_sorted_spectrum_atts.add(ie);	         
	   }
	   all_spectrum_atts = all_sorted_spectrum_atts; // use the sorted list
	}
	
	nb_atts = all_spectrum_atts.getSize();
	for (idx=0; idx < nb_atts; idx++)
	{
            spectrum_att = (IEntity) all_spectrum_atts.getElementAt(idx);
            if (spectrum_att instanceof INumberSpectrum)
	    {
	       nb_spectrum_att = (INumberSpectrum) spectrum_att;
	       // Create one Number Spectrum Panel per Number Spectrum Attribute
               sp_panel = new SpectrumPanel(nb_spectrum_att);
               // Add the spectrum panel as a tab into the tabbed panel of the main frame
               jTabbedPane1.addTab(nb_spectrum_att.getNameSansDevice(), sp_panel);
	       all_spectrum_panels.add(idx, sp_panel);
               
               // Add the min and max scalar att models if necessary
               if (all_scalar_atts.getSize() > 0)
               {
                   if (nb_spectrum_att.hasMinxMaxxAttributes())
                   {
                       String          attFullName = null;
                       IEntity         ie;
                       INumberScalar   minAtt=null, maxAtt=null; 
                       if ((nb_spectrum_att.getMinxAttName() != null) && (nb_spectrum_att.getMaxxAttName() != null))
                       {
                          attFullName = nb_spectrum_att.getDevice().getName()+"/"+ nb_spectrum_att.getMinxAttName();                          
                          ie = all_scalar_atts.get(attFullName);
                          if (ie != null)
                             if (ie instanceof INumberScalar)
                                 minAtt = (INumberScalar) ie;
                          
                          attFullName = nb_spectrum_att.getDevice().getName()+"/"+ nb_spectrum_att.getMaxxAttName();                          
                          ie = all_scalar_atts.get(attFullName);
                          if (ie != null)
                             if (ie instanceof INumberScalar)
                                 maxAtt = (INumberScalar) ie;                          
                       }
                       if ((minAtt != null) && (maxAtt != null))
                          sp_panel.setXminXmaxModels(minAtt, maxAtt); 
                   }
               }
	    }
	    else
	    {
	       if (spectrum_att instanceof IStringSpectrum)
	       {
		  str_spectrum_att = (IStringSpectrum) spectrum_att;
		  // Create one String Spectrum Panel per String Spectrum Attribute
        	  str_sp_panel = new StringSpectrumPanel(str_spectrum_att);
        	  // Add the spectrum panel as a tab into the tabbed panel of the main frame
        	  jTabbedPane1.addTab(str_spectrum_att.getNameSansDevice(), str_sp_panel);
		  all_spectrum_panels.add(idx, str_sp_panel);
		  //str_spectrum_att.setSkippingRefresh(true);
	       }
               else
               {
                   if (spectrum_att instanceof IDevStateSpectrum)
                   {
                       ds_spectrum_att = (IDevStateSpectrum) spectrum_att;
                       // Create one DevState Spectrum Panel per DevState Spectrum Attribute
                       ds_sp_panel = new DevStateSpectrumPanel(ds_spectrum_att);
                       // Add the spectrum panel as a tab into the tabbed panel of the main frame
                       jTabbedPane1.addTab(ds_spectrum_att.getNameSansDevice(), ds_sp_panel);
                       all_spectrum_panels.add(idx, ds_sp_panel);
                   }
                   else
                   {
                       if (spectrum_att instanceof IBooleanSpectrum)
                       {
                           bool_spectrum_att = (IBooleanSpectrum) spectrum_att;
                           // Create one Boolean Spectrum Panel per Boolean Spectrum Attribute
                           bool_sp_panel = new BooleanSpectrumPanel(bool_spectrum_att);
                           // Add the spectrum panel as a tab into the tabbed panel of the main frame
                           jTabbedPane1.addTab(bool_spectrum_att.getNameSansDevice(), bool_sp_panel);
                           all_spectrum_panels.add(idx, bool_sp_panel);
                       }
                   }
               }
	    }
        }
    }
    
    
    private void show_operator_spectrums()
    {
       int                  nb_tabs = 0;
       Component            specPanel = null;
       INumberSpectrum      nSpecAtt = null;
       IStringSpectrum      strSpecAtt = null;
       IDevStateSpectrum    dsSpecAtt = null;
       IBooleanSpectrum     boolSpecAtt = null;
       int                  ind, specInd;
       
       
       nb_tabs = jTabbedPane1.getTabCount();
       
       for (ind = 1; ind < nb_tabs; ind++)
       {
	  specPanel = jTabbedPane1.getComponentAt(ind);
	  if (specPanel instanceof SpectrumPanel)
	  {
	     SpectrumPanel sp = (SpectrumPanel) specPanel;
	     nSpecAtt = sp.getModel();
	     specInd = op_spectrum_atts.indexOf(nSpecAtt);
             if ( specInd < 0 )
             {
                 jTabbedPane1.removeTabAt( ind );
                 nb_tabs = jTabbedPane1.getTabCount();
                 ind--;//tab has been removed, index adapted
             }
	  }
	  else
	  {
	     if (specPanel instanceof StringSpectrumPanel)
	     {
		StringSpectrumPanel strsp = (StringSpectrumPanel) specPanel;
		strSpecAtt = strsp.getModel();
		specInd = op_spectrum_atts.indexOf(strSpecAtt);
                if ( specInd < 0 )
                {
                    jTabbedPane1.removeTabAt( ind );
                    nb_tabs = jTabbedPane1.getTabCount();
                    ind--;//tab has been removed, index adapted
                }
	     }
             else
             {
                 if (specPanel instanceof DevStateSpectrumPanel)
                 {
		    DevStateSpectrumPanel dssp = (DevStateSpectrumPanel) specPanel;
		    dsSpecAtt = dssp.getModel();
		    specInd = op_spectrum_atts.indexOf(dsSpecAtt);
                    if ( specInd < 0 )
                    {
                       jTabbedPane1.removeTabAt( ind );
                       nb_tabs = jTabbedPane1.getTabCount();
                       ind--;//tab has been removed, index adapted
                    }
                 }
                 else
                 {
                     if (specPanel instanceof BooleanSpectrumPanel)
                     {
                         BooleanSpectrumPanel boolsp = (BooleanSpectrumPanel) specPanel;                         
                         boolSpecAtt = boolsp.getModel();
                         specInd = op_spectrum_atts.indexOf(boolSpecAtt);
                         if (specInd < 0)
                         {
                             jTabbedPane1.removeTabAt(ind);
                             nb_tabs = jTabbedPane1.getTabCount();
                             ind--;//tab has been removed, index adapted
                         }
                     }
                 }
             }
	  }
       }
    }

    
    
    private void show_all_spectrums()
    {
       int                    nb_spectrums = 0;
       Object                 obj=null;
       JComponent             spPanel=null;
       SpectrumPanel          specPanel = null;
       StringSpectrumPanel    strspecPanel = null;
       DevStateSpectrumPanel  dsspecPanel = null;
       BooleanSpectrumPanel   boolspecPanel = null;
       INumberSpectrum        nSpecAtt = null;
       IStringSpectrum        strSpecAtt = null;
       IDevStateSpectrum      dsSpecAtt = null;
       IBooleanSpectrum       boolSpecAtt = null;
       int                    ind, specInd;
       
       
       nb_spectrums = all_spectrum_atts.getSize();      
       
       for (ind = 0; ind < nb_spectrums; ind++)
       {
	  obj = all_spectrum_atts.get(ind);
	  if (obj instanceof INumberSpectrum)
	  {
	      nSpecAtt = (INumberSpectrum) obj;
	      specInd = jTabbedPane1.indexOfTab(nSpecAtt.getNameSansDevice());
	      if (specInd < 0) // This spectrum is not currently in the tabbed pane
	      {
		spPanel = all_spectrum_panels.get(ind);
		if (spPanel instanceof SpectrumPanel)
		{
		   specPanel = (SpectrumPanel) spPanel;
        	   jTabbedPane1.addTab(nSpecAtt.getNameSansDevice(), specPanel);
		}
	      }
	  }
	  else
	  {
	      if (obj instanceof IStringSpectrum)
	      {
		 strSpecAtt = (IStringSpectrum) obj;
		 specInd = jTabbedPane1.indexOfTab(strSpecAtt.getNameSansDevice());
		 if (specInd < 0) // This spectrum is not currently in the tabbed pane
		 {
		   spPanel = all_spectrum_panels.get(ind);
		   if (spPanel instanceof StringSpectrumPanel)
		   {
		      strspecPanel = (StringSpectrumPanel) spPanel;
        	      jTabbedPane1.addTab(strSpecAtt.getNameSansDevice(), strspecPanel);
		   }
		 }
	      }
              else
              {
 	         if (obj instanceof IDevStateSpectrum)
	         {
		    dsSpecAtt = (IDevStateSpectrum) obj;
		    specInd = jTabbedPane1.indexOfTab(dsSpecAtt.getNameSansDevice());
		    if (specInd < 0) // This spectrum is not currently in the tabbed pane
		    {
		      spPanel = all_spectrum_panels.get(ind);
		      if (spPanel instanceof DevStateSpectrumPanel)
		      {
		         dsspecPanel = (DevStateSpectrumPanel) spPanel;
        	         jTabbedPane1.addTab(dsSpecAtt.getNameSansDevice(), dsspecPanel);
		      }
		    }
	         }
                 else
                 {
                     if (obj instanceof IBooleanSpectrum)
                     {
                         boolSpecAtt = (IBooleanSpectrum) obj;
                         specInd = jTabbedPane1.indexOfTab(boolSpecAtt.getNameSansDevice());
                         if (specInd < 0) // This spectrum is not currently in the tabbed pane
                         {
                             spPanel = all_spectrum_panels.get(ind);
                             if (spPanel instanceof BooleanSpectrumPanel)
                             {
                                 boolspecPanel = (BooleanSpectrumPanel) spPanel;
                                 jTabbedPane1.addTab(boolSpecAtt.getNameSansDevice(), boolspecPanel);
                             }
                         }
                     }
                 }
              }
	  }
       }
    }

    

    

    private void createAllNumberImageTabs()
    {
	int                                         nb_atts, idx;
        ImagePanel                                  att_tab;
	INumberImage                                image_att = null;
        fr.esrf.tangoatk.core.AttributeList         all_sorted_image_atts;

	
        //all_image_panels = new Vector();
	all_image_panels = new Vector<ImagePanel>();
	nb_atts = all_number_image_atts.getSize();
	
	if (nb_atts > 0)
	{
	   // Sort the operator and the expert attributes in all_number_image_atts
	   all_sorted_image_atts = new fr.esrf.tangoatk.core.AttributeList();
	   for (int ind=0; ind < op_number_image_atts.getSize(); ind++)
	   {
	      IEntity ie = (IEntity) op_number_image_atts.get(ind);
	      all_sorted_image_atts.add(ie);
	   }
	   for (int ind=0; ind < all_number_image_atts.getSize(); ind++)
	   {
	      IEntity ie = (IEntity) all_number_image_atts.get(ind);
	      if (ie.isExpert())
		 all_sorted_image_atts.add(ie);
	   }
	   all_number_image_atts = all_sorted_image_atts; // use the sorted list
	}
	
	nb_atts = all_number_image_atts.getSize();
	for (idx=0; idx < nb_atts; idx++)
	{
            image_att = (INumberImage) all_number_image_atts.getElementAt(idx);
            // Create one Image Panel per Number Imag Attribute
            att_tab = new ImagePanel(image_att);
            // Add the Image panel as a tab into the tabbed panel of the main frame
            jTabbedPane1.addTab(image_att.getNameSansDevice(), att_tab);
	    all_image_panels.add(idx, att_tab);
	    //image_att.setSkippingRefresh(true);
        }
    }
    
    
    private void show_operator_images ()
    {
        int                nb_tabs = 0;
        Component          imagePanel = null;
        INumberImage       nImageAtt = null;
        int                ind, imageInd;
	
	
        nb_tabs = jTabbedPane1.getTabCount();
        for (ind = 1; ind < nb_tabs; ind++)
        {
            imagePanel = jTabbedPane1.getComponentAt( ind );
            if ( imagePanel instanceof ImagePanel )
            {
                ImagePanel ip = (ImagePanel) imagePanel;
                nImageAtt = ip.getModel();
                imageInd = op_number_image_atts.indexOf( nImageAtt );
                if ( imageInd < 0 )
                {
                    jTabbedPane1.removeTabAt( ind );
                    nb_tabs = jTabbedPane1.getTabCount();
                    ind--;//tab has been removed, index adapted
                }
            }
        }
    }

    
    
    private void show_all_images()
    {
       int                  nb_images = 0;
       Object               obj=null;
       ImagePanel           imagePanel = null;
       INumberImage         nImageAtt = null;
       int                  ind, imageInd;
       
       
       nb_images = all_number_image_atts.getSize();      
       
       for (ind = 0; ind < nb_images; ind++)
       {
	  obj = all_number_image_atts.get(ind);
	  if (obj instanceof INumberImage)
	  {
	     nImageAtt = (INumberImage) obj;
	     imageInd = jTabbedPane1.indexOfTab(nImageAtt.getNameSansDevice());
	     if (imageInd < 0) // This image is not currently in the tabbed pane
	     {
		 imagePanel = all_image_panels.get(ind);
        	 jTabbedPane1.addTab(nImageAtt.getNameSansDevice(), imagePanel);
	     }
	  }
       }
    }

    

    

    private void createAllStringImageTabs()
    {
	int                                         nb_atts, idx;
        StringImagePanel                                  att_tab;
	IStringImage                                image_att = null;
        fr.esrf.tangoatk.core.AttributeList         all_sorted_image_atts;

	
        //all_string_image_panels = new Vector();
	all_string_image_panels = new Vector<StringImagePanel>();
	nb_atts = all_string_image_atts.getSize();
	
	if (nb_atts > 0)
	{
	   // Sort the operator and the expert attributes in all_string_image_atts
	   all_sorted_image_atts = new fr.esrf.tangoatk.core.AttributeList();
	   for (int ind=0; ind < op_string_image_atts.getSize(); ind++)
	   {
	      IEntity ie = (IEntity) op_string_image_atts.get(ind);
	      all_sorted_image_atts.add(ie);
	   }
	   for (int ind=0; ind < all_string_image_atts.getSize(); ind++)
	   {
	      IEntity ie = (IEntity) all_string_image_atts.get(ind);
	      if (ie.isExpert())
		 all_sorted_image_atts.add(ie);
	   }
	   all_string_image_atts = all_sorted_image_atts; // use the sorted list
	}
	
	nb_atts = all_string_image_atts.getSize();
	for (idx=0; idx < nb_atts; idx++)
	{
            image_att = (IStringImage) all_string_image_atts.getElementAt(idx);
            // Create one StringImage Panel per String Imag Attribute
            att_tab = new StringImagePanel(image_att);
            // Add the Image panel as a tab into the tabbed panel of the main frame
            jTabbedPane1.addTab(image_att.getNameSansDevice(), att_tab);
	    all_string_image_panels.add(idx, att_tab);
	    //image_att.setSkippingRefresh(true);
        }
    }
    
    
    private void show_operator_string_images ()
    {
        int                nb_tabs = 0;
        Component          imagePanel = null;
        IStringImage       sImageAtt = null;
        int                ind, imageInd;
	
	
        nb_tabs = jTabbedPane1.getTabCount();
        for (ind = 1; ind < nb_tabs; ind++)
        {
            imagePanel = jTabbedPane1.getComponentAt( ind );
            if ( imagePanel instanceof StringImagePanel )
            {
                StringImagePanel ip = (StringImagePanel) imagePanel;
                sImageAtt = ip.getModel();
                imageInd = op_string_image_atts.indexOf( sImageAtt );
                if ( imageInd < 0 )
                {
                    jTabbedPane1.removeTabAt( ind );
                    nb_tabs = jTabbedPane1.getTabCount();
                    ind--;//tab has been removed, index adapted
                }
            }
        }
    }

    
    
    private void show_all_string_images()
    {
       int                  nb_images = 0;
       Object               obj=null;
       StringImagePanel     strImagePanel = null;
       IStringImage         sImageAtt = null;
       int                  ind, imageInd;
       
       
       nb_images = all_string_image_atts.getSize();      
       
       for (ind = 0; ind < nb_images; ind++)
       {
	  obj = all_string_image_atts.get(ind);
	  if (obj instanceof IStringImage)
	  {
	     sImageAtt = (IStringImage) obj;
	     imageInd = jTabbedPane1.indexOfTab(sImageAtt.getNameSansDevice());
	     if (imageInd < 0) // This image is not currently in the tabbed pane
	     {
		 strImagePanel = all_string_image_panels.get(ind);
        	 jTabbedPane1.addTab(sImageAtt.getNameSansDevice(), strImagePanel);
	     }
	  }
       }
    }


    

    

    private void createAllRawImageTabs()
    {
	int                                         nb_atts, idx;
        RawImagePanel                               att_tab;
	IRawImage                                   image_att = null;
        fr.esrf.tangoatk.core.AttributeList         all_sorted_raw_image_atts;

	
	all_raw_image_panels = new Vector<RawImagePanel>();
	nb_atts = all_raw_image_atts.getSize();
	
	if (nb_atts > 0)
	{
	   // Sort the operator and the expert attributes in all_raw_image_atts
	   all_sorted_raw_image_atts = new fr.esrf.tangoatk.core.AttributeList();
	   for (int ind=0; ind < op_raw_image_atts.getSize(); ind++)
	   {
	      IEntity ie = (IEntity) op_raw_image_atts.get(ind);
	      all_sorted_raw_image_atts.add(ie);
	   }
	   for (int ind=0; ind < all_raw_image_atts.getSize(); ind++)
	   {
	      IEntity ie = (IEntity) all_raw_image_atts.get(ind);
	      if (ie.isExpert())
		 all_sorted_raw_image_atts.add(ie);
	   }
	   all_raw_image_atts = all_sorted_raw_image_atts; // use the sorted list
	}
	
	nb_atts = all_raw_image_atts.getSize();
	for (idx=0; idx < nb_atts; idx++)
	{
            image_att = (IRawImage) all_raw_image_atts.getElementAt(idx);
            // Create one RawImagePanel per RawImage Attribute
            att_tab = new RawImagePanel(image_att);
            // Add the RawImagePanel as a tab into the tabbed panel of the main frame
            jTabbedPane1.addTab(image_att.getNameSansDevice(), att_tab);
	    all_raw_image_panels.add(idx, att_tab);
        }
    }
    
    
    private void show_operator_raw_images ()
    {
        int                nb_tabs = 0;
        Component          imagePanel = null;
        IRawImage         rawImageAtt = null;
        int                ind, imageInd;
	
	
        nb_tabs = jTabbedPane1.getTabCount();
        for (ind = 1; ind < nb_tabs; ind++)
        {
            imagePanel = jTabbedPane1.getComponentAt( ind );
            if ( imagePanel instanceof RawImagePanel )
            {
                RawImagePanel  rip = (RawImagePanel) imagePanel;
                rawImageAtt = rip.getModel();
                imageInd = op_raw_image_atts.indexOf( rawImageAtt );
                if ( imageInd < 0 )
                {
                    jTabbedPane1.removeTabAt( ind );
                    nb_tabs = jTabbedPane1.getTabCount();
                    ind--;//tab has been removed, index adapted
                }
            }
        }
    }

    
    
    private void show_all_raw_images()
    {
       int                  nb_images = 0;
       Object               obj=null;
       RawImagePanel        rawImagePanel = null;
       IRawImage            rawImageAtt = null;
       int                  ind, imageInd;
       
       
       nb_images = all_raw_image_atts.getSize();      
       
       for (ind = 0; ind < nb_images; ind++)
       {
	  obj = all_raw_image_atts.get(ind);
	  if (obj instanceof IRawImage)
	  {
	     rawImageAtt = (IRawImage) obj;
	     imageInd = jTabbedPane1.indexOfTab(rawImageAtt.getNameSansDevice());
	     if (imageInd < 0) // This image is not currently in the tabbed pane
	     {
		 rawImagePanel = all_raw_image_panels.get(ind);
        	 jTabbedPane1.addTab(rawImageAtt.getNameSansDevice(), rawImagePanel);
	     }
	  }
       }
    }



    private void createAllCmdList()
    {
       int                 nb_cmds;
       CommandList         all_sorted_cmds;
       
       // Filter the commands to show here :
       
       nb_cmds = all_cmdl.getSize();
	
       if (nb_cmds > 0)
       {
	   // Sort operator and expert commands in all_cmdl
	   all_sorted_cmds = new fr.esrf.tangoatk.core.CommandList();
	   for (int ind=0; ind < op_cmdl.getSize(); ind++)
	   {
	      IEntity ie = (IEntity) op_cmdl.get(ind);
	      all_sorted_cmds.add(ie);
	   }
	   for (int ind=0; ind < all_cmdl.getSize(); ind++)
	   {
	      IEntity ie = (IEntity) all_cmdl.get(ind);
	      if (ie.isExpert())
		 all_sorted_cmds.add(ie);
	   }
	   all_cmdl = all_sorted_cmds; // use the sorted list

           commandComboViewer1.setModel(all_cmdl);
       }
       else
           commandComboViewer1.setModel(null);
    }
    
    
    private void show_operator_commands()
    {
	if (all_cmdl.getSize() > 0)
	{
           commandComboViewer1.setModel(op_cmdl);
	}
    }

    
    private void show_all_commands()
    {
	if (all_cmdl.getSize() > 0)
	{
           commandComboViewer1.setModel(all_cmdl);
	}
    }
    
    public void setExpertView(boolean  expView)
    {
        boolean     currentExpview;
	
	currentExpview = expertCheckBoxMenuItem.isSelected();
	if (currentExpview == expView) return;
	
	if (expView == true)
	   expertCheckBoxMenuItem.doClick();
	else
	   operatorCheckBoxMenuItem.doClick();	
    }
    
    public void doFileQuit()
    {
        jMenuItem1.doClick();
    }

    static private boolean getReadOnly(String args[])
    {
        if (args.length <= 0) return false;

        for (int i=0; i<args.length; i++)
        {
            if (args[i].equalsIgnoreCase("-ro"))
                return true;
        }
        return false;
    }

    private static String getDevName(String args[])
    {
        if (args.length <= 0)
            return (null);
        for (int i=0; i<args.length; i++)
        {
            if (args[i].equalsIgnoreCase("-ro"))
                continue;
            try
            {
                if (args[i].contains("/"))
                {
                    return (args[i]);
                }
            }
            catch (Exception ex) {}
        }
        return null;
    }

    private static String getTabName(String args[])
    {
        if (args.length <= 1)
            return (null);
        for (int i=0; i<args.length; i++)
        {
            if (args[i].equalsIgnoreCase("-ro"))
                continue;
            try
            {
                if (!args[i].contains("/"))
                {
                    return (args[i]);
                }
            }
            catch (Exception ex) {}
        }
        return null;
    }


    
    
     
    /**
    * @param args the command line arguments
    */
    public static void main(String args[])
    {
       // startup with standAlone=true, keepStateRefresher=false, modifPropButton=true, roMode=false
       if (args.length <= 0)
          new MainPanel((String) null, true, false); // modifPropButton and roMode = leur default values
       else // args.length > 0
       {
	   wpos = ATKGraphicsUtils.getWindowPosFromArgs(args);
           boolean ro = getReadOnly(args);
           String  dev_name = getDevName(args);
           String  tab_name = getTabName(args);
           if (dev_name == null) // no device name defined, ignore the tab name because does not make sens
           {
               if (ro)
                   new MainPanel(dev_name, true, false, false, true);
               else
                   new MainPanel(dev_name, true, false);
           }
           else //dev_name specified, so we can examine tha tab_name if present
           {
               if (tab_name == null) // dev name specified but not tab_name
               {
                   if (ro)
                       new MainPanel(dev_name, true, false, false, true);
                   else
                       new MainPanel(dev_name, true, false);
               }
               else
               {
                   if (ro)
                       new MainPanel(dev_name, tab_name, true, false, false, true);
                   else
                       new MainPanel(dev_name, tab_name, true, false);
               }
           }

           /*if (args[0].equalsIgnoreCase("-ro"))
           new MainPanel(args[1], true, false, false, true);
           else
           if (args[1].equalsIgnoreCase("-ro"))
           new MainPanel(args[0], true, false, false, true);
           else
           new MainPanel(args[0], true, false);*/
       }
        
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenu jMenu3;
    private javax.swing.JMenuItem jMenuItem5;
    private javax.swing.JMenuItem boolTrendJMenuItem;
    private javax.swing.JMenuItem tgDevTestJMenuItem;
    private javax.swing.JMenuItem jMenuItem3;
    private javax.swing.JMenuItem diagJMenuItem;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenuItem jMenuItem6;
    private javax.swing.JMenuItem jMenuItem7;
    private javax.swing.JMenuItem setRefPeriodJMenuItem;
    private javax.swing.JMenuItem timeoutJMenuItem;
    private javax.swing.JCheckBoxMenuItem expertCheckBoxMenuItem;
    private javax.swing.JCheckBoxMenuItem operatorCheckBoxMenuItem;
    private javax.swing.JMenu jMenu4;
    private javax.swing.JMenuItem jMenuItem4;
    private javax.swing.JPanel stateAndCmdJPanel;
    private javax.swing.JSplitPane mainJSplitPane;
    private javax.swing.JPanel topJPanel;
    private fr.esrf.tangoatk.widget.command.CommandComboViewer commandComboViewer1;
    private fr.esrf.tangoatk.widget.device.StateViewer devStateViewer;
    private fr.esrf.tangoatk.widget.device.StatusViewer devStatusViewer;
    private fr.esrf.tangoatk.widget.attribute.StateViewer attStateViewer;
    private fr.esrf.tangoatk.widget.attribute.StatusViewer attStatusViewer;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JPanel scrollJPanel;
    // End of variables declaration//GEN-END:variables

}

