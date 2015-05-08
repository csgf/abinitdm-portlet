<%
/**************************************************************************
Copyright (c) 2011-2015:
Istituto Nazionale di Fisica Nucleare (INFN), Italy
Consorzio COMETA (COMETA), Italy
    
See http://www.infn.it and and http://www.consorzio-lato.it for details 
on the copyright holders.
    
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
**************************************************************************/
%>
<%@ page import="com.liferay.portal.kernel.util.WebKeys" %>
<%@ page import="com.liferay.portal.util.PortalUtil" %>
<%@ page import="com.liferay.portal.model.Company" %>
<%@ page import="javax.portlet.*" %>
<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>
<%@ taglib uri="http://liferay.com/tld/theme" prefix="liferay-theme" %>
<%@ taglib uri="http://liferay.com/tld/ui" prefix="liferay-ui" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<portlet:defineObjects/>

<%
  Company company = PortalUtil.getCompany(request);
  String gateway = company.getName();
%>

<jsp:useBean id="GPS_table" class="java.lang.String" scope="request"/>
<jsp:useBean id="GPS_queue" class="java.lang.String" scope="request"/>

<jsp:useBean id="lato_abinit_INFRASTRUCTURE" class="java.lang.String" scope="request"/>
<jsp:useBean id="lato_abinit_LOGIN" class="java.lang.String" scope="request"/>
<jsp:useBean id="lato_abinit_PASSWD" class="java.lang.String" scope="request"/>
<jsp:useBean id="lato_abinit_WMS" class="java.lang.String" scope="request"/>
<jsp:useBean id="lato_abinit_MYPROXYSERVER" class="java.lang.String" scope="request"/>
<jsp:useBean id="lato_abinit_ETOKENSERVER" class="java.lang.String" scope="request"/>
<jsp:useBean id="lato_abinit_PORT" class="java.lang.String" scope="request"/>
<jsp:useBean id="lato_abinit_ROBOTID" class="java.lang.String" scope="request"/>
<jsp:useBean id="lato_abinit_ROLE" class="java.lang.String" scope="request"/>
<jsp:useBean id="lato_abinit_RENEWAL" class="java.lang.String" scope="request"/>
<jsp:useBean id="lato_abinit_DISABLEVOMS" class="java.lang.String" scope="request"/>

<jsp:useBean id="garuda_abinit_INFRASTRUCTURE" class="java.lang.String" scope="request"/>
<jsp:useBean id="garuda_abinit_VONAME" class="java.lang.String" scope="request"/>
<jsp:useBean id="garuda_abinit_TOPBDII" class="java.lang.String" scope="request"/>
<jsp:useBean id="garuda_abinit_WMS" class="java.lang.String" scope="request"/>
<jsp:useBean id="garuda_abinit_MYPROXYSERVER" class="java.lang.String" scope="request"/>
<jsp:useBean id="garuda_abinit_ETOKENSERVER" class="java.lang.String" scope="request"/>
<jsp:useBean id="garuda_abinit_PORT" class="java.lang.String" scope="request"/>
<jsp:useBean id="garuda_abinit_ROBOTID" class="java.lang.String" scope="request"/>
<jsp:useBean id="garuda_abinit_ROLE" class="java.lang.String" scope="request"/>
<jsp:useBean id="garuda_abinit_RENEWAL" class="java.lang.String" scope="request"/>
<jsp:useBean id="garuda_abinit_DISABLEVOMS" class="java.lang.String" scope="request"/>

<jsp:useBean id="eumed_abinit_INFRASTRUCTURE" class="java.lang.String" scope="request"/>
<jsp:useBean id="eumed_abinit_VONAME" class="java.lang.String" scope="request"/>
<jsp:useBean id="eumed_abinit_TOPBDII" class="java.lang.String" scope="request"/>
<jsp:useBean id="eumed_abinit_WMS" class="java.lang.String" scope="request"/>
<jsp:useBean id="eumed_abinit_MYPROXYSERVER" class="java.lang.String" scope="request"/>
<jsp:useBean id="eumed_abinit_ETOKENSERVER" class="java.lang.String" scope="request"/>
<jsp:useBean id="eumed_abinit_PORT" class="java.lang.String" scope="request"/>
<jsp:useBean id="eumed_abinit_ROBOTID" class="java.lang.String" scope="request"/>
<jsp:useBean id="eumed_abinit_ROLE" class="java.lang.String" scope="request"/>
<jsp:useBean id="eumed_abinit_RENEWAL" class="java.lang.String" scope="request"/>
<jsp:useBean id="eumed_abinit_DISABLEVOMS" class="java.lang.String" scope="request"/>

<jsp:useBean id="sagrid_abinit_INFRASTRUCTURE" class="java.lang.String" scope="request"/>
<jsp:useBean id="sagrid_abinit_VONAME" class="java.lang.String" scope="request"/>
<jsp:useBean id="sagrid_abinit_TOPBDII" class="java.lang.String" scope="request"/>
<jsp:useBean id="sagrid_abinit_WMS" class="java.lang.String" scope="request"/>
<jsp:useBean id="sagrid_abinit_MYPROXYSERVER" class="java.lang.String" scope="request"/>
<jsp:useBean id="sagrid_abinit_ETOKENSERVER" class="java.lang.String" scope="request"/>
<jsp:useBean id="sagrid_abinit_PORT" class="java.lang.String" scope="request"/>
<jsp:useBean id="sagrid_abinit_ROBOTID" class="java.lang.String" scope="request"/>
<jsp:useBean id="sagrid_abinit_ROLE" class="java.lang.String" scope="request"/>
<jsp:useBean id="sagrid_abinit_RENEWAL" class="java.lang.String" scope="request"/>
<jsp:useBean id="sagrid_abinit_DISABLEVOMS" class="java.lang.String" scope="request"/>

<jsp:useBean id="see_abinit_INFRASTRUCTURE" class="java.lang.String" scope="request"/>
<jsp:useBean id="see_abinit_TOPBDII" class="java.lang.String" scope="request"/>
<jsp:useBean id="see_abinit_WMS" class="java.lang.String" scope="request"/>
<jsp:useBean id="see_abinit_VONAME" class="java.lang.String" scope="request"/>
<jsp:useBean id="see_abinit_ETOKENSERVER" class="java.lang.String" scope="request"/>
<jsp:useBean id="see_abinit_MYPROXYSERVER" class="java.lang.String" scope="request"/>
<jsp:useBean id="see_abinit_PORT" class="java.lang.String" scope="request"/>
<jsp:useBean id="see_abinit_ROBOTID" class="java.lang.String" scope="request"/>
<jsp:useBean id="see_abinit_ROLE" class="java.lang.String" scope="request"/>
<jsp:useBean id="see_abinit_RENEWAL" class="java.lang.String" scope="request"/>
<jsp:useBean id="see_abinit_DISABLEVOMS" class="java.lang.String" scope="request"/>

