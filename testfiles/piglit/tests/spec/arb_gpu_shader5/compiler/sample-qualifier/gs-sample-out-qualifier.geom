// [config]
// expect_result: pass
// glsl_version: 1.50
// require_extensions: GL_ARB_gpu_shader5
// [end config]

// From the ARB_gpu_shader5 spec:
// "Vertex and geometry output variables output per-vertex data and
// are declared using the ..., or sample out storage qualifiers, ..."

#version 150
#extension GL_ARB_gpu_shader5: require

sample out vec4 x;

void main()
{
	x = vec4(0);
	gl_Position = vec4(1);
	EmitVertex();
}
