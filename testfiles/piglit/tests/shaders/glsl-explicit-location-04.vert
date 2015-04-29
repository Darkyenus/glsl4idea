#extension GL_ARB_explicit_attrib_location: require

layout(location = 1000) in vec4 vertex;

void main()
{
	gl_Position = vertex;
}
