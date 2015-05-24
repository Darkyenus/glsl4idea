float global_variable = 2.0;

/* This works around a bug in Apple's GLSL compiler.  Their compiler won't allow
 * a shader doesn't have any executable code.
 */
void apple_work_around2() {}
