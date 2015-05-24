/* [config]
 * expect_result: fail
 * glsl_version: 1.10
 * check_link: false
 * [end config]
 *
 * There is no digraph interpretation in GLSL, so <% should not be
 * interpreted as {, and %> should not be interpreted as }.
 */

void main()
{
	gl_Position = gl_ModelViewProjectionMatrix * gl_Vertex;
%>