<jsp:useBean id="gisela_abinit_INFRASTRUCTURE" class="java.lang.String" scope="request"/>
<jsp:useBean id="gisela_abinit_TOPBDII" class="java.lang.String" scope="request"/>
<jsp:useBean id="gisela_abinit_WMS" class="java.lang.String" scope="request"/>
<jsp:useBean id="gisela_abinit_VONAME" class="java.lang.String" scope="request"/>
<jsp:useBean id="gisela_abinit_ETOKENSERVER" class="java.lang.String" scope="request"/>
<jsp:useBean id="gisela_abinit_MYPROXYSERVER" class="java.lang.String" scope="request"/>
<jsp:useBean id="gisela_abinit_PORT" class="java.lang.String" scope="request"/>
<jsp:useBean id="gisela_abinit_ROBOTID" class="java.lang.String" scope="request"/>
<jsp:useBean id="gisela_abinit_ROLE" class="java.lang.String" scope="request"/>
<jsp:useBean id="gisela_abinit_RENEWAL" class="java.lang.String" scope="request"/>
<jsp:useBean id="gisela_abinit_DISABLEVOMS" class="java.lang.String" scope="request"/>

<jsp:useBean id="abinit_ENABLEINFRASTRUCTURE" class="java.lang.String[]" scope="request"/>
<jsp:useBean id="abinit_APPID" class="java.lang.String" scope="request"/>
<jsp:useBean id="abinit_OUTPUT_PATH" class="java.lang.String" scope="request"/>
<jsp:useBean id="abinit_SOFTWARE" class="java.lang.String" scope="request"/>
<jsp:useBean id="TRACKING_DB_HOSTNAME" class="java.lang.String" scope="request"/>
<jsp:useBean id="TRACKING_DB_USERNAME" class="java.lang.String" scope="request"/>
<jsp:useBean id="TRACKING_DB_PASSWORD" class="java.lang.String" scope="request"/>
<jsp:useBean id="SMTP_HOST" class="java.lang.String" scope="request"/>
<jsp:useBean id="SENDER_MAIL" class="java.lang.String" scope="request"/>

<script type="text/javascript">
    var latlng2markers = [];
    var icons = [];
    
    icons["plus"] = new google.maps.MarkerImage(
          '<%= renderRequest.getContextPath()%>/images/plus_new.png',
          new google.maps.Size(16,16),
          new google.maps.Point(0,0),
          new google.maps.Point(8,8));
    
    icons["minus"] = new google.maps.MarkerImage(
          '<%= renderRequest.getContextPath()%>/images/minus_new.png',
          new google.maps.Size(16,16),
          new google.maps.Point(0,0),
          new google.maps.Point(8,8));
    
    icons["ce"] = new google.maps.MarkerImage(
            '<%= renderRequest.getContextPath()%>/images/ce-run.png',
            new google.maps.Size(16,16),
            new google.maps.Point(0,0),
            new google.maps.Point(8,8));
    
    function hideMarkers(markersMap,map) 
    {
        for( var k in markersMap) 
        {
            if (markersMap[k].markers.length >1) 
            {
                var n = markersMap[k].markers.length;
                var centerMark = new google.maps.Marker({
                    title: "Coordinates:" + markersMap[k].markers[0].getPosition().toString(),
                    position: markersMap[k].markers[0].getPosition(),
                    icon: icons["plus"]
                });
                
                for ( var i=0 ; i<n ; i++ ) 
                    markersMap[k].markers[i].setVisible(false);
                    
                centerMark.setMap(map);
                google.maps.event.addListener(centerMark, 'click', function() 
                {
                    var markersMap = latlng2markers;
                    var k = this.getPosition().toString();
                    var visibility = markersMap[k].markers[0].getVisible();
                    if (visibility == false ) {
                        splitMarkersOnCircle(markersMap[k].markers,
                        markersMap[k].connectors,
                        this.getPosition(),
                        map);
                            
                        this.setIcon(icons["minus"]);
                    } else {
                        var n = markersMap[k].markers.length;
                        for ( var i=0 ; i<n ; i++ ) {
                            markersMap[k].markers[i].setVisible(false);
                            markersMap[k].connectors[i].setMap(null);
                        }
                        markersMap[k].connectors = [];
                        this.setIcon(icons["plus"]);
                    }
                 });
           }
        }
    }
    
    function splitMarkersOnCircle(markers, connectors, center, map) 
    {
        var z = map.getZoom();
        var r = 64.0 / (z*Math.exp(z/2));
        var n = markers.length;
        var dtheta = 2.0 * Math.PI / n
        var theta = 0;
            
        for ( var i=0 ; i<n ; i++ ) 
        {
            var X = center.lat() + r*Math.cos(theta);
            var Y = center.lng() + r*Math.sin(theta);
            markers[i].setPosition(new google.maps.LatLng(X,Y));
            markers[i].setVisible(true);
            theta += dtheta;
                
            var line = new google.maps.Polyline({
                path: [center,new google.maps.LatLng(X,Y)],
                clickable: false,
                strokeColor: "#0000ff",
                strokeOpacity: 1,
                strokeWeight: 2
             });
                
             line.setMap(map);
             connectors.push(line);
        }
    }
    
    function updateAverage(name) 
    {        
        $.getJSON('<portlet:resourceURL><portlet:param name="action" value="get-ratings"/></portlet:resourceURL>&abinit_CE='+name,
        function(data) {                                               
            console.log(data.avg);
            $("#fake-stars-on").width(Math.round(parseFloat(data.avg)*20)); // 20 = 100(px)/5(stars)
            $("#fake-stars-cap").text(new Number(data.avg).toFixed(2) + " (" + data.cnt + ")");
        });                
    }
    
    // Create the Google Map JavaScript APIs V3
    function initialize(lat, lng, zoom) 
    {
        console.log(lat);
        console.log(lng);
        console.log(zoom);
        
        var myOptions = {
            zoom: zoom,
            center: new google.maps.LatLng(lat, lng),
            mapTypeId: google.maps.MapTypeId.ROADMAP
        }
        
        var map = new google.maps.Map(document.getElementById('map_canvas'), myOptions);  
        var image = new google.maps.MarkerImage('<%= renderRequest.getContextPath() %>/images/ce-run.png');                
        
        var strVar="";
        strVar += "<table>";
        strVar += "<tr>";
        strVar += "<td>";
        strVar += "Vote the resource availability";
        strVar += "<\/td>";
        strVar += "<\/tr>";
        strVar += "<tr><td>\&nbsp;\&nbsp;";
        strVar += "<\/td><\/tr>";
        
        strVar += "<tr>";
        strVar += "<td>";
        strVar += "Rating: <span id=\"stars-cap\"><\/span>";
        strVar += "<div id=\"stars-wrapper2\">";
        strVar += "<select name=\"selrate\">";
        strVar += "<option value=\"1\">Very poor<\/option>";
        strVar += "<option value=\"2\">Not that bad<\/option>";
        strVar += "<option value=\"3\" selected=\"selected\">Average<\/option>";
        strVar += "<option value=\"4\">Good<\/option>";
        strVar += "<option value=\"5\">Perfect<\/option>";
        strVar += "<\/select>";
        strVar += "<\/div>";
        strVar += "<\/td>";
        strVar += "<\/tr>";

        strVar += "<tr>";        
        strVar += "<td>";
        strVar += "Average: <span id=\"fake-stars-cap\"><\/span>";
        strVar += "";
        strVar += "<div id=\"fake-stars-off\" class=\"stars-off\" style=\"width:100px\">";
        strVar += "<div id=\"fake-stars-on\" class=\"stars-on\"><\/div>";
        strVar += "";
        strVar += "<\/div>";
        strVar += "<\/td>";
        strVar += "<\/tr>";
        strVar += "<\/table>";
    
        var data = <%= GPS_table %>;
        var queues = <%= GPS_queue %>;
        
        var infowindow = new google.maps.InfoWindow();
        google.maps.event.addListener(infowindow, 'closeclick', function() {
            this.setContent('');
        });
        
        var infowindowOpts = { 
            maxWidth: 200
        };
               
       infowindow.setOptions(infowindowOpts);       
        
        var markers = [];
        for( var key in data ){
                        
            var LatLong = new google.maps.LatLng(parseFloat(data[key]["LAT"]), 
                                                 parseFloat(data[key]["LNG"]));                    
            
            // Identify locations on the map
            var marker = new google.maps.Marker ({
                animation: google.maps.Animation.DROP,
                position: LatLong,
                icon: image,
                title : key
            });    
            
            // Add the market to the map
            marker.setMap(map);
  
            var latlngKey=marker.position.toString();
            if ( latlng2markers[latlngKey] == null )
                latlng2markers[latlngKey] = {markers:[], connectors:[]};
            
            latlng2markers[latlngKey].markers.push(marker);
            markers.push(marker);                         
        
            google.maps.event.addListener(marker, 'click', function() {
             
            var ce_hostname = this.title;
            
            google.maps.event.addListenerOnce(infowindow, 'domready', function() {
                                        
                    $("#stars-wrapper2").stars({
                        cancelShow: false, 
                        oneVoteOnly: true,
                        inputType: "select",
                        callback: function(ui, type, value)
                        { 
                            $.getJSON('<portlet:resourceURL><portlet:param name="action" value="set-ratings"/></portlet:resourceURL>' +
                                '&abinit_CE=' + ce_hostname + 
                                '&vote=' + value);
                            
                            updateAverage(ce_hostname);                      
                        }
                    });
                    
                    updateAverage(ce_hostname);               
                });              
                                                
                infowindow.setContent('<h3>' + ce_hostname + '</h3><br/>' + strVar);
                infowindow.open(map, this);
                                           
                var CE_queue = (queues[ce_hostname]["QUEUE"]);
                $('#abinit_CE').val(CE_queue);
                                
                marker.setMap(map);
            }); // function                             
        } // for
        
        hideMarkers(latlng2markers,map);
        var markerCluster = new MarkerClusterer(map, markers, {maxZoom: 3, gridSize: 20});
    }    
