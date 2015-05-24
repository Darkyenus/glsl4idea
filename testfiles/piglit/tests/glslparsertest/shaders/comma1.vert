// [config]
// expect_result: fail
// glsl_version: 1.10
//
// [end config]

void main() 
{
    int i, j, k;
    float f;
    i = j, k, f;
    i = (j, k, f);   // float cannot be assigned to int
    gl_Position = vec4(1);
}
