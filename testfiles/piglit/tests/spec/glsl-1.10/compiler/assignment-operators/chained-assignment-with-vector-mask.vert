/* [config]
 * expect_result: pass
 * glsl_version: 1.10
 * [end config]
 */

void splat(vec2 a, float b)
{
    a.x = a.y = b;
}
