glsl4idea
=========

A language plugin for IntelliJ IDEA that provides basic support for GLSL language.

Current features:
* Syntax highlighting
* Grammar checking
* Support for all basic GLSL constructs, including structs and GLSL ES precision statements
* Basic support for preprocessor statements


Original code made by Yngve Devik Hammersland and Jean-Paul Balabanian.  
Originally hosted on https://code.google.com/p/glsl4idea/  
Changes made with permissions from original authors by Jan Polák (Darkyen).  

Licensed under GNU LGPL, see license file.

# How to use
1. Get the plugin
  - From [Jetbrains repo](http://plugins.jetbrains.com/plugin/6993) and skip step 2
  - Or from [releases](https://github.com/Darkyenus/glsl4idea/releases)
  - Or compile it yourself (see -Development-)
2. Install plugin (Tested on IntelliJ CE 14, similar versions may work) (Preferences -> Plugins -> Install plugin from disk)
3. Create .glsl file or use [language injections](https://www.jetbrains.com/help/idea/using-language-injections.html) to treat a string literal as GLSL code.


# Development

## Compiling
1. Clone (or download) this repository
2. Create Intellij project in this directory with Plugin module (Make sure you have official Plugin DevKit installed)
  - Easiest way might be to first create an empty project in `glsl4idea` folder and then create a Intellij Platform Plugin module from same folder
3. File -> Project Structure -> Modules -> Right-click the `resources` directory -> Mark as Resources
4. Build -> Prepare plugin for deployment -> You will be notified with plugin jar path
 
## Testing
To test changes, there are some testing files in `testfiles` directory.
You can also test on [piglit](http://piglit.freedesktop.org), run `DownloadPiglit.sh` to download
and sort shaders for testing.
