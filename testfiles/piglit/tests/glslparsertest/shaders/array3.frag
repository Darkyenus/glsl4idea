// [config]
// expect_result: fail
// glsl_version: 1.10
//
// [end config]

void main()
{
    int array1[2], array2[2];
    bool b = array1 == array2; // equality operator does not work on arrays but works on array elements 
}