</script>

<script type="text/javascript">
    var EnabledInfrastructure = null;           
    var infrastructures = new Array();  
    var i = 0;    
    
    <c:forEach items="<%= abinit_ENABLEINFRASTRUCTURE %>" var="current">
    <c:choose>
    <c:when test="${current!=null}">
        infrastructures[i] = '<c:out value='${current}'/>';       
        i++;  
    </c:when>
    </c:choose>
    </c:forEach>
        
    for (var i = 0; i < infrastructures.length; i++) {
       console.log("Reading array = " + infrastructures[i]);
       if (infrastructures[i]=="lato") EnabledInfrastructure='lato';       
       if (infrastructures[i]=="garuda") EnabledInfrastructure='garuda';       
       if (infrastructures[i]=="eumed")  EnabledInfrastructure='eumed';
       if (infrastructures[i]=="sagrid")  EnabledInfrastructure='sagrid';
       if (infrastructures[i]=="see") EnabledInfrastructure='see';
       if (infrastructures[i]=="gisela") EnabledInfrastructure='gisela';
    }
    
    var NMAX = infrastructures.length;
    //console.log (NMAX);   

    $(document).ready(function() 
    {           
        var lat; var lng; var zoom;
        var found=0;
        var flag=true;
        
        if (parseInt(NMAX)>1) { 
            console.log ("More than one infrastructure has been configured!");
            $("#error_infrastructure img:last-child").remove();
            $('#error_infrastructure').append("<img width='70' border='0'> More than one infrastructure has been configured!");            
            lat=35;lng=24;zoom=4; found=1;
        } else if (EnabledInfrastructure=='lato') {
            console.log ("Adding the LATO HPC CLuster");
            $('#lato_abinit_ENABLEINFRASTRUCTURE').attr('checked','checked');
            lat=37,57;lng=14,28;zoom=7;found=1;
        } else if (EnabledInfrastructure=='garuda') {
            console.log ("Adding the GARUDA Infrastructure");
            $('#garuda_abinit_ENABLEINFRASTRUCTURE').attr('checked','checked');
            lat=29.15;lng=77.41;zoom=4;found=1;
        } else if (EnabledInfrastructure=='eumed') {
            console.log ("Adding the EUMEDGRID-SUPPORT Infrastructure");
            $('#eumed_abinit_ENABLEINFRASTRUCTURE').attr('checked','checked');
            lat=34;lng=20;zoom=4;found=1;
        } else if (EnabledInfrastructure=='sagrid') {
            console.log ("Adding the SAGRID Infrastructure");
            $('#sagrid_abinit_ENABLEINFRASTRUCTURE').attr('checked','checked');
            lat=2;lng=28;zoom=3;found=1;        
        } else if (EnabledInfrastructure=='see') {
            console.log ("Adding the SEE Infrastructure");
            $('#see_abinit_ENABLEINFRASTRUCTURE').attr('checked','checked');
            lat=48;lng=16;zoom=4;found=1;        
        } else if (EnabledInfrastructure=='gisela') {
            console.log ("Adding the Latin American Infrastructure");
            $('#gisela_abinit_ENABLEINFRASTRUCTURE').attr('checked','checked');
            lat=2;lng=-36;zoom=3;found=1;
        }
        
        if (found==0) { 
            console.log ("None of the available computing infrastructure(s) have been configured!");
            $("#error_infrastructure img:last-child").remove();            
            $('#error_infrastructure').append("<img width='40' border='0'> None of the available infrastructures have been configured!");
        }
        
        var accOpts = {
            change: function(e, ui) {                       
                $("<div style='width:650px; font-family: Tahoma,Verdana,sans-serif,Arial; font-size: 14px;'>").addClass("notify ui-corner-all").text(ui.newHeader.find("a").text() +
                    " was activated... ").appendTo("#error_message").fadeOut(2500, function(){ $(this).remove(); });               
                // Get the active option                
                var active = $("#accordion").accordion("option", "active");
                if (active==1) initialize(lat, lng, zoom);
            },
            autoHeight: false
        };
        
        // Create the accordions
        //$("#accordion").accordion({ autoHeight: false });
        $("#accordion").accordion(accOpts);
        
        $( "#slider-abinit-maxwallclocktime" ).slider({
            orientation: "horizontal",
            range: "min",
            value: 1440,
            min: 480,
            max: 2880,
            slide: function( event, ui ) {
                $( "#abinit_maxwallclocktime" ).val( ui.value );
                $( "input[type=hidden][name='abinit_maxwallclocktime']").val( ui.value);
            }
        });
        $( "#abinit_maxwallclocktime" ).val( $( "#slider-abinit-maxwallclocktime" ).slider( "value" ) );
                
        // Check file input size with jQuery (Max. 5MB)
        $('input[type=file][name=\'abinit_file\']').bind('change', function() 
        {
            console.log ("File Size = " + this.files[0].size);
            if (this.files[0].size/1000 > 5120) 
            {     
                // Remove the img and text (if any)
                $("#error_message img:last-child").remove();
                $("#error_message").empty();
                $('#error_message').append("The input file size must be less than 2.5MB");
                $("#error_message").css({"color":"red","font-size":"14px"});
                // Removing the input file
                $('input[type=\'file\'][name=\'abinit_file\']').val('');
                return false;
            }           
        });
                
        // Validate input form
        $('#commentForm').validate({
            rules: {
                abinit_file: { required: true },
                abinit_fileoffiles: { required: true },
                abinit_pseudofile: { required: true },
                abinit_model: { required: true }
            },
            
            invalidHandler: function(form, validator) 
            {
                var errors = validator.numberOfInvalids();
                if (errors) {
                    $("#error_message").css({"color":"red","font-size":"14px"});
                    $("#error_message").empty();
                    var message = errors == 1
                    ? ' You missed 1 field. It has been highlighted'
                    : ' You missed ' + errors + ' fields. They have been highlighted';                    
                    $('#error_message').append(message);
                    $("#error_message").show();
                } else $("#error_message").hide();                
            },
            
            submitHandler: function(form) 
            {
                //form.submit();
                var flag=true;
                
                // Remove the img and text error (if any)
                $("#error_message img:last-child").remove();
                $("#error_message").empty();
                
                if (!$('#EnableDemo').attr('checked')) 
                {
                    console.log ("ABINIT calculation parameters file selected!");
                    var filename = ($('input[type=file][name=\'abinit_file\']').val());                
                    var total = filename.indexOf("_");
                    console.log("Input file = " + filename 
                        + " [ " + total + " ]");
                    
                    if (total > -1) { 
                        $('#error_message').append("PLEASE, do NOT use \"_\" in the input file names!");
                        $("#error_message").css({"color":"red","font-size":"14px"});                   
                        return false; 
                        flag=false; 
                    }
                
                    var files = $('#abinit_pseudofile').prop("files");
                    var names = $.map(files, function(val) { return val.name; });
                    console.log (names);
                    
                    for (i=0; i<names.length; i++)
                    {
                        total = names[i].indexOf("_");
                        console.log("Pseudo Potential file = " + names[i] 
                            + " [ " + total + " ]");
                        
                        if (total > -1) { 
                            $('#error_message').append("PLEASE, do NOT use \"_\" in the psuedo potential file names!");
                            $("#error_message").css({"color":"red","font-size":"14px"});                   
                            return false; 
                            flag=false; 
                        }
                    }
                    
                    // Check if the uploaded file is a .in file.                    
                    filename = ($('input[type=file][name=\'abinit_fileoffiles\']').val());
                    total = filename.indexOf("_");                   
                    console.log("File of files = " + filename 
                        + " [ " + total + " ]");                    
                    
                    if (total > -1) { 
                        $('#error_message').append("PLEASE, do NOT use \"_\" in the file of files names!");
                        $("#error_message").css({"color":"red","font-size":"14px"});                   
                        return false; 
                        flag=false; 
                    }
                }
                
                // Check if the input settings are valid before to
                // display the warning message.            
                if (flag==true) 
                { 
                    $("#error_message")
                    .css({"color":"red",
                          "font-size":"14px", 
                          "font-family": "Tahoma,Verdana,sans-serif,Arial"}); 
                      
                    $('#error_message')
                    .append("Submission in progress... Please wait!")(30000, function(){ $(this).remove(); });
                        
                    $("#dialog-message").append("<p>Thanks for submitting a new request! <br/><br/>\n\
                    Your request has been successfully submitted by the Science Gateway.\n\
                    Have a look on MyJobs area to get more information about all your submitted jobs.</p>");
            
                    $("#dialog-message").dialog({
                    modal: true,
                    title: "ABINIT Notification Message",
                    height: 200,
                    width: 350                    
                    });                                
                }             
            }
        });                
                   
        // Roller
        $('#abinit_footer').rollchildren({
            delay_time         : 3000,
            loop               : true,
            pause_on_mouseover : true,
            roll_up_old_item   : true,
            speed              : 'slow'   
        });
        
        $("#stars-wrapper1").stars({
            cancelShow: false,
            captionEl: $("#stars-cap"),
            callback: function(ui, type, value)
            {
                $.getJSON("ratings.php", {rate: value}, function(json)
                {                                        
                    $("#fake-stars-on").width(Math.round( $("#fake-stars-off").width()/ui.options.items*parseFloat(json.avg) ));
                    $("#fake-stars-cap").text(json.avg + " (" + json.votes + ")");
                });
            }
        });         
    });
    
    function enableDemo(f)
    {
        if ($('#EnableDemo').attr('checked')) 
        {
            $('input[type=\'file\'][name=\'abinit_file\']')
            .attr('disabled','disabled');
            
            $('input[type=\'file\'][name=\'abinit_pseudofile\']')
            .attr('disabled','disabled');
            
            $('input[type=\'file\'][name=\'abinit_fileoffiles\']')
            .attr('disabled','disabled');
            
            if (($('input:radio[name=abinit_model]:checked').val())=="abinit_par")
            {                
                $("#drope_box_par").show()
                $("#drope_box_seq").hide()
            } else {
                $("#drope_box_par").hide()
                $("#drope_box_seq").show()
            }
        }    
        else {
            $('input[type=\'file\'][name=\'abinit_file\']')
            .removeAttr('disabled');
            
            $('input[type=\'file\'][name=\'abinit_pseudofile\']')
            .removeAttr('disabled');
            
            $('input[type=\'file\'][name=\'abinit_fileoffiles\']')
            .removeAttr('disabled');
            
            // Hide demo messages
            $("#drope_box_seq").hide()
            $("#drope_box_par").hide()
            
            $('#EnableDemo').removeAttr('checked')
        }
    }
    
    function enableCores(f)
    {        
        if (($('input:radio[name=abinit_model]:checked').val())=="abinit_par")
        {
            console.log("enableCores ON");
            $("#drope_cores").show()
        } else { 
            console.log("enableCores OFF");
            $("#drope_cores").hide()
        }
        
        // Hide demo messages
        $("#drope_box_seq").hide()
        $("#drope_box_par").hide()
        
        $('#EnableDemo').removeAttr('checked')
    }
     
    function enableNotification()
    {
        if ($('#EnableNotification').attr('checked')) 
            $("#drope_mail").show()
        else $("#drope_mail").hide()        
    }
    
    function enable_outputFile(f)
    {
        console.log("Adding extra output files to be retrieved.");
        if ($('#enable_output').is(':checked')) {                    
            //$('#abinit_outputfiles')
            //.html("Add here the additional list of output files you want to retrieve");
            $('#abinit_outputfiles').removeAttr('disabled');            
        } else {        
            $('#abinit_outputfiles').attr('disabled','disabled');            
        }
    }        
