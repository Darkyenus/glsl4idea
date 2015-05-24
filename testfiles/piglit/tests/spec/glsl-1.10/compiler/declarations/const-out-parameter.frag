// [config]
// expect_result: fail
// glsl_version: 1.10
// [end config]

/* The GLSL 1.10 spec says:
 *
 *     "[T]he const qualifier cannot be used with out or inout."
 */
void function(const out int i) {}
