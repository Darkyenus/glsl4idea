// [config]
// expect_result: fail
// glsl_version: 1.10
//
// [end config]

struct s {
    int i;
} s1;

void main()
{
   s1 = -s1; // cannot calculate negative of a structure
}
