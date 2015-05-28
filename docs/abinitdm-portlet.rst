============
About
============
.. image:: images/ABINIT_logo.png
   :height: 100px
   :align: left
   :target: http://www.abinit.org/

.. class:: justify
is a package whose main program allows one to find the total energy, charge density and electronic structure of systems made of electrons and nuclei (molecules and periodic solids) within Density Functional Theory (DFT), using pseudopotentials and a planewave or wavelet basis. 

ABINIT also includes options to optimize the geometry according to the DFT forces and stresses, or to perform molecular dynamics simulations using these forces, or to generate dynamical matrices, Born effective charges, and dielectric tensors, based on Density-Functional Perturbation Theory, and many more properties. 

Excited states can be computed within the Many-Body Perturbation Theory (the GW approximation and the Bethe-Salpeter equation), and Time-Dependent Density Functional Theory (for molecules). In addition to the main ABINIT code, different utility programs are provided. 

ABINIT is a project that favours development and collaboration `(short presentation of the ABINIT project - 10 pages in pdf) <http://www.abinit.org/about/presentation.pdf>`_.

============
Installation
============
To install the abinitDM portlet the WAR file has to be deployed into the application server.

As soon as the portlet has been successfully deployed on the Science Gateway the administrator has to configure:

- the list of e-Infrastructures where the application can be executed;

- some additional application settings.

1.) To configure a generic e-Infrastructure, the following settings have to be provided:

**Enabled**: A true/false flag which enables or disable the generic e-Infrastructure;

**Infrastructure**: The acronym to reference the e-Infrastructure;

**VOName**: The VO for this e-Infrastructure;

**TopBDII**: The Top BDII for this e-Infrastructure;

**WMS Endpoint**: A list of WMS endpoint for this e-Infrastructure (max. 10);

**MyProxyServer**: The MyProxyServer for this e-Infrastructure;

**eTokenServer**: The eTokenServer for this e-Infrastructure;

**Port**: The eTokenServer port for this e-Infrastructure;

**Serial Number**: The MD5SUM of the robot certificate to be used for this e-Infrastructure;

**WebDAV**: The EMI-3 DPM Storage Element to be used for this e-Infrastructure;

In Fig. 1 is shown how the portlet has been configured to run simulation on the EUMEDGRID-Support e-Infrastructure.

.. image:: images/ABINIT_settings.jpg
   :width: 100px
   :align: center
   


2.) To configure the application, the following settings have to be provided:

**AppID**: The ApplicationID as registered in the UserTracking MySQL database (GridOperations table);

**Log Level**: The log level for the application (e.g.: INFO, VERBOSE);

**Metadata Host**: The Metadata hostname where download/upload digital-assets (e.g. glibrary.ct.infn.it);

**Software TAG**: The list of software tags requested by the application;

**SMTP Host**: The SMTP server used to send notification to users;

**Sender**: The FROM e-mail address to send notification messages about the jobs execution to users;

.. image:: images/ABINIT_settings2.jpg
   :width: 100px
   :align: center

============
Usage
============
The run abinit simulation the user has to click on the third accordion select the type of job to run (e.g. 'Sequential' or 'Parallel')
and upload the input files.

The ABINIT input files consist of:

- An input file (.in);

- A list of Pseudo Potential files;

- A file of files (.files).

For demonstrative use cases, the user can also click on 'Run Demo'.

.. image:: images/ABINIT_input.jpg
   :width: 100px
   :align: center

============
Support
============
For any support, please ask for `support <mailto:giuseppe.larocca@ct.infn.it>`_

:Author:
 Giuseppe LA ROCCA, INFN
 Mario TORRISI, UniCT
 Brahim LAGOUN,
 Ouafa BENTALB, CERIST

:Version: 2.0.1, 2014-2015

