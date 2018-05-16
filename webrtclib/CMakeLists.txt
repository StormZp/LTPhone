


cmake_minimum_required(VERSION 2.8)

project(webrtc)

set( CMAKE_C_FLAGS "${CMAKE_C_FLAGS} -Wall -pedantic" )

include_directories(${PROJECT_SOURCE_DIR}/include)



set(JNI_LIBS ${CMAKE_SOURCE_DIR}/../jnilibs)

add_library(speex-lib SHARED IMPORTED)
set_target_properties(speex-lib  PROPERTIES IMPORTED_LOCATION ${JNI_LIBS}/${ANDROID_ABI}/libspeex.so)


add_library(speexdsp-lib SHARED IMPORTED)
set_target_properties(speexdsp-lib  PROPERTIES IMPORTED_LOCATION ${JNI_LIBS}/${ANDROID_ABI}/libspeexdsp.so)

include_directories( ${JNI_LIBS}/speex_include )



add_definitions(-DFIXED_POINT -DUSE_KISS_FFT -DHAVE_CONFIG_H)

add_library(webrtc SHARED

                src/main/cpp/native-lib.cpp

                src/main/cpp/audio_ns.cpp
                src/main/cpp/fft4g.cpp
                src/main/cpp/noise_suppression.cpp
                src/main/cpp/ns_core.cpp

           )

target_link_libraries(webrtc log m)