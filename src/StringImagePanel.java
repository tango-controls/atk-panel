/*
 *
 *   Copyright (C) :	2007,2008,2009
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
 *   File          :   StringImagePanel.java
 *  
 *   Project       :   atkpanel generic java application
 *  
 *   Description   :   The panel to display a  String image attribute
 *  
 *   Author        :   Faranguiss Poncet
 *  
 *   Original      :   May 2007
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
 *   Revision 3.2  2007/10/08 11:08:41  poncet
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
 *   Revision 1.1  2007/05/03 16:39:42  poncet
 *   First release of StringImagePanel in cvs.
 *
 *  
 *  
 */

package atkpanel;

/**
 *
 * @author  poncet
 */

import javax.swing.JPanel;
import javax.swing.JScrollPane;

import fr.esrf.tangoatk.core.IStringImage;
import fr.esrf.tangoatk.widget.attribute.StringImageTableViewer;

public class StringImagePanel extends JPanel
{

    private StringImageTableViewer       sitv=null;
    private IStringImage                 siModel=null;


    /** Creates new form StringImagePanel */
    public StringImagePanel()
    {
        siModel = null;
	initComponents();
    }

    /** Creates new form StringImagePanel to display a StringImage attribute */
    public StringImagePanel(IStringImage  siAtt)
    {
        initComponents();
	siModel = siAtt;
        sitv.setAttModel(siModel);
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents()
    {
        java.awt.GridBagConstraints       gbc=new java.awt.GridBagConstraints();

        setLayout(new java.awt.GridBagLayout());

        sitv = new StringImageTableViewer();

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = java.awt.GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.insets = new java.awt.Insets(3, 3, 3, 5);
        add(sitv, gbc);
    }
    
    protected IStringImage getModel()
    {
       return siModel;
    }

    
    protected void clearModel()
    {
       if (siModel == null) return;
       if (sitv == null) return;
       sitv.clearModel();
    }

}
