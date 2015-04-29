// [config]
// expect_result: fail
// glsl_version: 1.10
//
// [end config]

int function(out int i)  
{  // function should return a value
}

void main()
{
    int i;
    function(i);  
}


