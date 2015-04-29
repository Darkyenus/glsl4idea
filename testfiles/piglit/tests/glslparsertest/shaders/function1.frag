// [config]
// expect_result: fail
// glsl_version: 1.10
//
// [end config]

void function(int i) 
{
    return i;  // void function cannot return a value
}

void main()
{
    int i;
    function(i);
}


