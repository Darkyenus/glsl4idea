// [config]
// expect_result: fail
// glsl_version: 1.10
//
// [end config]

void function(const int i)  
{
    i = 3;  // const value cant be modified
}

void main()
{
    int i;
    function(i);
}


