#ifndef _AUDIO_NS_H_
#define _AUDIO_NS_H_

long audio_ns_init(int sample_rate);


int audio_ns_process(long ns_handle ,  short *src_audio_data ,short *dest_audio_data);


void audio_ns_destroy(long ns_handle);

#endif