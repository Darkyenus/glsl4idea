// [config]
// expect_result: fail
// glsl_version: 1.10
//
// [end config]

void function(const inout int i)  
{  // inout parameters cannot be const
   i = 3;
}

void main()
{
    int i;
    function(i);  
}


