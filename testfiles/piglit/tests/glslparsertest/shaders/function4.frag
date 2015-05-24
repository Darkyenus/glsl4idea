// [config]
// expect_result: fail
// glsl_version: 1.10
//
// [end config]

uniform int uniformInt;

void function(out int i)  
{
    i = 1; 
}

void main()
{
    function(uniformInt);  // out and inout parameters cannot be uniform since uniforms cannot be modified
}

