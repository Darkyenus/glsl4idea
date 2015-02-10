glsl4idea
=========

A language plugin for IntelliJ IDEA that provides basic support for GLSL language.

Current features:
* Syntax highlighting
* Grammar checking
* Support for all basic GLSL constructs, including structs and GLSL ES precision statements


Original code made by Yngve Devik Hammersland and Jean-Paul Balabanian.  
Originally hosted on https://code.google.com/p/glsl4idea/  
Changes made with permissions from original authors by Jan Polák (Darkyen).  

Licensed under GNU LGPL, see license file.

# How to use
1. Download plugin .jar from here or compile it yourself (see below)
2. Install plugin (Tested on IntelliJ CE 14, similar versions may work) (Preferences -> Plugins -> Install plugin from disk)
3. Create .glsl file and enjoy

## Compiling
1. Clone (or download) this repository
2. Create Intellij project in this directory with Plugin module (Make sure you have official Plugin DevKit installed)
3. Build -> Prepare plugin for deployment -> You will be notified with plugin jar path
