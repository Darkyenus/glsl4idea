#ifdef USE_LOWP_HIGHP
#define lowp
#define highp
#endif

void main()
{
    #ifdef SOME_DEFINE
    lowp vec4 value = vec4(0, 0, 0, 0);
    #endif
}