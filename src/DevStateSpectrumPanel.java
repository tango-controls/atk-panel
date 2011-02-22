/*
 *
 *   Copyright (C) :	2008,2009
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
 *   $Log$
 *   Revision 4.2  2009/08/25 15:16:05  poncet
 *   Bug Fix.
 *
 *   Revision 4.1  2009/04/23 16:36:57  poncet
 *   Added support for RawImage attribute (corresponding to some Tango DevEncoded attributes).
 *
 *   Revision 4.0  2009/04/03 14:47:01  poncet
 *   Added the support for Xaxis min and max properties for NumberSpectrum attributes.
 *
 *   Revision 3.11  2009/03/10 14:40:50  poncet
 *   Adapted to the new version of the ATK StringImageTableViewer. All scrolling is now handled directly inside the ATK viewer.
 *
 *   Revision 3.10  2009/01/27 09:47:56  poncet
 *   Added the GNU General Public License statement in all source files.
 *
 *   Revision 3.9  2009/01/06 13:21:07  poncet
 *   Fixed the bug where the DevStateSpectrumPanel tabs were not refreshed by the refreshers.
 *
 *   Revision 3.8  2008/10/23 16:15:36  poncet
 *   Fixed a bug related to the DevicFactory's State and Status refresher.
 *
 *   Revision 3.7  2008/09/10 12:15:17  poncet
 *   Better management of attribute refreshing. Now refresh only the needed attributes. Takes into account operator/expert flag.
 *
 *   Revision 3.6  2008/07/10 14:34:29  poncet
 *   Added DevStateSpectrumPanel to display DevStateSpectrum attributes.
 *
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
