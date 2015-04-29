uniform float angle;
uniform float scale;
float global_variable = cos(angle) * scale;

float function(void)
{
  return global_variable;
}