</script>

<br/>
<div id="dialog-message" title="Notification"></div>

<form enctype="multipart/form-data" 
      id="commentForm" 
      action="<portlet:actionURL><portlet:param name="ActionEvent" 
      value="SUBMIT_ABINIT_PORTLET"/></portlet:actionURL>"      
      method="POST">

<fieldset>
<legend>[ A B I N I T ]</legend>
<div style="margin-left:15px" id="error_message"></div>

<!-- Accordions -->
<div id="accordion" style="width:650px; font-family: Tahoma,Verdana,sans-serif,Arial; font-size: 14px;">
<h3><a href="#">
    <img width="35" 
         align="absmiddle"
         src="<%=renderRequest.getContextPath()%>/images/glass_numbers_1.png" />
    
    <b>Portlet Settings</b>
    
    <img width="50" 
         align="absmiddle" 
         src="<%=renderRequest.getContextPath()%>/images/about.png"/>
    </a>
</h3>
<div> <!-- Inizio primo accordion -->
<p>The current ABINIT portlet has been configured for:</p>
<table id="results" border="0" width="600">
    
<!-- LATO -->
<tr></tr>
<tr>
    <td>
        <c:forEach var="enabled" items="<%= abinit_ENABLEINFRASTRUCTURE %>">
            <c:if test="${enabled=='lato'}">
                <c:set var="results_lato" value="true"></c:set>
            </c:if>
        </c:forEach>

        <c:choose>
        <c:when test="${results_lato=='true'}">
        <input type="checkbox" 
               id="lato_abinit_ENABLEINFRASTRUCTURE"
               name="lato_abinit_ENABLEINFRASTRUCTURE"
               size="55px;"
               checked="checked"
               class="textfield ui-widget ui-widget-content required"
               disabled="disabled"/> The LATO Infrastructure
        
        <a href="http://www.polooncologicocefalu.it">
        <img width="120" 
                 border="0"
                 align="absmiddle"
                 src="<%= renderRequest.getContextPath()%>/images/LATO2.png" 
                 title="LATO - Laboratorio di Tecnologie Oncologiche, HSR Giglio"/>            
        </c:when>
        </c:choose>
    </td>
