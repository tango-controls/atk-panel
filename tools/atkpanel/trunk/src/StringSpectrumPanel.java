/*
 *
 *   Copyright (C) :	2003,2004,2005,2006,2007,2008,2009,2010,2011,2012
 *                      2013, 2014, 2015
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
 *  
 *   File          :   StringSpectrumPanel.java
 *  
 *   Project       :   atkpanel generic java application
 *  
 *   Description   :   The panel to display a  string spectrum attribute
 *  
 *   Author        :   Faranguiss Poncet
 *  
 *   Original      :   December 2003
 *  
 *   $Revision$				$Author$
 *   $Date$					$State$
 *  
 *  
 */
 
package atkpanel;

/**
 *
 * @author  poncet
 */
import fr.esrf.tangoatk.core.*;
import fr.esrf.tangoatk.widget.attribute.*;

public class StringSpectrumPanel extends javax.swing.JPanel
{


    private SimpleStringSpectrumViewer       simpleStrSpectrumViewer;

    private IStringSpectrum                  strSpecModel;
    
    
    
    /** Creates new form StringSpectrumPanel */
    public StringSpectrumPanel()
    {
        strSpecModel = null;
	initComponents();
    }

    /** Creates new form StringSpectrumPanel to display a StringSpectrum attribute */
    public StringSpectrumPanel(IStringSpectrum  strspecAtt)
    {
        initComponents();
	strSpecModel = strspecAtt;
        simpleStrSpectrumViewer.setModel(strspecAtt);
    }

    private void initComponents()
    {
       simpleStrSpectrumViewer = new SimpleStringSpectrumViewer();            

       setLayout(new java.awt.GridBagLayout());
       java.awt.GridBagConstraints gridBagConstraints1;

       gridBagConstraints1 = new java.awt.GridBagConstraints();
       gridBagConstraints1.fill = java.awt.GridBagConstraints.BOTH;
       gridBagConstraints1.insets = new java.awt.Insets(1,1,1,1);
       gridBagConstraints1.weightx = 1.0;
       gridBagConstraints1.weighty = 1.0;
       add(simpleStrSpectrumViewer, gridBagConstraints1);        
    }
    
    
    protected IStringSpectrum getModel()
    {
       return strSpecModel;
    }

    
    protected void clearModel()
    {
       if (strSpecModel == null) return;
       if (simpleStrSpectrumViewer == null) return;
       simpleStrSpectrumViewer.setModel(null);
    }

}
