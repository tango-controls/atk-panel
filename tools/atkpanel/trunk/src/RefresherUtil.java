/*
 *
 *   Copyright (C) :	2007,2008,2009,2010,2011,2012
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
 *   File          :   RefresherUtil.java
 *  
 *   Project       :   atkpanel generic java application
 *  
 *   Description   :   The refresher utilities : handles stop and start refreshing of one
 *		       particular attribute.
 *  
 *   Author        :   Faranguiss Poncet
 *  
 *   Original      :   July 2007
 *  
 *   $Revision$				$Author$
 *   $Date$					$State$
 *  
 */
 
package atkpanel;

import java.awt.Component;
import fr.esrf.tangoatk.core.*;

public class RefresherUtil {

	public static void activateRefresh(Component comp)
	{
		if(comp instanceof ImagePanel)
		{
			ImagePanel imgComp = (ImagePanel)comp;
			imgComp.getModel().setSkippingRefresh(false);
		}
		if(comp instanceof SpectrumPanel)
		{
			SpectrumPanel specComp = (SpectrumPanel)comp;
			specComp.getModel().setSkippingRefresh(false);
		}
		if(comp instanceof StringSpectrumPanel)
		{
			StringSpectrumPanel specComp = (StringSpectrumPanel)comp;
			specComp.getModel().setSkippingRefresh(false);
		}		
		if(comp instanceof StringImagePanel)
		{
			StringImagePanel imgComp = (StringImagePanel)comp;
			imgComp.getModel().setSkippingRefresh(false);
		}		
		if(comp instanceof DevStateSpectrumPanel)
		{
			DevStateSpectrumPanel dssComp = (DevStateSpectrumPanel)comp;
			dssComp.getModel().setSkippingRefresh(false);
		}		
		if(comp instanceof RawImagePanel)
		{
			RawImagePanel rawImgComp = (RawImagePanel)comp;
			rawImgComp.getModel().setSkippingRefresh(false);
		}
	}
	
	public static void refresh(Component comp)
	{
		if(comp instanceof ImagePanel)
		{
			ImagePanel    imgComp = (ImagePanel)comp;
			INumberImage  ini = imgComp.getModel();
			refreshIfNeeded(ini);
		}
		if(comp instanceof SpectrumPanel)
		{
			SpectrumPanel    spectComp = (SpectrumPanel)comp;
			INumberSpectrum  ins = spectComp.getModel();
			refreshIfNeeded(ins);
		}
		if(comp instanceof StringSpectrumPanel)
		{
			StringSpectrumPanel strSpectComp = (StringSpectrumPanel)comp;
			IStringSpectrum     iss = strSpectComp.getModel();
			refreshIfNeeded(iss);
		}		
		if(comp instanceof StringImagePanel)
		{
			StringImagePanel    strImageComp = (StringImagePanel)comp;
			IStringImage        isi = strImageComp.getModel();
			refreshIfNeeded(isi);
		}		
		if(comp instanceof DevStateSpectrumPanel)
		{
			DevStateSpectrumPanel dssComp = (DevStateSpectrumPanel)comp;
                        IDevStateSpectrum     idss = dssComp.getModel();
			refreshIfNeeded(idss);
		}		
		if(comp instanceof RawImagePanel)
		{
			RawImagePanel  rawImgComp = (RawImagePanel)comp;
			IRawImage      iri = rawImgComp.getModel();
			refreshIfNeeded(iri);
		}
	}	
	
	public static void skippingRefresh(Component comp)
	{
		if(comp instanceof ImagePanel)
		{
			ImagePanel imgComp = (ImagePanel)comp;
			imgComp.getModel().setSkippingRefresh(true);
		}
		if(comp instanceof SpectrumPanel)
		{
			SpectrumPanel specComp = (SpectrumPanel)comp;
			specComp.getModel().setSkippingRefresh(true);
		}
		if(comp instanceof StringSpectrumPanel)
		{
			StringSpectrumPanel specComp = (StringSpectrumPanel)comp;
			specComp.getModel().setSkippingRefresh(true);
		}		
		if(comp instanceof StringImagePanel)
		{
			StringImagePanel imgComp = (StringImagePanel)comp;
			imgComp.getModel().setSkippingRefresh(true);
		}		
		if(comp instanceof DevStateSpectrumPanel)
		{
			DevStateSpectrumPanel dssComp = (DevStateSpectrumPanel)comp;
			dssComp.getModel().setSkippingRefresh(true);
		}		
		if(comp instanceof RawImagePanel)
		{
			RawImagePanel   rawImgComp = (RawImagePanel)comp;
			rawImgComp.getModel().setSkippingRefresh(true);
		}
	}
	
	public static void activateRefreshForAllComponent(javax.swing.JTabbedPane jtabbedPane)
	{
		Component[] components = jtabbedPane.getComponents();
		for(int i=0;i < components.length;i++)
		{
			activateRefresh(components[i]);
		}
	}
	
	public static void skippingRefreshForAllComponent(javax.swing.JTabbedPane jtabbedPane)
	{
		Component[] components = jtabbedPane.getComponents();
		for(int i=0;i < components.length;i++)
		{
			skippingRefresh(components[i]);
		}		
	}
	
	public static void refreshIfNeeded(IAttribute  iatt)
	{ // call refresh() if and only if the attribute is not updated thanks to the Tango events
	        if (!iatt.hasEvents()) // events are not possible for this attribute (event subscription failed)
        	{
        		iatt.refresh();
        	}

	}
        
        public static void skippingRefreshForAllAttributes(AttributeList attl)
        {
            IAttribute    iatt = null;
            int           nb_atts = attl.getSize();
            for (int idx=0; idx < nb_atts; idx++)
            {
                iatt = (IAttribute) attl.getElementAt(idx);
                iatt.setSkippingRefresh(true);
            }
        }

        public static void activateRefreshForAllAttributes(AttributeList attl)
        {
            IAttribute    iatt = null;
            int           nb_atts = attl.getSize();
            for (int idx=0; idx < nb_atts; idx++)
            {
                iatt = (IAttribute) attl.getElementAt(idx);
                iatt.setSkippingRefresh(false);
            }
        }
        
        public static void skippingRefreshForExpertAttributes(AttributeList attl)
        {
            IAttribute    iatt = null;
            int           nb_atts = attl.getSize();
            for (int idx=0; idx < nb_atts; idx++)
            {
                iatt = (IAttribute) attl.getElementAt(idx);
                if (iatt.isExpert())
                   iatt.setSkippingRefresh(true);
            }
        }
        
        public static void activateRefreshForExpertAttributes(AttributeList attl)
        {
            IAttribute    iatt = null;
            int           nb_atts = attl.getSize();
            for (int idx=0; idx < nb_atts; idx++)
            {
                iatt = (IAttribute) attl.getElementAt(idx);
                if (iatt.isExpert())
                   iatt.setSkippingRefresh(false);
            }
        }
		
}
