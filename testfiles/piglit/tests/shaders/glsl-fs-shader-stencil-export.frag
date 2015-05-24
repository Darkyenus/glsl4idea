#version 120
#extension GL_ARB_shader_stencil_export: enable

void main()
{
	gl_FragDepth = 1.0;
	gl_FragStencilRefARB = 129;
}
