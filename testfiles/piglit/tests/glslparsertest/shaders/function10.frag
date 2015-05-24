// [config]
// expect_result: fail
// glsl_version: 1.10
//
// [end config]

void function(in int i);  

void main()
{
    float f;
    // overloaded function not present
    function(f);  
}

void function(in int i)  
{  
   i = 3;
}