</tr>

<!-- GARUDA -->
<tr></tr>
<tr>
    <td>
        <c:forEach var="enabled" items="<%= abinit_ENABLEINFRASTRUCTURE %>">
            <c:if test="${enabled=='garuda'}">
                <c:set var="results_garuda" value="true"></c:set>
            </c:if>
        </c:forEach>

        <c:choose>
        <c:when test="${results_garuda=='true'}">
        <input type="checkbox" 
               id="garuda_abinit_ENABLEINFRASTRUCTURE"
               name="garuda_abinit_ENABLEINFRASTRUCTURE"
               size="55px;"
               checked="checked"
               class="textfield ui-widget ui-widget-content required"
               disabled="disabled"/> India's National Grid Computing Initiative
        
        <a href="http://cdac.in/">
        <img width="250" 
                 border="0"
                 align="absmiddle"
                 src="<%= renderRequest.getContextPath()%>/images/garuda_logo.png" 
                 title="The India's National Grid Computing Initiative"/>
        </a>
        </c:when>
        </c:choose>
    </td>
</tr>

<!-- SEE -->
<tr></tr>
<tr>
    <td>
        <c:forEach var="enabled" items="<%= abinit_ENABLEINFRASTRUCTURE %>">
            <c:if test="${enabled=='see'}">
                <c:set var="results_see" value="true"></c:set>
            </c:if>
        </c:forEach>

        <c:choose>
        <c:when test="${results_see=='true'}">
        <input type="checkbox"
               id="see_abinit_ENABLEINFRASTRUCTURE"
               name="see_abinit_ENABLEINFRASTRUCTURE"
               size="55px;"
               checked="checked"
               class="textfield ui-widget ui-widget-content required"
               disabled="disabled" /> The SEE Grid Infrastructure
        
        <a href="http://www.see-grid.org/">
        <img width="110" 
                 border="0"
                 align="absmiddle"
                 src="<%= renderRequest.getContextPath()%>/images/logo_see-grid.png" 
                 title="The SEE Grid Infrastructure"/>
        </a>
        </c:when>
        </c:choose>
    </td>
</tr>

