/*
 *
 *   Copyright (C) :	2003,2004,2005,2006,2007,2008,2009
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
 *   $Log$
 *   Revision 4.2  2009/08/25 15:16:05  poncet
 *   Bug Fix.
 *
 *   Revision 4.1  2009/04/23 16:36:57  poncet
 *   Added support for RawImage attribute (corresponding to some Tango DevEncoded attributes).
 *
 *   Revision 4.0  2009/04/03 14:47:02  poncet
 *   Added the support for Xaxis min and max properties for NumberSpectrum attributes.
 *
 *   Revision 3.11  2009/03/10 14:40:50  poncet
 *   Adapted to the new version of the ATK StringImageTableViewer. All scrolling is now handled directly inside the ATK viewer.
 *
 *   Revision 3.10  2009/01/27 09:47:58  poncet
 *   Added the GNU General Public License statement in all source files.
 *
 *   Revision 3.9  2009/01/06 13:21:07  poncet
 *   Fixed the bug where the DevStateSpectrumPanel tabs were not refreshed by the refreshers.
 *
 *   Revision 3.8  2008/10/23 16:15:36  poncet
 *   Fixed a bug related to the DevicFactory's State and Status refresher.
 *
 *   Revision 3.7  2008/09/10 12:15:18  poncet
 *   Better management of attribute refreshing. Now refresh only the needed attributes. Takes into account operator/expert flag.
 *
 *   Revision 3.6  2008/07/10 14:34:29  poncet
 *   Added DevStateSpectrumPanel to display DevStateSpectrum attributes.
 *
 *   Revision 3.5  2008/05/26 17:00:47  poncet
 *   The "Scalar" Tab is not refreshed anymore when it's hidden by another Tab. The attributePolledList refreshers (for trends) are not started any more by default.
 *
 *   Revision 3.4  2008/05/16 09:37:51  poncet
 *   Added clearModel() methods in all panel classes. Added clearAllModels() method in MainPanel.
 *
 *   Revision 3.3  2007/11/20 17:48:14  poncet
 *   Suppressed the Status and State attributes from scalar attribute lists because now they are added when attribute factory get + wildcard is called. This was not the case before ATK release 3.0.8.
 *
 *   Revision 3.2  2007/10/08 11:08:42  poncet
 *   Simplified the panel container hierarchy in ImagePanel.java to fix a display problem which was flashing in some cases. NumberImageViewer is now directly inside the ScrollPane.
 *
 *   Revision 3.1  2007/07/17 13:02:49  poncet
 *   For the spectrum and image attributes, now only the visible tab is refreshed to improve performance and network trafic.
 *
 *   Revision 3.0  2007/06/01 13:53:41  poncet
 *   Stop supporting Java 4.
 *
 *   Revision 2.18  2007/05/29 14:07:49  poncet
 *   Added the doFileQuit() method to the MainPanel class.
 *
 *   Revision 2.17  2007/05/23 13:42:53  poncet
 *   Added a public method : setExpertView(boolean) to the MainPanel class.
 *
 *   Revision 2.16  2007/05/03 16:46:21  poncet
 *   Added support for StringImage attributes. Needs ATK 2.8.11 or higher.
 *
 *   Revision 2.15  2007/03/30 15:33:40  poncet
 *   Added the tooltip to some of atk scalar viewer and the state is also added to the stateViewer's tooltip
 *
 *   Revision 2.14  2007/02/09 16:24:19  poncet
 *   Fixed a nullPointerException bug in MainPanel.java:stopAtkPanel() method.
 *
 *   Revision 2.13  2007/02/09 15:20:40  poncet
 *   Added the EnumScalar attributes in scalar attribute lists.
 *
 *   Revision 2.12  2007/01/31 17:52:30  poncet
 *   Removed "view graph" checkbox from spectrum panels. They use exclusively NumberSpectrumViewer now. Compatible with ATK 2.7.2 and higher.
 *
 *   Revision 2.11  2007/01/12 17:09:59  poncet
 *   Added BooleanScalarComboEditor and BooleanTrend. Needs ATK release>=2.6.2. Added the JSplitPane in the MainWindow.
 *
 *   Revision 2.10  2006/12/14 09:45:58  poncet
 *   Bug fixed by Raphael from Soleil concerning the NumberImage and NumberSpectrum attributes operator / expert.
 *
 *   Revision 2.9  2006/10/23 15:17:36  poncet
 *   Workaround to put optionPane in front of Splash. Modified Makefile.
 *
 *   Revision 2.7  2006/04/24 09:36:36  poncet
 *   Fixed a bug concerning Trend Frame. The Trend is now created earlier and centered according to the MainPanel.
 *
 *   Revision 2.6  2006/01/09 15:13:42  poncet
 *   Changed Splash window message
 *
 *   Revision 2.5  2006/01/09 15:03:15  poncet
 *   Fixed few bugs.
 *
 *   Revision 2.4  2005/11/24 13:59:45  poncet
 *   Added menu item to set the device connection timeout. Remove splash window when application is aborted and not standalone.
 *
 *   Revision 2.3  2005/11/10 14:22:08  poncet
 *   Removed traces.
 *
 *   Revision 2.2  2005/11/10 14:13:25  poncet
 *   The atkpanel releases > 2.0 are incompatible with ATK releases below ATKWidget-2.2.0. Release 2.2 of AtkPanel includes code which allows to free memory when atkpanel exits.
 *
 *   Revision 2.1  2005/11/08 14:16:16  poncet
 *   The 2.0 release of atkpanel and higher is incompatible with ATK releases below ATKWidget-2.2.0. Updated the release number displayed in Splash window.
 *
 *   Revision 2.0  2005/11/08 14:11:42  poncet
 *   The ErrorPopup is now a singleton class in ATK. The 2.0 release of atkpanel and higher is incompatible with ATK releases below ATKWidget-2.2.0.
 *
 *   Revision 1.16  2005/05/03 17:02:03  poncet
 *   Added an new attribute list for state and status attributes without events.
 *
 *   Revision 1.15  2005/04/15 12:16:29  poncet
 *   Added the BooleanScalarAttribute, stateAttributeViewer and statusAttributeViewer.
 *
 *   Revision 1.14  2004/11/24 14:11:56  poncet
 *   Added new constructor (Boolean arguments instead of boolean) to be used in synoptics.
 *
 *   Revision 1.13  2004/11/23 09:37:12  poncet
 *   Added ReadOnly mode for MainPanel which supresses all commands. Removed the
 *   TabbedPanel in the MainPanel when the device has no attribute at all. This
 *   Allows to have a smaller window for gauges for example.
 *
 *   Revision 1.11  2004/10/12 12:57:02  poncet
 *   Committed to keep the same CVS revision number for all files.
 *
 *   Revision 1.10  2003/12/16 17:56:56  poncet
 *   Added the handling of StringSpectrum Attributes in atkpanel.
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
