/*
 *
 *   Copyright (C) :	2008,2009,2010,2011,2012, 2013, 2014, 2015
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
 *   File          :   DevStateSpectrumPanel.java
 *  
 *   Project       :   atkpanel generic java application
 *  
 *   Description   :   The panel to display a  DevState spectrum attribute
 *  
 *   Author        :   Faranguiss Poncet
 *  
 *   Original      :   July 2008
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

import javax.swing.JScrollPane;
import fr.esrf.tangoatk.core.IDevStateSpectrum;
import fr.esrf.tangoatk.widget.attribute.DevStateSpectrumViewer;

public class DevStateSpectrumPanel extends JScrollPane
{

    private DevStateSpectrumViewer       dsSpectrumViewer;
    private IDevStateSpectrum            dsSpecModel;
    
        
    /** Creates new form StringSpectrumPanel */
    public DevStateSpectrumPanel()
    {
        dsSpecModel = null;
	initComponents();
    }

    /** Creates new form StringSpectrumPanel to display a StringSpectrum attribute */
    public DevStateSpectrumPanel(IDevStateSpectrum  dsSpecAtt)
    {
        initComponents();
	dsSpecModel = dsSpecAtt;
        dsSpectrumViewer.setModel(dsSpecAtt);
    }

    private void initComponents()
    {
       dsSpectrumViewer = new DevStateSpectrumViewer();         
       setViewportView(dsSpectrumViewer);
    }
    
    
    protected IDevStateSpectrum getModel()
    {
       return dsSpecModel;
    }

    
    protected void clearModel()
    {
       if (dsSpectrumViewer == null) return;
       dsSpectrumViewer.clearModel();
    }

}
