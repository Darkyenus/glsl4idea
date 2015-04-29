/* [config]
 * expect_result: fail
 * glsl_version: 1.10
 * [end config]
 *
 * #extension directives do not include semicolons, and specifying bogus
 * extra symbols at the end of the line should be rejected.
 */

#extension GL_ARB_fragment_coord_conventions : enable;
void main() { }
