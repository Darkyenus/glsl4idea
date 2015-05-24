// [config]
// expect_result: fail
// glsl_version: 1.10
//
// [end config]

void main()
{
    int i = 1;
    float f = 1.2;
    float result = f * i; // auto promotion now allowed 
}
