#include <jni.h>
#include "audio_ns.h"
#include "noise_suppression.h"


extern "C" {

NsHandle* handle = NULL;


void innerProcess(short in_sample[], short out_sample[], int length){

    int curPosition = 0;

    while(curPosition < length){

        audio_ns_process((long) handle, in_sample + curPosition, out_sample + curPosition);

        curPosition += 160;

    }

}

JNIEXPORT jboolean JNICALL
Java_com_test_jni_WebrtcProcessor_init(JNIEnv *env, jobject instance, jint sample_rate) {

    handle = (NsHandle *) audio_ns_init(sample_rate);

    return false;
}

JNIEXPORT jboolean JNICALL
Java_com_test_jni_WebrtcProcessor_processNoise(JNIEnv *env, jobject instance, jshortArray sample) {

    if(!handle)
        return false;

    jsize length = env->GetArrayLength(sample);

    jshort *sam = env->GetShortArrayElements(sample, 0);

    short in_sample[length];
    for(int i=0; i<length; i++){
        in_sample[i] = sam[i];
    }

    innerProcess(in_sample, sam, length);

    env->ReleaseShortArrayElements(sample, sam, 0);

    return true;
}

JNIEXPORT void JNICALL
Java_com_test_jni_WebrtcProcessor_release(JNIEnv *env, jobject instance) {

    if(handle){
        audio_ns_destroy((long) handle);
    }


}

}

#include <string>

static short s_encode_valprev;
static char s_encode_index;
static short s_decode_valprev;
static char s_decode_index;

/* Intel ADPCM step variation table */
static int indexTable[16] = {
        -1, -1, -1, -1, 2, 4, 6, 8,
        -1, -1, -1, -1, 2, 4, 6, 8,
};

static unsigned int stepsizeTable[89] = {
        1, 2, 3, 4, 5, 6, 7, 8, 9, 10,
        11, 12, 13, 14, 15, 16, 18, 20, 22, 24,
        26, 28, 30, 32, 34, 36, 39, 42, 46, 50,
        55, 62, 70, 80, 95, 115, 140, 165, 190, 220,
        255, 290, 330, 370, 420, 470, 530, 600, 680, 760,
        850, 950, 1050, 1160, 1280, 1410, 1550, 1700, 1860, 2030,
        2210, 2400, 2600, 2810, 3030, 3260, 3500, 3750, 4010, 4280,
        4560, 4850, 5150, 5460, 5780, 6110, 6450, 6820, 7220, 7660,
        8150, 8700, 9330, 9959, 10698, 11514, 12397, 13333, 14433
};

extern "C"
JNIEXPORT void JNICALL
Java_com_test_jni_ADPCM_adpcm_1thirdparty_1reset(JNIEnv *env, jobject instance) {

    s_encode_valprev = 0;
    s_encode_index = 0;

    s_decode_valprev = 0;
    s_decode_index = 0;

}

extern "C"
JNIEXPORT jbyteArray JNICALL
Java_com_test_jni_ADPCM_adpcm_1coder(JNIEnv *env, jobject instance, jshortArray indata_,
                                           jbyteArray outdata_, jint len) {
    jshort *indata = env->GetShortArrayElements(indata_, NULL);
    jbyte *outdata = env->GetByteArrayElements(outdata_, NULL);


    short *inp;         /* Input buffer pointer */
    unsigned char *outp;/* output buffer pointer */
    int val;            /* Current input sample value */
    int sign;           /* Current adpcm sign bit */
    unsigned int delta; /* Current adpcm output value */
    int diff;           /* Difference between val and valprev */
    unsigned int udiff; /* unsigned value of diff */
    unsigned int step;  /* Stepsize */
    int valpred;        /* Predicted output value */
    unsigned int vpdiff;/* Current change to valpred */
    int index;          /* Current step change index */
    unsigned int outputbuffer = 0;/* place to keep previous 4-bit value */
    int bufferstep;     /* toggle between outputbuffer/output */
    int count = 0;      /* the number of bytes encoded */

    outp = (unsigned char *) outdata;
    inp = indata;

    valpred = s_encode_valprev;
    index = s_encode_index;
    step = stepsizeTable[index];

    bufferstep = 1;

    while (len-- > 0) {
        val = *inp++;
        /* Step 1 - compute difference with previous value */
        diff = val - valpred;
        if (diff < 0) {
            sign = 8;
            diff = (-diff);
        } else {
            sign = 0;
        }
        /* diff will be positive at this point */
        udiff = (unsigned int) diff;

        /* Step 2 - Divide and clamp */
        /* Note:
        ** This code *approximately* computes:
        **    delta = diff*4/step;
        **    vpdiff = (delta+0.5)*step/4;
        ** but in shift step bits are dropped. The net result of this is
        ** that even if you have fast mul/div hardware you cannot put it to
        ** good use since the fixup would be too expensive.
        */
        delta = 0;
        vpdiff = (step >> 3);

        if (udiff >= step) {
            delta = 4;
            udiff -= step;
            vpdiff += step;
        }
        step >>= 1;
        if (udiff >= step) {
            delta |= 2;
            udiff -= step;
            vpdiff += step;
        }
        step >>= 1;
        if (udiff >= step) {
            delta |= 1;
            vpdiff += step;
        }

        /* Phil Frisbie combined steps 3 and 4 */
        /* Step 3 - Update previous value */
        /* Step 4 - Clamp previous value to 16 bits */
        if (sign != 0) {
            valpred -= vpdiff;
            if (valpred < -32768)
                valpred = -32768;
        } else {
            valpred += vpdiff;
            if (valpred > 32767)
                valpred = 32767;
        }

        /* Step 5 - Assemble value, update index and step values */
        delta |= sign;

        index += indexTable[delta];
        if (index < 0) index = 0;
        if (index > 88) index = 88;
        step = stepsizeTable[index];

        /* Step 6 - Output value */
        if (bufferstep != 0) {
            outputbuffer = (delta << 4);
        } else {
            *outp++ = (char) (delta | outputbuffer);
            count++;
        }
        bufferstep = !bufferstep;
    }

    /* Output last step, if needed */
    if (bufferstep == 0) {
        *outp++ = (char) outputbuffer;
        count++;
    }

    s_encode_valprev = (short) valpred;
    s_encode_index = (char) index;
    env->SetByteArrayRegion(outdata_, 0, count, outdata);
    return outdata_;

    env->ReleaseShortArrayElements(indata_, indata, 0);
    env->ReleaseByteArrayElements(outdata_, outdata, 0);
}