<!-- SAGRID -->
<tr></tr>
<tr>
    <td>
        <c:forEach var="enabled" items="<%= abinit_ENABLEINFRASTRUCTURE %>">
            <c:if test="${enabled=='sagrid'}">
                <c:set var="results_sagrid" value="true"></c:set>
            </c:if>
        </c:forEach>

        <c:choose>
        <c:when test="${results_sagrid=='true'}">
        <input type="checkbox"
               id="sagrid_abinit_ENABLEINFRASTRUCTURE"
               name="sagrid_abinit_ENABLEINFRASTRUCTURE"
               size="55px;"
               checked="checked"
               class="textfield ui-widget ui-widget-content required"
               disabled="disabled" /> The South Africa Grid Infrastructure
        
        <a href="http://www.csir.co.za/meraka/">
        <img width="80" 
                 border="0"
                 align="absmiddle"
                 src="<%= renderRequest.getContextPath()%>/images/SANCG_logoTransparent-cropped-scaled.png" 
                 title="The South Africa Grid Infrastructure"/>
        </a>
        </c:when>
        </c:choose>
    </td>
</tr>

<!-- GISELA -->
<tr></tr>
<tr>
    <td>
        <c:forEach var="enabled" items="<%= abinit_ENABLEINFRASTRUCTURE %>">
            <c:if test="${enabled=='gisela'}">
                <c:set var="results_gisela" value="true"></c:set>
            </c:if>
        </c:forEach>

        <c:choose>
        <c:when test="${results_gisela=='true'}">
        <input type="checkbox"
               id="gisela_abinit_ENABLEINFRASTRUCTURE"
               name="gisela_abinit_ENABLEINFRASTRUCTURE"
               size="55px;"
               checked="checked"
               class="textfield ui-widget ui-widget-content required"
               disabled="disabled" /> The Latin America Grid Infrastructure
        
        <a href="http://www.gisela-grid.eu/">
        <img width="120" 
                 border="0"
                 align="absmiddle"
                 src="<%= renderRequest.getContextPath()%>/images/GISELA_Logo_B.png" 
                 title="The Latin America Grid Infrastructure"/>
        </a>
        </c:when>
        </c:choose>
    </td>
</tr>

<!-- EUMED -->
<tr></tr>
<tr>
    <td>
        <c:forEach var="enabled" items="<%= abinit_ENABLEINFRASTRUCTURE %>">
            <c:if test="${enabled=='eumed'}">
                <c:set var="results_eumed" value="true"></c:set>
            </c:if>
        </c:forEach>

        <c:choose>
        <c:when test="${results_eumed=='true'}">
        <input type="checkbox"
               id="eumed_abinit_ENABLEINFRASTRUCTURE"
               name="eumed_abinit_ENABLEINFRASTRUCTURE"
               size="55px;"
               checked="checked"
               class="textfield ui-widget ui-widget-content required"
               disabled="disabled" /> The Mediterranean Grid e-Infrastructure
        <a href="http://www.eumedgrid.eu/">
        <img width="140" 
                 border="0"
                 align="absmiddle"
                 src="<%= renderRequest.getContextPath()%>/images/eumedgrid_logo.png" 
                 title="The EUMEDGRID Support"/>
        </a>
        </c:when>
        </c:choose>
    </td>
</tr>
</table>
<br/>
<div style="margin-left:15px" 
     id="error_infrastructure" 
     style="width:690px; font-family: Tahoma,Verdana,sans-serif,Arial; font-size: 14px; display:none;">    
</div>
<br/>

<p align="center">
<img width="120" align="center" src="<%=renderRequest.getContextPath()%>/images/separatore.gif"/>
</p>

<table id="results" border="0">
<tr>
    <td>
    <a href="http://www.abinit.org">
    <img width="220" src="<%=renderRequest.getContextPath()%>/images/logo.png"/></a>    
    <p align="justify"> 
    A package whose main program allows one to find the total energy, charge density 
    and electronic structure of systems made of electrons and nuclei (molecules and periodic solids) 
    within <a href="http://dft.sandia.gov/">Density Functional Theory (DFT)</a>, using pseudo-potentials 
    and a planewave or wavelet basis. <br/><br/>
    ABINIT also includes options to optimize the geometry according to the DFT forces and stresses, 
    or to perform molecular dynamics simulations using these forces, or to generate dynamical matrices, 
    Born effective charges, and dielectric tensors, based on Density-Functional Perturbation Theory, 
    and many more properties.<br/>
    A short presentation of the ABINIT project (10 pages in PDF) is here
    <a href="http://www.abinit.org/about/presentation.pdf">
    <img width="35" src="<%=renderRequest.getContextPath()%>/images/PDF_Logo.png"/></a>
    <br/></p>
    </td>
    <!--td>&nbsp;&nbsp;&nbsp;</td-->    
</tr>
</table>

<p align="justify">
<u>Instructions for users</u>:<br/><br/>
~ The present service is based on the following standards and software frameworks: <br/>
<p align="center">
<a href="http://grid.in2p3.fr/jsaga/">
<img width="200" src="<%=renderRequest.getContextPath()%>/images/jsaga.png"/></a>

<a href="https://glibrary.ct.infn.it/">
<img width="150" src="<%=renderRequest.getContextPath()%>/images/gLibrary.png"/></a>

<a href="http://www.catania-science-gateways.it">
<img width="100" 
     src="<%=renderRequest.getContextPath()%>/images/CataniaScienceGateways.png"/></a>
</p>

<img width="20" 
     src="<%=renderRequest.getContextPath()%>/images/help.png" 
     title="Read the Help"/>
For further details, please click
<a href="<portlet:renderURL portletMode='HELP'><portlet:param name='action' value='./help.jsp' />
         </portlet:renderURL>" >here</a>
<br/><br/>

To use this portlet you should select:<br/>
~ the <u>Programming Model</u> (e.g. 'Sequential' or 'Parallel');<br/>
~ the <u>Number of Core</u> to be used (only if the parallel computation is chosen).
<br/><br/>
  
The portlet takes as input:<br/>
~ a file containing the calculation parameters (.in);<br/>
~ a pseudo potential file (.file);<br/>
~ the list of files (.files);<br/>
<br/>

Each run will produce:<br/>
~ std.txt: the standard output file;<br/>
~ std.err: the standard error file;<br/>
~ abinit.log: the application log file;<br/>
~ some additional log files;<br/>
~ optionally you may specify a list of output files (file1, file2, ...) to retrieve.<br/> 
  &nbsp;&nbsp;&nbsp;By default, only the std OUT/ERR files will be provided;<br/>
~ *.tar.gz: the application results available through the gLibrary Metadata Server.<br/><br/>

<img width="30" src="<%=renderRequest.getContextPath()%>/images/notice.png" title="Important note :)"/>
All the ABINIT log files, will be available through the <u>Browse Service</u>!<br/>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
For more details, please read the output.README file produced during the run!<br/><br/>

