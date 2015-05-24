// [config]
// expect_result: fail
// glsl_version: 1.10
//
// [end config]

void function(int i[])  // size of array must be specified
{
}

void main()
{
    int i[2];
    function(i);
}


