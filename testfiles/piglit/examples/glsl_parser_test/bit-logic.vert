/* The config section below is required.
 *
 * [config]
 * # The config section may contain comments.
 * expect_result: fail
 * glsl_version: 1.30
 * [end config]
 *
 * Description: bit-and with argument type (int, uint)
 *
 * From page 50 (page 56 of PDF) of the GLSL 1.30 spec:
 *     "The fundamental types of the operands (signed or unsigned) must match"
 */

#version 130
void main() {
    int x = int(7) & uint(1);
}
