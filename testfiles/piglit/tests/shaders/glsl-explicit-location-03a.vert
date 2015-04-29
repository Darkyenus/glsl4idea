#extension GL_ARB_explicit_attrib_location: require

layout(location = 0) in vec4 vertex;
layout(location = 1) in vec3 color;

vec3 function(void);

void main()
{
	gl_Position = vertex;
	gl_FrontColor = vec4(color + function(), 1.0);
}
