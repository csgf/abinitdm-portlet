/*
*************************************************************************
Copyright (c) 2011-2015:
Istituto Nazionale di Fisica Nucleare (INFN), Italy
Consorzio COMETA (COMETA), Italy

See http://www.infn.it and and http://www.consorzio-cometa.it for details on
the copyright holders.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.

@author <a href="mailto:giuseppe.larocca@ct.infn.it">Giuseppe La Rocca</a>
***************************************************************************
*/
package it.infn.ct.abinit;

// import liferay libraries
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.model.Company;
import com.liferay.portal.model.User;
import com.liferay.portal.theme.ThemeDisplay;

// import DataEngine libraries
import com.liferay.portal.util.PortalUtil;
import it.infn.ct.GridEngine.InformationSystem.BDII;
import it.infn.ct.GridEngine.Job.*;

// import generic Java libraries
import it.infn.ct.GridEngine.UsersTracking.UsersTrackingDBInterface;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.net.InetAddress;
import java.net.URISyntaxException;
import java.net.URI;

// import portlet libraries
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.naming.NamingException;
import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.GenericPortlet;
import javax.portlet.PortletException;
import javax.portlet.PortletMode;
import javax.portlet.PortletPreferences;
import javax.portlet.PortletRequestDispatcher;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;

// Importing Apache libraries
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.archivers.ArchiveStreamFactory;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.utils.IOUtils;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.portlet.PortletFileUpload;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class Abinit extends GenericPortlet {

    private static Log log = LogFactory.getLog(Abinit.class);   

    @Override
    protected void doEdit(RenderRequest request,
            RenderResponse response)
            throws PortletException, IOException
    {

        PortletPreferences portletPreferences =
                (PortletPreferences) request.getPreferences();

        response.setContentType("text/html");
        
        // Getting the LATO INFRASTRUCTURE from the portlet preferences
        String lato_abinit_INFRASTRUCTURE = portletPreferences.getValue("lato_abinit_INFRASTRUCTURE", "N/A");
        // Getting the login credential from the portlet preferences for LATO
        String lato_abinit_LOGIN = portletPreferences.getValue("lato_abinit_LOGIN", "N/A");
        // Getting the password credential from the portlet preferences for LATO
        String lato_abinit_PASSWD = portletPreferences.getValue("lato_abinit_PASSWD", "N/A");
        // Getting the cluster hostname(s) from the portlet preferences for LATO
        String[] lato_abinit_WMS = portletPreferences.getValues("lato_abinit_WMS", new String[5]);
        // Getting the ETOKENSERVER from the portlet preferences for LATO
        String lato_abinit_ETOKENSERVER = portletPreferences.getValue("lato_abinit_ETOKENSERVER", "N/A");
        // Getting the MYPROXYSERVER from the portlet preferences for LATO
        String lato_abinit_MYPROXYSERVER = portletPreferences.getValue("lato_abinit_MYPROXYSERVER", "N/A");
        // Getting the PORT from the portlet preferences for LATO
        String lato_abinit_PORT = portletPreferences.getValue("lato_abinit_PORT", "N/A");
        // Getting the ROBOTID from the portlet preferences for LATO
        String lato_abinit_ROBOTID = portletPreferences.getValue("lato_abinit_ROBOTID", "N/A");
        // Getting the WEBDAV Server for LATO
        String lato_abinit_WEBDAV = portletPreferences.getValue("lato_abinit_WEBDAV", "N/A");
        // Getting the ROLE from the portlet preferences for LATO
        String lato_abinit_ROLE = portletPreferences.getValue("lato_abinit_ROLE", "N/A");
        // Getting the RENEWAL from the portlet preferences for LATO
        String lato_abinit_RENEWAL = portletPreferences.getValue("lato_abinit_RENEWAL", "checked");
        // Getting the DISABLEVOMS from the portlet preferences for LATO
        String lato_abinit_DISABLEVOMS = portletPreferences.getValue("lato_abinit_DISABLEVOMS", "unchecked");

        // Getting the ABINIT INFRASTRUCTURE from the portlet preferences for the GARUDA VO
        String garuda_abinit_INFRASTRUCTURE = portletPreferences.getValue("garuda_abinit_INFRASTRUCTURE", "N/A");
        // Getting the ABINIT VONAME from the portlet preferences for the GARUDA VO
        String garuda_abinit_VONAME = portletPreferences.getValue("garuda_abinit_VONAME", "N/A");
        // Getting the ABINIT TOPPBDII from the portlet preferences for the GARUDA VO
        String garuda_abinit_TOPBDII = portletPreferences.getValue("garuda_abinit_TOPBDII", "N/A");
        // Getting the ABINIT WMS from the portlet preferences for the GARUDA VO
        String[] garuda_abinit_WMS = portletPreferences.getValues("garuda_abinit_WMS", new String[5]);
        // Getting the ABINIT ETOKENSERVER from the portlet preferences for the GARUDA VO
        String garuda_abinit_ETOKENSERVER = portletPreferences.getValue("garuda_abinit_ETOKENSERVER", "N/A");
        // Getting the ABINIT MYPROXYSERVER from the portlet preferences for the GARUDA VO
        String garuda_abinit_MYPROXYSERVER = portletPreferences.getValue("garuda_abinit_MYPROXYSERVER", "N/A");
        // Getting the ABINIT PORT from the portlet preferences for the GARUDA VO
        String garuda_abinit_PORT = portletPreferences.getValue("garuda_abinit_PORT", "N/A");
        // Getting the ABINIT ROBOTID from the portlet preferences for the GARUDA VO
        String garuda_abinit_ROBOTID = portletPreferences.getValue("garuda_abinit_ROBOTID", "N/A");
        // Getting the WEBDAV Server for GARUDA
        String garuda_abinit_WEBDAV = portletPreferences.getValue("garuda_abinit_WEBDAV", "N/A");
        // Getting the ABINIT ROLE from the portlet preferences for the GARUDA VO
        String garuda_abinit_ROLE = portletPreferences.getValue("garuda_abinit_ROLE", "N/A");
        // Getting the ABINIT RENEWAL from the portlet preferences for the GARUDA VO
        String garuda_abinit_RENEWAL = portletPreferences.getValue("garuda_abinit_RENEWAL", "checked");
        // Getting the ABINIT DISABLEVOMS from the portlet preferences for the GARUDA VO
        String garuda_abinit_DISABLEVOMS = portletPreferences.getValue("garuda_abinit_DISABLEVOMS", "unchecked");

        // Getting the ABINIT INFRASTRUCTURE from the portlet preferences for the EUMED VO
        String eumed_abinit_INFRASTRUCTURE = portletPreferences.getValue("eumed_abinit_INFRASTRUCTURE", "N/A");
        // Getting the ABINIT VONAME from the portlet preferences for the EUMED VO
        String eumed_abinit_VONAME = portletPreferences.getValue("eumed_abinit_VONAME", "N/A");
        // Getting the ABINIT TOPPBDII from the portlet preferences for the EUMED VO
        String eumed_abinit_TOPBDII = portletPreferences.getValue("eumed_abinit_TOPBDII", "N/A");
        // Getting the ABINIT WMS from the portlet preferences for the EUMED VO
        String[] eumed_abinit_WMS = portletPreferences.getValues("eumed_abinit_WMS", new String[5]);
        // Getting the ABINIT ETOKENSERVER from the portlet preferences for the EUMED VO
        String eumed_abinit_ETOKENSERVER = portletPreferences.getValue("eumed_abinit_ETOKENSERVER", "N/A");
        // Getting the ABINIT MYPROXYSERVER from the portlet preferences for the EUMED VO
        String eumed_abinit_MYPROXYSERVER = portletPreferences.getValue("eumed_abinit_MYPROXYSERVER", "N/A");
        // Getting the ABINIT PORT from the portlet preferences for the EUMED VO
        String eumed_abinit_PORT = portletPreferences.getValue("eumed_abinit_PORT", "N/A");
        // Getting the ABINIT ROBOTID from the portlet preferences for the EUMED VO
        String eumed_abinit_ROBOTID = portletPreferences.getValue("eumed_abinit_ROBOTID", "N/A");
        // Getting the WEBDAV Server for EUMED
        String eumed_abinit_WEBDAV = portletPreferences.getValue("eumed_abinit_WEBDAV", "N/A");
        // Getting the ABINIT ROLE from the portlet preferences for the EUMED VO
        String eumed_abinit_ROLE = portletPreferences.getValue("eumed_abinit_ROLE", "N/A");
        // Getting the ABINIT RENEWAL from the portlet preferences for the EUMED VO
        String eumed_abinit_RENEWAL = portletPreferences.getValue("eumed_abinit_RENEWAL", "checked");
        // Getting the ABINIT DISABLEVOMS from the portlet preferences for the EUMED VO
        String eumed_abinit_DISABLEVOMS = portletPreferences.getValue("eumed_abinit_DISABLEVOMS", "unchecked");
        
        // Getting the ABINIT INFRASTRUCTURE from the portlet preferences for the SAGRID VO
        String sagrid_abinit_INFRASTRUCTURE = portletPreferences.getValue("sagrid_abinit_INFRASTRUCTURE", "N/A");
        // Getting the ABINIT VONAME from the portlet preferences for the SAGRID VO
        String sagrid_abinit_VONAME = portletPreferences.getValue("sagrid_abinit_VONAME", "N/A");
        // Getting the ABINIT TOPPBDII from the portlet preferences for the SAGRID VO
        String sagrid_abinit_TOPBDII = portletPreferences.getValue("sagrid_abinit_TOPBDII", "N/A");
        // Getting the ABINIT WMS from the portlet preferences for the SAGRID VO
        String[] sagrid_abinit_WMS = portletPreferences.getValues("sagrid_abinit_WMS", new String[5]);
        // Getting the ABINIT ETOKENSERVER from the portlet preferences for the SAGRID VO
        String sagrid_abinit_ETOKENSERVER = portletPreferences.getValue("sagrid_abinit_ETOKENSERVER", "N/A");
        // Getting the ABINIT MYPROXYSERVER from the portlet preferences for the SAGRID VO
        String sagrid_abinit_MYPROXYSERVER = portletPreferences.getValue("sagrid_abinit_MYPROXYSERVER", "N/A");
        // Getting the ABINIT PORT from the portlet preferences for the SAGRID VO
        String sagrid_abinit_PORT = portletPreferences.getValue("sagrid_abinit_PORT", "N/A");
        // Getting the ABINIT ROBOTID from the portlet preferences for the SAGRID VO
        String sagrid_abinit_ROBOTID = portletPreferences.getValue("sagrid_abinit_ROBOTID", "N/A");
        // Getting the WEBDAV Server for SAGRID
        String sagrid_abinit_WEBDAV = portletPreferences.getValue("sagrid_abinit_WEBDAV", "N/A");
        // Getting the ABINIT ROLE from the portlet preferences for the SAGRID VO
        String sagrid_abinit_ROLE = portletPreferences.getValue("sagrid_abinit_ROLE", "N/A");
        // Getting the ABINIT RENEWAL from the portlet preferences for the SAGRID VO
        String sagrid_abinit_RENEWAL = portletPreferences.getValue("sagrid_abinit_RENEWAL", "checked");
        // Getting the ABINIT DISABLEVOMS from the portlet preferences for the SAGRID VO
        String sagrid_abinit_DISABLEVOMS = portletPreferences.getValue("sagrid_abinit_DISABLEVOMS", "unchecked");

        // Getting the ABINIT INFRASTRUCTURE from the portlet preferences for the SEE VO
        String see_abinit_INFRASTRUCTURE = portletPreferences.getValue("see_abinit_INFRASTRUCTURE", "N/A");
        // Getting the ABINIT VONAME from the portlet preferences for the SEE VO
        String see_abinit_VONAME = portletPreferences.getValue("see_abinit_VONAME", "N/A");
        // Getting the ABINIT TOPPBDII from the portlet preferences for the SEE VO
        String see_abinit_TOPBDII = portletPreferences.getValue("see_abinit_TOPBDII", "N/A");
        // Getting the ABINIT WMS from the portlet preferences for the SEE VO
        String[] see_abinit_WMS = portletPreferences.getValues("see_abinit_WMS", new String[5]);
        // Getting the ABINIT ETOKENSERVER from the portlet preferences for the SEE VO
        String see_abinit_ETOKENSERVER = portletPreferences.getValue("see_abinit_ETOKENSERVER", "N/A");
        // Getting the ABINIT MYPROXYSERVER from the portlet preferences for the SEE VO
        String see_abinit_MYPROXYSERVER = portletPreferences.getValue("see_abinit_MYPROXYSERVER", "N/A");
        // Getting the ABINIT PORT from the portlet preferences for the SEE VO
        String see_abinit_PORT = portletPreferences.getValue("see_abinit_PORT", "N/A");
        // Getting the ABINIT ROBOTID from the portlet preferences for the SEE VO
        String see_abinit_ROBOTID = portletPreferences.getValue("see_abinit_ROBOTID", "N/A");
        // Getting the WEBDAV Server for SEE
        String see_abinit_WEBDAV = portletPreferences.getValue("see_abinit_WEBDAV", "N/A");
        // Getting the ABINIT ROLE from the portlet preferences for the SEE VO
        String see_abinit_ROLE = portletPreferences.getValue("see_abinit_ROLE", "N/A");
        // Getting the ABINIT RENEWAL from the portlet preferences for the SEE VO
        String see_abinit_RENEWAL = portletPreferences.getValue("see_abinit_RENEWAL", "checked");
        // Getting the ABINIT DISABLEVOMS from the portlet preferences for the SEE VO
        String see_abinit_DISABLEVOMS = portletPreferences.getValue("see_abinit_DISABLEVOMS", "unchecked");
        
        // Getting the ABINIT INFRASTRUCTURE from the portlet preferences for the GISELA VO
        String gisela_abinit_INFRASTRUCTURE = portletPreferences.getValue("gisela_abinit_INFRASTRUCTURE", "N/A");
        // Getting the ABINIT VONAME from the portlet preferences for the GISELA VO
        String gisela_abinit_VONAME = portletPreferences.getValue("gisela_abinit_VONAME", "N/A");
        // Getting the ABINIT TOPPBDII from the portlet preferences for the GISELA VO
        String gisela_abinit_TOPBDII = portletPreferences.getValue("gisela_abinit_TOPBDII", "N/A");
        // Getting the ABINIT WMS from the portlet preferences for the GISELA VO
        String[] gisela_abinit_WMS = portletPreferences.getValues("gisela_abinit_WMS", new String[5]);
        // Getting the ABINIT ETOKENSERVER from the portlet preferences for the GISELA VO
        String gisela_abinit_ETOKENSERVER = portletPreferences.getValue("gisela_abinit_ETOKENSERVER", "N/A");
        // Getting the ABINIT MYPROXYSERVER from the portlet preferences for the GISELA VO
        String gisela_abinit_MYPROXYSERVER = portletPreferences.getValue("gisela_abinit_MYPROXYSERVER", "N/A");
        // Getting the ABINIT PORT from the portlet preferences for the GISELA VO
        String gisela_abinit_PORT = portletPreferences.getValue("gisela_abinit_PORT", "N/A");
        // Getting the ABINIT ROBOTID from the portlet preferences for the GISELA VO
        String gisela_abinit_ROBOTID = portletPreferences.getValue("gisela_abinit_ROBOTID", "N/A");
        // Getting the WEBDAV Server for GISELA
        String gisela_abinit_WEBDAV = portletPreferences.getValue("gisela_abinit_WEBDAV", "N/A");
        // Getting the ABINIT ROLE from the portlet preferences for the GISELA VO
        String gisela_abinit_ROLE = portletPreferences.getValue("gisela_abinit_ROLE", "N/A");
        // Getting the ABINIT RENEWAL from the portlet preferences for the GISELA VO
        String gisela_abinit_RENEWAL = portletPreferences.getValue("gisela_abinit_RENEWAL", "checked");
        // Getting the ABINIT DISABLEVOMS from the portlet preferences for the GISELA VO
        String gisela_abinit_DISABLEVOMS = portletPreferences.getValue("gisela_abinit_DISABLEVOMS", "unchecked");

        // Getting the ABINIT APPID from the portlet preferences
        String abinit_APPID = portletPreferences.getValue("abinit_APPID", "N/A");
        // Getting the LOG LEVEL from the portlet preferences
        String abinit_LOGLEVEL = portletPreferences.getValue("abinit_LOGLEVEL", "INFO");
        // Getting the METADATA METADATA_HOST from the portlet preferences
        String abinit_METADATA_HOST = portletPreferences.getValue("abinit_METADATA_HOST", "N/A");
        // Getting the ABINIT OUTPUT_PATH from the portlet preferences
        String abinit_OUTPUT_PATH = portletPreferences.getValue("abinit_OUTPUT_PATH", "/tmp");
        // Getting the ABINIT SOFTWARE from the portlet preferences
        String abinit_SOFTWARE = portletPreferences.getValue("abinit_SOFTWARE", "N/A");
        // Getting the ABINIT LOCAL_PROXY from the portlet preferences
        String abinit_LOCAL_PROXY = portletPreferences.getValue("abinit_LOCAL_PROXY", "N/A");
        // Getting the TRACKING_DB_HOSTNAME from the portlet preferences
        String TRACKING_DB_HOSTNAME = portletPreferences.getValue("TRACKING_DB_HOSTNAME", "N/A");
        // Getting the TRACKING_DB_USERNAME from the portlet preferences
        String TRACKING_DB_USERNAME = portletPreferences.getValue("TRACKING_DB_USERNAME", "N/A");
        // Getting the TRACKING_DB_PASSWORD from the portlet preferences
        String TRACKING_DB_PASSWORD = portletPreferences.getValue("TRACKING_DB_PASSWORD", "N/A");
        // Getting the SMTP_HOST from the portlet preferences
        String SMTP_HOST = portletPreferences.getValue("SMTP_HOST", "N/A");
        // Getting the SENDER MAIL from the portlet preferences
        String SENDER_MAIL = portletPreferences.getValue("SENDER_MAIL", "N/A");
        // Get the list of enabled Infrastructures
        String[] infras = portletPreferences.getValues("abinit_ENABLEINFRASTRUCTURE", new String[3]);

        // Set the default portlet preferences
        request.setAttribute("garuda_abinit_INFRASTRUCTURE", garuda_abinit_INFRASTRUCTURE.trim());
        request.setAttribute("garuda_abinit_VONAME", garuda_abinit_VONAME.trim());
        request.setAttribute("garuda_abinit_TOPBDII", garuda_abinit_TOPBDII.trim());
        request.setAttribute("garuda_abinit_WMS", garuda_abinit_WMS);
        request.setAttribute("garuda_abinit_ETOKENSERVER", garuda_abinit_ETOKENSERVER.trim());
        request.setAttribute("garuda_abinit_MYPROXYSERVER", garuda_abinit_MYPROXYSERVER.trim());
        request.setAttribute("garuda_abinit_PORT", garuda_abinit_PORT.trim());
        request.setAttribute("garuda_abinit_ROBOTID", garuda_abinit_ROBOTID.trim());
        request.setAttribute("garuda_abinit_WEBDAV", garuda_abinit_WEBDAV.trim());
        request.setAttribute("garuda_abinit_ROLE", garuda_abinit_ROLE.trim());
        request.setAttribute("garuda_abinit_RENEWAL", garuda_abinit_RENEWAL);
        request.setAttribute("garuda_abinit_DISABLEVOMS", garuda_abinit_DISABLEVOMS);
        
        request.setAttribute("lato_abinit_INFRASTRUCTURE", lato_abinit_INFRASTRUCTURE.trim());
        request.setAttribute("lato_abinit_LOGIN", lato_abinit_LOGIN.trim());
        request.setAttribute("lato_abinit_PASSWD", lato_abinit_PASSWD.trim());
        request.setAttribute("lato_abinit_WMS", lato_abinit_WMS);
        request.setAttribute("lato_abinit_ETOKENSERVER", lato_abinit_ETOKENSERVER.trim());
        request.setAttribute("lato_abinit_MYPROXYSERVER", lato_abinit_MYPROXYSERVER.trim());
        request.setAttribute("lato_abinit_PORT", lato_abinit_PORT.trim());
        request.setAttribute("lato_abinit_ROBOTID", lato_abinit_ROBOTID.trim());
        request.setAttribute("lato_abinit_WEBDAV", lato_abinit_WEBDAV.trim());
        request.setAttribute("lato_abinit_ROLE", lato_abinit_ROLE.trim());
        request.setAttribute("lato_abinit_RENEWAL", lato_abinit_RENEWAL);
        request.setAttribute("lato_abinit_DISABLEVOMS", lato_abinit_DISABLEVOMS);

        request.setAttribute("eumed_abinit_INFRASTRUCTURE", eumed_abinit_INFRASTRUCTURE.trim());
        request.setAttribute("eumed_abinit_VONAME", eumed_abinit_VONAME.trim());
        request.setAttribute("eumed_abinit_TOPBDII", eumed_abinit_TOPBDII.trim());
        request.setAttribute("eumed_abinit_WMS", eumed_abinit_WMS);
        request.setAttribute("eumed_abinit_ETOKENSERVER", eumed_abinit_ETOKENSERVER.trim());
        request.setAttribute("eumed_abinit_MYPROXYSERVER", eumed_abinit_MYPROXYSERVER.trim());
        request.setAttribute("eumed_abinit_PORT", eumed_abinit_PORT.trim());
        request.setAttribute("eumed_abinit_ROBOTID", eumed_abinit_ROBOTID.trim());
        request.setAttribute("eumed_abinit_WEBDAV", eumed_abinit_WEBDAV.trim());
        request.setAttribute("eumed_abinit_ROLE", eumed_abinit_ROLE.trim());
        request.setAttribute("eumed_abinit_RENEWAL", eumed_abinit_RENEWAL);
        request.setAttribute("eumed_abinit_DISABLEVOMS", eumed_abinit_DISABLEVOMS);
        
        request.setAttribute("sagrid_abinit_INFRASTRUCTURE", sagrid_abinit_INFRASTRUCTURE.trim());
        request.setAttribute("sagrid_abinit_VONAME", sagrid_abinit_VONAME.trim());
        request.setAttribute("sagrid_abinit_TOPBDII", sagrid_abinit_TOPBDII.trim());
        request.setAttribute("sagrid_abinit_WMS", sagrid_abinit_WMS);
        request.setAttribute("sagrid_abinit_ETOKENSERVER", sagrid_abinit_ETOKENSERVER.trim());
        request.setAttribute("sagrid_abinit_MYPROXYSERVER", sagrid_abinit_MYPROXYSERVER.trim());
        request.setAttribute("sagrid_abinit_PORT", sagrid_abinit_PORT.trim());
        request.setAttribute("sagrid_abinit_ROBOTID", sagrid_abinit_ROBOTID.trim());
        request.setAttribute("sagrid_abinit_WEBDAV", sagrid_abinit_WEBDAV.trim());
        request.setAttribute("sagrid_abinit_ROLE", sagrid_abinit_ROLE.trim());
        request.setAttribute("sagrid_abinit_RENEWAL", sagrid_abinit_RENEWAL);
        request.setAttribute("sagrid_abinit_DISABLEVOMS", sagrid_abinit_DISABLEVOMS);

        request.setAttribute("see_abinit_INFRASTRUCTURE", see_abinit_INFRASTRUCTURE.trim());
        request.setAttribute("see_abinit_VONAME", see_abinit_VONAME.trim());
        request.setAttribute("see_abinit_TOPBDII", see_abinit_TOPBDII.trim());
        request.setAttribute("see_abinit_WMS", see_abinit_WMS);
        request.setAttribute("see_abinit_ETOKENSERVER", see_abinit_ETOKENSERVER.trim());
        request.setAttribute("see_abinit_MYPROXYSERVER", see_abinit_MYPROXYSERVER.trim());
        request.setAttribute("see_abinit_PORT", see_abinit_PORT.trim());
        request.setAttribute("see_abinit_ROBOTID", see_abinit_ROBOTID.trim());
        request.setAttribute("see_abinit_WEBDAV", see_abinit_WEBDAV.trim());
        request.setAttribute("see_abinit_ROLE", see_abinit_ROLE.trim());
        request.setAttribute("see_abinit_RENEWAL", see_abinit_RENEWAL);
        request.setAttribute("see_abinit_DISABLEVOMS", see_abinit_DISABLEVOMS);
        
        request.setAttribute("gisela_abinit_INFRASTRUCTURE", gisela_abinit_INFRASTRUCTURE.trim());
        request.setAttribute("gisela_abinit_VONAME", gisela_abinit_VONAME.trim());
        request.setAttribute("gisela_abinit_TOPBDII", gisela_abinit_TOPBDII.trim());
        request.setAttribute("gisela_abinit_WMS", gisela_abinit_WMS);
        request.setAttribute("gisela_abinit_ETOKENSERVER", gisela_abinit_ETOKENSERVER.trim());
        request.setAttribute("gisela_abinit_MYPROXYSERVER", gisela_abinit_MYPROXYSERVER.trim());
        request.setAttribute("gisela_abinit_PORT", gisela_abinit_PORT.trim());
        request.setAttribute("gisela_abinit_ROBOTID", gisela_abinit_ROBOTID.trim());
        request.setAttribute("gisela_abinit_WEBDAV", gisela_abinit_WEBDAV.trim());
        request.setAttribute("gisela_abinit_ROLE", gisela_abinit_ROLE.trim());
        request.setAttribute("gisela_abinit_RENEWAL", gisela_abinit_RENEWAL);
        request.setAttribute("gisela_abinit_DISABLEVOMS", gisela_abinit_DISABLEVOMS);

        request.setAttribute("abinit_ENABLEINFRASTRUCTURE", infras);
        request.setAttribute("abinit_APPID", abinit_APPID.trim());
        request.setAttribute("abinit_LOGLEVEL", abinit_LOGLEVEL.trim());
        request.setAttribute("abinit_METADATA_HOST", abinit_METADATA_HOST.trim());
        request.setAttribute("abinit_OUTPUT_PATH", abinit_OUTPUT_PATH.trim());
        request.setAttribute("abinit_SOFTWARE", abinit_SOFTWARE.trim());
        request.setAttribute("abinit_LOCAL_PROXY", abinit_LOCAL_PROXY.trim());
        request.setAttribute("TRACKING_DB_HOSTNAME", TRACKING_DB_HOSTNAME.trim());
        request.setAttribute("TRACKING_DB_USERNAME", TRACKING_DB_USERNAME.trim());
        request.setAttribute("TRACKING_DB_PASSWORD", TRACKING_DB_PASSWORD.trim());
        request.setAttribute("SMTP_HOST", SMTP_HOST.trim());
        request.setAttribute("SENDER_MAIL", SENDER_MAIL.trim());
        
        if (abinit_LOGLEVEL.trim().equals("VERBOSE")) {
        log.info("\nStarting the EDIT mode...with this settings"
        + "\nlato_abinit_INFRASTRUCTURE: " + lato_abinit_INFRASTRUCTURE
        + "\nlato_abinit_LOGIN: " + lato_abinit_LOGIN
        + "\nlato_abinit_PASSWD: " + lato_abinit_PASSWD                    
        + "\nlato_abinit_ETOKENSERVER: " + lato_abinit_ETOKENSERVER
        + "\nlato_abinit_MYPROXYSERVER: " + lato_abinit_MYPROXYSERVER
        + "\nlato_abinit_PORT: " + lato_abinit_PORT
        + "\nlato_abinit_ROBOTID: " + lato_abinit_ROBOTID
        + "\nlato_abinit_WEBDAV: " + lato_abinit_WEBDAV
        + "\nlato_abinit_ROLE: " + lato_abinit_ROLE
        + "\nlato_abinit_RENEWAL: " + lato_abinit_RENEWAL
        + "\nlato_abinit_DISABLEVOMS: " + lato_abinit_DISABLEVOMS
                
        + "\ngaruda_abinit_INFRASTRUCTURE: " + garuda_abinit_INFRASTRUCTURE
        + "\ngaruda_abinit_VONAME: " + garuda_abinit_VONAME
        + "\ngaruda_abinit_TOPBDII: " + garuda_abinit_TOPBDII                    
        + "\ngaruda_abinit_ETOKENSERVER: " + garuda_abinit_ETOKENSERVER
        + "\ngaruda_abinit_MYPROXYSERVER: " + garuda_abinit_MYPROXYSERVER
        + "\ngaruda_abinit_PORT: " + garuda_abinit_PORT
        + "\ngaruda_abinit_ROBOTID: " + garuda_abinit_ROBOTID
        + "\ngaruda_abinit_WEBDAV: " + garuda_abinit_WEBDAV 
        + "\ngaruda_abinit_ROLE: " + garuda_abinit_ROLE
        + "\ngaruda_abinit_RENEWAL: " + garuda_abinit_RENEWAL
        + "\ngaruda_abinit_DISABLEVOMS: " + garuda_abinit_DISABLEVOMS

        + "\n\neumed_abinit_INFRASTRUCTURE: " + eumed_abinit_INFRASTRUCTURE
        + "\neumed_abinit_VONAME: " + eumed_abinit_VONAME
        + "\neumed_abinit_TOPBDII: " + eumed_abinit_TOPBDII                    
        + "\neumed_abinit_ETOKENSERVER: " + eumed_abinit_ETOKENSERVER
        + "\neumed_abinit_MYPROXYSERVER: " + eumed_abinit_MYPROXYSERVER
        + "\neumed_abinit_PORT: " + eumed_abinit_PORT
        + "\neumed_abinit_ROBOTID: " + eumed_abinit_ROBOTID
        + "\neumed_abinit_WEBDAV: " + eumed_abinit_WEBDAV
        + "\neumed_abinit_ROLE: " + eumed_abinit_ROLE
        + "\neumed_abinit_RENEWAL: " + eumed_abinit_RENEWAL
        + "\neumed_abinit_DISABLEVOMS: " + eumed_abinit_DISABLEVOMS
                
        + "\n\nsagrid_abinit_INFRASTRUCTURE: " + sagrid_abinit_INFRASTRUCTURE
        + "\nsagrid_abinit_VONAME: " + sagrid_abinit_VONAME
        + "\nsagrid_abinit_TOPBDII: " + sagrid_abinit_TOPBDII                    
        + "\nsagrid_abinit_ETOKENSERVER: " + sagrid_abinit_ETOKENSERVER
        + "\nsagrid_abinit_MYPROXYSERVER: " + sagrid_abinit_MYPROXYSERVER
        + "\nsagrid_abinit_PORT: " + sagrid_abinit_PORT
        + "\nsagrid_abinit_ROBOTID: " + sagrid_abinit_ROBOTID
        + "\nsagrid_abinit_WEBDAV: " + sagrid_abinit_WEBDAV
        + "\nsagrid_abinit_ROLE: " + sagrid_abinit_ROLE
        + "\nsagrid_abinit_RENEWAL: " + sagrid_abinit_RENEWAL
        + "\nsagrid_abinit_DISABLEVOMS: " + sagrid_abinit_DISABLEVOMS

        + "\n\nsee_abinit_INFRASTRUCTURE: " + see_abinit_INFRASTRUCTURE
        + "\nsee_abinit_VONAME: " + see_abinit_VONAME
        + "\nsee_abinit_TOPBDII: " + see_abinit_TOPBDII                   
        + "\nsee_abinit_ETOKENSERVER: " + see_abinit_ETOKENSERVER
        + "\nsee_abinit_MYPROXYSERVER: " + see_abinit_MYPROXYSERVER
        + "\nsee_abinit_PORT: " + see_abinit_PORT
        + "\nsee_abinit_ROBOTID: " + see_abinit_ROBOTID
        + "\nsee_abinit_WEBDAV: " + see_abinit_WEBDAV 
        + "\nsee_abinit_ROLE: " + see_abinit_ROLE
        + "\nsee_abinit_RENEWAL: " + see_abinit_RENEWAL
        + "\nsee_abinit_DISABLEVOMS: " + see_abinit_DISABLEVOMS
                
        + "\n\ngisela_abinit_INFRASTRUCTURE: " + gisela_abinit_INFRASTRUCTURE
        + "\ngisela_abinit_VONAME: " + gisela_abinit_VONAME
        + "\ngisela_abinit_TOPBDII: " + gisela_abinit_TOPBDII                   
        + "\ngisela_abinit_ETOKENSERVER: " + gisela_abinit_ETOKENSERVER
        + "\ngisela_abinit_MYPROXYSERVER: " + gisela_abinit_MYPROXYSERVER
        + "\ngisela_abinit_PORT: " + gisela_abinit_PORT
        + "\ngisela_abinit_ROBOTID: " + gisela_abinit_ROBOTID
        + "\ngisela_abinit_WEBDAV: " + gisela_abinit_WEBDAV 
        + "\ngisela_abinit_ROLE: " + gisela_abinit_ROLE
        + "\ngisela_abinit_RENEWAL: " + gisela_abinit_RENEWAL
        + "\ngisela_abinit_DISABLEVOMS: " + gisela_abinit_DISABLEVOMS                
        
        + "\nabinit_APPID: " + abinit_APPID
        + "\nabinit_LOGLEVEL: " + abinit_LOGLEVEL
        + "\nabinit_METADATA_HOST: " + abinit_METADATA_HOST
        + "\nabinit_OUTPUT_PATH: " + abinit_OUTPUT_PATH
        + "\nabinit_SOFTWARE: " +abinit_SOFTWARE
        + "\nabinit_LOCAL_PROXY: " +abinit_LOCAL_PROXY
        + "\nTracking_DB_Hostname: " + TRACKING_DB_HOSTNAME
        + "\nTracking_DB_Username: " + TRACKING_DB_USERNAME
        + "\nTracking_DB_Password: " + TRACKING_DB_PASSWORD
        + "\nSMTP Server: " + SMTP_HOST
        + "\nSender: " + SENDER_MAIL);
        }

        PortletRequestDispatcher dispatcher =
                getPortletContext().getRequestDispatcher("/edit.jsp");

        dispatcher.include(request, response);
    }

    @Override
    protected void doHelp(RenderRequest request, RenderResponse response)
            throws PortletException, IOException {
        //super.doHelp(request, response);

        response.setContentType("text/html");

        log.info("\nStarting the HELP mode...");
        PortletRequestDispatcher dispatcher =
                getPortletContext().getRequestDispatcher("/help.jsp");

        dispatcher.include(request, response);
    }

    @Override
    protected void doView(RenderRequest request, RenderResponse response)
            throws PortletException, IOException {

        PortletPreferences portletPreferences =
                (PortletPreferences) request.getPreferences();

        response.setContentType("text/html");

        //java.util.Enumeration listPreferences = portletPreferences.getNames();
        PortletRequestDispatcher dispatcher = null;
        
        String lato_abinit_PASSWD = "";
        String lato_abinit_LOGIN = "";
        String garuda_abinit_TOPBDII = "";
        String garuda_abinit_VONAME = "";
        String eumed_abinit_TOPBDII = "";
        String eumed_abinit_VONAME = "";
        String sagrid_abinit_TOPBDII = "";
        String sagrid_abinit_VONAME = "";
        String see_abinit_TOPBDII = "";
        String see_abinit_VONAME = "";
        String gisela_abinit_TOPBDII = "";
        String gisela_abinit_VONAME = "";
        
        String lato_abinit_ENABLEINFRASTRUCTURE = "";
        String garuda_abinit_ENABLEINFRASTRUCTURE = "";
        String eumed_abinit_ENABLEINFRASTRUCTURE = "";
        String sagrid_abinit_ENABLEINFRASTRUCTURE = "";
        String see_abinit_ENABLEINFRASTRUCTURE = "";
        String gisela_abinit_ENABLEINFRASTRUCTURE = "";
        String[] infras = new String[6];
        
        String[] lato_abinit_WMS = new String [5];
        
        String[] abinit_INFRASTRUCTURES = 
                portletPreferences.getValues("abinit_ENABLEINFRASTRUCTURE", new String[5]);
        
        for (int i=0; i<abinit_INFRASTRUCTURES.length; i++) {            
            if (abinit_INFRASTRUCTURES[i]!=null && abinit_INFRASTRUCTURES[i].equals("lato")) 
                { lato_abinit_ENABLEINFRASTRUCTURE = "checked"; log.info ("\n LATO!"); }
            if (abinit_INFRASTRUCTURES[i]!=null && abinit_INFRASTRUCTURES[i].equals("garuda")) 
                { garuda_abinit_ENABLEINFRASTRUCTURE = "checked"; log.info ("\n GARUDA!"); }
            if (abinit_INFRASTRUCTURES[i]!=null && abinit_INFRASTRUCTURES[i].equals("eumed")) 
                { eumed_abinit_ENABLEINFRASTRUCTURE = "checked"; log.info ("\n EUMED!"); }
            if (abinit_INFRASTRUCTURES[i]!=null && abinit_INFRASTRUCTURES[i].equals("sagrid")) 
                { sagrid_abinit_ENABLEINFRASTRUCTURE = "checked"; log.info ("\n SAGRID!"); }
            if (abinit_INFRASTRUCTURES[i]!=null && abinit_INFRASTRUCTURES[i].equals("see")) 
                { see_abinit_ENABLEINFRASTRUCTURE = "checked"; log.info ("\n SEE!"); }
            if (abinit_INFRASTRUCTURES[i]!=null && abinit_INFRASTRUCTURES[i].equals("gisela")) 
                { gisela_abinit_ENABLEINFRASTRUCTURE = "checked"; log.info ("\n GISELA!"); }
        }
        
        // Getting the ABINIT ENABLEINFRASTRUCTURE from the portlet preferences
        //abinit_ENABLEINFRASTRUCTURE = portletPreferences.getValue("abinit_ENABLEINFRASTRUCTURE", "NULL");
        // Getting the ABINIT APPID from the portlet preferences
        String abinit_APPID = portletPreferences.getValue("abinit_APPID", "N/A");
        // Getting the LOGLEVEL from the portlet preferences
        String abinit_LOGLEVEL = portletPreferences.getValue("abinit_LOGLEVEL", "INFO");
        // Getting the METADATA METADATA_HOST from the portlet preferences
        String abinit_METADATA_HOST = portletPreferences.getValue("abinit_METADATA_HOST", "N/A");
        // Getting the ABINIT OUTPUT_PATH from the portlet preferences
        String abinit_OUTPUT_PATH = portletPreferences.getValue("abinit_OUTPUT_PATH", "/tmp");
        // Getting the ABINIT SOFTWARE from the portlet preferences
        String abinit_SOFTWARE = portletPreferences.getValue("abinit_SOFTWARE", "N/A");
        // Getting the ABINIT LOCAL_PROXY from the portlet preferences
        String abinit_LOCAL_PROXY = portletPreferences.getValue("abinit_LOCAL_PROXY", "N/A");
        // Getting the TRACKING_DB_HOSTNAME from the portlet preferences
        String TRACKING_DB_HOSTNAME = portletPreferences.getValue("TRACKING_DB_HOSTNAME", "N/A");
        // Getting the TRACKING_DB_USERNAME from the portlet preferences
        String TRACKING_DB_USERNAME = portletPreferences.getValue("TRACKING_DB_USERNAME", "N/A");
        // Getting the TRACKING_DB_PASSWORD from the portlet preferences
        String TRACKING_DB_PASSWORD = portletPreferences.getValue("TRACKING_DB_PASSWORD", "N/A");
        // Getting the SMTP_HOST from the portlet preferences
        String SMTP_HOST = portletPreferences.getValue("SMTP_HOST", "N/A");
        // Getting the SENDER_MAIL from the portlet preferences
        String SENDER_MAIL = portletPreferences.getValue("SENDER_MAIL", "N/A");
        
        if (lato_abinit_ENABLEINFRASTRUCTURE.equals("checked"))
        {
            infras[0]="lato";
            // Getting the ABINIT INFRASTRUCTURE from the portlet preferences for LATO
            String lato_abinit_INFRASTRUCTURE = portletPreferences.getValue("lato_abinit_INFRASTRUCTURE", "N/A");
            // Getting the ABINIT VONAME from the portlet preferences for LATO
            lato_abinit_LOGIN = portletPreferences.getValue("lato_abinit_LOGIN", "N/A");
            // Getting the ABINIT TOPPBDII from the portlet preferences for LATO
            lato_abinit_PASSWD = portletPreferences.getValue("lato_abinit_PASSWD", "N/A");
            // Getting the ABINIT WMS from the portlet preferences for LATO
            lato_abinit_WMS = portletPreferences.getValues("lato_abinit_WMS", new String[5]);
            // Getting the ABINIT ETOKENSERVER from the portlet preferences for LATO
            String lato_abinit_ETOKENSERVER = portletPreferences.getValue("lato_abinit_ETOKENSERVER", "N/A");
            // Getting the ABINIT MYPROXYSERVER from the portlet preferences for LATO
            String lato_abinit_MYPROXYSERVER = portletPreferences.getValue("lato_abinit_MYPROXYSERVER", "N/A");
            // Getting the ABINIT PORT from the portlet preferences for LATO
            String lato_abinit_PORT = portletPreferences.getValue("lato_abinit_PORT", "N/A");
            // Getting the ABINIT ROBOTID from the portlet preferences for LATO
            String lato_abinit_ROBOTID = portletPreferences.getValue("lato_abinit_ROBOTID", "N/A");
            // Getting the ABINIT WEBDAV from the portlet preferences for LATO
            String lato_abinit_WEBDAV = portletPreferences.getValue("lato_abinit_WEBDAV", "N/A");
            // Getting the ABINIT ROLE from the portlet preferences for LATO
            String lato_abinit_ROLE = portletPreferences.getValue("lato_abinit_ROLE", "N/A");
            // Getting the ABINIT RENEWAL from the portlet preferences for LATO
            String lato_abinit_RENEWAL = portletPreferences.getValue("lato_abinit_RENEWAL", "checked");
            // Getting the ABINIT DISABLEVOMS from the portlet preferences for LATO
            String lato_abinit_DISABLEVOMS = portletPreferences.getValue("lato_abinit_DISABLEVOMS", "unchecked");
            
            // Fetching all the WMS Endpoints for LATO
            String lato_WMS = "";
            if (lato_abinit_ENABLEINFRASTRUCTURE.equals("checked")) {
                if (lato_abinit_WMS!=null) {
                    //log.info("length="+lato_abinit_WMS.length);
                    for (int i = 0; i < lato_abinit_WMS.length; i++)
                        if (!(lato_abinit_WMS[i].trim().equals("N/A")) ) 
                            lato_WMS += lato_abinit_WMS[i] + " ";                        
                } else { log.info("WMS not set for LATO!"); lato_abinit_ENABLEINFRASTRUCTURE="unchecked"; }
            }
            
            // Save the portlet preferences
            request.setAttribute("lato_abinit_INFRASTRUCTURE", lato_abinit_INFRASTRUCTURE.trim());
            request.setAttribute("lato_abinit_LOGIN", lato_abinit_LOGIN.trim());
            request.setAttribute("lato_abinit_PASSWD", lato_abinit_PASSWD.trim());
            request.setAttribute("lato_abinit_WMS", lato_WMS);
            request.setAttribute("lato_abinit_ETOKENSERVER", lato_abinit_ETOKENSERVER.trim());
            request.setAttribute("lato_abinit_MYPROXYSERVER", lato_abinit_MYPROXYSERVER.trim());
            request.setAttribute("lato_abinit_PORT", lato_abinit_PORT.trim());
            request.setAttribute("lato_abinit_ROBOTID", lato_abinit_ROBOTID.trim());
            request.setAttribute("lato_abinit_WEBDAV", lato_abinit_WEBDAV.trim());
            request.setAttribute("lato_abinit_ROLE", lato_abinit_ROLE.trim());
            request.setAttribute("lato_abinit_RENEWAL", lato_abinit_RENEWAL);
            request.setAttribute("lato_abinit_DISABLEVOMS", lato_abinit_DISABLEVOMS);
            
            //request.setAttribute("abinit_ENABLEINFRASTRUCTURE", abinit_ENABLEINFRASTRUCTURE);
            request.setAttribute("abinit_APPID", abinit_APPID.trim());
            request.setAttribute("abinit_LOGLEVEL", abinit_LOGLEVEL.trim());
            request.setAttribute("abinit_METADATA_HOST", abinit_METADATA_HOST.trim());
            request.setAttribute("abinit_SOFTWARE", abinit_SOFTWARE.trim());
            request.setAttribute("abinit_LOCAL_PROXY", abinit_LOCAL_PROXY.trim());
            request.setAttribute("TRACKING_DB_HOSTNAME", TRACKING_DB_HOSTNAME.trim());
            request.setAttribute("TRACKING_DB_USERNAME", TRACKING_DB_USERNAME.trim());
            request.setAttribute("TRACKING_DB_PASSWORD", TRACKING_DB_PASSWORD.trim());
            request.setAttribute("SMTP_HOST", SMTP_HOST.trim());
            request.setAttribute("SENDER_MAIL", SENDER_MAIL.trim());
        }
        
        if (garuda_abinit_ENABLEINFRASTRUCTURE.equals("checked"))
        {
            infras[1]="garuda";
            // Getting the ABINIT INFRASTRUCTURE from the portlet preferences for the GARUDA VO
            String garuda_abinit_INFRASTRUCTURE = portletPreferences.getValue("garuda_abinit_INFRASTRUCTURE", "N/A");
            // Getting the ABINIT VONAME from the portlet preferences for the GARUDA VO
            garuda_abinit_VONAME = portletPreferences.getValue("garuda_abinit_VONAME", "N/A");
            // Getting the ABINIT TOPPBDII from the portlet preferences for the GARUDA VO
            garuda_abinit_TOPBDII = portletPreferences.getValue("garuda_abinit_TOPBDII", "N/A");
            // Getting the ABINIT WMS from the portlet preferences for the GARUDA VO
            String[] garuda_abinit_WMS = portletPreferences.getValues("garuda_abinit_WMS", new String[5]);
            // Getting the ABINIT ETOKENSERVER from the portlet preferences for the GARUDA VO
            String garuda_abinit_ETOKENSERVER = portletPreferences.getValue("garuda_abinit_ETOKENSERVER", "N/A");
            // Getting the ABINIT MYPROXYSERVER from the portlet preferences for the GARUDA VO
            String garuda_abinit_MYPROXYSERVER = portletPreferences.getValue("garuda_abinit_MYPROXYSERVER", "N/A");
            // Getting the ABINIT PORT from the portlet preferences for the GARUDA VO
            String garuda_abinit_PORT = portletPreferences.getValue("garuda_abinit_PORT", "N/A");
            // Getting the ABINIT ROBOTID from the portlet preferences for the GARUDA VO
            String garuda_abinit_ROBOTID = portletPreferences.getValue("garuda_abinit_ROBOTID", "N/A");
            // Getting the ABINIT WEBDAV from the portlet preferences for the GARUDA VO
            String garuda_abinit_WEBDAV = portletPreferences.getValue("garuda_abinit_WEBDAV", "N/A");
            // Getting the ABINIT ROLE from the portlet preferences for the GARUDA VO
            String garuda_abinit_ROLE = portletPreferences.getValue("garuda_abinit_ROLE", "N/A");
            // Getting the ABINIT RENEWAL from the portlet preferences for the GARUDA VO
            String garuda_abinit_RENEWAL = portletPreferences.getValue("garuda_abinit_RENEWAL", "checked");
            // Getting the ABINIT DISABLEVOMS from the portlet preferences for the GARUDA VO
            String garuda_abinit_DISABLEVOMS = portletPreferences.getValue("garuda_abinit_DISABLEVOMS", "unchecked");
            
            // Fetching all the WMS Endpoints for the GARUDA VO
            String garuda_WMS = "";
            if (garuda_abinit_ENABLEINFRASTRUCTURE.equals("checked")) {            
                if (garuda_abinit_WMS!=null) {
                    //log.info("length="+garuda_abinit_WMS.length);
                    for (int i = 0; i < garuda_abinit_WMS.length; i++)
                        if (!(garuda_abinit_WMS[i].trim().equals("N/A")) ) 
                            garuda_WMS += garuda_abinit_WMS[i] + " ";                        
                } else { log.info("WMS not set for GARUDA!"); garuda_abinit_ENABLEINFRASTRUCTURE="unchecked"; }
            }
            
            // Save the portlet preferences
            request.setAttribute("garuda_abinit_INFRASTRUCTURE", garuda_abinit_INFRASTRUCTURE.trim());
            request.setAttribute("garuda_abinit_VONAME", garuda_abinit_VONAME.trim());
            request.setAttribute("garuda_abinit_TOPBDII", garuda_abinit_TOPBDII.trim());
            request.setAttribute("garuda_abinit_WMS", garuda_WMS);
            request.setAttribute("garuda_abinit_ETOKENSERVER", garuda_abinit_ETOKENSERVER.trim());
            request.setAttribute("garuda_abinit_MYPROXYSERVER", garuda_abinit_MYPROXYSERVER.trim());
            request.setAttribute("garuda_abinit_PORT", garuda_abinit_PORT.trim());
            request.setAttribute("garuda_abinit_ROBOTID", garuda_abinit_ROBOTID.trim());
            request.setAttribute("garuda_abinit_WEBDAV", garuda_abinit_WEBDAV.trim());
            request.setAttribute("garuda_abinit_ROLE", garuda_abinit_ROLE.trim());
            request.setAttribute("garuda_abinit_RENEWAL", garuda_abinit_RENEWAL);
            request.setAttribute("garuda_abinit_DISABLEVOMS", garuda_abinit_DISABLEVOMS);
            
            //request.setAttribute("abinit_ENABLEINFRASTRUCTURE", abinit_ENABLEINFRASTRUCTURE);
            request.setAttribute("abinit_APPID", abinit_APPID.trim());
            request.setAttribute("abinit_LOGLEVEL", abinit_LOGLEVEL.trim());
            request.setAttribute("abinit_METADATA_HOST", abinit_METADATA_HOST.trim());
            request.setAttribute("abinit_OUTPUT_PATH", abinit_OUTPUT_PATH.trim());
            request.setAttribute("abinit_SOFTWARE", abinit_SOFTWARE.trim());
            request.setAttribute("abinit_LOCAL_PROXY", abinit_LOCAL_PROXY.trim());
            request.setAttribute("TRACKING_DB_HOSTNAME", TRACKING_DB_HOSTNAME.trim());
            request.setAttribute("TRACKING_DB_USERNAME", TRACKING_DB_USERNAME.trim());
            request.setAttribute("TRACKING_DB_PASSWORD", TRACKING_DB_PASSWORD.trim());
            request.setAttribute("SMTP_HOST", SMTP_HOST.trim());
            request.setAttribute("SENDER_MAIL", SENDER_MAIL.trim());
        }
        
        if (eumed_abinit_ENABLEINFRASTRUCTURE.equals("checked"))
        {
            infras[2]="eumed";
            // Getting the ABINIT INFRASTRUCTURE from the portlet preferences for the EUMED VO
            String eumed_abinit_INFRASTRUCTURE = portletPreferences.getValue("eumed_abinit_INFRASTRUCTURE", "N/A");
            // Getting the ABINIT VONAME from the portlet preferences for the EUMED VO
            eumed_abinit_VONAME = portletPreferences.getValue("eumed_abinit_VONAME", "N/A");
            // Getting the ABINIT TOPPBDII from the portlet preferences for the EUMED VO
            eumed_abinit_TOPBDII = portletPreferences.getValue("eumed_abinit_TOPBDII", "N/A");
            // Getting the ABINIT WMS from the portlet preferences for the EUMED VO
            String[] eumed_abinit_WMS = portletPreferences.getValues("eumed_abinit_WMS", new String[5]);
            // Getting the ABINIT ETOKENSERVER from the portlet preferences for the EUMED VO
            String eumed_abinit_ETOKENSERVER = portletPreferences.getValue("eumed_abinit_ETOKENSERVER", "N/A");
            // Getting the ABINIT MYPROXYSERVER from the portlet preferences for the EUMED VO
            String eumed_abinit_MYPROXYSERVER = portletPreferences.getValue("eumed_abinit_MYPROXYSERVER", "N/A");
            // Getting the ABINIT PORT from the portlet preferences for the EUMED VO
            String eumed_abinit_PORT = portletPreferences.getValue("eumed_abinit_PORT", "N/A");
            // Getting the ABINIT ROBOTID from the portlet preferences for the EUMED VO
            String eumed_abinit_ROBOTID = portletPreferences.getValue("eumed_abinit_ROBOTID", "N/A");
            // Getting the ABINIT WEBDAV from the portlet preferences for the EUMED VO
            String eumed_abinit_WEBDAV = portletPreferences.getValue("eumed_abinit_WEBDAV", "N/A");
            // Getting the ABINIT ROLE from the portlet preferences for the EUMED VO
            String eumed_abinit_ROLE = portletPreferences.getValue("eumed_abinit_ROLE", "N/A");
            // Getting the ABINIT RENEWAL from the portlet preferences for the EUMED VO
            String eumed_abinit_RENEWAL = portletPreferences.getValue("eumed_abinit_RENEWAL", "checked");
            // Getting the ABINIT DISABLEVOMS from the portlet preferences for the EUMED VO
            String eumed_abinit_DISABLEVOMS = portletPreferences.getValue("eumed_abinit_DISABLEVOMS", "unchecked");
                                    
            // Fetching all the WMS Endpoints for the EUMED VO
            String eumed_WMS = "";
            if (eumed_abinit_ENABLEINFRASTRUCTURE.equals("checked")) {            
                if (eumed_abinit_WMS!=null) {
                    //log.info("length="+eumed_abinit_WMS.length);
                    for (int i = 0; i < eumed_abinit_WMS.length; i++)
                        if (!(eumed_abinit_WMS[i].trim().equals("N/A")) ) 
                            eumed_WMS += eumed_abinit_WMS[i] + " ";                        
                } else { log.info("WMS not set for EUMED!"); eumed_abinit_ENABLEINFRASTRUCTURE="unchecked"; }
            }
            
            // Save the portlet preferences
            request.setAttribute("eumed_abinit_INFRASTRUCTURE", eumed_abinit_INFRASTRUCTURE.trim());
            request.setAttribute("eumed_abinit_VONAME", eumed_abinit_VONAME.trim());
            request.setAttribute("eumed_abinit_TOPBDII", eumed_abinit_TOPBDII.trim());
            request.setAttribute("eumed_abinit_WMS", eumed_WMS);
            request.setAttribute("eumed_abinit_ETOKENSERVER", eumed_abinit_ETOKENSERVER.trim());
            request.setAttribute("eumed_abinit_MYPROXYSERVER", eumed_abinit_MYPROXYSERVER.trim());
            request.setAttribute("eumed_abinit_PORT", eumed_abinit_PORT.trim());
            request.setAttribute("eumed_abinit_ROBOTID", eumed_abinit_ROBOTID.trim());
            request.setAttribute("eumed_abinit_WEBDAV", eumed_abinit_WEBDAV.trim());
            request.setAttribute("eumed_abinit_ROLE", eumed_abinit_ROLE.trim());
            request.setAttribute("eumed_abinit_RENEWAL", eumed_abinit_RENEWAL);
            request.setAttribute("eumed_abinit_DISABLEVOMS", eumed_abinit_DISABLEVOMS);

            //request.setAttribute("abinit_ENABLEINFRASTRUCTURE", abinit_ENABLEINFRASTRUCTURE);
            request.setAttribute("abinit_APPID", abinit_APPID.trim());
            request.setAttribute("abinit_LOGLEVEL", abinit_LOGLEVEL.trim());
            request.setAttribute("abinit_METADATA_HOST", abinit_METADATA_HOST.trim());
            request.setAttribute("abinit_OUTPUT_PATH", abinit_OUTPUT_PATH.trim());
            request.setAttribute("abinit_SOFTWARE", abinit_SOFTWARE.trim());
            request.setAttribute("abinit_LOCAL_PROXY", abinit_LOCAL_PROXY.trim());
            request.setAttribute("TRACKING_DB_HOSTNAME", TRACKING_DB_HOSTNAME.trim());
            request.setAttribute("TRACKING_DB_USERNAME", TRACKING_DB_USERNAME.trim());
            request.setAttribute("TRACKING_DB_PASSWORD", TRACKING_DB_PASSWORD.trim());
            request.setAttribute("SMTP_HOST", SMTP_HOST.trim());
            request.setAttribute("SENDER_MAIL", SENDER_MAIL.trim());
        }
        
        if (sagrid_abinit_ENABLEINFRASTRUCTURE.equals("checked"))
        {
            infras[3]="sagrid";
            // Getting the ABINIT INFRASTRUCTURE from the portlet preferences for the SAGRID VO
            String sagrid_abinit_INFRASTRUCTURE = portletPreferences.getValue("sagrid_abinit_INFRASTRUCTURE", "N/A");
            // Getting the ABINIT VONAME from the portlet preferences for the SAGRID VO
            sagrid_abinit_VONAME = portletPreferences.getValue("sagrid_abinit_VONAME", "N/A");
            // Getting the ABINIT TOPPBDII from the portlet preferences for the SAGRID VO
            sagrid_abinit_TOPBDII = portletPreferences.getValue("sagrid_abinit_TOPBDII", "N/A");
            // Getting the ABINIT WMS from the portlet preferences for the SAGRID VO
            String[] sagrid_abinit_WMS = portletPreferences.getValues("sagrid_abinit_WMS", new String[5]);
            // Getting the ABINIT ETOKENSERVER from the portlet preferences for the SAGRID VO
            String sagrid_abinit_ETOKENSERVER = portletPreferences.getValue("sagrid_abinit_ETOKENSERVER", "N/A");
            // Getting the ABINIT MYPROXYSERVER from the portlet preferences for the SAGRID VO
            String sagrid_abinit_MYPROXYSERVER = portletPreferences.getValue("sagrid_abinit_MYPROXYSERVER", "N/A");
            // Getting the ABINIT PORT from the portlet preferences for the SAGRID VO
            String sagrid_abinit_PORT = portletPreferences.getValue("sagrid_abinit_PORT", "N/A");
            // Getting the ABINIT ROBOTID from the portlet preferences for the SAGRID VO
            String sagrid_abinit_ROBOTID = portletPreferences.getValue("sagrid_abinit_ROBOTID", "N/A");
            // Getting the ABINIT WEBDAV from the portlet preferences for the SAGRID VO
            String sagrid_abinit_WEBDAV = portletPreferences.getValue("sagrid_abinit_WEBDAV", "N/A");
            // Getting the ABINIT ROLE from the portlet preferences for the SAGRID VO
            String sagrid_abinit_ROLE = portletPreferences.getValue("sagrid_abinit_ROLE", "N/A");
            // Getting the ABINIT RENEWAL from the portlet preferences for the SAGRID VO
            String sagrid_abinit_RENEWAL = portletPreferences.getValue("sagrid_abinit_RENEWAL", "checked");
            // Getting the ABINIT DISABLEVOMS from the portlet preferences for the SAGRID VO
            String sagrid_abinit_DISABLEVOMS = portletPreferences.getValue("sagrid_abinit_DISABLEVOMS", "unchecked");
                                    
            // Fetching all the WMS Endpoints for the EUMED VO
            String sagrid_WMS = "";
            if (sagrid_abinit_ENABLEINFRASTRUCTURE.equals("checked")) {            
                if (sagrid_abinit_WMS!=null) {
                    //log.info("length="+sagrid_abinit_WMS.length);
                    for (int i = 0; i < sagrid_abinit_WMS.length; i++)
                        if (!(sagrid_abinit_WMS[i].trim().equals("N/A")) ) 
                            sagrid_WMS += sagrid_abinit_WMS[i] + " ";                        
                } else { log.info("WMS not set for SAGRID!"); sagrid_abinit_ENABLEINFRASTRUCTURE="unchecked"; }
            }
            
            // Save the portlet preferences
            request.setAttribute("sagrid_abinit_INFRASTRUCTURE", sagrid_abinit_INFRASTRUCTURE.trim());
            request.setAttribute("sagrid_abinit_VONAME", sagrid_abinit_VONAME.trim());
            request.setAttribute("sagrid_abinit_TOPBDII", sagrid_abinit_TOPBDII.trim());
            request.setAttribute("sagrid_abinit_WMS", sagrid_WMS);
            request.setAttribute("sagrid_abinit_ETOKENSERVER", sagrid_abinit_ETOKENSERVER.trim());
            request.setAttribute("sagrid_abinit_MYPROXYSERVER", sagrid_abinit_MYPROXYSERVER.trim());
            request.setAttribute("sagrid_abinit_PORT", sagrid_abinit_PORT.trim());
            request.setAttribute("sagrid_abinit_ROBOTID", sagrid_abinit_ROBOTID.trim());
            request.setAttribute("sagrid_abinit_WEBDAV", sagrid_abinit_WEBDAV.trim());
            request.setAttribute("sagrid_abinit_ROLE", sagrid_abinit_ROLE.trim());
            request.setAttribute("sagrid_abinit_RENEWAL", sagrid_abinit_RENEWAL);
            request.setAttribute("sagrid_abinit_DISABLEVOMS", sagrid_abinit_DISABLEVOMS);

            //request.setAttribute("abinit_ENABLEINFRASTRUCTURE", abinit_ENABLEINFRASTRUCTURE);
            request.setAttribute("abinit_APPID", abinit_APPID.trim());
            request.setAttribute("abinit_LOGLEVEL", abinit_LOGLEVEL.trim());
            request.setAttribute("abinit_METADATA_HOST", abinit_METADATA_HOST.trim());
            request.setAttribute("abinit_OUTPUT_PATH", abinit_OUTPUT_PATH.trim());
            request.setAttribute("abinit_SOFTWARE", abinit_SOFTWARE.trim());
            request.setAttribute("abinit_LOCAL_PROXY", abinit_LOCAL_PROXY.trim());
            request.setAttribute("TRACKING_DB_HOSTNAME", TRACKING_DB_HOSTNAME.trim());
            request.setAttribute("TRACKING_DB_USERNAME", TRACKING_DB_USERNAME.trim());
            request.setAttribute("TRACKING_DB_PASSWORD", TRACKING_DB_PASSWORD.trim());
            request.setAttribute("SMTP_HOST", SMTP_HOST.trim());
            request.setAttribute("SENDER_MAIL", SENDER_MAIL.trim());
        }        

        if (see_abinit_ENABLEINFRASTRUCTURE.equals("checked"))
        {
            infras[4]="see";
            // Getting the ABINIT INFRASTRUCTURE from the portlet preferences for the SEE VO
            String see_abinit_INFRASTRUCTURE = portletPreferences.getValue("see_abinit_INFRASTRUCTURE", "N/A");
            // Getting the ABINIT VONAME from the portlet preferences for the see VO
            see_abinit_VONAME = portletPreferences.getValue("see_abinit_VONAME", "N/A");
            // Getting the ABINIT TOPPBDII from the portlet preferences for the SEE VO
            see_abinit_TOPBDII = portletPreferences.getValue("see_abinit_TOPBDII", "N/A");
            // Getting the ABINIT WMS from the portlet preferences for the SEE VO
            String[] see_abinit_WMS = portletPreferences.getValues("see_abinit_WMS", new String[5]);
            // Getting the ABINIT ETOKENSERVER from the portlet preferences for the SEE VO
            String see_abinit_ETOKENSERVER = portletPreferences.getValue("see_abinit_ETOKENSERVER", "N/A");
            // Getting the ABINIT MYPROXYSERVER from the portlet preferences for the SEE VO
            String see_abinit_MYPROXYSERVER = portletPreferences.getValue("see_abinit_MYPROXYSERVER", "N/A");
            // Getting the ABINIT PORT from the portlet preferences for the SEE VO
            String see_abinit_PORT = portletPreferences.getValue("see_abinit_PORT", "N/A");
            // Getting the ABINIT ROBOTID from the portlet preferences for the SEE VO
            String see_abinit_ROBOTID = portletPreferences.getValue("see_abinit_ROBOTID", "N/A");
            // Getting the ABINIT WEBDAV from the portlet preferences for the SEE VO
            String see_abinit_WEBDAV = portletPreferences.getValue("see_abinit_WEBDAV", "N/A");
            // Getting the ABINIT ROLE from the portlet preferences for the SEE VO
            String see_abinit_ROLE = portletPreferences.getValue("see_abinit_ROLE", "N/A");
            // Getting the ABINIT RENEWAL from the portlet preferences for the SEE VO
            String see_abinit_RENEWAL = portletPreferences.getValue("see_abinit_RENEWAL", "checked");
            // Getting the ABINIT DISABLEVOMS from the portlet preferences for the SEE VO
            String see_abinit_DISABLEVOMS = portletPreferences.getValue("see_abinit_DISABLEVOMS", "unchecked");              
            
            // Fetching all the WMS Endpoints for the SEE VO
            String see_WMS = "";
            if (see_abinit_ENABLEINFRASTRUCTURE.equals("checked")) {            
                if (see_abinit_WMS!=null) {
                    //log.info("length="+see_abinit_WMS.length);
                    for (int i = 0; i < see_abinit_WMS.length; i++)
                        if (!(see_abinit_WMS[i].trim().equals("N/A")) ) 
                            see_WMS += see_abinit_WMS[i] + " ";                        
                } else { log.info("WMS not set for SEE!"); see_abinit_ENABLEINFRASTRUCTURE="unchecked"; }
            }
            
            // Save the portlet preferences
            request.setAttribute("see_abinit_INFRASTRUCTURE", see_abinit_INFRASTRUCTURE.trim());
            request.setAttribute("see_abinit_VONAME", see_abinit_VONAME.trim());
            request.setAttribute("see_abinit_TOPBDII", see_abinit_TOPBDII.trim());
            request.setAttribute("see_abinit_WMS", see_WMS);
            request.setAttribute("see_abinit_ETOKENSERVER", see_abinit_ETOKENSERVER.trim());
            request.setAttribute("see_abinit_MYPROXYSERVER", see_abinit_MYPROXYSERVER.trim());
            request.setAttribute("see_abinit_PORT", see_abinit_PORT.trim());
            request.setAttribute("see_abinit_ROBOTID", see_abinit_ROBOTID.trim());
            request.setAttribute("see_abinit_WEBDAV", see_abinit_WEBDAV.trim());
            request.setAttribute("see_abinit_ROLE", see_abinit_ROLE.trim());
            request.setAttribute("see_abinit_RENEWAL", see_abinit_RENEWAL);
            request.setAttribute("see_abinit_DISABLEVOMS", see_abinit_DISABLEVOMS);

            //request.setAttribute("abinit_ENABLEINFRASTRUCTURE", abinit_ENABLEINFRASTRUCTURE);
            request.setAttribute("abinit_APPID", abinit_APPID.trim());
            request.setAttribute("abinit_LOGLEVEL", abinit_LOGLEVEL.trim());
            request.setAttribute("abinit_METADATA_HOST", abinit_METADATA_HOST.trim());
            request.setAttribute("abinit_OUTPUT_PATH", abinit_OUTPUT_PATH.trim());
            request.setAttribute("abinit_SOFTWARE", abinit_SOFTWARE.trim());
            request.setAttribute("abinit_LOCAL_PROXY", abinit_LOCAL_PROXY.trim());
            request.setAttribute("TRACKING_DB_HOSTNAME", TRACKING_DB_HOSTNAME.trim());
            request.setAttribute("TRACKING_DB_USERNAME", TRACKING_DB_USERNAME.trim());
            request.setAttribute("TRACKING_DB_PASSWORD", TRACKING_DB_PASSWORD.trim());
            request.setAttribute("SMTP_HOST", SMTP_HOST.trim());
            request.setAttribute("SENDER_MAIL", SENDER_MAIL.trim());
        }
        
        if (gisela_abinit_ENABLEINFRASTRUCTURE.equals("checked"))
        {
            infras[5]="gisela";
            // Getting the ABINIT INFRASTRUCTURE from the portlet preferences for the GISELA VO
            String gisela_abinit_INFRASTRUCTURE = portletPreferences.getValue("gisela_abinit_INFRASTRUCTURE", "N/A");
            // Getting the ABINIT VONAME from the portlet preferences for the GISELA VO
            gisela_abinit_VONAME = portletPreferences.getValue("gisela_abinit_VONAME", "N/A");
            // Getting the ABINIT TOPPBDII from the portlet preferences for the GISELA VO
            gisela_abinit_TOPBDII = portletPreferences.getValue("gisela_abinit_TOPBDII", "N/A");
            // Getting the ABINIT WMS from the portlet preferences for the GISELA VO
            String[] gisela_abinit_WMS = portletPreferences.getValues("gisela_abinit_WMS", new String[5]);
            // Getting the ABINIT ETOKENSERVER from the portlet preferences for the GISELA VO
            String gisela_abinit_ETOKENSERVER = portletPreferences.getValue("gisela_abinit_ETOKENSERVER", "N/A");
            // Getting the ABINIT MYPROXYSERVER from the portlet preferences for the GISELA VO
            String gisela_abinit_MYPROXYSERVER = portletPreferences.getValue("gisela_abinit_MYPROXYSERVER", "N/A");
            // Getting the ABINIT PORT from the portlet preferences for the GISELA VO
            String gisela_abinit_PORT = portletPreferences.getValue("gisela_abinit_PORT", "N/A");
            // Getting the ABINIT ROBOTID from the portlet preferences for the GISELA VO
            String gisela_abinit_ROBOTID = portletPreferences.getValue("gisela_abinit_ROBOTID", "N/A");
            // Getting the ABINIT WEBDAV from the portlet preferences for the GISELA VO
            String gisela_abinit_WEBDAV = portletPreferences.getValue("gisela_abinit_WEBDAV", "N/A");
            // Getting the ABINIT ROLE from the portlet preferences for the GISELA VO
            String gisela_abinit_ROLE = portletPreferences.getValue("gisela_abinit_ROLE", "N/A");
            // Getting the ABINIT RENEWAL from the portlet preferences for the GISELA VO
            String gisela_abinit_RENEWAL = portletPreferences.getValue("gisela_abinit_RENEWAL", "checked");
            // Getting the ABINIT DISABLEVOMS from the portlet preferences for the GISELA VO
            String gisela_abinit_DISABLEVOMS = portletPreferences.getValue("gisela_abinit_DISABLEVOMS", "unchecked");
            
            // Fetching all the WMS Endpoints for the GISELA VO
            String gisela_WMS = "";
            if (gisela_abinit_ENABLEINFRASTRUCTURE.equals("checked")) {            
                if (gisela_abinit_WMS!=null) {
                    //log.info("length="+gisela_abinit_WMS.length);
                    for (int i = 0; i < gisela_abinit_WMS.length; i++)
                        if (!(gisela_abinit_WMS[i].trim().equals("N/A")) ) 
                            gisela_WMS += gisela_abinit_WMS[i] + " ";                        
                } else { log.info("WMS not set for GISELA!"); gisela_abinit_ENABLEINFRASTRUCTURE="unchecked"; }
            }
            
            // Save the portlet preferences
            request.setAttribute("gisela_abinit_INFRASTRUCTURE", gisela_abinit_INFRASTRUCTURE.trim());
            request.setAttribute("gisela_abinit_VONAME", gisela_abinit_VONAME.trim());
            request.setAttribute("gisela_abinit_TOPBDII", gisela_abinit_TOPBDII.trim());
            request.setAttribute("gisela_abinit_WMS", gisela_WMS);
            request.setAttribute("gisela_abinit_ETOKENSERVER", gisela_abinit_ETOKENSERVER.trim());
            request.setAttribute("gisela_abinit_MYPROXYSERVER", gisela_abinit_MYPROXYSERVER.trim());
            request.setAttribute("gisela_abinit_PORT", gisela_abinit_PORT.trim());
            request.setAttribute("gisela_abinit_ROBOTID", gisela_abinit_ROBOTID.trim());
            request.setAttribute("gisela_abinit_WEBDAV", gisela_abinit_WEBDAV.trim());
            request.setAttribute("gisela_abinit_ROLE", gisela_abinit_ROLE.trim());
            request.setAttribute("gisela_abinit_RENEWAL", gisela_abinit_RENEWAL);
            request.setAttribute("gisela_abinit_DISABLEVOMS", gisela_abinit_DISABLEVOMS);

            //request.setAttribute("abinit_ENABLEINFRASTRUCTURE", abinit_ENABLEINFRASTRUCTURE);
            request.setAttribute("abinit_APPID", abinit_APPID.trim());
            request.setAttribute("abinit_LOGLEVEL", abinit_LOGLEVEL.trim());
            request.setAttribute("abinit_METADATA_HOST", abinit_METADATA_HOST.trim());
            request.setAttribute("abinit_OUTPUT_PATH", abinit_OUTPUT_PATH.trim());
            request.setAttribute("abinit_SOFTWARE", abinit_SOFTWARE.trim());
            request.setAttribute("abinit_LOCAL_PROXY", abinit_LOCAL_PROXY.trim());
            request.setAttribute("TRACKING_DB_HOSTNAME", TRACKING_DB_HOSTNAME.trim());
            request.setAttribute("TRACKING_DB_USERNAME", TRACKING_DB_USERNAME.trim());
            request.setAttribute("TRACKING_DB_PASSWORD", TRACKING_DB_PASSWORD.trim());
            request.setAttribute("SMTP_HOST", SMTP_HOST.trim());
            request.setAttribute("SENDER_MAIL", SENDER_MAIL.trim());
        }                
        
        // Save in the preferences the list of supported infrastructures 
        request.setAttribute("abinit_ENABLEINFRASTRUCTURE", infras);

        HashMap<String,Properties> GPS_table = new HashMap<String, Properties>();
        HashMap<String,Properties> GPS_queue = new HashMap<String, Properties>();

        // ********************************************************
        List<String> CEqueues_lato = null;        
        List<String> CEqueues_garuda = null;
        List<String> CEqueues_eumed = null;
        List<String> CEqueues_sagrid = null;
        List<String> CEqueues_see = null;
        List<String> CEqueues_gisela = null;
        
        List<String> CEs_list_lato = null;        
        List<String> CEs_list_garuda = null;        
        List<String> CEs_list_eumed = null;
        List<String> CEs_list_sagrid = null;
        List<String> CEs_list_see = null;
        List<String> CEs_list_gisela = null;
        
        List<String> CEs_list_TOT = new ArrayList<String>();
        List<String> CEs_queue_TOT = new ArrayList<String>();
        
        BDII bdii = null;
        
        String[] SOFTWARE_LIST = abinit_SOFTWARE.split(",");
        
        // Scanning the list of resources publishing the SW TAG(s)
        for(String SOFTWARE: SOFTWARE_LIST)
        {

            try {
                if (lato_abinit_ENABLEINFRASTRUCTURE.equals("checked") && 
                   (!lato_abinit_PASSWD.equals("N/A")) ) 
                {
                    if (abinit_LOGLEVEL.trim().equals("VERBOSE"))
                            log.info("-----*FETCHING*THE*<LATO>*RESOURCES*-----");
                    
                    CEs_list_lato = new ArrayList();                    
                    CEqueues_lato = new ArrayList();
                    
                    // Fetching all the WMS Endpoints for LATO                    
                    if (lato_abinit_WMS!=null) {
                        for (int i = 0; i < lato_abinit_WMS.length; i++)
                            if (!(lato_abinit_WMS[i].trim().equals("N/A")) ) {                                    
                                CEqueues_lato.add(lato_abinit_WMS[i].trim());
                                CEs_list_lato.add(lato_abinit_WMS[i].trim().replace("ssh://", ""));                                    
                            }
                    } 
                }
             
                //=========================================================
                // IMPORTANT: THIS FIX IS ONLY TO SHOW GARUDA SITES 
                //            IN THE GOOGLE MAP                
                //=========================================================
                if (garuda_abinit_ENABLEINFRASTRUCTURE.equals("checked") && 
                   (!garuda_abinit_TOPBDII.equals("N/A")) ) 
                {
                    if (abinit_LOGLEVEL.trim().equals("VERBOSE"))
                            log.info("-----*FETCHING*THE*<GARUDA>*RESOURCES*-----");
                    
                    CEs_list_garuda = new ArrayList();
                    CEs_list_garuda.add("xn03.ctsf.cdacb.in");
                    
                    CEqueues_garuda = new ArrayList();
                    CEqueues_garuda.add("gatekeeper://xn03.ctsf.cdacb.in:8443/GW");
                }
                
                if (eumed_abinit_ENABLEINFRASTRUCTURE.equals("checked") && 
                   (!eumed_abinit_TOPBDII.equals("N/A")) ) 
                {
                    if (abinit_LOGLEVEL.trim().equals("VERBOSE"))
                        log.info("-----*FETCHING*THE*<EUMED>*RESOURCES*-----");
                    
                    bdii = new BDII(new URI(eumed_abinit_TOPBDII));
                    CEs_list_eumed = 
                            getListofCEForSoftwareTag(eumed_abinit_VONAME,
                                                      eumed_abinit_TOPBDII,
                                                      SOFTWARE);
                    
                    CEqueues_eumed = 
                            bdii.queryCEQueues(eumed_abinit_VONAME);
                }
                
                if (sagrid_abinit_ENABLEINFRASTRUCTURE.equals("checked") && 
                   (!sagrid_abinit_TOPBDII.equals("N/A")) ) 
                {
                    if (abinit_LOGLEVEL.trim().equals("VERBOSE"))
                        log.info("-----*FETCHING*THE*<SAGRID>*RESOURCES*-----");
                    
                    bdii = new BDII(new URI(sagrid_abinit_TOPBDII));
                    CEs_list_sagrid = 
                            getListofCEForSoftwareTag(sagrid_abinit_VONAME,
                                                      sagrid_abinit_TOPBDII,
                                                      SOFTWARE);
                    
                    CEqueues_sagrid = 
                            bdii.queryCEQueues(sagrid_abinit_VONAME);
                }
                
                if (see_abinit_ENABLEINFRASTRUCTURE.equals("checked") &&
                   (!see_abinit_TOPBDII.equals("N/A")) ) 
                {
                    if (abinit_LOGLEVEL.trim().equals("VERBOSE"))
                        log.info("-----*FETCHING*THE*<SEE>*RESOURCES*-----");
                    
                    bdii = new BDII(new URI(see_abinit_TOPBDII));
                    CEs_list_see = 
                            getListofCEForSoftwareTag(see_abinit_VONAME,
                                                      see_abinit_TOPBDII,
                                                      SOFTWARE);
                    
                    CEqueues_see = 
                            bdii.queryCEQueues(see_abinit_VONAME);
                }
                
                if (gisela_abinit_ENABLEINFRASTRUCTURE.equals("checked") &&
                   (!gisela_abinit_TOPBDII.equals("N/A")) ) 
                {
                    if (abinit_LOGLEVEL.trim().equals("VERBOSE"))
                        log.info("-----*FETCHING*THE*<GISELA>*RESOURCES*-----");
                    
                    bdii = new BDII(new URI(gisela_abinit_TOPBDII));
                    CEs_list_gisela = 
                            getListofCEForSoftwareTag(gisela_abinit_VONAME,
                                                      gisela_abinit_TOPBDII,
                                                      SOFTWARE);
                    
                    CEqueues_gisela = 
                            bdii.queryCEQueues(gisela_abinit_VONAME);
                }
                
                // Merging the list of CEs and queues                
                if (lato_abinit_ENABLEINFRASTRUCTURE.equals("checked"))
                        CEs_list_TOT.addAll(CEs_list_lato);
                if (garuda_abinit_ENABLEINFRASTRUCTURE.equals("checked"))                        
                        CEs_list_TOT.addAll(CEs_list_garuda);
                if (eumed_abinit_ENABLEINFRASTRUCTURE.equals("checked"))
                        CEs_list_TOT.addAll(CEs_list_eumed);
                if (sagrid_abinit_ENABLEINFRASTRUCTURE.equals("checked"))
                        CEs_list_TOT.addAll(CEs_list_sagrid);
                if (see_abinit_ENABLEINFRASTRUCTURE.equals("checked"))
                        CEs_list_TOT.addAll(CEs_list_see);
                if (gisela_abinit_ENABLEINFRASTRUCTURE.equals("checked"))
                        CEs_list_TOT.addAll(CEs_list_gisela);
                                
                if (lato_abinit_ENABLEINFRASTRUCTURE.equals("checked"))
                    CEs_queue_TOT.addAll(CEqueues_lato);
                if (garuda_abinit_ENABLEINFRASTRUCTURE.equals("checked"))
                    CEs_queue_TOT.addAll(CEqueues_garuda);
                if (eumed_abinit_ENABLEINFRASTRUCTURE.equals("checked"))
                    CEs_queue_TOT.addAll(CEqueues_eumed);
                if (sagrid_abinit_ENABLEINFRASTRUCTURE.equals("checked"))
                    CEs_queue_TOT.addAll(CEqueues_sagrid);
                if (see_abinit_ENABLEINFRASTRUCTURE.equals("checked"))
                    CEs_queue_TOT.addAll(CEqueues_see);
                if (gisela_abinit_ENABLEINFRASTRUCTURE.equals("checked"))
                    CEs_queue_TOT.addAll(CEqueues_gisela);
                
            } catch (URISyntaxException ex) {
               Logger.getLogger(Abinit.class.getName()).log(Level.SEVERE, null, ex);
            } catch (NamingException e) {}
        } // fine list SW tag
                                
        //=========================================================
        // IMPORTANT: INSTANCIATE THE UsersTrackingDBInterface
        //            CLASS USING THE EMPTY CONSTRUCTOR WHEN
        //            WHEN THE PORTLET IS DEPLOYED IN PRODUCTION!!!
        //=========================================================
        /*UsersTrackingDBInterface DBInterface =
            new UsersTrackingDBInterface(
                TRACKING_DB_HOSTNAME.trim(),
                TRACKING_DB_USERNAME.trim(),
                TRACKING_DB_PASSWORD.trim());*/
                
         UsersTrackingDBInterface DBInterface =
            new UsersTrackingDBInterface();
                    
         if ( (CEs_list_TOT != null) && (!CEs_list_TOT.isEmpty()) )
         {
            log.info("NOT EMPTY LIST!");
            // Fetching the list of CEs publushing the SW
            for (String CE:CEs_list_TOT) 
            {
                log.info("Fetching the CE="+CE);
                Properties coordinates = new Properties();
                Properties queue = new Properties();

                float coords[] = DBInterface.getCECoordinate(CE);                        

                String GPS_LAT = Float.toString(coords[0]);
                String GPS_LNG = Float.toString(coords[1]);

                coordinates.setProperty("LAT", GPS_LAT);
                coordinates.setProperty("LNG", GPS_LNG);

                // Fetching the Queues
                for (String CEqueue:CEs_queue_TOT)
                    if (CEqueue.contains(CE))
                        queue.setProperty("QUEUE", CEqueue);
                        
                // Saving the GPS location in a Java HashMap
                GPS_table.put(CE, coordinates);

                // Saving the queue in a Java HashMap
                GPS_queue.put(CE, queue);
            }
         } else log.info ("EMPTY LIST!");

         // Checking the HashMap
         Set set = GPS_table.entrySet();
         Iterator iter = set.iterator();
         
         while ( iter.hasNext() )
         {
            Map.Entry entry = (Map.Entry)iter.next();
            log.info(" - GPS location of the CE " 
                     + entry.getKey() 
                     + " => " 
                     + entry.getValue());
         }

         // Checking the HashMap
         set = GPS_queue.entrySet();
         iter = set.iterator();
         while ( iter.hasNext() )
         {
            Map.Entry entry = (Map.Entry)iter.next();
            log.info(" - Queue " 
                     + entry.getKey() 
                     + " => " 
                     + entry.getValue());
         }

         Gson gson = new GsonBuilder().create();
         request.setAttribute ("GPS_table", gson.toJson(GPS_table));
         request.setAttribute ("GPS_queue", gson.toJson(GPS_queue));

         // ********************************************************
         dispatcher = getPortletContext().getRequestDispatcher("/view.jsp");       
         dispatcher.include(request, response);
    }

    // The init method will be called when installing for the first time the portlet
    // This is the right time to setup the default values into the preferences
    @Override
    public void init() throws PortletException {
        super.init();
    }

    @Override
    public void processAction(ActionRequest request,
                              ActionResponse response)
                throws PortletException, IOException 
    {
        try {
            String action = "";

            // Getting the action to be processed from the request
            action = request.getParameter("ActionEvent");

            // Determine the username and the email address
            ThemeDisplay themeDisplay = (ThemeDisplay) request.getAttribute(WebKeys.THEME_DISPLAY);        
            User user = themeDisplay.getUser();
            
            String username = user.getScreenName();
            String emailAddress = user.getDisplayEmailAddress();
            
            Company company = PortalUtil.getCompany(request);
                String portal = company.getName();

            PortletPreferences portletPreferences =
                    (PortletPreferences) request.getPreferences();

            if (action.equals("CONFIG_ABINIT_PORTLET")) {
                log.info("\nPROCESS ACTION => " + action);
                
                // Getting the ABINIT APPID from the portlet request
                String abinit_APPID = request.getParameter("abinit_APPID");
                // Getting the LOGLEVEL from the portlet request
                String abinit_LOGLEVEL = request.getParameter("abinit_LOGLEVEL");
                // Getting the ABINIT_METADATA_HOST from the portlet request
                String abinit_METADATA_HOST = request.getParameter("abinit_METADATA_HOST");
                // Getting the ABINIT OUTPUT_PATH from the portlet request
                String abinit_OUTPUT_PATH = request.getParameter("abinit_OUTPUT_PATH");
                // Getting the ABINIT SOFTWARE from the portlet request
                String abinit_SOFTWARE = request.getParameter("abinit_SOFTWARE");
                // Getting the ABINIT LOCAL_PROXY from the portlet request
                String abinit_LOCAL_PROXY = request.getParameter("abinit_LOCAL_PROXY");
                // Getting the TRACKING_DB_HOSTNAME from the portlet request
                String TRACKING_DB_HOSTNAME = request.getParameter("TRACKING_DB_HOSTNAME");
                // Getting the TRACKING_DB_USERNAME from the portlet request
                String TRACKING_DB_USERNAME = request.getParameter("TRACKING_DB_USERNAME");
                // Getting the TRACKING_DB_PASSWORD from the portlet request
                String TRACKING_DB_PASSWORD = request.getParameter("TRACKING_DB_PASSWORD");
                // Getting the SMTP_HOST from the portlet request
                String SMTP_HOST = request.getParameter("SMTP_HOST");
                // Getting the SENDER_MAIL from the portlet request
                String SENDER_MAIL = request.getParameter("SENDER_MAIL");
                String[] infras = new String[6];
                
                String lato_abinit_ENABLEINFRASTRUCTURE = "unchecked";
                String garuda_abinit_ENABLEINFRASTRUCTURE = "unchecked";
                String eumed_abinit_ENABLEINFRASTRUCTURE = "unchecked";
                String sagrid_abinit_ENABLEINFRASTRUCTURE = "unchecked";
                String see_abinit_ENABLEINFRASTRUCTURE = "unchecked";
                String gisela_abinit_ENABLEINFRASTRUCTURE = "unchecked";
                
                String[] abinit_INFRASTRUCTURES = request.getParameterValues("abinit_ENABLEINFRASTRUCTURES");         

                if (abinit_INFRASTRUCTURES != null) {
                    Arrays.sort(abinit_INFRASTRUCTURES);
                    lato_abinit_ENABLEINFRASTRUCTURE =
                        Arrays.binarySearch(abinit_INFRASTRUCTURES, "lato") >= 0 ? "checked" : "unchecked";
                    garuda_abinit_ENABLEINFRASTRUCTURE =
                        Arrays.binarySearch(abinit_INFRASTRUCTURES, "garuda") >= 0 ? "checked" : "unchecked";
                    eumed_abinit_ENABLEINFRASTRUCTURE =
                        Arrays.binarySearch(abinit_INFRASTRUCTURES, "eumed") >= 0 ? "checked" : "unchecked";
                    sagrid_abinit_ENABLEINFRASTRUCTURE =
                        Arrays.binarySearch(abinit_INFRASTRUCTURES, "sagrid") >= 0 ? "checked" : "unchecked";
                    see_abinit_ENABLEINFRASTRUCTURE =
                        Arrays.binarySearch(abinit_INFRASTRUCTURES, "see") >= 0 ? "checked" : "unchecked";
                    gisela_abinit_ENABLEINFRASTRUCTURE =
                        Arrays.binarySearch(abinit_INFRASTRUCTURES, "gisela") >= 0 ? "checked" : "unchecked";
                }           
                
                if (lato_abinit_ENABLEINFRASTRUCTURE.equals("checked"))
                {
                    infras[0]="lato";
                     // Getting the ABINIT INFRASTRUCTURE from the portlet request for LATO
                    String lato_abinit_INFRASTRUCTURE = request.getParameter("lato_abinit_INFRASTRUCTURE");
                    // Getting the ABINIT VONAME from the portlet request for LATO
                    String lato_abinit_LOGIN = request.getParameter("lato_abinit_LOGIN");
                    // Getting the ABINIT TOPBDII from the portlet request for LATO
                    String lato_abinit_PASSWD = request.getParameter("lato_abinit_PASSWD");
                    // Getting the ABINIT WMS from the portlet request for LATO
                    String[] lato_abinit_WMS = request.getParameterValues("lato_abinit_WMS");
                    // Getting the ABINIT ETOKENSERVER from the portlet request for LATO
                    String lato_abinit_ETOKENSERVER = request.getParameter("lato_abinit_ETOKENSERVER");
                    // Getting the ABINIT MYPROXYSERVER from the portlet request for LATO
                    String lato_abinit_MYPROXYSERVER = request.getParameter("lato_abinit_MYPROXYSERVER");
                    // Getting the ABINIT PORT from the portlet request for LATO
                    String lato_abinit_PORT = request.getParameter("lato_abinit_PORT");
                    // Getting the ABINIT ROBOTID from the portlet request for LATO
                    String lato_abinit_ROBOTID = request.getParameter("lato_abinit_ROBOTID");
                    // Getting the ABINIT WEBDAV from the portlet request for LATO
                    String lato_abinit_WEBDAV = request.getParameter("lato_abinit_WEBDAV");
                    // Getting the ABINIT ROLE from the portlet request for LATO
                    String lato_abinit_ROLE = request.getParameter("lato_abinit_ROLE");
                    // Getting the ABINIT OPTIONS from the portlet request for LATO
                    String[] lato_abinit_OPTIONS = request.getParameterValues("lato_abinit_OPTIONS");

                    String lato_abinit_RENEWAL = "";
                    String lato_abinit_DISABLEVOMS = "";

                    if (lato_abinit_OPTIONS == null){
                        lato_abinit_RENEWAL = "checked";
                        lato_abinit_DISABLEVOMS = "unchecked";
                    } else {
                        Arrays.sort(lato_abinit_OPTIONS);
                        // Getting the ABINIT RENEWAL from the portlet preferences for LATO
                        lato_abinit_RENEWAL = Arrays.binarySearch(lato_abinit_OPTIONS, "enableRENEWAL") >= 0 ? "checked" : "unchecked";
                        // Getting the ABINIT DISABLEVOMS from the portlet preferences for LATO
                        lato_abinit_DISABLEVOMS = Arrays.binarySearch(lato_abinit_OPTIONS, "disableVOMS") >= 0 ? "checked" : "unchecked";
                    }
                    
                    int nmax=0;                
                    for (int i = 0; i < lato_abinit_WMS.length; i++)
                        if ( lato_abinit_WMS[i]!=null && (!lato_abinit_WMS[i].trim().equals("N/A")) )                        
                            nmax++;
                    
                    log.info("\n\nLength="+nmax);
                    String[] lato_abinit_WMS_trimmed = new String[nmax];
                    for (int i = 0; i < nmax; i++)
                    {
                        lato_abinit_WMS_trimmed[i]=lato_abinit_WMS[i].trim();
                        log.info ("\n\nLATO [" + i + "] WMS=[" + lato_abinit_WMS_trimmed[i] + "]");
                    }
                    
                    // Set the portlet preferences
                    portletPreferences.setValue("lato_abinit_INFRASTRUCTURE", lato_abinit_INFRASTRUCTURE.trim());
                    portletPreferences.setValue("lato_abinit_LOGIN", lato_abinit_LOGIN.trim());
                    portletPreferences.setValue("lato_abinit_PASSWD", lato_abinit_PASSWD.trim());
                    portletPreferences.setValues("lato_abinit_WMS", lato_abinit_WMS_trimmed);
                    portletPreferences.setValue("lato_abinit_ETOKENSERVER", lato_abinit_ETOKENSERVER.trim());
                    portletPreferences.setValue("lato_abinit_MYPROXYSERVER", lato_abinit_MYPROXYSERVER.trim());
                    portletPreferences.setValue("lato_abinit_PORT", lato_abinit_PORT.trim());
                    portletPreferences.setValue("lato_abinit_ROBOTID", lato_abinit_ROBOTID.trim());
                    portletPreferences.setValue("lato_abinit_WEBDAV", lato_abinit_WEBDAV.trim());
                    portletPreferences.setValue("lato_abinit_ROLE", lato_abinit_ROLE.trim());
                    portletPreferences.setValue("lato_abinit_RENEWAL", lato_abinit_RENEWAL);
                    portletPreferences.setValue("lato_abinit_DISABLEVOMS", lato_abinit_DISABLEVOMS);                
                    
                    portletPreferences.setValue("abinit_APPID", abinit_APPID.trim());
                    portletPreferences.setValue("abinit_LOGLEVEL", abinit_LOGLEVEL.trim());
                    portletPreferences.setValue("abinit_METADATA_HOST", abinit_METADATA_HOST.trim());
                    portletPreferences.setValue("abinit_OUTPUT_PATH", abinit_OUTPUT_PATH.trim());
                    portletPreferences.setValue("abinit_SOFTWARE", abinit_SOFTWARE.trim());
                    portletPreferences.setValue("abinit_LOCAL_PROXY", abinit_LOCAL_PROXY.trim());
                    portletPreferences.setValue("TRACKING_DB_HOSTNAME", TRACKING_DB_HOSTNAME.trim());
                    portletPreferences.setValue("TRACKING_DB_USERNAME", TRACKING_DB_USERNAME.trim());
                    portletPreferences.setValue("TRACKING_DB_PASSWORD", TRACKING_DB_PASSWORD.trim());
                    portletPreferences.setValue("SMTP_HOST", SMTP_HOST.trim());
                    portletPreferences.setValue("SENDER_MAIL", SENDER_MAIL.trim());
                    
                    if (abinit_LOGLEVEL.trim().equals("VERBOSE")) {
                    log.info("\n\nPROCESS ACTION => " + action
                        + "\n- Storing the ABINIT portlet preferences ..."
                        + "\nlato_abinit_INFRASTRUCTURE: " + lato_abinit_INFRASTRUCTURE
                        + "\nlato_abinit_LOGIN: " + lato_abinit_LOGIN
                        + "\nlato_abinit_PASSWD: " + lato_abinit_PASSWD                    
                        + "\nlato_abinit_ETOKENSERVER: " + lato_abinit_ETOKENSERVER
                        + "\nlato_abinit_MYPROXYSERVER: " + lato_abinit_MYPROXYSERVER
                        + "\nlato_abinit_PORT: " + lato_abinit_PORT
                        + "\nlato_abinit_ROBOTID: " + lato_abinit_ROBOTID
                        + "\nlato_abinit_WEBDAV: " + lato_abinit_WEBDAV
                        + "\nlato_abinit_ROLE: " + lato_abinit_ROLE
                        + "\nlato_abinit_RENEWAL: " + lato_abinit_RENEWAL
                        + "\nlato_abinit_DISABLEVOMS: " + lato_abinit_DISABLEVOMS
                            
                        + "\n\nabinit_ENABLEINFRASTRUCTURE: " + "lato"
                        + "\nabinit_APPID: " + abinit_APPID
                        + "\nabinit_LOGLEVEL: " + abinit_LOGLEVEL
                        + "\nabinit_METADATA_HOSTL: " + abinit_METADATA_HOST
                        + "\nabinit_OUTPUT_PATH: " + abinit_OUTPUT_PATH
                        + "\nabinit_SOFTWARE: " + abinit_SOFTWARE
                        + "\nabinit_LOCAL_PROXY: " + abinit_LOCAL_PROXY
                        + "\nTracking_DB_Hostname: " + TRACKING_DB_HOSTNAME
                        + "\nTracking_DB_Username: " + TRACKING_DB_USERNAME
                        + "\nTracking_DB_Password: " + TRACKING_DB_PASSWORD
                        + "\nSMTP_HOST: " + SMTP_HOST
                        + "\nSENDER_MAIL: " + SENDER_MAIL);
                    }
                }

                if (garuda_abinit_ENABLEINFRASTRUCTURE.equals("checked"))
                {
                    infras[1]="garuda";
                     // Getting the ABINIT INFRASTRUCTURE from the portlet request for the GARUDA VO
                    String garuda_abinit_INFRASTRUCTURE = request.getParameter("garuda_abinit_INFRASTRUCTURE");
                    // Getting the ABINIT VONAME from the portlet request for the GARUDA VO
                    String garuda_abinit_VONAME = request.getParameter("garuda_abinit_VONAME");
                    // Getting the ABINIT TOPBDII from the portlet request for the GARUDA VO
                    String garuda_abinit_TOPBDII = request.getParameter("garuda_abinit_TOPBDII");
                    // Getting the ABINIT WMS from the portlet request for the GARUDA VO
                    String[] garuda_abinit_WMS = request.getParameterValues("garuda_abinit_WMS");
                    // Getting the ABINIT ETOKENSERVER from the portlet request for the GARUDA VO
                    String garuda_abinit_ETOKENSERVER = request.getParameter("garuda_abinit_ETOKENSERVER");
                    // Getting the ABINIT MYPROXYSERVER from the portlet request for the GARUDA VO
                    String garuda_abinit_MYPROXYSERVER = request.getParameter("garuda_abinit_MYPROXYSERVER");
                    // Getting the ABINIT PORT from the portlet request for the GARUDA VO
                    String garuda_abinit_PORT = request.getParameter("garuda_abinit_PORT");
                    // Getting the ABINIT ROBOTID from the portlet request for the GARUDA VO
                    String garuda_abinit_ROBOTID = request.getParameter("garuda_abinit_ROBOTID");
                    // Getting the ABINIT WEBDAV from the portlet request for the GARUDA VO
                    String garuda_abinit_WEBDAV = request.getParameter("garuda_abinit_WEBDAV");
                    // Getting the ABINIT ROLE from the portlet request for the GARUDA VO
                    String garuda_abinit_ROLE = request.getParameter("garuda_abinit_ROLE");
                    // Getting the ABINIT OPTIONS from the portlet request for the GARUDA VO
                    String[] garuda_abinit_OPTIONS = request.getParameterValues("garuda_abinit_OPTIONS");

                    String garuda_abinit_RENEWAL = "";
                    String garuda_abinit_DISABLEVOMS = "";

                    if (garuda_abinit_OPTIONS == null){
                        garuda_abinit_RENEWAL = "checked";
                        garuda_abinit_DISABLEVOMS = "unchecked";
                    } else {
                        Arrays.sort(garuda_abinit_OPTIONS);
                        // Getting the ABINIT RENEWAL from the portlet preferences for the GARUDA VO
                        garuda_abinit_RENEWAL = Arrays.binarySearch(garuda_abinit_OPTIONS, "enableRENEWAL") >= 0 ? "checked" : "unchecked";
                        // Getting the ABINIT DISABLEVOMS from the portlet preferences for the GARUDA VO
                        garuda_abinit_DISABLEVOMS = Arrays.binarySearch(garuda_abinit_OPTIONS, "disableVOMS") >= 0 ? "checked" : "unchecked";
                    }
                    
                    int nmax=0;                
                    for (int i = 0; i < garuda_abinit_WMS.length; i++)
                        if ( garuda_abinit_WMS[i]!=null && (!garuda_abinit_WMS[i].trim().equals("N/A")) )                        
                            nmax++;
                    
                    log.info("\n\nLength="+nmax);
                    String[] garuda_abinit_WMS_trimmed = new String[nmax];
                    for (int i = 0; i < nmax; i++)
                    {
                        garuda_abinit_WMS_trimmed[i]=garuda_abinit_WMS[i].trim();
                        log.info ("\n\nLATO [" + i + "] WMS=[" + garuda_abinit_WMS_trimmed[i] + "]");
                    }
                    
                    // Set the portlet preferences
                    portletPreferences.setValue("garuda_abinit_INFRASTRUCTURE", garuda_abinit_INFRASTRUCTURE.trim());
                    portletPreferences.setValue("garuda_abinit_VONAME", garuda_abinit_VONAME.trim());
                    portletPreferences.setValue("garuda_abinit_TOPBDII", garuda_abinit_TOPBDII.trim());
                    portletPreferences.setValues("garuda_abinit_WMS", garuda_abinit_WMS_trimmed);
                    portletPreferences.setValue("garuda_abinit_ETOKENSERVER", garuda_abinit_ETOKENSERVER.trim());
                    portletPreferences.setValue("garuda_abinit_MYPROXYSERVER", garuda_abinit_MYPROXYSERVER.trim());
                    portletPreferences.setValue("garuda_abinit_PORT", garuda_abinit_PORT.trim());
                    portletPreferences.setValue("garuda_abinit_ROBOTID", garuda_abinit_ROBOTID.trim());
                    portletPreferences.setValue("garuda_abinit_WEBDAV", garuda_abinit_WEBDAV.trim());
                    portletPreferences.setValue("garuda_abinit_ROLE", garuda_abinit_ROLE.trim());
                    portletPreferences.setValue("garuda_abinit_RENEWAL", garuda_abinit_RENEWAL);
                    portletPreferences.setValue("garuda_abinit_DISABLEVOMS", garuda_abinit_DISABLEVOMS);                
                    
                    portletPreferences.setValue("abinit_APPID", abinit_APPID.trim());
                    portletPreferences.setValue("abinit_LOGLEVEL", abinit_LOGLEVEL.trim());
                    portletPreferences.setValue("abinit_METADATA_HOST", abinit_METADATA_HOST.trim());
                    portletPreferences.setValue("abinit_OUTPUT_PATH", abinit_OUTPUT_PATH.trim());
                    portletPreferences.setValue("abinit_SOFTWARE", abinit_SOFTWARE.trim());
                    portletPreferences.setValue("abinit_LOCAL_PROXY", abinit_LOCAL_PROXY.trim());
                    portletPreferences.setValue("TRACKING_DB_HOSTNAME", TRACKING_DB_HOSTNAME.trim());
                    portletPreferences.setValue("TRACKING_DB_USERNAME", TRACKING_DB_USERNAME.trim());
                    portletPreferences.setValue("TRACKING_DB_PASSWORD", TRACKING_DB_PASSWORD.trim());
                    portletPreferences.setValue("SMTP_HOST", SMTP_HOST.trim());
                    portletPreferences.setValue("SENDER_MAIL", SENDER_MAIL.trim());
                    
                    if (abinit_LOGLEVEL.trim().equals("VERBOSE")) {
                    log.info("\n\nPROCESS ACTION => " + action
                        + "\n- Storing the ABINIT portlet preferences ..."
                        + "\ngaruda_abinit_INFRASTRUCTURE: " + garuda_abinit_INFRASTRUCTURE
                        + "\ngaruda_abinit_VONAME: " + garuda_abinit_VONAME
                        + "\ngaruda_abinit_TOPBDII: " + garuda_abinit_TOPBDII                    
                        + "\ngaruda_abinit_ETOKENSERVER: " + garuda_abinit_ETOKENSERVER
                        + "\ngaruda_abinit_MYPROXYSERVER: " + garuda_abinit_MYPROXYSERVER
                        + "\ngaruda_abinit_PORT: " + garuda_abinit_PORT
                        + "\ngaruda_abinit_ROBOTID: " + garuda_abinit_ROBOTID
                        + "\ngaruda_abinit_WEBDAV: " + garuda_abinit_WEBDAV
                        + "\ngaruda_abinit_ROLE: " + garuda_abinit_ROLE
                        + "\ngaruda_abinit_RENEWAL: " + garuda_abinit_RENEWAL
                        + "\ngaruda_abinit_DISABLEVOMS: " + garuda_abinit_DISABLEVOMS
                            
                        + "\n\nabinit_ENABLEINFRASTRUCTURE: " + "garuda"
                        + "\nabinit_APPID: " + abinit_APPID
                        + "\nabinit_LOGLEVEL: " + abinit_LOGLEVEL
                        + "\nabinit_METADATA_HOST: " + abinit_METADATA_HOST
                        + "\nabinit_OUTPUT_PATH: " + abinit_OUTPUT_PATH
                        + "\nabinit_SOFTWARE: " + abinit_SOFTWARE
                        + "\nabinit_LOCAL_PROXY: " + abinit_LOCAL_PROXY
                        + "\nTracking_DB_Hostname: " + TRACKING_DB_HOSTNAME
                        + "\nTracking_DB_Username: " + TRACKING_DB_USERNAME
                        + "\nTracking_DB_Password: " + TRACKING_DB_PASSWORD
                        + "\nSMTP_HOST: " + SMTP_HOST
                        + "\nSENDER_MAIL: " + SENDER_MAIL);
                    }
                }

                if (eumed_abinit_ENABLEINFRASTRUCTURE.equals("checked"))
                {
                    infras[2]="eumed";
                    // Getting the ABINIT INFRASTRUCTURE from the portlet request for the EUMED VO
                    String eumed_abinit_INFRASTRUCTURE = request.getParameter("eumed_abinit_INFRASTRUCTURE");
                    // Getting the ABINIT VONAME from the portlet request for the EUMED VO
                    String eumed_abinit_VONAME = request.getParameter("eumed_abinit_VONAME");
                    // Getting the ABINIT TOPBDII from the portlet request for the EUMED VO
                    String eumed_abinit_TOPBDII = request.getParameter("eumed_abinit_TOPBDII");
                    // Getting the ABINIT WMS from the portlet request for the EUMED VO
                    String[] eumed_abinit_WMS = request.getParameterValues("eumed_abinit_WMS");
                    // Getting the ABINIT ETOKENSERVER from the portlet request for the EUMED VO
                    String eumed_abinit_ETOKENSERVER = request.getParameter("eumed_abinit_ETOKENSERVER");
                    // Getting the ABINIT MYPROXYSERVER from the portlet request for the EUMED VO
                    String eumed_abinit_MYPROXYSERVER = request.getParameter("eumed_abinit_MYPROXYSERVER");
                    // Getting the ABINIT PORT from the portlet request for the EUMED VO
                    String eumed_abinit_PORT = request.getParameter("eumed_abinit_PORT");
                    // Getting the ABINIT ROBOTID from the portlet request for the EUMED VO
                    String eumed_abinit_ROBOTID = request.getParameter("eumed_abinit_ROBOTID");
                    // Getting the ABINIT WEBDAV from the portlet request for the EUMED VO
                    String eumed_abinit_WEBDAV = request.getParameter("eumed_abinit_WEBDAV");
                    // Getting the ABINIT ROLE from the portlet request for the EUMED VO
                    String eumed_abinit_ROLE = request.getParameter("eumed_abinit_ROLE");
                    // Getting the ABINIT OPTIONS from the portlet request for the EUMED VO
                    String[] eumed_abinit_OPTIONS = request.getParameterValues("eumed_abinit_OPTIONS");

                    String eumed_abinit_RENEWAL = "";
                    String eumed_abinit_DISABLEVOMS = "";

                    if (eumed_abinit_OPTIONS == null){
                        eumed_abinit_RENEWAL = "checked";
                        eumed_abinit_DISABLEVOMS = "unchecked";
                    } else {
                        Arrays.sort(eumed_abinit_OPTIONS);
                        // Getting the ABINIT RENEWAL from the portlet preferences for the EUMED VO
                        eumed_abinit_RENEWAL = Arrays.binarySearch(eumed_abinit_OPTIONS, "enableRENEWAL") >= 0 ? "checked" : "unchecked";
                        // Getting the ABINIT DISABLEVOMS from the portlet preferences for the GARUDA VO
                        eumed_abinit_DISABLEVOMS = Arrays.binarySearch(eumed_abinit_OPTIONS, "disableVOMS") >= 0 ? "checked" : "unchecked";
                    }
                    
                    int nmax=0;                
                    for (int i = 0; i < eumed_abinit_WMS.length; i++)
                        if ( eumed_abinit_WMS[i]!=null && (!eumed_abinit_WMS[i].trim().equals("N/A")) )                        
                            nmax++;
                    
                    log.info("\n\nLength="+nmax);
                    String[] eumed_abinit_WMS_trimmed = new String[nmax];
                    for (int i = 0; i < nmax; i++)
                    {
                        eumed_abinit_WMS_trimmed[i]=eumed_abinit_WMS[i].trim();
                        log.info ("\n\nEUMED [" + i + "] WMS=[" + eumed_abinit_WMS_trimmed[i] + "]");
                    }
                    
                    // Set the portlet preferences
                    portletPreferences.setValue("eumed_abinit_INFRASTRUCTURE", eumed_abinit_INFRASTRUCTURE.trim());
                    portletPreferences.setValue("eumed_abinit_VONAME", eumed_abinit_VONAME.trim());
                    portletPreferences.setValue("eumed_abinit_TOPBDII", eumed_abinit_TOPBDII.trim());
                    portletPreferences.setValues("eumed_abinit_WMS", eumed_abinit_WMS_trimmed);
                    portletPreferences.setValue("eumed_abinit_ETOKENSERVER", eumed_abinit_ETOKENSERVER.trim());
                    portletPreferences.setValue("eumed_abinit_MYPROXYSERVER", eumed_abinit_MYPROXYSERVER.trim());
                    portletPreferences.setValue("eumed_abinit_PORT", eumed_abinit_PORT.trim());
                    portletPreferences.setValue("eumed_abinit_ROBOTID", eumed_abinit_ROBOTID.trim());
                    portletPreferences.setValue("eumed_abinit_WEBDAV", eumed_abinit_WEBDAV.trim());
                    portletPreferences.setValue("eumed_abinit_ROLE", eumed_abinit_ROLE.trim());
                    portletPreferences.setValue("eumed_abinit_RENEWAL", eumed_abinit_RENEWAL);
                    portletPreferences.setValue("eumed_abinit_DISABLEVOMS", eumed_abinit_DISABLEVOMS); 
                    
                    portletPreferences.setValue("abinit_APPID", abinit_APPID.trim());
                    portletPreferences.setValue("abinit_LOGLEVEL", abinit_LOGLEVEL.trim());
                    portletPreferences.setValue("abinit_METADATA_HOST", abinit_METADATA_HOST.trim());
                    portletPreferences.setValue("abinit_OUTPATH_PATH", abinit_OUTPUT_PATH.trim());
                    portletPreferences.setValue("abinit_SOFTWARE", abinit_SOFTWARE.trim());
                    portletPreferences.setValue("abinit_LOCAL_PROXY", abinit_LOCAL_PROXY.trim());
                    portletPreferences.setValue("TRACKING_DB_HOSTNAME", TRACKING_DB_HOSTNAME.trim());
                    portletPreferences.setValue("TRACKING_DB_USERNAME", TRACKING_DB_USERNAME.trim());
                    portletPreferences.setValue("TRACKING_DB_PASSWORD", TRACKING_DB_PASSWORD.trim());
                    portletPreferences.setValue("SMTP_HOST", SMTP_HOST.trim());
                    portletPreferences.setValue("SENDER_MAIL", SENDER_MAIL.trim());
                    
                    if (abinit_LOGLEVEL.trim().equals("VERBOSE")) {
                    log.info("\n\nPROCESS ACTION => " + action
                        + "\n- Storing the ABINIT portlet preferences ..."                    
                        + "\n\neumed_abinit_INFRASTRUCTURE: " + eumed_abinit_INFRASTRUCTURE
                        + "\neumed_abinit_VONAME: " + eumed_abinit_VONAME
                        + "\neumed_abinit_TOPBDII: " + eumed_abinit_TOPBDII                    
                        + "\neumed_abinit_ETOKENSERVER: " + eumed_abinit_ETOKENSERVER
                        + "\neumed_abinit_MYPROXYSERVER: " + eumed_abinit_MYPROXYSERVER
                        + "\neumed_abinit_PORT: " + eumed_abinit_PORT
                        + "\neumed_abinit_ROBOTID: " + eumed_abinit_ROBOTID
                        + "\neumed_abinit_WEBDAV: " + eumed_abinit_WEBDAV
                        + "\neumed_abinit_ROLE: " + eumed_abinit_ROLE
                        + "\neumed_abinit_RENEWAL: " + eumed_abinit_RENEWAL
                        + "\neumed_abinit_DISABLEVOMS: " + eumed_abinit_DISABLEVOMS

                        + "\n\nabinit_ENABLEINFRASTRUCTURE: " + "eumed"
                        + "\nabinit_APPID: " + abinit_APPID
                        + "\nabinit_LOGLEVEL: " + abinit_LOGLEVEL
                        + "\nabinit_METADATA_HOST: " + abinit_METADATA_HOST
                        + "\nabinit_OUTPUT_PATH: " + abinit_OUTPUT_PATH
                        + "\nabinit_SOFTWARE: " + abinit_SOFTWARE
                        + "\nabinit_LOCAL_PROXY: " + abinit_LOCAL_PROXY
                        + "\nTracking_DB_Hostname: " + TRACKING_DB_HOSTNAME
                        + "\nTracking_DB_Username: " + TRACKING_DB_USERNAME
                        + "\nTracking_DB_Password: " + TRACKING_DB_PASSWORD
                        + "\nSMTP_HOST: " + SMTP_HOST
                        + "\nSENDER_MAIL: " + SENDER_MAIL);
                    }
                }                                
                
                if (sagrid_abinit_ENABLEINFRASTRUCTURE.equals("checked"))
                {
                    infras[3]="sagrid";
                    // Getting the ABINIT INFRASTRUCTURE from the portlet request for the SAGRID VO
                    String sagrid_abinit_INFRASTRUCTURE = request.getParameter("sagrid_abinit_INFRASTRUCTURE");
                    // Getting the ABINIT VONAME from the portlet request for the SAGRID VO
                    String sagrid_abinit_VONAME = request.getParameter("sagrid_abinit_VONAME");
                    // Getting the ABINIT TOPBDII from the portlet request for the SAGRID VO
                    String sagrid_abinit_TOPBDII = request.getParameter("sagrid_abinit_TOPBDII");
                    // Getting the ABINIT WMS from the portlet request for the SAGRID VO
                    String[] sagrid_abinit_WMS = request.getParameterValues("sagrid_abinit_WMS");
                    // Getting the ABINIT ETOKENSERVER from the portlet request for the SAGRID VO
                    String sagrid_abinit_ETOKENSERVER = request.getParameter("sagrid_abinit_ETOKENSERVER");
                    // Getting the ABINIT MYPROXYSERVER from the portlet request for the SAGRID VO
                    String sagrid_abinit_MYPROXYSERVER = request.getParameter("sagrid_abinit_MYPROXYSERVER");
                    // Getting the ABINIT PORT from the portlet request for the SAGRID VO
                    String sagrid_abinit_PORT = request.getParameter("sagrid_abinit_PORT");
                    // Getting the ABINIT ROBOTID from the portlet request for the SAGRID VO
                    String sagrid_abinit_ROBOTID = request.getParameter("sagrid_abinit_ROBOTID");
                    // Getting the ABINIT WEBDAV from the portlet request for the SAGRID VO
                    String sagrid_abinit_WEBDAV = request.getParameter("sagrid_abinit_WEBDAV");
                    // Getting the ABINIT ROLE from the portlet request for the SAGRID VO
                    String sagrid_abinit_ROLE = request.getParameter("sagrid_abinit_ROLE");
                    // Getting the ABINIT OPTIONS from the portlet request for the SAGRID VO
                    String[] sagrid_abinit_OPTIONS = request.getParameterValues("sagrid_abinit_OPTIONS");

                    String sagrid_abinit_RENEWAL = "";
                    String sagrid_abinit_DISABLEVOMS = "";

                    if (sagrid_abinit_OPTIONS == null){
                        sagrid_abinit_RENEWAL = "checked";
                        sagrid_abinit_DISABLEVOMS = "unchecked";
                    } else {
                        Arrays.sort(sagrid_abinit_OPTIONS);
                        // Getting the ABINIT RENEWAL from the portlet preferences for the SAGRID VO
                        sagrid_abinit_RENEWAL = Arrays.binarySearch(sagrid_abinit_OPTIONS, "enableRENEWAL") >= 0 ? "checked" : "unchecked";
                        // Getting the ABINIT DISABLEVOMS from the portlet preferences for the SAGRID VO
                        sagrid_abinit_DISABLEVOMS = Arrays.binarySearch(sagrid_abinit_OPTIONS, "disableVOMS") >= 0 ? "checked" : "unchecked";
                    }
                    
                    int nmax=0;                
                    for (int i = 0; i < sagrid_abinit_WMS.length; i++)
                        if ( sagrid_abinit_WMS[i]!=null && (!sagrid_abinit_WMS[i].trim().equals("N/A")) )                        
                            nmax++;
                    
                    log.info("\n\nLength="+nmax);
                    String[] sagrid_abinit_WMS_trimmed = new String[nmax];
                    for (int i = 0; i < nmax; i++)
                    {
                        sagrid_abinit_WMS_trimmed[i]=sagrid_abinit_WMS[i].trim();
                        log.info ("\n\nSAGRID [" + i + "] WMS=[" + sagrid_abinit_WMS_trimmed[i] + "]");
                    }
                    
                    // Set the portlet preferences
                    portletPreferences.setValue("sagrid_abinit_INFRASTRUCTURE", sagrid_abinit_INFRASTRUCTURE.trim());
                    portletPreferences.setValue("sagrid_abinit_VONAME", sagrid_abinit_VONAME.trim());
                    portletPreferences.setValue("sagrid_abinit_TOPBDII", sagrid_abinit_TOPBDII.trim());
                    portletPreferences.setValues("sagrid_abinit_WMS", sagrid_abinit_WMS_trimmed);
                    portletPreferences.setValue("sagrid_abinit_ETOKENSERVER", sagrid_abinit_ETOKENSERVER.trim());
                    portletPreferences.setValue("sagrid_abinit_MYPROXYSERVER", sagrid_abinit_MYPROXYSERVER.trim());
                    portletPreferences.setValue("sagrid_abinit_PORT", sagrid_abinit_PORT.trim());
                    portletPreferences.setValue("sagrid_abinit_ROBOTID", sagrid_abinit_ROBOTID.trim());
                    portletPreferences.setValue("sagrid_abinit_WEBDAV", sagrid_abinit_WEBDAV.trim());
                    portletPreferences.setValue("sagrid_abinit_ROLE", sagrid_abinit_ROLE.trim());
                    portletPreferences.setValue("sagrid_abinit_RENEWAL", sagrid_abinit_RENEWAL);
                    portletPreferences.setValue("sagrid_abinit_DISABLEVOMS", sagrid_abinit_DISABLEVOMS); 
                    
                    portletPreferences.setValue("abinit_APPID", abinit_APPID.trim());
                    portletPreferences.setValue("abinit_LOGLEVEL", abinit_LOGLEVEL.trim());
                    portletPreferences.setValue("abinit_METADATA_HOST", abinit_METADATA_HOST.trim());
                    portletPreferences.setValue("abinit_OUTPATH_PATH", abinit_OUTPUT_PATH.trim());
                    portletPreferences.setValue("abinit_SOFTWARE", abinit_SOFTWARE.trim());
                    portletPreferences.setValue("abinit_LOCAL_PROXY", abinit_LOCAL_PROXY.trim());
                    portletPreferences.setValue("TRACKING_DB_HOSTNAME", TRACKING_DB_HOSTNAME.trim());
                    portletPreferences.setValue("TRACKING_DB_USERNAME", TRACKING_DB_USERNAME.trim());
                    portletPreferences.setValue("TRACKING_DB_PASSWORD", TRACKING_DB_PASSWORD.trim());
                    portletPreferences.setValue("SMTP_HOST", SMTP_HOST.trim());
                    portletPreferences.setValue("SENDER_MAIL", SENDER_MAIL.trim());
                    
                    if (abinit_LOGLEVEL.trim().equals("VERBOSE")) {
                    log.info("\n\nPROCESS ACTION => " + action
                        + "\n- Storing the ABINIT portlet preferences ..."                    
                        + "\n\nsagrid_abinit_INFRASTRUCTURE: " + sagrid_abinit_INFRASTRUCTURE
                        + "\nsagrid_abinit_VONAME: " + sagrid_abinit_VONAME
                        + "\nsagrid_abinit_TOPBDII: " + sagrid_abinit_TOPBDII                    
                        + "\nsagrid_abinit_ETOKENSERVER: " + sagrid_abinit_ETOKENSERVER
                        + "\nsagrid_abinit_MYPROXYSERVER: " + sagrid_abinit_MYPROXYSERVER
                        + "\nsagrid_abinit_PORT: " + sagrid_abinit_PORT
                        + "\nsagrid_abinit_ROBOTID: " + sagrid_abinit_ROBOTID                            
                        + "\nsagrid_abinit_WEBDAV: " + sagrid_abinit_WEBDAV
                        + "\neumed_abinit_ROLE: " + sagrid_abinit_ROLE
                        + "\nsagrid_abinit_RENEWAL: " + sagrid_abinit_RENEWAL
                        + "\nsagrid_abinit_DISABLEVOMS: " + sagrid_abinit_DISABLEVOMS

                        + "\n\nabinit_ENABLEINFRASTRUCTURE: " + "sagrid"
                        + "\nabinit_APPID: " + abinit_APPID
                        + "\nabinit_LOGLEVEL: " + abinit_LOGLEVEL
                        + "\nabinit_METADATA_HOST: " + abinit_METADATA_HOST
                        + "\nabinit_OUTPUT_PATH: " + abinit_OUTPUT_PATH
                        + "\nabinit_SOFTWARE: " + abinit_SOFTWARE
                        + "\nabinit_LOCAL_PROXY: " + abinit_LOCAL_PROXY
                        + "\nTracking_DB_Hostname: " + TRACKING_DB_HOSTNAME
                        + "\nTracking_DB_Username: " + TRACKING_DB_USERNAME
                        + "\nTracking_DB_Password: " + TRACKING_DB_PASSWORD
                        + "\nSMTP_HOST: " + SMTP_HOST
                        + "\nSENDER_MAIL: " + SENDER_MAIL);
                    }
                }
                
                if (see_abinit_ENABLEINFRASTRUCTURE.equals("checked"))
                {
                    infras[4]="see";
                    // Getting the ABINIT INFRASTRUCTURE from the portlet request for the SEE VO
                    String see_abinit_INFRASTRUCTURE = request.getParameter("see_abinit_INFRASTRUCTURE");
                    // Getting the ABINIT VONAME from the portlet request for the SEE VO
                    String see_abinit_VONAME = request.getParameter("see_abinit_VONAME");
                    // Getting the ABINIT TOPBDII from the portlet request for the SEE VO
                    String see_abinit_TOPBDII = request.getParameter("see_abinit_TOPBDII");
                    // Getting the ABINIT WMS from the portlet request for the SEE VO
                    String[] see_abinit_WMS = request.getParameterValues("see_abinit_WMS");
                    // Getting the ABINIT ETOKENSERVER from the portlet request for the SEE VO
                    String see_abinit_ETOKENSERVER = request.getParameter("see_abinit_ETOKENSERVER");
                    // Getting the ABINIT MYPROXYSERVER from the portlet request for the SEE VO
                    String see_abinit_MYPROXYSERVER = request.getParameter("see_abinit_MYPROXYSERVER");
                    // Getting the ABINIT PORT from the portlet request for the SEE VO
                    String see_abinit_PORT = request.getParameter("see_abinit_PORT");
                    // Getting the ABINIT ROBOTID from the portlet request for the SEE VO
                    String see_abinit_ROBOTID = request.getParameter("see_abinit_ROBOTID");
                    // Getting the ABINIT WEBDAV from the portlet request for the SEE VO
                    String see_abinit_WEBDAV = request.getParameter("see_abinit_WEBDAV");
                    // Getting the ABINIT ROLE from the portlet request for the SEE VO
                    String see_abinit_ROLE = request.getParameter("see_abinit_ROLE");
                    // Getting the ABINIT OPTIONS from the portlet request for the SEE VO
                    String[] see_abinit_OPTIONS = request.getParameterValues("see_abinit_OPTIONS");

                    String see_abinit_RENEWAL = "";
                    String see_abinit_DISABLEVOMS = "";

                    if (see_abinit_OPTIONS == null){
                        see_abinit_RENEWAL = "checked";
                        see_abinit_DISABLEVOMS = "unchecked";
                    } else {
                        Arrays.sort(see_abinit_OPTIONS);
                        // Get the ABINIT RENEWAL from the portlet preferences for the SEE VO
                        see_abinit_RENEWAL = Arrays.binarySearch(see_abinit_OPTIONS, "enableRENEWAL") >= 0 ? "checked" : "unchecked";
                        // Get the ABINIT DISABLEVOMS from the portlet preferences for the SEE VO
                        see_abinit_DISABLEVOMS = Arrays.binarySearch(see_abinit_OPTIONS, "disableVOMS") >= 0 ? "checked" : "unchecked";
                    }
                    
                    int nmax=0;                
                    for (int i = 0; i < see_abinit_WMS.length; i++)
                        if ( see_abinit_WMS[i]!=null && (!see_abinit_WMS[i].trim().equals("N/A")) )                        
                            nmax++;
                    
                    log.info("\n\nLength="+nmax);
                    String[] see_abinit_WMS_trimmed = new String[nmax];
                    for (int i = 0; i < nmax; i++)
                    {
                        see_abinit_WMS_trimmed[i]=see_abinit_WMS[i].trim();
                        log.info ("\n\nSEE [" + i + "] WMS=[" + see_abinit_WMS_trimmed[i] + "]");
                    }

                    // Set the portlet preferences
                    portletPreferences.setValue("see_abinit_INFRASTRUCTURE", see_abinit_INFRASTRUCTURE.trim());
                    portletPreferences.setValue("see_abinit_VONAME", see_abinit_VONAME.trim());
                    portletPreferences.setValue("see_abinit_TOPBDII", see_abinit_TOPBDII.trim());
                    portletPreferences.setValues("see_abinit_WMS", see_abinit_WMS_trimmed);
                    portletPreferences.setValue("see_abinit_ETOKENSERVER", see_abinit_ETOKENSERVER.trim());
                    portletPreferences.setValue("see_abinit_MYPROXYSERVER", see_abinit_MYPROXYSERVER.trim());
                    portletPreferences.setValue("see_abinit_PORT", see_abinit_PORT.trim());
                    portletPreferences.setValue("see_abinit_ROBOTID", see_abinit_ROBOTID.trim());
                    portletPreferences.setValue("see_abinit_WEBDAV", see_abinit_WEBDAV.trim());
                    portletPreferences.setValue("see_abinit_ROLE", see_abinit_ROLE.trim());
                    portletPreferences.setValue("see_abinit_RENEWAL", see_abinit_RENEWAL);
                    portletPreferences.setValue("see_abinit_DISABLEVOMS", see_abinit_DISABLEVOMS);
                    
                    portletPreferences.setValue("abinit_APPID", abinit_APPID.trim());
                    portletPreferences.setValue("abinit_LOGLEVEL", abinit_LOGLEVEL.trim());
                    portletPreferences.setValue("abinit_METADATA_HOST", abinit_METADATA_HOST.trim());
                    portletPreferences.setValue("abinit_OUTPUT_PATH", abinit_OUTPUT_PATH.trim());
                    portletPreferences.setValue("abinit_SOFTWARE", abinit_SOFTWARE.trim());
                    portletPreferences.setValue("abinit_LOCAL_PROXY", abinit_LOCAL_PROXY.trim());
                    portletPreferences.setValue("TRACKING_DB_HOSTNAME", TRACKING_DB_HOSTNAME.trim());
                    portletPreferences.setValue("TRACKING_DB_USERNAME", TRACKING_DB_USERNAME.trim());
                    portletPreferences.setValue("TRACKING_DB_PASSWORD", TRACKING_DB_PASSWORD.trim());
                    portletPreferences.setValue("SMTP_HOST", SMTP_HOST.trim());
                    portletPreferences.setValue("SENDER_MAIL", SENDER_MAIL.trim());
                    
                    if (abinit_LOGLEVEL.trim().equals("VERBOSE")) {
                    log.info("\n\nPROCESS ACTION => " + action
                        + "\n- Storing the ABINIT portlet preferences ..."                    
                        + "\n\nsee_abinit_INFRASTRUCTURE: " + see_abinit_INFRASTRUCTURE
                        + "\nsee_abinit_VONAME: " + see_abinit_VONAME
                        + "\nsee_abinit_TOPBDII: " + see_abinit_TOPBDII                    
                        + "\nsee_abinit_ETOKENSERVER: " + see_abinit_ETOKENSERVER
                        + "\nsee_abinit_MYPROXYSERVER: " + see_abinit_MYPROXYSERVER
                        + "\nsee_abinit_PORT: " + see_abinit_PORT
                        + "\nsee_abinit_ROBOTID: " + see_abinit_ROBOTID
                        + "\nsee_abinit_WEBDAV: " + see_abinit_WEBDAV
                        + "\nsee_abinit_ROLE: " + see_abinit_ROLE
                        + "\nsee_abinit_RENEWAL: " + see_abinit_RENEWAL
                        + "\nsee_abinit_DISABLEVOMS: " + see_abinit_DISABLEVOMS

                        + "\n\nabinit_ENABLEINFRASTRUCTURE: " + "see"
                        + "\nabinit_APPID: " + abinit_APPID
                        + "\nabinit_LOGLEVEL: " + abinit_LOGLEVEL
                        + "\nabinit_METADATA_HOST: " + abinit_METADATA_HOST
                        + "\nabinit_OUTPUT_PATH: " + abinit_OUTPUT_PATH
                        + "\nabinit_SOFTWARE: " + abinit_SOFTWARE
                        + "\nabinit_LOCAL_PROXY: " + abinit_LOCAL_PROXY
                        + "\nTracking_DB_Hostname: " + TRACKING_DB_HOSTNAME
                        + "\nTracking_DB_Username: " + TRACKING_DB_USERNAME
                        + "\nTracking_DB_Password: " + TRACKING_DB_PASSWORD
                        + "\nSMTP_HOST: " + SMTP_HOST
                        + "\nSENDER_MAIL: " + SENDER_MAIL);
                    }
                }
                
                if (gisela_abinit_ENABLEINFRASTRUCTURE.equals("checked"))
                {
                    infras[5]="gisela";
                    // Getting the ABINIT INFRASTRUCTURE from the portlet request for the GISELA VO
                    String gisela_abinit_INFRASTRUCTURE = request.getParameter("gisela_abinit_INFRASTRUCTURE");
                    // Getting the ABINIT VONAME from the portlet request for the GISELA VO
                    String gisela_abinit_VONAME = request.getParameter("gisela_abinit_VONAME");
                    // Getting the ABINIT TOPBDII from the portlet request for the GISELA VO
                    String gisela_abinit_TOPBDII = request.getParameter("gisela_abinit_TOPBDII");
                    // Getting the ABINIT WMS from the portlet request for the GISELA VO
                    String[] gisela_abinit_WMS = request.getParameterValues("gisela_abinit_WMS");
                    // Getting the ABINIT ETOKENSERVER from the portlet request for the GISELA VO
                    String gisela_abinit_ETOKENSERVER = request.getParameter("gisela_abinit_ETOKENSERVER");
                    // Getting the ABINIT MYPROXYSERVER from the portlet request for the GISELA VO
                    String gisela_abinit_MYPROXYSERVER = request.getParameter("gisela_abinit_MYPROXYSERVER");
                    // Getting the ABINIT PORT from the portlet request for the GISELA VO
                    String gisela_abinit_PORT = request.getParameter("gisela_abinit_PORT");
                    // Getting the ABINIT ROBOTID from the portlet request for the GISELA VO
                    String gisela_abinit_ROBOTID = request.getParameter("gisela_abinit_ROBOTID");
                    // Getting the ABINIT WEBDAV from the portlet request for the GISELA VO
                    String gisela_abinit_WEBDAV = request.getParameter("gisela_abinit_WEBDAV");
                    // Getting the ABINIT ROLE from the portlet request for the GISELA VO
                    String gisela_abinit_ROLE = request.getParameter("gisela_abinit_ROLE");
                    // Getting the ABINIT OPTIONS from the portlet request for the GISELA VO
                    String[] gisela_abinit_OPTIONS = request.getParameterValues("gisela_abinit_OPTIONS");

                    String gisela_abinit_RENEWAL = "";
                    String gisela_abinit_DISABLEVOMS = "";

                    if (gisela_abinit_OPTIONS == null){
                        gisela_abinit_RENEWAL = "checked";
                        gisela_abinit_DISABLEVOMS = "unchecked";
                    } else {
                        Arrays.sort(gisela_abinit_OPTIONS);
                        // Get the ABINIT RENEWAL from the portlet preferences for the GISELA VO
                        gisela_abinit_RENEWAL = Arrays.binarySearch(gisela_abinit_OPTIONS, "enableRENEWAL") >= 0 ? "checked" : "unchecked";
                        // Get the ABINIT DISABLEVOMS from the portlet preferences for the GISELA VO
                        gisela_abinit_DISABLEVOMS = Arrays.binarySearch(gisela_abinit_OPTIONS, "disableVOMS") >= 0 ? "checked" : "unchecked";
                    }
                    
                    int nmax=0;                
                    for (int i = 0; i < gisela_abinit_WMS.length; i++)
                        if ( gisela_abinit_WMS[i]!=null && (!gisela_abinit_WMS[i].trim().equals("N/A")) )                        
                            nmax++;
                    
                    log.info("\n\nLength="+nmax);
                    String[] gisela_abinit_WMS_trimmed = new String[nmax];
                    for (int i = 0; i < nmax; i++)
                    {
                        gisela_abinit_WMS_trimmed[i]=gisela_abinit_WMS[i].trim();
                        log.info ("\n\nGISELA [" + i + "] WMS=[" + gisela_abinit_WMS_trimmed[i] + "]");
                    }

                    // Set the portlet preferences
                    portletPreferences.setValue("gisela_abinit_INFRASTRUCTURE", gisela_abinit_INFRASTRUCTURE.trim());
                    portletPreferences.setValue("gisela_abinit_VONAME", gisela_abinit_VONAME.trim());
                    portletPreferences.setValue("gisela_abinit_TOPBDII", gisela_abinit_TOPBDII.trim());
                    portletPreferences.setValues("gisela_abinit_WMS", gisela_abinit_WMS_trimmed);
                    portletPreferences.setValue("gisela_abinit_ETOKENSERVER", gisela_abinit_ETOKENSERVER.trim());
                    portletPreferences.setValue("gisela_abinit_MYPROXYSERVER", gisela_abinit_MYPROXYSERVER.trim());
                    portletPreferences.setValue("gisela_abinit_PORT", gisela_abinit_PORT.trim());
                    portletPreferences.setValue("gisela_abinit_ROBOTID", gisela_abinit_ROBOTID.trim());
                    portletPreferences.setValue("gisela_abinit_WEBDAV", gisela_abinit_WEBDAV.trim());
                    portletPreferences.setValue("gisela_abinit_ROLE", gisela_abinit_ROLE.trim());
                    portletPreferences.setValue("gisela_abinit_RENEWAL", gisela_abinit_RENEWAL);
                    portletPreferences.setValue("gisela_abinit_DISABLEVOMS", gisela_abinit_DISABLEVOMS);
                    
                    portletPreferences.setValue("abinit_APPID", abinit_APPID.trim());
                    portletPreferences.setValue("abinit_LOGLEVEL", abinit_LOGLEVEL.trim());
                    portletPreferences.setValue("abinit_METADATA_HOST", abinit_METADATA_HOST.trim());
                    portletPreferences.setValue("abinit_OUTPUT_PATH", abinit_OUTPUT_PATH.trim());
                    portletPreferences.setValue("abinit_SOFTWARE", abinit_SOFTWARE.trim());
                    portletPreferences.setValue("abinit_LOCAL_PROXY", abinit_LOCAL_PROXY.trim());
                    portletPreferences.setValue("TRACKING_DB_HOSTNAME", TRACKING_DB_HOSTNAME.trim());
                    portletPreferences.setValue("TRACKING_DB_USERNAME", TRACKING_DB_USERNAME.trim());
                    portletPreferences.setValue("TRACKING_DB_PASSWORD", TRACKING_DB_PASSWORD.trim());
                    portletPreferences.setValue("SMTP_HOST", SMTP_HOST.trim());
                    portletPreferences.setValue("SENDER_MAIL", SENDER_MAIL.trim());
                    
                    if (abinit_LOGLEVEL.trim().equals("VERBOSE")) {
                    log.info("\n\nPROCESS ACTION => " + action
                        + "\n- Storing the ABINIT portlet preferences ..."                    
                        + "\n\ngisela_abinit_INFRASTRUCTURE: " + gisela_abinit_INFRASTRUCTURE
                        + "\ngisela_abinit_VONAME: " + gisela_abinit_VONAME
                        + "\ngisela_abinit_TOPBDII: " + gisela_abinit_TOPBDII                    
                        + "\ngisela_abinit_ETOKENSERVER: " + gisela_abinit_ETOKENSERVER
                        + "\ngisela_abinit_MYPROXYSERVER: " + gisela_abinit_MYPROXYSERVER
                        + "\ngisela_abinit_PORT: " + gisela_abinit_PORT
                        + "\ngisela_abinit_ROBOTID: " + gisela_abinit_ROBOTID
                        + "\ngisela_abinit_WEBDAV: " + gisela_abinit_WEBDAV
                        + "\ngisela_abinit_ROLE: " + gisela_abinit_ROLE
                        + "\ngisela_abinit_RENEWAL: " + gisela_abinit_RENEWAL
                        + "\ngisela_abinit_DISABLEVOMS: " + gisela_abinit_DISABLEVOMS

                        + "\n\nabinit_ENABLEINFRASTRUCTURE: " + "gisela"
                        + "\nabinit_APPID: " + abinit_APPID
                        + "\nabinit_LOGLEVEL: " + abinit_LOGLEVEL
                        + "\nabinit_METADATA_HOST: " + abinit_METADATA_HOST
                        + "\nabinit_OUTPUT_PATH: " + abinit_OUTPUT_PATH
                        + "\nabinit_SOFTWARE: " + abinit_SOFTWARE
                        + "\nabinit_LOCAL_PROXY: " + abinit_LOCAL_PROXY
                        + "\nTracking_DB_Hostname: " + TRACKING_DB_HOSTNAME
                        + "\nTracking_DB_Username: " + TRACKING_DB_USERNAME
                        + "\nTracking_DB_Password: " + TRACKING_DB_PASSWORD
                        + "\nSMTP_HOST: " + SMTP_HOST
                        + "\nSENDER_MAIL: " + SENDER_MAIL);
                    }
                }
                
                for (int i=0; i<infras.length; i++)
                log.info("\n - Infrastructure Enabled = " + infras[i]);
                portletPreferences.setValues("abinit_ENABLEINFRASTRUCTURE", infras);
                portletPreferences.setValue("lato_abinit_ENABLEINFRASTRUCTURE", infras[0]);            
                portletPreferences.setValue("garuda_abinit_ENABLEINFRASTRUCTURE", infras[1]);            
                portletPreferences.setValue("eumed_abinit_ENABLEINFRASTRUCTURE", infras[2]);
                portletPreferences.setValue("sagrid_abinit_ENABLEINFRASTRUCTURE", infras[3]);
                portletPreferences.setValue("see_abinit_ENABLEINFRASTRUCTURE", infras[4]);
                portletPreferences.setValue("gisela_abinit_ENABLEINFRASTRUCTURE", infras[5]);

                portletPreferences.store();
                response.setPortletMode(PortletMode.VIEW);
            } // end PROCESS ACTION [ CONFIG_ABINIT_PORTLET ]
            

            if (action.equals("SUBMIT_ABINIT_PORTLET")) {
                log.info("\nPROCESS ACTION => " + action);            
                InfrastructureInfo infrastructures[] = new InfrastructureInfo[6];
                String jdlRequirements[] = new String[2];
                String lato_abinit_WEBDAV = "";
                String garuda_abinit_WEBDAV = "";
                String eumed_abinit_WEBDAV = "";
                String sagrid_abinit_WEBDAV = "";
                String see_abinit_WEBDAV = "";
                String gisela_abinit_WEBDAV = "";
                int MAX=0;                
                
                // Getting the ABINIT APPID from the portlet preferences
                String abinit_APPID = portletPreferences.getValue("abinit_APPID", "N/A");
                // Getting the LOGLEVEL from the portlet preferences
                String abinit_LOGLEVEL = portletPreferences.getValue("abinit_LOGLEVEL", "INFO");
                // Getting the ABINIT_METADATA_HOST from the portlet preferences
                String abinit_METADATA_HOST = portletPreferences.getValue("abinit_METADATA_HOST", "INFO");
                // Getting the ABINIT OUTPUT_PATH from the portlet preferences
                String abinit_OUTPUT_PATH = portletPreferences.getValue("abinit_OUTPUT_PATH", "/tmp");
                // Getting the ABINIT SOFTWARE from the portlet preferences
                String abinit_SOFTWARE = portletPreferences.getValue("abinit_SOFTWARE", "N/A");
                // Getting the ABINIT LOCAL_PROXY from the portlet preferences
                String abinit_LOCAL_PROXY = portletPreferences.getValue("abinit_LOCAL_PROXY", "N/A");
                // Getting the TRACKING_DB_HOSTNAME from the portlet request
                String TRACKING_DB_HOSTNAME = portletPreferences.getValue("TRACKING_DB_HOSTNAME", "N/A");
                // Getting the TRACKING_DB_USERNAME from the portlet request
                String TRACKING_DB_USERNAME = portletPreferences.getValue("TRACKING_DB_USERNAME", "N/A");
                // Getting the TRACKING_DB_PASSWORD from the portlet request
                String TRACKING_DB_PASSWORD = portletPreferences.getValue("TRACKING_DB_PASSWORD","N/A");
                // Getting the SMTP_HOST from the portlet request
                String SMTP_HOST = portletPreferences.getValue("SMTP_HOST","N/A");
                // Getting the SENDER_MAIL from the portlet request
                String SENDER_MAIL = portletPreferences.getValue("SENDER_MAIL","N/A");
                
                String lato_abinit_ENABLEINFRASTRUCTURE =
                        portletPreferences.getValue("lato_abinit_ENABLEINFRASTRUCTURE","null");
                String garuda_abinit_ENABLEINFRASTRUCTURE =
                        portletPreferences.getValue("garuda_abinit_ENABLEINFRASTRUCTURE","null");
                String eumed_abinit_ENABLEINFRASTRUCTURE =
                        portletPreferences.getValue("eumed_abinit_ENABLEINFRASTRUCTURE","null");
                String sagrid_abinit_ENABLEINFRASTRUCTURE =
                        portletPreferences.getValue("sagrid_abinit_ENABLEINFRASTRUCTURE","null");
                String see_abinit_ENABLEINFRASTRUCTURE =
                        portletPreferences.getValue("see_abinit_ENABLEINFRASTRUCTURE","null");
                String gisela_abinit_ENABLEINFRASTRUCTURE =
                        portletPreferences.getValue("gisela_abinit_ENABLEINFRASTRUCTURE","null");
                
                if (lato_abinit_ENABLEINFRASTRUCTURE != null &&
                    lato_abinit_ENABLEINFRASTRUCTURE.equals("lato"))
                {
                    MAX++;
                    // Getting the ABINIT VONAME from the portlet preferences for LATO
                    String lato_abinit_INFRASTRUCTURE = portletPreferences.getValue("lato_abinit_INFRASTRUCTURE", "N/A");
                    // Getting the ABINIT VONAME from the portlet preferences for LATO
                    String lato_abinit_LOGIN = portletPreferences.getValue("lato_abinit_LOGIN", "N/A");
                    // Getting the ABINIT TOPPBDII from the portlet preferences for LATO
                    String lato_abinit_PASSWD = portletPreferences.getValue("lato_abinit_PASSWD", "N/A");
                    // Getting the ABINIT WMS from the portlet preferences for LATO                
                    String[] lato_abinit_WMS = portletPreferences.getValues("lato_abinit_WMS", new String[5]);
                    // Getting the ABINIT ETOKENSERVER from the portlet preferences for LATO
                    String lato_abinit_ETOKENSERVER = portletPreferences.getValue("lato_abinit_ETOKENSERVER", "N/A");
                    // Getting the ABINIT MYPROXYSERVER from the portlet preferences for LATO
                    String lato_abinit_MYPROXYSERVER = portletPreferences.getValue("lato_abinit_MYPROXYSERVER", "N/A");
                    // Getting the ABINIT PORT from the portlet preferences for LATO
                    String lato_abinit_PORT = portletPreferences.getValue("lato_abinit_PORT", "N/A");
                    // Getting the ABINIT ROBOTID from the portlet preferences for LATO
                    String lato_abinit_ROBOTID = portletPreferences.getValue("lato_abinit_ROBOTID", "N/A");
                    // Getting the ABINIT WEBDAV from the portlet preferences for LATO
                    lato_abinit_WEBDAV = portletPreferences.getValue("lato_abinit_WEBDAV", "N/A");
                    // Getting the ABINIT ROLE from the portlet preferences for LATO
                    String lato_abinit_ROLE = portletPreferences.getValue("lato_abinit_ROLE", "N/A");
                    // Getting the ABINIT RENEWAL from the portlet preferences for LATO
                    String lato_abinit_RENEWAL = portletPreferences.getValue("lato_abinit_RENEWAL", "checked");
                    // Getting the ABINIT DISABLEVOMS from the portlet preferences for LATO
                    String lato_abinit_DISABLEVOMS = portletPreferences.getValue("lato_abinit_DISABLEVOMS", "unchecked");                    
                    
                    if (abinit_LOGLEVEL.trim().equals("VERBOSE")) {
                    log.info("\n- Getting the ABINIT portlet preferences ..."
                        + "\nlato_abinit_INFRASTRUCTURE: " + lato_abinit_INFRASTRUCTURE
                        + "\nlato_abinit_LOGIN: " + lato_abinit_LOGIN
                        + "\nlato_abinit_PASSWD: " + lato_abinit_PASSWD                    
                        + "\nlato_abinit_ETOKENSERVER: " + lato_abinit_ETOKENSERVER
                        + "\nlato_abinit_MYPROXYSERVER: " + lato_abinit_MYPROXYSERVER
                        + "\nlato_abinit_PORT: " + lato_abinit_PORT
                        + "\nlato_abinit_ROBOTID: " + lato_abinit_ROBOTID
                        + "\nlato_abinit_WEBDAV: " + lato_abinit_WEBDAV
                        + "\nlato_abinit_ROLE: " + lato_abinit_ROLE
                        + "\nlato_abinit_RENEWAL: " + lato_abinit_RENEWAL
                        + "\nlato_abinit_DISABLEVOMS: " + lato_abinit_DISABLEVOMS
                       
                        + "\n\nabinit_ENABLEINFRASTRUCTURE: " + lato_abinit_ENABLEINFRASTRUCTURE
                        + "\nabinit_APPID: " + abinit_APPID
                        + "\nabinit_LOGLEVEL: " + abinit_LOGLEVEL
                        + "\nabinit_METADATA_HOST: " + abinit_METADATA_HOST
                        + "\nabinit_OUTPUT_PATH: " + abinit_OUTPUT_PATH
                        + "\nabinit_SOFTWARE: " + abinit_SOFTWARE
                        + "\nabinit_LOCAL_PROXY: " + abinit_LOCAL_PROXY
                        + "\nTracking_DB_Hostname: " + TRACKING_DB_HOSTNAME
                        + "\nTracking_DB_Username: " + TRACKING_DB_USERNAME
                        + "\nTracking_DB_Password: " + TRACKING_DB_PASSWORD
                        + "\nSMTP_HOST: " + SMTP_HOST
                        + "\nSENDER_MAIL: " + SENDER_MAIL);
                    }
                    
                    // Defining the WMS list for the "LATO" Infrastructure
                    int nmax=0;
                    for (int i=0; i<lato_abinit_WMS.length; i++)
                        if ((lato_abinit_WMS[i]!=null) && 
                            (!lato_abinit_WMS[i].equals("N/A"))) nmax++;

                    String lato_wmsList[] = new String [nmax];                
                        for (int i=0; i<nmax; i++)
                        {
                            if (lato_abinit_WMS[i]!=null) {
                            lato_wmsList[i]=lato_abinit_WMS[i].trim();
                            log.info ("\n\n[" + nmax
                                              + "] Submitting to LATO ["
                                              + i
                                              + "] using WMS=["
                                              + lato_wmsList[i]
                                              + "]");
                            }
                        }
                    
                    infrastructures[0] = new InfrastructureInfo(
                            "SSH",
                            "ssh",
                            lato_abinit_LOGIN,
                            lato_abinit_PASSWD,
                            lato_wmsList);               
                }
                            
                if (garuda_abinit_ENABLEINFRASTRUCTURE != null &&
                    garuda_abinit_ENABLEINFRASTRUCTURE.equals("garuda"))
                {
                    MAX++;
                    // Getting the ABINITVONAME from the portlet preferences for the GARUDA VO
                    String garuda_abinit_INFRASTRUCTURE = portletPreferences.getValue("garuda_abinit_INFRASTRUCTURE", "N/A");
                    // Getting the ABINIT VONAME from the portlet preferences for the GARUDA VO
                    String garuda_abinit_VONAME = portletPreferences.getValue("garuda_abinit_VONAME", "N/A");
                    // Getting the ABINIT TOPPBDII from the portlet preferences for the GARUDA VO
                    String garuda_abinit_TOPBDII = portletPreferences.getValue("garuda_abinit_TOPBDII", "N/A");
                    // Getting the ABINIT WMS from the portlet preferences for the GARUDA VO                
                    String[] garuda_abinit_WMS = portletPreferences.getValues("garuda_abinit_WMS", new String[5]);
                    // Getting the ABINIT ETOKENSERVER from the portlet preferences for the GARUDA VO
                    String garuda_abinit_ETOKENSERVER = portletPreferences.getValue("garuda_abinit_ETOKENSERVER", "N/A");
                    // Getting the ABINIT MYPROXYSERVER from the portlet preferences for the GARUDA VO
                    String garuda_abinit_MYPROXYSERVER = portletPreferences.getValue("garuda_abinit_MYPROXYSERVER", "N/A");
                    // Getting the ABINIT PORT from the portlet preferences for the GARUDA VO
                    String garuda_abinit_PORT = portletPreferences.getValue("garuda_abinit_PORT", "N/A");
                    // Getting the ABINIT ROBOTID from the portlet preferences for the GARUDA VO
                    String garuda_abinit_ROBOTID = portletPreferences.getValue("garuda_abinit_ROBOTID", "N/A");
                    // Getting the ABINIT WEBDAV from the portlet preferences for the GARUDA VO
                    garuda_abinit_WEBDAV = portletPreferences.getValue("garuda_abinit_WEBDAV", "N/A");
                    // Getting the ABINIT ROLE from the portlet preferences for the GARUDA VO
                    String garuda_abinit_ROLE = portletPreferences.getValue("garuda_abinit_ROLE", "N/A");
                    // Getting the ABINIT RENEWAL from the portlet preferences for the GARUDA VO
                    String garuda_abinit_RENEWAL = portletPreferences.getValue("garuda_abinit_RENEWAL", "checked");
                    // Getting the ABINIT DISABLEVOMS from the portlet preferences for the GARUDA VO
                    String garuda_abinit_DISABLEVOMS = portletPreferences.getValue("garuda_abinit_DISABLEVOMS", "unchecked");                    
                            
                    if (abinit_LOGLEVEL.trim().equals("VERBOSE")) {
                    log.info("\n- Getting the ABINIT portlet preferences ..."
                        + "\ngaruda_abinit_INFRASTRUCTURE: " + garuda_abinit_INFRASTRUCTURE
                        + "\ngaruda_abinit_VONAME: " + garuda_abinit_VONAME
                        + "\ngaruda_abinit_TOPBDII: " + garuda_abinit_TOPBDII                    
                        + "\ngaruda_abinit_ETOKENSERVER: " + garuda_abinit_ETOKENSERVER
                        + "\ngaruda_abinit_MYPROXYSERVER: " + garuda_abinit_MYPROXYSERVER
                        + "\ngaruda_abinit_PORT: " + garuda_abinit_PORT
                        + "\ngaruda_abinit_ROBOTID: " + garuda_abinit_ROBOTID
                        + "\ngaruda_abinit_WEBDAV: " + garuda_abinit_WEBDAV
                        + "\ngaruda_abinit_ROLE: " + garuda_abinit_ROLE
                        + "\ngaruda_abinit_RENEWAL: " + garuda_abinit_RENEWAL
                        + "\ngaruda_abinit_DISABLEVOMS: " + garuda_abinit_DISABLEVOMS
                       
                        + "\n\nabinit_ENABLEINFRASTRUCTURE: " + garuda_abinit_ENABLEINFRASTRUCTURE
                        + "\nabinit_APPID: " + abinit_APPID
                        + "\nabinit_LOGLEVEL: " + abinit_LOGLEVEL
                        + "\nabinit_METADATA_HOST: " + abinit_METADATA_HOST
                        + "\nabinit_OUTPUT_PATH: " + abinit_OUTPUT_PATH
                        + "\nabinit_SOFTWARE: " + abinit_SOFTWARE
                        + "\nabinit_LOCAL_PROXY: " + abinit_LOCAL_PROXY
                        + "\nTracking_DB_Hostname: " + TRACKING_DB_HOSTNAME
                        + "\nTracking_DB_Username: " + TRACKING_DB_USERNAME
                        + "\nTracking_DB_Password: " + TRACKING_DB_PASSWORD
                        + "\nSMTP_HOST: " + SMTP_HOST
                        + "\nSENDER_MAIL: " + SENDER_MAIL);                                            
                    }
                    
                    // Defining the WMS list for the "GARUDA" Infrastructure
                    int nmax=0;
                    for (int i=0; i<garuda_abinit_WMS.length; i++)
                        if ((garuda_abinit_WMS[i]!=null) && 
                            (!garuda_abinit_WMS[i].equals("N/A"))) nmax++;

                    String wmsList[] = new String [nmax];
                    for (int i=0; i<nmax; i++)
                    {
                        if (garuda_abinit_WMS[i]!=null) {
                        wmsList[i]=garuda_abinit_WMS[i].trim();
                        log.info ("\n\n[" + nmax
                                          + "] Submitting to GARUDA ["
                                          + i
                                          + "] using WMSGRAM=["
                                          + wmsList[i]
                                          + "]");
                        }
                    }

                    infrastructures[1] = new InfrastructureInfo(
                        "GARUDA", 
                        "gatekeeper", 
                        wmsList, 
                        abinit_LOCAL_PROXY);
                }
                
                if (eumed_abinit_ENABLEINFRASTRUCTURE != null &&
                    eumed_abinit_ENABLEINFRASTRUCTURE.equals("eumed"))
                {
                    MAX++;
                    // Getting the ABINIT VONAME from the portlet preferences for the EUMED VO
                    String eumed_abinit_INFRASTRUCTURE = portletPreferences.getValue("eumed_abinit_INFRASTRUCTURE", "N/A");
                    // Getting the ABINIT VONAME from the portlet preferences for the EUMED VO
                    String eumed_abinit_VONAME = portletPreferences.getValue("eumed_abinit_VONAME", "N/A");
                    // Getting the ABINIT TOPPBDII from the portlet preferences for the EUMED VO
                    String eumed_abinit_TOPBDII = portletPreferences.getValue("eumed_abinit_TOPBDII", "N/A");
                    // Getting the ABINIT WMS from the portlet preferences for the EUMED VO
                    String[] eumed_abinit_WMS = portletPreferences.getValues("eumed_abinit_WMS", new String[5]);
                    // Getting the ABINIT ETOKENSERVER from the portlet preferences for the EUMED VO
                    String eumed_abinit_ETOKENSERVER = portletPreferences.getValue("eumed_abinit_ETOKENSERVER", "N/A");
                    // Getting the ABINIT MYPROXYSERVER from the portlet preferences for the EUMED VO
                    String eumed_abinit_MYPROXYSERVER = portletPreferences.getValue("eumed_abinit_MYPROXYSERVER", "N/A");
                    // Getting the ABINIT PORT from the portlet preferences for the EUMED VO
                    String eumed_abinit_PORT = portletPreferences.getValue("eumed_abinit_PORT", "N/A");
                    // Getting the ABINIT ROBOTID from the portlet preferences for the EUMED VO
                    String eumed_abinit_ROBOTID = portletPreferences.getValue("eumed_abinit_ROBOTID", "N/A");
                    // Getting the ABINIT WEBDAV from the portlet preferences for the EUMED VO
                    eumed_abinit_WEBDAV = portletPreferences.getValue("eumed_abinit_WEBDAV", "N/A");
                    // Getting the ABINIT ROLE from the portlet preferences for the EUMED VO
                    String eumed_abinit_ROLE = portletPreferences.getValue("eumed_abinit_ROLE", "N/A");
                    // Getting the ABINIT RENEWAL from the portlet preferences for the EUMED VO
                    String eumed_abinit_RENEWAL = portletPreferences.getValue("eumed_abinit_RENEWAL", "checked");
                    // Getting the ABINIT DISABLEVOMS from the portlet preferences for the EUMED VO
                    String eumed_abinit_DISABLEVOMS = portletPreferences.getValue("eumed_abinit_DISABLEVOMS", "unchecked");                    
                    
                    if (abinit_LOGLEVEL.trim().equals("VERBOSE")) {
                    log.info("\n- Getting the ABINIT portlet preferences ..."
                        + "\n\neumed_abinit_INFRASTRUCTURE: " + eumed_abinit_INFRASTRUCTURE
                        + "\neumed_abinit_VONAME: " + eumed_abinit_VONAME
                        + "\neumed_abinit_TOPBDII: " + eumed_abinit_TOPBDII                    
                        + "\neumed_abinit_ETOKENSERVER: " + eumed_abinit_ETOKENSERVER
                        + "\neumed_abinit_MYPROXYSERVER: " + eumed_abinit_MYPROXYSERVER
                        + "\neumed_abinit_PORT: " + eumed_abinit_PORT
                        + "\neumed_abinit_ROBOTID: " + eumed_abinit_ROBOTID
                        + "\neumed_abinit_WEBDAV: " + eumed_abinit_WEBDAV
                        + "\neumed_abinit_ROLE: " + eumed_abinit_ROLE
                        + "\neumed_abinit_RENEWAL: " + eumed_abinit_RENEWAL
                        + "\neumed_abinit_DISABLEVOMS: " + eumed_abinit_DISABLEVOMS

                        + "\n\nabinit_ENABLEINFRASTRUCTURE: " + eumed_abinit_ENABLEINFRASTRUCTURE
                        + "\nabinit_APPID: " + abinit_APPID
                        + "\nabinit_LOGLEVEL: " + abinit_LOGLEVEL
                        + "\nabinit_METADATA_HOST: " + abinit_METADATA_HOST
                        + "\nabinit_DEFAULT_STORAGE: " + eumed_abinit_WEBDAV
                        + "\nabinit_OUTPUT_PATH: " + abinit_OUTPUT_PATH
                        + "\nabinit_SOFTWARE: " + abinit_SOFTWARE
                        + "\nabinit_LOCAL_PROXY: " + abinit_LOCAL_PROXY
                        + "\nTracking_DB_Hostname: " + TRACKING_DB_HOSTNAME
                        + "\nTracking_DB_Username: " + TRACKING_DB_USERNAME
                        + "\nTracking_DB_Password: " + TRACKING_DB_PASSWORD
                        + "\nSMTP_HOST: " + SMTP_HOST
                        + "\nSENDER_MAIL: " + SENDER_MAIL);                                           
                    }
                    
                    // Defining the WMS list for the "EUMED" Infrastructure
                    int nmax=0;
                    for (int i=0; i<eumed_abinit_WMS.length; i++)
                        if ((eumed_abinit_WMS[i]!=null) && 
                            (!eumed_abinit_WMS[i].equals("N/A"))) nmax++;
                    
                    String wmsList[] = new String [nmax];
                    for (int i=0; i<nmax; i++)
                    {
                        if (eumed_abinit_WMS[i]!=null) {
                        wmsList[i]=eumed_abinit_WMS[i].trim();
                        log.info ("\n\n[" + nmax
                                          + "] Submitting to EUMED ["
                                          + i
                                          + "] using WMS=["
                                          + wmsList[i]
                                          + "]");
                        }
                    }
                    
                    // Setting the JDL Requirements
                    String[] SOFTWARE_LIST = abinit_SOFTWARE.split(",");
                    int index=0;
                    
                    for(String SOFTWARE: SOFTWARE_LIST)
                    {
                        jdlRequirements[index++] = 
                            "VO-"
                            + eumed_abinit_VONAME
                            + "-"
                            + SOFTWARE;                            
                    }
                    
                    infrastructures[2] = new InfrastructureInfo(
                        eumed_abinit_VONAME,
                        eumed_abinit_TOPBDII,
                        wmsList,
                        eumed_abinit_ETOKENSERVER,
                        eumed_abinit_PORT,
                        eumed_abinit_ROBOTID,
                        eumed_abinit_VONAME,
                        eumed_abinit_ROLE,
                        true, // set the RFC proxy for the infrastructure                            
                        jdlRequirements[0] + "," + jdlRequirements[1]);
                        //"VO-" + eumed_abinit_VONAME + "-" + abinit_SOFTWARE);
                }                                
                
                if (sagrid_abinit_ENABLEINFRASTRUCTURE != null &&
                    sagrid_abinit_ENABLEINFRASTRUCTURE.equals("sagrid"))
                {
                    MAX++;
                    // Getting the ABINIT VONAME from the portlet preferences for the SAGRID VO
                    String sagrid_abinit_INFRASTRUCTURE = portletPreferences.getValue("sagrid_abinit_INFRASTRUCTURE", "N/A");
                    // Getting the ABINIT VONAME from the portlet preferences for the SAGRID VO
                    String sagrid_abinit_VONAME = portletPreferences.getValue("sagrid_abinit_VONAME", "N/A");
                    // Getting the ABINIT TOPPBDII from the portlet preferences for the SAGRID VO
                    String sagrid_abinit_TOPBDII = portletPreferences.getValue("sagrid_abinit_TOPBDII", "N/A");
                    // Getting the ABINIT WMS from the portlet preferences for the SAGRID VO
                    String[] sagrid_abinit_WMS = portletPreferences.getValues("sagrid_abinit_WMS", new String[5]);
                    // Getting the ABINIT ETOKENSERVER from the portlet preferences for the SAGRID VO
                    String sagrid_abinit_ETOKENSERVER = portletPreferences.getValue("sagrid_abinit_ETOKENSERVER", "N/A");
                    // Getting the ABINIT MYPROXYSERVER from the portlet preferences for the SAGRID VO
                    String sagrid_abinit_MYPROXYSERVER = portletPreferences.getValue("sagrid_abinit_MYPROXYSERVER", "N/A");
                    // Getting the ABINIT PORT from the portlet preferences for the SAGRID VO
                    String sagrid_abinit_PORT = portletPreferences.getValue("sagrid_abinit_PORT", "N/A");
                    // Getting the ABINIT ROBOTID from the portlet preferences for the SAGRID VO
                    String sagrid_abinit_ROBOTID = portletPreferences.getValue("sagrid_abinit_ROBOTID", "N/A");
                    // Getting the ABINIT WEBDAV from the portlet preferences for the SAGRID VO
                    sagrid_abinit_WEBDAV = portletPreferences.getValue("sagrid_abinit_WEBDAV", "N/A");
                    // Getting the ABINIT ROLE from the portlet preferences for the SAGRID VO
                    String sagrid_abinit_ROLE = portletPreferences.getValue("sagrid_abinit_ROLE", "N/A");
                    // Getting the ABINIT RENEWAL from the portlet preferences for the SAGRID VO
                    String sagrid_abinit_RENEWAL = portletPreferences.getValue("sagrid_abinit_RENEWAL", "checked");
                    // Getting the ABINIT DISABLEVOMS from the portlet preferences for the SAGRID VO
                    String sagrid_abinit_DISABLEVOMS = portletPreferences.getValue("sagrid_abinit_DISABLEVOMS", "unchecked");                   
                    
                    if (abinit_LOGLEVEL.trim().equals("VERBOSE")) {
                    log.info("\n- Getting the ABINIT portlet preferences ..."
                        + "\n\nsagrid_abinit_INFRASTRUCTURE: " + sagrid_abinit_INFRASTRUCTURE
                        + "\nsagrid_abinit_VONAME: " + sagrid_abinit_VONAME
                        + "\nsagrid_abinit_TOPBDII: " + sagrid_abinit_TOPBDII                    
                        + "\nsagrid_abinit_ETOKENSERVER: " + sagrid_abinit_ETOKENSERVER
                        + "\nsagrid_abinit_MYPROXYSERVER: " + sagrid_abinit_MYPROXYSERVER
                        + "\nsagrid_abinit_PORT: " + sagrid_abinit_PORT
                        + "\nsagrid_abinit_ROBOTID: " + sagrid_abinit_ROBOTID
                        + "\nsagrid_abinit_WEBDAV: " + sagrid_abinit_WEBDAV
                        + "\nsagrid_abinit_ROLE: " + sagrid_abinit_ROLE
                        + "\nsagrid_abinit_RENEWAL: " + sagrid_abinit_RENEWAL
                        + "\nsagrid_abinit_DISABLEVOMS: " + sagrid_abinit_DISABLEVOMS

                        + "\n\nabinit_ENABLEINFRASTRUCTURE: " + eumed_abinit_ENABLEINFRASTRUCTURE
                        + "\nabinit_APPID: " + abinit_APPID
                        + "\nabinit_LOGLEVEL: " + abinit_LOGLEVEL
                        + "\nabinit_METADATA_HOST: " + abinit_METADATA_HOST
                        + "\nabinit_DEFAULT_STORAGE: " + sagrid_abinit_WEBDAV
                        + "\nabinit_OUTPUT_PATH: " + abinit_OUTPUT_PATH
                        + "\nabinit_SOFTWARE: " + abinit_SOFTWARE
                        + "\nabinit_LOCAL_PROXY: " + abinit_LOCAL_PROXY
                        + "\nTracking_DB_Hostname: " + TRACKING_DB_HOSTNAME
                        + "\nTracking_DB_Username: " + TRACKING_DB_USERNAME
                        + "\nTracking_DB_Password: " + TRACKING_DB_PASSWORD
                        + "\nSMTP_HOST: " + SMTP_HOST
                        + "\nSENDER_MAIL: " + SENDER_MAIL);                                            
                    }
                    
                    // Defining the WMS list for the "SAGRID" Infrastructure
                    int nmax=0;
                    for (int i=0; i<sagrid_abinit_WMS.length; i++)
                        if ((sagrid_abinit_WMS[i]!=null) && 
                            (!sagrid_abinit_WMS[i].equals("N/A"))) nmax++;
                    
                    String wmsList[] = new String [nmax];
                    for (int i=0; i<nmax; i++)
                    {
                        if (sagrid_abinit_WMS[i]!=null) {
                        wmsList[i]=sagrid_abinit_WMS[i].trim();
                        log.info ("\n\n[" + nmax
                                          + "] Submitting to SAGRID ["
                                          + i
                                          + "] using WMS=["
                                          + wmsList[i]
                                          + "]");
                        }
                    }
                    
                    // Setting the JDL Requirements
                    String[] SOFTWARE_LIST = abinit_SOFTWARE.split(",");
                    int index=0;
                    
                    for(String SOFTWARE: SOFTWARE_LIST)
                    {
                        jdlRequirements[index++] = 
                            "VO-"
                            + sagrid_abinit_VONAME
                            + "-"
                            + SOFTWARE;                            
                    }
                    
                    infrastructures[3] = new InfrastructureInfo(
                        sagrid_abinit_VONAME,
                        sagrid_abinit_TOPBDII,
                        wmsList,
                        sagrid_abinit_ETOKENSERVER,
                        sagrid_abinit_PORT,
                        sagrid_abinit_ROBOTID,
                        sagrid_abinit_VONAME,
                        sagrid_abinit_ROLE,
                        true, // set the RFC proxy for the infrastructure                            
                        jdlRequirements[0] + "," + jdlRequirements[1]);
                        //"VO-" + sagrid_abinit_VONAME + "-" + abinit_SOFTWARE);
                }

                if (see_abinit_ENABLEINFRASTRUCTURE != null &&
                    see_abinit_ENABLEINFRASTRUCTURE.equals("see")) 
                {
                    MAX++;
                    // Getting the ABINIT VONAME from the portlet preferences for the SEE VO
                    String see_abinit_INFRASTRUCTURE = portletPreferences.getValue("see_abinit_INFRASTRUCTURE", "N/A");
                    // Getting the ABINIT VONAME from the portlet preferences for the SEE VO
                    String see_abinit_VONAME = portletPreferences.getValue("see_abinit_VONAME", "N/A");
                    // Getting the ABINIT TOPPBDII from the portlet preferences for the SEE VO
                    String see_abinit_TOPBDII = portletPreferences.getValue("see_abinit_TOPBDII", "N/A");
                    // Getting the ABINIT WMS from the portlet preferences for the SEE VO
                    String[] see_abinit_WMS = portletPreferences.getValues("see_abinit_WMS", new String[5]);
                    // Getting the ABINIT ETOKENSERVER from the portlet preferences for the SEE VO
                    String see_abinit_ETOKENSERVER = portletPreferences.getValue("see_abinit_ETOKENSERVER", "N/A");
                    // Getting the ABINIT MYPROXYSERVER from the portlet preferences for the SEE VO
                    String see_abinit_MYPROXYSERVER = portletPreferences.getValue("see_abinit_MYPROXYSERVER", "N/A");
                    // Getting the ABINIT PORT from the portlet preferences for the SEE VO
                    String see_abinit_PORT = portletPreferences.getValue("see_abinit_PORT", "N/A");
                    // Getting the ABINIT ROBOTID from the portlet preferences for the SEE VO
                    String see_abinit_ROBOTID = portletPreferences.getValue("see_abinit_ROBOTID", "N/A");
                    // Getting the ABINIT WEBDAV from the portlet preferences for the SEE VO
                    see_abinit_WEBDAV = portletPreferences.getValue("see_abinit_WEBDAV", "N/A");
                    // Getting the ABINIT ROLE from the portlet preferences for the SEE VO
                    String see_abinit_ROLE = portletPreferences.getValue("see_abinit_ROLE", "N/A");
                    // Getting the ABINIT RENEWAL from the portlet preferences for the SEE VO
                    String see_abinit_RENEWAL = portletPreferences.getValue("see_abinit_RENEWAL", "checked");
                    // Getting the ABINIT DISABLEVOMS from the portlet preferences for the SEE VO
                    String see_abinit_DISABLEVOMS = portletPreferences.getValue("see_abinit_DISABLEVOMS", "unchecked");                    
                    
                    if (abinit_LOGLEVEL.trim().equals("VERBOSE")) {
                    log.info("\n- Getting the ABINIT portlet preferences ..."
                        + "\n\nsee_abinit_INFRASTRUCTURE: " + see_abinit_INFRASTRUCTURE
                        + "\nsee_abinit_VONAME: " + see_abinit_VONAME
                        + "\nsee_abinit_TOPBDII: " + see_abinit_TOPBDII                        
                        + "\nsee_abinit_ETOKENSERVER: " + see_abinit_ETOKENSERVER
                        + "\nsee_abinit_MYPROXYSERVER: " + see_abinit_MYPROXYSERVER
                        + "\nsee_abinit_PORT: " + see_abinit_PORT
                        + "\nsee_abinit_ROBOTID: " + see_abinit_ROBOTID
                        + "\nsee_abinit_WEBDAV: " + see_abinit_WEBDAV
                        + "\nsee_abinit_ROLE: " + see_abinit_ROLE
                        + "\nsee_abinit_RENEWAL: " + see_abinit_RENEWAL
                        + "\nsee_abinit_DISABLEVOMS: " + see_abinit_DISABLEVOMS

                        + "\n\nabinit_ENABLEINFRASTRUCTURE: " + see_abinit_ENABLEINFRASTRUCTURE
                        + "\nabinit_APPID: " + abinit_APPID
                        + "\nabinit_LOGLEVEL: " + abinit_LOGLEVEL
                        + "\nabinit_METADATA_HOST: " + abinit_METADATA_HOST
                        + "\nabinit_DEFAULT_STORAGE: " + sagrid_abinit_WEBDAV
                        + "\nabinit_OUTPUT_PATH: " + abinit_OUTPUT_PATH                        
                        + "\nabinit_SOFTWARE: " + abinit_SOFTWARE
                        + "\nabinit_LOCAL_PROXY: " + abinit_LOCAL_PROXY                            
                        + "\nTracking_DB_Hostname: " + TRACKING_DB_HOSTNAME
                        + "\nTracking_DB_Username: " + TRACKING_DB_USERNAME
                        + "\nTracking_DB_Password: " + TRACKING_DB_PASSWORD
                        + "\nSMTP_HOST: " + SMTP_HOST
                        + "\nSENDER_MAIL: " + SENDER_MAIL);                                            
                    }
                    
                    // Defining the WMS list for the "SEE" Infrastructure
                    int nmax=0;
                    for (int i=0; i<see_abinit_WMS.length; i++)
                        if ((see_abinit_WMS[i]!=null) && 
                            (!see_abinit_WMS[i].equals("N/A"))) nmax++;
                    
                    String wmsList[] = new String [see_abinit_WMS.length];
                    for (int i=0; i<see_abinit_WMS.length; i++)
                    {
                        if (see_abinit_WMS[i]!=null) {
                        wmsList[i]=see_abinit_WMS[i].trim();
                        log.info ("\n\nSubmitting for SEE [" + i + "] using WMS=[" + wmsList[i] + "]");
                        }
                    }
                    
                    // Setting the JDL Requirements
                    String[] SOFTWARE_LIST = abinit_SOFTWARE.split(",");
                    int index=0;
                    
                    for(String SOFTWARE: SOFTWARE_LIST)
                    {
                        jdlRequirements[index++] = 
                        "VO-"
                        + see_abinit_VONAME
                        + "-"
                        + SOFTWARE;                            
                    }
                    
                    infrastructures[4] = new InfrastructureInfo(
                        see_abinit_VONAME,
                        see_abinit_TOPBDII,
                        wmsList,
                        see_abinit_ETOKENSERVER,
                        see_abinit_PORT,
                        see_abinit_ROBOTID,
                        see_abinit_VONAME,
                        see_abinit_ROLE,
                        true, // set the RFC proxy for the infrastructure
                        jdlRequirements[0] + "," + jdlRequirements[1]);
                        //"VO-" + see_abinit_VONAME + "-" + abinit_SOFTWARE);                                                            
                }
                
                if (gisela_abinit_ENABLEINFRASTRUCTURE != null &&
                    gisela_abinit_ENABLEINFRASTRUCTURE.equals("gisela")) 
                {
                    MAX++;
                    // Getting the ABINIT VONAME from the portlet preferences for the GISELA VO
                    String gisela_abinit_INFRASTRUCTURE = portletPreferences.getValue("gisela_abinit_INFRASTRUCTURE", "N/A");
                    // Getting the ABINIT VONAME from the portlet preferences for the GISELA VO
                    String gisela_abinit_VONAME = portletPreferences.getValue("gisela_abinit_VONAME", "N/A");
                    // Getting the ABINIT TOPPBDII from the portlet preferences for the GISELA VO
                    String gisela_abinit_TOPBDII = portletPreferences.getValue("gisela_abinit_TOPBDII", "N/A");
                    // Getting the ABINIT WMS from the portlet preferences for the GISELA VO
                    String[] gisela_abinit_WMS = portletPreferences.getValues("gisela_abinit_WMS", new String[5]);
                    // Getting the ABINIT ETOKENSERVER from the portlet preferences for the GISELA VO
                    String gisela_abinit_ETOKENSERVER = portletPreferences.getValue("gisela_abinit_ETOKENSERVER", "N/A");
                    // Getting the ABINIT MYPROXYSERVER from the portlet preferences for the GISELA VO
                    String gisela_abinit_MYPROXYSERVER = portletPreferences.getValue("gisela_abinit_MYPROXYSERVER", "N/A");
                    // Getting the ABINIT PORT from the portlet preferences for the GISELA VO
                    String gisela_abinit_PORT = portletPreferences.getValue("gisela_abinit_PORT", "N/A");
                    // Getting the ABINIT ROBOTID from the portlet preferences for the GISELA VO
                    String gisela_abinit_ROBOTID = portletPreferences.getValue("gisela_abinit_ROBOTID", "N/A");
                    // Getting the ABINIT WEBDAV from the portlet preferences for the GISELA VO
                    gisela_abinit_WEBDAV = portletPreferences.getValue("gisela_abinit_WEBDAV", "N/A");
                    // Getting the ABINIT ROLE from the portlet preferences for the GISELA VO
                    String gisela_abinit_ROLE = portletPreferences.getValue("gisela_abinit_ROLE", "N/A");
                    // Getting the ABINIT RENEWAL from the portlet preferences for the GISELA VO
                    String gisela_abinit_RENEWAL = portletPreferences.getValue("gisela_abinit_RENEWAL", "checked");
                    // Getting the ABINIT DISABLEVOMS from the portlet preferences for the GISELA VO
                    String gisela_abinit_DISABLEVOMS = portletPreferences.getValue("gisela_abinit_DISABLEVOMS", "unchecked");                    
                    
                    if (abinit_LOGLEVEL.trim().equals("VERBOSE")) {
                    log.info("\n- Getting the ABINIT portlet preferences ..."
                        + "\n\ngisela_abinit_INFRASTRUCTURE: " + gisela_abinit_INFRASTRUCTURE
                        + "\ngisela_abinit_VONAME: " + gisela_abinit_VONAME
                        + "\ngisela_abinit_TOPBDII: " + gisela_abinit_TOPBDII                        
                        + "\ngisela_abinit_ETOKENSERVER: " + gisela_abinit_ETOKENSERVER
                        + "\ngisela_abinit_MYPROXYSERVER: " + gisela_abinit_MYPROXYSERVER
                        + "\ngisela_abinit_PORT: " + gisela_abinit_PORT
                        + "\ngisela_abinit_ROBOTID: " + gisela_abinit_ROBOTID
                        + "\ngisela_abinit_WEBDAV: " + gisela_abinit_WEBDAV
                        + "\ngisela_abinit_ROLE: " + gisela_abinit_ROLE
                        + "\ngisela_abinit_RENEWAL: " + gisela_abinit_RENEWAL
                        + "\ngisela_abinit_DISABLEVOMS: " + gisela_abinit_DISABLEVOMS

                        + "\n\nabinit_ENABLEINFRASTRUCTURE: " + see_abinit_ENABLEINFRASTRUCTURE
                        + "\nabinit_APPID: " + abinit_APPID
                        + "\nabinit_LOGLEVEL: " + abinit_LOGLEVEL
                        + "\nabinit_METADATA_HOST: " + abinit_METADATA_HOST
                        + "\nabinit_DEFAULT_STORAGE: " + gisela_abinit_WEBDAV
                        + "\nabinit_OUTPUT_PATH: " + abinit_OUTPUT_PATH                        
                        + "\nabinit_SOFTWARE: " + abinit_SOFTWARE
                        + "\nabinit_LOCAL_PROXY: " + abinit_LOCAL_PROXY
                        + "\nTracking_DB_Hostname: " + TRACKING_DB_HOSTNAME
                        + "\nTracking_DB_Username: " + TRACKING_DB_USERNAME
                        + "\nTracking_DB_Password: " + TRACKING_DB_PASSWORD
                        + "\nSMTP_HOST: " + SMTP_HOST
                        + "\nSENDER_MAIL: " + SENDER_MAIL);                                            
                    }
                    
                    // Defining the WMS list for the "GISELA" Infrastructure
                    int nmax=0;
                    for (int i=0; i<gisela_abinit_WMS.length; i++)
                        if ((gisela_abinit_WMS[i]!=null) && 
                            (!gisela_abinit_WMS[i].equals("N/A"))) nmax++;
                    
                    String wmsList[] = new String [gisela_abinit_WMS.length];
                    for (int i=0; i<gisela_abinit_WMS.length; i++)
                    {
                        if (gisela_abinit_WMS[i]!=null) {
                        wmsList[i]=gisela_abinit_WMS[i].trim();
                        log.info ("\n\nSubmitting for GISELA [" + i + "] using WMS=[" + wmsList[i] + "]");
                        }
                    }
                    
                    // Setting the JDL Requirements
                    String[] SOFTWARE_LIST = abinit_SOFTWARE.split(",");
                    int index=0;
                    
                    for(String SOFTWARE: SOFTWARE_LIST)
                    {
                        jdlRequirements[index++] = 
                        "VO-"
                        + gisela_abinit_VONAME
                        + "-"
                        + SOFTWARE;                            
                    }
                    
                    infrastructures[5] = new InfrastructureInfo(
                        gisela_abinit_VONAME,
                        gisela_abinit_TOPBDII,
                        wmsList,
                        gisela_abinit_ETOKENSERVER,
                        gisela_abinit_PORT,
                        gisela_abinit_ROBOTID,
                        gisela_abinit_VONAME,
                        gisela_abinit_ROLE,
                        true, // set the RFC proxy for the infrastructure
                        jdlRequirements[0] + "," + jdlRequirements[1]);
                        //"VO-" + gisela_abinit_VONAME + "-" + abinit_SOFTWARE);                                                            
                }
                
                String[] ABINIT_Parameters = new String [11];                

                // Upload the input settings for the application
                ABINIT_Parameters = uploadAbinitSettings( request, response, username );                
                
                log.info ("\n\nStarting ABINIT with these settings: ");
                log.info("\n- Input Parameters: ");
                if (ABINIT_Parameters[0].equals("abinit_par")) {
                    log.info("\n- Computing Model : " + ABINIT_Parameters[0]);
                    log.info("\n- #CPUs cores : " + ABINIT_Parameters[1]);
                } else {
                    log.info("\n- Computing Model : " + ABINIT_Parameters[0]);
                    ABINIT_Parameters[1]="0"; // no #CPUs core
                }
                
                if (ABINIT_Parameters[3]!=null)
                    ABINIT_Parameters[3] = ABINIT_Parameters[3]
                            .replace("null", "");
                
                log.info("\n- Calculation Parameters ASCII file = " + ABINIT_Parameters[2]);
                log.info("\n- Pseduo Potential ASCII file(s) = " + ABINIT_Parameters[3]);
                log.info("\n- Files ASCII files = " + ABINIT_Parameters[4]);
                log.info("\n- List of output files = " + ABINIT_Parameters[5]);
                log.info("\n- Description = " + ABINIT_Parameters[6]);
                log.info("\n- WallClock Time = " + ABINIT_Parameters[7]);
                log.info("\n- ABINIT_CE = " + ABINIT_Parameters[10]);                
                log.info("\n- Enable Demo = " + ABINIT_Parameters[8]);
                log.info("\n- Enable Notification = " + ABINIT_Parameters[9]);
                
                if (abinit_LOGLEVEL.trim().equals("VERBOSE"))
                log.info("\n - Creating a TAR file with a list of Pseudo Potential ASCII file(s).");
                File ABINIT_Repository = new File ("/tmp");
                DateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
                String timeStamp = dateFormat.format(Calendar.getInstance().getTime());                                
                    
                String ABINIT_PseudoPotentialARCHIVE = 
                        ABINIT_Repository + "/" 
                        + timeStamp + "_" + username
                        + "_pseudofiles.tar";
                
                if (ABINIT_Parameters[8]==null) {                                        
                    try {
                        getArchive (ABINIT_PseudoPotentialARCHIVE, 
                                    ABINIT_Parameters[3]);
                    } catch (Exception ex) {
                        Logger.getLogger(Abinit.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                
                // Preparing to submit jobs in different grid infrastructure..
                //=============================================================
                // IMPORTANT: THIS CLASS *MUST* BE INSTANCIATED USING 
                //            EMPTY CONSTRUCTOR WHEN THE PORTLET IS DEPLOYED 
                //            IN PRODUCTION!!!
                //=============================================================
                /*MultiInfrastructureJobSubmission AbinitMultiJobSubmission =
                new MultiInfrastructureJobSubmission(TRACKING_DB_HOSTNAME,
                                                     TRACKING_DB_USERNAME,
                                                     TRACKING_DB_PASSWORD);*/
                                
                MultiInfrastructureJobSubmission AbinitMultiJobSubmission =
                    new MultiInfrastructureJobSubmission();

                // Set the list of infrastructure(s) activated for the portlet                
                if (infrastructures[0]!=null) {
                    if (abinit_LOGLEVEL.trim().equals("VERBOSE"))
                        log.info("\n- Adding the LATO Infrastructure.");                     
                     AbinitMultiJobSubmission.addInfrastructure(infrastructures[0]); 
                }            
                if (infrastructures[1]!=null) {
                    if (abinit_LOGLEVEL.trim().equals("VERBOSE"))
                        log.info("\n- Adding the GARUDA Infrastructure.");                     
                     AbinitMultiJobSubmission.addInfrastructure(infrastructures[1]); 
                }
                if (infrastructures[2]!=null) {
                    if (abinit_LOGLEVEL.trim().equals("VERBOSE"))
                        log.info("\n- Adding the EUMED Infrastructure.");                     
                     AbinitMultiJobSubmission.addInfrastructure(infrastructures[2]);
                }
                if (infrastructures[3]!=null) {
                    if (abinit_LOGLEVEL.trim().equals("VERBOSE"))
                        log.info("\n- Adding the SAGRID Infrastructure.");                     
                     AbinitMultiJobSubmission.addInfrastructure(infrastructures[3]);
                }
                if (infrastructures[4]!=null) {
                    if (abinit_LOGLEVEL.trim().equals("VERBOSE"))
                        log.info("\n- Adding the SEE Infrastructure.");                     
                     AbinitMultiJobSubmission.addInfrastructure(infrastructures[4]);
                }
                if (infrastructures[5]!=null) {
                    if (abinit_LOGLEVEL.trim().equals("VERBOSE"))
                        log.info("\n- Adding the GISELA Infrastructure.");                     
                     AbinitMultiJobSubmission.addInfrastructure(infrastructures[5]);
                }                                
                
                String AbinitFilesPath = getPortletContext().getRealPath("/") +
                                        "WEB-INF/config";                        
                
                // Set the Output path forresults
                //AbinitMultiJobSubmission.setOutputPath("/tmp");
                AbinitMultiJobSubmission.setOutputPath(abinit_OUTPUT_PATH);
                            
                // Set the StandardOutput for ABINIT
                AbinitMultiJobSubmission.setJobOutput("std.txt");

                // Set the StandardError for ABINIT
                AbinitMultiJobSubmission.setJobError("std.err");
                
                // Set the Executable for ABINIT
                AbinitMultiJobSubmission.setExecutable("start_abinit-7.6.4.sh");                        
                
                String InputSandbox = "";
                String Arguments = "";
                String outputFiles = "";
                
                InfrastructureInfo infrastructure = 
                        AbinitMultiJobSubmission.getInfrastructure();
                
                String Middleware = null;
                if (infrastructure.getMiddleware().equals("glite"))
                    Middleware = "gLite";
                    
                if (infrastructure.getMiddleware().equals("gatekeeper"))
                    Middleware = "GARUDA";
                    
                if (infrastructure.getMiddleware().equals("ssh"))
                    Middleware = "ssh";
                
                String abinit_DEFAULT_STORAGE = "";
                if (abinit_LOGLEVEL.trim().equals("VERBOSE")) 
                {
                    log.info("\n- Selected Infrastructure = " 
                            + infrastructure.getName());
                    
                    if (infrastructure.getName().equals("eumed"))
                        abinit_DEFAULT_STORAGE = eumed_abinit_WEBDAV;
                    if (infrastructure.getName().equals("see"))
                        abinit_DEFAULT_STORAGE = see_abinit_WEBDAV;
                    if (infrastructure.getName().equals("gisela"))
                        abinit_DEFAULT_STORAGE = gisela_abinit_WEBDAV;
                
                    log.info("\n- Enabled Middleware = " + Middleware);
                    log.info("\n- WebDAV Storage = " + abinit_DEFAULT_STORAGE);
                }
                
                if (ABINIT_Parameters[5]!=null) 
                {
                    outputFiles = ABINIT_Repository
                                  + "/" + timeStamp + "_" + username
                                  + "_outputs.txt";
                    
                    storeFile (outputFiles, ABINIT_Parameters[5]);                    
                }
                                          
                if (ABINIT_Parameters[8]!=null) {
                    // Demo ? Yes
                    if (ABINIT_Parameters[0].equals("abinit_seq"))
                    {
                       InputSandbox = 
                            AbinitFilesPath + "/start_abinit-7.6.4.sh" + "," 
                          + AbinitFilesPath + "/demo_seq/demo.in" + ","
                          + AbinitFilesPath + "/demo_seq/01h.pspgth" + ","
                          + AbinitFilesPath + "/demo_seq/demo.files" + ","
                          + AbinitFilesPath + "/curl" + ","
                          + AbinitFilesPath + "/libcurl.so.3" + ","
                          + AbinitFilesPath + "/libssl.so.6" + ","
                          + AbinitFilesPath + "/libcrypto.so.6" + ","
                          + outputFiles;
                    
                        Arguments =                                 
                            "demo.in" + ","
                          + "01h.pspgth" + ","
                          + "demo.files" + ","
                          + ABINIT_Parameters[0] + "," + ABINIT_Parameters[1] + ","
                          + Middleware + "," 
                          + abinit_METADATA_HOST + ","
                          + username + "," 
                          + ABINIT_Parameters[6].replaceAll("\\s", "_") + ","                          
                          + abinit_DEFAULT_STORAGE + ","
                          + outputFiles;
                    } else {
                        InputSandbox = 
                            AbinitFilesPath + "/start_abinit-7.6.4.sh" + "," 
                          + AbinitFilesPath + "/demo_par/demopara.in" + ","
                          + AbinitFilesPath + "/demo_par/13al.pspgth" + ","
                          + AbinitFilesPath + "/demo_par/demopara.files" + ","
                          + AbinitFilesPath + "/curl" + ","
                          + AbinitFilesPath + "/libcurl.so.3" + ","
                          + AbinitFilesPath + "/libssl.so.6" + ","
                          + AbinitFilesPath + "/libcrypto.so.6" + ","
                          + outputFiles;
                    
                        Arguments = 
                           "demopara.in" + ","
                          + "13al.pspgth" + ","
                          + "demopara.files" + ","
                          + ABINIT_Parameters[0] + "," + ABINIT_Parameters[1] + ","
                          + Middleware + ","
                          + abinit_METADATA_HOST + ","
                          + username + ","
                          + ABINIT_Parameters[6].replaceAll("\\s", "_") + ","
                          + abinit_DEFAULT_STORAGE + ","
                          + outputFiles;
                    }
                } else {
                    // User's input files
                    InputSandbox = 
                            AbinitFilesPath + "/start_abinit-7.6.4.sh" + "," 
                          + ABINIT_Parameters[2] + ","                          
                          + ABINIT_PseudoPotentialARCHIVE + ","
                          + ABINIT_Parameters[4] + ","
                          + AbinitFilesPath + "/curl" + ","
                          + AbinitFilesPath + "/libcurl.so.3" + ","
                          + AbinitFilesPath + "/libssl.so.6" + ","
                          + AbinitFilesPath + "/libcrypto.so.6" + ","
                          + outputFiles;                          
                    
                    Arguments = 
                            ABINIT_Parameters[2] + "," 
                          + ABINIT_PseudoPotentialARCHIVE + ","
                          + ABINIT_Parameters[4] + "," 
                          + ABINIT_Parameters[0] + "," + ABINIT_Parameters[1] + ","
                          + Middleware + ","
                          + abinit_METADATA_HOST + ","
                          + username + ","
                          + ABINIT_Parameters[6].replaceAll("\\s", "_") + ","                          
                          + abinit_DEFAULT_STORAGE + ","
                          + outputFiles; 
                }
                
                // Set the list of Arguments for ABINIT
                AbinitMultiJobSubmission.setArguments(Arguments);
                
                // Set InputSandbox files (string with comma separated list of file names)
                AbinitMultiJobSubmission.setInputFiles(InputSandbox);
                
                // OutputSandbox (string with comma separated list of file names)
                String ABINIT_LOG = "abinit.log";
                String ENV_LOG = "env.log";
                String CURL_LOG = "curl.log";
                String README = "output.README";
                
                AbinitMultiJobSubmission
                    .setOutputFiles(ENV_LOG + "," + ABINIT_LOG 
                        + "," + CURL_LOG + "," + README);
                
                // Set the MaxWallClockTime Requirements
                String MaxWallClockTimeRequirements[] = new String[1];
                MaxWallClockTimeRequirements[0] = 
                        "JDLRequirements=(other.GlueCEPolicyMaxWallClockTime>"
                        + ABINIT_Parameters[7]
                        + ")";
                
                AbinitMultiJobSubmission
                        .setJDLRequirements(MaxWallClockTimeRequirements);
                
                // Check if more than one infrastructure have been enabled
                // If NMAX>1 this option is disabled.
                if (MAX==1) 
                {
                    
                    String abinit_VONAME = "";
                    String abinit_TOPBDII = "";
                    String RANDOM_CE = "";                    
                    int MAXWallClockTime = Integer.parseInt(ABINIT_Parameters[7]);
                    
                    if (eumed_abinit_ENABLEINFRASTRUCTURE != null &&
                        eumed_abinit_ENABLEINFRASTRUCTURE.equals("eumed")) 
                    {
                        // Getting the ABINIT VONAME from the portlet preferences for the EUMED VO
                        abinit_VONAME = portletPreferences.getValue("eumed_abinit_VONAME", "N/A");
                        // Getting the ABINIT TOPBDII from the portlet preferences for the EUMED VO
                        abinit_TOPBDII = portletPreferences.getValue("eumed_abinit_TOPBDII", "N/A");
                        
                        if (!ABINIT_Parameters[10].isEmpty()) {
                            log.info("\n- Submitting to the CE [ " 
                                    + ABINIT_Parameters[10] 
                                    + " ] in progress...");
                            
                            AbinitMultiJobSubmission
                                .setJobQueue(ABINIT_Parameters[10]);
                        } else { 
                        // Get the random CE for the ABINIT portlet
                        RANDOM_CE = getRandomCE(abinit_VONAME, abinit_TOPBDII, 
                                                abinit_SOFTWARE, MAXWallClockTime, "");
                        
                        log.info("\n- Submitting to the CE [ " 
                                + RANDOM_CE 
                                + " ] in progress...");
                        
                        AbinitMultiJobSubmission
                                .setJobQueue(RANDOM_CE.toString().trim());
                        }
                    }
                    
                    if (gisela_abinit_ENABLEINFRASTRUCTURE != null &&
                        gisela_abinit_ENABLEINFRASTRUCTURE.equals("gisela")) 
                    {
                        // Getting the ABINIT VONAME from the portlet preferences for the GISELA VO
                        abinit_VONAME = portletPreferences.getValue("gisela_abinit_VONAME", "N/A");
                        // Getting the ABINIT TOPBDII from the portlet preferences for the GISELA VO
                        abinit_TOPBDII = portletPreferences.getValue("gisela_abinit_TOPBDII", "N/A");
                        
                        if (!ABINIT_Parameters[10].isEmpty()) {
                            log.info("\n- Submitting to the CE [ " 
                                    + ABINIT_Parameters[10] 
                                    + " ] in progress...");
                            
                            AbinitMultiJobSubmission
                                .setJobQueue(ABINIT_Parameters[10]);
                        } else { 
                        // Get the random CE for the ABINIT portlet
                        RANDOM_CE = getRandomCE(abinit_VONAME, abinit_TOPBDII, 
                                                abinit_SOFTWARE, MAXWallClockTime, "");
                        
                        log.info("\n- Submitting to the CE [ " 
                                + RANDOM_CE 
                                + " ] in progress...");
                        
                        AbinitMultiJobSubmission
                                .setJobQueue(RANDOM_CE.toString().trim());
                        }
                    }
                    
                    if (see_abinit_ENABLEINFRASTRUCTURE != null &&
                        see_abinit_ENABLEINFRASTRUCTURE.equals("see")) 
                    {
                        // Getting the ABINIT VONAME from the portlet preferences for the SEE VO
                        abinit_VONAME = portletPreferences.getValue("see_abinit_VONAME", "N/A");
                        // Getting the ABINIT TOPBDII from the portlet preferences for the SEE VO
                        abinit_TOPBDII = portletPreferences.getValue("see_abinit_TOPBDII", "N/A");
                        
                        if (!ABINIT_Parameters[10].isEmpty()) {
                            log.info("\n- Submitting to the CE [ " 
                                    + ABINIT_Parameters[10] 
                                    + " ] in progress...");
                            
                            AbinitMultiJobSubmission
                                .setJobQueue(ABINIT_Parameters[10]);
                        } else { 
                        // Get the random CE for the ABINIT portlet
                        RANDOM_CE = getRandomCE(abinit_VONAME, abinit_TOPBDII, 
                                                abinit_SOFTWARE, MAXWallClockTime, "");
                        
                        log.info("\n- Submitting to the CE [ " 
                                + RANDOM_CE 
                                + " ] in progress...");
                        
                        AbinitMultiJobSubmission
                                .setJobQueue(RANDOM_CE.toString().trim());
                        }
                    }
                }
                
                InetAddress addr = InetAddress.getLocalHost();
                //Company company;
                
                try {
                    company = PortalUtil.getCompany(request);
                    String gateway = company.getName();
                    
                    // Send a notification email to the user if enabled.
                    if (ABINIT_Parameters[9]!=null)
                        if ( (SMTP_HOST==null) || 
                             (SMTP_HOST.trim().equals("")) ||
                             (SMTP_HOST.trim().equals("N/A")) ||
                             (SENDER_MAIL==null) || 
                             (SENDER_MAIL.trim().equals("")) ||
                             (SENDER_MAIL.trim().equals("N/A"))
                           )
                        log.info ("\nThe Notification Service is not properly configured!!");
                    else {
                            // Setting the user's email for notifications
                            AbinitMultiJobSubmission
                                .setUserEmail(emailAddress);
                                
                            // Setting the Sender
                            if (!SENDER_MAIL.isEmpty())
                                AbinitMultiJobSubmission
                                    .setSenderEmail(SENDER_MAIL);
                            
                            sendHTMLEmail(username, 
                                          emailAddress, 
                                          SENDER_MAIL, 
                                          SMTP_HOST, 
                                          "ABINIT-7.6.4", 
                                          gateway);
                    }      
                    
                    log.info("\n- Submission in progress...");
                    AbinitMultiJobSubmission.submitJobAsync(
                            infrastructure,
                            username,
                            addr.getHostAddress()+":8162",
                            Integer.valueOf(abinit_APPID),
                            ABINIT_Parameters[6]);
                    
                } catch (PortalException ex) {
                    Logger.getLogger(Abinit.class.getName()).log(Level.SEVERE, null, ex);
                } catch (SystemException ex) {
                    Logger.getLogger(Abinit.class.getName()).log(Level.SEVERE, null, ex);
                }                                    
            } // end PROCESS ACTION [ SUBMIT_ABINIT_PORTLET ]
        } catch (PortalException ex) {
            Logger.getLogger(Abinit.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SystemException ex) {
            Logger.getLogger(Abinit.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void serveResource(ResourceRequest request, ResourceResponse response)
                throws PortletException, IOException
    {
        //super.serveResource(request, response);
        PortletPreferences portletPreferences = (PortletPreferences) request.getPreferences();

        final String action = (String) request.getParameter("action");

        if (action.equals("get-ratings")) {
            //Get CE Ratings from the portlet preferences
            String abinit_CE = (String) request.getParameter("abinit_CE");

            String json = "{ \"avg\":\"" + 
                    portletPreferences.getValue(abinit_CE+"_avg", "0.0") +
                    "\", \"cnt\":\"" + portletPreferences.getValue(abinit_CE+"_cnt", "0") + "\"}";

            response.setContentType("application/json");
            response.getPortletOutputStream().write( json.getBytes() );

        } else if (action.equals("set-ratings")) {

            String abinit_CE = (String) request.getParameter("abinit_CE");
            int vote = Integer.parseInt(request.getParameter("vote"));

             double avg = Double.parseDouble(portletPreferences.getValue(abinit_CE+"_avg", "0.0"));
             long cnt = Long.parseLong(portletPreferences.getValue(abinit_CE+"_cnt", "0"));

             portletPreferences.setValue(abinit_CE+"_avg", Double.toString(((avg*cnt)+vote) / (cnt +1)));
             portletPreferences.setValue(abinit_CE+"_cnt", Long.toString(cnt+1));

             portletPreferences.store();
        }
    }

    private void storeFile (String fileName, String fileContent) 
                              throws IOException 
    {         
        log.info("\n- Writing ASCII file [ " + fileName + " ]");
        
        BufferedWriter writer = 
                new BufferedWriter(new FileWriter(fileName));
        
        String[] LIST = fileContent.split(",");           
        for(String output_file: LIST) {            
            writer.write(output_file);
            writer.write("\n");
        }
        
        writer.close();                
    }
    
    public void getArchive (String _abinit_tar_file, String _files) 
            throws Exception
    {
        OutputStream tar_output = null;
        File tar_input_file = null;
        TarArchiveEntry tar_file = null;
        
        log.info("\n Creating the archive [ " 
                + _abinit_tar_file + " ]");
        
        tar_output = new FileOutputStream(new File(_abinit_tar_file));
        
        //Create Archive Output Stream that attaches File Output Stream 
        //and specifies TAR as type of compression 
        ArchiveOutputStream _abinit_tar = new ArchiveStreamFactory()
        .createArchiveOutputStream(ArchiveStreamFactory.TAR, tar_output);
            
        String[] list = _files.split(" ");
        for (String filename : list) 
        {
            log.info("\n Adding the file = [ " + filename 
                    + " ] to the archive "
                    + _abinit_tar_file);
            
            // Add filename to the archive 
            tar_input_file = new File (filename);
            tar_file = new TarArchiveEntry(tar_input_file);
            
            // Length of the file needs to be set using setSize method
            tar_file.setSize(tar_input_file.length());
            
            _abinit_tar.putArchiveEntry(tar_file);
            IOUtils.copy(new FileInputStream(tar_input_file), _abinit_tar);
            
            // Close Archieve entry, write trailer information 
            _abinit_tar.closeArchiveEntry();
        }        
        
        _abinit_tar.finish(); 
        // Close output stream, our files are zipped
        tar_output.close();
        
        log.info("\n The archive [ " + _abinit_tar_file 
                + " ] has been successgully created!");
    }

    // Upload ABINIT input files
    public String[] uploadAbinitSettings(ActionRequest actionRequest,
                                          ActionResponse actionResponse, 
                                          String username)
    {
        String[] ABINIT_Parameters = new String [11];
        boolean status;

        // Check that we have a file upload request
        boolean isMultipart = PortletFileUpload.isMultipartContent(actionRequest);

        if (isMultipart) {
            // Create a factory for disk-based file items.
            DiskFileItemFactory factory = new DiskFileItemFactory();

            // Set factory constrains
            File ABINIT_Repository = new File ("/tmp");
            if (!ABINIT_Repository.exists()) status = ABINIT_Repository.mkdirs();
            factory.setRepository(ABINIT_Repository);

            // Create a new file upload handler.
            PortletFileUpload upload = new PortletFileUpload(factory);

            try {
                    // Parse the request
                    List items = upload.parseRequest(actionRequest);
                    // Processing items
                    Iterator iter = items.iterator();

                    while (iter.hasNext())
                    {
                        FileItem item = (FileItem) iter.next();
                        String fieldName = item.getFieldName();
                        
                        DateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
                        String timeStamp = dateFormat.format(Calendar.getInstance().getTime());

                        // Processing a regular form field
                        if ( item.isFormField() )
                        {   
                            if (fieldName.equals("abinit_model"))
                                ABINIT_Parameters[0]=item.getString();
                            
                            if (fieldName.equals("abinit_CPUcores"))
                                ABINIT_Parameters[1]=item.getString();
                                
                            if (fieldName.equals("abinit_desc"))
                                if (item.getString().equals("Please, insert here a description"))
                                    ABINIT_Parameters[6]="ABINIT Simulation Started";
                                else
                                    ABINIT_Parameters[6]=item.getString();                            
                                                        
                            if (fieldName.equals("abinit_CE"))
                                ABINIT_Parameters[10]=item.getString();
                            
                        } else {
                            // Processing a file upload
                            if (fieldName.equals("abinit_file"))
                            {                                                               
                                log.info("\n- Uploading the file: "
                                       + "\n[ " + item.getName() + " ]"
                                       + "\n[ " + item.getContentType() + " ]"
                                       + "\n[ " + item.getSize() + "KBytes ]");                                                                

                                // Writing the file to disk
                                String uploadAbinitFile = 
                                        ABINIT_Repository +
                                        "/" + timeStamp + "_" + username +
                                        "_" + item.getName();
                                
                                String _tmpuploadAbinitFile = 
                                        ABINIT_Repository +
                                        "/" + timeStamp + "_" + username +
                                        "_temp";

                               item.write(new File(_tmpuploadAbinitFile));
                                
                                ABINIT_Parameters[2]=
                                        RemoveCarriageReturn(
                                        _tmpuploadAbinitFile, 
                                        uploadAbinitFile);                                
                            }
                            
                            if (fieldName.equals("abinit_pseudofile"))
                            {                                                               
                                log.info("\n- Uploading the file: "
                                       + "\n[ " + item.getName() + " ]"
                                       + "\n[ " + item.getContentType() + " ]"
                                       + "\n[ " + item.getSize() + "KBytes ]");                                                                

                                // Writing the file to disk
                                String uploadAbinitFile = 
                                        ABINIT_Repository +
                                        "/" + timeStamp +
                                        "_" + username +
                                        "_" + item.getName();
                                
                                String _tmpuploadAbinitFile = 
                                        ABINIT_Repository +
                                        "/" + timeStamp + "_" + username +
                                        "_temp";

                                item.write(new File(_tmpuploadAbinitFile));
                                                                
                                ABINIT_Parameters[3] +=
                                        RemoveCarriageReturn(
                                        _tmpuploadAbinitFile, 
                                        uploadAbinitFile) + " "; 
                            }
                            
                            if (fieldName.equals("abinit_fileoffiles"))
                            {                                                               
                                log.info("\n- Uploading the file: "
                                       + "\n[ " + item.getName() + " ]"
                                       + "\n[ " + item.getContentType() + " ]"
                                       + "\n[ " + item.getSize() + "KBytes ]");                                                                

                                // Writing the file to disk
                                String uploadAbinitFile = 
                                        ABINIT_Repository +
                                        "/" + timeStamp +
                                        "_" + username +
                                        "_" + item.getName();
                                
                                String _tmpuploadAbinitFile = 
                                        ABINIT_Repository +
                                        "/" + timeStamp + "_" + username +
                                        "_temp";

                                item.write(new File(_tmpuploadAbinitFile));
                                
                                ABINIT_Parameters[4]=
                                        RemoveCarriageReturn(
                                        _tmpuploadAbinitFile, 
                                        uploadAbinitFile);                                
                            }
                        }
                        
                        if (fieldName.equals("abinit_outputfiles"))
                                ABINIT_Parameters[5]=item.getString();
                        
                        if (fieldName.equals("EnableNotification"))
                                ABINIT_Parameters[9]=item.getString();
                        
                        if (fieldName.equals("EnableDemo"))
                                ABINIT_Parameters[8]=item.getString();
                        
                        if (fieldName.equals("abinit_maxwallclocktime"))
                                ABINIT_Parameters[7]=item.getString();
                    } // end while
            } catch (FileUploadException ex) {
              Logger.getLogger(Abinit.class.getName()).log(Level.SEVERE, null, ex);
            } catch (Exception ex) {
              Logger.getLogger(Abinit.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return ABINIT_Parameters;
    }
    
    // Retrieve a random Computing Element
    // matching the Software Tag for the application    
    public String getRandomCE(String abinit_VONAME,
                              String abinit_TOPBDII,
                              String abinit_SOFTWARE,
                              Integer abinit_MaxCPUTime,
                              String selected)
                              throws PortletException, IOException
    {
        String randomWCT_CE = null;
        String randomCE = null;
        List<String> CEqueues = null;
        BDII bdii = null;                   
                        
        String[] SOFTWARE_LIST = abinit_SOFTWARE.split(",");
        
        for(String SOFTWARE: SOFTWARE_LIST)
        {
            log.info("\n- Querying the Information System [ "
                      + abinit_TOPBDII
                      + " ] and fetching a random CE matching the SW tag [ VO-"
                      + abinit_VONAME
                      + "-"
                      + SOFTWARE + " ]");  

            try {               

                bdii = new BDII( new URI(abinit_TOPBDII) );               
                
                // Get the list of the available queues
                CEqueues = bdii.queryCEQueues(abinit_VONAME);                
                                
                // Get the random CE matching the Software and 
                // the MaxWallClockTime reqs.
                randomWCT_CE =          
                        bdii.getRandomCEFromSWTag_MaxWallClockTime(  
                        "VO-" + abinit_VONAME + "-" + SOFTWARE,
                        abinit_VONAME,
                        abinit_MaxCPUTime);
                
                // Fetching the Queues
                for (String CEqueue:CEqueues) {                        
                        if (CEqueue.contains(randomWCT_CE))
                            randomCE=CEqueue;
                }                                

            } catch (URISyntaxException ex) {
                    Logger.getLogger(Abinit.class.getName()).log(Level.SEVERE, null, ex);
            } catch (Exception ex) {
                    Logger.getLogger(Abinit.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        if (selected.isEmpty()) 
        log.info("\n- [getRandomCE] Selected *randomly* the following cluster = " + randomCE);
        
        return randomCE;
    }        

    // Retrieve the list of Computing Elements
    // matching the Software Tag for the ABINIT application    
    public List<String> getListofCEForSoftwareTag(String abinit_VONAME,
                                                  String abinit_TOPBDII,
                                                  String abinit_SOFTWARE)
                                throws PortletException, IOException
    {
        List<String> CEs_list = null;
        BDII bdii = null;
        
        log.info("\n- Querying the Information System [ "
                     + abinit_TOPBDII
                     + " ] and looking for CEs matching SW tag [ VO-"
                     + abinit_VONAME
                     + "-"
                     + abinit_SOFTWARE + " ]");  

            try {
                    bdii = new BDII( new URI(abinit_TOPBDII) );                
                    CEs_list = bdii.queryCEForSWTag(
                               "VO-"
                               + abinit_VONAME
                               + "-"
                               + abinit_SOFTWARE);

            } catch (URISyntaxException ex) {
                    Logger.getLogger(Abinit.class.getName()).log(Level.SEVERE, null, ex);
            } catch (Exception ex) {
                    Logger.getLogger(Abinit.class.getName()).log(Level.SEVERE, null, ex);
            }        

        return CEs_list;
    }

    // Get the GPS location of the given grid resource
    public String[] getCECoordinate(RenderRequest request,
                                    String CE)
                                    throws PortletException, IOException
    {
        String[] GPS_locations = null;
        BDII bdii = null;

        PortletPreferences portletPreferences =
                (PortletPreferences) request.getPreferences();

        // Getting the ABINIT TOPPBDII from the portlet preferences
        String garuda_abinit_TOPBDII = 
                portletPreferences.getValue("garuda_abinit_TOPBDII", "N/A");
        String eumed_abinit_TOPBDII = 
                portletPreferences.getValue("eumed_abinit_TOPBDII", "N/A");
        String sagrid_abinit_TOPBDII = 
                portletPreferences.getValue("sagrid_abinit_TOPBDII", "N/A");
        String see_abinit_TOPBDII = 
                portletPreferences.getValue("see_abinit_TOPBDII", "N/A");
        String gisela_abinit_TOPBDII = 
                portletPreferences.getValue("gisela_abinit_TOPBDII", "N/A");
        
        // Getting the ABINIT ENABLEINFRASTRUCTURE from the portlet preferences
        String abinit_ENABLEINFRASTRUCTURE = 
                portletPreferences.getValue("abinit_ENABLEINFRASTRUCTURE", "N/A");

        try {
            if ( abinit_ENABLEINFRASTRUCTURE.equals("garuda") )
                 bdii = new BDII( new URI(garuda_abinit_TOPBDII) );

            if ( abinit_ENABLEINFRASTRUCTURE.equals("eumed") )
                 bdii = new BDII( new URI(eumed_abinit_TOPBDII) );
                
            if ( abinit_ENABLEINFRASTRUCTURE.equals("sagridd") )
                 bdii = new BDII( new URI(sagrid_abinit_TOPBDII) );

            if ( abinit_ENABLEINFRASTRUCTURE.equals("see") )
                 bdii = new BDII( new URI(see_abinit_TOPBDII) );
                
            if ( abinit_ENABLEINFRASTRUCTURE.equals("gisela") )
                 bdii = new BDII( new URI(gisela_abinit_TOPBDII) );

            GPS_locations = bdii.queryCECoordinate("ldap://" + CE + ":2170");

        } catch (URISyntaxException ex) {
                Logger.getLogger(Abinit.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
                Logger.getLogger(Abinit.class.getName()).log(Level.SEVERE, null, ex);
        }

        return GPS_locations;
    }
    
    private void sendHTMLEmail (String USERNAME,
                                String TO, 
                                String FROM, 
                                String SMTP_HOST, 
                                String ApplicationAcronym,
                                String GATEWAY)
    {
                
        log.info("\n- Sending email notification to the user " + USERNAME + " [ " + TO + " ]");
        
        log.info("\n- SMTP Server = " + SMTP_HOST);
        log.info("\n- Sender = " + FROM);
        log.info("\n- Receiver = " + TO);
        log.info("\n- Application = " + ApplicationAcronym);
        log.info("\n- Gateway = " + GATEWAY);        
        
        // Assuming you are sending email from localhost
        String HOST = "localhost";
        
        // Get system properties
        Properties properties = System.getProperties();
        properties.setProperty(SMTP_HOST, HOST);
        
        // Get the default Session object.
        Session session = Session.getDefaultInstance(properties);
        
        try {
         // Create a default MimeMessage object.
         MimeMessage message = new MimeMessage(session);

         // Set From: header field of the header.
         message.setFrom(new InternetAddress(FROM));

         // Set To: header field of the header.
         message.addRecipient(Message.RecipientType.TO, new InternetAddress(TO));
         //message.addRecipient(Message.RecipientType.CC, new InternetAddress(FROM));

         // Set Subject: header field
         message.setSubject(" [liferay-sg-gateway] - [ " + GATEWAY + " ] ");

	 Date currentDate = new Date();
	 currentDate.setTime (currentDate.getTime());

         // Send the actual HTML message, as big as you like
         message.setContent(
	 "<br/><H4>" +         
         "<img src=\"http://fbcdn-profile-a.akamaihd.net/hprofile-ak-snc6/195775_220075701389624_155250493_n.jpg\" width=\"100\">Science Gateway Notification" +
	 "</H4><hr><br/>" +
         "<b>Description:</b> Notification for the application <b>[ " + ApplicationAcronym + " ]</b><br/><br/>" +         
         "<i>The application has been successfully submitted from the [ " + GATEWAY + " ]</i><br/><br/>" +
         "<b>TimeStamp:</b> " + currentDate + "<br/><br/>" +
	 "<b>Disclaimer:</b><br/>" +
	 "<i>This is an automatic message sent by the Science Gateway based on Liferay technology.<br/>" + 
	 "If you did not submit any jobs through the Science Gateway, please " +
         "<a href=\"mailto:" + FROM + "\">contact us</a></i>",
	 "text/html");

         // Send message
         Transport.send(message);         
      } catch (MessagingException ex) { 
          Logger.getLogger(Abinit.class.getName()).log(Level.SEVERE, null, ex);          
      }
    }
    
    public String RemoveCarriageReturn (String InputFileName, String OutputFileName)             
    {
        // Remove the carriage return char from a named file.                                
        FileInputStream fis;
        try {
            
            fis = new FileInputStream(InputFileName);
            BufferedReader in = new BufferedReader(new InputStreamReader(fis));
            
            File fout = new File(OutputFileName);
            FileOutputStream fos = new FileOutputStream(fout);                                
            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(fos));
            
            // The pattern matches control characters
            Pattern p = Pattern.compile("\r");
            Matcher m = p.matcher("");
            String aLine = null;

            try {
                while((aLine = in.readLine()) != null) {
                    m.reset(aLine);
                    //Replaces control characters with an empty string.
                    String result = m.replaceAll("");                    
                    out.write(result);
                    out.newLine();
                }
                out.close();                
            } catch (IOException ex) {
                Logger.getLogger(Abinit.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Abinit.class.getName()).log(Level.SEVERE, null, ex);
        }                                                                                
        
        log.info("\n- Writing the user's stripped file: [ " + 
                  OutputFileName.toString() + " ] to disk");
        
        return OutputFileName;
    }
}
