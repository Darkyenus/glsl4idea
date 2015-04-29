// [config]
// expect_result: fail
// glsl_version: 1.10
//
// [end config]

void function(uniform int i)  
{  // uniform qualifier cannot be used with function parameters
}

void main()
{
    int i;
    function(i);  
}


