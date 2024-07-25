#include <stdint.h>
#include <stdio.h>
#include <stdlib.h>

#include "jni.h"
#include "me_mrletsplay_pwmixer_PWMixer.h"
#include "pwmixer.h"

#define MAX_FILTERS 256

// Helper functions

typedef struct {
	uint32_t in;
	uint32_t out;
	jobject filterFunction;
} JavaFilterData;

JavaFilterData *activeFilters[MAX_FILTERS];
uint32_t activeFiltersCount;

JavaVM *vm;
JNIEnv *ioThreadEnv;

bool getJNIEnv(JavaVM *vm, JNIEnv **env) {
	*env = NULL;
	jint getEnv = (*vm)->GetEnv(vm, (void **) env, JNI_VERSION_1_6);
	if(getEnv == JNI_EDETACHED) {
		if((*vm)->AttachCurrentThread(vm, (void **) env, NULL) == JNI_OK) {
			return true;
		} else {
			printf("JNI failed to attach thread\n");
			return false;
		}
	} else if(getEnv == JNI_EVERSION) {
		printf("Invalid JNI version\n");
		return false;
	}

	return false;
}

static void attachJNIEnv(void *userdata) {
	printf("Attaching JNI env\n");
	bool attached = getJNIEnv(vm, &ioThreadEnv);
	printf("Attached: %s\n", attached ? "true" : "false");
}

static void detachJNIEnv(void *userdata) {
	printf("Detaching JNI env\n");
	(*vm)->DetachCurrentThread(vm);
}

static void runJavaFilter(float *samples, size_t sampleCount, void *userdata) {
	JavaFilterData *filterData = userdata;

	if(getJNIEnv(vm, &ioThreadEnv)) printf("Attached to JVM\n");
	JNIEnv *env = ioThreadEnv;

	jclass class = (*env)->GetObjectClass(env, filterData->filterFunction);
	jmethodID methodID = (*env)->GetMethodID(env, class, "filter", "([F)V");

	jfloatArray floatArray = (*env)->NewFloatArray(env, sampleCount * PWM_CHANNELS);
	(*env)->SetFloatArrayRegion(env, floatArray, 0, sampleCount * PWM_CHANNELS, samples);
	(*env)->CallVoidMethod(env, filterData->filterFunction, methodID, floatArray);
	(*env)->GetFloatArrayRegion(env, floatArray, 0, sampleCount * PWM_CHANNELS, samples);
}

// Native Java functions

JNIEXPORT void JNICALL Java_me_mrletsplay_pwmixer_PWMixer_sysConnect(JNIEnv *env, jclass clz, jobjectArray argv) {
	printf("Connecting\n");
	(*env)->GetJavaVM(env, &vm);

	jsize argc = (*env)->GetArrayLength(env, argv);

	jstring *strings = malloc(argc * sizeof(jstring *));
	char **argvVal = malloc(argc * sizeof(char *));
	for(jsize i = 0; i < argc; i++) {
		jstring str = (*env)->GetObjectArrayElement(env, argv, i);
		strings[i] = str;
		argvVal[i] = (char *) (*env)->GetStringUTFChars(env, str, NULL);
	}

	pwm_sysConnect(&argc, &argvVal);

	for(jsize i = 0; i < argc; i++) {
		(*env)->ReleaseStringUTFChars(env, strings[i], argvVal[i]);
	}

	pwm_ioRunInLoop(attachJNIEnv, NULL);
}

JNIEXPORT void JNICALL Java_me_mrletsplay_pwmixer_PWMixer_sysDisconnect(JNIEnv *env, jclass clz) {
	printf("Disconnecting\n");
	pwm_ioRunInLoop(detachJNIEnv, NULL);
	pwm_sysDisconnect();
}

JNIEXPORT jboolean JNICALL Java_me_mrletsplay_pwmixer_PWMixer_sysIsRunning(JNIEnv *env, jclass clz) {
	return pwm_sysIsRunning();
}

JNIEXPORT void JNICALL Java_me_mrletsplay_pwmixer_PWMixer_ioConnect0(JNIEnv *env, jclass clz, jlong input, jlong output) {
	pwm_IO *in = pwm_ioGetByID(input);
	pwm_IO *out = pwm_ioGetByID(output);
	pwm_ioConnect(in, out);
}

JNIEXPORT void JNICALL Java_me_mrletsplay_pwmixer_PWMixer_ioDisconnect0(JNIEnv *env, jclass clz, jlong input, jlong output) {
	pwm_IO *in = pwm_ioGetByID(input);
	pwm_IO *out = pwm_ioGetByID(output);
	pwm_ioDisconnect(in, out);
}

