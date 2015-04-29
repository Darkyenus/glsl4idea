// [config]
// expect_result: fail
// glsl_version: 1.10
//
// [end config]

/* Only one version statement is allowed; two should raise an error. */
#version 110
#version 110

void main()
{
   gl_FragColor = vec4(1);    
}
