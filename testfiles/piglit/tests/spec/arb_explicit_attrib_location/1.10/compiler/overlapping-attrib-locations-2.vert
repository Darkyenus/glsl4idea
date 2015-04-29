// [config]
// expect_result: pass
// glsl_version: 1.10
// check_link: true
// [end config]
//
// Binding more than one attribute name to the same location is referred
// to as aliasing. It is allowed only on vertex shader input variables
// in OpenGL (2.0 and above). Check that vertex shader compiles and links
// successfully in case of overlapping input attribute locations. This
// shader uses attributes of different sizes with overlapping locations.

#version 110
#extension  GL_ARB_explicit_attrib_location : require

layout(location=0) in mat4 mat;
layout(location=3) in vec4 p;
uniform int x;

void main()
{
    if (x == 0)
       gl_Position = mat * vec4(1.0);
    else if (x == 1)
       gl_Position = p;
    else
       gl_Position = vec4(0.0);
}