JNIEXPORT jlong JNICALL Java_me_mrletsplay_pwmixer_PWMixer_ioCreateInput0(JNIEnv *env, jclass clz, jstring name, jboolean isSink) {
	const char *nameChars = (*env)->GetStringUTFChars(env, name, NULL);
	pwm_IO *in = pwm_ioCreateInput(nameChars, isSink);

	(*env)->ReleaseStringUTFChars(env, name, nameChars);
	return pwm_ioGetID(in);
}

JNIEXPORT void JNICALL Java_me_mrletsplay_pwmixer_PWMixer_ioDestroyInput0(JNIEnv *env, jclass clz, jlong id) {
	pwm_IO *obj = pwm_ioGetByID(id);
	pwm_ioDestroy(obj);
}

JNIEXPORT jlong JNICALL Java_me_mrletsplay_pwmixer_PWMixer_ioCreateOutput0(JNIEnv *env, jclass clz, jstring name, jboolean isSource) {
	const char *nameChars = (*env)->GetStringUTFChars(env, name, NULL);
	pwm_IO *in = pwm_ioCreateOutput(nameChars, isSource);

	(*env)->ReleaseStringUTFChars(env, name, nameChars);
	return pwm_ioGetID(in);
}

JNIEXPORT void JNICALL Java_me_mrletsplay_pwmixer_PWMixer_ioDestroyOutput0(JNIEnv *env, jclass clz, jlong id) {
	pwm_IO *obj = pwm_ioGetByID(id);
	pwm_ioDestroy(obj);
}

JNIEXPORT void JNICALL Java_me_mrletsplay_pwmixer_PWMixer_ioSetVolume0(JNIEnv *env, jclass clz, jlong id, jfloat volume) {
	pwm_IO *obj = pwm_ioGetByID(id);
	pwm_ioSetVolume(obj, volume);
}

JNIEXPORT void JNICALL Java_me_mrletsplay_pwmixer_PWMixer_ioSetConnectionVolume0(JNIEnv *env, jclass clz, jlong input, jlong output, jfloat volume) {
	pwm_IO *in = pwm_ioGetByID(input);
	pwm_IO *out = pwm_ioGetByID(output);
	pwm_ioSetConnectionVolume(in, out, volume);
}

JNIEXPORT jfloat JNICALL Java_me_mrletsplay_pwmixer_PWMixer_ioGetLastVolume0(JNIEnv *env, jclass clz, jlong id) {
	pwm_IO *obj = pwm_ioGetByID(id);
	return pwm_ioGetLastVolume(obj);
}

JNIEXPORT void JNICALL Java_me_mrletsplay_pwmixer_PWMixer_ioSetFilterFunction0(JNIEnv *env, jclass clz, jlong input, jlong output, jobject filterFunction) {
	pwm_IO *in = pwm_ioGetByID(input);
	pwm_IO *out = pwm_ioGetByID(output);
	if(in == NULL || out == NULL) return;

	// Make sure we're not acidentally calling a filter after is has been freed
	pwm_ioSetConnectionFilter(in, out, NULL, NULL);

	for(uint32_t i = 0; i < activeFiltersCount; i++) {
		JavaFilterData *data = activeFilters[i];
		if(data->in == input && data->out == output) {
			// Another filter was active previously, free it
			printf("Removing previous filter\n");
			(*env)->DeleteGlobalRef(env, activeFilters[i]->filterFunction);
			free(activeFilters[i]);

			for(uint32_t j = i; j < activeFiltersCount - 1; j++) {
				activeFilters[j] = activeFilters[j + 1];
				activeFilters[j + 1] = NULL;
			}

			activeFiltersCount--;

			break;
		}
	}

	if(activeFiltersCount >= MAX_FILTERS) return; // TODO: return error

	jobject filterFunctionRef = (*env)->NewGlobalRef(env, filterFunction);

	JavaFilterData *filterData = calloc(1, sizeof(JavaFilterData));
	filterData->in = input;
	filterData->out = output;
	filterData->filterFunction = filterFunctionRef;

	activeFilters[activeFiltersCount++] = filterData;

	pwm_ioSetConnectionFilter(in, out, runJavaFilter, filterData);
}

JNIEXPORT void JNICALL Java_me_mrletsplay_pwmixer_PWMixer_debugEnableLog(JNIEnv *env, jclass clz, jboolean log) {
	pwm_debugEnableLog(log);
}

JNIEXPORT jboolean JNICALL Java_me_mrletsplay_pwmixer_PWMixer_debugIsLogEnabled(JNIEnv *env, jclass clz) {
	return pwm_debugIsLogEnabled();
}