<p><img width="20" src="<%=renderRequest.getContextPath()%>/images/help.png" title="Get in touch!"/>
If you need any help, please contact the
<a href="mailto:credentials-admin@ct.infn.it?Subject=Request for Technical Support [<%=gateway%> Science Gateway]&Body=Describe Your Problems&CC=sg-licence@ct.infn.it"> administrator</a>
</p>

<liferay-ui:ratings
    className="<%= it.infn.ct.abinit.Abinit.class.getName()%>"
    classPK="<%= request.getAttribute(WebKeys.RENDER_PORTLET).hashCode()%>" />

<!--div id="pageNavPosition"></div-->
</div> <!-- Fine Primo Accordion -->

<h3><a href="#">
    <img width="35" 
         align="absmiddle" 
         src="<%=renderRequest.getContextPath()%>/images/glass_numbers_2.png" />
    
    <b>The Infrastructure(s)</b>
    <img width="50" 
         align="absmiddle"
         src="<%=renderRequest.getContextPath()%>/images/infrastructure.png"/>
    </a>
</h3>           
<div> <!-- Inizio Terzo accordion -->
    <a href="https://developers.google.com/maps/documentation/javascript/tutorial?hl=it/">
    <img width="150" 
             align="absmiddle"
             src="<%= renderRequest.getContextPath()%>/images/developers-logo.svg" 
             border="0" title="Google Maps JavaScript API v3"/>
    </a>
    
    <p align="justify">
    See with the Google Map APIs where the software has been successfully installed.
    Select the GPS location of the computing resource where you want run your application
    <u>OR, BETTER,</u> let the Science Gateway to choose the best one for you!<br/><br/>
    <img width="20" src="<%=renderRequest.getContextPath()%>/images/help.png"/>
    This option is NOT SUPPORTED if more than one computing infrastructure is enabled!
    </p>

    <table border="0">
    <tr>
        <td><legend>Legend</legend></td>
        <td>&nbsp;<img src="<%=renderRequest.getContextPath()%>/images/plus_new.png"/></td>
        <td>&nbsp;Split close sites&nbsp;</td>
        
        <td><img src="<%=renderRequest.getContextPath()%>/images/minus_new.png"/></td>
        <td>&nbsp;Unsplit close sites&nbsp;</td>
            
        <td><img src="<%=renderRequest.getContextPath()%>/images/ce-run.png"/></td>
        <td>&nbsp;Computing resource&nbsp;</td>
    </tr>    
    <tr><td>&nbsp;</td></tr>
    </table>

    <legend>
        <div id="map_canvas" style="width:570px; height:600px;"></div>
    </legend>

    <input type="hidden" 
           name="abinit_CE" 
           id="abinit_CE"
           size="25px;" 
           class="textfield ui-widget ui-widget-content"
           value=""/>                  
</div> <!-- Fine Secondo Accordion -->        

<h3><a href="#">
    <img width="35" 
         align="absmiddle"
         src="<%=renderRequest.getContextPath()%>/images/glass_numbers_3.png" />
    
    <b>ABINIT Settings</b>
    <img width="40" 
         align="absmiddle" 
         src="<%=renderRequest.getContextPath()%>/images/icon_small_settings.png"/>
    </a>
</h3>      
    
<div> <!-- Inizio Terzo accordion -->
<p>
    <img width="20" src="<%=renderRequest.getContextPath()%>/images/help.png"/>
    Please, upload your ABINIT input files or choose a demo!
</p>

