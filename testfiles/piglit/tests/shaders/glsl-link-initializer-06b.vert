uniform float angle;
uniform float scale;
float global_variable = scale * sin(angle);

float function(void);

void main()
{
  gl_Position = gl_Vertex * global_variable * function();
}
