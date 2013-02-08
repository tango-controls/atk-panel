/*
 *
 *   Copyright (C) :	2013
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
 *   File          :   BooleanSpectrumPanel.java
 *  
 *   Project       :   atkpanel generic java application
 *  
 *   Description   :   The panel to display a  DevState spectrum attribute
 *  
 *   Author        :   Faranguiss Poncet
 *  
 *   Original      :   February 2013
 *  
 *   $Revision$				$Author$
 *   $Date$					$State$
 *  
 */
 


/**
 *
 * @author  poncet
 */
 
package atkpanel;


import fr.esrf.tangoatk.core.IBooleanSpectrum;
import javax.swing.JScrollPane;
import fr.esrf.tangoatk.widget.attribute.BooleanSpectrumViewer;

public class BooleanSpectrumPanel extends JScrollPane
{

    private BooleanSpectrumViewer       boolSpectrumViewer;
    private IBooleanSpectrum            boolSpecModel;
    
        
    /** Creates new form StringSpectrumPanel */
    public BooleanSpectrumPanel()
    {
        boolSpecModel = null;
	initComponents();
    }

    /** Creates new form StringSpectrumPanel to display a StringSpectrum attribute */
    public BooleanSpectrumPanel(IBooleanSpectrum  boolSpecAtt)
    {
        initComponents();
	boolSpecModel = boolSpecAtt;
        boolSpectrumViewer.setModel(boolSpecAtt);
    }

    private void initComponents()
    {
       boolSpectrumViewer = new BooleanSpectrumViewer();         
       setViewportView(boolSpectrumViewer);
    }
    
    
    protected IBooleanSpectrum getModel()
    {
       return boolSpecModel;
    }

    
    protected void clearModel()
    {
       if (boolSpectrumViewer == null) return;
       boolSpectrumViewer.clearModel();
    }

}
