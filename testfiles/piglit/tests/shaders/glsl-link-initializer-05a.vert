uniform float angle;
uniform float scale;
float global_variable = sin(angle) * scale;

/* This works around a bug in Apple's GLSL compiler.  Their compiler won't allow
 * a shader doesn't have any executable code.
 */
void apple_work_around1() {}
