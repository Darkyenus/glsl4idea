// [config]
// expect_result: pass
// glsl_version: 1.10
// [end config]

#define DEF 1
#undef UNDEF

#if defined DEF
#else
#error
#endif

#if defined UNDEF
#error
#else
#endif

#if 1 == 0 || defined DEF
#else
#error
#endif

#if 1 == 0 || defined UNDEF
#error
#else
#endif

#if defined(DEF)
#else
#error
#endif

#if defined(UNDEF)
#error
#else
#endif

#if 1 == 0 || defined(DEF)
#else
#error
#endif

#if 1 == 0 || defined(UNDEF)
#error
#else
#endif

void main()
{
  gl_Position = gl_Vertex;
}

