// [config]
// expect_result: fail
// glsl_version: 1.10
//
// [end config]

void main()
{
    mat2 m1,m2;
    bool b = m1 > m2;  // greater-than operator can operate on matrices, however, equal (==) and not equal (!=) operators can be used with matrices
}