extern "C"
JNIEXPORT jshortArray JNICALL
Java_com_test_jni_ADPCM_adpcm_1decoder(JNIEnv *env, jobject instance, jbyteArray indata_,
                                             jshortArray outdata_, jint len) {
    jbyte *indata = env->GetByteArrayElements(indata_, NULL);
    jshort *outdata = env->GetShortArrayElements(outdata_, NULL);

//    jbyte *indata = env->GetByteArrayElements(indata_, NULL);
//    jshort *outdata = env->GetShortArrayElements(outdata_, NULL);

    unsigned char *inp; /* Input buffer pointer */
    short *outp;        /* output buffer pointer */
    unsigned int sign;  /* Current adpcm sign bit */
    unsigned int delta; /* Current adpcm output value */
    unsigned int step;  /* Stepsize */
    int valpred;        /* Predicted value */
    unsigned int vpdiff;/* Current change to valpred */
    int index;          /* Current step change index */
    unsigned int inputbuffer = 0;/* place to keep next 4-bit value */
    int bufferstep;     /* toggle between inputbuffer/input */
    int count = 0;

    outp = outdata;
    inp = (unsigned char *) indata;

    valpred = s_decode_valprev;
    index = (int) s_decode_index;
    step = stepsizeTable[index];

    bufferstep = 0;

    len *= 2;

    while (len-- > 0) {

        /* Step 1 - get the delta value */
        if (bufferstep != 0) {
            delta = inputbuffer & 0xf;
        } else {
            inputbuffer = (unsigned int) *inp++;
            delta = (inputbuffer >> 4);
        }
        bufferstep = !bufferstep;

        /* Step 2 - Find new index value (for later) */
        index += indexTable[delta];
        if (index < 0) index = 0;
        if (index > 88) index = 88;

        /* Step 3 - Separate sign and magnitude */
        sign = delta & 8;
        delta = delta & 7;

        /* Phil Frisbie combined steps 4 and 5 */
        /* Step 4 - Compute difference and new predicted value */
        /* Step 5 - clamp output value */
        /*
        ** Computes 'vpdiff = (delta+0.5)*step/4', but see comment
        ** in adpcm_coder.
        */
        vpdiff = step >> 3;
        if ((delta & 4) != 0) vpdiff += step;
        if ((delta & 2) != 0) vpdiff += step >> 1;
        if ((delta & 1) != 0) vpdiff += step >> 2;

//        valpred*=1. 5;

        if (sign != 0) {
            valpred -= vpdiff;
            if (valpred < -32768)
                valpred = -32768;
        } else {
            valpred += vpdiff;
            if (valpred > 32767)
                valpred = 32767;
        }

        /* Step 6 - Update step value */
        step = stepsizeTable[index];

        /* Step 7 - Output value */
        *outp++ = (short) valpred;
        count++;
    }

    s_decode_valprev = (short) valpred;
    s_decode_index = (char) index;
    env->SetShortArrayRegion(outdata_, 0, count, outdata);
//    env->ReleaseByteArrayElements(indata_, indata, 0);
//    env->ReleaseShortArrayElements(outdata_, outdata, 0);

    return outdata_;

    env->ReleaseByteArrayElements(indata_, indata, 0);
    env->ReleaseShortArrayElements(outdata_, outdata, 0);
}

