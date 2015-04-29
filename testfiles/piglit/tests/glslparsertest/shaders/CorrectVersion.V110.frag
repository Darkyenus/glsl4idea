// [config]
// expect_result: pass
// glsl_version: 1.10
//
// [end config]

/* #version can only be followed by number 110. The only statements before 
   #version can be comment or white spaces */



#version 110


void main()
{
   gl_FragColor = vec4(1);    
}