<fieldset style="width: 567px; border: 1px solid green;">
<table border="0" width="580">
    <tr>        
        <td width="180">
        <img width="30" 
             align="absmiddle"
             src="<%= renderRequest.getContextPath()%>/images/question.png" 
             border="0" 
             title="Select the programming model"/>Programming Model<em>*</em>
        </td>
        
        <td width="250">   
        <input type="radio" 
               name="abinit_model"
               id="abinit_seq"
               value="abinit_seq"
               checked="checked"
               class="required" onchange="enableCores(this.form);"/> Sequential
        
        <input type="radio" 
               name="abinit_model"
               id="abinit_par"
               value="abinit_par"
               class="required" onchange="enableCores(this.form);"/> Parallel
        </td>                
    </tr>
    
    <tr>
        <td width="180"></td>
        <td width="80">         
            <div id="drope_cores" 
                 style="padding-left: 10px; border-style: solid; border-color: grey; 
                        border-width: 1px; display:none; width:242px;">
            <p>
            <br/><img width="20" src="<%=renderRequest.getContextPath()%>/images/help.png"/>
            Number of CPU cores
            <select name="abinit_CPUcores" 
                    style="height:25px; padding-left: 1px; border-style: solid; 
                           border-color: grey; border-width: 1px;">
            
            <option value="2">2</option>        
            <option value="4">4</option>
            <option value="6">6</option>
            </select>
            </p>
            </div>
        </td>
    </tr>
        
    <tr>        
        <td width="180">
        <img width="30" 
             align="absmiddle"
             src="<%= renderRequest.getContextPath()%>/images/question.png" 
             border="0" 
             title="Upload the ABINIT parameters file for the calculation (Max 5.0 MB)"/>
        Upload Params <small><sub>(.in)</sub></small><em>*</em>
        </td>
        
        <td width="250">
        <input type="file"
               name="abinit_file"               
               style="padding-left: 1px; border-style: solid; 
                      border-color: gray; border-width: 1px; 
                      padding-left: 1px; width: 250px;" 
               class="required"/>
        </td>
    </tr>
    
    <tr>        
        <td width="180">
        <img width="30" 
             align="absmiddle"
             src="<%= renderRequest.getContextPath()%>/images/question.png" 
             border="0" 
             title="Upload the ABINIT pseudo potential files for the calculation (Max 5.0 MB)"/>
        Pseudo Pot. files <em>*</em>
        </td>
        
        <td width="250">
        <input type="file"
               name="abinit_pseudofile"
               id="abinit_pseudofile"
               multiple="multiple"
               style="padding-left: 1px; border-style: solid; 
                      border-color: gray; border-width: 1px; 
                      padding-left: 1px; width: 250px;" 
               class="required"/>
        </td>
    </tr>
    
    <tr>        
        <td width="180">
        <img width="30" 
             align="absmiddle"
             src="<%= renderRequest.getContextPath()%>/images/question.png" 
             border="0" 
             title="Upload the files for ABINIT files (Max 5.0 MB)"/>
        File of files <small><sub>(.files)</sub></small><em>*</em>
        </td>
        
        <td width="250">
        <input type="file"
               name="abinit_fileoffiles" 
               style="padding-left: 1px; border-style: solid; 
                      border-color: gray; border-width: 1px; 
                      padding-left: 1px; width: 250px;" 
               class="required"/>
        </td>
    </tr>
    
    <tr>        
        <td width="180">
        <img width="30" 
             align="absmiddle"
             src="<%= renderRequest.getContextPath()%>/images/question.png" 
             border="0" 
             title="A list of output files to be retrieved"/>Output files
        </td>
        
        <td width="250">
        <textarea id="abinit_outputfiles"                       
                  name="abinit_outputfiles" 
                  style="border-style: solid; border-color: gray; 
                         border-width: 1px; padding-left: 0px; width:250px;"
                  disabled="disabled"
                  rows="5" cols="33">
        </textarea>
        
        <br/>
        <input type="checkbox" 
              name="enable_output"
              id="enable_output"
              value="enable_output"              
              onchange="enable_outputFile(this.form);"/>Specify additional output files to be retrieved<br/>
        
        <img width="20" src="<%=renderRequest.getContextPath()%>/images/help.png"/>
            Please, use a comma to separate the list of files
        </td>
    </tr>
    
    <tr><td><br/></td></tr>
    
    <tr>
        <td width="180">
        <img width="30" 
             align="absmiddle"
             src="<%= renderRequest.getContextPath()%>/images/question.png" 
             border="0" title="Please, insert here a short description "/>
        
        <label for="abinit_desc">Description</label>
        </td>
        
        <td width="250">
        <input type="text" 
               id="abinit_desc"
               name="abinit_desc"
               style="border-style: solid; border-color: gray; 
                         border-width: 1px; padding-left: 0px; width:250px;"
               value="Please, insert here a description"
               size="33" />
        </td>           
    </tr>
    
    <tr>
        <td width="180">
        <img width="30" 
             align="absmiddle"
             src="<%= renderRequest.getContextPath()%>/images/question.png" 
             border="0" title="Select the MAX Wall Clock time requested by each simulation" />

        <label for="abinit_maxwallclocktime">Wall Clock Time</label>
        </td>

        <td width="250">
        <div align="absmiddle" id="slider-abinit-maxwallclocktime" style="width:340px; margin:15px;"></div>
        <input type="hidden" name="abinit_maxwallclocktime" value="1440"/>
            
        &nbsp;&nbsp;Max WallClock time per simulation (in min.): 
        <input type="text" 
               id="abinit_maxwallclocktime"
               value="1440"
               disabled="disabled"                  
               style="width:40px; border:0; background:#C9C9C9; color:blue; font-weight:bold; font-size:12;"
               class="textfield ui-widget ui-widget-content ui-state-focus"/>
        </td>
   </tr>
    
    <tr><td><br/></td></tr>
    
    <tr>
        <td width="180">
        <img width="30" 
             align="absmiddle"
             src="<%= renderRequest.getContextPath()%>/images/question.png" 
             border="0" title="Run a simple demo test"/>
                                       
        <input type="checkbox" 
               name="EnableDemo"
               id="EnableDemo"
               onchange="enableDemo(this.form);"/> Run demo
        </td>
        <td width="100">            
            <div id="drope_box_seq" 
                 style="padding-left: 10px; border-style: solid;
                        border-width: 0px; display:none; width:350px;">
            <p>
            <br/><img width="20" src="<%=renderRequest.getContextPath()%>/images/help.png"/>
            Input files selected for sequential demo:<br/>
            &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
            demo.in, 01h.pspgth, demo.files<br/>
            </p>
            </div>
            
            <div id="drope_box_par" 
                 style="padding-left: 10px; border-style: solid; 
                        border-width: 0px; display:none; width:350px;">
            <p>
            <br/><img width="20" src="<%=renderRequest.getContextPath()%>/images/help.png"/>
            Input files selected for parallel demo:<br/>
            &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
            demopara.in, 13al.pspgth, demopara.files<br/>
            </p>
            </div>
        </td>
    </tr>
    
    <tr>
        <td width="180">
        <img width="30" 
             align="absmiddle"
             src="<%= renderRequest.getContextPath()%>/images/question.png" 
             border="0" title="Enable email notification to the user"/>
                                       
        <c:set var="enabled_SMTP" value="<%= SMTP_HOST %>" />
        <c:set var="enabled_SENDER" value="<%= SENDER_MAIL %>" />
        <c:choose>
        <c:when test="${empty enabled_SMTP || empty enabled_SENDER}">        
        <input type="checkbox" 
               name="EnableNotification"
               id="EnableNotification"
               disabled="disabled"
               value="yes"
               onchange="enableNotification();"/> Notification        
        </c:when>
        <c:otherwise>
        <input type="checkbox" 
               name="EnableNotification"
               id="EnableNotification"
               onchange="enableNotification();"/> Notification
        </c:otherwise>
        </c:choose>
        </td>

        <td>                
        <div id="drope_mail" 
                 style="padding-left: 1px; border-style: hidden; 
                        border-width: 0px; padding-left: 1px; display:none;">
        <p>
        <br/><img width="20" src="<%=renderRequest.getContextPath()%>/images/help.png"/>
        SMTP settings: [ <%= SMTP_HOST %> ]<br/>        
        </p>
        </div>
        </td>
        
        <tr><td><br/></td></tr>
    </tr>   
   
   <tr>
       <td colspan="2">
       <p align="justify">
       <img width="30" src="<%=renderRequest.getContextPath()%>/images/notice.png"/>
       All the ABINIT log files, will be available through the <u>Browse Service</u>!<br/>
       &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
       For more details, please read the output.README file produced during the run!<br/>
       <img width="30" src="<%=renderRequest.getContextPath()%>/images/notice.png"/>
       PLEASE, do NOT use "_" in the file names!<br/><br/>
       </p>
       </td>
    </tr>
                
   <tr>                    
   <td align="left">
        <input type="image" 
               src="<%= renderRequest.getContextPath()%>/images/start-icon.png"
               width="60"                   
               name="submit"
               id ="submit" 
               title="Run your Simulation!" />                    
   </td>
   </tr>
</table>
</fieldset>
</div>	<!-- Fine Terzo Accordion -->
</div> <!-- Fine Accordions -->
</fieldset>
</form>                                                                         

<div id="abinit_footer" style="width:690px; font-family: Tahoma,Verdana,sans-serif,Arial; font-size: 14px;">
    <div>ABINIT-7.6.4 portlet ver. 2.0.1</div>
    <div>The Italian National Institute of Nuclear Physics (INFN), Division of Catania, Italy</div>    
    <div>Copyright © 2014 - 2015. All rights reserved</div>    
    <div>This work has been partially supported by
    <a href="http://www.chain-project.eu/">
    <img width="45" 
         border="0"
         src="<%= renderRequest.getContextPath()%>/images/chain-logo-220x124.png" 
         title="The CHAIN-REDS EU FP7 Project"/>
    </a>
    </div>    
</div>               