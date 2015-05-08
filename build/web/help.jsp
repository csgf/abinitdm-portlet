<%
/**************************************************************************
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
****************************************************************************/
%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page import="javax.portlet.*"%>
<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet"%>
<portlet:defineObjects/>

<script type="text/javascript">
    $(document).ready(function() {
              
    $('.slideshow').cycle({
	fx: 'fade' // choose your transition type (fade, scrollUp, shuffle, etc)
    });
    
    // Roller
    $('#abinit_footer').rollchildren({
        delay_time         : 3000,
        loop               : true,
        pause_on_mouseover : true,
        roll_up_old_item   : true,
        speed              : 'slow'   
    });
    
    //var $tumblelog = $('#tumblelog');  
    $('#tumblelog').imagesLoaded( function() {
      $tumblelog.masonry({
        columnWidth: 440
      });
    });
});
</script>
                    
<br/>

<fieldset>
<legend>About ABINIT</legend>

<section id="content">

<div id="tumblelog" class="clearfix">
    
  <div class="story col3">
  <a href="http://www.abinit.org/">
  <img width="220" src="<%=renderRequest.getContextPath()%>/images/logo.png"/></a><br/>
  <p style="text-align:justify; position: relative;">
  A package whose main program allows one to find the total energy, charge density 
  and electronic structure of systems made of electrons and nuclei (molecules and 
  periodic solids) within <a href="http://dft.sandia.gov/">Density Functional Theory (DFT)</a>, 
  using pseudo-potentials and a planewave or wavelet basis. <br/><br/>
  ABINIT also includes options to optimize the geometry according to the DFT forces 
  and stresses, or to perform molecular dynamics simulations using these forces, or 
  to generate dynamical matrices, Born effective charges, and dielectric tensors, based 
  on Density-Functional Perturbation Theory, and many more properties. <br/>
  A short presentation of the ABINIT project (10 pages in PDF) is here
  <a href="http://www.abinit.org/about/presentation.pdf">
  <img width="35" src="<%=renderRequest.getContextPath()%>/images/PDF_Logo.png"/></a>
  <br/><br/>
  <img width="250" 
       src="<%=renderRequest.getContextPath()%>/images/imageabinit1.png"/>
  <img width="200" 
       src="<%=renderRequest.getContextPath()%>/images/imageabinit2.png"/>  
  </p>      
  </div>
                                     
  <div class="story col3" style="font-family: Tahoma,Verdana,sans-serif,Arial; font-size: 14px;">
      <h2>
      <a href="mailto:info@sg-licence@ct.infn.it">
      <img width="100" 
           src="<%= renderRequest.getContextPath()%>/images/contact6.jpg" 
           title="Get in touch with the project"/></a>Contacts
      </h2>
      <p style="text-align:justify;">Giuseppe LA ROCCA (INFN)<i> &mdash; (Responsible for deployment)</i></p>
      <p style="text-align:justify;">Mario TORRISI (UniCT)<i> &mdash; (Responsible for deployment)</i></p>
      <p style="text-align:justify;">Brahim LAGOUN<i> &mdash; (Application key contact)</i></p>
      <p style="text-align:justify;">Ouafa BENTALEB (CERIST)<i> &mdash; (NREN contact)</i></p>
  </div>               
    
  <div class="story col3" style="font-family: Tahoma,Verdana,sans-serif,Arial; font-size: 13px;">
        <h2>Sponsors & Credits</h2>
        <table border="0">                        
            <tr>                
            <td>
            <p align="justify">
            <a href="http://www.ct.infn.it/">
                <img align="center" width="150"                      
                     src="<%= renderRequest.getContextPath()%>/images/Infn_Logo.jpg" 
                     border="0" title="The Italian National Institute of Nuclear Physics (INFN)" />
            </a>
            </p>
            </td>
            
            <td>&nbsp;&nbsp;</td>
            
            <td>
            <p align="justify">
            <a href="http://www.grid.arn.dz/">
                <img align="center" width="200"                      
                     src="<%= renderRequest.getContextPath()%>/images/arn.png" 
                     border="0" title="DZ e-Science Grid Home Page" />
            </a>
            </p>
            </td>
            
            <td>&nbsp;&nbsp;</td>
            <td>&nbsp;&nbsp;</td>
            
            <td>
            <p align="justify">
            <a href="https://www.chain-project.eu/">
                <img align="center" width="150"                      
                     src="<%= renderRequest.getContextPath()%>/images/chain-logo-220x124.png" 
                     border="0" title="The CHAIN-REDS Project Home Page" />
            </a>
            </p>
            </td>                                                
            </tr>                                  
        </table>   
  </div>
</div>
</section>
</fieldset>           
                     
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