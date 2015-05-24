uniform float angle;
uniform float scale;
float global_variable = sin(angle) * scale;

void main()
{
  gl_Position = gl_Vertex;
}
