// [config]
// expect_result: fail
// glsl_version: 1.10
//
// [end config]

/* This is  a comment*/ int i; // This is a global decl
#version 110  // error #version should be the first statement in the program


void main()
{
   gl_FragColor = vec4(1);    
}
